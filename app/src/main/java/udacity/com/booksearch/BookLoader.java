package udacity.com.booksearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

// Класс загрузчик книг BookLoader для выполнения в асинхронном потоке
public class BookLoader extends AsyncTaskLoader<List<Book>> {

    String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e("MyTAGS", "сработал метод onStartLoading()");
    }

    @Override
    public List<Book> loadInBackground() {
        // Если длина переданного массива urls меньше 1 или первая строчка равна null
        // то возвращаем null и ничего дальше не делаем
        if (url == null) {
            return null;
        }

        // иначе создаем список землетрясений result и возвращаем его
        List<Book> books = QueryUtils.fetchBooksData(url);
        Log.e("MyTAGS", "сработал метод loadInBackground()");
        Log.e("MyTAGS", url.toString());
        return books;
    }
}
