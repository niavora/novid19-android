package com.example.qr_niavo.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qr_niavo.Models.MessageModel;
import com.example.qr_niavo.R;

import java.util.List;

public class MessageAdaptor extends BaseAdapter {
    List<MessageModel> listes;
    private LayoutInflater inflater;
    private Context context;

    public MessageAdaptor(Context context, List<MessageModel> listes){
        this.listes=listes;
        this.context=context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listes.size();
    }

    @Override
    public MessageModel getItem(int i) {
        return this.listes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_message_item, null);
        final MessageModel test=this.getItem(i);

        TextView message=(TextView)view.findViewById(R.id.message);


        message.setText(test.getMessage());
        return view;
    }
}
