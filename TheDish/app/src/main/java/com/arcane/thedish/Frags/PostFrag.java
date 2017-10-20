package com.arcane.thedish.Frags;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcane.thedish.Models.DataManager;
import com.arcane.thedish.Models.Post;
import com.arcane.thedish.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class PostFrag extends Fragment {
    public static final String POST_TAG = "POST_TAG";
    public static final String TAG = "PostFrag.Tag";
    private static final int PICK_IMAGE_REQUEST = 1;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Posts");
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference childRef;
    private String user;
    private EditText postMessage;
    private ImageView imageView;
    private ProgressBar progressBar;
    private StorageReference postImageRef;
    private Uri postImageUri;
    private Uri imageUri;
    public static PostFrag newInstance() {
        return new PostFrag();
    }

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
        View root = inflater.inflate(R.layout.post_frag_layout, container, false);
        progressBar = root.findViewById(R.id.progressBarPost);
        postMessage = root.findViewById(R.id.post_text);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        childRef = myRef.push();
        imageView = root.findViewById(R.id.preview);
        imageView.setDrawingCacheEnabled(true);
        ImageButton sendButton = root.findViewById(R.id.send_button);
        ImageButton cameraButton = root.findViewById(R.id.camera_button);
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
            imageUri = uri;


            Picasso.with(getContext())
                    .load(uri)
                    .into(imageView);

        }
    }

    private void selectPhoto() {
        Intent intent = new Intent();
// set type to image so only images are displayed
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void sendPost() {
        progressBar.setVisibility(View.VISIBLE);
        final Post post = new Post();
        post.setUser(user);
        post.setTime(System.currentTimeMillis());
        post.setDowns(new HashMap<String, Boolean>());
        post.setUps(new HashMap<String, Boolean>());
        post.setHyperLink();
        post.setImgURL(null);

        final String postID = childRef.getKey();
        post.setId(postID);


        if (imageView.getDrawable() != null) {
            post.setPostText(postMessage.getText().toString());
            //imageView.buildDrawingCache();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            //Get direct bitmap
//            Bitmap uploadBitmap = mBitmap;
////            Bitmap uploadBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//            //compresses bitmap to png
//            uploadBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            //writes to a byte array
//            byte[] imgData = baos.toByteArray();
            //path for image in firebase
            String path = "Post_Images/" + UUID.randomUUID() + ".png";
            postImageRef = storage.getReference(path);
            UploadTask uploadTask = postImageRef.putFile(imageUri);

//            UploadTask uploadTask = postImageRef.putBytes(imgData);
            uploadTask.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    postImageUri = taskSnapshot.getDownloadUrl();
                    assert postImageUri != null;
                    post.setImgURL(postImageUri.toString());
                    //mapping values from post object
                    Map<String, Object> postValues = post.toMap();
                    //setting map for updateValues
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/Posts/" + postID, postValues);
                    childUpdates.put("/User Posts/" + user + "/" + postID, postValues);
                    database.getReference().updateChildren(childUpdates);
                    progressBar.setVisibility(View.INVISIBLE);
                    getActivity().finish();
                }
            });
        } else if (DataManager.stringValidate(postMessage.getText().toString()) != null) {
            post.setPostText(postMessage.getText().toString());
            //mapping values from post object
            Map<String, Object> postValues = post.toMap();
            //setting map for updateValues
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + postID, postValues);
            childUpdates.put("/User Posts/" + user + "/" + postID, postValues);
            database.getReference().updateChildren(childUpdates);
            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Enter some text or a dank meme", Toast.LENGTH_LONG).show();

        }


    }


}
