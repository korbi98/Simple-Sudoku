package com.korbi.simplesudoku.logic;

import android.content.Context;

import com.korbi.simplesudoku.R;
import com.korbi.simplesudoku.sudokuviews.SudokuCellView;

/**
 * Created by korbi on 7/21/17.
 */

public class SudokuGrid {
    private static int currentPosition = 0;

    private SudokuCellView[][] sudoku = new SudokuCellView[9][9];
    private Context context;

    public SudokuGrid(Context context){
        this.context = context;

        int i = 1;
        for( int y = 0; y < 9; y++){
            for( int x = 0; x < 9; x++){
                sudoku[x][y] = new SudokuCellView(context);
                sudoku[x][y].setIsPreSet(true);
                sudoku[x][y].setValue(i);
                i++;
                sudoku[x][y].setBackgroundResource(R.color.cellPreset);
            }
        }
    }

    public SudokuCellView getItem(int x , int y ){
        return sudoku[x][y];
    }

    public SudokuCellView getItem( int position ){
        int x = position % 9;
        int y = position / 9;

        return sudoku[x][y];
    }

    public void setGrid( int[][] grid ){
        for( int x = 0 ; x < 9 ; x++ ){
            for( int y = 0 ; y < 9 ; y++){
                sudoku[x][y].setValue(grid[x][y]);
            }
        }
    }

    public void highlightCells(int position){
        if (!getCurrentPosition().isPreSet()){
            getCurrentPosition().setBackgroundResource(R.color.cellNotPreset);
        }
        currentPosition= position;
        if (!getCurrentPosition().isPreSet()){
            getCurrentPosition().setBackgroundResource(R.color.cellHighlight);
        }

    }

    public SudokuCellView getCurrentPosition() { return getItem(currentPosition); }

}
