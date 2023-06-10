const express = require('express');
const router = express.Router();
const { registerUser, loginUser, logoutUser, predict } = require('./authController');

// Register user
router.post('/register', registerUser);

// Login user
router.post('/login', loginUser);

// Logout user
router.post('/logout', logoutUser);

// Predict model
// router.post('/predict', predict);

module.exports = router;