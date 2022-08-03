package com.example.qr_niavo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qr_niavo.Managers.HttpHandler;
import com.example.qr_niavo.Service.Session;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    Button scan_qr;
    Session sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    @Override
    protected void onStart() {
        super.onStart();
        onClick();
    }

    private void initView(){
        try{
            this.getSupportActionBar().hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.scan_qr=(Button)findViewById(R.id.scan_qr);
        sh=new Session(this);
    }

    private void onClick(){
        this.scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    initScan();
            }
        });
    }


    private void initScan() {
        //Initialisation <CAMERA QR CODE>
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
      /*  integrator.setCaptureActivity(Qr_Activity.class);
        integrator.setPrompt("Scannez le Qr Code");*/
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Data: données dans le QR CODE

        if (data != null) {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult == null) {
                Toast.makeText(this, "QR CODE incorrect", Toast.LENGTH_SHORT).show();
                //Utility.ShowToast(this,"QR CODE incorrect");
            } else {
                String resultat = null;
                resultat = intentResult.getContents();
                //resultat : Texte dans le QR CODE
                if (resultat != null) {
                    try {
                        new AfterScan(resultat).execute();
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }



    private class AfterScan extends AsyncTask<Void,Void, JSONObject>{
        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        String userId;
        AfterScan(String id){
            userId=id;
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
            String url=Config.HOST+Config.AUTHENTIFICATION+userId;
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
                Toast.makeText(MainActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            }

            else{
                    //TREATMENT & REDIRECTION
                try {
                    sh.saveUser(result);
                    MainActivity.this.finish();
                    Intent intent=new Intent(MainActivity.this,AccueilActivity.class);
                    startActivity(intent);
                    
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }
    }

}