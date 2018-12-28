package com.example.ranga.popularmoviesstage2;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Favourite.MovieUser;

public class FavouriteTaskAdapter extends RecyclerView.Adapter<FavouriteTaskAdapter.ViewHolder> {


    String path = "https://image.tmdb.org/t/p/w500";
    private Context mFavContext;
    private RecyclerViewAdapter.ItemClickListener mFavClickListener;
    private LayoutInflater mFavInflater;
    ArrayList<MovieUser> favMoviesList;
    private String[] movie_images;
    final static String LOG_TAG = "FavouriteTaskAdapter";
    private String movie_id;
    private int adapterPosition;


    public FavouriteTaskAdapter(Context c,
                                ArrayList<MovieUser> tasks,
                                MainActivity listener) {

        favMoviesList = tasks;
        this.mFavContext = c;
        this.mFavClickListener =  listener;



        movie_images = new String[favMoviesList.size()];
        for (int i = 0; i < favMoviesList.size(); i++) {
            String poster = favMoviesList.get(i).getMovie_thumbnail();
           // poster = path + poster;
            movie_images[i] = poster;

        }
    }

     public FavouriteTaskAdapter(Context mContext, ArrayList<MovieUser> movieUsers, Observer<List<MovieUser>> observer) {

         this.mFavContext = mContext;
         favMoviesList = movieUsers;
         mFavInflater = LayoutInflater.from(mFavContext);
            Log.d("mutable live adapter",favMoviesList.toString());
         movie_images = new String[favMoviesList.size()];
         for (int i = 0; i < favMoviesList.size(); i++) {
             String poster = favMoviesList.get(i).getMovie_thumbnail();
             movie_images[i] = poster;

         }
     }

     public FavouriteTaskAdapter(MainActivity listener){
        Log.d("listener set","fav adaoter");
        this.mFavClickListener = (RecyclerViewAdapter.ItemClickListener) listener;
     }



    @Override
    public int getItemCount() {
        return favMoviesList.size();
    }
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mFavInflater.inflate(R.layout.recyclerview_favouritetask, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get()
                .load(movie_images[position])
                .error(R.drawable.picture_unavailable)
                .placeholder(R.drawable.loading_icon1)
                .into(holder.movieImagesView);

    }

    public void notifyWhenChanged(ArrayList<MovieUser> changedList){
        favMoviesList = changedList;
        notifyDataSetChanged();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        ImageView movieImagesView;

        ViewHolder(View itemView) {
            super(itemView);
            movieImagesView = itemView.findViewById(R.id.imagesViewId);
          itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mFavClickListener != null)
                adapterPosition = getAdapterPosition();
            Log.d("adapter listener", toString().valueOf(adapterPosition));

            mFavClickListener.onFavItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    String getItem(int id) {
        return movie_images[id];
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mFavClickListener = (RecyclerViewAdapter.ItemClickListener) itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onFavItemClick(View view, int position);
    }
}


