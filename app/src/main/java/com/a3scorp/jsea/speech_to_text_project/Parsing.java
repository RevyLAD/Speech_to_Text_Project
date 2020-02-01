package com.a3scorp.jsea.speech_to_text_project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


public class Parsing extends AppCompatActivity {
    ParsingRecyclerViewAdapter recyclerViewAdapter;

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;

    int big=0;
    int middle=0;
    int small=0;

    Document doc;
    String in_doc="https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=21&categoryTypeCode=0&targetLanguage=en";
    Elements mElementDataSize;
    private RecyclerView recyclerView;
    private ArrayList<ParsingItemObject> list = new ArrayList();
    static TextToSpeech tts;

    ArrayAdapter<CharSequence> spin1, spin2, spin3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview);


        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);

        spin1 = ArrayAdapter.createFromResource(this, R.array.대분류,android.R.layout.simple_spinner_dropdown_item);
        spin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2 =  ArrayAdapter.createFromResource(this, R.array.중분류,android.R.layout.simple_spinner_dropdown_item);
        spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3 =  ArrayAdapter.createFromResource(this, R.array.소분류,android.R.layout.simple_spinner_dropdown_item);
        spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(spin1); //대분류
        spinner2.setAdapter(spin2); //중분류
        spinner3.setAdapter(spin3); //소분류

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if (spin1.getItem(position).equals("기본표현")) {
                   spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_중분류, android.R.layout.simple_spinner_dropdown_item);
                   spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   spinner2.setAdapter(spin2);

                   spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           switch (position) {
                               //아이템의 위치에따라
                               case 0:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_인사안부_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 21;
                                   small = 145 - 1;
                                   break;
                               case 1:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_부탁요청수락거절_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 22;
                                   small = 153 - 1;
                                   break;
                               case 2:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_축하칭찬_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 23;
                                   small = 156 - 1;
                                   break;
                               case 3:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_감사_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 24;
                                   small = 159 - 1;
                                   break;
                               case 4:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_사과용서양보_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 25;
                                   small = 162 - 1;
                                   break;
                               case 5:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_신상성향_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 26;
                                   small = 165 - 1;
                                   break;
                               case 6:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_행동지시_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 27;
                                   small = 175 - 1;
                                   break;
                               case 7:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_질문대답_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 28;
                                   small = 181 - 1;
                                   break;
                               case 8:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_숫자_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 29;
                                   small = 0;
                                   break;
                               case 9:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_시각_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 30;
                                   small = 0;
                                   break;
                               case 10:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_시간_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 31;
                                   small = 0;
                                   break;
                               case 11:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_날짜_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 32;
                                   small = 186 - 1;
                                   break;
                               case 12:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_날씨_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 33;
                                   small = 189 - 1;
                                   break;
                               case 13:
                                   spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.기본표현_숫자날짜단어장_소분류, android.R.layout.simple_spinner_dropdown_item);
                                   middle = 532;
                                   small = 0;
                                   break;
                               default:
                                   break;
                           }
                           spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                           spinner3.setAdapter(spin3);

                           spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                               @Override
                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                   for (int i = 0; i <= position; i++) {
                                       if (i == position) {
                                           if (i == 0) //자주쓰이는 표현
                                           {
                                               in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                               //recyclerViewAdapter.delete();
                                               new Description().execute();
                                               break;
                                           }
                                           //나머지 소분류들
                                           small = small + position;
                                           in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                           recyclerViewAdapter.delete();
                                           new Description().execute();
                                           small = small - position;
                                           break;
                                       }
                                   }
                               }

                               @Override
                               public void onNothingSelected(AdapterView<?> parent) {
                               }
                           });

                       }

                       @Override
                       public void onNothingSelected(AdapterView<?> parent) {
                       }
                   });
               }

                if (spin1.getItem(position).equals("공항/비행기")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.공항비행기_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공항비행기_항공권예약_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 34;
                                    small = 191 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공항비행기_발권_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 35;
                                    small = 195 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공항비행기_출입국수속_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 36;
                                    small = 198 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공항비행기_비행기내_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 37;
                                    small = 202 - 1;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공항비행기_비행기환승연착_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 38;
                                    small = 209 - 1;
                                    break;
                                case 5:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공항비행기_수하물환전공항이동_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 39;
                                    small = 212 - 1;
                                    break;
                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("숙박")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.숙박_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.숙박_예약체크인_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 40;
                                    small = 216 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.숙박_서비스_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 41;
                                    small = 218 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.숙박_체크아웃_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 42;
                                    small = 0;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("식당")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_식사제의_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 43;
                                    small = 0;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_예약입구_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 45;
                                    small = 222 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_주문_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 46;
                                    small = 225 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_식사_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 47;
                                    small = 233 - 1;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_불편사항_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 48;
                                    small = 239 - 1;
                                    break;
                                case 5:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_계산_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 49;
                                    small = 242 - 1;
                                    break;
                                case 6:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_패스트푸드_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 50;
                                    small = 245 - 1;
                                    break;
                                case 7:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_카페_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 51;
                                    small = 247 - 1;
                                    break;
                                case 8:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.식당_술집바_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 52;
                                    small = 250 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("쇼핑")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_위치찾기_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 53;
                                    small = 0;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_물건찾기고르기_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 54;
                                    small = 259 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_가격흥정_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 55;
                                    small = 266 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_계산포장배달_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 56;
                                    small = 269 - 1;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_교환환불_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 57;
                                    small = 0;
                                    break;
                                case 5:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_면세점_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 58;
                                    small = 0;
                                    break;
                                case 6:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_옷가게신발가게_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 59;
                                    small = 274 - 1;
                                    break;
                                case 7:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_화장품점_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 60;
                                    small = 0;
                                    break;
                                case 8:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_마트_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 61;
                                    small = 278 - 1;
                                    break;
                                case 9:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.쇼핑_서점문구점_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 62;
                                    small = 0;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("교통")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.교통_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.교통_길찾기_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 63;
                                    small = 280 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.교통_택시_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 64;
                                    small = 284 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.교통_버스_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 65;
                                    small = 288 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.교통_지하철_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 66;
                                    small = 294 - 1;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.교통_기차배_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 67;
                                    small = 300 - 1;
                                    break;
                                case 5:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.교통_렌터카_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 68;
                                    small = 305 - 1;
                                    break;
                                case 6:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.교통_운전_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 69;
                                    small = 309 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("관광")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.관광_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.관광_관광정보_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 70;
                                    small = 317 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.관광_촬영_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 71;
                                    small = 323 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("엔터테인먼트")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.엔터테인먼트_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.엔터테인먼트_티켓_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 72;
                                    small = 0;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.엔터테인먼트_좌석_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 73;
                                    small = 325 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.엔터테인먼트_극장공연장_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 74;
                                    small = 329 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.엔터테인먼트_박물관미술관_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 75;
                                    small = 0;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.엔터테인먼트_놀이공원피크닉_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 76;
                                    small = 332 - 1;
                                    break;
                                case 5:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.엔터테인먼트_카지노_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 77;
                                    small = 335 - 1;
                                    break;
                                case 6:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.엔터테인먼트_경기장_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 78;
                                    small = 340 - 1;
                                    break;
                                case 7:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.엔터테인먼트_골프장_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 79;
                                    small = 345 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("전화")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.전화_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.전화_일반전화통화_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 80;
                                    small = 349 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.전화_부재중메시지전달_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 81;
                                    small = 354 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.전화_공중전화국제전화_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 82;
                                    small = 361 - 1;
                                    break;
                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(spin3.getItem(position).equals("부재중"))
                                    {
                                        in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=354&categoryTypeCode=0&targetLanguage=en";
                                        recyclerViewAdapter.delete();
                                        new Description().execute();
                                    }

                                    else if(spin3.getItem(position).equals("메시지 부탁"))
                                    {
                                        in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=357&categoryTypeCode=0&targetLanguage=en";
                                        recyclerViewAdapter.delete();
                                        new Description().execute();
                                    }

                                    else if(spin3.getItem(position).equals("전화 연결"))
                                    {
                                        in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=359&categoryTypeCode=0&targetLanguage=en";
                                        recyclerViewAdapter.delete();
                                        new Description().execute();
                                    }

                                    else if(spin3.getItem(position).equals("메시지전달 받은 후"))
                                    {
                                        in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=360&categoryTypeCode=0&targetLanguage=en";
                                        recyclerViewAdapter.delete();
                                        new Description().execute();
                                    }

                                    else {
                                        for (int i = 0; i <= position; i++) {
                                            if (i == position) {
                                                if (i == 0) //자주쓰이는 표현
                                                {
                                                    in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                    recyclerViewAdapter.delete();
                                                    new Description().execute();
                                                    break;
                                                }
                                                //나머지 소분류들
                                                small = small + position;
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                small = small - position;
                                                break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("감정/의견표현")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_기쁨즐거움_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 83;
                                    small = 364 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_슬픔걱정위로충고_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 84;
                                    small = 366 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_실망비난변명_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 85;
                                    small = 371 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_화짜증_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 86;
                                    small = 0;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_긴장두려움_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 87;
                                    small = 0;
                                    break;
                                case 5:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_맞장구농담비밀_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 88;
                                    small = 373 - 1;
                                    break;
                                case 6:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_다툼화해_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 89;
                                    small = 0;
                                    break;
                                case 7:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_오해후회_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 90;
                                    small = 377 - 1;
                                    break;
                                case 8:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_동의찬성반대_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 91;
                                    small = 379 - 1;
                                    break;
                                case 9:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.감정의견표현_화제전환말잇기_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 92;
                                    small = 384 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("생활/가정")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_이성소개_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 93;
                                    small = 388 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_데이트사랑_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 94;
                                    small = 390 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_결혼_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 95;
                                    small = 396 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_가정생활_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 96;
                                    small = 402 - 1;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_요리_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 97;
                                    small = 411 -1;
                                    break;
                                case 5:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_초대파티_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 98;
                                    small = 415 - 1;
                                    break;
                                case 6:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_약속_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 99;
                                    small = 427 - 1;
                                    break;
                                case 7:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_일상생활_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 100;
                                    small = 432 - 1;
                                    break;
                                case 8:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.생활가정_재정문제_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 101;
                                    small = 438 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("직장")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.직장_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.직장_구직_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 124;
                                    small = 521 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.직장_업무_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 125;
                                    small = 526 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.직장_거래_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 126;
                                    small = 0;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.직장_기타_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 127;
                                    small = 530 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("병원/약국")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.병원약국_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.병원약국_예약진찰_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 102;
                                    small = 441 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.병원약국_내과이비인후과_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 103;
                                    small = 447 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.병원약국_피부과비뇨기과_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 104;
                                    small = 449 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.병원약국_외과신경외과_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 105;
                                    small = 451 - 1;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.병원약국_산부인과소아과_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 106;
                                    small = 454 - 1;
                                    break;
                                case 5:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.병원약국_안과치과_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 107;
                                    small = 456 - 1;
                                    break;
                                case 6:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.병원약국_문병_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 108;
                                    small = 458 - 1;
                                    break;
                                case 7:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.병원약국_약구입_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 109;
                                    small = 460 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("공공기관")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.공공기관_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공공기관_학교_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 110;
                                    small = 463 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공공기관_도서관_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 111;
                                    small = 472 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공공기관_은행_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 112;
                                    small = 474 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공공기관_우체국_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 113;
                                    small = 479 - 1;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.공공기관_일반관공서_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 114;
                                    small = 482 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("편의시설")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_부동산_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 115;
                                    small = 484 - 1;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_미용실_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 116;
                                    small = 490 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_세탁소_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 117;
                                    small = 496 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_주유소_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 118;
                                    small = 500 - 1;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_카센터_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 119;
                                    small = 503 - 1;
                                    break;
                                case 5:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_헬스클럽_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 120;
                                    small = 507 - 1;
                                    break;
                                case 6:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_놀이방_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 121;
                                    small = 510 - 1;
                                    break;
                                case 7:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_사진관_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 122;
                                    small = 513 - 1;
                                    break;
                                case 8:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.편의시설_컴퓨터인터넷_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 123;
                                    small = 516 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                if (spin1.getItem(position).equals("긴급상황")) {
                    spin2 = ArrayAdapter.createFromResource(Parsing.this, R.array.긴급상황_중분류, android.R.layout.simple_spinner_dropdown_item);
                    spin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(spin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                //아이템의 위치에따라
                                case 0:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.긴급상황_응급상황_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 531;
                                    small = 0;
                                    break;
                                case 1:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.긴급상황_분실범죄_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 17;
                                    small = 128 - 1;
                                    break;
                                case 2:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.긴급상황_재난재해_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 18;
                                    small = 137 - 1;
                                    break;
                                case 3:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.긴급상황_교통사고_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 19;
                                    small = 140 - 1;
                                    break;
                                case 4:
                                    spin3 = ArrayAdapter.createFromResource(Parsing.this, R.array.긴급상황_교통위반_소분류, android.R.layout.simple_spinner_dropdown_item);
                                    middle = 20;
                                    small = 142 - 1;
                                    break;

                                default:
                                    break;
                            }
                            spin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner3.setAdapter(spin3);

                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i = 0; i <= position; i++) {
                                        if (i == position) {
                                            if (i == 0) //자주쓰이는 표현
                                            {
                                                in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&categoryTypeCode=0&targetLanguage=en";
                                                recyclerViewAdapter.delete();
                                                new Description().execute();
                                                break;
                                            }
                                            //나머지 소분류들
                                            small = small + position;
                                            in_doc = "https://phrase.dict.naver.com/detail.nhn?bigCategoryNo=2&middleCategoryNo=" + middle + "&smallCategoryNo=" + small + "&targetLanguage=en";
                                            recyclerViewAdapter.delete();
                                            new Description().execute();
                                            small = small - position;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

           }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getApplicationContext(),new LinearLayoutManager(this).getOrientation());

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));




        //AsyncTask 작동시킴(파싱)
        new Description().execute();


    }


    //웹 파싱
    private class Description extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                doc = Jsoup.connect(in_doc).get();

                Elements mElementDataSize = doc.select("div[class=dic_cont]").select("li"); //필요한 녀석만 꼬집어서 지정
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.

                for (Element elem : mElementDataSize) { //이렇게 요긴한 기능이
                    //영화목록 <li> 에서 다시 원하는 데이터를 추출해 낸다.
                    String my_title = elem.select("span[class=info_txt]").text();
                    String my_release = elem.select("span[class=info_txt2]").text();
                    String my_director = elem.select("span[class=info_txt3 kopron]").text();
                    //Log.d("test", "test" + mTitle);
                    //ArrayList에 계속 추가한다.
                    list.add(new ParsingItemObject(my_title, my_release, my_director));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //추출한 전체 <li> 출력해 보자.
            Log.d("debug :", "List " + mElementDataSize);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            recyclerViewAdapter = new ParsingRecyclerViewAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(recyclerViewAdapter);

        }
    }

}
