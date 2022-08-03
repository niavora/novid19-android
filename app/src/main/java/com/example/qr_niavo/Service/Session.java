package com.example.qr_niavo.Service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qr_niavo.Models.Personne;
import com.example.qr_niavo.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class Session {
        private Context context;

        public Session(Context sh){
            this.context=sh;
        }


        public void saveUser(JSONObject resultat) throws JSONException {
            //ANRxGAME
            SharedPreferences sharedpreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("id", resultat.getString("_id"));
            editor.putString("nom", resultat.getString("nom"));
            editor.putString("prenom", resultat.getString("prenom"));
            editor.putString("mail", resultat.getString("mail"));
            editor.putString("cin", resultat.getString("cin"));
            editor.putString("dateddn", resultat.getString("date_naissance"));
            editor.putString("adresse", resultat.getString("adresse"));
            editor.putInt("sexe", resultat.getInt("sexe"));
            editor.commit();
        }

        public Personne getUser(){
            SharedPreferences sh = this.context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
            Personne m = null;
            m = new Personne(
                    sh.getString("id", ""),
                    sh.getString("nom", ""),
                    sh.getString("prenom", ""),
                    sh.getString("mail", ""),
                    Utility.formatDate(sh.getString("dateddn", "").substring(0,10)),
                    sh.getString("adresse", ""),
                    sh.getString("cin", ""),
                    sh.getInt("sexe", 0));
            return m;
        }

        public void deleteSession(){
            SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
            sp.edit().clear().commit();
        }
}
