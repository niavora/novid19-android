package com.example.qr_niavo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_niavo.Adaptor.ListeLieuAdaptor;
import com.example.qr_niavo.Adaptor.ListeTestAdaptor;
import com.example.qr_niavo.Managers.HttpHandler;
import com.example.qr_niavo.Models.Lieu;
import com.example.qr_niavo.Models.Test;
import com.example.qr_niavo.Service.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListeLieu extends AppCompatActivity {
    ListView listView;
    List<Lieu> listeLieu;
    ListeLieuAdaptor lieuAdaptor;
    HashMap<String,String> lieuMap;
    Context ctx;
    String jsonListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_lieu);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        new getListeLieu().execute();
    }

    private void initView(){
        try{
            this.getSupportActionBar().hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        this.listView=(ListView)findViewById(R.id.listeview);
        this.listeLieu=new ArrayList<>();
        this.ctx=this;
        this.lieuMap=new HashMap<>();
        Bundle bundle=getIntent().getExtras();
        jsonListe="";
        jsonListe=bundle.getString("Resultat");


        //List onClick
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Lieu item=listeLieu.get(i);

                //
            }
        });
    }


    private class getListeLieu extends AsyncTask<Void,Void, JSONObject>{
        SweetAlertDialog pDialog = new SweetAlertDialog(ListeLieu.this, SweetAlertDialog.PROGRESS_TYPE);
        getListeLieu(){
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
            String url=Config.HOST+Config.LIEU;

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
                Toast.makeText(ListeLieu.this, "Erreur", Toast.LENGTH_SHORT).show();
            }
            else{
                //TREATMENT & REDIRECTION
                try {
                    if(result.has("docs")){
                        lieuMap=Utility.ListeLieu(result);
                        listeLieu=Utility.jsonToList(jsonListe,lieuMap);

                        //SETUP UI
                        ListeLieu.this.lieuAdaptor=new ListeLieuAdaptor(ctx,listeLieu);
                        ListeLieu.this.listView.setAdapter( ListeLieu.this.lieuAdaptor);


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
        Intent intent=new Intent(ListeLieu.this,AccueilActivity.class);
         Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent,bundle);
    }
}