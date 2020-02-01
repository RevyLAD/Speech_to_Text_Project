package com.a3scorp.jsea.speech_to_text_project;

import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ParsingRecyclerViewAdapter extends RecyclerView.Adapter<ParsingRecyclerViewAdapter.ViewHolder>  {
    ImageView imageView_img;
    ImageButton listenButton;
    //데이터 배열 선언
    private ArrayList<ParsingItemObject> mList;

    public  class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_title, textView_release, texView_director;


        public ViewHolder(View itemView) {

            super(itemView);
            imageView_img =  itemView.findViewById(R.id.imageView_img);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_release = itemView.findViewById(R.id.textView_release);
            texView_director = itemView.findViewById(R.id.textView_director);
            listenButton = itemView.findViewById(R.id.listening);
            imageView_img.setImageResource(R.drawable.ic_person_black_24dp);

            listenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = mList.get(getAdapterPosition()).getRelease();
                    Parsing.tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
            });

            for(int i = 0; i < mList.size(); i = i+2)
            {

                if(i+1 == mList.size()) {
                    break;
                }
                else {
                    addSentence task = new addSentence();
                    task.execute(mList.get(i).getRelease(), mList.get(i + 1).getRelease(), mList.get(i + 1).getTitle(), "add2");
                }
            }
        }
    }

    public void delete() {

        try {

            mList.clear();
            notifyDataSetChanged();

        } catch (IndexOutOfBoundsException ex) {

            ex.printStackTrace();

        }
    }

    //생성자
    public ParsingRecyclerViewAdapter(ArrayList<ParsingItemObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public ParsingRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParsingRecyclerViewAdapter.ViewHolder holder,int position) {

        holder.textView_title.setText(String.valueOf(mList.get(position).getTitle()));
        holder.textView_release.setText(String.valueOf(mList.get(position).getRelease()));
        holder.texView_director.setText(String.valueOf(mList.get(position).getDirector()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}

