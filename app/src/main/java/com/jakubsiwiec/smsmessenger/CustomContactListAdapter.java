package com.jakubsiwiec.smsmessenger;


import android.app.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomContactListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final Integer[] imgid;

    public CustomContactListAdapter(Context context, ArrayList<String> maintitle, ArrayList<String>  subtitle, ArrayList<Integer>  imgid) {
        super(context, R.layout.custom_contact_list, maintitle);
        // TODO Auto-generated constructor stub

        this.context= (Activity) context;
        this.maintitle= maintitle.toArray(new String[0]);
        this.subtitle= subtitle.toArray(new String[0]);
        this.imgid= imgid.toArray(new Integer[0]);

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_contact_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(maintitle[position]);
        imageView.setImageResource(imgid[position]);
        subtitleText.setText(subtitle[position]);

        return rowView;

    };

}


