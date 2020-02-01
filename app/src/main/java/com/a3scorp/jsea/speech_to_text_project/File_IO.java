package com.a3scorp.jsea.speech_to_text_project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class File_IO {

    String FILENAME = "check.txt";

    Context context;


    public File_IO(Context context) {
        this.context = context;
    }

    public void createFile()
    {
        File file = new File(context.getFilesDir(), FILENAME);
        if(!file.exists())
        {
            fileWrite("1");
            FirebaseMessaging.getInstance().subscribeToTopic("Arlam");
        }
    }

    public String fileRead()
    {
        String filecheck = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(FILENAME);
            byte[] buffer = new byte[fileInputStream.available()];

            fileInputStream.read(buffer);
             filecheck = new String(buffer);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filecheck;
    }
    public void fileWrite(String str)
    {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(str.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
