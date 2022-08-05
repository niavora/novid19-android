package com.example.qr_niavo.Models;

public class Lieu {
    String datePassage,lieu;

    public Lieu() {
    }

    public Lieu(String datePassage, String lieu) {
        this.datePassage = datePassage;
        this.lieu = lieu;
    }

    //GETTERS & SETTERS

    public String getDatePassage() {
        return datePassage;
    }

    public void setDatePassage(String datePassage) {
        this.datePassage = datePassage;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
}
