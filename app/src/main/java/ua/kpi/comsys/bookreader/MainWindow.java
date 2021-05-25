package ua.kpi.comsys.bookreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.io.File;
import java.util.ArrayList;

import ua.kpi.comsys.bookreader.models.Book;

public class MainWindow extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private BookArrayAdapter bookArrayAdapter;
    private ArrayList<Book> books;
    public static LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        loadingDialog = new LoadingDialog(MainWindow.this);
        ListView listView = findViewById(R.id.list);
        books = new ArrayList<>();
        Search_Dir(Environment.getExternalStorageDirectory());
        bookArrayAdapter = new BookArrayAdapter(MainWindow.this, R.layout.book_info, books);
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
        loadingDialog.startLoadingDialog();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Initialize menu inflater
        MenuInflater menuInflater = getMenuInflater();
        //Inflate menu
        menuInflater.inflate(R.menu.top_menu, menu);
        //Initialize menu item
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //Initialize search view
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bookArrayAdapter.getFilter().filter(newText);
                return true;
            }
        });

        /*MenuItem sortItem = menu.findItem(R.id.action_sort);
        Button button = (Button) MenuItemCompat.getActionView(sortItem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookArrayAdapter.sort();
            }
        });*/
        /*MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bookArrayAdapter.getFilter().filter(newText);
                return true;
            }
        });*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_sort) {
            bookArrayAdapter.sort();
            return true;
        }
        if (id == R.id.action_sort_reversed) {
            bookArrayAdapter.sortReversed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void closeLoadingDialog() {
        loadingDialog.dismissDialog();
    }
}
