package com.korbi.simplesudoku.logic;

import android.content.Context;

import com.korbi.simplesudoku.db.SudokuDBhelper;

/**
 * Created by korbi on 2/16/18.
 * Runs in the background and generates Sudokus, which will be stored, so that there is no waiting
 * time, when starting a new game
 */

public class CreateSudokusRunnable implements Runnable {

    private Context context;

    public CreateSudokusRunnable(Context context){
        this.context = context;
    }

    private SudokuDBhelper db;
    private SudokuGrid grid;
    private SudokuLogic sHelper;

    @Override
    public void run() {

        db = new SudokuDBhelper(context);
        grid = new SudokuGrid(context);
        sHelper = new SudokuLogic(grid);

        inventoryCheck();
    }

    //checks if at least one sudoku of each difficulty is stored and generated one if not
    private void inventoryCheck(){
        boolean inventoryFull = false;

        while(!inventoryFull){
            inventoryFull = true;
            for (int i = 1; i <= 10; i++){
                int count = db.getDifficultyCount(i);

                if (count < 30){
                    generateSudoku(i);
                    inventoryFull = false;
                }
            }
        }
    }

    private void generateSudoku(int difficulty){
        grid = new SudokuGrid(context);
        sHelper.generateSudoku(grid);
        sHelper.clearCells(grid, difficulty);
        grid.clearFaultyLists();
        db.addSudoku(grid.getGridString(), difficulty);
    }
}
