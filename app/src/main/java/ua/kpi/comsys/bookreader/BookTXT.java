package ua.kpi.comsys.bookreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BookTXT extends AppCompatActivity {

    TextView textView;
    String text;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_book_txt);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingDialog = new LoadingDialog(BookTXT.this);
        loadingDialog.startLoadingDialog();
        textView = findViewById(R.id.tvText);
        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        text = openFile(path);
    }

    @Override
    protected void onStart() {
        super.onStart();
        textView.setText(text);
        loadingDialog.dismissDialog();
    }

    private String openFile(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer);
            //llProgressBar.setVisibility(View.GONE);
            //textView.setText(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Error";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainWindow.closeLoadingDialog();
        onBackPressed();
        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        return super.onOptionsItemSelected(item);
    }
}
