package com.vidhyalearning.myquizapp;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class editQuestion extends AppCompatActivity {
    Button nxtButton;
    private static final int RESULT_LOAD_IMG =1 ;
    String imgDecodableString;
    Bitmap image = null;
    ImageView imgView;
    TextView textSubjectView,textQnView,textAnsView,textLevelView;
    String subjectText;
    String level,strLevel;
    MyDBHelper myDB;
    int bgColor =0,id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        initPage();
    }
    private void initPage() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quiz Maker");
        imgView = (ImageView) findViewById(R.id.imgView1);
        if(getIntent().hasExtra("byteArray")) {

            Bitmap _bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
           if(_bitmap!=null)
            imgView.setImageBitmap(_bitmap);
        }
        textSubjectView = (TextView)findViewById(R.id.categorytext);
        if(getIntent().hasExtra("subjectArray")){
            subjectText = getIntent().getStringExtra("subjectArray");

            textSubjectView.setText(subjectText);
        }
        if(getIntent().hasExtra("bgColor")){

            bgColor = getIntent().getIntExtra("bgColor",0);

            //getWindow().getDecorView().setBackgroundColor(bgColor);
            View v = findViewById(R.id.editquizview);
            v.setBackgroundColor(bgColor);
        }

        if(getIntent().hasExtra("id")){
            id = Integer.parseInt(getIntent().getStringExtra("id"));

        }
        textQnView = (TextView)findViewById(R.id.questionText);
        if(getIntent().hasExtra("question")){
            String question = getIntent().getStringExtra("question");

            textQnView.setText(question);
        }
        textAnsView = (TextView)findViewById(R.id.answerText);
        if(getIntent().hasExtra("answer")){
            String answer = getIntent().getStringExtra("answer");

            textAnsView.setText(answer);
        }
        //textLevelView = (TextView)findViewById(R.id.txtLevel);
        if(getIntent().hasExtra("level")){
             level = getIntent().getStringExtra("level");

           // textLevelView.setText(level);
        }

        myDB = new MyDBHelper(this,"MyDBName.db",null,MyDBHelper.DB_VERSION);
        Spinner staticSpinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.class_array,
                        android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);
        selectSpinnerItemByValue(staticSpinner ,  level) ;
        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    }
    public static void selectSpinnerItemByValue(Spinner spnr, String value) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            String strItem = adapter.getItem(position).toString();
            if(strItem.equals(value)) {
                spnr.setSelection(position);
                return;
            }
        }
    }


    public void deleteFn() {
        AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete Image");

        alertDialogBuilder.setMessage("Press Yes if you want to  delete the Image");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                imgView.setImageBitmap(null);
               image = null;
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

    public void submitQuestion(View view) {
        questionAnswer qa =  myDB.getQA(id);
        String qnString =textQnView.getText().toString();
        String ansString =textAnsView.getText().toString();

        String subject =textSubjectView.getText().toString();
        qa.setQuestion(qnString);
        qa.setAnswer(ansString);
        qa.setImage(image);
        qa.setLevel(strLevel);
        qa.setSubject(subject);
        myDB.updateQA(qa);
        int rows = myDB.qaNumberOfRows();

        finish();
    }

    public void capturePhotos(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                image = BitmapFactory
                        .decodeFile(imgDecodableString);
                if(image.getAllocationByteCount() >1500000)
                {
                    Toast.makeText(this,"Image size is too large."
                            + "\n Please restrict image size to less han 100 KB.\n Tip:You can crop the image or take a screenshot of image",Toast.LENGTH_LONG ).show();
                    return;
                }

                imgView.setImageBitmap(image);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
                Bitmap bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
                if(bitmap!=null)
                    deleteFn(  );

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Quizapp ", e.getMessage());
            Toast.makeText(this, "Something went wrong" +  e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void deleteQA(View view) {
        myDB.deleteQA(id);
        Toast.makeText(this, "Question Deleted ", Toast.LENGTH_LONG)
                .show();
        finish();
    }

    public void addQA(View view) {
        questionAnswer qa =  myDB.getQA(id);
        String qnString =textQnView.getText().toString();
        String ansString =textAnsView.getText().toString();

        String subject =textSubjectView.getText().toString();
        qa.setQuestion(qnString);
        qa.setAnswer(ansString);
        qa.setImage(image);
        qa.setLevel(strLevel);
        qa.setSubject(subject);
        myDB.insertQA(qa);
        int rows = myDB.qaNumberOfRows();

        finish();
    }
}
