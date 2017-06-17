package com.vidhyalearning.myquizapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vidhyalearning.myquizapp.questionFragment.OnListFragmentInteractionListener;
import com.vidhyalearning.myquizapp.dummy.DummyContent.DummyItem;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyquestionRecyclerViewAdapter extends RecyclerView.Adapter<MyquestionRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<questionAnswer> mValues;
    private final OnListFragmentInteractionListener mListener;
    private List<questionAnswer> dictionaryWords ;
    private List<questionAnswer> filteredList ;
    private Random mRandom = new Random();
    private CustomFilter mFilter;
    Vector<Integer> colorList = new Vector<Integer>();
    public MyquestionRecyclerViewAdapter(ArrayList<questionAnswer> qaList, OnListFragmentInteractionListener listener) {
        mValues = qaList;
        mListener = listener;
        dictionaryWords = new ArrayList<questionAnswer>();
        //filteredList = new ArrayList<questionAnswer>();
        filteredList = qaList;
        dictionaryWords.addAll(qaList);
        mFilter = new CustomFilter(MyquestionRecyclerViewAdapter.this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_question, parent, false);

        return new ViewHolder(view);
    }
    // Custom method to generate random HSV color
    protected int getRandomHSVColor(){
        // Generate a random hue value between 0 to 360
        int hue = mRandom.nextInt(361);
        int attempt=1;
        if(colorList.contains(hue) == true) {
            while(colorList.contains(hue) && attempt <=3)
            {
                hue = mRandom.nextInt(361);
                attempt++;
            }

        }

        colorList.add(hue);

        // We make the color depth full
        float saturation = 1.0f;
        // We make a full bright color
        float value = 1.0f;
        // We avoid color transparency
        int alpha = 255;
        // Finally, generate the color
        int color = Color.HSVToColor(alpha, new float[]{hue, saturation, value});
        // Return the color
        return color;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.mView.setBackgroundColor(getRandomHSVColor());
        holder.qaObject = mValues.get(position);

        holder.mQnView.setText(  mValues.get(position).getQuestion());
        holder.mAnsView.setText( mValues.get(position).getAnswer());
        holder.mLevelView.setText(mValues.get(position).getLevel());
        holder.mSubjectView.setText( mValues.get(position).getSubject());
        holder.mPosView.setText(String.valueOf(position+1));
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
        holder.mIdView.setVisibility(View.INVISIBLE);
        if(mValues.get(position).getImage() !=null) {
            holder.mImgView.setImageBitmap(mValues.get(position).getImage());
        }

        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                displayScreen(holder);
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayScreen(MyquestionRecyclerViewAdapter.ViewHolder holder) {

        Intent intent = new Intent();
        if(holder.mImgView.getDrawable()!=null) {
            Bitmap _bitmap = ((BitmapDrawable) holder.mImgView.getDrawable()).getBitmap(); // your bitmap
            ByteArrayOutputStream _bs = new ByteArrayOutputStream();
            _bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
            intent.putExtra("byteArray", _bs.toByteArray());
        }
        intent.putExtra("subjectArray",holder.mSubjectView.getText().toString());
        intent.putExtra("id",holder.mIdView.getText().toString());
        intent.putExtra("question",holder.mQnView.getText().toString());
        intent.putExtra("answer",holder.mAnsView.getText().toString());
        int bgcolor =( (ColorDrawable)holder.mView.getBackground()).getColor();
        intent.putExtra("bgColor",bgcolor);
        intent.putExtra("level",holder.mLevelView.getText().toString());
        intent.setClass(holder.mView.getContext(),editQuestion.class);
        holder.mView.getContext().startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mQnView;
        public final TextView mAnsView;
        public final TextView mLevelView;
        public final TextView mIdView,mPosView;
        public final TextView mSubjectView;
       public final ImageView mImgView;
        public final Button mEditButton;
        public questionAnswer qaObject;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mQnView = (TextView) view.findViewById(R.id.txtquestion);
            mAnsView = (TextView) view.findViewById(R.id.txtAnswer);
            mLevelView=(TextView) view.findViewById(R.id.txtLevel);
            mImgView =(ImageView) view.findViewById(R.id.imgListView);
            mIdView=(TextView) view.findViewById(R.id.txtId);
            mPosView=(TextView) view.findViewById(R.id.txtPosition);
            mSubjectView=(TextView) view.findViewById(R.id.txtSubject);
            mEditButton = (Button)view.findViewById(R.id.editQnButton);
        }
        }

    public class CustomFilter extends Filter {
        protected MyquestionRecyclerViewAdapter mAdapter;
        protected CustomFilter(MyquestionRecyclerViewAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(dictionaryWords);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final questionAnswer mWords : dictionaryWords) {
                    if (mWords.getQuestion().toLowerCase().contains(filterPattern) ||
                            mWords.getAnswer().toLowerCase().contains(filterPattern) ||
                            mWords.getLevel().toLowerCase().contains(filterPattern)
                            ) {
                        filteredList.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           System.out.println("Count Number 2 " + ((List<questionAnswer>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
