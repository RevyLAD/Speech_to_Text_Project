package com.a3scorp.jsea.speech_to_text_project;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class CustomListViewAdapter extends BaseAdapter {

    Context con;
    LayoutInflater inflater;
    ArrayList<CustomListView> arrayList;
    int layout;


    public CustomListViewAdapter(Context context, int alayout, ArrayList<CustomListView> arrayList1) {
        con = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrayList = arrayList1;
        layout = alayout;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            view = inflater.inflate(layout, viewGroup, false);
        }

        if(arrayList.get(i).comTitle.equals("MR.MinZICK"))
        {
            TextView comtitle = (TextView)view.findViewById(R.id.comTitle);
            //comtitle.setText(arrayList.get(i).comTitle);

            TextView comText = (TextView)view.findViewById(R.id.comText);
            comText.setText(arrayList.get(i).comText);
            comText.setBackgroundResource(R.drawable.leftchat);
            comText.setTextColor(Color.BLACK);

            RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.comText);
            rlp2.setMargins(0,0,20,0);
            comText.setLayoutParams(rlp2);
        }
        else
        {

            TextView comtitle = (TextView)view.findViewById(R.id.comTitle);
            //comtitle.setText(arrayList.get(i).comTitle);

            TextView comText = (TextView)view.findViewById(R.id.comText);
            comText.setText(arrayList.get(i).comText);
            comText.setBackgroundResource(R.drawable.rightchat);
            comText.setTextColor(Color.BLACK);


            /*RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_END, R.id.comTitle);
            comtitle.setLayoutParams(rlp);
            comtitle.setGravity(Gravity.END); */


            RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.comText);
            rlp2.setMargins(0,0,20,0);
            comText.setLayoutParams(rlp2);
        }

        return view;
    }
}

class CustomListView
{
    String comTitle;
    String comText;
    CustomListView(String a, String a2)
    {
        comTitle = a;
        comText = a2;
    }
}