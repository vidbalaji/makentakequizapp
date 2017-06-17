package com.vidhyalearning.myquizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 08-05-2017.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final int DB_VERSION=12;
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "username";
    public static final String CONTACTS_COLUMN_PSWD = "password";
    public static final String QA_COLUMN_IMAGE = "image";
    public static final String QA_COLUMN_QUESTION = "question";
    public static final String QA_COLUMN_ANSWER = "answer";
    public static final String QA_COLUMN_SUBJECT = "subject";
    public static final String QA_COLUMN_LEVEL = "level";
    public static final String CATEGORY_TABLE="TBL_CATEGORY";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, " + CONTACTS_COLUMN_NAME + " text, "
                  + CONTACTS_COLUMN_PSWD + " text   );"

        );
        db.execSQL(
                "create table tbl_question_answer " +
                        "(id integer primary key, question text, answer text,level text,subject text, image  blob    );"

        );
        db.execSQL(
                "create table TBL_CATEGORY " +
                        "(subject text primary key, image  blob    );"

        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL("DROP TABLE IF EXISTS tbl_question_answer");
        db.execSQL("DROP TABLE IF EXISTS tbl_category");
        onCreate(db);

    }

    public void insertCategory(String subject , Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("subject",subject);
        if(image!=null) {
            values.put(QA_COLUMN_IMAGE, getBytes(image));
        }

        db.insert("tbl_category", null, values);

        db.close();

    }

    public void insertQA(questionAnswer qa){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(QA_COLUMN_QUESTION, qa.getQuestion());
        values.put(QA_COLUMN_ANSWER, qa.getAnswer());
        values.put(QA_COLUMN_LEVEL,qa.getLevel());
        if(qa.getImage()!=null) {
            values.put(QA_COLUMN_IMAGE, getBytes(qa.getImage()));
        }
        values.put(QA_COLUMN_SUBJECT,qa.getSubject());
        db.insert("tbl_question_answer", null, values);

        db.close();

        // insert row
    }

    public void deleteCategory(String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tbl_category",  "subject = ?",
                new String[] { subject });
    }

    public int updateQA(questionAnswer qa) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QA_COLUMN_QUESTION, qa.getQuestion());
        values.put(QA_COLUMN_ANSWER, qa.getAnswer());
        values.put(QA_COLUMN_LEVEL,qa.getLevel());
       if(qa.getImage()!=null) {
           values.put(QA_COLUMN_IMAGE, getBytes(qa.getImage()));
       }
       else
        values.put(QA_COLUMN_IMAGE, (byte[]) null);

        //values.put(QA_COLUMN_SUBJECT,qa.getSubject());// updating row
        return db.update("tbl_question_answer", values,  "id = ?",
                new String[] { String.valueOf(qa.getId()) });
    }
    /*
 * Deleting a todo
 */
    public void deleteQA(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tbl_question_answer",  "id = ?",
                new String[] { String.valueOf(id) });

        db.close();

    }

    public ArrayList<questionAnswer> getSubjects()
    {
        ArrayList<questionAnswer> qaList = new ArrayList<questionAnswer>();
        String selectQuery = "SELECT  * FROM  tbl_category  ";

        Log.e("Here DB", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                questionAnswer t = new questionAnswer();
                t.setSubject(c.getString(c.getColumnIndex(QA_COLUMN_SUBJECT)));


                byte[] blob = c.getBlob(c.getColumnIndex(QA_COLUMN_IMAGE));
                if(blob != null) {
                    Bitmap bit = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                    // adding to tags
                    // list
                    t.setImage(bit);
                }

                qaList.add(t);
            } while (c.moveToNext());
        }
        if(c.isClosed()==false)
            c.close();
        db.close();

        return qaList;

    }

    public int getNumberQAs(String subject) {

        String selectQuery = "SELECT  1 FROM  tbl_question_answer  ";
        if (subject != null && !(subject.isEmpty())) {
            selectQuery = selectQuery + "where subject=" + "'" + subject + "'";
        }

        Log.e("Here DB", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
       return(c.getCount());

    }
        public ArrayList<questionAnswer> getAllQAs(String subject,String level) {
        ArrayList<questionAnswer> qaList = new ArrayList<questionAnswer>();
        String selectQuery = "SELECT  * FROM  tbl_question_answer  ";
        if(subject!=null && !(subject.isEmpty()) ){
            selectQuery = selectQuery + "where subject=" + "'" + subject + "'";
        }
        if(level!=null && !(level.isEmpty()) ){
            selectQuery = selectQuery + "and level=" + "'" + level + "'" ;
        }
        Log.e("Here DB", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                questionAnswer t = new questionAnswer();
                t.setId(c.getInt((c.getColumnIndex(CONTACTS_COLUMN_ID))));
                t.setQuestion(c.getString(c.getColumnIndex(QA_COLUMN_QUESTION)));
                t.setAnswer(c.getString(c.getColumnIndex(QA_COLUMN_ANSWER)));
                t.setSubject(c.getString(c.getColumnIndex(QA_COLUMN_SUBJECT)));
                t.setLevel(c.getString(c.getColumnIndex(QA_COLUMN_LEVEL)));

                byte[] blob = c.getBlob(c.getColumnIndex(QA_COLUMN_IMAGE));
                if(blob != null) {
                    Bitmap bit = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                    // adding to tags
                    // list
                    t.setImage(bit);
                }

                qaList.add(t);
            } while (c.moveToNext());
        }
        if(c.isClosed()==false)
            c.close();
        db.close();

        return qaList;
    }

    public questionAnswer getQA(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM  tbl_question_answer where id = "
               +id;

        Log.e("Here DB", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        questionAnswer qa = new questionAnswer();
        qa.setId(c.getInt((c.getColumnIndex(CONTACTS_COLUMN_ID))));
        qa.setQuestion(c.getString(c.getColumnIndex(QA_COLUMN_QUESTION)));
        qa.setAnswer(c.getString(c.getColumnIndex(QA_COLUMN_ANSWER)));
        qa.setSubject(c.getString(c.getColumnIndex(QA_COLUMN_SUBJECT)));
        qa.setLevel(c.getString(c.getColumnIndex(QA_COLUMN_LEVEL)));

        byte[] blob = c.getBlob(c.getColumnIndex(QA_COLUMN_IMAGE));
        if(blob != null) {
            Bitmap bit = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            // adding to tags list
            qa.setImage(bit);
        }
        if(c.isClosed()==false)
            c.close();
        db.close();

        return qa;
    }

    public boolean getSubject(String subject) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM  TBL_CATEGORY where subject = "
                +"'" + subject + "'";

        Log.e("Here DB", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            if(c.isClosed()==false)
                c.close();
            db.close();
            return true;
        }
        else
            return false;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    public boolean insertContact (String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", name);
        contentValues.put("password", password);

        db.insert("contacts", null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where username='"+name+"'", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        db.close();
        return numRows;
    }

    public int qaNumberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "tbl_question_answer");
        db.close();
        return numRows;
    }
    public int updateContact(String uName,String pswd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CONTACTS_COLUMN_PSWD, pswd);

        // updating row
        return db.update(CONTACTS_TABLE_NAME, values,  CONTACTS_COLUMN_NAME + "= ?",
                new String[]{uName});
    }

    public ArrayList<String> getAllSubjects() {

        ArrayList<String> allSubjects = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM  tbl_category  ";

        Log.e("Here DB", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                allSubjects.add(c.getString(c.getColumnIndex(QA_COLUMN_SUBJECT)));

            } while (c.moveToNext());
        }
        if(c.isClosed()==false)
            c.close();
        db.close();

        return allSubjects;
    }


    public ArrayList<String> getAllUsers() {
        ArrayList<String> allUsers = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM  contacts where username<>'admin' ";

        Log.e("Here DB", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                allUsers.add(c.getString(c.getColumnIndex("username")));



            } while (c.moveToNext());
        }
        if(c.isClosed()==false)
            c.close();
        db.close();

        return allUsers;
    }

    public void deleteUser(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contacts",  "username = ?",
                new String[] { user });
        db.close();
    }

    public int deleteBulkQuestions(String subjectText, String strLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected=0;
        if(strLevel.isEmpty()==false) {
            rowsAffected=db.delete("tbl_question_answer",  "subject = ? and level = ?",
                    new String[] { subjectText, strLevel});

        }
        else
        {
            rowsAffected=db.delete("tbl_question_answer",  "subject = ? ",
                    new String[] { subjectText});
        }
        db.close();
        return rowsAffected;
    }
}
