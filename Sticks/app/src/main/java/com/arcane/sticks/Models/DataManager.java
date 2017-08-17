//Justin Hinds
//MDF3 - 1707
//DataManager.java
package com.arcane.sticks.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class DataManager {
    private  Context mContext = null;
    public DataManager(Context context){
        mContext = context;
    }
    private final String FILE_LOCATION = "com.arcane.sticks." + "FILE_LOCATION";
    public void saveImg(Bitmap bitmap, String location) {
        try {
            FileOutputStream fileStream = mContext.openFileOutput( location + ".png", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileStream);
            fileStream.close();
            Log.d("ICON SAVED", location);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SAVE IMG","FAILED");
        }

    }
    public void saveSmartphone(Post post){

        ArrayList<Post> arrayList;
        File file = mContext.getFileStreamPath(FILE_LOCATION);

        if(file.exists()){
            arrayList = readSavedData();
        }else {
            arrayList = new ArrayList<>();
        }
        try {
            FileOutputStream fileStream = mContext.openFileOutput(FILE_LOCATION, MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            arrayList.add(post);
            objectStream.writeObject(arrayList);
            objectStream.close();
            fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Bitmap readSavedBitmap(){

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
    public ArrayList readSavedData(){
        try {
            FileInputStream fileInputStream = mContext.openFileInput(FILE_LOCATION);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            ArrayList readPhones = (ArrayList) ois.readObject();
            ois.close();
            fileInputStream.close();
            //noinspection unchecked
            return readPhones;
        } catch (IOException | ClassNotFoundException e ) {
            e.printStackTrace();

            return null;
        }
    }
}
