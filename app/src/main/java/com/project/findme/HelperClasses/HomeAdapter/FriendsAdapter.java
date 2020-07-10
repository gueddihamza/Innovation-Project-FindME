package com.project.findme.HelperClasses.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.project.findme.R;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsAdapterViewHolder> {

    ArrayList<FriendsHelperClass> list_friends;
    private int[] mMaterialColors;

    public FriendsAdapter(ArrayList<FriendsHelperClass> list_friends) {

    this.list_friends=list_friends;

    }


    @NonNull
    @Override
    public FriendsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_card_design,parent,false);
        FriendsAdapterViewHolder friendsAdapterViewHolder = new FriendsAdapterViewHolder(view);
        return friendsAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendsAdapterViewHolder holder, int position) {
        FriendsHelperClass helperClass = list_friends.get(position);

        holder.fullname.setText(helperClass.getFullname());
        holder.username.setText(helperClass.getUsername());
        holder.image.setInitials(true);
        holder.image.setInitialsNumber(2);
        holder.image.setLetterSize(18);
        holder.image.setLetter(helperClass.getFullname());
        mMaterialColors=holder.mMaterialColors;

        char firstChar = holder.image.getLetter().charAt(0);
        if(firstChar == 'A' || firstChar == 'B' || firstChar == 'C' ) {
            holder.image.setShapeColor(mMaterialColors[0]);
        }

        else if(firstChar == 'D' || firstChar == 'E' || firstChar == 'F' ) {
            holder.image.setShapeColor(mMaterialColors[1]);
        }

        else if(firstChar == 'G' || firstChar == 'H' || firstChar == 'I' ) {
            holder.image.setShapeColor(mMaterialColors[2]);
        }

        else if(firstChar == 'J' || firstChar == 'K' || firstChar == 'L' ) {
            holder.image.setShapeColor(mMaterialColors[3]);
        }

        else if(firstChar == 'M' || firstChar == 'N') {
            holder.image.setShapeColor(mMaterialColors[4]);
        }

        else if(firstChar == 'O' || firstChar == 'P') {
            holder.image.setShapeColor(mMaterialColors[5]);
        }

        else if(firstChar == 'Q' || firstChar == 'R') {
            holder.image.setShapeColor(mMaterialColors[6]);
        }

        else if(firstChar == 'S' || firstChar == 'T') {
            holder.image.setShapeColor(mMaterialColors[7]);
        }

        else if(firstChar == 'U' || firstChar == 'V') {
            holder.image.setShapeColor(mMaterialColors[8]);
        }

        else if(firstChar == 'W' || firstChar == 'X') {
            holder.image.setShapeColor(mMaterialColors[9]);
        }

        else if(firstChar == 'Y' || firstChar == 'Z') {
            holder.image.setShapeColor(mMaterialColors[10]);
        }
    }


    @Override
    public int getItemCount() {
        return list_friends.size();
    }

    public static class FriendsAdapterViewHolder extends RecyclerView.ViewHolder {
        MaterialLetterIcon image;
        TextView fullname;
        TextView username;
        private int[] mMaterialColors;


        public FriendsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.ms_image);
            fullname=itemView.findViewById(R.id.ms_fullname);
            username=itemView.findViewById(R.id.ms_username);
            mMaterialColors=itemView.getResources().getIntArray(R.array.colors);
        }
    }
}
