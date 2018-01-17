package com.korbi.simplesudoku.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.korbi.simplesudoku.R;
import com.korbi.simplesudoku.logic.SudokuAdapter;
import com.korbi.simplesudoku.logic.SudokuGrid;
import com.korbi.simplesudoku.logic.SudokuLogic;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public  SudokuGrid sGrid;
    private SharedPreferences preferences;
    SudokuLogic sHelper;
    public static final int LOAD_GAME = 1;
    public static final int NEW_GAME = 0;
    public static final String LOAD_OR_NEW = "load";
    public static final String SAVE_FILE = "savefile";
    public static boolean showErrorPreference;
    public static boolean highlightCellPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button[] numberButtons;
        ImageButton deleteNumber;
        SudokuAdapter adapter;
        GridView sudokuField;


        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        showErrorPreference = preferences.getBoolean(getString(R.string.game_settings_show_errors_key), true);
        highlightCellPreference = preferences.getBoolean(getString(R.string.game_settings_show_current_position_key), true);

        numberButtons = new Button[9];
        numberButtons[0] = (Button) findViewById(R.id.button1);
        numberButtons[1] = (Button) findViewById(R.id.button2);
        numberButtons[2] = (Button) findViewById(R.id.button3);
        numberButtons[3] = (Button) findViewById(R.id.button4);
        numberButtons[4] = (Button) findViewById(R.id.button5);
        numberButtons[5] = (Button) findViewById(R.id.button6);
        numberButtons[6] = (Button) findViewById(R.id.button7);
        numberButtons[7] = (Button) findViewById(R.id.button8);
        numberButtons[8] = (Button) findViewById(R.id.button9);

        for (int i = 0; i < 9; i++) numberButtons[i].setOnClickListener(this);

        deleteNumber = (ImageButton) findViewById(R.id.buttonDelete);
        deleteNumber.setOnClickListener(this);

        sGrid = new SudokuGrid(getApplicationContext());
        sHelper = new SudokuLogic(sGrid);

        sudokuField = (GridView) findViewById(R.id.sudoku_field);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getInt(LOAD_OR_NEW) == NEW_GAME){
            newGame();
        }
        else if (bundle.getInt(LOAD_OR_NEW) == LOAD_GAME){
            loadGame();
        }

        adapter = new SudokuAdapter(sGrid);
        sudokuField.setAdapter(adapter);
    }

    @Override
    public void onClick(View view){

        int value = 0;
        switch (view.getId()){
            case R.id.button1: value = 1; break;
            case R.id.button2: value = 2; break;
            case R.id.button3: value = 3; break;
            case R.id.button4: value = 4; break;
            case R.id.button5: value = 5; break;
            case R.id.button6: value = 6; break;
            case R.id.button7: value = 7; break;
            case R.id.button8: value = 8; break;
            case R.id.button9: value = 9; break;
        }

        if (!sGrid.getCurrentPosition().isPreSet()){
            sGrid.getCurrentPosition().setValue(value);
            sGrid.highlightFaultyCells();

            if (sHelper.checkIfFilled(sGrid)){
                if (sHelper.checkGame()){
                    createGameFinishDialog();
                }
            }
        }
    }

    @Override
    public void onPause(){
        if (sGrid != null) preferences.edit().putString(SAVE_FILE, sGrid.getGridString()).apply();
        super.onPause();
    }

    private void newGame(){
        String difficultyString = preferences.getString(getString(R.string.game_settings_difficulty_key), "1");
        Integer difficulty = Integer.parseInt(difficultyString);
        sHelper.generateSudoku(sGrid);
        sHelper.clearCells(sGrid, difficulty);
        sGrid.clearFaultyLists();
    }

    private void loadGame(){
        String savefile = preferences.getString(SAVE_FILE, "");
        if (savefile.length() == 162){
            sGrid.loadGridString(savefile);
        }
        if (showErrorPreference) sGrid.initializeHighlighting();
    }

    private void createGameFinishDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle(getString(R.string.congratulation))
                .setMessage(getString(R.string.congratulation_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < 81; i++){
                            sGrid.getItem(i).setIsPreSet(true);
                        }
                        newGame();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sGrid = null;
                        preferences.edit().putString(SAVE_FILE, null).apply();
                        finish();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
