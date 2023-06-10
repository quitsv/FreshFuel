const { getFirestore } = require('firebase-admin/firestore');
const admin = require('firebase-admin');
require('../utils/firebase-config.js');
// const { PredictionServiceClient } = require('@google-cloud/aiplatform');

const db = getFirestore();

// Register a new user
exports.getRecipe = async (req, res) => {
  try {
    const recipe = req.query.recipeName
    const recipesRef = db.collection('recipes');
    const snapshot = await recipesRef.where('Recipe_Name', '==', recipe).get();
    if (snapshot.empty) {
        res.status(200).json({ message: 'No matching recipe'});
        return;
    }
    var responseObj = {}
    snapshot.forEach(doc => {
        responseObj["properties"] = {};
        responseObj["technique"] = {};
        responseObj["composition"] = {};
        Object.entries(doc.data()).forEach(([key, value]) => { 
            if (key == "22-minute meals" || key == "3-ingredient recipes" || key == "advance prep required" || key == "alcoholic" || key == "high fiber" || key == "low/no sugar"  || key == "low cholesterol" || key == "quick & easy") {
                responseObj["properties"][key] = value;
            } else if (key == "bake" || key == "boil" || key == "braise" || key == "broil" || key == "deep-fry" || key == "double boiler"  || key == "fry" || key == "grill" || key == "roast" || key == "saute" || key == "steam" || key == "stew" || key == "stir-fry") {
                responseObj["technique"][key] = value;
            } else{
                if (value != 0) {
                    responseObj["composition"][key] = value;
                }
            }
        })
    });
    res.status(200).json({ message: 'Get recipe success', error: null, data: responseObj});
  } catch (error) {
        res.status(500).json({ message: 'Failed to get recipe', error: error.message });
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
