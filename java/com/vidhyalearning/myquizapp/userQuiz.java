package com.vidhyalearning.myquizapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class userQuiz extends AppCompatActivity {
    Button nxtButton;
    private static final int RESULT_LOAD_IMG =1 ;
    String imgDecodableString;
    Bitmap image = null;
    ImageView imgView;
    TextView textSubjectView,textQnView,textLevelView,txtIdView;
    EditText textAnsView;
    String subjectText;
    AlertDialog dialog;
    String level,strLevel,actAnswer;
    MyDBHelper myDB;
    int bgColor =0,id=0;
    float qns=0,marks=0;
    ArrayList<questionAnswer> qaList;
    private SoundPool soundPool;
    private int soundID,rightSound,wrongSound;
    boolean loaded = false;
    ListIterator<questionAnswer> qaIterator;
    ArrayList<String> answerArray = new ArrayList<String>();
    ArrayList<reviewQuestionAnswer> reviewQAArray = new ArrayList<reviewQuestionAnswer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_quiz);

        initPage();
    }
    private void initPage() {
        answerArray.clear();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quiz Challenge");
            imgView = (ImageView) findViewById(R.id.imgListView);

        if(getIntent().hasExtra("Subject")){
            subjectText = getIntent().getStringExtra("Subject");
            textSubjectView = (TextView)findViewById(R.id.txtSubject);
            textSubjectView.setText(subjectText);
        }
        if(getIntent().hasExtra("bgColor")){

            bgColor = getIntent().getIntExtra("bgColor",0);

            //getWindow().getDecorView().setBackgroundColor(bgColor);
            View v = findViewById(R.id.user_quiz_view);
            v.setBackgroundColor(bgColor);
        }


        textQnView = (TextView)findViewById(R.id.txtquestion);
        textQnView.setMovementMethod(new ScrollingMovementMethod());
        textAnsView = (EditText)findViewById(R.id.txtAnswer);
        txtIdView = (TextView)findViewById(R.id.txtId);
        if(getIntent().hasExtra("level")){
            level = getIntent().getStringExtra("level");
            textLevelView = (TextView)findViewById(R.id.txtLevel);
            textLevelView.setText(level);
        }

        myDB = new MyDBHelper(this,"MyDBName.db",null,MyDBHelper.DB_VERSION);
        qaList = (ArrayList<questionAnswer>)myDB.getAllQAs(subjectText,level);
        Collections.shuffle(qaList);
        if(qaList.isEmpty()==false){
            qaIterator =qaList.listIterator();
            populateQAs(qaIterator.next());
        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("No Questions");
            alertDialogBuilder.setMessage("Please contact admin for the questions");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete

                    finish();
                }
            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            setDialog(alertDialog);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();


        }
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Load the sound
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });

        soundID = soundPool.load(this, R.raw.sound1, 1);
      //  rightSound=soundPool.load(this,R.raw.rightanswer,1);
       // wrongSound=soundPool.load(this,R.raw.wronganswer,1);
        rightSound=soundPool.load(this,R.raw.right,1);
        wrongSound=soundPool.load(this,R.raw.wrong,1);
    }

    private void populateQAs(questionAnswer qaObject) {
        if(qaObject.getImage()!=null){
            imgView.setImageBitmap(qaObject.getImage());

        }
        else
        {
            imgView.setImageBitmap(null);
        }
        answerArray.clear();

        textAnsView.setText("");
        textQnView.setText(qaObject.getQuestion());
        textQnView.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        actAnswer = qaObject.getAnswer().trim();
        StringTokenizer st = new StringTokenizer(actAnswer,"\n");
        while (st.hasMoreTokens()) {
            String answerLowerCase= st.nextToken().toLowerCase().trim();
            answerArray.add(answerLowerCase);
        }
        qns++;
        txtIdView.setText("Qn." +String.valueOf((int)qns));
    }


    public void evaluateFunction(View view) {
        String answer= textAnsView.getText().toString();
        answer = answer.trim().toString().toLowerCase();
        String question=  textQnView.getText().toString();
       // textAnsView.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));

        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Result:");

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
   boolean rightOrWrong = false;
        if(answerArray.contains(answer)){
        //if(answer.equals(actAnswer)){
            dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_2;
            text.setText("Right !!!");
            rightOrWrong =true;
            image.setImageResource(R.mipmap.happysmiley);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                    float actualVolume = (float) audioManager
                            .getStreamVolume(AudioManager.STREAM_MUSIC);
                    float maxVolume = (float) audioManager
                            .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    float volume = actualVolume / maxVolume;
                    // Is the sound loaded already?
                    if (loaded) {
                        soundPool.play(rightSound, volume, volume, 1, 0, 1f);
                        Log.e("Test", "Played sound");
                    }
                }
            });
            marks++;
        }
        else{
            rightOrWrong =false;
            dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_3;
            text.setText("Wrong !!!\n Correct answer: \n"+answerArray.get(0));
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                    float actualVolume = (float) audioManager
                            .getStreamVolume(AudioManager.STREAM_MUSIC);
                    float maxVolume = (float) audioManager
                            .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    float volume = actualVolume / maxVolume;
                    // Is the sound loaded already?
                    if (loaded) {
                        soundPool.play(wrongSound, volume, volume, 1, 0, 1f);
                        Log.e("Test", "Played sound");
                    }
                }
            });
            image.setImageResource(R.mipmap.sadsmiley);
        }

        reviewQuestionAnswer rQA = new reviewQuestionAnswer(question,answer,answerArray.get(0),(int)qns,rightOrWrong);
        reviewQAArray.add(rQA);
        dialog.setCanceledOnTouchOutside(false);
dialog.setCancelable(false);
        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();

                if (qaIterator.hasNext()) {
                    populateQAs(qaIterator.next());
                } else {
                        displaymarks();

                }
            }
        });

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 ) {
            finish();
        }

    }

    private void displaymarks() {
        final Dialog dialog1 = new Dialog(userQuiz.this);
        // Include dialog.xml file
        dialog1.setContentView(R.layout.dialog1);
        // Set dialog title
        dialog1.setTitle("Result:");

        // set values for custom dialog components - text, image and button
        TextView text1 = (TextView) dialog1.findViewById(R.id.textDialog);

        text1.setText("You Scored " + marks + " Out of " + qns);
        float markspercent = (100)*(marks/qns);
        text1.append("\n Thats " + markspercent + "% !!\n" );
        ImageView image1 = (ImageView) dialog1.findViewById(R.id.imageDialog);

        if(markspercent>=90 && markspercent<=100){
            text1.append("You are a Champion.Keep up the good work");
            image1.setImageResource(R.mipmap.goldmedal);
        }
        else if(markspercent>=80 && markspercent<=89){
            text1.append("Very good!!!Why dont you aim for a gold medal next time?");
            image1.setImageResource(R.mipmap.silvermedal);
        }
        else if(markspercent>=70 && markspercent<=79){
            text1.append("Good!!!Why dont you aim for a gold medal next time?");
            image1.setImageResource(R.mipmap.bronzemedal);
        }
        else {
            text1.append("You must work hard buddy");
            image1.setImageResource(R.mipmap.encourage);
        }

        dialog1.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_1;
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                float actualVolume = (float) audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                float maxVolume = (float) audioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float volume = actualVolume / maxVolume;
                // Is the sound loaded already?
                if (loaded) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    Log.e("Test", "Played sound");
                }
            }
        });
        dialog1.show();

        Button declineButton1 = (Button) dialog1.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog1.dismiss();
                finish();
            }
        });

        Button reviewQuizButton = (Button) dialog1.findViewById(R.id.reviewBtn);
        reviewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog1.dismiss();
               showReviewScreen();

            }
        });

    }

    private void showReviewScreen() {
        Intent i = new Intent();
        i.setClass(this,reviewquiz.class);
        i.putExtra("Marks",marks);
        i.putExtra("reviewQAArray",reviewQAArray);
        startActivityForResult(i,101);
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public void finishQuiz(View view) {

        if(qns<=1){
            Toast.makeText(this,"Please attempt atleast 2 questions",Toast.LENGTH_SHORT).show();
            return;
        }
        qns--;
        displaymarks();
    }
}





