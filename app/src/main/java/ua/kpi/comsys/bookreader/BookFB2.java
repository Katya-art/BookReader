package ua.kpi.comsys.bookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import javax.xml.parsers.ParserConfigurationException;

public class BookFB2 extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_fb2);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.tvText);
        Bundle arguments = getIntent().getExtras();
        String path = arguments.get("path").toString();
        textView.setText(bookText(path));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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