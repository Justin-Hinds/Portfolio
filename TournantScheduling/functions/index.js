const functions = require('firebase-functions');
const admin = require('firebase-admin');
// const firebase = require("firebase");
// require("firebase/firestore");


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
exports.sendCompanyNotification = functions.firestore
.document('Restaurants/{restaurantID}/CompanyMessages/{messageID}')
.onCreate((snap,context) =>{
    const message = snap.data();
    const text = message.message;
    const tokens = message.companyList;

            const payload = {
                notification: {
                  title: message.senderName,
                  body: text ? (text.length <= 100 ? text : text.substring(0, 97) + '...') : '',
                  sound: "default",
                //   click_action: ".Activities.HomeScreenActivity"
                },
                data: {
                    'sender': message.sender,
                    senderName : message.senderName,
                }
              };
            return admin.messaging().sendToDevice(tokens, payload).then( function (response){
                console.log( 'success')
                return response;
            }).catch(error =>{
                console.log('Your princess is in another castle!' + error)
            });

})
exports.timeoffRequests = functions.firestore
.document('Restaurants/{restaurantID}/Users/{userID}/TimeOff/{timeOffID}')
.onCreate((snap,context) => {
    const timeoff = snap.data();
    console.log(timeoff.sender);
    console.log(timeoff.managers);
    const tokens = timeoff.managers;
    const payload = {
        notification: {
          title: 'Time off request',
          body:  timeoff.senderName + ' has requested time off',
          sound: "default",
        },
        data: {
            'sender': timeoff.sender,
            senderName : timeoff.senderName,
            // managers: timeoff.managers
        
        }

      };
      return admin.messaging().sendToDevice(tokens, payload).then( function (response){
        console.log( 'success')
        return response;
    }).catch(error =>{
        console.log('Your princess is in another castle!' + error)
    });

})
exports.sendNotification = functions.firestore
.document('Restaurants/{restaurantID}/Messages/{messageID}')
.onCreate((snap, context) =>{
        const message = snap.data();
        console.log(message.sender);
        console.log(message.deviceToken);
          const text = message.message;

            const payload = {
                notification: {
                  title: message.senderName,
                  body: text ? (text.length <= 100 ? text : text.substring(0, 97) + '...') : '',
                  tag: message.receiver,
                  sound: "default",
                //   click_action: ".Activities.HomeScreenActivity"
                },
                data: {
                    'sender': message.sender,
                    senderName : message.senderName,
                    receiver: message.receiver
                }
              };
            return admin.messaging().sendToDevice(message.deviceToken, payload).then( function (response){
                console.log( 'success')
                return response;
            }).catch(error =>{
                console.log('Your princess is in another castle!' + error)
            });

       
        //
        // // access a particular field as you would any JS property
        // const name = newValue.name;
        // admin.sendNotification
        // perform desired operations ...
    });
