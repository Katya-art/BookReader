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

public class MainWindowActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ListView listView = findViewById(R.id.list);
        books = new ArrayList<>();
        Search_Dir(Environment.getExternalStorageDirectory());
        BookArrayAdapter bookArrayAdapter = new BookArrayAdapter(MainWindowActivity.this, R.layout.book_info, books);
        listView.setAdapter(bookArrayAdapter);
        listView.setOnItemClickListener(this);
    }

    public void Search_Dir(File dir) {
        File FileList[] = dir.listFiles();

        if (FileList != null) {
            for (int i = 0; i < FileList.length; i++) {
                if (FileList[i].isDirectory()) {
                    Search_Dir(FileList[i]);
                } else {
                    if (FileList[i].getName().endsWith(".pdf")){
                        Book book = new Book();
                        book.setName(FileList[i].getName());
                        book.setPath(FileList[i].getAbsolutePath());
                        System.out.println(FileList[i].getAbsolutePath());
                        books.add(book);
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Book book = (Book) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, BookPDF.class);
        intent.putExtra("name", book.getName());
        intent.putExtra("path", book.getPath());
        startActivity(intent);
    }
}
