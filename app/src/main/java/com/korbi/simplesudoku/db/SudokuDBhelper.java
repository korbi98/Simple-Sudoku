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

package com.korbi.simplesudoku.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.korbi.simplesudoku.logic.SudokuGrid;

/**
 * Created by korbi on 2/16/18.
 * This database stores a bunch of sudokus that have been created in advance
 */

public class SudokuDBhelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "SudokuDatabase.db";
    private static final int DB_VERSION = 1;
    private static final String SUDOKU_TABLE = "sudokus";

    private static final String COL_ID = "_id";
    private static final String COL_DIFFICULTY = "difficulty";
    private static final String COL_SUDOKU_STRING = "sudoku_string";

    private Context context;

    public SudokuDBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTaskTable = "CREATE TABLE " + SUDOKU_TABLE + " ( " + COL_ID + " INTEGER PRIMARY KEY, "
                + COL_DIFFICULTY + " INTEGER, " + COL_SUDOKU_STRING + " TEXT)";
        db.execSQL(createTaskTable);
        initializeDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) //This method ensures that there are no compatibility problems if you update from an older version of the app
    {}

    public void addSudoku(String sudokuString, int difficulty) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_DIFFICULTY, difficulty);
        values.put(COL_SUDOKU_STRING, sudokuString);

        db.insert(SUDOKU_TABLE, null, values);
        db.close();
    }

    public void addSudoku(String sudokuString, int difficulty, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COL_DIFFICULTY, difficulty);
        values.put(COL_SUDOKU_STRING, sudokuString);

        db.insert(SUDOKU_TABLE, null, values);
    }



    public SudokuGrid getSudokuByDifficulty(int difficulty) {

        String sudokuString = "";
        String selectQuery = "SELECT  * FROM " + SUDOKU_TABLE + " WHERE " + COL_DIFFICULTY + " = "
                                + String.valueOf(difficulty);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            sudokuString = cursor.getString(2);
            deleteSudoku(cursor.getInt(0)); // immediately deletes a sudoku, that gets used
        }
        cursor.close();
        SudokuGrid grid = new SudokuGrid(context);
        grid.loadGridString(sudokuString);
        return grid;
    }

    //returns how much sudokus of a given difficulty are stored
    public int getDifficultyCount(int difficulty) {

        int count = 0;
        String selectQuery = "SELECT  * FROM " + SUDOKU_TABLE + " WHERE " + COL_DIFFICULTY + " = "
                + String.valueOf(difficulty);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        count = cursor.getCount();

        cursor.close();
        return count;
    }

    private void deleteSudoku(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SUDOKU_TABLE, COL_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    //copies a bunch of more difficult sudokus into the db so that the user can start right away with more difficult sudokus
    private void initializeDB(SQLiteDatabase db){
        addSudoku("120009073053000000007340010391020000000000236276003000035000001060058097719600800110001011011000000001110010111010000000000111111001000011000001010011011111100100", 7, db);
        addSudoku("260040003390520000700000850030200080850403000100908365000000009070000608082630000110010001110110000100000110010100010110101000100101111000000001010000101011110000", 8, db);
        addSudoku("840003000300086000107000300070852100000000000208604000534270000692040000000060050110001000100011000101000100010111100000000000101101000111110000111010000000010010", 9, db);
        addSudoku("080600309907003000040000002730000000009000000004080067400800000200004001305070024010100101101001000010000001110000000001000000001010011100100000100001001101010011", 10, db);
    }
}
