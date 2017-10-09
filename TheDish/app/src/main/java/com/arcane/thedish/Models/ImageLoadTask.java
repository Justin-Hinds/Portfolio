package com.arcane.thedish.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by ChefZatoichi on 10/4/17.
 */

public class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        private static int MaxTextureSize = 2048; /* True for most devices. */

        public ImageView v;

        public ImageLoadTask(ImageView iv) {
            v = iv;
        }

        protected Bitmap doInBackground(String... params) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
//            opt.inPurgeable = true;
//            opt.inPreferQualityOverSpeed = false;
            opt.inSampleSize = 0;

            String url = params[0];

            Bitmap bitmap = null;
            if(isCancelled()) {
                return bitmap;
            }



            try {

            InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            Log.d("BITMAP", bitmap.toString());
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(v != null) {
                v.setImageBitmap(result);
            }
        }


}
