package ua.kpi.comsys.bookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class BookPDF extends AppCompatActivity {

    PDFView bookPDFView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pdf);

        bookPDFView = findViewById(R.id.pdfView);
        bookPDFView.fromAsset("Java.Programming.pdf").load();
    }
}