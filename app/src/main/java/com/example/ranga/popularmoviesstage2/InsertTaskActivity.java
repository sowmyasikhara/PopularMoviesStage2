package com.example.ranga.popularmoviesstage2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import Favourite.AppDatabase;
import Favourite.MovieUser;

public class InsertTaskActivity extends AppCompatActivity {


    private int movie_id;
    private String movie_title_name;
    private String movie_release_date;
    private String movie_vote_avg;
    private String movie_overview;
    private String poster;
    private MovieUser movieUser;
  //  String path = "https://image.tmdb.org/t/p/w500";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Intent i = getIntent();



        movie_id = i.getExtras().getInt("movie_id");
        movie_title_name = i.getExtras().getString("movie_title_name");
        movie_vote_avg = i.getExtras().getString("movie_vote_avg");
        movie_release_date = i.getExtras().getString("movie_release_date");
        movie_overview = i.getExtras().getString("movie_overview");
        poster = i.getExtras().getString("movie_poster_path");

        GetMovieIdTask gt = new GetMovieIdTask();
        gt.execute();

    }

    @Override
    protected void onStart() {
        //checkIfInsertedBefore(movie_id);

        super.onStart();

    }

    class InsertTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {



            //creating a task


            movieUser = new MovieUser(movie_id, movie_title_name, movie_release_date, movie_vote_avg, poster, movie_overview);

            movieUser.setMovieId(movie_id);
            movieUser.setMovie_name(movie_title_name);
            movieUser.setMovie_release_date(movie_release_date);
            movieUser.setMovie_vote_average(movie_vote_avg);
            movieUser.setMovie_thumbnail(poster);
            movieUser.setMovie_overview(movie_overview);

            //adding to database

                AppDatabase.getInstance(getApplicationContext()).getAppDatabase()
                        .movieDao()
                        .addMovieDetails(movieUser);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(getApplicationContext(), "Favourite Movie Added", Toast.LENGTH_LONG).show();
        }
    }

    //check if inserted before

    class GetMovieIdTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String movie_name = AppDatabase.getInstance(getApplicationContext()).getAppDatabase()
                    .movieDao()
                    .getMovieIdDetailsFromDB(movie_id);

            return movie_name;
        }

        @Override
        protected void onPostExecute(String tasks) {


            super.onPostExecute(tasks);
         //   Log.d("movie_nAME INSERT",tasks);
            if (tasks != null) {
                Toast.makeText(getApplicationContext(), "Making unfavourite", Toast.LENGTH_LONG).show();
                Intent i =new Intent(getApplicationContext(), DeleteTaskActivity.class);
                i.putExtra("movie_id",movie_id);
                startActivity(i);

            } else if ((tasks == null) || (tasks == " ")) {
                Toast.makeText(getApplicationContext(), "Adding to favourites", Toast.LENGTH_LONG).show();
                InsertTask gt = new InsertTask();
                gt.execute();

            }
        }


    }




}


