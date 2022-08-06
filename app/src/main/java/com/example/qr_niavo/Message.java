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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_niavo.Adaptor.ListeTestAdaptor;
import com.example.qr_niavo.Adaptor.MessageAdaptor;
import com.example.qr_niavo.Managers.HttpHandler;
import com.example.qr_niavo.Models.MessageModel;
import com.example.qr_niavo.Models.Personne;
import com.example.qr_niavo.Models.Test;
import com.example.qr_niavo.Service.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Message extends AppCompatActivity {
    ListView listView;
    List<MessageModel> listeMessage;
    MessageAdaptor messageAdaptor;
    HashMap<String,String> listeCentre;
    Context ctx;
    String resultatTestString;
    TextView nom;
    Session sh;
    Personne p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Traitement(resultatTestString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        try{
            this.getSupportActionBar().hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        sh = new Session(this);
        p = sh.getUser();
        nom = (TextView) findViewById(R.id.nom);
        nom.setText(p.getNom() + " "+p.getPrenom());
        this.listView=(ListView)findViewById(R.id.listeview);
        this.listeMessage=new ArrayList<>();
        this.ctx=this;
        resultatTestString="";
        Bundle bundle=getIntent().getExtras();
        resultatTestString=bundle.getString("Resultat");


        //List onClick
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageModel item=listeMessage.get(i);

                //
            }
        });
    }

    private void Traitement(String message) throws JSONException {
        JSONArray ja=new JSONArray(message);
        for (int i=0;i<ja.length();i++){
            JSONObject jo=ja.getJSONObject(i);
            MessageModel m=new MessageModel(jo.getString("message"));
            listeMessage.add(m);
        }
        this.messageAdaptor=new MessageAdaptor(this,listeMessage);
        this.listView.setAdapter(messageAdaptor);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent=new Intent(Message.this,AccueilActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent,bundle);
    }
}