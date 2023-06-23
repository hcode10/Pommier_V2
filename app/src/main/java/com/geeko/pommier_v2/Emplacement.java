package com.geeko.pommier_v2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Emplacement {
    @ColumnInfo(name = "emplacement_palette")
    public String emplacementPalette;
    @ColumnInfo(name = "note")
    public String note;
    @ColumnInfo(name = "quantite")
    public String quantite;
    @ColumnInfo(name = "ref")
    public String ref;
    @PrimaryKey
    public int uid;

}
