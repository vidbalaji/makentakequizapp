package com.vidhyalearning.myquizapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link deleteElement.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link deleteElement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class deleteElement extends AdminActivity.PlaceholderFragment {

    // TODO: Rename and change types of parameters
    private String mParam1;
   // private String mParam2;
Button deleteBtn;
MyDBHelper mydb;
    Spinner staticSpinner;
    ArrayAdapter<String> staticAdapter;
    String strData;
    public deleteElement() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static deleteElement newInstance(String param1) {
        deleteElement fragment = new deleteElement();
        Bundle args = new Bundle();
        args.putString("DeleteType", param1);

        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("DeleteType");

        }
        mydb = new MyDBHelper(getActivity(),"MyDBName.db",null,MyDBHelper.DB_VERSION);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_delete_element, container, false);
        deleteBtn=(Button)view.findViewById(R.id.deleteQnBtn);
         staticSpinner = (Spinner) view.findViewById(R.id.spinnerDelete);
        deleteBtn.setEnabled(true);
        // Spinner Drop down elements

        ArrayList<String> labels = null;
        if(mParam1 == "CATEGORY") {
        labels=mydb.getAllSubjects();
            if(labels.isEmpty()){
                labels.add("No categories added");
                deleteBtn.setEnabled(false);
            }

        }
        else if(mParam1 == "USER") {
            labels=mydb.getAllUsers();
            if(labels.isEmpty()){
                labels.add("No users added");
                deleteBtn.setEnabled(false);

            }
        }

        // Creating adapter for spinner
        staticAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, labels);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);


        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                strData = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                deleteFn();
            }
        });

        return view;
    }

    private void deleteFn() {
        if(mParam1=="CATEGORY")
        {
            if(mydb.getNumberQAs(strData)!=0)
            {
                Toast.makeText(getActivity(),"Cannot delete category.Please Delete the questions under this Category",Toast.LENGTH_LONG).show();
                return;
            }
            else{
                mydb.deleteCategory(strData);
                Toast.makeText(getActivity(),"Deleted "+strData + " Category",Toast.LENGTH_LONG).show();
                return;
            }

        }


        else if(mParam1=="USER")
        {
            mydb.deleteUser(strData);
        }
        staticAdapter.notifyDataSetChanged();

    }


}
