package com.example.qr_niavo.Models;

public class Vaccin {
    private String id,nomVaccin,centre,personneId,dateVaccin;
    private int etat;

    public Vaccin(String nomVaccin,String centre,String dateVaccin){
        this.nomVaccin=nomVaccin;
        this.centre=centre;
        this.dateVaccin=dateVaccin;
    }

    //GETTERS AND SETTERS


    public String getNomVaccin() {
        return nomVaccin;
    }

    public void setNomVaccin(String nomVaccin) {
        this.nomVaccin = nomVaccin;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public String getDateVaccin() {
        return dateVaccin;
    }

    public void setDateVaccin(String dateVaccin) {
        this.dateVaccin = dateVaccin;
    }
}
