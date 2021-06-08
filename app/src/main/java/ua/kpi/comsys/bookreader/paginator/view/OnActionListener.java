package ua.kpi.comsys.bookreader.paginator.view;


import ua.kpi.comsys.bookreader.paginator.pagination.ReadState;

/**
 * Interface definition for a callback to be invoked when user interacts with control
 */
public interface OnActionListener {

    /**
     * Notification about displaying new page
     *
     * @param state a state of displayed page
     */
    default void onPageLoaded(ReadState state) {}

    /**
     * Notification about click by paragraph
     *
     * @param paragraph paragraph clicked by the user
     */
    default void onClick(String paragraph) {}

    /**
     * Notification about long click by word
     *
     * @param word word long clicked by the user
     */
    default void onLongClick(String word) {}
}