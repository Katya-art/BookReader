package ua.kpi.comsys.bookreader.paginator.view;

/**
 * Interface definition for a callback to be invoked when user make a swipe
 */
public interface OnSwipeListener {

    /**
     * Swipe from right to left
     */
    default void onSwipeLeft() {}

    /**
     * Swipe from left to right
     */
    default void onSwipeRight() {}
}