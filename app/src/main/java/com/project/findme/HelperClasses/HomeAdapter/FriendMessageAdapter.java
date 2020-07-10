package com.project.findme.HelperClasses.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.findme.R;

import java.util.ArrayList;
import java.util.List;

public class FriendMessageAdapter extends RecyclerView.Adapter<FriendMessageAdapter.GroupViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    ArrayList<FriendMessage> friendMessages;
    FirebaseUser user;
    private int[] mMaterialColors;

    public FriendMessageAdapter(ArrayList<FriendMessage> friendMessages) {
        this.friendMessages = friendMessages;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType== MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            GroupViewHolder groupViewHolder = new GroupViewHolder(view);
            return groupViewHolder;
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            GroupViewHolder groupViewHolder = new GroupViewHolder(view);
            return groupViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {

        FriendMessage friendMessage = friendMessages.get(position);

        holder.show_message.setText(friendMessage.getMessage());

        holder.username.setText(friendMessage.getUsername()+" says :");
        holder.profile_image.setInitials(true);
        holder.profile_image.setInitialsNumber(2);
        holder.profile_image.setLetterSize(18);
        holder.profile_image.setLetter(friendMessage.getUsername());
        mMaterialColors=holder.mMaterialColors;

        char firstChar = holder.profile_image.getLetter().charAt(0);
        if(firstChar == 'A' || firstChar == 'B' || firstChar == 'C' ) {
            holder.profile_image.setShapeColor(mMaterialColors[0]);
        }

        else if(firstChar == 'D' || firstChar == 'E' || firstChar == 'F' ) {
            holder.profile_image.setShapeColor(mMaterialColors[1]);
        }

        else if(firstChar == 'G' || firstChar == 'H' || firstChar == 'I' ) {
            holder.profile_image.setShapeColor(mMaterialColors[2]);
        }

        else if(firstChar == 'J' || firstChar == 'K' || firstChar == 'L' ) {
            holder.profile_image.setShapeColor(mMaterialColors[3]);
        }

        else if(firstChar == 'M' || firstChar == 'N') {
            holder.profile_image.setShapeColor(mMaterialColors[4]);
        }

        else if(firstChar == 'O' || firstChar == 'P') {
            holder.profile_image.setShapeColor(mMaterialColors[5]);
        }

        else if(firstChar == 'Q' || firstChar == 'R') {
            holder.profile_image.setShapeColor(mMaterialColors[6]);
        }

        else if(firstChar == 'S' || firstChar == 'T') {
            holder.profile_image.setShapeColor(mMaterialColors[7]);
        }

        else if(firstChar == 'U' || firstChar == 'V') {
            holder.profile_image.setShapeColor(mMaterialColors[8]);
        }

        else if(firstChar == 'W' || firstChar == 'X') {
            holder.profile_image.setShapeColor(mMaterialColors[9]);
        }

        else if(firstChar == 'Y' || firstChar == 'Z') {
            holder.profile_image.setShapeColor(mMaterialColors[10]);
        }

    }

    @Override
    public int getItemCount()
    {
        return friendMessages.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder  {
        MaterialLetterIcon profile_image;
        TextView show_message;
        TextView username;
        private int[] mMaterialColors;




        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image=itemView.findViewById(R.id.profile_image);
            show_message=itemView.findViewById(R.id.show_message);
            username=itemView.findViewById(R.id.username_msg);
            mMaterialColors=itemView.getResources().getIntArray(R.array.colors);
        }



    }

    @Override
    public int getItemViewType(int position) {
        user= FirebaseAuth.getInstance().getCurrentUser();
        if(friendMessages.get(position).getUserUID().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}
