package com.vanlanduytsr.hashscrambler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    private Group detailGroup;
    private DetailAdapter detailAdapter;
    private RecyclerView detailRecyclerView;
    private GroupDatabase db;
    private FloatingActionButton detailFab;
    private ClipboardManager mClipboard;
    private ClipData mClip;
    private HashTagger ht;
    private Scrambler scrambler;
    private SettingsManager sm;
    private String paragraphSpacing = ".\n.\n.\n.\n.\n ";

    TextView detailName;
    TextView numTimesUsed;
    TextView lastUsedOn;
    TextView lastModifiedOn;
    TextView numItems;
    ImageButton isFavoriteButton;
    ImageButton isNotFavoriteButton;
    Button scrambleButton;
    Button copyAllButton;
    TextView numToScramble;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        detailGroup = (Group) getIntent().getExtras().getSerializable("getDetailOf");
        sm = new SettingsManager(this);

        detailRecyclerView = findViewById(R.id.item_recycler_view);
        isNotFavoriteButton = findViewById(R.id.is_favorite_false_button);
        isFavoriteButton = findViewById(R.id.is_favorite_true_button);
        detailName = findViewById(R.id.detail_group_name);
        numToScramble = findViewById(R.id.num_to_scramble);
        scrambleButton = findViewById(R.id.scramble_button);
        copyAllButton = findViewById(R.id.copy_all_button);
        numTimesUsed = findViewById(R.id.num_times_used);
        lastUsedOn = findViewById(R.id.last_used_on);
        lastModifiedOn = findViewById(R.id.last_modified_on);
        numItems = findViewById(R.id.number_of_items);

        ht = new HashTagger(this);
        mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        db = Room.databaseBuilder(getApplicationContext(), GroupDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailAdapter = new DetailAdapter(detailGroup, this, db);
        detailRecyclerView.setAdapter(detailAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        detailRecyclerView.addItemDecoration(itemDecoration);

        setFavoriteButton();

        detailName.setText(detailGroup.getName());
        setNumItems();
        numTimesUsed.setText(Integer.toString(detailGroup.getNumTimesUsed()));
        lastModifiedOn.setText(detailGroup.getModifiedOn());
        lastUsedOn.setText(detailGroup.getLastUsed());


        isNotFavoriteButton.setOnClickListener(view -> {
            if (!detailGroup.getIsFavorite()) {
                detailGroup.setIsFavorite(true);
                db.groupDao().update(detailGroup);
                setFavoriteButton();
            }
        });


        isFavoriteButton.setOnClickListener(view -> {
            if (detailGroup.getIsFavorite()) {
                detailGroup.setIsFavorite(false);
                db.groupDao().update(detailGroup);
                setFavoriteButton();
            }
        });

        detailFab = findViewById(R.id.detail_fab);
        detailFab.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, EditGroup.class).putExtra("editGroup", detailGroup);
            startActivity(intent);
        });

        scrambleButton.setOnClickListener(view -> {
            int intToScramble = 0;
            try {
                intToScramble = Integer.parseInt(numToScramble.getText().toString());
            } catch(Exception e) {
                Snackbar.make(view, "Illegal Number Entered", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", view1 -> {

                        })
                        .show();
            }

            if (intToScramble > detailGroup.getListArray().length) {
                Snackbar.make(view, "Illegal Number Entered... \n There are only " + detailGroup.getListArray().length + " items in this group.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", view1 -> {
                            numToScramble.setText("");
                        })
                        .show();
            } else {
                scrambler = new Scrambler(detailGroup, intToScramble);
                String scrambled = scrambler.scramble(this);

                if (sm.getParagraphSpacing()) {
                    scrambled = paragraphSpacing + scrambled;
                }

                String finalScrambled = scrambled;
                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Do you want to copy \n\n" + ht.addHashesFromString(finalScrambled) + "\n\n to your clipboard?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                detailGroup.setNumTimesUsed(detailGroup.getNumTimesUsed() + 1);
                                detailGroup.setLastUsed(Calendar.getInstance().getTime());

                                mClip = ClipData.newPlainText("Copied Hash Tags", ht.addHashesFromString(finalScrambled));
                                mClipboard.setPrimaryClip(mClip);

                                db.groupDao().update(detailGroup);
                                setNumTimes();
                                setLastUsed();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        copyAllButton.setOnClickListener(view -> {
            String scrambled = "";
            if (sm.getScrambleAll()) {
                scrambler = new Scrambler(detailGroup, detailGroup.getListArray().length);
                scrambled = scrambler.scramble(this);
            } else {
                scrambled = detailGroup.getListString();
            }

            if (sm.getParagraphSpacing()) {
                scrambled = paragraphSpacing + scrambled;
            }

            String finalScrambled = scrambled;
            new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Do you want to copy \n\n" + ht.addHashesFromString(finalScrambled) + "\n\n to your clipboard?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                detailGroup.setNumTimesUsed(detailGroup.getNumTimesUsed() + 1);
                                detailGroup.setLastUsed(Calendar.getInstance().getTime());

                                mClip = ClipData.newPlainText("Copied Hash Tags", ht.addHashesFromString(finalScrambled));
                                mClipboard.setPrimaryClip(mClip);

                                db.groupDao().update(detailGroup);
                                setNumTimes();
                                setLastUsed();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
        });


    }

    public void setFavoriteButton() {
        if (detailGroup.getIsFavorite()) {
            isNotFavoriteButton.setVisibility(View.GONE);
            isFavoriteButton.setVisibility(View.VISIBLE);
        } else {
            isFavoriteButton.setVisibility(View.GONE);
            isNotFavoriteButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(DetailActivity.this, SettingsActivity.class));
        }

        else if (id == R.id.action_delete) {
            if (detailAdapter.getDeleteMode()) {
                detailAdapter.setDeleteMode(false);
            } else {
                detailAdapter.setDeleteMode(true);
            }
        }
        return super.onOptionsItemSelected(item);
    }

     public int getNumItems() {
        return detailGroup.getListArray().length;
     }

     public void setLastUsed() {
        lastUsedOn.setText(detailGroup.getLastUsed());
     }

     public void setNumItems() {
        numItems.setText(Integer.toString(getNumItems()));
     }

     public void setNumTimes() {
        numTimesUsed.setText(Integer.toString(detailGroup.getNumTimesUsed()));
     }


}
