package com.vidhyalearning.myquizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class adminquiz extends Activity {

    private static final int RESULT_LOAD_IMG =1 ;
    String imgDecodableString;
    EditText qnText,ansText;
    String strLevel,subject,level;
Bitmap image = null;
    ImageView imgView;
    MyDBHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminquiz);
        ansText   = (EditText)findViewById(R.id.answerText);
        qnText   = (EditText)findViewById(R.id.questionText);
        subject = getIntent().getStringExtra("Subject");
        level = getIntent().getStringExtra("level");
        int bgColor = getIntent().getIntExtra("bgColor",0);
        View v = findViewById(R.id.adminquizview);
        v.setBackgroundColor(bgColor);
        myDB = new MyDBHelper(this,"MyDBName.db",null,MyDBHelper.DB_VERSION);
        Spinner staticSpinner = (Spinner) findViewById(R.id.spinner);


        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.class_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout
        // to use when the list of choices appears
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
    public void capturePhotos(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         imgView= (ImageView) findViewById(R.id.imgView1);;
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

                // Set the Image in ImageView after decoding the String

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
            Log.d("VB ", e.getMessage());
            Toast.makeText(this, "Something went wrong" +  e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }


    public void submitQuestion(View view)
    {

        String qnString = qnText.getText().toString().trim();
        String ansString = ansText.getText().toString().trim();
        if(qnString.isEmpty() || ansString.isEmpty())
        {
            Toast.makeText(this, "Question/Answer fields cannot be empty", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        questionAnswer qa = new questionAnswer( qnString,  ansString, strLevel, subject,  image);
        myDB.insertQA(qa);
        qnText.setText("");
        ansText.setText("");

        int rows = myDB.qaNumberOfRows();
        Toast.makeText(this, "Rows" + rows, Toast.LENGTH_LONG)
                .show();
    }
    public void deleteFn() {
        AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete Image");
        alertDialogBuilder.setMessage("Press Yes if you want to  delete the Image");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                imgView.setImageBitmap(null);

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

    public void backtohomeFn(View view) {
        finish();
    }
}
