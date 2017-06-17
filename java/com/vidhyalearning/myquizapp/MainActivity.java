package com.vidhyalearning.myquizapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.Boolean.FALSE;

public class MainActivity extends AppCompatActivity {
    EditText txtUserName,txtUserPassword;
    MyDBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new MyDBHelper(this,"MyDBName.db",null,MyDBHelper.DB_VERSION);
        txtUserName = (EditText)findViewById(R.id.textUserName);
        txtUserPassword = (EditText)findViewById(R.id.textPassword);
        //String path =getApplicationContext().getPackageCodePath();
        if(mydb.qaNumberOfRows()==0)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setMessage("Do you want to preload some questions?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete

                    parseXML("qalist.xml");

                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    dialog.dismiss();
                }
            });


            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        }


    }

    private void parseXML(String xmlFile) {
        try {
            InputStream is = getAssets().open(xmlFile);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("qaset");
            Vector<String> subjectList = new Vector<String>();
            subjectList.clear();
            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    String question = getValue("question", element2);
                    String answer =getValue("answer", element2);
                    String level = getValue("level", element2);
                    String subject = getValue("subject", element2);
                    questionAnswer qa = new questionAnswer(question,answer,level,subject,null);
                    if((subjectList.isEmpty() || subjectList.contains(subject) == false))
                    {
                        mydb.insertCategory(subject,null);
                        subjectList.add(subject);
                    }
                    mydb.insertQA(qa);
                }
            }

        } catch (Exception e) {e.printStackTrace();}
    }
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public void createNewUser(View view) {
        //Toast.makeText(this,"Here",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,NewUser.class);
        int requestCode=0;
        startActivityForResult(intent,requestCode);
    }

    public void loginFunction(View view) {
        String strUser = txtUserName.getText().toString();
        String strPassword = txtUserPassword.getText().toString();

        if(strUser.length()>0)
        {
            Cursor rs = mydb.getData(strUser);

            String dbNam,dbPswd;
            if(rs.getCount()!=0) {
                rs.moveToFirst();
                dbNam = rs.getString(rs.getColumnIndex(MyDBHelper.CONTACTS_COLUMN_NAME));
                dbPswd = rs.getString(rs.getColumnIndex(MyDBHelper.CONTACTS_COLUMN_PSWD));
            }
            else
            {
                Toast.makeText(this,"Invalid UserName",Toast.LENGTH_LONG).show();
                return;
            }
            if(!(dbPswd.equals(strPassword)))
            {
                Toast.makeText(this,"Invalid Password",Toast.LENGTH_LONG).show();
                return;
            }
            if (!rs.isClosed()) {
                rs.close();
            }
            // Toast.makeText(this,path,Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra("UserName",strUser);

                intent.setClass(this,AdminActivity.class);
            startActivity(intent);

        }

    }

    public void resetPassword(View view) {
         final String strUser = txtUserName.getText().toString();
        if(strUser.isEmpty()) {
            Toast.makeText(this,"UserName is blank",Toast.LENGTH_LONG).show();
            return;
        }
        if (strUser.length() > 0) {
            Cursor rs = mydb.getData(strUser);

            String dbNam, dbPswd;
            if (rs.getCount() == 0) {
                Toast.makeText(this, "UserName does not exists", Toast.LENGTH_LONG).show();
                return;
            }
        }

        AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Reset Password");
        alertDialogBuilder.setMessage("Are you sure you want to reset the password to 1234?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                            mydb.updateContact(strUser, "1234");
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


    public void helpFunction(View view) {
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog2);
        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.okButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();

            }
        });
    }
}
