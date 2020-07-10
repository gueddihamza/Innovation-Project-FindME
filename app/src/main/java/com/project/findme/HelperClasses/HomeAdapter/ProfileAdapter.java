package com.project.findme.HelperClasses.HomeAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;

import java.util.ArrayList;
import java.util.Random;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    ArrayList<UserInfo> userInfos;
    private UserListener userListener;
    private int[] mMaterialColors;

    public ProfileAdapter(ArrayList<UserInfo> userInfos, UserListener userListener) {
        this.userInfos = userInfos;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, userListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserInfo userInfo = userInfos.get(position);
        holder.fullname.setText(userInfo.getFullName());
        holder.username.setText(userInfo.getUsername());
        holder.icon.setInitials(true);
        holder.icon.setInitialsNumber(2);
        holder.icon.setLetterSize(18);
        holder.icon.setLetter(userInfo.getFullName());
        mMaterialColors=holder.mMaterialColors;

        char firstChar = holder.icon.getLetter().charAt(0);
        if(firstChar == 'A' || firstChar == 'B' || firstChar == 'C' ) {
            holder.icon.setShapeColor(mMaterialColors[0]);
        }

        else if(firstChar == 'D' || firstChar == 'E' || firstChar == 'F' ) {
            holder.icon.setShapeColor(mMaterialColors[1]);
        }

        else if(firstChar == 'G' || firstChar == 'H' || firstChar == 'I' ) {
            holder.icon.setShapeColor(mMaterialColors[2]);
        }

        else if(firstChar == 'J' || firstChar == 'K' || firstChar == 'L' ) {
            holder.icon.setShapeColor(mMaterialColors[3]);
        }

        else if(firstChar == 'M' || firstChar == 'N') {
            holder.icon.setShapeColor(mMaterialColors[4]);
        }

        else if(firstChar == 'O' || firstChar == 'P') {
            holder.icon.setShapeColor(mMaterialColors[5]);
        }

        else if(firstChar == 'Q' || firstChar == 'R') {
            holder.icon.setShapeColor(mMaterialColors[6]);
        }

        else if(firstChar == 'S' || firstChar == 'T') {
            holder.icon.setShapeColor(mMaterialColors[7]);
        }

        else if(firstChar == 'U' || firstChar == 'V') {
            holder.icon.setShapeColor(mMaterialColors[8]);
        }

        else if(firstChar == 'W' || firstChar == 'X') {
            holder.icon.setShapeColor(mMaterialColors[9]);
        }

        else if(firstChar == 'Y' || firstChar == 'Z') {
            holder.icon.setShapeColor(mMaterialColors[10]);
        }
    }


    @Override
    public int getItemCount() {
        return userInfos.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView fullname, username;
        MaterialLetterIcon icon;
        UserListener userlistener;
        private int[] mMaterialColors;

        public MyViewHolder(@NonNull View itemView, UserListener userlistener) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullname_text);
            username = itemView.findViewById(R.id.username_text);
            icon = itemView.findViewById(R.id.letter_icon);
            mMaterialColors=itemView.getResources().getIntArray(R.array.colors);
            this.userlistener = userlistener;
            itemView.setOnClickListener(this);
        }



    @Override
    public void onClick(View v) {

        userlistener.onProfileClick(getAdapterPosition());
    }
    }


    public interface UserListener {
        void onProfileClick(int position);
    }
}