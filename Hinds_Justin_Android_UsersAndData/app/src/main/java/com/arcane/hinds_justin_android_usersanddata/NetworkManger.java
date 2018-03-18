package com.arcane.hinds_justin_android_usersanddata;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class NetworkManger {

    public static boolean isConnected(Context _context) {

        ConnectivityManager mgr = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(mgr != null) {
            NetworkInfo info = mgr.getActiveNetworkInfo();

            if(info != null) {
                if(info.isConnected()) {
                    return true;
                }
            }
        }

        return false;
    }

}
