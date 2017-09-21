package com.arcane.thedish;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class ProfileEditFrag extends Fragment {


    public static ProfileEditFrag newInstance(){return new ProfileEditFrag();}
    private static final int PICK_IMAGE_REQUEST = 1;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference userRef = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private EditText userName;
    private EditText faveFoodEdit;
    private ImageView imageView;
    private EditText faveDrinkEdit;
    private EditText faveRestaurantEdit;
    private DishUser dishUser;
    private ProgressBar progressBar;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private Uri profileImageUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_edit_layout,container,false);
        userName =  root.findViewById(R.id.user_name);
        faveFoodEdit =  root.findViewById(R.id.favorite_food);
        faveDrinkEdit =  root.findViewById(R.id.favorite_drink);
        faveRestaurantEdit =  root.findViewById(R.id.favorite_restaurant);
        imageView =  root.findViewById(R.id.profile_icon);
        progressBar = root.findViewById(R.id.progressBar);
        imageView.setDrawingCacheEnabled(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dishUser = dataSnapshot.getValue(DishUser.class);
                assert dishUser != null;
                userName.setText(dishUser.getName());
                faveFoodEdit.setText(dishUser.getFavoriteFood());
                faveDrinkEdit.setText(dishUser.getFavoriteDrink());
                faveRestaurantEdit.setText(dishUser.getFavoriteRestaurant());
                if(dishUser.getProfilePicURL() != null){

                    Picasso.with(getContext())
                            .load(dishUser.getProfilePicURL())
                            .transform(new CropCircleTransformation())
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                ExifInterface exif = new ExifInterface(getRealPathFromURI(getContext(),uri));
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,1);
                Log.d("ORIENTATION", getRealPathFromURI(getContext(),uri) + "");
                switch (orientation){
                    case 1:
                        Picasso.with(getContext())
                                .load(uri)
                                .transform(new CropCircleTransformation())
                                .into(imageView);

                        break;
                    case 3:
                        Picasso.with(getContext())
                                .load(uri)
                                .rotate(180)
                                .transform(new CropCircleTransformation())
                                .into(imageView);

                        break;
                    case 6:
                        Picasso.with(getContext())
                                .load(uri)
                                .rotate(90)
                                .transform(new CropCircleTransformation())
                                .into(imageView);

                        break;
                    case 8:
                        Picasso.with(getContext())
                                .load(uri)
                                .rotate(270)
                                .transform(new CropCircleTransformation())
                                .into(imageView);

                        break;
                    default:
                        Picasso.with(getContext())
                                .load(uri)
                                .transform(new CropCircleTransformation())
                                .into(imageView);

                        break;

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectPhoto(){
        Intent intent = new Intent();
// set type to image so only images are displayed
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_PICK);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
       // startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public void save(){
        progressBar.setVisibility(View.VISIBLE);
       // Log.i("SAVE: ", "HIT");
        String name = userName.getText().toString();
        String faveRestaurant = faveRestaurantEdit.getText().toString();
        String faveFood = faveFoodEdit.getText().toString();
        String faveDrink = faveDrinkEdit.getText().toString();
        if(DataManager.stringValidate(name) != null){
        dishUser.setName(name);
        }else {
            dishUser.setName("N/A");
        }
        if(DataManager.stringValidate(faveRestaurant) != null){
            dishUser.setFavoriteDrink(faveRestaurant);
        }else {
            dishUser.setFavoriteDrink("N/A");
        }
        if(DataManager.stringValidate(faveDrink) != null){
            dishUser.setFavoriteRestaurant(faveDrink);
        }else {
            dishUser.setFavoriteRestaurant("N/A");
        }
        if(DataManager.stringValidate(faveFood) != null){
            dishUser.setFavoriteFood(faveFood);
        }else {
            dishUser.setFavoriteFood("N/A");
        }

        imageView.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap uploadBitmap = imageView.getDrawingCache();
        //compresses bitmap to png
        uploadBitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        //writes to a byte array
        byte[] imgData = baos.toByteArray();
        //path for image in firebase
        String path = "Profile_Pics/" + dishUser.getId() +"/" + UUID.randomUUID() + ".png";
        userRef.setValue(dishUser);
        StorageReference profileImageRef = storage.getReference(path);
        UploadTask uploadTask = profileImageRef.putBytes(imgData);
        uploadTask.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImageUri = taskSnapshot.getDownloadUrl();
                assert profileImageUri != null;
                dishUser.setProfilePicURL(profileImageUri.toString());
                userRef.setValue(dishUser);
                progressBar.setVisibility(View.INVISIBLE);
                getActivity().finish();
            }
        });




    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
