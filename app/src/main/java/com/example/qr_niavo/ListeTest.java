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
import android.widget.ListView;
import android.widget.Toast;

import com.example.qr_niavo.Adaptor.ListeTestAdaptor;
import com.example.qr_niavo.Managers.HttpHandler;
import com.example.qr_niavo.Models.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListeTest extends AppCompatActivity {
    ListView listView;
    List<Test> listeTest;
    ListeTestAdaptor testAdaptor;
    HashMap<String,String> listeCentre;
    Context ctx;
    String resultatTestString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listetest);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Centre().execute();
    }

    private void initView(){
        try{
            this.getSupportActionBar().hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        this.listView=(ListView)findViewById(R.id.listeview);
        this.listeTest=new ArrayList<>();
        this.listeCentre=new HashMap<>();
        this.ctx=this;
        resultatTestString="";
        Bundle bundle=getIntent().getExtras();
        resultatTestString=bundle.getString("Resultat");


        //List onClick
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Test item=listeTest.get(i);

                //
            }
        });
    }



    private class Centre extends AsyncTask<Void,Void, JSONObject> {
        SweetAlertDialog pDialog = new SweetAlertDialog(ListeTest.this, SweetAlertDialog.PROGRESS_TYPE);
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
                Toast.makeText(ListeTest.this, "Erreur", Toast.LENGTH_SHORT).show();
            }

            else{
                //TREATMENT & REDIRECTION
                try {
                    if(result.has("docs")){
                        listeCentre=Utility.ListeCentre(result);
                        listeTest=Utility.jsonToListTEST(resultatTestString,listeCentre);
                        ListeTest.this.testAdaptor=new ListeTestAdaptor(ctx,listeTest);
                        ListeTest.this.listView.setAdapter(ListeTest.this.testAdaptor);
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
        Intent intent=new Intent(ListeTest.this,AccueilActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent,bundle);
    }
}