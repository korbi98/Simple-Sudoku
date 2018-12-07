package com.korbi.simplesudoku.sudokuviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by korbi on 7/21/17.
 */

public class SudokuGridView extends GridView {

    public SudokuGridView(final Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
