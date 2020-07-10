package com.project.findme.HelperClasses.HomeAdapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.findme.R;

import java.util.ArrayList;

public class GeolocationAdapter extends RecyclerView.Adapter<GeolocationAdapter.AdapterGeolocationViewHolder> {

    ArrayList<GeolocationHelperClass> geolocation_items;

    public GeolocationAdapter(ArrayList<GeolocationHelperClass> geolocation_items){
        this.geolocation_items = geolocation_items;
    }

    @NonNull
    @Override
    public AdapterGeolocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.geolocation_card_design,parent,false);
        AdapterGeolocationViewHolder viewHolder = new AdapterGeolocationViewHolder(view);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull AdapterGeolocationViewHolder holder, int position) {
        GeolocationHelperClass helperClass = geolocation_items.get(position);
        holder.imageView.setImageResource(helperClass.getImage());
        holder.textView.setText(helperClass.getTitle());
        holder.relativeLayout.setBackground(helperClass.getGradientDrawable());
    }

    @Override
    public int getItemCount() {
        return geolocation_items.size();
    }


    public static class AdapterGeolocationViewHolder extends RecyclerView.ViewHolder{
            RelativeLayout relativeLayout;
            ImageView imageView;
            TextView textView;


        public AdapterGeolocationViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout=itemView.findViewById(R.id.background_gradient);
            imageView=itemView.findViewById(R.id.geolocation_image);
            textView=itemView.findViewById(R.id.geolocation_title);

        }
    }

}
