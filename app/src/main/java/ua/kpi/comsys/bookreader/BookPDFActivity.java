package ua.kpi.comsys.bookreader;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.Objects;

public class BookPDFActivity extends AppCompatActivity {

    PDFView bookPDFView;
    ActionBar actionBar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pdf);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        bookPDFView = findViewById(R.id.pdfView);
        actionBar = getSupportActionBar();
        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        bookPDFView.fromFile(new File(path))
                .nightMode(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                .load();
        //bookPDFView.setNightMode(true);
        //bookPDFView.canScrollHorizontally(bookPDFView.getLayoutDirection());
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
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6200EE")));
                bookPDFView.setNightMode(false);
                bookPDFView.requestLayout();
                item.setIcon(R.drawable.mode_night_white_24dp);
                return true;
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            System.out.println("TEST " + AppCompatDelegate.MODE_NIGHT_YES);
            item.setIcon(R.drawable.light_mode_white_24dp);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#121212")));
            bookPDFView.setNightMode(true);
            bookPDFView.requestLayout();
            return true;
        }
        //MainWindow.closeLoadingDialog();
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}