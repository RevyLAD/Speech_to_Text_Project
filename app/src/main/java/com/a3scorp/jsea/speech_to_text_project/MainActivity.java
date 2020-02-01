package com.a3scorp.jsea.speech_to_text_project;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String FILENAME = "chat.txt";

    ArrayList<CustomListView> arrayList = new ArrayList<CustomListView>();
    ArrayList<String> items = new ArrayList<String>() ;

    private String mSelectedString;
    private ArrayList<String> mResult;
            CustomListView customListView;
            CustomListViewAdapter adapter;
    private BackPressCloseHandler backPressCloseHandler;



    String minjik = "";

    static TextToSpeech tts;

    ListView listView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("ASDF", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("ASDF", msg);
                    }
                });

        // [END retrieve_current_token]
        backPressCloseHandler = new BackPressCloseHandler(this);


        listView = findViewById(R.id.listview);


        url = "http://jsea.myq-see.com/download/getChat.php";

        checkPermission();

        ImageButton button = findViewById(R.id.button);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 듣기를 시작합니다.

                startRecord();
            }
        });

        readFromFile();
        adapter = new CustomListViewAdapter(getApplicationContext(), R.layout.list_view_item, arrayList);
        listView.setAdapter(adapter);
        listView.setSelection(adapter.getCount() - 1);


        registerForContextMenu(listView);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressd();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        Log.d("test","onCreateOptionsMenu");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Log.d("test","onOptionsItemSelected");

        int id = item.getItemId();

        switch (id) {
            case R.id.search://사전 검색
                Intent intent1 = new Intent(MainActivity.this,Dictionary.class);
                startActivity(intent1);
                return true;
            case R.id.delete://대화내용지우기
                deleteFile();
                return true;
            case R.id.daily://일일 대화/문장
                Intent intent2 = new Intent(MainActivity.this,Parsing.class);
                startActivity(intent2);
                return true;
            case R.id.option://설정
                Intent intent3 = new Intent(MainActivity.this, OptionsMenu.class);
                startActivity(intent3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void deleteFile()
    {
        final AlertDialog.Builder alertDialg = new AlertDialog.Builder(this);
        alertDialg.setTitle("대화 내용 삭제 도우미");
        alertDialg.setMessage("정말로 대화 내용을 삭제 하시겠습니까?");
        alertDialg.setCancelable(false);
        alertDialg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialg.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file = new File(getFilesDir(), FILENAME);
                if( file.exists() ){
                    if(file.delete()){
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "정상적으로 대화내용을 삭제합니다.", Toast.LENGTH_SHORT).show();
                        items.clear();
                    }else{
                        System.out.println("파일삭제 실패");
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "대화 내용이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("파일이 존재하지 않습니다.");
                }

            }
        });
        alertDialg.show();
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        getMenuInflater().inflate(R.menu.menu,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int index = info.position;
        String text = arrayList.get(index).comTitle;
        String text3 = null;

        String dialogPop;

        switch( item.getItemId() ){

            case R.id.modify: //번역보기

                if(text.equals("MR.MinZICK"))
                {
                    try {
                        text3 = arrayList.get(index).comText;

                        download_sql d = new download_sql(getApplicationContext(), url, listView, text3, 1);
                        dialogPop = d.execute().get();

                        AlertDialog ad = new AlertDialog.Builder(this).setTitle("번역 도움말")
                                .setMessage(dialogPop)
                                .setCancelable(false)
                                .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        ad.show();

                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    text3 = arrayList.get(index).comText;
                }

                //NaverTranslateTask asyncTask = new NaverTranslateTask();
                //asyncTask.execute(text3);

                break;
            case R.id.info:

                if(text.equals("MR.MinZICK"))
                {
                    text3 = arrayList.get(index).comText;
                }
                else
                {
                    text3 = arrayList.get(index).comText;
                }
                tts.speak(text3, TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
        return true;

    }


    public void checkPermission()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            //퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    public void readFromFile()
    {
        File file = new File(getFilesDir(), FILENAME);
        FileReader fr = null;
        BufferedReader bufrd = null;
        boolean compare = false;
        String str;
        if (file.exists()) {
            try { // open file.
                fr = new FileReader(file);
                bufrd = new BufferedReader(fr);
                while ((str = bufrd.readLine()) != null) {
                    if(!compare)
                    {
                        customListView = new CustomListView( "나", str);
                        arrayList.add(customListView);
                        items.add(str);
                        Log.d("readFromFile", "ME: "+str);
                        compare = true;
                    }
                    else
                    {
                        customListView = new CustomListView("MR.MinZICK", str);
                        arrayList.add(customListView);
                        items.add(str);
                        Log.d("readFromFile", "MR.MinZICK: "+str);
                        compare = false;
                    }
                }
                bufrd.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void writeFromFile()
    {
        File file = new File(getFilesDir(), FILENAME);
        FileWriter fw = null ;
        BufferedWriter bufwr = null ;
        try {
            fw = new FileWriter(file);
            bufwr = new BufferedWriter(fw);
            for(String str : items) {
                bufwr.write(str);
                bufwr.newLine();
            }
            bufwr.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            // close file.
            if (bufwr != null)
            {
                bufwr.close();
            }
            if (fw != null)
            {
                fw.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
        }
    }

    public void startRecord()
    {
        startActivityForResult(new Intent(this, CustomUIActivity.class), 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if( resultCode == RESULT_OK  &&  requestCode == 1000){		//결과가 있으면
            showSelectDialog(requestCode, data);				//결과를 다이얼로그로 출력.
        }
        else{															//결과가 없으면 에러 메시지 출력
            String msg = null;

            //내가 만든 activity에서 넘어오는 오류 코드를 분류
            switch(resultCode){
                case SpeechRecognizer.ERROR_AUDIO:
                    msg = "오디오 입력 중 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    msg = "단말에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    msg = "권한이 없습니다.";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    msg = "네트워크 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    msg = "일치하는 항목이 없습니다.";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    msg = "음성인식 서비스가 과부하 되었습니다.";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    msg = "서버에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    msg = "입력이 없습니다.";
                    break;
            }
            if(msg != null)		//오류 메시지가 null이 아니면 메시지 출력
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
        if( resultCode == RESULT_OK  &&  requestCode == 1001){		//결과가 있으면
            showAnswerDialog(requestCode, data);				//결과를 다이얼로그로 출력.
        }
    }

    //결과 list 출력하는 다이얼로그 생성
    private void showSelectDialog(int requestCode, Intent data){
        if(requestCode == 1000) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;    //키값 설정

            mResult = data.getStringArrayListExtra(key);        //인식된 데이터 list 받아옴.
            String[] result = new String[mResult.size()];            //배열생성. 다이얼로그에서 출력하기 위해
            mResult.toArray(result);                                    //	list 배열로 변환

            //1개 선택하는 다이얼로그 생성
            AlertDialog ad = new AlertDialog.Builder(this).setTitle("어떤것이 가장 정확한가요?")
                    .setSingleChoiceItems(result, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSelectedString = mResult.get(which);        //선택하면 해당 글자 저장
                        }
                    })
                    .setPositiveButton("선택하기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                download_sql d = new download_sql(getApplicationContext(), url, listView, mSelectedString, 0);
                                minjik = d.execute().get();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            customListView = new CustomListView("나", mSelectedString);
                            arrayList.add(customListView);
                            items.add(mSelectedString);

                            customListView = new CustomListView("MR.MinZICK", minjik);
                            arrayList.add(customListView);
                            items.add(minjik);
                            writeFromFile();

                            adapter.notifyDataSetChanged();
                            tts.speak(minjik, TextToSpeech.QUEUE_FLUSH, null);

                            if(minjik.equals("Sorry.")) // 패턴 인식 시작.
                            {
                                showFuckingDialog();
                            }

                        }
                    })
                    .setNegativeButton("다시 말하기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(MainActivity.this, CustomUIActivity.class), 1000);
                        }
                    }).create();
            ad.setCancelable(false);
            ad.show();
        }
    }

    //결과 list 출력하는 다이얼로그 생성
    private void showAnswerDialog(int requestCode, Intent data){
        if(requestCode == 1001) {
            final String tempData = mSelectedString;
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;    //키값 설정

            mResult = data.getStringArrayListExtra(key);        //인식된 데이터 list 받아옴.
            String[] result = new String[mResult.size()];            //배열생성. 다이얼로그에서 출력하기 위해
            mResult.toArray(result);                                    //	list 배열로 변환

            //1개 선택하는 다이얼로그 생성
            AlertDialog ad = new AlertDialog.Builder(this).setTitle("어떤것이 가장 정확한가요?")
                    .setSingleChoiceItems(result, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSelectedString = mResult.get(which);        //선택하면 해당 글자 저장
                        }
                    })
                    .setPositiveButton("선택하기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 여기서 제이에스피로 접속 해야돼 씨발

                            AddDataBase task = new AddDataBase();
                            try {
                                task.execute(tempData,mSelectedString, "add");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .setNegativeButton("다시 말하기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(MainActivity.this, CustomUIActivity.class), 1001);
                        }
                    }).create();
            ad.setCancelable(false);
            ad.show();
        }
    }

    //네이버 기계번역 API (Papago SMT 번역)
    public class NaverTranslateTask extends AsyncTask<String, Void, String> {

        public String resultText;
        //Naver
        String clientId = "eVdYJsh2n6EewKJcpcxe";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "3MwzBdo9u8";//애플리케이션 클라이언트 시크릿값";
        //언어선택도 나중에 사용자가 선택할 수 있게 옵션 처리해 주면 된다.
        String sourceLang = "en";
        String targetLang = "ko";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //AsyncTask 메인처리
        @Override
        protected String doInBackground(String... strings) {
            //네이버제공 예제 복사해 넣자.
            //Log.d("AsyncTask:", "1.Background");

            String sourceText = strings[0];

            try {
                String text = URLEncoder.encode(sourceText, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/language/translate";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                // post request
                String postParams = "source="+sourceLang+"&target="+targetLang+"&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                //System.out.println(response.toString());
                return response.toString();

            } catch (Exception e) {
                //System.out.println(e);
                Log.d("error", e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //최종 결과 처리부
            //Log.d("background result", s.toString()); //네이버에 보내주는 응답결과가 JSON 데이터이다.

            //JSON데이터를 자바객체로 변환해야 한다.
            //Gson을 사용할 것이다.

            Gson gson = new GsonBuilder().create();
            JsonParser parser = new JsonParser();
            JsonElement rootObj = parser.parse(s)
                    //원하는 데이터 까지 찾아 들어간다.
                    .getAsJsonObject().get("message")
                    .getAsJsonObject().get("result");
            //안드로이드 객체에 담기
            TranslatedItem items = gson.fromJson(rootObj.toString(), TranslatedItem.class);
            //Log.d("result", items.getTranslatedText());
            //번역결과를 텍스트뷰에 넣는다.
            Toast.makeText(MainActivity.this, items.getTranslatedText(), Toast.LENGTH_LONG).show();
        }


        //자바용 그릇
        private class TranslatedItem {
            String translatedText;

            public String getTranslatedText() {
                return translatedText;
            }
        }
    }


    private void showFuckingDialog() {

        AlertDialog ads = new AlertDialog.Builder(this)
                .setTitle("답변 설정해주실래요?")
                .setMessage("질문: "+ mSelectedString+"\n답변 : __________")
                .setNegativeButton("아니요, 안할래요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("네, 답변할래요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(MainActivity.this, CustomUIActivity.class), 1001);

                    }
                }).create();
        ads.setCancelable(false);
        ads.show();

        /* 1. 타이틀 : 데이터 베이스에 답변 삽입 하시겠습니까?
            2. Q : 질문
            3. A : _______
               아니오, 안할래요. 네 -> 스타트 엑비티티 폴 리절트 해서 답 선택 해가지고 설정 해주기.

         */
    }
}
