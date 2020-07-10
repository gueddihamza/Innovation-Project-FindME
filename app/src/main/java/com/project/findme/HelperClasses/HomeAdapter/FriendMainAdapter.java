package com.project.findme.HelperClasses.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.project.findme.R;

import java.util.ArrayList;

public class FriendMainAdapter extends RecyclerView.Adapter<FriendMainAdapter.FriendMainAdapterViewHolder> {
    ArrayList<FriendHelperClass> list_friends;
    private FriendListener friendListener;
    private int[] mMaterialColors;

    public FriendMainAdapter(ArrayList<FriendHelperClass> list_friends , FriendListener friendListener) {

        this.list_friends=list_friends;
        this.friendListener=friendListener;
    }


    @NonNull
    @Override
    public FriendMainAdapter.FriendMainAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_card,parent,false);
        FriendMainAdapter.FriendMainAdapterViewHolder friendMainAdapterViewHolder = new FriendMainAdapter.FriendMainAdapterViewHolder(view,friendListener);
        return friendMainAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendMainAdapter.FriendMainAdapterViewHolder holder, int position) {
        FriendHelperClass helperClass = list_friends.get(position);
        holder.username.setText(helperClass.getUsername());
        holder.image.setInitials(true);
        holder.image.setInitialsNumber(2);
        holder.image.setLetterSize(18);
        holder.image.setLetter(helperClass.getUsername());
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

    public static class FriendMainAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MaterialLetterIcon image;
        TextView username;
        FriendListener friendListener;
        Button open_chat_btn;
        private int[] mMaterialColors;


        public FriendMainAdapterViewHolder(@NonNull View itemView , FriendListener friendListener) {
            super(itemView);
            image=itemView.findViewById(R.id.ms_image);
            username=itemView.findViewById(R.id.ms_username);
            mMaterialColors=itemView.getResources().getIntArray(R.array.colors);
            open_chat_btn=itemView.findViewById(R.id.action_open_chat);
            this.friendListener=friendListener;
            open_chat_btn.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==open_chat_btn.getId())
            friendListener.onOpenChat(getAdapterPosition());
            else
                friendListener.onProfileOpen(getAdapterPosition());
        }


    }
    public interface FriendListener {
        void onOpenChat(int position);
        void onProfileOpen(int position);
    }
}
