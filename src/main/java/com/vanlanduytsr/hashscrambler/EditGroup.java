package com.vanlanduytsr.hashscrambler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.Calendar;

public class EditGroup extends AppCompatActivity {

    Group editGroup;
    EditText newGroupName;
    EditText newGroupList;
    TextView currentGroupName;
    TextView currentGroupList;
    Button saveButton;
    Button clearListButton;
    Button saveNameButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group);

        editGroup = (Group) getIntent().getExtras().getSerializable("editGroup");

        HashTagger ht = new HashTagger(this);
        currentGroupList = findViewById(R.id.current_group_list);
        currentGroupName = findViewById(R.id.current_group_name);
        newGroupName = findViewById(R.id.group_name);
        newGroupList = findViewById(R.id.list_string);
        saveButton = findViewById(R.id.save_button);
        clearListButton = findViewById(R.id.clear_list_button);
        saveNameButton = findViewById(R.id.save_name);

        final GroupDatabase db = Room.databaseBuilder(getApplicationContext(), GroupDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        currentGroupName.setText(editGroup.getName());
        currentGroupList.setText(ht.addHashesFromGroup(editGroup));

        saveNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newGroupName.getText().toString().isEmpty() || newGroupName.getText().toString() == "" || newGroupName.getText().toString() == " ") {
                    editGroup.setName(editGroup.getName());
                } else {
                    editGroup.setName(newGroupName.getText().toString().trim());
                }

                editGroup.setModifiedOn(Calendar.getInstance().getTime());
                db.groupDao().update(editGroup);
                newGroupName.setText("");
                currentGroupName.setText(editGroup.getName());
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newGroupList.getText().toString() != "" && newGroupList.getText().toString() != " ") {
                    String newListString = editGroup.getListString();
                    editGroup.setListString(newListString += editGroup.cleanString(newGroupList.getText().toString().trim()));
                    editGroup.setModifiedOn(Calendar.getInstance().getTime());
                    db.groupDao().update(editGroup);
                    newGroupList.setText("");
                    currentGroupList.setText(ht.addHashesFromGroup(editGroup));
                }
            }
        });

        clearListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditGroup.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to clear this list?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                editGroup.setListString("");
                                editGroup.setModifiedOn(Calendar.getInstance().getTime());
                                db.groupDao().update(editGroup);
                                currentGroupList.setText(ht.addHashesFromGroup(editGroup));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditGroup.this, DetailActivity.class).putExtra("getDetailOf", editGroup));
    }
}
