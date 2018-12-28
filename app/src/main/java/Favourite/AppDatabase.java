package Favourite;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppDatabase {


    private Context mCtx;
    private static AppDatabase mInstance;

    //our app database object
    private MovieDatabase movieAppDatabase;

    private static List<MovieUser> favMoviesList;

    private AppDatabase(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MovieDB is the name of the database
        movieAppDatabase = Room.databaseBuilder(mCtx, MovieDatabase.class, "MovieDB")
                .fallbackToDestructiveMigration()
               // .addCallback(roomCallback)
                .build();
        Log.d("database created","in appdatabase");
    }

    public static synchronized AppDatabase getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new AppDatabase(mCtx);
        }
        return mInstance;
    }

    public MovieDatabase getAppDatabase() {
        return movieAppDatabase;
    }



    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            favMoviesList = new ArrayList();
            favMoviesList = (List<MovieUser>) new GetTasks().execute();
        }
    };




    //populate db when opened
    static class GetTasks extends AsyncTask<Void, Void, List<MovieUser>> {


        @Override
        protected List<MovieUser> doInBackground(Void... voids) {
           favMoviesList = (List<MovieUser>) mInstance.getAppDatabase()
                    .movieDao()
                    .getAll();


            return favMoviesList;
        }

    }

}
