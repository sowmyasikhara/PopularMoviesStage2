package LiveData;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;


import java.util.List;

import Favourite.MovieUser;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private LiveData<List<MovieUser>> mFavMoviesList;


    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
       // mFavMoviesList = movieRepository.getFavMoviesList();
    }

    public LiveData<List<MovieUser>> getmFavMoviesList() {

        if(mFavMoviesList == null){
            mFavMoviesList = new MutableLiveData<>();
            mFavMoviesList = movieRepository.getFavMoviesList();
        }
        return mFavMoviesList;
    }
}
