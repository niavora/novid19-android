package com.example.qr_niavo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.qr_niavo.Adaptor.ListeTestAdaptor;
import com.example.qr_niavo.Models.Test;

import java.util.ArrayList;
import java.util.List;

public class ListeTest extends AppCompatActivity {
    ListView listView;
    List<Test> listeTest;
    ListeTestAdaptor testAdaptor;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listetest);
        initView();

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
        this.ctx=this;
        this.testAdaptor=new ListeTestAdaptor(ctx,listeTest);
        this.listView.setAdapter(this.testAdaptor);

        //List onClick
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Test item=listeTest.get(i);

                //
            }
        });
    }
}