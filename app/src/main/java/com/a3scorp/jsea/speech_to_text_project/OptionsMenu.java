package com.a3scorp.jsea.speech_to_text_project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class OptionsMenu extends AppCompatActivity {

    OptionsRecyclerViewAdapter recyclerViewAdapter;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<OptionsItemObject> list = new ArrayList();
    String check;

    Switch arlam;


    File_IO file_io = new File_IO(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);

        file_io.createFile();
        check = file_io.fileRead();

        arlam = findViewById(R.id.switch_button);

        recyclerView = (RecyclerView) findViewById(R.id.option_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        list.add(new OptionsItemObject("개발팀","강원대학교 삼척캠퍼스 컴퓨터 공학과 팀 JSEA/3S"));
        list.add(new OptionsItemObject("개발자","컴퓨터 공학과 서석빈\n컴퓨터 공학과 함성민"));


        OptionsRecyclerViewAdapter optionsRecyclerViewAdapter = new OptionsRecyclerViewAdapter(list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(48));
        recyclerView.setAdapter(optionsRecyclerViewAdapter);

        if(check.equals("1"))
        {
            arlam.setChecked(true);
        }
        else
        {
            arlam.setChecked(false);
        }

        arlam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    file_io.fileWrite("1");
                    FirebaseMessaging.getInstance().subscribeToTopic("Arlam")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = "구독 성공";
                                    if (!task.isSuccessful()) {
                                        msg = "구독 실패";
                                    }
                                    Log.d("Subscribe", msg);
                                    Toast.makeText(OptionsMenu.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    file_io.fileWrite("0");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Arlam")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = "구독 취소";
                                    if (!task.isSuccessful()) {
                                        msg = "구독 취소 실패";
                                    }
                                    Log.d("Subscribe", msg);
                                    Toast.makeText(OptionsMenu.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
