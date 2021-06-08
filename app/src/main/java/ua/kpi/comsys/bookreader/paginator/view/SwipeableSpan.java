package ua.kpi.comsys.bookreader.paginator.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * An extended version of ClickableSpan, which also support long clicks and swipes
 */
abstract class SwipeableSpan extends ClickableSpan {

    /**
     * Long click event
     *
     * @param view View, which rise the event
     */
    abstract void onLongClick(View view);

    /**
     * Swipe from right to left
     *
     * @param view View, which rise the event
     */
    abstract void onSwipeLeft(View view);

    /**
     * Swipe from left to right
     *
     * @param view View, which rise the event
     */
    abstract void onSwipeRight(View view);

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        //ds.setColor(Color.BLACK);
    }
}