package com.example.qr_niavo;

import com.example.qr_niavo.Models.Lieu;
import com.example.qr_niavo.Models.Test;
import com.example.qr_niavo.Models.Vaccin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utility {

    public static String formatDate(String dateInput){
        String[] dateA=dateInput.split("-");
        String retour=dateA[2]+"/"+dateA[1]+"/"+dateA[0];
        return retour;
    }

    public static List<Lieu> jsonToList(String resultat, HashMap<String,String> valueLieu) throws JSONException {
        JSONArray ja=new JSONArray(resultat);
        List<Lieu> listeLieu=new ArrayList<>();

        for (int i=0;i<ja.length();i++){
            JSONObject JSONlieu=ja.getJSONObject(i);
            Lieu lieu=null;
            if(JSONlieu.has("date_passage")){
                String datePassage=JSONlieu.getString("date_passage").substring(0,10);
                String lieuId=JSONlieu.getString("lieu_id").toString();
                String lieuPassage=valueLieu.get(lieuId);
                lieu=new Lieu(datePassage,lieuPassage);
                listeLieu.add(lieu);
            }

        }
        return listeLieu;
    }

    public static HashMap<String,String> ListeLieu(JSONObject resultat) throws JSONException {
            JSONArray jsonArray=resultat.getJSONArray("docs");
            HashMap<String,String> retour=new HashMap<>();
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                System.out.println("JSON ==="+jsonObject.toString());
                if(jsonObject.has("nom_lieu")){
                    String _id=jsonObject.getString("_id");
                    System.out.println("id"+jsonObject.getString("_id"));
                    String nom_lieu=jsonObject.getString("nom_lieu");
                    System.out.println("Nom lieu"+jsonObject.getString("nom_lieu"));
                    String adresse=jsonObject.getString("adresse_lieu");
                    adresse=adresse.split(",")[0];

                    String adresseLocal=nom_lieu+" "+adresse;

                    retour.put(_id,adresseLocal);
                }

            }
            return retour;

    }


    //LIST CENTRE
    public static HashMap<String,String> ListeCentre(JSONObject resultat) throws JSONException {
        JSONArray jsonArray=resultat.getJSONArray("docs");
        HashMap<String,String> retour=new HashMap<>();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            System.out.println("JSON ==="+jsonObject.toString());
            if(jsonObject.has("nom_centre")){
                String _id=jsonObject.getString("_id");
                String nom_lieu=jsonObject.getString("nom_centre");

                retour.put(_id,nom_lieu);
            }

        }
        return retour;

    }

    //LISTE TEST
    public static List<Test> jsonToListTEST(String resultat, HashMap<String,String> valueCentre) throws JSONException {
        JSONArray ja=new JSONArray(resultat);
        List<Test> listeLieu=new ArrayList<>();

        for (int i=0;i<ja.length();i++){
            JSONObject JSONlieu=ja.getJSONObject(i);
            Test test=null;
            if(JSONlieu.has("date_test")){
                String dateTest=JSONlieu.getString("date_test").substring(0,10);
                String centreId=JSONlieu.getString("centre_id").toString();
                String lieuTest=valueCentre.get(centreId);
                test=new Test(dateTest,lieuTest);
                listeLieu.add(test);
            }

        }
        return listeLieu;
    }

    //LISTE vaccin
    public static List<Vaccin> jsonToListVaccin(String resultat, HashMap<String,String> valueCentre) throws JSONException {
        JSONArray ja=new JSONArray(resultat);
        List<Vaccin> listeVaccin=new ArrayList<>();

        for (int i=0;i<ja.length();i++){
            JSONObject JSONlieu=ja.getJSONObject(i);
            Vaccin vaccin=null;
            System.out.println("JSON VC "+JSONlieu.toString());
            if(JSONlieu.has("nom_vaccin")){
                System.out.println("Miditra atoo");
                String dateVaccin=JSONlieu.getString("date_vaccin").substring(0,10);
                String centreId=JSONlieu.getString("centre_id").toString();
                String nom_vaccin=JSONlieu.getString("nom_vaccin").toString().toUpperCase();
                String lieuTest=valueCentre.get(centreId);
                vaccin=new Vaccin(nom_vaccin,lieuTest,dateVaccin);
                listeVaccin.add(vaccin);
            }

        }
        return listeVaccin;
    }




}
