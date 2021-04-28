package ua.kpi.comsys.bookreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

import ua.kpi.comsys.bookreader.models.Book;

public class MainWindow extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ListView listView = findViewById(R.id.list);
        books = new ArrayList<>();
        Search_Dir(Environment.getExternalStorageDirectory());
        BookArrayAdapter bookArrayAdapter = new BookArrayAdapter(MainWindow.this, R.layout.book_info, books);
        listView.setAdapter(bookArrayAdapter);
        listView.setOnItemClickListener(this);
    }

    public void Search_Dir(File dir) {
        File[] FileList = dir.listFiles();

        if (FileList != null) {
            for (File file : FileList) {
                if (file.isDirectory()) {
                    Search_Dir(file);
                } else {
                    if (file.getName().endsWith(".pdf") ||
                            file.getName().endsWith(".txt") ||
                            file.getName().endsWith(".fb2") ||
                            file.getName().endsWith(".epub")){
                        Book book = new Book();
                        book.setName(file.getName());
                        book.setPath(file.getAbsolutePath());
                        System.out.println(file.getAbsolutePath());
                        books.add(book);
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Book book = (Book) parent.getItemAtPosition(position);
        Intent intent = null;
        if (book.getName().endsWith(".pdf")) {
            intent = new Intent(this, BookPDF.class);
        } else if (book.getName().endsWith(".txt")) {
            intent = new Intent(this, BookTXT.class);
        } else if (book.getName().endsWith(".fb2")) {
            intent = new Intent(this, BookFB2.class);
        } else if (book.getName().endsWith(".epub")) {
            intent = new Intent(this, BookEPUB.class);
        }
        assert intent != null;
        intent.putExtra("name", book.getName());
        intent.putExtra("path", book.getPath());
        startActivity(intent);
    }
}
