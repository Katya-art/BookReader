package ua.kpi.comsys.bookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class BookEPUB extends AppCompatActivity {

    TextView textView;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_epub);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.bookEpub);
        webView.setOverScrollMode(1);

        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        try {
            // find InputStream for book
            InputStream epubInputStream = new FileInputStream(path);

            // Load Book from inputStream
            Book book = (new EpubReader()).readEpub(epubInputStream);
            StringBuilder data = new StringBuilder();
            List<Resource> bookChapters = book.getContents();
            for (int i = 1; i < bookChapters.size(); i++) {
                data.append(new String(bookChapters.get(i).getData()));
            }
            /*String data0 = new String(book.getContents().get(0).getData());
            String data1 = new String(book.getContents().get(1).getData());
            String data2 = new String(book.getContents().get(2).getData());
            String data3 = new String(book.getContents().get(3).getData());
            String data4 = new String(book.getContents().get(4).getData());
            String data = data0 + data1 + data2 + data3 + data4;*/
            webView.loadDataWithBaseURL(path, new String(data), "text/html", "UTF-8", null);
            webView.setVerticalScrollBarEnabled(true);
            webView.setVerticalScrollBarEnabled(true);


        } catch (IOException e) {
            Log.e("epublib", e.getMessage());
        }

        /*textView = findViewById(R.id.tvBookEpub);
        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        try {
            // find InputStream for book
            InputStream epubInputStream = new FileInputStream(path);

            // Load Book from inputStream
            Book book = (new EpubReader()).readEpub(epubInputStream);

            StringBuilder text = new StringBuilder();
            text.append("Annotation\n").append(book.getMetadata().getDescriptions().toString()
                    .replace("[", "").replace("]", ""))
                    .append("\n").append("\n");

            List<TOCReference> tocReferences = book.getTableOfContents().getTocReferences();
            for (int i = 0; i < tocReferences.size(); i++) {
                text.append(tocReferences.get(i).getTitle()).append("\n");
                List<TOCReference> chapters = tocReferences.get(i).getChildren();
                for (int j = 0; j < chapters.size(); j++) {
                    text.append(chapters.get(j).getTitle()).append("\n");
                    System.out.println(chapters.get(j).getChildren().get(0));
                }
            }
            textView.setText(text);
        } catch (IOException e) {
            Log.e("epublib", e.getMessage());
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainWindow.closeLoadingDialog();
        onBackPressed();
        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Recursively Log the Table of Contents
     *
     * @param tocReferences
     * @param depth
     */
    private String logTableOfContents(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (TOCReference tocReference : tocReferences) {
            StringBuilder tocString = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                tocString.append("\t");
            }
            tocString.append(tocReference.getTitle());
            result.append(tocString).append("\n");

            logTableOfContents(tocReference.getChildren(), depth + 1);
        }
        return result.toString();
    }
}