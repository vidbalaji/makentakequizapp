package com.vidhyalearning.myquizapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.id.content;

public class reviewquiz extends AppCompatActivity {
    ArrayList<reviewQuestionAnswer> reviewQAArray;
    float marks;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewquiz);
         marks= getIntent().getFloatExtra("Marks",0);
        reviewQAArray= (ArrayList<reviewQuestionAnswer>) getIntent().getSerializableExtra("reviewQAArray");
        populateTables();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void populateTables() {
        TableLayout tl = (TableLayout) findViewById(R.id.tableLayout01);
        int i;
        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setStyle(Paint.Style.STROKE);
        border.getPaint().setColor(Color.BLACK);
        if (reviewQAArray.size() > 0) {
            for (i = 0; i <= reviewQAArray.size() - 1; i++) {
                TableRow tr = new TableRow(this);
                TextView tId = new TextView(this);
                TextView tQn = new TextView(this);
                TextView tYa = new TextView(this);
                TextView tCa = new TextView(this);
                tId.setText(String.valueOf(reviewQAArray.get(i).getId()));
                tId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
               // tId.setBackground(border);
                tQn.setText(reviewQAArray.get(i).getQuestion());
                tQn.setVerticalScrollBarEnabled(true);

                tQn.setLayoutParams(new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams tLP = (TableRow.LayoutParams) tQn.getLayoutParams();

                tQn.setBackground(border);

                String yourAnswer = reviewQAArray.get(i).getYourAnswer();
                String correctAnswer = reviewQAArray.get(i).getCorrectAnswer();
                tYa.setText(yourAnswer);

            tYa.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, tLP.WRAP_CONTENT));
                tYa.setBackground(border);
                tCa.setText(correctAnswer);

                tCa.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                tCa.setBackground(border);
                if(reviewQAArray.get(i).getRightOrWrong()){
                    tYa.setBackgroundColor(getResources().getColor(R.color.correctAnswer));

                }
                else
                {
                    tYa.setBackgroundColor(getResources().getColor(R.color.wrongAnswer));
                }
                tr.addView(tId);
                        tr.addView(tQn);
                tr.addView(tCa);
                tr.addView(tYa);

                tr.setBackground(border);

                tl.addView(tr, new ActionBar.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
            TableRow tr1 = new TableRow(this);
            TextView tMarks = new TextView(this);
            TextView tDescMarks = new TextView(this);
            tMarks.setText(String.valueOf(marks) + "/" + String.valueOf(reviewQAArray.get(i-1).getId()));
            tDescMarks.setText("Total Score:");
            TextView tDummy = new TextView(this);
            TextView tDummy1 = new TextView(this);
            tDummy.setText("              ");
            tDummy1.setText("              ");
            tr1.addView(tDummy);
            tr1.addView(tDescMarks);
            tr1.addView(tMarks);
            tr1.addView(tDummy1);
            tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr1.setBackground(border);
            tl.addView(tr1, new ActionBar.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(101, returnIntent);
        finish();
    }
}
