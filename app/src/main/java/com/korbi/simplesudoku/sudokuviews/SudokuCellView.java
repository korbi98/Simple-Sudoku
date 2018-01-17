package com.korbi.simplesudoku.sudokuviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.korbi.simplesudoku.R;

/**
 * Created by korbi on 7/21/17.
 */

public class SudokuCellView extends BasicSudokuCell {

    Paint paint;
    Context context;

    public SudokuCellView(Context context){
        super(context);
        setGravity(Gravity.CENTER);
        setTextSize(24);

        this.context = context;
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getValue() != 0)
        {
            if (isPreSet()) setTypeface(null, Typeface.BOLD);
            else setTypeface(null, Typeface.NORMAL);
            setText(String.valueOf(getValue()));
            setTextColor(getResources().getColor(R.color.sudokuTextColor));
        } else setText("");

        drawBorders(canvas);
    }

    @Override
    public void setIsPreSet(boolean isPreset){
        super.setIsPreSet(isPreset);
        if (isPreset){
            setBackgroundResource(R.color.cellPreset);
        } else {
            setBackgroundResource(R.color.cellNotPreset);
        }
    }

    private void drawBorders(Canvas canvas) {
        paint.setColor(ContextCompat.getColor(context, R.color.cellBorder));
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }
}
