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
