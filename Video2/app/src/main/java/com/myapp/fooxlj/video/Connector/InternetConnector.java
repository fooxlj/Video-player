package com.myapp.fooxlj.video.Connector;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fooxlj on 27/04/2016.
 */
public class InternetConnector {
////Problems.............!!!!!!!!!
    private static String LOG = "Internet Connection";
    Context context;
    public InternetConnector(Context con){
        this.context = con;
    }

    public boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void alertInternetConnection(Context context){
        new AlertDialog.Builder(context)
                .setTitle("No Internet")
                .setMessage("You are not connect to internet, please connect to internet")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
