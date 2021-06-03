package ua.kpi.comsys.bookreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.siegmann.epublib.epub.EpubReader;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(context).inflate(resource, parent, false);

        TextView bookName = view.findViewById(R.id.textView);
        bookName.setText(booksFiltered.get(position).getName());
        BitmapWorkerTask task = new BitmapWorkerTask((ImageView) view.findViewById(R.id.imageView));
        task.execute(position);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap pdfToBitmap(File pdfFile) {
        Bitmap bitmap = null;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
            final int pageCount = renderer.getPageCount();
            if(pageCount>0){
                PdfRenderer.Page page = renderer.openPage(0);
                int width = page.getWidth();
                int height = page.getHeight();
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
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

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
        }

        // Decode image in background.
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Bitmap doInBackground(Integer... params) {
            if (booksFiltered.get(params[0]).getName().endsWith("pdf")) {
                return pdfToBitmap(new File(booksFiltered.get(params[0]).getPath()));
            }
            if (booksFiltered.get(params[0]).getName().endsWith("epub")) {
                try {
                    InputStream epubInputStream = new FileInputStream(booksFiltered.get(params[0]).getPath());
                    nl.siegmann.epublib.domain.Book book = (new EpubReader()).readEpub(epubInputStream);
                    byte[] asd = book.getCoverImage().getData();
                    return BitmapFactory.decodeByteArray(asd, 0, asd.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
