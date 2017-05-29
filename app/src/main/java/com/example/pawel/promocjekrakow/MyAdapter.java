package com.example.pawel.promocjekrakow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Pawel on 27.05.2017.
 */

public class MyAdapter extends ArrayAdapter<News> {


    public MyAdapter(Context context, int item, List<News> newsList) {
        super(context, 0, newsList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        final News n = getItem(position);
        Button more = (Button) view.findViewById(R.id.more);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        TextView tvLoc = (TextView) view.findViewById(R.id.tvLoc);
        TextView tvStrPrice = (TextView) view.findViewById(R.id.tvStrPrice);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        tvStrPrice.setPaintFlags(tvStrPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvName.setText(n.mTitle);
        tvPrice.setText(n.mPrice);
        tvLoc.setText(n.mLoc);
        tvStrPrice.setText(n.mStrPrice);
        tvDesc.setText(n.mDesc);

//нажатие кнопки конктретного елемента листвью
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri site = Uri.parse(n.getUrl());
                Intent siteIntent = new Intent(Intent.ACTION_VIEW,site);
                v.getContext().startActivity(siteIntent);
            }

        });


        return view;
    }


}