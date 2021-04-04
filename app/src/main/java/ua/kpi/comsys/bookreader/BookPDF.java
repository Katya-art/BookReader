package ua.kpi.comsys.bookreader;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class BookPDF extends AppCompatActivity {

    PDFView bookPDFView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pdf);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookPDFView = findViewById(R.id.pdfView);
        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        bookPDFView.fromFile(new File(path)).load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        return super.onOptionsItemSelected(item);
    }
}