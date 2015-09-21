package com.tgs.atm.adapter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tgs.atm.R;
import com.tgs.atm.bean.LocationsBean;

import java.util.ArrayList;

/**
 * Created by Anurag.
 */
public class LocationsAdapter extends BaseAdapter {

    private Activity activity;
    LocationsBean placesBean;
    private ArrayList<LocationsBean> data;
    private static LayoutInflater inflater = null;
    public LocationsAdapter(Activity a,
                         ArrayList<LocationsBean> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

// create the inflater xml file for list view items loading
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;

        try {
            placesBean = (LocationsBean) getItem(position);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (convertView == null)
            rootView = inflater.inflate(R.layout.places_inflater, null);
            

        TextView tv_Name=(TextView)rootView.findViewById(R.id.tvName);

        TextView tv_Loc=(TextView)rootView.findViewById(R.id.tvLocation);


        tv_Name.setText(placesBean.getmName());

        tv_Loc.setText(placesBean.getmAddress());

        if (position % 2 == 1) {
            rootView.setBackgroundResource(R.color.list_bg_2);
        } else {
            rootView.setBackgroundResource(R.color.list_bg_1);
        }




        return rootView;
    }
}
