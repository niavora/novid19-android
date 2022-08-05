package com.example.qr_niavo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_niavo.Managers.HttpHandler;
import com.example.qr_niavo.Models.Personne;
import com.example.qr_niavo.Service.Session;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Resultat extends AppCompatActivity {
    TextView resultat;
    String resultatText;
    String nomCentre,idCentre,etatTest,dateTest,nomVaccin,dateVaccin;
    int origine=-1;
    Session sh;
    Personne p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        initView();
    }

    private void initView(){
        try{
            this.getSupportActionBar().hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        resultat=(TextView)findViewById(R.id.textResultat);


        Bundle bundle=getIntent().getExtras();
        origine=bundle.getInt("origine");
        sh=new Session(this);
        p=sh.getUser();
        if(origine==0){
            idCentre=bundle.getString("centre_id");
            etatTest=bundle.getString("etat_test");
            dateTest=bundle.getString("date_test");
            new Centre(idCentre).execute();
        }
        else if(origine==1){
            idCentre=bundle.getString("centre_id");
            nomVaccin=bundle.getString("nom_vaccin");
            dateVaccin=bundle.getString("date_vaccin");
            new Centre(idCentre).execute();
        }
    }




    private String renderText(String ...params){
        if(origine==0){
            String nom=params[0];
            String prenoms=params[1];
            String dateDdn=params[2];
            String adresse=params[3];
            String dateTest=params[4];
            String resultat=params[5];
            String lieuTest=params[6];

            StringBuilder sb=new StringBuilder();
            sb.append("Nous, le centre ");
            sb.append(lieuTest+" ");
            sb.append("attestons par la présente que M./Mme/Mlle ");
            sb.append("<font><b>"+nom+"</b></font> ");
            sb.append("<font><b>"+prenoms+"</b></font> ");
            sb.append("né(e) le");
            sb.append(" <font><b>"+dateDdn+"</b></font> ");
            sb.append("domicilé(e) au ");
            sb.append(" <font><b>"+adresse+"</b></font> ");
            sb.append(" a été tésté(e) le");
            sb.append(" <font><b>"+dateTest+"</b></font> ");
            sb.append("avec un résultat");
            sb.append(" <font><b>"+resultat+"</b></font> ");
            sb.append(".");

            return sb.toString();
        }
        else{
            String nom=params[0];
            String prenoms=params[1];
            String dateDdn=params[2];
            String adresse=params[3];
            String dateVaccin=params[4];
            String nomVaccin=params[5];
            String lieuTest=params[6];

            StringBuilder sb=new StringBuilder();
            sb.append("<font><b>Nom: </b></font> "+nom+"<br><br>");
            sb.append("<font><b>Prénom(s): </b></font> "+prenoms+"<br><br>");
            sb.append("<font><b>Date de naissance: </b></font> "+dateDdn+"<br><br>");
            sb.append("<font><b>Adresse: </b></font> "+adresse+"<br><br>");
            sb.append("<font><b>Nom de la vaccination: </b></font> "+nomVaccin.toUpperCase()+"<br><br>");
            sb.append("<font><b>Date de vaccination:  </b></font> "+dateVaccin+"<br><br>");
            sb.append("<font><b>Site de vaccination:  </b></font> "+lieuTest+"<br><br>");

            sb.append(".");

            return sb.toString();
        }

        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Resultat.this.finish();
        Intent intent=new Intent(Resultat.this,AccueilActivity.class);
         Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent,bundle);
    }

    private class Centre extends AsyncTask<Void,Void, JSONObject> {
        SweetAlertDialog pDialog = new SweetAlertDialog(Resultat.this, SweetAlertDialog.PROGRESS_TYPE);
        String centreId;
        Centre(String id){
            centreId=id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#66ccff"));
            pDialog.setTitleText("Chargement");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            List params = new ArrayList();
            //PARAMS : Key - value
            // params.add(new BasicNameValuePair("key", value));

            HttpHandler handler = new HttpHandler();

            //Host : EndPoint
            String url=Config.HOST+Config.CENTRE;
            url+=centreId;


            String apiResponse = handler.getHttp(url);
            System.out.println("API RESPONSE"+apiResponse);

            try {
                if(apiResponse!=null){
                    return new JSONObject(apiResponse);
                }
                else {
                    throw new JSONException("JSON vide");
                }

            } catch (JSONException e) {
                Log.e("Exception json",e.getMessage().toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if(pDialog!=null){
                pDialog.dismissWithAnimation();
            }

            //Result is null

            if(result==null){
                Toast.makeText(Resultat.this, "Erreur", Toast.LENGTH_SHORT).show();
            }

            else{
                //TREATMENT & REDIRECTION
                try {
                    if(result.has("nom_centre")){
                        nomCentre=result.getString("nom_centre");
                        resultatText="";


                        if(origine==0){
                            String resultatTest=(etatTest=="0")?"négatif":"positif";
                            resultatText=renderText(p.getNom(),p.getPrenom(),
                                    p.getDateDdn(),p.getAdresse(),Utility.formatDate(dateTest.substring(0,10)),resultatTest,nomCentre);

                        }
                        else{
                            resultatText=renderText(p.getNom(),p.getPrenom(),
                                    p.getDateDdn(),p.getAdresse(),Utility.formatDate(dateVaccin.substring(0,10)),nomVaccin,nomCentre);

                        }


                        resultat.setText(Html.fromHtml(resultatText));
                    }
                    else{
                        throw new Exception("Aucun résultat");
                    }




                } catch (Exception e) {
                  resultat.setText(Html.fromHtml(e.getMessage()));
                }

            }
        }
    }

}