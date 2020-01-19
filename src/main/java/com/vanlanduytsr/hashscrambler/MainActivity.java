package com.vanlanduytsr.hashscrambler;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GroupAdapter adapter;
    DetailAdapter detailAdapter;
    FloatingActionButton fab;
    GroupDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.group_recycler_view);

        db = Room.databaseBuilder(getApplicationContext(), GroupDatabase.class, "production")
                .allowMainThreadQueries()
                .build();
        List<Group> groups = db.groupDao().getAllGroups();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupAdapter(groups, this, db);
        detailAdapter = new DetailAdapter();
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateGroup.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        else if (id == R.id.action_delete) {
            if (adapter.getDeleteMode()) {
                adapter.setDeleteMode(false);
                detailAdapter.setDeleteMode(false);
            } else {
                adapter.setDeleteMode(true);
                detailAdapter.setDeleteMode(true);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
