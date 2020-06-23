package com.jakubsiwiec.smsmessenger;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomMessagesListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final Integer[] isSent;

    public CustomMessagesListAdapter(Context context, ArrayList<String> maintitle, ArrayList<String>  subtitle, ArrayList<Integer> isSent) {
        super(context, R.layout.custom_contact_list, maintitle);
        // TODO Auto-generated constructor stub

        this.context= (Activity) context;
        this.maintitle= maintitle.toArray(new String[0]);
        this.subtitle= subtitle.toArray(new String[0]);
        this.isSent= isSent.toArray(new Integer[0]);

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_messages_list, null,true);

        LinearLayout messageLayout = (LinearLayout) rowView.findViewById(R.id.messageLayout);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(maintitle[position]);
        subtitleText.setText(subtitle[position]);

        // Adjust the message position based on who is
        // the sender, and who is receiver
        // sent messages are positioned to right
        // received ones to left
        if(isSent[position] == 0){
            messageLayout.setGravity(Gravity.LEFT);
            messageLayout.setPadding(10, 5, 40, 5);
        }
        else{
            messageLayout.setGravity(Gravity.RIGHT);
            messageLayout.setPadding(40, 5, 10, 5);
        }

        return rowView;

    };

}


