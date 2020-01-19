package com.vanlanduytsr.hashscrambler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    Context mContext;
    Group detailGroup;
    String[] listArray;
    ViewHolder holder;
    GroupDatabase db;
    boolean deleteItemsMode;


    public DetailAdapter(Group detailGroup, Context mContext, GroupDatabase db) {

        this.detailGroup = detailGroup;
        this.mContext = mContext;
        this.db = db;
        listArray = detailGroup.getListArray();
    }


    public DetailAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_group_detail, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        holder.groupItem.setText(listArray[position]);
    }


    @Override
    public int getItemCount() {
        return listArray.length;
    }

    public void setDeleteMode(boolean set) {
        if (deleteItemsMode != set) {
            deleteItemsMode = set;
        }
        notifyDataSetChanged();
    }

    public boolean getDeleteMode() {
        return deleteItemsMode;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView groupItem;
        ConstraintLayout parentLayout;
        AppCompatImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupItem = itemView.findViewById(R.id.group_item);
            parentLayout = itemView.findViewById(R.id.parent_layout);

            deleteButton = itemView.findViewById(R.id.detail_delete_button);

            if (deleteItemsMode) {
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String listString = "";

                    ArrayList<String> tempArrayList = new ArrayList<>();
                    for (int i = 0; i < listArray.length; i++) {
                        tempArrayList.add(listArray[i]);
                    }

                    tempArrayList.remove(position);
                    listArray = new String[tempArrayList.size()];

                    for (int i = 0; i < tempArrayList.size(); i++) {
                        listArray[i] = tempArrayList.get(i);
                    }

                    for (int i = 0; i < listArray.length; i++) {
                        listString += listArray[i] + " ";
                    }

                    detailGroup.setListString(listString);
                    db.groupDao().update(detailGroup);
                    ((DetailActivity)mContext).setNumItems();
                    notifyDataSetChanged();
                }
            });
        }
    }
}