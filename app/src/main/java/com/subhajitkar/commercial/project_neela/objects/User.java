package com.subhajitkar.commercial.project_neela.objects;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static final String TAG = "UserCredentials";

    private String userName, userGender;
    private int userAge;
    private List<String> quotesPref, newsPref;

    public User(String name, int age, String  gender, List<String> quotesPref, List<String> newsPref){
        userName = name;
        userAge = age;
        userGender = gender;
        this.quotesPref = quotesPref;
        this.newsPref = newsPref;
    }

    public User(){
        //initializing fields in case of no args constructor
        userName = "";
        userGender = "";
        userAge = 0;
        quotesPref = new ArrayList<>();
        newsPref = new ArrayList<>();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setNewsPref(List<String> newsPref) {
        this.newsPref = newsPref;
    }

    public void setQuotesPref(List<String> quotesPref) {
        this.quotesPref = quotesPref;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public List<String> getNewsPref() {
        return newsPref;
    }

    public List<String> getQuotesPref() {
        return quotesPref;
    }
}
