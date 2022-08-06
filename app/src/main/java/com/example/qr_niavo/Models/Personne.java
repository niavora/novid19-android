package com.example.qr_niavo.Models;

public class Personne {
    private String id,nom,prenom,mail,dateDdn,adresse,cin, CarteId;
    private int sexe;

    public Personne(String id, String nom, String prenom, String mail, String dateDdn, String adresse, String cin, int sexe, String carte) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.dateDdn = dateDdn;
        this.adresse = adresse;
        this.cin = cin;
        this.sexe = sexe;
        this.CarteId = carte;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDateDdn() {
        return dateDdn;
    }

    public void setDateDdn(String dateDdn) {
        this.dateDdn = dateDdn;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public int getSexe() {
        return sexe;
    }

    public void setSexe(int sexe) {
        this.sexe = sexe;
    }

    public void setCarteId(String sexe) {
        this.CarteId = sexe;
    }

    public String getCarteId() {
        return CarteId;
    }
}
