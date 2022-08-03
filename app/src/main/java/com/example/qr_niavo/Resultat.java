package com.example.qr_niavo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Resultat extends AppCompatActivity {
    TextView resultat;
    String resultatText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        initView();
    }

    private void initView(){
        resultat=(TextView)findViewById(R.id.textResultat);
        resultatText="";

        resultatText=renderText();
        resultat.setText(Html.fromHtml(resultatText));
    }


    private String renderText(String ...params){
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

        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Resultat.this.finish();
    }
}