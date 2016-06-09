package dontkillthetree.scu.edu.UI;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager{
    private float downX;
    private float downY;
    private int horizontalSwipe = -1; // -1 = no horizontal swipe, 0 = swiped left, 1 = swiped right


    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent (MotionEvent ev) {
        horizontalSwipe = -1;

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
            downY = ev.getY();
        }
        else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float upX = ev.getX();
            float upY = ev.getY();

            // if there is a horizontal swipe
            if (Math.abs((upX - downX) / (upY - downY)) > 1 && Math.abs(upX - downX) >= 5) {
                // if the horizontal swipe is long enough, regard it as a valid swipe
                if (Math.abs(upX - downX) >= 10) {
                    horizontalSwipe = upX > downX ? 1 : 0;
                }
                return true;
            }
        }
        else if (ev.getAction() == MotionEvent.ACTION_UP) {
            // if not a click
            if (horizontalSwipe != -1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // swipe right
        if (horizontalSwipe == 1) {
            setCurrentItem(0, true);
            return true;
        }
        // swipe left
        else if (horizontalSwipe == 0) {
            setCurrentItem(1, true);
            return true;
        }

        return false;
    }
}
