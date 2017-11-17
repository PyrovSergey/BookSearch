package udacity.com.booksearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

// Класс адаптер для класса Book
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView priceTextView = (TextView) listItemView.findViewById(R.id.price);
        String price = String.valueOf(currentBook.getPrice());
        priceTextView.setText(price);

        TextView autorTextView = (TextView) listItemView.findViewById(R.id.autor);
        autorTextView.setText(currentBook.getAuthor());

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentBook.getTitle());


        return listItemView;
    }
}
