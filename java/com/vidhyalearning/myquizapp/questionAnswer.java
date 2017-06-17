package com.vidhyalearning.myquizapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

/**
 * Created by user on 19-05-2017.
 */

public class questionAnswer {
    int id;
    String question ;
    String answer ;
    String level ;
    String subject;
    Bitmap image;

    public questionAnswer() {
    }

    public questionAnswer(String question, String answer,String level,String subject,Bitmap image) {
        String compressed = question;
        compressed = question.replaceAll("\n{2,}", "\n");
        this.question = compressed;
        String anscompressed = answer;
        anscompressed = answer.replaceAll("\n{2,}", "\n");
        this.answer = anscompressed;
        this.level = level;
        this.subject=subject;
        this.image = image;

    }



    public String getAnswer() {
        return answer;
    }

    public String getSubject() {
        return subject;
    }

    public String getQuestion() {
        return question;
    }

    public String getLevel() {
        return level;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setAnswer(String answer) {

        String anscompressed = answer;
        anscompressed = answer.replaceAll("\n{2,}", "\n");
        this.answer = anscompressed;

    }

    public void setQuestion(String question) {
        String compressed = question;
         compressed = question.replaceAll("\n{2,}", "\n");

        this.question = compressed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
