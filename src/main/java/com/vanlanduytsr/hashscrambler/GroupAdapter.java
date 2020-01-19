package com.vanlanduytsr.hashscrambler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private List<Group> groups;
    private Context mContext;
    private ViewHolder holder;
    private GroupDatabase db;
    private HashTagger ht;
    private boolean deleteMode;

    public GroupAdapter(List<Group> groups, Context mContext, GroupDatabase db) {
        this.groups = groups;
        this.mContext = mContext;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_row, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        ht = new HashTagger(mContext);
        holder.groupName.setText(groups.get(position).getName());
        holder.listString.setText(ht.addHashesFromGroup(groups.get(position)));

        if (deleteMode) {
            holder.setButtonsToDelete();
        } else {
            holder.setFavoriteButtons(groups.get(position));
        }

        holder.itemView.setOnClickListener(v -> {
            Group getDetailOf = groups.get(position);
            Intent intent = new Intent(mContext, DetailActivity.class).putExtra("getDetailOf", getDetailOf);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView groupName;
        private TextView listString;
        private AppCompatImageButton favTrueButton;
        private AppCompatImageButton favFalseButton;
        private AppCompatImageButton deleteButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name_list_layout);
            listString = itemView.findViewById(R.id.group_list_list_layout);

            favTrueButton = itemView.findViewById(R.id.favorite_true_button);
            favFalseButton = itemView.findViewById(R.id.favorite_false_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }


        void setFavoriteButtons(Group group) {

            favTrueButton.setOnClickListener(v -> {
                group.setIsFavorite(false);
                db.groupDao().update(group);
                setFavoriteButtons(group);
            });

            favFalseButton.setOnClickListener(v -> {
                group.setIsFavorite(true);
                db.groupDao().update(group);
                setFavoriteButtons(group);
            });


            if (group.getIsFavorite()) {
                deleteButton.setVisibility(View.GONE);
                favFalseButton.setVisibility(View.GONE);
                favTrueButton.setVisibility(View.VISIBLE);
            } else if (!group.getIsFavorite()) {
                deleteButton.setVisibility(View.GONE);
                favTrueButton.setVisibility(View.GONE);
                favFalseButton.setVisibility(View.VISIBLE);
            }
        }

        void setButtonsToDelete() {
            favFalseButton.setVisibility(View.GONE);
            favTrueButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                new AlertDialog.Builder(mContext)
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to delete " + groups.get(position).getName() + "?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                            db.groupDao().delete(groups.get(position));
                            groups.remove(position);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(android.R.string.no, null).show();


            });
        }
    }

    void setDeleteMode(boolean set) {
        if (deleteMode != set) {
            deleteMode = set;
        }
        notifyDataSetChanged();
    }

    boolean getDeleteMode() {
        return deleteMode;
    }
}
