package com.a3scorp.jsea.speech_to_text_project;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class OptionsRecyclerViewAdapter extends RecyclerView.Adapter<OptionsRecyclerViewAdapter.ViewHolder>  {


    //데이터 배열 선언
    private ArrayList<OptionsItemObject> mList;

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_mainText, textView_subText;

        public ViewHolder(View view) {

            super(view);
            textView_mainText = view.findViewById(R.id.mainText);
            textView_subText = view.findViewById(R.id.subText);


            /*
            //리사이클러뷰 클릭리스너
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"위치는:"+getAdapterPosition(),Toast.LENGTH_SHORT).show();
                }
            });
            */
        }
    }

    //생성자
    public OptionsRecyclerViewAdapter(ArrayList<OptionsItemObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public OptionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_item, parent, false);
        return new OptionsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OptionsRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.textView_mainText.setText(String.valueOf(mList.get(position).getMainText()));
        holder.textView_subText.setText(String.valueOf(mList.get(position).getSubText()));

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

}
