package com.a3scorp.jsea.speech_to_text_project;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DictionaryRecyclerViewAdapter extends RecyclerView.Adapter<DictionaryRecyclerViewAdapter.ViewHolder>  {


    private ArrayList<DictionaryItemObject> mList;

    public  class ViewHolder extends RecyclerView.ViewHolder {

        private TextView value1, value2;


        public ViewHolder(View itemView) {

            super(itemView);
            value1 = itemView.findViewById(R.id.value1);
            value2 = itemView.findViewById(R.id.value2);


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
    public DictionaryRecyclerViewAdapter(ArrayList<DictionaryItemObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public DictionaryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dictionary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.value1.setText(String.valueOf(mList.get(position).getValue1()));
        holder.value2.setText(String.valueOf(mList.get(position).getValue2()));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
