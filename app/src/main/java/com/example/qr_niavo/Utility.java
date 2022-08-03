package com.example.qr_niavo;

public class Utility {

    public static String formatDate(String dateInput){
        String[] dateA=dateInput.split("-");
        String retour=dateA[2]+"/"+dateA[1]+"/"+dateA[0];
        return retour;
    }
}
