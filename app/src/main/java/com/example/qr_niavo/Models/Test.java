package com.example.qr_niavo.Models;

public class Test {
    private String dateTest;
    private String lieuTest;

    //Constructor
    public Test(){

    }



    public Test(String dateTest, String lieuTest) {
        this.dateTest = dateTest;
        this.lieuTest = lieuTest;
    }

    //Setters & getters

    public String getDateTest() {
        return dateTest;
    }

    public void setDateTest(String dateTest) {
        this.dateTest = dateTest;
    }

    public String getLieuTest() {
        return lieuTest;
    }

    public void setLieuTest(String lieuTest) {
        this.lieuTest = lieuTest;
    }
}
