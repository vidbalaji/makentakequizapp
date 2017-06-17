package com.vidhyalearning.myquizapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends AdminActivity.PlaceholderFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String userName;
    private String mParam2;

    EditText txtOldPassword,txtNewPassword,txtConfirmPassword;
    Button submitButton;
    private OnFragmentInteractionListener mListener;
    MyDBHelper mydb;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String userName) {
        String param1="",param2="";
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userName);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mydb = new MyDBHelper(getActivity(),"MyDBName.db",null,MyDBHelper.DB_VERSION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_change_password, container, false);
        txtOldPassword  = (EditText)view.findViewById(R.id.txtOldPassword);
        txtNewPassword = (EditText)view.findViewById(R.id.txtNewPassword);
        txtConfirmPassword = (EditText)  view.findViewById(R.id.txtConfirmPassword);
        submitButton = (Button)view.findViewById(R.id.button2);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Cursor rs = mydb.getData(userName);
                String strPassword = txtOldPassword.getText().toString();
                String strNewPassword = txtNewPassword.getText().toString();
                String strConfirmPassword = txtConfirmPassword.getText().toString();
                if(!(strNewPassword.equals(strConfirmPassword)))
                {
                    Toast.makeText(getActivity(),"Passwords do not match",Toast.LENGTH_LONG).show();
                    return;
                }
                String dbNam,dbPswd;
                if(rs.getCount()!=0) {
                    rs.moveToFirst();
                    dbNam = rs.getString(rs.getColumnIndex(MyDBHelper.CONTACTS_COLUMN_NAME));
                    dbPswd = rs.getString(rs.getColumnIndex(MyDBHelper.CONTACTS_COLUMN_PSWD));
                }
                else
                {
                    Toast.makeText(getActivity(),"Invalid UserName",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!(dbPswd.equals(strPassword)))
                {
                    Toast.makeText(getActivity(),"Invalid Old Password",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!rs.isClosed()) {
                    rs.close();
                }

                mydb.updateContact(userName,strNewPassword);

                Toast.makeText(getActivity(),"Password Changed successfully!!",Toast.LENGTH_LONG).show();

            }

        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void changePasswordInDB(View view) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
