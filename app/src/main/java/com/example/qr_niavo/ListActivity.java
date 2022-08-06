package com.example.qr_niavo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_niavo.Adaptor.ListeVaccinAdaptor;
import com.example.qr_niavo.Managers.HttpHandler;
import com.example.qr_niavo.Models.Test;
import com.example.qr_niavo.Models.Vaccin;
import com.example.qr_niavo.Service.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListActivity extends AppCompatActivity {
    ListView listView;
    List<Vaccin> listVaccin;
    ListeVaccinAdaptor vaccinAdaptor;
    HashMap<String,String> listeCentre;
    Context ctx;
    String resultatTestString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        new ListActivity.Centre().execute();
    }

    private void initView(){
        try{
            this.getSupportActionBar().hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        this.listView=(ListView)findViewById(R.id.listeview);
        this.listVaccin=new ArrayList<>();
        this.listeCentre=new HashMap<>();
        this.ctx=this;
        resultatTestString="";
        Bundle bundle=getIntent().getExtras();
        resultatTestString=bundle.getString("Resultat");

        System.out.println("Resultat test string"+resultatTestString);
        //List onClick
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vaccin item=listVaccin.get(i);

                //
            }
        });
    }



    class Centre extends AsyncTask<Void,Void, JSONObject> {
        SweetAlertDialog pDialog = new SweetAlertDialog(ListActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        Centre(){

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

            String apiResponse = handler.getHttp(url);
            System.out.println("RESULTAT TEST RESPONSE"+apiResponse);

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
                Toast.makeText(ListActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            }

            else{
                //TREATMENT & REDIRECTION
                try {
                    System.out.println("ATOOOOO");
                    if(result.has("docs")){
                        listeCentre=Utility.ListeCentre(result);
                        listVaccin=Utility.jsonToListVaccin(resultatTestString,listeCentre);
                        System.out.println("Liste vaccin"+listVaccin.size());
                        ListActivity.this.vaccinAdaptor=new ListeVaccinAdaptor(ctx,listVaccin);
                        ListActivity.this.listView.setAdapter(ListActivity.this.vaccinAdaptor);
                        System.out.println("VITA NY AFFICHAGE");

                    }
                    else{
                        throw new Exception("Aucun résultat");
                    }

                } catch (Exception e) {
                    Log.e("Erreur",e.getMessage());
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent=new Intent(ListActivity.this,AccueilActivity.class);
         Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent,bundle);
    }
}
