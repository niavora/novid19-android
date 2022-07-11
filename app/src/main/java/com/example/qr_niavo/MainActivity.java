package com.example.qr_niavo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Button scan_qr;
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
                        Toast.makeText(this, "RESULTAT"+resultat, Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }

}