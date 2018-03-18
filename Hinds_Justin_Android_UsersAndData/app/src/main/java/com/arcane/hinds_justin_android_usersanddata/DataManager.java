//Justin Hinds
//MDF3 - 1707
//DataManager.java
package com.arcane.hinds_justin_android_usersanddata;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


class DataManager {
    private Context mContext = null;
    static final String FILE_LOCATION = "USER_FILE";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = database.getReference("Users");
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public DataManager(Context context) {
        mContext = context;
    }

    public static String stringValidate(String s) {
        String regex = "[0-9]+";
        if (s.length() < 1) {
            return null;
        }
        if (s.trim().length() == 0) {
            return null;
        }
        if(s.matches(regex)){
            return null;
        }
        return s;
    }
    public static String passwordValidate(String s, Context context){
        Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[A-Z])[a-z  A-Z 0-9]{6,} ");
        Matcher matcher = passwordPattern.matcher(s);
        if (s.length()< 6){
            Toast.makeText(context, "Password must be at least 6 Characters", Toast.LENGTH_SHORT).show();
            return  null;
        }
        if(matcher.matches()){
            return s;
        }else {
            Toast.makeText(context, "Password must contain  6 characters, upper and lower case and a number ", Toast.LENGTH_LONG).show();

            return null;
        }
    }
    public static String emailValidate(String s) {
        if (s.length() < 1) {
            return null;
        }
        if (s.trim().length() == 0) {
            return null;
        }
        if(!s.contains("@")){
            return null;
        }
        if(s.contains(".com") || s.contains(".edu")){
            return s;
        }else {
            return null;
        }

    }

    public void overwrite(ArrayList<User> dataList){
        try {
            FileOutputStream fileStream = mContext.openFileOutput(FILE_LOCATION, MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(dataList);
            objectStream.close();
            fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveList(User data){
        ArrayList<User> arrayList;
        File file = mContext.getFileStreamPath(FILE_LOCATION);

        if(file.exists()){
            arrayList = readSavedData();
        }else {
            arrayList = new ArrayList<>();
        }
        try {
            FileOutputStream fileStream = mContext.openFileOutput(FILE_LOCATION, MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            arrayList.add(data);
            objectStream.writeObject(arrayList);
            objectStream.close();
            fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList readSavedData() {
        try {
            FileInputStream fileInputStream = mContext.openFileInput(FILE_LOCATION);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            ArrayList dataList = (ArrayList) ois.readObject();
            ois.close();
            fileInputStream.close();
            //noinspection unchecked
            return dataList;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            return null;
        }
    }

//Function for adding new user to database
private void addUser(FirebaseUser user, View view) {
        final String id = user.getUid();
        final User user1 = new User();
        EditText userName = view.findViewById(R.id.editText_name);
        EditText userAge = view.findViewById(R.id.editText_age);
    if (userName != null && userAge != null){
        String userNameText = stringValidate(userName.getText().toString());
        Integer age = Integer.parseInt(userAge.getText().toString());
        if (user.getDisplayName() != null) {
            user1.setName(user.getDisplayName());
        }else if (userNameText != null ){
            user1.setName(userNameText);
        }else {
            user1.setName("John Doe");
        }
        if(age > 0){
            user1.setAge(age);
        }
    }else {
        if (user.getDisplayName() != null) {
            user1.setName(user.getDisplayName());
        }else {
            user1.setName("John Doe");
        }
    }


     //   user1.setId(id);
    ref.child(id).setValue(user1);
        Log.d("AFTER ", "CHECK ");
    mContext.startActivity(new Intent(mContext,MainActivity.class));
    }
    //Checks user for login/creation submission
    public void userCheck(final View view) {
        DatabaseReference userRef = database.getReference("Users");
        final User[] currentUser = new User[1];
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    Log.d("SNAP: ", child.getValue().toString());
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (userID.equals(user != null ? user.getId() : null)) {
                        currentUser[0] = user;
                        if (currentUser[0] != null && currentUser[0].getId().equals(userID)) {
                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                            return;
                        }
                    }
                }
                if( view != null){
                addUser(mAuth.getCurrentUser(), view);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
