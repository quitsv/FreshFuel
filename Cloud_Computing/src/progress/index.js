const express = require('express');
const router = express.Router();
const { getProgress, addProgress, updateProgress } = require('./progressController');

// Get all progress or progress by date from one user 
router.get('/progress/user/:email', getProgress);

// Add progress user by date
router.post('/progress/user/:email', addProgress);

// Update progress user by date
router.put('/progress/user/:email', updateProgress);

module.exports = router;