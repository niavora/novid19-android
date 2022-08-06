package com.example.qr_niavo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_niavo.Adaptor.ListeLieuAdaptor;
import com.example.qr_niavo.Managers.HttpHandler;
import com.example.qr_niavo.Models.Personne;
import com.example.qr_niavo.Service.Session;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AccueilActivity extends AppCompatActivity {
    TextView nom;
    Session sh;
    Personne p;
    LinearLayout scanTest, scanCarte, scanLieux, historiqueLieu, historiqueTest, historiqueVaccin, notification;
    HashMap<String, String> lieuMap;
    int lieuPartenaires;
    int casPositif;
    int testEffectue;
    TextView testChiffre, lieuChiffre, positifChiffre;
    TextView notif;
    String erreur = "";
    String listeVaccin;

    /*
        0:TEST
        1:VACCIN
        2:LIEU
    * **/
    int origine;


    /**
     * TEST,
     * VACCIN,
     * LIEU
     */
    String historique = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new getListeLieu().execute();

        new MessageCount().execute();

        //EVENT
        onClick();
    }

    private void initView() {
        try {
            this.getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        scanTest = (LinearLayout) findViewById(R.id.scan_test);
        scanCarte = (LinearLayout) findViewById(R.id.scan_vaccin);
        scanLieux = (LinearLayout) findViewById(R.id.scan_lieux);
        historiqueLieu = (LinearLayout) findViewById(R.id.historique_lieu);
        historiqueTest = (LinearLayout) findViewById(R.id.historique_test);
        historiqueVaccin = (LinearLayout) findViewById(R.id.historique_vaccin);
        testChiffre = (TextView) findViewById(R.id.test_chiffre);
        positifChiffre = (TextView) findViewById(R.id.positif_chiffre);
        lieuChiffre = (TextView) findViewById(R.id.lieux_chiffre);
        notif = (TextView) findViewById(R.id.chiffre);
        notification = (LinearLayout) findViewById(R.id.notification);
        nom = (TextView) findViewById(R.id.nom);

        lieuMap = new HashMap<>();
        casPositif = 0;
        testEffectue = 0;
        lieuPartenaires = 0;

        sh = new Session(this);
        p = null;
        p = sh.getUser();

        nom = (TextView) findViewById(R.id.nom);
        nom.setText(p.getNom() + " " + p.getPrenom());

        //ORIGINE CLICK
        origine = -1;
    }

    private void onClick() {
        scanTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                origine = 0;
                initScan();
            }
        });
        scanCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                origine = 1;
                initScan();
            }
        });
        scanLieux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                origine = 2;
                initScan();
            }
        });
        nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sh = new Session(AccueilActivity.this);
                sh.deleteSession();

                AccueilActivity.this.finish();
                Intent intent = new Intent(AccueilActivity.this, MainActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                startActivity(intent, bundle);

            }
        });


        //HISTORIQUE
        historiqueLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historique = "LIEU";
                new HistoriqueAPI().execute();
            }
        });
        historiqueTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historique = "TEST";
                new HistoriqueAPI().execute();
            }
        });
        historiqueVaccin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historique = "VACCIN";
                new HistoriqueAPI().execute();
            }
        });

        //notification
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Message().execute();
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
                        if (origine == 0 || origine == 1) {
                            new TestOrVaccin(resultat).execute();
                        } else if (origine == 2) {
                            //POST
                            new PostLieu(resultat).execute();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sh.deleteSession();
        this.finish();
        Intent intent = new Intent(AccueilActivity.this, MainActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
    }


    //CALL TEST OR VACCIN
    private class TestOrVaccin extends AsyncTask<Void, Void, JSONObject> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        String userId;

        TestOrVaccin(String id) {
            userId = id;
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
            String url = Config.HOST;
            if (origine == 0) {
                url += Config.TESTSCAN;
            } else if (origine == 1) {
                url += Config.VACCINSCAN;
            }
            url += userId;

            String apiResponse = handler.getHttp(url);

            try {
                if (!apiResponse.trim().equals("null")) {
                    if (origine == 1) {
                        JSONObject reponse = new JSONObject(apiResponse);
                        String carte_id = reponse.getString("carte_id");
                        url = Config.HOST + Config.CARTE + carte_id;
                        String apiResponse2 = handler.getHttp(url);
                        if (!apiResponse2.trim().equals("null")) {
                            JSONObject reponse2 = new JSONObject(apiResponse2);
                            if (reponse2.getString("personne_id").equals(p.getId())) {
                                return reponse;
                            } else {
                                erreur = "Vous n'avez pas encore eu ce vaccin";
                                throw new JSONException("Vous n'avez pas encore eu ce vaccin");
                            }
                        }
                    } else {
                        return new JSONObject(apiResponse);
                    }
                } else if (apiResponse.trim().equals("null") && origine == 1) {
                    System.out.println("ATOOOO"+origine);
                    url = Config.HOST + Config.CARTE + userId;
                    String apiResponse2 = handler.getHttp(url);
                    System.out.println("/////////////////"+apiResponse2);
                    if (!apiResponse2.trim().equals("null")) {
                        JSONObject reponse2 = new JSONObject(apiResponse2);
                        if (reponse2.getString("personne_id").equals(p.getId())) {
                            sh.saveCarteId(reponse2.getString("_id"));
                            url = Config.HOST + Config.VACCINCARTE + reponse2.getString("_id");
                            String apiResponse3 = handler.getHttp(url);
                            listeVaccin = "ok";
                            return new JSONObject("{\"docs\":"+apiResponse3+"}");
                        } else {
                            erreur = "Cette carte n'est pas à vous";
                            throw new JSONException("Cette carte n'est pas à vous");
                        }
                    }
                    else {
                        return new JSONObject(apiResponse);
                    }
                } else {
                    erreur = "Aucun résultat 1";
                    throw new JSONException("Aucun résultat 1");
                }
                return null;
            } catch (JSONException e) {
                Log.e("Exception json", e.getMessage().toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if (pDialog != null) {
                pDialog.dismissWithAnimation();
            }

            //Result is null

            if (result == null) {
                Toast.makeText(AccueilActivity.this, erreur == "" ? "erreur" : erreur  , Toast.LENGTH_SHORT).show();
            }
            else if(listeVaccin.equals("ok") && result != null){
                AccueilActivity.this.finish();
                Intent intent = new Intent(AccueilActivity.this, ListVaccin.class);
                intent.putExtra("Resultat", result.toString());
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                startActivity(intent, bundle);
            } else {
                //TREATMENT & REDIRECTION
                try {


                    //COMPARAISON IDUSER
                    if (origine == 0) {
                        String _idUser = result.getString("personne_id");
                        if (_idUser.equals(p.getId())) {
                            String centre_id = result.getString("centre_id");
                            String etat_test = result.getString("etat_test");
                            String datetest = result.getString("date_test");


                            AccueilActivity.this.finish();

                            Intent intent = new Intent(AccueilActivity.this, Resultat.class);
                            intent.putExtra("centre_id", centre_id);
                            intent.putExtra("etat_test", etat_test);
                            intent.putExtra("date_test", datetest);
                            intent.putExtra("origine", origine);

                            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                                    android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                            startActivity(intent, bundle);
                        } else {
                            throw new Exception("Ce test n'est pas à vous");
                        }
                    } else if (origine == 1) {

                        String centre_id = result.getString("centre_id");
                        String nomvaccin = result.getString("nom_vaccin");
                        String date_vaccin = result.getString("date_vaccin");

                        AccueilActivity.this.finish();
                        Intent intent = new Intent(AccueilActivity.this, Resultat.class);
                        intent.putExtra("centre_id", centre_id);
                        intent.putExtra("nom_vaccin", nomvaccin);
                        intent.putExtra("date_vaccin", date_vaccin);
                        intent.putExtra("origine", origine);
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                        startActivity(intent, bundle);

                    } else {

                    }
                } catch (Exception e) {
                    Toast.makeText(AccueilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }
    }

    //CALL POST
    private class PostLieu extends AsyncTask<Void, Void, JSONObject> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        String lieuId;

        PostLieu(String id) {
            lieuId = id;
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
            String pattern = "dd-MM-yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            Date today = Calendar.getInstance().getTime();
            String datePassage = df.format(today);


            List params = new ArrayList();
            //PARAMS : Key - value

            params.add(new BasicNameValuePair("lieu_id", lieuId));
            params.add(new BasicNameValuePair("personne_id", p.getId()));
            params.add(new BasicNameValuePair("date_passage", datePassage));


            System.out.println("Lieu Id" + lieuId);
            System.out.println("Personne Id" + p.getId());
            System.out.println("Date de passeage" + datePassage);

            HttpHandler handler = new HttpHandler();

            //Host : EndPoint
            String url = Config.HOST + Config.HISTORIQUELIEU;

            String apiResponse = handler.PostHttp(url, (ArrayList<BasicNameValuePair>) params);
            System.out.println("API RESPONSE" + apiResponse);

            try {
                if (apiResponse != null) {
                    return new JSONObject(apiResponse);
                } else {
                    throw new JSONException("JSON vide");
                }

            } catch (JSONException e) {
                Log.e("Exception json", e.getMessage().toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (pDialog != null) {
                pDialog.dismissWithAnimation();
            }
            if (jsonObject != null) {
                SweetAlertDialog sw = new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sw.setTitleText("Succès");
                sw.setContentText("Lieu sauvegardé");
                sw.setCanceledOnTouchOutside(false);

                sw.show();
            } else {
                Toast.makeText(AccueilActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //API
    private class HistoriqueAPI extends AsyncTask<Void, Void, JSONArray> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        String userId;

        HistoriqueAPI() {
            userId = p.getId();
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
        protected JSONArray doInBackground(Void... voids) {
            List params = new ArrayList();
            //PARAMS : Key - value
            //  params.add(new BasicNameValuePair("personne_id", p.getId()));

            HttpHandler handler = new HttpHandler();

            //Host : EndPoint
            String url = Config.HOST;
            if (historique.equals("LIEU")) {
                url += Config._HISTORIQUELIEU;
                url += p.getId();
            } else if (historique.equals("TEST")) {
                url += Config._HISTORIQUETEST;
                url += p.getId();
            } else if (historique.equals("VACCIN")) {
                url += Config._HISTORIQUEVACCIN + p.getCarteId();
            }

            System.out.println("URL" + url);
            String apiResponse = handler.getHttp(url);
            System.out.println("TEST RESPONSE" + apiResponse);

            try {
                if (apiResponse != null) {
                    return new JSONArray(apiResponse);
                } else {
                    erreur = "Scannez d'abord votre carte de vaccination.";
                    throw new JSONException("JSON vide");
                }

            } catch (JSONException e) {
                Log.e("Exception json", e.getMessage().toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);

            if (pDialog != null) {
                pDialog.dismissWithAnimation();
            }

            //Result is null

            if (result == null) {
                Toast.makeText(AccueilActivity.this, erreur != null ? erreur : "Erreur", Toast.LENGTH_SHORT).show();
            } else {
                //TREATMENT & REDIRECTION
                try {
                    if (historique.equals("LIEU")) {
                        AccueilActivity.this.finish();
                        Intent intent = new Intent(AccueilActivity.this, ListeLieu.class);
                        intent.putExtra("Resultat", result.toString());
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                        startActivity(intent, bundle);
                    } else if (historique.equals("TEST")) {
                        AccueilActivity.this.finish();
                        Intent intent = new Intent(AccueilActivity.this, ListeTest.class);
                        intent.putExtra("Resultat", result.toString());
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                        startActivity(intent, bundle);
                    } else {
                        System.out.println("resultat" + result.toString());
                        AccueilActivity.this.finish();
                        Intent intent = new Intent(AccueilActivity.this, ListActivity.class);
                        intent.putExtra("Resultat", result.toString());
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                        startActivity(intent, bundle);
                    }


                } catch (Exception e) {
                    Toast.makeText(AccueilActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }
    }


    //APPEL API LIEU
    private class getListeLieu extends AsyncTask<Void, Void, JSONObject> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        getListeLieu() {
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
            String url = Config.HOST + Config.LIEU;

            String apiResponse = handler.getHttp(url);
            System.out.println("API RESPONSE" + apiResponse);

            try {
                if (apiResponse != null) {
                    return new JSONObject(apiResponse);
                } else {
                    throw new JSONException("JSON vide");
                }

            } catch (JSONException e) {
                Log.e("Exception json", e.getMessage().toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if (pDialog != null) {
                pDialog.dismissWithAnimation();
            }

            //Result is null

            if (result == null) {
                Toast.makeText(AccueilActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            } else {
                //TREATMENT & REDIRECTION
                try {
                    if (result.has("docs")) {
                        lieuMap = Utility.ListeLieu(result);
                        lieuPartenaires = lieuMap.size();
                        new getListeTest().execute();
                    } else {
                        throw new Exception("Aucun résultat");
                    }


                } catch (Exception e) {
                    Log.e("Erreur", e.getMessage());
                }

            }
        }
    }

    private class getListeTest extends AsyncTask<Void, Void, JSONObject> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        getListeTest() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog.getProgressHelper().setBarColor(Color.parseColor("#66ccff"));
            pDialog.setTitleText("Chargement");
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            List params = new ArrayList();
            //PARAMS : Key - value
            // params.add(new BasicNameValuePair("key", value));

            HttpHandler handler = new HttpHandler();

            //Host : EndPoint
            String url = Config.HOST + Config.HISTORIQUETEST;

            String apiResponse = handler.getHttp(url);
            System.out.println("API RESPONSE" + apiResponse);

            try {
                if (apiResponse != null) {
                    return new JSONObject(apiResponse);
                } else {
                    throw new JSONException("JSON vide");
                }

            } catch (JSONException e) {
                Log.e("Exception json", e.getMessage().toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

          /*  if(pDialog!=null){
                pDialog.dismissWithAnimation();
            }*/

            //Result is null

            if (result == null) {
                Toast.makeText(AccueilActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            } else {
                //TREATMENT & REDIRECTION
                try {
                    if (result.has("docs")) {
                        JSONArray jo = result.getJSONArray("docs");
                        testEffectue = jo.length();
                        casPositif = 0;
                        for (int i = 0; i < jo.length(); i++) {
                            JSONObject jsonObject = jo.getJSONObject(i);
                            if (jsonObject.getInt("etat_test") == 2) {
                                ++casPositif;
                            }
                        }

                        lieuChiffre.setText(lieuPartenaires + "");
                        testChiffre.setText(testEffectue + "");
                        positifChiffre.setText(casPositif + "");
                        nom.setText(p.getNom() + " "+p.getPrenom());
                    } else {
                        throw new Exception("Aucun résultat");
                    }


                } catch (Exception e) {
                    Log.e("Erreur", e.getMessage());
                }

            }
        }
    }


    //MESSAGE
    private class Message extends AsyncTask<Void, Void, JSONArray> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        String userId;

        Message() {
            userId = p.getId();
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
        protected JSONArray doInBackground(Void... voids) {
            List params = new ArrayList();
            //PARAMS : Key - value
            //  params.add(new BasicNameValuePair("personne_id", p.getId()));

            HttpHandler handler = new HttpHandler();

            //Host : EndPoint
            String url = Config.HOST + Config.MESSAGE + p.getId();
            String apiResponse = handler.getHttp(url);
            System.out.println("TEST RESPONSE" + apiResponse);

            try {
                if (apiResponse != null) {
                    return new JSONArray(apiResponse);
                } else {
                    throw new JSONException("JSON vide");
                }

            } catch (JSONException e) {
                Log.e("Exception json", e.getMessage().toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);

            if (pDialog != null) {
                pDialog.dismissWithAnimation();
            }

            //Result is null

            if (result == null) {
                Toast.makeText(AccueilActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            } else {
                //TREATMENT & REDIRECTION
                try {
                    System.out.println("resultat" + result.toString());
                    AccueilActivity.this.finish();
                    Intent intent = new Intent(AccueilActivity.this, com.example.qr_niavo.Message.class);
                    intent.putExtra("Resultat", result.toString());
                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                            android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                    startActivity(intent, bundle);


                } catch (Exception e) {
                    Toast.makeText(AccueilActivity.this, "Aucun message", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }
    }

    //MESSAGE
    private class MessageCount extends AsyncTask<Void, Void, JSONArray> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AccueilActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        String userId;

        MessageCount() {
            userId = p.getId();
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
        protected JSONArray doInBackground(Void... voids) {
            List params = new ArrayList();
            //PARAMS : Key - value
            //  params.add(new BasicNameValuePair("personne_id", p.getId()));

            HttpHandler handler = new HttpHandler();

            //Host : EndPoint
            String url = Config.HOST + Config.MESSAGE + p.getId();
            String apiResponse = handler.getHttp(url);
            System.out.println("TEST RESPONSE" + apiResponse);

            try {
                if (apiResponse != null) {
                    return new JSONArray(apiResponse);
                } else {
                    throw new JSONException("JSON vide");
                }

            } catch (JSONException e) {
                Log.e("Exception json", e.getMessage().toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);

            if (pDialog != null) {
                pDialog.dismissWithAnimation();
            }

            //Result is null

            if (result == null) {
//                Toast.makeText(AccueilActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            } else {
                //TREATMENT & REDIRECTION
                try {
                    sh.saveNofifCount(result.length());
                    notif.setText(result.length()+"");
                } catch (Exception e) {
//                    Toast.makeText(AccueilActivity.this, "Aucun message", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }
    }
}