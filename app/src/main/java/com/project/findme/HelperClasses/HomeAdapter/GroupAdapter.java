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


public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {
    ArrayList<GroupHelperClass> groupInfos;
    private GroupListener groupListener;
    private int[] mMaterialColors;

    public GroupAdapter(ArrayList<GroupHelperClass> groupInfos, GroupListener groupListener) {
        this.groupInfos = groupInfos;
        this.groupListener = groupListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, groupListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GroupHelperClass groupInfo = groupInfos.get(position);
        holder.group_name.setText(groupInfo.getName());
        holder.group_desc.setText(groupInfo.getDescription());
        holder.group_owner.setText("Created By : "+groupInfo.getOwner());
        if(groupInfo.isMember()){
            holder.btn.setText("GO TO GROUP");
        }
        else{
            holder.btn.setText("JOIN");
        }
        holder.icon.setInitials(true);
        holder.icon.setInitialsNumber(2);
        holder.icon.setLetterSize(18);
        holder.icon.setLetter(groupInfo.getName());
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
        return groupInfos.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView group_name, group_desc , group_owner;
        Button btn;
        MaterialLetterIcon icon;
        GroupListener groupListener;
        private int[] mMaterialColors;

        public MyViewHolder(@NonNull View itemView, GroupListener groupListener) {
            super(itemView);
            group_name = itemView.findViewById(R.id.group_name);
            group_desc = itemView.findViewById(R.id.group_description);
            group_owner = itemView.findViewById(R.id.group_owner);
            btn = itemView.findViewById(R.id.button);
            icon = itemView.findViewById(R.id.letter_icon);
            mMaterialColors=itemView.getResources().getIntArray(R.array.colors);
            this.groupListener = groupListener;
            btn.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            groupListener.onGroupClick(getAdapterPosition());
        }
    }


    public interface GroupListener {
        void onGroupClick(int position);
    }
}