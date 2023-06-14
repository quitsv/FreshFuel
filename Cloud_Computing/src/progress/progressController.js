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
                allProgress.push(doc.data());
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
                res.status(200).json({ message: 'Get progress success', error: null, data: doc.data()});
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
                progress: [
                    {
                        recipe: breakfastMenu,
                        calories: calories,
                        rating: null,
                        status: 0
                    }
                ]
            }
            await progressRef.add(progressData);
            res.status(200).json({ message: 'Add progress success', error: null, data: progressData });
            return;
        }
        snapshot.forEach(doc => {
            res.status(200).json({ message: 'Progress already exists', error: null, data: doc.data() });
        });
    } catch (error) {
        res.status(500).json({ message: 'Failed to add progress', error: error.message });
    }
  };

// Update progress user by date
exports.updateProgress = async (req, res) => {
    try {
        const { date, lunchMenu, dinnerMenu, rating } = req.body;
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
                progress: [
                    {
                        recipe: oldData["progress"][0]["recipe"],
                        calories: oldData["progress"][0]["calories"],
                        rating: rating,
                        status: 1
                    },
                    {
                        recipe: lunchMenuRecipe["information"]["Recipe_Name"],
                        calories: calories,
                        rating: null,
                        status: 0
                    }
                ]
            }
            await progressRef.doc(id).update(dataLunch);
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
                progress: [
                    {
                        recipe: oldData["progress"][0]["recipe"],
                        calories: oldData["progress"][0]["calories"],
                        rating: oldData["progress"][0]["rating"],
                        status: 1
                    },
                    {
                        recipe: oldData["progress"][1]["recipe"],
                        calories: oldData["progress"][1]["calories"],
                        rating: rating,
                        status: 1
                    },
                    {
                        recipe: dinnerMenuRecipe["information"]["Recipe_Name"],
                        calories: calories,
                        rating: null,
                        status: 0
                    }
                ]
            }
            await progressRef.doc(id).update(dataDinner);
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
                progress: [
                    {
                        recipe: oldData["progress"][0]["recipe"],
                        calories: oldData["progress"][0]["calories"],
                        rating: oldData["progress"][0]["rating"],
                        status: 1
                    },
                    {
                        recipe: oldData["progress"][1]["recipe"],
                        calories: oldData["progress"][1]["calories"],
                        rating: oldData["progress"][1]["rating"],
                        status: 1
                    },
                    {
                        recipe: oldData["progress"][2]["recipe"],
                        calories: oldData["progress"][2]["calories"],
                        rating: rating,
                        status: 1
                    }
                ]
            }
            await progressRef.doc(id).update(dataDinner);
        }
        
        const dataUpdated = await progressRef.where('user', '==', user).where('date', '==', date).get();
        dataUpdated.forEach(doc => {
            res.status(500).json({ message: 'Update progress success', error: null, data: doc.data() });
        });
    } catch (error) {
        res.status(500).json({ message: 'Failed to update progress', error: error.message });
    }
};

