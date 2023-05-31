const firebase = require('firebase/app');
const admin = require('firebase-admin');
require('../utils/firebase-config.js');
require('firebase/database');

const db = admin.database();
const ref = db.ref('/user');

// Register a new user
exports.registerUser = async (req, res) => {
  try {
    const { name, email, password, allergies } = req.body;

    // Create a new user in Firebase Authentication
    await firebase.auth().createUserWithEmailAndPassword(email, password);

    // Add to realtime database
    const newUserRef = ref.push();
    await newUserRef.set({
      name: name,
      email: email,
      allergies: allergies
    });

    res.status(201).json({ message: 'User registered successfully' });
  } catch (error) {
    res.status(500).json({ message: 'Failed to register user', error: error.message });
  }
};

// Login user
exports.loginUser = async (req, res) => {
  try {
    const { email, password } = req.body;

    // Sign in the user with Firebase Authentication
    const userCredential = await firebase.auth().signInWithEmailAndPassword(email, password);;

    // Generate an access token
    const accessToken = await userCredential.user.getIdToken();
    const emailUser = userCredential.user.providerData[0].email;
    var data = {}
    ref.orderByChild('email').equalTo(emailUser).once('value', (snapshot) => {
      data = Object.values(snapshot.val())[0];
      res.status(200).json({ message: 'Login successful', accessToken: accessToken, dataUser: data });
    });

  } catch (error) {
    res.status(401).json({ message: 'Login Error', error: error.message });
  }
};

// Logout user
exports.logoutUser = async (req, res) => {
  try {
    // Sign out the user from Firebase Authentication
    await firebase.auth().signOut();

    res.status(200).json({ message: 'User logged out successfully' });
  } catch (error) {
    res.status(500).json({ message: 'Failed to log out user', error: error.message });
  }
};