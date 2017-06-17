package com.vidhyalearning.myquizapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link downloadToXml.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link downloadToXml#newInstance} factory method to
 * create an instance of this fragment.
 */
public class downloadToXml extends AdminActivity.PlaceholderFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String strData,strLevel,xmlString;
    Button downLoadBtn,shareItButton;
    TextView xmlStrText;
    MyDBHelper mydb;
    Spinner staticSpinner;
    ArrayAdapter<String> staticAdapter;
    public downloadToXml() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment downloadToXml.
     */
    // TODO: Rename and change types and number of parameters
    public static downloadToXml newInstance() {
        downloadToXml fragment = new downloadToXml();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_download_to_xml, container, false);
        xmlStrText = (TextView)view.findViewById(R.id.xmlText) ;
        xmlStrText.setMovementMethod(new ScrollingMovementMethod());
        mydb = new MyDBHelper(getActivity(),"MyDBName.db",null,MyDBHelper.DB_VERSION);
        ArrayList<String> labels = null;
        labels=mydb.getAllSubjects();
        labels.add("All");
        // Creating adapter for spinner
        staticAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, labels);


        Spinner staticSpinner = (Spinner) view.findViewById(R.id.spinnerCategory);
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

        Spinner staticSpinnerLevel = (Spinner) view.findViewById(R.id.spinnerGrade);
        String[] newArray = getResources().getStringArray(R.array.class_array);
        ArrayList<String> classlist = new ArrayList<String>();
        classlist.add("All");
        for (int j = 0; j < newArray.length; j++) {
            classlist.add(newArray[j]);
        }


        // Create an ArrayAdapter using the string array and a default spinner

        ArrayAdapter<String> staticAdapterLevel = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, classlist);


        // Specify the layout to use when the list of choices appears
        staticAdapterLevel
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinnerLevel.setAdapter(staticAdapterLevel);

        staticSpinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                strLevel = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        downLoadBtn = (Button)view.findViewById(R.id.submitBtn);
        shareItButton = (Button)view.findViewById(R.id.sharebutton);
        shareItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, xmlString);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        downLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject="",grade="";
                if(!strData.equals("All")){
                    subject=strData;
                }
                if(!strLevel.equals("All")){
                    grade=strLevel;
                }
                downloadxmlFn(subject,grade);
                shareItButton.setEnabled(true);
            }
        });
        return view;
    }

    private void downloadxmlFn(String subject, String grade)  {
       ArrayList<questionAnswer> qaList =  mydb.getAllQAs(subject,grade);
        if(qaList.isEmpty()){
            Toast.makeText(getActivity(),"No questions to download",Toast.LENGTH_SHORT).show();
            xmlStrText.setText("");
            shareItButton.setEnabled(false);
            return;

        }
        StringWriter writer;
        try {
            XmlSerializer xmlSerializer = Xml.newSerializer();
             writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            // start DOCUMENT
            xmlSerializer.startDocument("UTF-8", true);
            // open tag: <record>
            xmlSerializer.startTag("", "record");
            int i;
            for(i=0;i<qaList.size();i++) {
                questionAnswer qaObj = qaList.get(i);
                // open tag: <qaset>
                xmlSerializer.startTag("", "qaset");


                // open tag: <question>
                xmlSerializer.startTag("", "question");
                xmlSerializer.text(qaObj.getQuestion());
                // close tag: </question>
                xmlSerializer.endTag("", "question");

                // open tag: <answer>
                xmlSerializer.startTag("", "answer");
                xmlSerializer.text(qaObj.getAnswer());
                // close tag: </answer>
                xmlSerializer.endTag("", "answer");

                // open tag: <level>
                xmlSerializer.startTag("", "level");
                xmlSerializer.text(qaObj.getLevel());
                // close tag: </level>
                xmlSerializer.endTag("", "level");

                // open tag: <subject>
                xmlSerializer.startTag("", "subject");
                xmlSerializer.text(qaObj.getSubject());
                // close tag: </subject>
                xmlSerializer.endTag("", "subject");

                // close tag: </study>
                xmlSerializer.endTag("", "qaset");
            }
            // close tag: </record>
            xmlSerializer.endTag("", "record");

            // end DOCUMENT
            xmlSerializer.endDocument();
            xmlString = writer.toString();
            xmlStrText.setText(xmlString);
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(),"Some Error  " + e.getMessage()   ,Toast.LENGTH_SHORT).show();
        }
    }

}
