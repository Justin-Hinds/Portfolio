//Justin Hinds
//MDF3 - 1707
//DataManager.java
package com.arcane.thedish.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.arcane.thedish.Activities.MainActivity;
import com.arcane.thedish.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;


public class DataManager {
    private final String FILE_LOCATION = "com.arcane.thedish." + "FILE_LOCATION";
    private Context mContext = null;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = database.getReference("Users");
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public DataManager(Context context) {
        mContext = context;
    }

    public static String stringValidate(String s) {
        if (s.length() < 1) {
            return null;
        }
        if (s.trim().length() == 0) {
            return null;
        }

        return s;
    }

    public void saveImg(Bitmap bitmap, String location) {
        try {
            FileOutputStream fileStream = mContext.openFileOutput(location + ".png", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileStream);
            fileStream.close();
            Log.d("ICON SAVED", location);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SAVE IMG", "FAILED");
        }

    }


    public Bitmap readSavedBitmap() {

        Bitmap bitmap = null;
        try {
            File filePath = mContext.getFileStreamPath(FILE_LOCATION + ".png");
            FileInputStream fileInputStream = new FileInputStream(filePath);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            Log.d("BITMAP", bitmap.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("READ FAILED", FILE_LOCATION + ".png");
        }
        return bitmap;
    }

    public ArrayList readSavedData() {
        try {
            FileInputStream fileInputStream = mContext.openFileInput(FILE_LOCATION);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            ArrayList readPhones = (ArrayList) ois.readObject();
            ois.close();
            fileInputStream.close();
            //noinspection unchecked
            return readPhones;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            return null;
        }
    }

//Function for adding new user to database
    public void addPlayer(FirebaseUser user, ImageView imageView, View view) {
        final String id = user.getUid();
        final DishUser dishUser = new DishUser();
        EditText userName = view.findViewById(R.id.user_name);
        EditText faveFoodEdit = view.findViewById(R.id.favorite_food);
        EditText faveDrinkEdit = view.findViewById(R.id.favorite_drink);
        EditText faveRestaurantEdit = view.findViewById(R.id.favorite_restaurant);
        String userNameText = stringValidate(userName.getText().toString());
        String faveFoodText = stringValidate(faveFoodEdit.getText().toString());
        String faveDrinkText = stringValidate(faveDrinkEdit.getText().toString());
        String faveRestaurantText = stringValidate(faveRestaurantEdit.getText().toString());

        if (user.getDisplayName() != null) {
            dishUser.setName(user.getDisplayName());
        }else if (userNameText != null ){
            dishUser.setName(userNameText);
        }else {
            dishUser.setName("Guest");
        }

        dishUser.setId(id);

        if(faveFoodText != null){
            dishUser.setFavoriteFood(faveFoodText);
        }else{
        dishUser.setFavoriteFood("N/A");
        }

        if(faveDrinkText != null){
            dishUser.setFavoriteDrink(faveDrinkText);
        }else{
        dishUser.setFavoriteDrink("N/A");
        }

        if(faveRestaurantText != null){
            dishUser.setFavoriteRestaurant(faveRestaurantText);
        }else {
        dishUser.setFavoriteRestaurant("N/A");
        }

        imageView.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap uploadBitmap = imageView.getDrawingCache();
//        compresses bitmap to png
        uploadBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        writes to a byte array
        byte[] imgData = baos.toByteArray();
//        path for image in firebase
        String path = "Profile_Pics/" + dishUser.getId() + "/" + UUID.randomUUID() + ".png";
        StorageReference profileImageRef = storage.getReference(path);
        UploadTask uploadTask = profileImageRef.putBytes(imgData);
        uploadTask.addOnSuccessListener((Activity) mContext, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri profileImageUri;
                profileImageUri = taskSnapshot.getDownloadUrl();
                assert profileImageUri != null;
                dishUser.setProfilePicURL(profileImageUri.toString());
                ref.child(id).setValue(dishUser);
                mContext.startActivity(new Intent(mContext, MainActivity.class));
                Log.d("Check", dishUser.getProfilePicURL());

            }
        });
        Log.d("AFTER ", "CHECK ");

    }
    //Checks user for login/creation submission
    public DishUser userCheck(final ImageView imageView, final View view) {
        DatabaseReference userRef = database.getReference("Users");
        final DishUser[] currentDishUser = new DishUser[1];
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    DishUser dishUser = child.getValue(DishUser.class);
                    Log.d("SNAP: ", child.getValue().toString());
                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (user.equals(dishUser.getId())) {
                        currentDishUser[0] = dishUser;
                        if (currentDishUser[0].getId().equals(user)) {
                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                            return;
                        }
                    }
                }
                addPlayer(mAuth.getCurrentUser(),imageView, view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return currentDishUser[0];
    }
//Function to sort based off of up to down ratio then chronologically
 public static ArrayList<Post> sortingPosts(ArrayList<Post> sortingList){
     ArrayList<Post> upsAndDowns = new ArrayList<>();
     ArrayList<Post> nothing = new ArrayList<>();

     for(Post post : sortingList){
         if(post.getUpCount() > 0 || post.getDownCount() > 0 ){
             upsAndDowns.add(post);
         }else if(post.getUpCount() == 0 && post.getDownCount() == 0){
             nothing.add(post);
         }
     }
     Collections.sort(upsAndDowns);
     Collections.sort(nothing, new timeCompare());

     for(Post post : nothing){
         upsAndDowns.add(post);
     }



return upsAndDowns;
 }


// Comparator for chronological sorting
    static class timeCompare implements Comparator<Post>{

        @Override
        public int compare(Post post, Post t1) {
            if(post.getTime() > t1.getTime()){
                return 1;
            }else if(post.getTime() < t1.getTime()) {
                return -1;
            }
            return 0;
        }
    }
}
