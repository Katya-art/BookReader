package ua.kpi.comsys.bookreader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainWindowActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int EXT_STORAGE_PERMISSION_CODE = 0 ;
    private static final String TAG = "";
    Button btnJava_Programming_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        if (ContextCompat.checkSelfPermission(
                MainWindowActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                    MainWindowActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXT_STORAGE_PERMISSION_CODE);
            Log.d(TAG, "After getting permission: "+ Manifest.permission.WRITE_EXTERNAL_STORAGE +
                    " " + ContextCompat.checkSelfPermission(
                    MainWindowActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE));

        } else {
            // We were granted permission already before
            Log.d(TAG, "Already has permission to write to external storage");
        }

        btnJava_Programming_pdf = (Button) findViewById(R.id.btnJava_Programming_pdf);
        btnJava_Programming_pdf.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnJava_Programming_pdf:
                // TODO Call second activity
                Intent intent1 = new Intent(this, BookPDF.class);
                startActivity(intent1);
                break;

            default:
                break;
        }
    }
}
