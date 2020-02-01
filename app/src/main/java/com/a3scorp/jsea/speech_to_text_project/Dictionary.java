package com.a3scorp.jsea.speech_to_text_project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Dictionary extends AppCompatActivity {

    String data;
    EditText editText1;
    Button findButton;


    Document doc;
    DictionaryRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private ArrayList<DictionaryItemObject> list = new ArrayList();

    public String word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);

        editText1 = (EditText)findViewById(R.id.editText1);
        findButton = (Button)findViewById(R.id.find);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = editText1.getText().toString();
                word = data.toLowerCase();

                list.clear();
                //AsyncTask 작동시킴(파싱)
                new Description().execute();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview2);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getApplicationContext(),new LinearLayoutManager(this).getOrientation());

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));





    }

    //웹 파싱
    private class Description extends AsyncTask<Void, Void, Void> {

        String url = "https://alldic.daum.net/search.do?q="+data+"&dic=eng&search_first=Y";
        int i=1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                doc = Jsoup.connect(url).get();

                Elements mElementDataSize = doc.select("div[class=cleanword_type kuek_type]"); //필요한 녀석만 꼬집어서 지정
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.

                Elements mElementDataSize2 = doc.select("div[class=search_type kuek_type]");
                int mElementSize2 = ((Elements) mElementDataSize2).size();


                for(Element elem : mElementDataSize ) {
                    String value2=elem.select("ul[class=list_search]").text();

                    list.add(new DictionaryItemObject(word,value2));
                }



                for(Element elem : mElementDataSize2)
                {
                    String ex1 = elem.select("div[class=search_word]").text();
                    String ex2 = elem.select("ul[class=list_search]").text();

                    list.add(new DictionaryItemObject(ex1,ex2));
                }





            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            recyclerViewAdapter = new DictionaryRecyclerViewAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(recyclerViewAdapter);

        }
    }
}

