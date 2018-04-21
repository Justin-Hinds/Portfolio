const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
let firebaseConfig = JSON.parse(process.env.FIREBASE_CONFIG);

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
.onCreate((snap, context) =>{
        // Get an object representing the document
        // e.g. {'name': 'Marie', 'age': 66}
        const message = snap.data();
        console.log(message.sender);
        // Notification details.
          const text = message.message;
          const getDeviceTokensPromise = admin.firestore.document('fcmTokens/deviceTokens/'+message.receiver);
        console.log(getDeviceTokensPromise.newValue);
          // The snapshot to the user's tokens.
        // let tokensSnapshot;

        // The array containing all the user's tokens.
      //  let tokens;
  
            // tokensSnapshot = results;   
            // if (!tokensSnapshot.hasChildren()) {
            //     return console.log('There are no notification tokens to send to.');
            //   }         
            // tokens = Object.keys(tokensSnapshot.val());
           // console.log(tokensSnapshot);
            

            const payload = {
                notification: {
                  title: `${message.sender} posted ${text ? 'a message' : 'an image'}`,
                  body: text ? (text.length <= 100 ? text : text.substring(0, 97) + '...') : '',
                  //icon: snapshot.val().photoUrl || '/images/profile_placeholder.png',
                  click_action: `https://${functions.config().firebase.authDomain}`
                }
              };

            return admin.messaging().sendToDevice(getDeviceTokensPromise, payload).catch(error =>{
                console.log("Your princess is in another castle!")
            });

       
        //
        // // access a particular field as you would any JS property
        // const name = newValue.name;
        // admin.sendNotification
        // perform desired operations ...
    });
