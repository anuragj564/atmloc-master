package com.tgs.atm.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tgs.atm.R;

/**
 * Created by Anurag.
 */
public class Utils {

    // JSON Node names
    public static String jState="state";
    public static String jAddress="address";
    public static String jCity="city";
    public static String jZip="zip";
    public static String jName="name";
    public static String jLat="lat";
    public static String jLong="lng";
    public static String jBank="bank";
    public static String jAtm="atms";
    public static String jPhone="phone";
    public static String jDistance="distance";
    public static String jArrLoc="locations";
    // URL to get Locations JSON
    public static String urlLoc="https://m.chase.com/PSRWeb/location/list.action?lat=40.147864&lng=-82.990959";

    public static String strBrowserKey="AIzaSyCaYFGCpXQ8bov4q9zVmipuO6ECejc-G20";

//    public static double nLat=0.0;
  //  public static double nLang=0.0;

    // Custom Toast Alert dialog
    public static void showCustomAlert(Activity a,String msg)
    {

        Context context = a.getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = a.getLayoutInflater();

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast, null);

        TextView tvMsg=(TextView)toastRoot.findViewById(R.id.txtid);
        tvMsg.setText(msg);

        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

}
