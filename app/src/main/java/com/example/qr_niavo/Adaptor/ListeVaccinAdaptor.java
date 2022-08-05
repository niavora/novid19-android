package com.example.qr_niavo.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qr_niavo.Models.Test;
import com.example.qr_niavo.Models.Vaccin;
import com.example.qr_niavo.R;

import java.util.List;

public class ListeVaccinAdaptor extends BaseAdapter {
    List<Vaccin> listes;
    private LayoutInflater inflater;
    private Context context;

    public ListeVaccinAdaptor(Context context, List<Vaccin> listes){
        this.listes=listes;
        this.context=context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listes.size();
    }

    @Override
    public Vaccin getItem(int i) {
        return this.listes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.liste_item_vaccin, null);
        final Vaccin vaccin=this.getItem(i);

        TextView dateVaccin=(TextView)view.findViewById(R.id.dateVaccination);
        TextView nomVaccin=(TextView)view.findViewById(R.id.nomVaccination);
        TextView centre=(TextView)view.findViewById(R.id.centre);

        String nomV="Nom: "+vaccin.getNomVaccin();
        String dateV="Date: "+vaccin.getDateVaccin();
        String siteV="Site: "+vaccin.getCentre();

        nomVaccin.setText(nomV);
        dateVaccin.setText(dateV);
        centre.setText(siteV);


        return view;
    }
}
