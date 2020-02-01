package com.a3scorp.jsea.speech_to_text_project;

public class OptionsItemObject {
    private String mainText;
    private String subText;


    public OptionsItemObject(String mainText, String subText){
        this.mainText = mainText;
        this.subText = subText;
    }


    public String getMainText() {
        return mainText;
    }

    public String getSubText() {
        return subText;
    }

}
