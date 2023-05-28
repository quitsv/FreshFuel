const firebase = require('firebase/app');
require('firebase/auth');
require('firebase/firestore');
const admin = require('firebase-admin');
const serviceAccount = require('../../freshFuelServiceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://fresh-fuel-demo-default-rtdb.asia-southeast1.firebasedatabase.app/',
});

// Add Firebase SDK Snippet
const firebaseConfig = {
    apiKey: "AIzaSyBr6Q_vfI_-lfsv3__hX3hsTgVsT31_lZw",
    authDomain: "fresh-fuel-demo.firebaseapp.com",
    projectId: "fresh-fuel-demo",
    storageBucket: "fresh-fuel-demo.appspot.com",
    messagingSenderId: "124073971479",
    appId: "1:124073971479:web:7b5bc7dba47733bba47152",
    measurementId: "G-XW2DCQ5V1H"
  };

firebase.initializeApp(firebaseConfig);