package com.vidhyalearning.myquizapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vidhyalearning.myquizapp.ListCategoryFragment.OnListFragmentInteractionListener;
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
public class MyListCategoryRecyclerViewAdapter extends RecyclerView.Adapter<MyListCategoryRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<questionAnswer> mValues;

    private Random mRandom = new Random();
    private String mUserName;
    private Context mContext;
    Vector<Integer>  colorList = new Vector<Integer>();

    public MyListCategoryRecyclerViewAdapter(Context context,ArrayList<questionAnswer> subjectList, String userName) {
        mValues = subjectList;
        mUserName = userName;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_listcategory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mSubjectView.setText(mValues.get(position).getSubject());
        //holder.mContentView.setText(mValues.get(position).content);
        final String getSubject = mValues.get(position).getSubject();
        if(mValues.get(position).getImage() !=null) {
            holder.mImgView.setImageBitmap(mValues.get(position).getImage());
        }
        else
        {
            if(getSubject.equals("Maths"))
            {
                holder.mImgView.setImageResource(R.mipmap.maths);
            }
            else if(getSubject.equals("English")){
                holder.mImgView.setImageResource(R.mipmap.english);
            }
            else if(getSubject.equals("Computer")){
                holder.mImgView.setImageResource(R.mipmap.computer);
            }
            else if(getSubject.equals("Science")){
                holder.mImgView.setImageResource(R.mipmap.science);
            }
            else {
                holder.mImgView.setImageResource(R.mipmap.ic_launcher);
            }
        }
        holder.mView.setBackgroundColor(getRandomHSVColor());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                /*if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }*/
                //Toast.makeText(holder.mView.getActivity(),holder.mSubjectView.getText().toString(),Toast.LENGTH_LONG).show();
                if(getSubject == "+"){
                    addSubject(holder);
                }
                else {
                    displayScreen(holder);
                }

            }
        });
    }

    private void addSubject(ViewHolder holder) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayScreen(ViewHolder holder) {
        Pair[] pair = new Pair[2];
        pair[0] = new Pair<View,String>( holder.mSubjectView,"expandSubject");
        pair[1]=new Pair<View,String>(holder.mImgView,"expandImage");

        ActivityOptions options =null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {


            options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, pair);
        }
        Intent intent = new Intent();
        Bitmap _bitmap = ((BitmapDrawable) holder.mImgView.getDrawable()).getBitmap(); // your bitmap
        ByteArrayOutputStream _bs = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
        intent.putExtra("byteArray", _bs.toByteArray());
        intent.putExtra("subjectArray",holder.mSubjectView.getText().toString());
        intent.putExtra("Username",mUserName);
        int bgcolor =( (ColorDrawable)holder.mView.getBackground()).getColor();

        intent.putExtra("bgColor",bgcolor);

        intent.setClass(holder.mView.getContext(),OpenCategory.class);
        if(options!=null)
        holder.mView.getContext().startActivity(intent,options.toBundle());
        else
            holder.mView.getContext().startActivity(intent);
    }

    // Custom method to generate random HSV color
    protected int getRandomHSVColor(){
        // Generate a random hue value between 0 to 360
        int hue = mRandom.nextInt(361);
        int attempt=1;
        if(colorList.contains(hue) == true) {
            while(colorList.contains(hue) && attempt<=3)
            {
                hue = mRandom.nextInt(361);
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
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSubjectView;
        //public final TextView mContentView;
        public questionAnswer mItem;
        public final ImageView mImgView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSubjectView = (TextView) view.findViewById(R.id.categorytext);
           // mContentView = (TextView) view.findViewById(R.id.content);

            mImgView = (ImageView)view.findViewById(R.id.subjectImgView);
        }

       // @Override
        //public String toString() {
          //  return super.toString() + " '" + mContentView.getText() + "'";

    }
}
