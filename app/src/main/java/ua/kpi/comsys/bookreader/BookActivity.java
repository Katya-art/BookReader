package ua.kpi.comsys.bookreader;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.kursx.parser.fb2.Element;
import com.kursx.parser.fb2.FictionBook;
import com.kursx.parser.fb2.P;
import com.kursx.parser.fb2.Section;

import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import ua.kpi.comsys.bookreader.paginator.pagination.ReadState;
import ua.kpi.comsys.bookreader.paginator.view.OnActionListener;
import ua.kpi.comsys.bookreader.paginator.view.OnSwipeListener;
import ua.kpi.comsys.bookreader.paginator.view.PaginatedTextView;

public class BookActivity extends AppCompatActivity implements OnSwipeListener, OnActionListener {

    private String tag = "BookTXT";
    private TextView tvBookName;
    private TextView tvReadPercent;
    private TextView tvReadPages;
    private PaginatedTextView tvBookContent;
    private ActionBar actionBar;
    private CharSequence text;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();

        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        String name = arguments.get("name").toString();

        tvBookName = findViewById(R.id.tv_book_name);
        tvReadPercent = findViewById(R.id.tv_percent_read);
        tvReadPages = findViewById(R.id.tv_pages_read);

        tvBookContent = findViewById(R.id.tv_book_text);

        if (name.endsWith(".txt")) {
            tvBookName.setText(name.replace(".txt", ""));
            text = getTextTXT(path);
        }

        if (name.endsWith(".fb2")) {
            tvBookName.setText(name.replace(".fb2", ""));
            text = getTextFB2(path);
        }

        if (name.endsWith(".epub")) {
            tvBookName.setText(name.replace(".epub", ""));
            text = getTextEpub(path);
        }

        tvBookContent.setup(text);
        tvBookContent.setLinkTextColor(Color.parseColor("#2D170A"));
        tvBookContent.setTextColor(Color.parseColor("#2D170A"));

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            tvBookContent.setLinkTextColor(Color.parseColor("#FFFFFF"));
            tvBookContent.setTextColor(Color.parseColor("#FFFFFF"));
        }

        tvBookContent.setOnActionListener(this);
        tvBookContent.setOnSwipeListener(this);
    }

    @Override
    public void onSwipeLeft() {
        Log.e(tag, "left swipe!");
    }

    @Override
    public void onSwipeRight() {
        Log.e(tag, "right swipe!");
    }

    @Override
    public void onClick(@NotNull String s) {
        showToast("Paragraph clicked: " + s);
    }

    @Override
    public void onLongClick(@NotNull String s) {
        showToast("Word clicked: " + s);
    }

    @Override
    public void onPageLoaded(@NotNull ReadState readState) {
        displayReadState(readState);
    }

    private String getTextTXT(String path) {
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

    private String getTextFB2(String path) {
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

    private String getTextEpub(String path) {
        try {
            // find InputStream for book
            InputStream epubInputStream = new FileInputStream(path);

            // Load Book from inputStream
            Book book = (new EpubReader()).readEpub(epubInputStream);
            StringBuilder data = new StringBuilder();
            List<Resource> bookChapters = book.getContents();
            for (int i = 1; i < bookChapters.size(); i++) {
                data.append(new String(bookChapters.get(i).getData()));
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return String.valueOf(Html.fromHtml(String.valueOf(data),Html.FROM_HTML_MODE_LEGACY));
            } else {
                return String.valueOf(Html.fromHtml(String.valueOf(data)));
            }
        } catch (IOException e) {
            Log.e("epublib", e.getMessage());
            return String.valueOf(e);
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayReadState(ReadState readState) {
        tvReadPages.setText(readState.getCurrentIndex() + "/" + readState.getPagesCount());
        tvReadPercent.setText( readState.getReadPercent() + "%");
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6E382B")));
                tvBookContent.setBackgroundColor(Color.parseColor("#F4E6D7"));
                tvBookContent.setLinkTextColor(Color.parseColor("#2D170A"));
                tvBookContent.setTextColor(Color.parseColor("#2D170A"));
                item.setIcon(R.drawable.mode_night_white_24dp);
                return true;
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#121212")));
            tvBookContent.setBackgroundColor(Color.parseColor("#121212"));
            tvBookContent.setLinkTextColor(Color.parseColor("#FFFFFF"));
            tvBookContent.setTextColor(Color.parseColor("#FFFFFF"));
            item.setIcon(R.drawable.light_mode_white_24dp);
            return true;
        }

        if (id == android.R.id.home) {
            ListOfBooks.closeLoadingDialog();
            onBackPressed();
            return true;
        }

        if (id == R.id.color_black) {
            tvBookContent.setLinkTextColor(Color.parseColor("#121212"));
            tvBookContent.setTextColor(Color.parseColor("#121212"));
            return true;
        }

        if (id == R.id.color_grey) {
            tvBookContent.setLinkTextColor(Color.parseColor("#757575"));
            tvBookContent.setTextColor(Color.parseColor("#757575"));
            return true;
        }

        if (id == R.id.color_white) {
            tvBookContent.setLinkTextColor(Color.parseColor("#FFFFFF"));
            tvBookContent.setTextColor(Color.parseColor("#FFFFFF"));
            return true;
        }

        if (id == R.id.font_default) {
            tvBookContent.setTextPaintTypeface(Typeface.DEFAULT);
            tvBookContent.setup(text);
            tvBookContent.setTypeface(Typeface.DEFAULT);
            return true;
        }

        if (id == R.id.font_monospace) {
            tvBookContent.setTextPaintTypeface(Typeface.MONOSPACE);
            tvBookContent.setup(text);
            tvBookContent.setTypeface(Typeface.MONOSPACE);
            return true;
        }

        if (id == R.id.font_serif) {
            tvBookContent.setTextPaintTypeface(Typeface.SERIF);
            tvBookContent.setup(text);
            tvBookContent.setTypeface(Typeface.SERIF);
        }

        if (id == R.id.size_8 || id == R.id.size_12 || id == R.id.size_14 || id == R.id.size_20 ||
                id == R.id.size_24 || id == R.id.size_32 || id == R.id.size_40 || id == R.id.size_64) {
            tvBookContent.setTextPaintSize((float) (Float.parseFloat(item.getTitle().toString()) * 2.75));
            tvBookContent.setup(text);
            tvBookContent.setTextSize(Float.parseFloat(item.getTitle().toString()));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
