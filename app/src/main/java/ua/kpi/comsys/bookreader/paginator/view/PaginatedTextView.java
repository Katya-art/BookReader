package ua.kpi.comsys.bookreader.paginator.view;

import android.content.Context;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ua.kpi.comsys.bookreader.paginator.extension.AllWordsPositions;
import ua.kpi.comsys.bookreader.paginator.pagination.PaginationController;
import ua.kpi.comsys.bookreader.paginator.pagination.ReadState;


/**
 * An extended TextView, which support pagination, clicks by paragraphs and long clicks by words
 */
public class PaginatedTextView extends AppCompatTextView {

    public PaginatedTextView(@NonNull @NotNull Context context) {
        super(context);
        initPaginatedTextView();
    }

    public PaginatedTextView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaginatedTextView();
    }

    public PaginatedTextView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaginatedTextView();
    }

    private TextPaint textPaint = new TextPaint(getPaint());
    private OnSwipeListener swipeListener = null;
    private OnActionListener actionListener = null;
    private PaginationController controller;
    private boolean isMeasured = false;

    public boolean getMeasured() {
        return isMeasured;
    }

    public void setMeasured(boolean measured) {
        isMeasured = measured;
    }

    @Override
    public void scrollTo(int x, int y) {}

    /**
     * Setup the TextView
     * @param text text to set
     */
    public void setup(CharSequence text) {
        if (getMeasured()) {
            loadFirstPage(text);
        } else {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    setMeasured(true);
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    loadFirstPage(text);
                }
            });
        }
    }

    /**
     * Set up the [OnActionListener]
     */
    public void setOnActionListener(OnActionListener listener) {
        this.actionListener = listener;
    }

    /**
     * Setting up a listener, which will receive swipe callbacks
     * @param swipeListener a listener which will receive swipe callbacks
     */
    public void setOnSwipeListener(OnSwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    private void initPaginatedTextView() {
        setMovementMethod(new SwipeableMovementMethod());
        //setHighlightColor(Color.TRANSPARENT);
    }

    private void setPageState(ReadState pageState) {
        this.setText(pageState.getPageText());
        actionListener.onPageLoaded(pageState);
        updateWordsSpannables();
    }

    private String getSelectedWord() {
        return getText().subSequence(getSelectionStart(), getSelectionEnd()).toString().trim();
    }

    private void loadFirstPage(CharSequence text) {
        int effectWidth = getWidth() - (getPaddingLeft() + getPaddingRight());
        int effectHeight = getHeight() - (getPaddingTop() + getPaddingBottom());
        controller = new PaginationController(text, effectWidth, effectHeight, textPaint, getLineSpacingMultiplier(), getLineSpacingExtra());
        setPageState(controller.getCurrentPage());
    }

    private void updateWordsSpannables() {
        Spannable spans = (Spannable) getText();
        AllWordsPositions allWordsPositions = new AllWordsPositions();
        List<Integer> spaceIndexes = allWordsPositions.allWordsPositions(getText().toString().trim());
        int wordStart = 0;
        int wordEnd;
        for (int i = 0; i < spaceIndexes.size(); i++) {
            SwipeableSpan swipeableSpan = createSwipeableSpan();
            if (i < spaceIndexes.size()) {
                wordEnd = spaceIndexes.get(i);
            } else {
                wordEnd = spans.length();
            }
            spans.setSpan(swipeableSpan, wordStart, wordEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordStart = wordEnd + 1;
        }
    }

    private String getSelectedParagraph(TextView widget) {
        CharSequence text = widget.getText();
        int selStart = widget.getSelectionStart();
        int selEnd = widget.getSelectionEnd();
        int parStart = findLeftLineBreak(text, selStart);
        int parEnd = findRightLineBreak(text, selEnd);
        return text.subSequence(parStart, parEnd).toString();
    }

    private int findLeftLineBreak(CharSequence text, int selStart) {
        for (int i = selStart; i > 0; i--) {
            if (text.charAt(i) == '\n') {
                return i + 1;
            }
        }
        return 0;
    }

    private int findRightLineBreak(CharSequence text, int selEnd) {
        for (int i = selEnd; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                return i + 1;
            }
        }
        return text.length() - 1;
    }

    private SwipeableSpan createSwipeableSpan() {
        return new SwipeableSpan() {
            @Override
            void onLongClick(View view) {
                String word = getSelectedWord();
                if (!TextUtils.isEmpty(word)) {
                    actionListener.onLongClick(word);
                }
            }

            @Override
            void onSwipeLeft(View view) {
                ReadState prevPage = controller.getPrevPage();
                if (prevPage != null) {
                    setPageState(prevPage);
                }
                swipeListener.onSwipeLeft();
            }

            @Override
            void onSwipeRight(View view) {
                ReadState nextPage = controller.getNextPage();
                if (nextPage != null) {
                    setPageState(nextPage);
                }
                swipeListener.onSwipeRight();
            }

            @Override
            public void onClick(@NonNull View widget) {
                 String paragraph = getSelectedParagraph((TextView) widget);
                 if (!TextUtils.isEmpty(paragraph)) {
                     actionListener.onClick(paragraph);
                 }
            }
        };
    }

}