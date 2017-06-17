package com.vidhyalearning.myquizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OpenCategory extends AppCompatActivity {
    Button nxtButton,editButton,delButton;
    ImageView imgView;
    TextView textView;
    String subjectText;

    int bgColor =0;
    Spinner staticSpinner;
    MyDBHelper mydb;
    String strLevel;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_category);

        initPage();
    }

    private void initPage() {
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = "";
        mydb = new MyDBHelper(this,"MyDBName.db",null,MyDBHelper.DB_VERSION);
        nxtButton = (Button) findViewById(R.id.nextButton);
        delButton = (Button) findViewById(R.id.deleteQnBtn);
        staticSpinner = (Spinner) findViewById(R.id.spinner2);
        if (getIntent().hasExtra("Username")) {
            user = getIntent().getStringExtra("Username");


        }
        String[] newArray = getResources().getStringArray(R.array.class_array);
        ArrayList<String> classlist = new ArrayList<String>();


        if (user.equals("admin")) {
            getSupportActionBar().setTitle("Quiz Maker");
            staticSpinner.setVisibility(View.INVISIBLE);
            nxtButton.setText("Add Questions");
            classlist.add("All");
            delButton.setVisibility(View.VISIBLE);
        } else {
            getSupportActionBar().setTitle("Quiz Challenge");
            editButton = (Button) findViewById(R.id.editqnButton);
            editButton.setVisibility(View.INVISIBLE);
            nxtButton.setText("Start Quiz");
            delButton.setVisibility(View.INVISIBLE);
        }
        staticSpinner.setVisibility(View.VISIBLE);
        // Create an ArrayAdapter using the string array and a default spinner


        for (int j = 0; j < newArray.length; j++) {
            classlist.add(newArray[j]);
        }

        // Create an ArrayAdapter using the string array and a default spinner

        ArrayAdapter<String> staticAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, classlist);


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
                strLevel = (String) parent.getItemAtPosition(position);
                if (strLevel.equals("All")) {
                    strLevel = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    
        
        if(getIntent().hasExtra("byteArray")) {
            imgView = (ImageView) findViewById(R.id.subjectImgView);
            Bitmap _bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            imgView.setImageBitmap(_bitmap);
        }
        if(getIntent().hasExtra("subjectArray")){
             subjectText = getIntent().getStringExtra("subjectArray");
            textView = (TextView)findViewById(R.id.categorytext);
            textView.setText(subjectText);
        }
        if(getIntent().hasExtra("bgColor")){
            bgColor = getIntent().getIntExtra("bgColor",0);
            //getWindow().getDecorView().setBackgroundColor(bgColor);
            View v = findViewById(R.id.openCategoryView);
                    v.setBackgroundColor(bgColor);
        }

        nxtButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gotoNextScreen();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                deleteFunction();
            }
        });
    }

    private void deleteFunction() {
        AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete Questions");
        String tempLvl = strLevel;
        if(tempLvl.isEmpty())
            tempLvl ="All";
        alertDialogBuilder.setMessage("Press Yes if you want to  delete all the questions for:\n "+ subjectText + "/" + "Level:"+tempLvl );
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                int rowsAffected =mydb.deleteBulkQuestions(subjectText,strLevel);
                Toast.makeText(getApplicationContext(),rowsAffected + " questions deleted",Toast.LENGTH_LONG).show();

            }
        });
        alertDialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
                dialog.cancel();
            }
        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();


    }

    private void gotoNextScreen()
    {
        Intent intent = new Intent();
        if(user.equals("admin")) {
            intent.setClass(this, adminquiz.class);
        }
        else
        {
            intent.setClass(this,userQuiz.class);
        }
        intent.putExtra("Subject",subjectText);
        intent.putExtra("level",strLevel);
        intent.putExtra("bgColor",bgColor);
        startActivityForResult(intent,1);
    }


    public void editQuestion(View view) {
        Intent intent = new Intent();
        intent.setClass(this,questionListDisplay.class);
        intent.putExtra("Subject",subjectText);
        intent.putExtra("level",strLevel);
        intent.putExtra("bgColor",bgColor);
        startActivityForResult(intent,1);
    }
}
