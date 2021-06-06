package ua.kpi.comsys.bookreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.kursx.parser.fb2.Element;
import com.kursx.parser.fb2.FictionBook;
import com.kursx.parser.fb2.P;
import com.kursx.parser.fb2.Person;
import com.kursx.parser.fb2.Section;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

public class BookFB2 extends AppCompatActivity {

    TextView textView;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_fb2);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.tvText);
        actionBar = getSupportActionBar();
        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        textView.setText(bookText(path));
        //textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
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
                textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textView.setTextColor(Color.parseColor("#757575"));
                item.setIcon(R.drawable.mode_night_white_24dp);
                return true;
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#121212")));
            textView.setBackgroundColor(Color.parseColor("#121212"));
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            item.setIcon(R.drawable.light_mode_white_24dp);
            return true;
        }

        //MainWindow.closeLoadingDialog();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.color_black) {
            textView.setTextColor(Color.parseColor("#121212"));
            return true;
        }

        if (id == R.id.color_grey) {
            textView.setTextColor(Color.parseColor("#757575"));
            return true;
        }

        if (id == R.id.color_white) {
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            return true;
        }

        if (id == R.id.font_default) {
            textView.setTypeface(Typeface.DEFAULT);
            return true;
        }

        if (id == R.id.font_monospace) {
            textView.setTypeface(Typeface.MONOSPACE);
            return true;
        }

        if (id == R.id.font_serif) {
            textView.setTypeface(Typeface.SERIF);
        }

        if (id == R.id.size_8 || id == R.id.size_12 || id == R.id.size_14 || id == R.id.size_20 ||
                id == R.id.size_24 || id == R.id.size_32 || id == R.id.size_40 || id == R.id.size_64) {
            textView.setTextSize(Float.parseFloat(item.getTitle().toString()));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String bookText(String path) {
        StringBuilder text = new StringBuilder();
        try {
            FictionBook fb2 = new FictionBook(new File(path));
            ArrayList<P> paragraphs = fb2.getBody().getTitle().getParagraphs();
            for (int i = 0; i < paragraphs.size(); i++) {
                text.append(paragraphs.get(i).getText()).append("\n");
            }
            ArrayList<Section> sections = fb2.getBody().getSections();
            for (int i = 0; i < sections.size(); i++) {
                ArrayList<Element> elements = sections.get(i).getElements();
                String title = sections.get(i).toString();
                text.append(title.substring(0, title.length() - 4)).append("\n");
                for (int j = 0; j < elements.size(); j++) {
                    text.append(elements.get(j).getText()).append("\n");
                }
            }
            //text.append(fb2.getBody().getSections().get(0).getElements().get(0).getText());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
}