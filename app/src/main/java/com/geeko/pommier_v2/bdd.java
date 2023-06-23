package com.geeko.pommier_v2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface bdd {
    @Delete
    void delete(Emplacement emplacement);

    @Query("DELETE FROM `emplacement`")
    void deleteALL();

    @Query("SELECT * FROM `emplacement` WHERE ref = :ref")
    Emplacement findByRef(String ref);
    @Query("SELECT * FROM `emplacement` WHERE uid = :uid")
    Emplacement findByUID(Long uid);

    @Query("SELECT * FROM `emplacement`")
    List<Emplacement> getAll();
    @Insert
    void insert(Emplacement emp);

    //List<Emplacement> loadAllByIds(int[] userIds);

    @Query("UPDATE `emplacement` SET emplacement_palette = :emp WHERE uid = :uid")
    void updateEmplacement(String emp, Long uid);

    @Query("UPDATE `emplacement` SET note = :note WHERE uid = :uid")
    void updateNote(String note, Long uid);

    @Query("UPDATE `emplacement` SET quantite = :quantity WHERE uid = :uid")
    void updateQuantity(String quantity, Long uid);
    @Query("UPDATE `emplacement` SET ref = :ref WHERE uid = :uid")
    void updateRef(String ref, Long uid);
}