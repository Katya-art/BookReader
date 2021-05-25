package ua.kpi.comsys.bookreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ua.kpi.comsys.bookreader.models.Book;

public class BookArrayAdapter extends ArrayAdapter implements Filterable {

    private List<Book> books;
    private List<Book> booksFiltered;
    private int resource;
    private Context context;

    public BookArrayAdapter(Context context, int resource, List<Book> books) {
        super(context, resource, books);
        this.context = context;
        this.resource = resource;
        this.books = books;
        booksFiltered = new ArrayList<>(books);
    }

    @Override
    public int getCount() {
        return booksFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return booksFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);

        TextView bookName = view.findViewById(R.id.textView);
        bookName.setText(booksFiltered.get(position).getName());
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(books);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Book item : books) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            booksFiltered.clear();
            booksFiltered.addAll((List) results.values);
            if (booksFiltered.size() == 0) {
                Toast.makeText(getContext(), "No matches", Toast.LENGTH_SHORT).show();
            }
            notifyDataSetChanged();
        }
    };

    public void sort() {
        Collections.sort(booksFiltered, new Book());
        notifyDataSetChanged();
    }

    public void sortReversed() {
        Collections.sort(booksFiltered, Collections.reverseOrder(new Book()));
        notifyDataSetChanged();
    }
}
