const express = require('express');
const router = express.Router();
const { getRecipe, search, generateRandom } = require('./modelController');

// Get Recipe
router.get('/getRecipe', getRecipe);

// Search
router.get('/search', search);

// Generate 5 random menu
router.get('/generateRandom', generateRandom);

module.exports = router;