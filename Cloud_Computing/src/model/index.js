const express = require('express');
const router = express.Router();
const { getRecipe, predict, search } = require('./modelController');

// Get Recipe
router.get('/getRecipe', getRecipe);

// Search
router.get('/search', search);

// Predict
router.post('/predict', predict);

module.exports = router;