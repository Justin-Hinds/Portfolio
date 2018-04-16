package com.arcane.tournantscheduling.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class NetworkUtils {

    public static boolean isConnected(Context _context) {

        ConnectivityManager mgr = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(mgr != null) {
            NetworkInfo info = mgr.getActiveNetworkInfo();

            if(info != null) {
                if(info.isConnected()) {
                    Log.d("INFO STATE", info.getState().name());
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hostAvailable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);
            return true;
        } catch (IOException e) {
            // Either we have a timeout or unreachable host or failed DNS lookup
            System.out.println(e);
            return false;
        }
    }
}
