package com.vidhyalearning.myquizapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddCategory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCategory extends AdminActivity.PlaceholderFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String imgDecodableString;
    Bitmap image = null;
    private static final int RESULT_LOAD_IMG =1 ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MyDBHelper myDB;
    EditText txtSubject;
    ImageView imgView;
    Button captureBtn,submitBtn;


    public AddCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCategory newInstance(String userName) {
        AddCategory fragment = new AddCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userName);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_category, container, false);
         txtSubject=(EditText) view.findViewById(R.id.txtSubject);
        Button captureBtn,submitBtn;
        imgView = (ImageView) view.findViewById(R.id.imageView2);

        captureBtn = (Button)view.findViewById(R.id.captureImage);
        submitBtn = (Button)view.findViewById(R.id.addSubject);
        captureBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);




            }

        });
        submitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               String strSubject = txtSubject.getText().toString().trim();
                if(strSubject.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Subject", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                myDB = new MyDBHelper(getActivity(),"MyDBName.db",null,MyDBHelper.DB_VERSION);
                myDB.insertCategory(strSubject,image);
                Toast.makeText(getActivity(), "Subject Added", Toast.LENGTH_LONG)
                        .show();

            }

        });
        return view;

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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
                    Toast.makeText(getActivity(),"Image size is too large."
                            + "\n Please restrict image size to less han 100 KB.\n Tip:You can crop the image or take a screenshot of image",Toast.LENGTH_LONG ).show();
                    return;
                }

                imgView.setImageBitmap(image);

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("VB ", e.getMessage());
            Toast.makeText(getActivity(), "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

}
