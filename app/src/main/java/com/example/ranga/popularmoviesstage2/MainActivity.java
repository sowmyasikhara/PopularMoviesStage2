package com.example.ranga.popularmoviesstage2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Favourite.AppDatabase;
import Favourite.MovieUser;
import LiveData.MovieRepository;
import LiveData.MovieViewModel;
import model.Movie;
import utils.MovieNetworkUtils;
import utils.ParseMovieJSONDetails;

public class MainActivity extends AppCompatActivity  implements RecyclerViewAdapter.ItemClickListener {

    private RecyclerViewAdapter mAdapter;
    private FavouriteTaskAdapter mFavAdapter;
    private GridLayoutManager gridLayoutManager;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private MovieViewModel viewModel;


    private ImageView imageview;
    Movie obj;

    ArrayList<Movie> allMoviesList;
    ArrayList<MovieUser> favMoviesList;
    private static String response1;
    private String sortByValue;
    public String sortByPref;
    final static String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mContext = MainActivity.this;
        int itemThatWasClickedId = item.getItemId();

        if (itemThatWasClickedId == R.id.popular_movie) {
            sortByPref = getString(R.string.popular);
            //sortByValue = String.valueOf(R.string.sort_by_popularity);
            sortByValue = getString(R.string.sort_by_popularity);
            Log.d(LOG_TAG, sortByValue);

            String textToShow = "showing popular movies";
            Toast.makeText(mContext, textToShow, Toast.LENGTH_LONG).show();
            getMovieDetails(sortByValue, sortByPref);

        } else {
            if (itemThatWasClickedId == R.id.top_rated) {

                sortByPref = getString(R.string.top_rated);
                sortByValue = String.valueOf(R.string.sort_by_topRated);
                Log.d(LOG_TAG, sortByValue);
                String textToShow = "showing top rated movies";
                Toast.makeText(mContext, textToShow, Toast.LENGTH_LONG).show();
                getMovieDetails(sortByValue, sortByPref);
            }

            if (itemThatWasClickedId == R.id.favourite) {

                favMoviesList = new ArrayList<MovieUser>();
                loadFavMovieData();
               // new GetTasks().execute();


            }
            return true;
        }


        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onStart() {

        //default loading popular movies
        sortByPref = getString(R.string.popular);

        //checking if internet connection is available or not
        if (isNetworkAvailable()) {
            //getting list of movie details
            getMovieDetails(sortByValue, sortByPref);

        } else {
            Toast.makeText(mContext, "Oops,No Internet!Please check Internet Connection", Toast.LENGTH_SHORT).show();
        }
        super.onStart();
    }

    private void loadFavMovieData(){
        Log.d("in load fav ","movie data");
      //  mFavAdapter = new FavouriteTaskAdapter(mContext,favMoviesList,this);
       // mRecyclerView.setAdapter(mFavAdapter);

       //implementing view model for favourite movies
        //get view model


        viewModel = new MovieViewModel(getApplication());
        //favMoviesList = viewModel.getmFavMoviesList();
        viewModel = ViewModelProviders.of(
                MainActivity.this).get(MovieViewModel.class);
         viewModel.getmFavMoviesList().observe(MainActivity.this, new Observer<List<MovieUser>>() {
            @Override
            public void onChanged(@Nullable List<MovieUser> movieUsers) {
                Log.d("mutable live data",movieUsers.toString());
                mFavAdapter.notifyWhenChanged((ArrayList<MovieUser>) movieUsers);
                mRecyclerView.setAdapter(mFavAdapter);
            }

        });


    }




    private void getMovieDetails(String sortByValue,String sortByPref) {

        //checking if internet connection is available or not
        if(isNetworkAvailable()) {
            String sortBy = sortByValue;
            URL movieDBSearchURL = MovieNetworkUtils.buildUrl(sortBy,sortByPref);
            new MoviesAsyncTask().execute(movieDBSearchURL);
        }
        else
        {
            Toast.makeText(mContext,"Oops,No Internet!Please check Internet Connection",Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onItemClick(View view, int position) {
        Log.d("item clicked",allMoviesList.get(position).getPlot_synopsis());

        Intent i = new Intent(MainActivity.this, MovieDetailView.class);

        // Pass image index

        String title = allMoviesList.get(position).getTitle();
        i.putExtra("name", title);

        String release_date = allMoviesList.get(position).getRelease_date();
        i.putExtra("release_date", release_date);

        String vote_average = allMoviesList.get(position).getVote_average();
        i.putExtra("vote_average", vote_average);

        String overview = allMoviesList.get(position).getPlot_synopsis();
        i.putExtra("overview", overview);

        String poster_path = allMoviesList.get(position).getMovie_poster();

        i.putExtra("poster_path", poster_path);

        String movie_id = allMoviesList.get(position).getMovie_id();
        i.putExtra("movie_id",movie_id);

        startActivity(i);

    }

    @Override
    public void onFavItemClick(View view, int position) {
        Log.d("item clicked",favMoviesList.get(position).getMovie_overview());

        Intent i = new Intent(MainActivity.this, MovieDetailView.class);

        // Pass image index

        String title = favMoviesList.get(position).getMovie_name();
        i.putExtra("name", title);

        String release_date = favMoviesList.get(position).getMovie_release_date();
        i.putExtra("release_date", release_date);

        String vote_average = favMoviesList.get(position).getMovie_vote_average();
        i.putExtra("vote_average", vote_average);

        String overview = favMoviesList.get(position).getMovie_overview();
        i.putExtra("overview", overview);

        String poster_path = favMoviesList.get(position).getMovie_thumbnail();
        Log.d("path fav from db",poster_path);

        i.putExtra("poster_path", poster_path);

        int movie_id = favMoviesList.get(position).getMovieId();
        String movie_ids = String.valueOf(movie_id);
        i.putExtra("movie_id",movie_ids);

        startActivity(i);

    }

    public class MoviesAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];

            try {
                response1 = MovieNetworkUtils.getResponseFromHttpUrl(searchUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response1;
        }

        @Override
        protected void onPostExecute(String response1) {
            if (response1 != null && !response1.equals("")) {
                loadMoviesData(response1);

            }
        }
    }


    private void loadMoviesData(String response) {
       // RecyclerViewAdapter.ItemClickListener ItemClickListener = null;

        ParseMovieJSONDetails parse = new ParseMovieJSONDetails();

        allMoviesList = parse.getAllMoviesList(response);

        gridLayoutManager = new GridLayoutManager(mContext,2);

        mRecyclerView = (RecyclerView)findViewById(R.id.rvNumbers);
        mAdapter = new RecyclerViewAdapter(MainActivity.this, allMoviesList,this);
        // setting layout columns count
        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        }
        else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        }
        mRecyclerView.setAdapter(mAdapter);

    }

    private boolean isNetworkAvailable() {
        mContext = MainActivity.this;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }
}
