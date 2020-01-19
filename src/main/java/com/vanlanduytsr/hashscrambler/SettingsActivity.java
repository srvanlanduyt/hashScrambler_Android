package com.vanlanduytsr.hashscrambler;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch scrambleAll;
    private Switch paragraphSpacing;

    private boolean isScrambleAll;
    private boolean isParagraphSpacing;

    private SettingsManager sm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        scrambleAll = findViewById(R.id.scramble_on_copy_all);
        paragraphSpacing = findViewById(R.id.paragraph_switch);

        sm = new SettingsManager(getApplicationContext());

        isScrambleAll = sm.getScrambleAll();
        isParagraphSpacing = sm.getParagraphSpacing();

        scrambleAll.setChecked(isScrambleAll);
        paragraphSpacing.setChecked(isParagraphSpacing);

        scrambleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isScrambleAll) {
                    isScrambleAll = false;
                } else {
                    isScrambleAll = true;
                }

                sm.saveSettings(isScrambleAll, isParagraphSpacing);
            }
        });

        paragraphSpacing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isParagraphSpacing) {
                    isParagraphSpacing = false;
                } else {
                    isParagraphSpacing = true;
                }

                sm.saveSettings(isScrambleAll, isParagraphSpacing);
            }
        });
    }
}
