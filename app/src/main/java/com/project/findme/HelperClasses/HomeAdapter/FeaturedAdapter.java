package com.project.findme.HelperClasses.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.findme.R;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    ArrayList<FeaturedHelperClass> featuredLocations;
    private onFeaturedListener FeaturedListener;

    public FeaturedAdapter(ArrayList<FeaturedHelperClass> featuredLocations , onFeaturedListener featuredListener) {
        this.featuredLocations = featuredLocations;
        this.FeaturedListener = featuredListener;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design, parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view, FeaturedListener);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        FeaturedHelperClass featuredHelperClass = featuredLocations.get(position);

        holder.image.setImageResource(featuredHelperClass.getImage());
        holder.title.setText(featuredHelperClass.getTitle());
        holder.desc.setText(featuredHelperClass.getDescription());
    }

    @Override
    public int getItemCount()
    {
        return featuredLocations.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        onFeaturedListener onFeaturedlistener;
        ImageView image;
        TextView title;
        TextView desc;



        public FeaturedViewHolder(@NonNull View itemView , onFeaturedListener onFeaturedlistener) {
            super(itemView);

            image=itemView.findViewById(R.id.featured_image);
            title=itemView.findViewById(R.id.featured_title);
            desc=itemView.findViewById(R.id.featured_desc);
            this.onFeaturedlistener=onFeaturedlistener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFeaturedlistener.onFeaturedClick(getAdapterPosition());
        }
    }

    public interface onFeaturedListener{
        void onFeaturedClick(int position);
    }
}
