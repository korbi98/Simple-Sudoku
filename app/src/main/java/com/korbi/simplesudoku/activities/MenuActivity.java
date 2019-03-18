/*
 * Copyright 2019 Korbinian Moser
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.korbi.simplesudoku.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.korbi.simplesudoku.R;
import com.korbi.simplesudoku.db.SudokuDBhelper;
import com.korbi.simplesudoku.logic.CreateSudokusRunnable;
import com.korbi.simplesudoku.logic.SudokuGrid;

public class MenuActivity extends AppCompatActivity {

    public static SudokuGrid sudokuGrid;
    private SharedPreferences preferences;
    private Button loadButton;
    CreateSudokusRunnable sudokusRunnable;
    public static Thread sudokuCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar();

        if (sudokuCreator == null){
            sudokusRunnable = new CreateSudokusRunnable(getApplicationContext());
            sudokuCreator = new Thread(sudokusRunnable);
            sudokuCreator.setPriority(Thread.MAX_PRIORITY);
        }

        if (!sudokuCreator.isAlive()){
            sudokusRunnable = new CreateSudokusRunnable(getApplicationContext());
            sudokuCreator = new Thread(sudokusRunnable);
            sudokuCreator.setPriority(Thread.MAX_PRIORITY);
            sudokuCreator.start();
        }

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
        SudokuDBhelper db = new SudokuDBhelper(getApplicationContext());
        String difficultyString = preferences.getString(getString(R.string.game_settings_difficulty_key), "1");
        Integer difficulty = Integer.parseInt(difficultyString);

        if (db.getDifficultyCount(difficulty) == 0){

            createInventoryWarningDialog();

        } else {
            Intent createNewGame = new Intent(MenuActivity.this, GameActivity.class);
            createNewGame.putExtra(GameActivity.LOAD_OR_NEW, GameActivity.NEW_GAME);
            startActivity(createNewGame);
        }
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

    private void createInventoryWarningDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.inventory_warning_message))
                .setTitle(R.string.inventory_warning)
                .setPositiveButton(getString(R.string.proceed), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent createNewGame = new Intent(MenuActivity.this, GameActivity.class);
                        createNewGame.putExtra(GameActivity.LOAD_OR_NEW, GameActivity.NEW_GAME);
                        startActivity(createNewGame);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
}
