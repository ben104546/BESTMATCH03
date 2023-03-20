package com.example.a5dayappchat2.Activity.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NoTouchViewPage extends ViewPager {

    private boolean enabled;


    public NoTouchViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = false;
    }

    //触摸没有反应就可以了
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}