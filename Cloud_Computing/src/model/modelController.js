const { getFirestore } = require('firebase-admin/firestore');
require('../utils/firebase-config.js');

const db = getFirestore();
const recipesRef = db.collection('recipes');

// Get Detail Recipe
exports.getRecipe = async (req, res) => {
    try {
        const recipe = req.query.recipeName
        const responseObj = await getRecipeData(recipe, res);
        console.log(responseObj);
        if(responseObj == null){
            res.status(200).json({ message: 'No matching recipe', error: null, data: null });
            return;
        }
        res.status(200).json({ message: 'Get recipe success', error: null, data: responseObj});
    } catch (error) {
        res.status(500).json({ message: 'Failed to get recipe', error: error.message });
    }
};

// Get Search Recipe
exports.search = async (req, res) => {
    try {
        const recipeName = req.query.recipeName
        var listRecipes = [];
        const snapshot = await recipesRef.select('Recipe_Name').get();
        
        for (const recipe of snapshot.docs) {
            if(listRecipes.length < 10 ){
                if (recipe.data()["Recipe_Name"].toLowerCase().includes(recipeName.toLowerCase())){
                    listRecipes.push(recipe.data()["Recipe_Name"]);
                }
            }else{
                break;
            }
        }
        res.status(200).json({ message: 'Search recipe success', error: null, data: listRecipes});
    } catch (error) {
        res.status(500).json({ message: 'Failed to search recipe', error: error.message });
    }
};

// Get 5 random menu
exports.generateRandom = async (req, res) => {
    try {
        var listRecipes = [];
        var noAllergy = true;
        const allergies = req.query.allergies;
        if (allergies != "" && allergies != null){
            noAllergy = false;
            listAllergies = allergies.toLowerCase().split(', ');
        }
        const snapshot = await recipesRef.limit(300).get();

        // Get document with random index
        do{
            var containAllergy = false;
            var doubleRecipe = false;
            var randomIndex = Math.floor(Math.random() * (300 - 0 + 1) + 0)
            if (!noAllergy){
                for (var j = 0; j < listAllergies.length; j++){
                    if(snapshot.docs[randomIndex].data()["Recipe_Name"].toLowerCase().includes(listAllergies[j])){
                        containAllergy = true;
                        break;
                    }
                    if(snapshot.docs[randomIndex].data()[listAllergies[j]]==1){
                        containAllergy = true;
                        break;
                    }
                }
            }
            if(containAllergy){continue;}

            if (listRecipes.length == 0){
                listRecipes.push(snapshot.docs[randomIndex].data()["Recipe_Name"]);
            } else if (listRecipes.length < 5){
                for (var j = 0; j < listRecipes.length; j++){
                    if (listRecipes[j]==snapshot.docs[randomIndex].data()["Recipe_Name"]){
                        doubleRecipe = true;
                        break;
                    }
                }
                if(doubleRecipe){continue;}
                listRecipes.push(snapshot.docs[randomIndex].data()["Recipe_Name"]);
            }
        }while(listRecipes.length < 5);

        res.status(200).json({ message: 'Generate recipe success', error: null, data: listRecipes});
    } catch (error) {
        res.status(500).json({ message: 'Failed to generate random recipe', error: error.message });
    }
};

const getRecipeData = async (recipe, res) => {
    const snapshot = await recipesRef.where('Recipe_Name', '==', recipe).get();
    if (snapshot.empty) {
        return null;
    }
    var responseObj = {}
    snapshot.forEach(doc => {
        responseObj["information"] = {};
        responseObj["properties"] = [];
        responseObj["technique"] = [];
        responseObj["ingredients"] = [];
        Object.entries(doc.data()).forEach(([key, value]) => { 
            if (value != 0) {
                if (key == "Recipe_Name" || key == "rating" || key == "sodium" || key == "protein" || key == "calories" || key == "fat") {
                    responseObj["information"][key] = value;
                } else if (key == "22-minute meals" || key == "3-ingredient recipes" || key == "advance prep required" || key == "alcoholic" || key == "high fiber" || key == "low/no sugar"  || key == "low cholesterol" || key == "quick & easy") {
                    responseObj["properties"].push(key);
                } else if (key == "bake" || key == "boil" || key == "braise" || key == "broil" || key == "deep-fry" || key == "double boiler"  || key == "fry" || key == "grill" || key == "roast" || key == "saute" || key == "steam" || key == "stew" || key == "stir-fry") {
                    responseObj["technique"].push(key);
                } else{
                    responseObj["ingredients"].push(key);
                }
            }
        })
    });
    return responseObj;
}
exports.getRecipeData = getRecipeData
