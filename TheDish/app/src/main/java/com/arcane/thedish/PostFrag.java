package com.arcane.thedish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class PostFrag extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    public static PostFrag newInstance(){return new PostFrag();}
    public static final String POST_TAG = "POST_TAG";
    public static final String TAG = "PostFrag.Tag";
    private DatabaseReference childRef;
    private String user;
    private EditText postMessage;
    private ImageView imageView;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Posts");
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference postImageRef;
    private Uri postImageUri;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (postImageRef != null) {
            outState.putString("reference", postImageRef.toString());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // If there was an upload in progress, get its reference and create a new StorageReference
//        final String stringRef = savedInstanceState.getString("reference");
//        if (stringRef == null) {
//            return;
//        }
//        postImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);
//
//        // Find all UploadTasks under this StorageReference (in this example, there should be one)
//        List<UploadTask> tasks = postImageRef.getActiveUploadTasks();
//        if (tasks.size() > 0) {
//            // Get the task monitoring the upload
//            UploadTask task = tasks.get(0);
//
//            // Add new listeners to the task using an Activity scope
//            task.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot state) {
//                    postImageUri = state.getDownloadUrl();
//
//                    //handleSuccess(state); //call a user defined function to handle the event.
//                }
//            });
//        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.post_frag_layout, container,false);
         postMessage = (EditText) root.findViewById(R.id.post_text);
         user = FirebaseAuth.getInstance().getCurrentUser().getUid();
         childRef = myRef.push();
        imageView = (ImageView) root.findViewById(R.id.preview);
        imageView.setDrawingCacheEnabled(true);
        ImageButton sendButton = (ImageButton) root.findViewById(R.id.send_button);
        ImageButton cameraButton = (ImageButton) root.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
         sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendPost();
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

                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void selectPhoto(){
        Intent intent = new Intent();
// set type to image so only images are displayed
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void sendPost(){
       // DatabaseReference userPostRef = database.getReference("User Posts").child(user);
        final Post post = new Post();
        post.setUser(user);
        post.setTime(System.currentTimeMillis());
        //post.setComments(new HashMap<String, Boolean>());
        post.setDowns(new HashMap<String, Boolean>());
        post.setUps(new HashMap<String, Boolean>());
        post.setHyperLink(null);
        post.setImgURL(null);

        final String postID = childRef.getKey();
        post.setId(postID);


        if(imageView.getDrawable() != null){
            post.setPostText(postMessage.getText().toString());
            imageView.buildDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap uploadBitmap = imageView.getDrawingCache();
            //compresses bitmap to png
            uploadBitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            //writes to a byte array
            byte[] imgData = baos.toByteArray();
            //path for image in firebase
            String path = "Post_Images/" + UUID.randomUUID() + ".png";
            postImageRef = storage.getReference(path);
            UploadTask uploadTask = postImageRef.putBytes(imgData);
            uploadTask.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    postImageUri = taskSnapshot.getDownloadUrl();
                    assert postImageUri != null;
                    post.setImgURL(postImageUri.toString());
                    //mapping values from post object
                    Map<String,Object> postValues = post.toMap();
                    //setting map for updateValues
                    Map<String,Object> childUpdates = new HashMap<>();
                    childUpdates.put("/Posts/" + postID, postValues);
                    childUpdates.put("/User Posts/" + user + "/" + postID, postValues);
                    database.getReference().updateChildren(childUpdates);
                    getActivity().finish();
                }
            });
        }else if (DataManager.stringValidate(postMessage.getText().toString())!= null) {
            post.setPostText(postMessage.getText().toString());
            //mapping values from post object
            Map<String,Object> postValues = post.toMap();
            //setting map for updateValues
            Map<String,Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + postID, postValues);
            childUpdates.put("/User Posts/" + user + "/" + postID, postValues);
            database.getReference().updateChildren(childUpdates);
            getActivity().finish();
        }else {
            Toast.makeText(getContext(),  "Enter some text or a dank meme", Toast.LENGTH_LONG).show();

        }


    }






}
