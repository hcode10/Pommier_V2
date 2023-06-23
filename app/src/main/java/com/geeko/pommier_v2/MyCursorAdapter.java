package com.geeko.pommier_v2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {
    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override // android.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_row, parent, false);
    }

    @Override // android.widget.CursorAdapter
    public void bindView(View view, Context context, Cursor cursor) {
        TextView Labelref = (TextView) view.findViewById(R.id.labelRef);
        TextView Labelquantity = (TextView) view.findViewById(R.id.labelQuantite);
        TextView Labelemp = (TextView) view.findViewById(R.id.labelEmplacement);
        String ref = cursor.getString(cursor.getColumnIndexOrThrow("ref"));
        String quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantite"));
        String emp = cursor.getString(cursor.getColumnIndexOrThrow("emplacement_palette"));
        Labelref.setText(ref);
        Labelquantity.setText("Quantité : " + quantity);
        Labelemp.setText("Emplacement : " + emp);
    }
}
