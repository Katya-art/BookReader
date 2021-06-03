package ua.kpi.comsys.bookreader;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

public class BookEPUB extends AppCompatActivity {
    TextView textView;
    LoadingDialog loadingDialog;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_epub);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.bookEpub);
        textView.setOverScrollMode(1);
        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        new LoadBook().execute(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Initialize menu inflater
        MenuInflater menuInflater = getMenuInflater();
        //Inflate menu
        menuInflater.inflate(R.menu.night_mode_menu, menu);
        MenuItem changeModeItem = menu.findItem(R.id.night_mode);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            changeModeItem.setIcon(R.drawable.light_mode_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.night_mode) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                return true;
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            return true;
        }
        //MainWindow.closeLoadingDialog();
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    class LoadBook extends AsyncTask<String, Void, String[]> {
        //This method will run on UIThread and it will execute before doInBackground
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = new LoadingDialog(BookEPUB.this);
            loadingDialog.startLoadingDialog();
        }

        //This method will run on background thread and after completion it will return result to onPostExecute
        @Override
        protected String[] doInBackground(String... strings) {
            try {
                // find InputStream for book
                InputStream epubInputStream = new FileInputStream(strings[0]);

                // Load Book from inputStream
                Book book = (new EpubReader()).readEpub(epubInputStream);
                StringBuilder data = new StringBuilder();
                List<Resource> bookChapters = book.getContents();
                for (int i = 1; i < bookChapters.size(); i++) {
                    data.append(new String(bookChapters.get(i).getData()));
                }
                return new String[] {strings[0], String.valueOf(data)};
            } catch (IOException e) {
                Log.e("epublib", e.getMessage());
            }
            return null;
        }

        //This method runs on UIThread and it will execute when doINBackground is completed
        @Override
        protected void onPostExecute(String... s) {
            super.onPostExecute(s);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(s[1],Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setText(Html.fromHtml(s[1]));
            }
            loadingDialog.dismissDialog();
        }
    }
}