const { getFirestore } = require('firebase-admin/firestore');
require('../utils/firebase-config.js');
const { getRecipeData } = require('../model/modelController');

const db = getFirestore();
const progressRef = db.collection('progress');

// Get All Progress or get progress by date
exports.getProgress = async (req, res) => {
    try {
        if (Object.keys(req.query).length === 0){
            const user = req.params['email']
            const snapshot = await progressRef.where('user', '==', user).get();
            if (snapshot.empty) {
                res.status(200).json({ message: 'No progress found', error: null, data: null});
                return;
            }
            var allProgress = []
            snapshot.forEach(doc => {
                const responseData = {
                    user: user,
                    date: doc.data()["date"],
                    progress: [
                        [doc.data()["progress"]["breakfast"]["recipe"], doc.data()["progress"]["breakfast"]["calories"], doc.data()["progress"]["breakfast"]["status"]],
                        [doc.data()["progress"]["lunch"]["recipe"], doc.data()["progress"]["lunch"]["calories"], doc.data()["progress"]["lunch"]["status"]],
                        [doc.data()["progress"]["dinner"]["recipe"], doc.data()["progress"]["dinner"]["calories"], doc.data()["progress"]["dinner"]["status"]]
                    ]
                }
                allProgress.push(responseData);
            });
            allProgress.sort(function(a,b){
                return new Date(b.date) - new Date(a.date);
            });
            res.status(200).json({ message: 'Get all progress success', error: null, data: allProgress});
        }else{
            const date = req.query.date
            const user = req.params['email']
            const snapshot = await progressRef.where('user', '==', user).where('date', '==', date).get();
            if (snapshot.empty) {
                res.status(200).json({ message: 'No matching progress', error: null, data: null});
                return;
            }
            snapshot.forEach(doc => {
                const responseData = {
                    user: user,
                    date: date,
                    progress: [
                        [doc.data()["progress"]["breakfast"]["recipe"], doc.data()["progress"]["breakfast"]["calories"], doc.data()["progress"]["breakfast"]["status"]],
                        [doc.data()["progress"]["lunch"]["recipe"], doc.data()["progress"]["lunch"]["calories"], doc.data()["progress"]["lunch"]["status"]],
                        [doc.data()["progress"]["dinner"]["recipe"], doc.data()["progress"]["dinner"]["calories"], doc.data()["progress"]["dinner"]["status"]]
                    ]
                }
                res.status(200).json({ message: 'Get progress success', error: null, data: responseData});
            });
        }
    } catch (error) {
        res.status(500).json({ message: 'Failed to get progress', error: error.message });
    }
};

// Add progress user by date
exports.addProgress = async (req, res) => {
    try {
        const { date, breakfastMenu } = req.body;
        const user = req.params['email']
        const snapshot = await progressRef.where('user', '==', user).where('date', '==', date).get();
        if (snapshot.empty) {
            var calories = 0
            const dataRecipe = await getRecipeData(breakfastMenu, res);
            if(dataRecipe == null){
                res.status(200).json({ message: 'No matching recipe', error: null, data: null });
                return;
            }
            if (dataRecipe["information"]["calories"] != null){
                calories = dataRecipe["information"]["calories"]
            }
            const progressData = {
                user: user,
                date: date,
                progress: {
                    breakfast: {
                        calories: calories,
                        recipe: breakfastMenu,
                        status: 0
                    },
                    lunch: {
                        calories: 0,
                        recipe: null,
                        status: 0
                    },
                    dinner: {
                        calories: 0,
                        recipe: null,
                        status: 0
                    }
                }
            }
            const responseData = {
                user: user,
                date: date,
                progress: [
                    [breakfastMenu, calories, 0],
                    [null, 0, 0],
                    [null, 0, 0]
                ]
            }
            await progressRef.add(progressData);
            res.status(200).json({ message: 'Add progress success', error: null, data: responseData });
            return;
        }
        snapshot.forEach(doc => {
            const responseData = {
                user: user,
                date: date,
                progress: [
                    [doc.data()["progress"]["breakfast"]["recipe"], doc.data()["progress"]["breakfast"]["calories"], doc.data()["progress"]["breakfast"]["status"]],
                    [doc.data()["progress"]["lunch"]["recipe"], doc.data()["progress"]["lunch"]["calories"], doc.data()["progress"]["lunch"]["status"]],
                    [doc.data()["progress"]["dinner"]["recipe"], doc.data()["progress"]["dinner"]["calories"], doc.data()["progress"]["dinner"]["status"]]
                ]
            }
            res.status(200).json({ message: 'Progress already exists', error: null, data: responseData });
        });
    } catch (error) {
        res.status(500).json({ message: 'Failed to add progress', error: error.message });
    }
  };

// Update progress user by date
exports.updateProgress = async (req, res) => {
    try {
        const { date, lunchMenu, dinnerMenu } = req.body;
        const user = req.params['email']
        var calories = 0
        if (lunchMenu != null){
            const lunchMenuRecipe = await getRecipeData(lunchMenu, res);

            // check recipe
            if(lunchMenuRecipe == null){
                res.status(200).json({ message: 'No matching recipe', error: null, data: null });
                return;
            }

            if (lunchMenuRecipe["information"]["calories"] != null){
                calories = lunchMenuRecipe["information"]["calories"]
            }

            // check progress exists or not
            const snapshot = await progressRef.where('user', '==', user).where('date', '==', date).get();
            if (snapshot.empty) {
                res.status(200).json({ message: 'No matching progress', error: null, data: null});
                return;
            }
            // get id of the document
            var id = ""
            var oldData;
            snapshot.forEach(doc => {
                id = doc.id
                oldData = doc.data()
            });

            const dataLunch = {
                progress: {
                    breakfast: {
                        calories: oldData["progress"]["breakfast"]["calories"],
                        recipe: oldData["progress"]["breakfast"]["recipe"],
                        status: 1
                    },
                    lunch: {
                        calories: calories,
                        recipe: lunchMenuRecipe["information"]["Recipe_Name"],
                        status: 0
                    },
                    dinner: {
                        calories: 0,
                        recipe: null,
                        status: 0
                    }
                }
            }
            const responseData = {
                user: user,
                date: date,
                progress: [
                    [oldData["progress"]["breakfast"]["recipe"], oldData["progress"]["breakfast"]["calories"], 1],
                    [lunchMenuRecipe["information"]["Recipe_Name"], calories, 0],
                    [null, 0, 0]
                ]
            }
            await progressRef.doc(id).update(dataLunch);
            res.status(500).json({ message: 'Update progress success', error: null, data: responseData });
        }else if (dinnerMenu != null){
            const dinnerMenuRecipe = await getRecipeData(dinnerMenu, res);
            // check recipe
            if(dinnerMenuRecipe == null){
                res.status(200).json({ message: 'No matching recipe', error: null, data: null });
                return;
            }

            if (dinnerMenuRecipe["information"]["calories"] != null){
                calories = dinnerMenuRecipe["information"]["calories"]
            }

            // check progress exists or not
            const snapshot = await progressRef.where('user', '==', user).where('date', '==', date).get();
            if (snapshot.empty) {
                res.status(200).json({ message: 'No matching progress', error: null, data: null});
                return;
            }
            // get id of the document
            var id = ""
            var oldData;
            snapshot.forEach(doc => {
                id = doc.id
                oldData = doc.data()
            });

            const dataDinner = {
                progress: {
                    breakfast: {
                        calories: oldData["progress"]["breakfast"]["calories"],
                        recipe: oldData["progress"]["breakfast"]["recipe"],
                        status: 1
                    },
                    lunch: {
                        calories: oldData["progress"]["lunch"]["calories"],
                        recipe: oldData["progress"]["lunch"]["recipe"],
                        status: 1
                    },
                    dinner: {
                        calories: calories,
                        recipe: dinnerMenuRecipe["information"]["Recipe_Name"],
                        status: 0
                    }
                }
            }
            const responseData = {
                user: user,
                date: date,
                progress: [
                    [oldData["progress"]["breakfast"]["recipe"], oldData["progress"]["breakfast"]["calories"], 1],
                    [oldData["progress"]["lunch"]["recipe"], oldData["progress"]["lunch"]["calories"], 1],
                    [dinnerMenuRecipe["information"]["Recipe_Name"], calories, 0]
                ]
            }
            await progressRef.doc(id).update(dataDinner);
            res.status(500).json({ message: 'Update progress success', error: null, data: responseData });
        } else {
            // check progress exists or not
            const snapshot = await progressRef.where('user', '==', user).where('date', '==', date).get();
            if (snapshot.empty) {
                res.status(200).json({ message: 'No matching progress', error: null, data: null});
                return;
            }
            // get id of the document
            var id = ""
            var oldData;
            snapshot.forEach(doc => {
                id = doc.id
                oldData = doc.data()
            });

            const dataDinner = {
                progress: {
                    breakfast: {
                        calories: oldData["progress"]["breakfast"]["calories"],
                        recipe: oldData["progress"]["breakfast"]["recipe"],
                        status: 1
                    },
                    lunch: {
                        calories: oldData["progress"]["lunch"]["calories"],
                        recipe: oldData["progress"]["lunch"]["recipe"],
                        status: 1
                    },
                    dinner: {
                        calories: oldData["progress"]["dinner"]["calories"],
                        recipe: oldData["progress"]["dinner"]["recipe"],
                        status: 1
                    }
                }
            }
            const responseData = {
                user: user,
                date: date,
                progress: [
                    [oldData["progress"]["breakfast"]["recipe"], oldData["progress"]["breakfast"]["calories"], 1],
                    [oldData["progress"]["lunch"]["recipe"], oldData["progress"]["lunch"]["calories"], 1],
                    [oldData["progress"]["dinner"]["recipe"], oldData["progress"]["dinner"]["calories"], 1]
                ]
            }
            await progressRef.doc(id).update(dataDinner);
            res.status(500).json({ message: 'Update progress success', error: null, data: responseData });
        }
    } catch (error) {
        res.status(500).json({ message: 'Failed to update progress', error: error.message });
    }
};

