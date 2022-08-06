package com.example.qr_niavo.Models;

public class Test {
    private String dateTest;
    private String lieuTest;
    private String etatTest;


    //Constructor
    public Test(){

    }



    public Test(String dateTest, String lieuTest, String etatTest) {
        this.dateTest = dateTest;
        this.lieuTest = lieuTest;
        this.etatTest = etatTest;
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

    public String getEtatTest() {
        return etatTest;
    }

    public void setLieuTest(String lieuTest) {
        this.lieuTest = lieuTest;
    }

    public void setEtatTest(String lieuTest) {
        this.etatTest = lieuTest;
    }
}
