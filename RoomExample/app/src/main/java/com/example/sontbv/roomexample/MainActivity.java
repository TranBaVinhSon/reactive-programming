package com.example.sontbv.roomexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Repo repo = new Repo("rails123", "123", 1);

        final List<Repo> repos = new ArrayList<>();
        final User user = new User("sontbv", "sontbv");
        repos.add(new Repo("react", "xyz", 1));
        repos.add(new Repo("angular", "xyz", 1));
        repos.add(new Repo("vue", "xyz", 1));
        repos.add(new Repo("rails", "xyz", 1));
        repos.add(new Repo("php", "xyz", 1));

        if(isFirstTime()) {
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    RepoDatabase.getInstance(getApplicationContext())
                            .getUserDao()
                            .insert(user);
                    RepoDatabase.getInstance(getApplicationContext())
                            .getRepoDao()
                            .insertListRecord(repos);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "subscribe");
                        }
                        @Override
                        public void onComplete() {
                            Log.d(TAG, TAG);
                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getMessage());
                        }
                    });
        } else {
            compositeDisposable.add(
                RepoDatabase.getInstance(getApplicationContext())
                    .getRepoDao()
                    .getAllRepos()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Repo>>() {
                        @Override
                        public void onSuccess(List<Repo> repos) {
                            Log.d(TAG, repos.size() + "");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getMessage());
                        }
                    })
            );

            compositeDisposable.add(
                RepoDatabase.getInstance(getApplicationContext())
                    .getRepoDao()
                    .findRepositoriesForUser(1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Repo>>() {
                        @Override
                        public void onSuccess(List<Repo> repos) {
                            Log.d(TAG, repos.size() + "");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getMessage());
                        }
                    })
            );


        }
    }

    private boolean isFirstTime(){
        Boolean isFirstTime = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstTime", true);

        if(isFirstTime) {
            SharedPreferences.Editor editor = getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        }
        return isFirstTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
