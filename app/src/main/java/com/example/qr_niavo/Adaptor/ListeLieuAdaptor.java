package com.example.qr_niavo.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qr_niavo.Models.Lieu;
import com.example.qr_niavo.Models.Test;
import com.example.qr_niavo.R;

import java.util.List;

public class ListeLieuAdaptor extends BaseAdapter {
    List<Lieu> listes;
    private LayoutInflater inflater;
    private Context context;

    public ListeLieuAdaptor(Context context, List<Lieu> listes){
        this.listes=listes;
        this.context=context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listes.size();
    }

    @Override
    public Lieu getItem(int i) {
        return this.listes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.liste_item_lieu, null);
        final Lieu test=this.getItem(i);

        TextView datePassage=(TextView)view.findViewById(R.id.datePassage);
        TextView lieu=(TextView)view.findViewById(R.id.lieu);

        datePassage.setText(test.getDatePassage());
        lieu.setText(test.getLieu());
        return view;
    }
}
