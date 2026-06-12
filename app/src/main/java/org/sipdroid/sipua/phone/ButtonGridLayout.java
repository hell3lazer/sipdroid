package org.sipdroid.sipua.phone;

/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * Copyright (C) 2006 The Android Open Source Project
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ButtonGridLayout extends ViewGroup {

    private final int mColumns = 3;
    private int mPaddingBottom = 0,mPaddingLeft = 0,mPaddingRight = 0,mPaddingTop = 0;
    
    public ButtonGridLayout(Context context) {
        super(context);
    }

    public ButtonGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int y = mPaddingTop;
        final int rows = getRows();
        final View child0 = getChildAt(0);
        final int yInc = (getHeight() - mPaddingTop - mPaddingBottom) / rows;
        final int xInc = (getWidth() - mPaddingLeft - mPaddingRight) / mColumns;
        int maxChildWidth = 0;
        int maxChildHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            maxChildWidth = Math.max(maxChildWidth, getChildAt(i).getMeasuredWidth());
            maxChildHeight = Math.max(maxChildHeight, getChildAt(i).getMeasuredHeight());
        }

        final int xOffset = Math.max(0, (xInc - maxChildWidth) / 2);
        final int yOffset = Math.max(0, (yInc - maxChildHeight) / 2);
        
        for (int row = 0; row < rows; row++) {
            int x = mPaddingLeft;
            for (int col = 0; col < mColumns; col++) {
                int cell = row * mColumns + col;
                if (cell >= getChildCount()) {
                    break;
                }
                View child = getChildAt(cell);
                child.layout(x + xOffset, y + yOffset, 
                        x + xOffset + maxChildWidth, 
                        y + yOffset + maxChildHeight);
                x += xInc;
            }
            y += yInc;
        }
    }

    private int getRows() {
        return (getChildCount() + mColumns - 1) / mColumns; 
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mPaddingLeft + mPaddingRight;
        int height = mPaddingTop + mPaddingBottom;
        
        int maxChildWidth = 0;
        int maxChildHeight = 0;
        
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            int childWidthSpec = (lp != null && lp.width > 0) ? 
                MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY) : MeasureSpec.UNSPECIFIED;
            int childHeightSpec = (lp != null && lp.height > 0) ? 
                MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY) : MeasureSpec.UNSPECIFIED;
                
            child.measure(childWidthSpec, childHeightSpec);
            maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth());
            maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
        }
        
        width += mColumns * maxChildWidth;
        height += getRows() * maxChildHeight;
        
        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
