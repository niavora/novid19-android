package com.example.qr_niavo.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qr_niavo.Models.Test;
import com.example.qr_niavo.R;

import java.util.List;

public class ListeTestAdaptor  extends BaseAdapter {
    List<Test> listes;
    private LayoutInflater inflater;
    private Context context;

    public ListeTestAdaptor(Context context, List<Test> listes){
        this.listes=listes;
        this.context=context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listes.size();
    }

    @Override
    public Test getItem(int i) {
        return this.listes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.liste_item, null);
        final Test test=this.getItem(i);

        TextView dateTest=(TextView)view.findViewById(R.id.dateTest);
        TextView lieuTest=(TextView)view.findViewById(R.id.lieuTest);

        dateTest.setText(test.getDateTest());
        lieuTest.setText(test.getLieuTest());
        return view;
    }
}
