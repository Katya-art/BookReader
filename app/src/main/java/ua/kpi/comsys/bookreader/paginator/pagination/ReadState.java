package ua.kpi.comsys.bookreader.paginator.pagination;

/**
 * A data structure, who represents the state of control:
 * - current page index
 * - total pages count
 * - read percentage
 * - text of current page
 */
public class ReadState {
    private int currentIndex;
    private int pagesCount;
    private float readPercent;
    private CharSequence pageText;

    public ReadState(int currentIndex, int pagesCount, float readPercent, CharSequence pageText) {
        this.currentIndex = currentIndex;
        this.pagesCount = pagesCount;
        this.readPercent = readPercent;
        this.pageText = pageText;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public float getReadPercent() {
        return readPercent;
    }

    public void setReadPercent(float readPercent) {
        this.readPercent = readPercent;
    }

    public CharSequence getPageText() {
        return pageText;
    }

    public void setPageText(CharSequence pageText) {
        this.pageText = pageText;
    }
}