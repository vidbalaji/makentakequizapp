package com.vidhyalearning.myquizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class questionListDisplay extends AppCompatActivity {
String subject,level;
    EditText searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subject = getIntent().getStringExtra("Subject");
        level = getIntent().getStringExtra("level");
        setContentView(R.layout.activity_question_list_display);
        searchText = (EditText)findViewById(R.id.search_box);
        final questionFragment qf = (questionFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_question);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                qf.mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }
    public String getSubject(){
        return subject;
    }
    public String getLevel(){
        return level;
    }
}
