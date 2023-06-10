const express = require('express');
const router = express.Router();
const { getRecipe, predict } = require('./modelController');

// Get Recipe
router.get('/getRecipe', getRecipe);

// Predict
router.post('/predict', predict);

module.exports = router;