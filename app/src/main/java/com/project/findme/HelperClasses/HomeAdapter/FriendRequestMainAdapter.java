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

public class FriendRequestMainAdapter extends RecyclerView.Adapter<FriendRequestMainAdapter.FriendRequestMainAdapterViewHolder> {
    ArrayList<FriendHelperClass> list_friends;
    private FriendreqListener friendreqListener;
    private int[] mMaterialColors;

    public FriendRequestMainAdapter(ArrayList<FriendHelperClass> list_friends , FriendreqListener friendreqListener) {

        this.list_friends=list_friends;
        this.friendreqListener=friendreqListener;
    }


    @NonNull
    @Override
    public FriendRequestMainAdapter.FriendRequestMainAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_card,parent,false);
        FriendRequestMainAdapter.FriendRequestMainAdapterViewHolder friendRequestMainAdapterViewHolder = new FriendRequestMainAdapter.FriendRequestMainAdapterViewHolder(view,friendreqListener);
        return friendRequestMainAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestMainAdapter.FriendRequestMainAdapterViewHolder holder, int position) {
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

    public static class FriendRequestMainAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MaterialLetterIcon image;
        TextView username;
        FriendreqListener friendreqListener;
        private int[] mMaterialColors;
        Button accept_btn;
        Button refuse_btn;



        public FriendRequestMainAdapterViewHolder(@NonNull View itemView , FriendreqListener friendreqListener) {
            super(itemView);
            image=itemView.findViewById(R.id.activity_profile_single);
            username=itemView.findViewById(R.id.activity_title_single);
            mMaterialColors=itemView.getResources().getIntArray(R.array.colors);
            accept_btn=itemView.findViewById(R.id.action_btn_accept);
            refuse_btn=itemView.findViewById(R.id.action_btn_refuse);
            this.friendreqListener=friendreqListener;
            itemView.setOnClickListener(this);
            accept_btn.setOnClickListener(this);
            refuse_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==accept_btn.getId()){
                friendreqListener.onAccept(getAdapterPosition());
            }
            else if(v.getId()==refuse_btn.getId()){
                friendreqListener.onRefuse(getAdapterPosition());
            }
            else{
                friendreqListener.onProfileClick(getAdapterPosition());
            }



        }


    }
    public interface FriendreqListener {
        void onProfileClick(int position);
        void onAccept(int position);
        void onRefuse(int position);
    }
}
