package com.korbi.simplesudoku.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.korbi.simplesudoku.R;
import com.korbi.simplesudoku.logic.SudokuGrid;

public class MenuActivity extends AppCompatActivity {

    public static SudokuGrid sudokuGrid;
    private SharedPreferences preferences;
    private Button loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar();
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitleTextColor(getResources().getColor(android.R.color.primary_text_light));
        //setSupportActionBar(toolbar);

        loadButton = (Button) findViewById(R.id.button_menu_load);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String savefile = preferences.getString(GameActivity.SAVE_FILE, null);
        if (savefile == null) loadButton.setEnabled(false);
    }

    @Override
    public void onResume(){
        String savefile = preferences.getString(GameActivity.SAVE_FILE, null);
        if (savefile == null) loadButton.setEnabled(false);
        super.onResume();
    }

    public void createNewGame(View v){
        Intent createNewGame = new Intent(MenuActivity.this, GameActivity.class);
        createNewGame.putExtra(GameActivity.LOAD_OR_NEW, GameActivity.NEW_GAME);
        startActivity(createNewGame);
    }

    public void loadGame(View v){
        String savefile = preferences.getString(GameActivity.SAVE_FILE, "");
        if (savefile.length() != 162){
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.no_valid_savefile), Toast.LENGTH_LONG).show();
        }

        Intent loadGame = new Intent(MenuActivity.this, GameActivity.class);
        loadGame.putExtra(GameActivity.LOAD_OR_NEW, GameActivity.LOAD_GAME);
        startActivity(loadGame);
    }

    public void launchSettings(View v){
        Intent launchSettings = new Intent(MenuActivity.this,
                SettingsActivity.class);
        startActivity(launchSettings);
    }
}
