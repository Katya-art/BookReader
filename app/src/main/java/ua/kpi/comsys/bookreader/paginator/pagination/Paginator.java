package ua.kpi.comsys.bookreader.paginator.pagination;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

/**
 * A class, which split text by pages, and control pages switching
 */
public class Paginator {
    private List<CharSequence> pages;
    private int currentIndex = 0;
    private int pagesCount;

    public Paginator(CharSequence text, int width, int height, TextPaint paint, Float spacingMult, float spacingAdd) {
        setPages(parsePages(text, width, height, paint, spacingMult, spacingAdd));
        setPagesCount(getPages().size());
    }

    public List<CharSequence> getPages() {
        return pages;
    }

    public void setPages(List<CharSequence> pages) {
        this.pages = pages;
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

    /**
     * Get current page text
     */
    public CharSequence getCurrentPage() {
        return pages.get(currentIndex);
    }

    private List<CharSequence> parsePages(CharSequence content, int width, int height,
                                          TextPaint paint, float spacingMult, float spacingAdd) {
        StaticLayout layout = new StaticLayout(content, paint,width, Layout.Alignment.ALIGN_NORMAL,
                spacingMult, spacingAdd, true);
        CharSequence text = layout.getText();
        int startOffset = 0;
        int lastBottomLineHeight = height;
        List<CharSequence> parsedPages = new ArrayList<>();
        for (int i = 0; i < layout.getLineCount(); i++) {
            if (lastBottomLineHeight < layout.getLineBottom(i)) {
                CharSequence page = text.subSequence(startOffset, layout.getLineStart(i));
                parsedPages.add(page);
                startOffset = layout.getLineStart(i);
                lastBottomLineHeight = layout.getLineTop(i) + height;
            }
        }
        CharSequence lastPage = text.subSequence(startOffset,
                layout.getLineEnd(layout.getLineCount() - 1));
        parsedPages.add(lastPage);
        return parsedPages;
    }
}