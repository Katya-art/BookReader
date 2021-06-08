package ua.kpi.comsys.bookreader.paginator.pagination;

import android.text.TextPaint;

/**
 * Helper class for work with text pages
 */
public class PaginationController {
    CharSequence text;
    int width;
    int height;
    TextPaint paint;
    float spacingMult;
    float spacingExtra;
    Paginator paginator;

    public PaginationController(CharSequence text, int width, int height, TextPaint paint, float spacingMult, float spacingExtra) {
        this.text = text;
        this.width = width;
        this.height = height;
        this.paint = paint;
        this.spacingMult = spacingMult;
        this.spacingExtra = spacingExtra;
        paginator = new Paginator(text, width, height, paint, spacingMult, spacingExtra);
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public TextPaint getPaint() {
        return paint;
    }

    public void setPaint(TextPaint paint) {
        this.paint = paint;
    }

    public float getSpacingMult() {
        return spacingMult;
    }

    public void setSpacingMult(float spacingMult) {
        this.spacingMult = spacingMult;
    }

    public float getSpacingExtra() {
        return spacingExtra;
    }

    public void setSpacingExtra(float spacingExtra) {
        this.spacingExtra = spacingExtra;
    }

    /**
     * Get current page state
     * @return current page state
     */
    public ReadState getCurrentPage() {
        return new ReadState(paginator.getCurrentIndex() + 1, paginator.getPagesCount(),
                getReadPercent(), paginator.getCurrentPage());
    }

    /**
     * Get state of next page
     * @return next page state, or null, if this page is last
     */
    public ReadState getNextPage() {
        if (paginator.getCurrentIndex() < paginator.getPagesCount() - 1) {
            paginator.setCurrentIndex(paginator.getCurrentIndex() + 1);
            return getCurrentPage();
        }
        return null;
    }

    /**
     * Get state of previous page
     * @return previous page state, or null, if this page is first
     */
    public ReadState getPrevPage() {
        if (paginator.getCurrentIndex() > 0) {
            paginator.setCurrentIndex(paginator.getCurrentIndex() - 1);
            return getCurrentPage();
        }
        return null;
    }

    private float getReadPercent() {
        if (paginator.getPagesCount() == 0) {
            return 0;
        }
        return (paginator.getCurrentIndex() + 1) / (float) paginator.getPagesCount() * 100;
    }
}