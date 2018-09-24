package com.example.sontbv.roomexample;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface RepoDao {

    @Query("SELECT * FROM repo")
    Single<List<Repo>> getAllRepos();

    @Insert
    void insert(Repo... repos);

    @Insert
    void insertListRecord(List<Repo> repos);

    @Update
    void update(Repo... repos);

    @Delete
    void delete(Repo... repos);

    @Query("SELECT * FROM repo WHERE userId=:userId")
    Single<List<Repo>> findRepositoriesForUser(final int userId);
}
