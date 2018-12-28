package com.example.ranga.popularmoviesstage2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.Movie;

public class TrailerTaskAdapter extends RecyclerView.Adapter<TrailerTaskAdapter.ViewHolder> {


    private Context mTrailerContext;
    private LayoutInflater mTrailerInflater;
    private ItemClickListener mTrailerClick;
    ArrayList<Movie> mTrailerList;
    String[] trailerViews;

    final static String LOG_TAG = "TrailerTaskAdapter";

    private int adapterPosition;

    TextView trailerName;
    String[] trailerNames;


    public TrailerTaskAdapter(Context c,
                              ArrayList<Movie> tasks,
                              ItemClickListener listener) {
        mTrailerList = new ArrayList<>();
        mTrailerList = tasks;
        Log.d("trailersize", String.valueOf(mTrailerList.size()));
        this.mTrailerContext = c;
        mTrailerClick = listener;
        mTrailerInflater = LayoutInflater.from(mTrailerContext);

        trailerViews= new String[mTrailerList.size()];
        trailerNames = new String[mTrailerList.size()];

        for(int i = 0;i<
                mTrailerList.size();i++){
            trailerViews[i] = mTrailerList.get(i).getTrailer_key();
            trailerNames[i] = mTrailerList.get(i).getTrailer_name();

        }


    }


    @Override
    public int getItemCount() {
        Log.d("trailerlength", String.valueOf(trailerViews.length));
        return trailerViews.length;
    }
    @Override
    @NonNull
    public TrailerTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mTrailerInflater.inflate(R.layout.recyclerview_trailer, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TrailerTaskAdapter.ViewHolder holder, int position) {


        Picasso.get()
                .load(R.drawable.play_button)
                .error(R.drawable.picture_unavailable)
                .placeholder(R.drawable.loading_icon1)
                .into(holder.trailerVideoView);

        holder.trailerName.setText(trailerNames[position]);
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        ImageView trailerVideoView;
        TextView trailerName;
        ViewHolder(View itemView) {
            super(itemView);
            trailerVideoView = itemView.findViewById(R.id.trailerVideo_id);
            trailerName = itemView.findViewById(R.id.trailer_name_id);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mTrailerClick != null)
                adapterPosition = getAdapterPosition();
            mTrailerClick.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    void setClickListener(TrailerTaskAdapter.ItemClickListener itemClickListener) {
        this.mTrailerClick = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}


