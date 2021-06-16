package ua.kpi.comsys.bookreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.MenuItemCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import ua.kpi.comsys.bookreader.models.Book;

public class ListOfBooks extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private BookArrayAdapter bookArrayAdapter;
    private ArrayList<Book> books;
    public static LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_books);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        loadingDialog = new LoadingDialog(ListOfBooks.this);
        ListView listView = findViewById(R.id.list);
        books = new ArrayList<>();
        Search_Dir(Environment.getExternalStorageDirectory());
        bookArrayAdapter = new BookArrayAdapter(ListOfBooks.this, R.layout.book_info, books);
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
            intent = new Intent(this, BookPDFActivity.class);
        } else {
            intent = new Intent(this, BookActivity.class);
        }
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
        MenuItem changeModeItem = menu.findItem(R.id.night_mode);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            changeModeItem.setIcon(R.drawable.light_mode_white_24dp);
        }
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
        if (id == R.id.night_mode) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                return true;
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            return true;
        }
        if (id == R.id.filter_pdf || id == R.id.filter_epub || id == R.id.filter_fb2 || id == R.id.filter_txt) {
            bookArrayAdapter.filterByType((String) item.getTitle());
            return true;
        }
        if (id == R.id.showAll) {
            bookArrayAdapter.showAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void closeLoadingDialog() {
        loadingDialog.dismissDialog();
    }
}
