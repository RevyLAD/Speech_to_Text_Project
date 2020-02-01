package com.a3scorp.jsea.speech_to_text_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class download_sql extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;
    ListView lv;
    String search;
    int Type;

    public download_sql(Context c, String address, ListView lv, String search, int Type) {
        this.c = c;
        this.address = address;
        this.lv = lv;
        this.search = search;
        this.Type = Type;
    }

    //B4 JOB STARTS
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String data = downloadData();
        String text = "Sorry.";

        int value = 0;

        if (data != null) {
                /*Parser_sql p = new Parser_sql(c, s, lv, search);
                p.execute();*/
            try
            {
                //ADD THAT DATA TO JSON ARRAY FIRST
                JSONArray ja = new JSONArray(data);

                //CREATE JO OBJ TO HOLD A SINGLE ITEM
                JSONObject jo=null;

                //arrayList.clear();

                //LOOP THRU ARRAY

                for(int i=0;i<ja.length();i++)
                {
                    jo=ja.getJSONObject(i);

                    String title = jo.getString("question");
                    String answer = jo.getString("answer");
                    if((!(search.isEmpty()) && title.toLowerCase().contains(search.toLowerCase())) && Type == 0) {
                        text = jo.getString("answer");
                        value = 1;
                        break;
                    }
                    else if((!(search.isEmpty()) && answer.toLowerCase().contains(search.toLowerCase())) && Type == 1)
                    {
                        text = jo.getString("translate");
                        value = 1;
                        break;
                    }

                }
                if(value != 1 && Type == 0)
                    text = "Sorry.";
                else if(value != 1 && Type == 1)
                {
                    text = "죄송합니다. 번역을 못했어요 ㅠㅠ";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(c, "데이터를 다운로드 하는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
        return text;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

//        pd.dismiss();

    }

    private String downloadData() {
        //connect and get a stream
        InputStream is = null;
        String line = null;

        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(con.getInputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuffer sb = new StringBuffer();

            if (br != null) {

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

            } else {
                return null;
            }

            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}