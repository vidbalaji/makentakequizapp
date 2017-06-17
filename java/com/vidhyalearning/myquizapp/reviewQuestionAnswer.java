package com.vidhyalearning.myquizapp;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by user on 08-06-2017.
 */

public class reviewQuestionAnswer implements Serializable{
    int id;
    String question;
    String yourAnswer;
    String correctAnswer;
    boolean rightOrWrong;
    float marksPercent;
    public reviewQuestionAnswer() {
    }

    public reviewQuestionAnswer(String question, String yourAnswer,String correctAnswer,int id,boolean rightOrWrong) {
        this.question = question;
        this.yourAnswer = yourAnswer;
        this.correctAnswer = correctAnswer;
        this.id=id;
        this.rightOrWrong=rightOrWrong;
    }

    public String getYourAnswer() {
        return yourAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }



    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean getRightOrWrong() {
        return rightOrWrong;
    }
}
