package LiveData;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Favourite.AppDatabase;
import Favourite.MovieDao;
import Favourite.MovieDatabase;
import Favourite.MovieUser;


public class MovieRepository {

    private MovieDao movieDao;
    private MovieDatabase movieDatabase;
    private LiveData<List<MovieUser>> mFavMoviesList;
    private AppDatabase appDatabase;
    private ArrayList<MovieUser> listfromdb;

    public MovieRepository(Application application){
        appDatabase = AppDatabase.getInstance(application);
        movieDao = appDatabase.getAppDatabase().movieDao();
        mFavMoviesList = movieDao.getAll();
        Log.d("moviedao.getall",mFavMoviesList.toString());
    }

    public LiveData<List<MovieUser>> getFavMoviesList() {
        mFavMoviesList = new MutableLiveData<>();
        new GetTasks().execute();
        Log.d("mutable live data",mFavMoviesList.toString());
        return  mFavMoviesList;
    }

    //get tasks from db
    class GetTasks extends AsyncTask<Void, Void, LiveData<List<MovieUser>>> {
        @Override
        protected LiveData<List<MovieUser>> doInBackground(Void... voids) {
            List<MovieUser> listFromDB = (List<MovieUser>) appDatabase.getAppDatabase()
                    .movieDao()
                    .getAll();


            return (LiveData<List<MovieUser>>) listFromDB;
        }

        @Override
        protected void onPostExecute(LiveData<List<MovieUser>> listLiveData) {
            super.onPostExecute(listLiveData);
            mFavMoviesList = listLiveData;

            //mFavMoviesList.setValue(listLiveData);
        }
    }

}
