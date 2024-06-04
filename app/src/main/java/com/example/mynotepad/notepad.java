package com.example.mynotepad;


public class notepad {
    String date;
    String note;
    public notepad(String date,String note){
        this.date=date;
        this.note=note;
    }
    public String getDate(){
        return date;
    }
    public String getNote() {
        return note;
    }
}
