package com.jakubsiwiec.smsmessenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    private static final String TAG = "ListDataFragment";
    private DataBaseHelper dataBaseHelper;
    private ListView listViewContacts;


    private void populateListView(){

        Log.d(TAG, "Populate list view: displaying data in the ListView");
        Cursor data = dataBaseHelper.getContacts();
        ArrayList<String> listData = new ArrayList<>();
        ArrayList<String>  maintitle = new ArrayList<>();
        ArrayList<String>  subtitle = new ArrayList<>();
        ArrayList<Integer>  imgid= new ArrayList<>();
        int i = 1;
        while(data.moveToNext()){

            // Get name and phone number
            String contactName = data.getString(1);
            String contactPhoneNumber = data.getString(2);

            maintitle.add(contactName);
            subtitle.add(contactPhoneNumber);
            imgid.add(R.drawable.ic_baseline_account_circle_24);

            Log.d(TAG, data.getString(1));
            i++;
        }

        Log.d(TAG, String.valueOf(data.getCount()));

        CustomContactListAdapter adapter=new CustomContactListAdapter(getContext(), maintitle, subtitle,imgid);
        listViewContacts.setAdapter(adapter);

    }


    private AlertDialog deleteDialog(Object o){

        final String contactName = o.toString();

//        Log.d("Event position", String.valueOf(eventPosition));
//        final Cursor data = dataBaseHelper.getContacts();
//
//        data.moveToPosition(eventPosition);
//
//        String eventTitle = data.getString(2);
//
//        //represent date in readable format
//        String fullDate = data.getString(6);
//        String displayDate = fullDate.substring(0, 10) + " " + fullDate.substring(fullDate.length() - 4);
//
//        //trim seconds
//        String fullStartTime = data.getString(7);
//        String fullFinishTime = data.getString(8);
//
//        String dispStartTime = fullStartTime.substring(0, 5);
//        String dispFinishTime = fullFinishTime.substring(0, 5);
//
//        String eventInfo = data.getString(2) + "\n\nLocation: " + data.getString(3)
//                + "\nDate:" + displayDate + ",   " + dispStartTime + "-" + dispFinishTime;

        AlertDialog.Builder editDeleteContactsDialogBuilder = new AlertDialog.Builder(getActivity());
        editDeleteContactsDialogBuilder.setTitle(contactName);

        editDeleteContactsDialogBuilder.setPositiveButton("EDIT CONTACT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });



        editDeleteContactsDialogBuilder.setNegativeButton("DELETE CONTACT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dataBaseHelper.deleteContact(contactName);
                populateListView();
            }
        });


        return editDeleteContactsDialogBuilder.create();
    }


    public ContactsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle bundle = new Bundle();

        listViewContacts = (ListView) view.findViewById(R.id.listViewContacts);
        dataBaseHelper = new DataBaseHelper(getContext());

        populateListView();

        listViewContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                Object o = listViewContacts.getItemAtPosition(position);

                Log.i(TAG, "Item of position " + position + " and l " + l + " held");

                AlertDialog deleteDialog = deleteDialog(o);
                deleteDialog.show();
                return true;
            }

        });

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = listViewContacts.getItemAtPosition(position);

                bundle.putString("contactName", o.toString());

                Log.i(TAG, "Item of position " + position + " clicked");

            }
        });
    }
}