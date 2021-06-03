package ua.kpi.comsys.bookreader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_book_txt);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        textView1 = findViewById(R.id.tvText1);
        textView2 = findViewById(R.id.tvText2);
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
            int index = s.length()/2;
            textView1.setText(s.substring(0, index));
            textView2.setText(s.substring(index));
            loadingDialog.dismissDialog();
        }
    }
}
