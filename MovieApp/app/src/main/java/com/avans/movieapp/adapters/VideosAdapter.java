package com.avans.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.R;
import com.avans.movieapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private ArrayList<Movie> movies;

    public VideosAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie m = movies.get(position);

        holder.tvTitle.setText(m.getTitle());
        holder.tvInfo.setText(String.format("%d - %s", m.getId(), m.getVoteAverage())); //Just for testing
        String overview = m.getOverview();
        holder.tvOverview.setText(overview.substring(0, Math.min(overview.length(), 60))+"...");

        if(m.getImageUrlPoster().length() > 10) {
            Picasso.get().load(m.getImageUrlPoster()).into(holder.ivImage);
        }
        //More items...
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvOverview;
        TextView tvInfo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
