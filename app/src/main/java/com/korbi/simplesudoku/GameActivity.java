package com.korbi.simplesudoku;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.korbi.simplesudoku.logic.SudokuAdapter;
import com.korbi.simplesudoku.logic.SudokuGrid;
import com.korbi.simplesudoku.logic.SudokuLogic;
import com.korbi.simplesudoku.sudokuviews.SudokuCellView;
import com.korbi.simplesudoku.sudokuviews.SudokuGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public GridView[] regions;
    public  SudokuGrid sudokuGrid;
    private Random random;
    private Button[] numberButtons;
    private ImageButton deleteNumber;
    private SudokuAdapter adapter;
    private GridView sudokuField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

        random = new Random();
        sudokuGrid = new SudokuGrid(this);
        final SudokuLogic sudokuHelper = new SudokuLogic(sudokuGrid);

        sudokuField = (GridView) findViewById(R.id.sudoku_field);

        sudokuHelper.generateSudoku(sudokuGrid);
        sudokuHelper.clearCells(sudokuGrid, 40);
        adapter = new SudokuAdapter(this, sudokuGrid);
        sudokuField.setAdapter(adapter);

        Toast.makeText(this, String.valueOf(sudokuHelper.checkGame(sudokuGrid)), Toast.LENGTH_LONG).show();

        for (int i : sudokuHelper.getColumnPositions(5)){
            Log.d("test", String.valueOf(i));
        }
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
            case R.id.button9: value = 8; break;
        }
        if (!sudokuGrid.getCurrentPosition().isPreSet()){
            sudokuGrid.getCurrentPosition().setValue(value);
        }
    }
}
