const { getFirestore } = require('firebase-admin/firestore');
const admin = require('firebase-admin');
require('../utils/firebase-config.js');
// const { PredictionServiceClient } = require('@google-cloud/aiplatform');

const db = getFirestore();

// Get Detail Recipe
exports.getRecipe = async (req, res) => {
  try {
    const recipe = req.query.recipeName
    const recipesRef = db.collection('recipes');
    const snapshot = await recipesRef.where('Recipe_Name', '==', recipe).get();
    if (snapshot.empty) {
        res.status(200).json({ message: 'No matching recipe', error: error.message});
        return;
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
        const recipesRef = db.collection('recipes');
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

exports.predict = async (req, res) => {
    try {
        const { allergies, recipeName, rating } = req.body;

        // load model ML
        // const modelPath = './path/to/your/model';
        // const model = await tf.loadLayersModel(`file://${modelPath}`);

        // Perform prediction
        // const inputTensor = tf.tensor([inputData]);
        // const outputTensor = model.predict(inputTensor);
        // const prediction = outputTensor.arraySync()[0];

        var prediction = ["Sweet Potato and Sausage Soup","Black Bean and Vegetable Wraps","Baked Beans","Tomato and Mozzarella Bread-Bowl Souffles"]

        res.status(200).json({ message: 'Predict success', error: null, data: prediction });

    } catch (error) {
        res.status(500).json({ message: 'Failed to predict', error: error.message });
    }
  };
