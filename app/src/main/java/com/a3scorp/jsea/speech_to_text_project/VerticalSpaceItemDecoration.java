package com.a3scorp.jsea.speech_to_text_project;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//리사이클러뷰 아이템간의 공백
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight){
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 마지막 아이템이 아닌 경우, 공백 추가
        if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() -1 ){
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
