package com.example.sontbv.roomexample;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = CASCADE))
public class Repo {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String url;
    public int userId;

    public Repo(String name, String url, int userId) {
        this.name = name;
        this.url = url;
        this.userId = userId;
    }
}
