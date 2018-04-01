const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
// // Create and Deploy Your First Cloud Functions
// https:firebase.google.com/docs/functions/write-firebase-functions

exports.helloWorld = functions.https.onRequest((request, response) => {
 response.send("Hello from Firebase!");
});
exports.restaurantCreated = functions.firestore
    .document('Restaurants/{userId}')
    .onCreate(event => {
        // Get an object representing the document
        // e.g. {'name': 'Marie', 'age': 66}
        const newValue = event.data.data();

        // access a particular field as you would any JS property
        const name = newValue.name;
        console.log(name);

        // perform desired operations ...
    });
exports.sendNotification = functions.firestore
.document('Restaurants/{restaurantID}/Messages/{messageID}')
.onCreate(event =>{
        // Get an object representing the document
        // e.g. {'name': 'Marie', 'age': 66}
        const message = event.data.data();
        console.log(message.sender);
        // Notification details.
        //   const text = snapshot.val().text;
        //   const payload = {
        //     notification: {
        //       title: `${snapshot.val().name} posted ${text ? 'a message' : 'an image'}`,
        //       body: text ? (text.length <= 100 ? text : text.substring(0, 97) + '...') : '',
        //       //icon: snapshot.val().photoUrl || '/images/profile_placeholder.png',
        //       click_action: `https://${functions.config().firebase.authDomain}`
        //     }
        //   };
        //
        // // access a particular field as you would any JS property
        // const name = newValue.name;

        // perform desired operations ...
    });
