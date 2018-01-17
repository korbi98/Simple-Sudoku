package com.korbi.simplesudoku.sudokuviews;

import android.content.Context;

/**
 * Created by korbi on 7/21/17.
 */

public class BasicSudokuCell extends android.support.v7.widget.AppCompatTextView
{
    private int value = 0;
    private boolean isPreSet = true;

    public BasicSudokuCell(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setIsPreSet(boolean isPreSet) {
        this.isPreSet = isPreSet;
    }

    public boolean isPreSet() { return isPreSet; }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
