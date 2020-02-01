package com.a3scorp.jsea.speech_to_text_project;

import android.widget.ImageButton;

public class ParsingItemObject {
    private String title;
    private String release;
    private String director;


    public ParsingItemObject(String title, String release, String director){
        this.title = title;
        this.release = release;
        this.director = director;
    }


    public String getTitle() {
        return title;
    }

    public String getRelease() {
        return release;
    }

    public String getDirector() {
        return director;
    }

}