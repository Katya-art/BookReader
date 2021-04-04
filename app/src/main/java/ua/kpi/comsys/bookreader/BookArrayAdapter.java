package ua.kpi.comsys.bookreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import ua.kpi.comsys.bookreader.models.Book;

public class BookArrayAdapter extends ArrayAdapter {

    private List<Book> books;
    private int resource;
    private Context context;

    public BookArrayAdapter(Context context, int resource, List<Book> books) {
        super(context, resource, books);
        this.context = context;
        this.resource = resource;
        this.books = books;
    }

    @NonNull
    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        TextView bookName = (TextView) view.findViewById(R.id.textView);
        bookName.setText(books.get(position).getName());
        return view;
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

}
