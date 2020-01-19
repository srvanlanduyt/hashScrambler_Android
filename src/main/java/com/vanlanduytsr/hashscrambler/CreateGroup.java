package com.vanlanduytsr.hashscrambler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class CreateGroup extends AppCompatActivity {

    EditText newGroupName;
    EditText newGroupList;
    Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        newGroupName = findViewById(R.id.group_name);
        newGroupList = findViewById(R.id.list_string);
        saveButton = findViewById(R.id.save_button);

        final GroupDatabase db = Room.databaseBuilder(getApplicationContext(), GroupDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.groupDao().insertAll(new Group(newGroupName.getText().toString(), newGroupList.getText().toString()));
                startActivity(new Intent(CreateGroup.this, MainActivity.class));
            }
        });
    }
}
