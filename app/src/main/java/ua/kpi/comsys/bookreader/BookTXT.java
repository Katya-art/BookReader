package ua.kpi.comsys.bookreader;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class BookTXT extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    LoadingDialog loadingDialog;
    ActionBar actionBar;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_book_txt);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //textView1 = findViewById(R.id.tvText1);
        //textView2 = findViewById(R.id.tvText2);
        linearLayout = findViewById(R.id.layout);
        actionBar = getSupportActionBar();
        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        new LoadBook().execute(path);
    }

    private String openFile(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Error";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Initialize menu inflater
        MenuInflater menuInflater = getMenuInflater();
        //Inflate menu
        menuInflater.inflate(R.menu.book_setting_menu, menu);
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
                textView1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textView1.setTextColor(Color.parseColor("#757575"));
                textView2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textView2.setTextColor(Color.parseColor("#757575"));
                item.setIcon(R.drawable.mode_night_white_24dp);
                return true;
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#121212")));
            textView1.setBackgroundColor(Color.parseColor("#121212"));
            textView1.setTextColor(Color.parseColor("#FFFFFF"));
            textView2.setBackgroundColor(Color.parseColor("#121212"));
            textView2.setTextColor(Color.parseColor("#FFFFFF"));
            item.setIcon(R.drawable.light_mode_white_24dp);
            return true;
        }

        //MainWindow.closeLoadingDialog();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.color_black) {
            textView1.setTextColor(Color.parseColor("#121212"));
            textView2.setTextColor(Color.parseColor("#121212"));
            return true;
        }

        if (id == R.id.color_grey) {
            textView1.setTextColor(Color.parseColor("#757575"));
            textView2.setTextColor(Color.parseColor("#757575"));
            return true;
        }

        if (id == R.id.color_white) {
            textView1.setTextColor(Color.parseColor("#FFFFFF"));
            textView2.setTextColor(Color.parseColor("#FFFFFF"));
            return true;
        }

        if (id == R.id.font_default) {
            textView1.setTypeface(Typeface.DEFAULT);
            textView2.setTypeface(Typeface.DEFAULT);
            return true;
        }

        if (id == R.id.font_monospace) {
            textView1.setTypeface(Typeface.MONOSPACE);
            textView2.setTypeface(Typeface.MONOSPACE);
            return true;
        }

        if (id == R.id.font_serif) {
            textView1.setTypeface(Typeface.SERIF);
            textView2.setTypeface(Typeface.SERIF);
        }

        if (id == R.id.size_8 || id == R.id.size_12 || id == R.id.size_14 || id == R.id.size_20 ||
                id == R.id.size_24 || id == R.id.size_32 || id == R.id.size_40 || id == R.id.size_64) {
            textView1.setTextSize(Float.parseFloat(item.getTitle().toString()));
            textView2.setTextSize(Float.parseFloat(item.getTitle().toString()));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadBook extends AsyncTask<String, Void, String> {
        //This method will run on UIThread and it will execute before doInBackground
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = new LoadingDialog(BookTXT.this);
            loadingDialog.startLoadingDialog();
        }

        //This method will run on background thread and after completion it will return result to onPostExecute
        @Override
        protected String doInBackground(String... strings) {
            return openFile(strings[0]);
        }

        //This method runs on UIThread and it will execute when doINBackground is completed
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int numberOfViews = s.length() / 1000;
            for (int i = 0; i < numberOfViews; i++) {
                TextView textView = new TextView(BookTXT.this);
                textView.setText(s.substring(i * 1000, (i + 1) * 1000));
                textView.setTextSize(26);
                linearLayout.addView(textView);
            }
            /*int index = s.length()/2;
            textView1.setText(s.substring(0, index));
            textView2.setText(s.substring(index));*/
            loadingDialog.dismissDialog();
        }
    }
}
