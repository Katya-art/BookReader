package ua.kpi.comsys.bookreader.paginator.view;

import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * An extended version of LinkMovement method, which also support long clicks and swipes
 */
public class SwipeableMovementMethod extends LinkMovementMethod {

    private final Handler longClickHandler = new  Handler();
    private double startXCoord = 0.0;
    private long startTime = 0;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            handleCancelAction();
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handleDownAction(event, buffer, widget);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            handleUpAction(event, buffer, widget);
        }
        return true;
    }

    private void handleCancelAction() {
        longClickHandler.removeCallbacksAndMessages(null);
    }

    private void handleDownAction(MotionEvent event, Spannable buffer, TextView widget) {
        startXCoord = event.getX();
        startTime = event.getEventTime();

        SwipeableSpan link = getClickableSpan(event, widget, buffer);
        if (link != null) {
            Selection.setSelection(buffer, buffer.getSpanStart(link), buffer.getSpanEnd(link));

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    link.onLongClick(widget);
                }
            };

            long DEFAULT_LONG_CLICK_DELAY = 1000L;
            longClickHandler.postDelayed(r, DEFAULT_LONG_CLICK_DELAY);
        }
    }

    private void handleUpAction(MotionEvent event, Spannable buffer, TextView widget) {
        long timeDiff = event.getEventTime() - startTime;
        double xCoordDiff = event.getX() - startXCoord;

        int MAX_TIME_THRESHOLD = 500;
        if (timeDiff < MAX_TIME_THRESHOLD) {
            longClickHandler.removeCallbacksAndMessages(null);
            SwipeableSpan link = getClickableSpan(event, widget, buffer);
            if (link != null) {
                int MIN_COORD_POSITIVE_THRESHOLD = 100;
                int MIN_COORD_NEGATIVE_THRESHOLD = MIN_COORD_POSITIVE_THRESHOLD * -1;
                if (xCoordDiff > MIN_COORD_POSITIVE_THRESHOLD) {
                    link.onSwipeLeft(widget);
                } else if (xCoordDiff < MIN_COORD_NEGATIVE_THRESHOLD) {
                    link.onSwipeRight(widget);
                } else {
                    link.onClick(widget);
                }
            }
        }
    }

    private SwipeableSpan getClickableSpan(MotionEvent event, TextView widget, Spannable buffer) {
        int clickX = (int) event.getX() - widget.getTotalPaddingLeft() + widget.getScrollX();
        int clickY = (int) event.getY() - widget.getTotalPaddingTop() + widget.getScrollX();

        int line = widget.getLayout().getLineForVertical(clickY);
        int offset = widget.getLayout().getOffsetForHorizontal(line, clickX);
        SwipeableSpan[] spans = buffer.getSpans(offset, offset, SwipeableSpan.class);
        if (spans.length == 0) {
            return null;
        }
        return spans[0];
    }
}