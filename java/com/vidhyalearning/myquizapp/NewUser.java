package com.vidhyalearning.myquizapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewUser extends Activity {
    EditText txtUserName,txtUserPassword,txtConfirmPswd;
    MyDBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        mydb = new MyDBHelper(this,"MyDBName.db",null,MyDBHelper.DB_VERSION);
        txtUserName = (EditText)findViewById(R.id.txtOldPassword);
        txtUserPassword = (EditText)findViewById(R.id.txtNewPassword);
        txtConfirmPswd = (EditText)findViewById(R.id.txtConfirmPassword);
    }

    public void createNewUserInDB(View view) {
        String strUser, strPassword, strConfirmPassword;
        strUser = txtUserName.getText().toString();
        strPassword = txtUserPassword.getText().toString();
        strConfirmPassword = txtConfirmPswd.getText().toString();
        if (strUser.length() > 0) {
            Cursor rs = mydb.getData(strUser);

            String dbNam, dbPswd;
            if (rs.getCount() != 0) {
                Toast.makeText(this, "UserName already exists", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(!(strPassword.equals(strConfirmPassword))){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }
        mydb.insertContact(strUser,strPassword);
        Toast.makeText(this, "Login Username/Password created successfully", Toast.LENGTH_LONG).show();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }
}
