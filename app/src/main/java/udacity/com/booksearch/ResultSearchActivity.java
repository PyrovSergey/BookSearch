package udacity.com.booksearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultSearchActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<Book>> {
    // Адаптет для книг
    private BookAdapter bookAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    private static final String str1 = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String str2 = "&maxResults=40";
    private static String result;

    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        result = str1 + MainActivity.requestString + str2;
        result = result.replaceAll(" ", "+");



        ListView booksListView = (ListView) findViewById(R.id.list);
        bookAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Цепляем ссылку на "пустой" View
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);

        booksListView.setAdapter(bookAdapter);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Находим текущее книгу, на которю было нажатие
                Book currentBook = bookAdapter.getItem(i);

                // Преобразование String URL в объект URI (для перехода в конструктор Intent)
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // Создаем новое намерение для просмотра URI землетрясения
                Intent bookIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Посылаем команду на запуск браузера
                startActivity(bookIntent);
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        Log.e("MyTAGS", "сработал метод initLoader()");

        // Получить ссылку на ConnectivityManager для проверки состояния сетевого подключения
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Получить информацию о текущей активной сети передачи данных по умолчанию
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Если есть сетевое подключение, выберите данные
        if (networkInfo != null && networkInfo.isConnected()) {
            // Получите ссылку на LoaderManager, чтобы взаимодействовать с загрузчиками.
            loaderManager = getLoaderManager();

            // Инициализация загрузчика. Перейдите в константу ID ID, указанную выше, и передайте значение null для
            // связка. Перейдите в эту операцию для параметра LoaderCallbacks (который действителен
            // потому что эта активность реализует интерфейс LoaderCallbacks).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // В противном случае, ошибка отображения
            // Сначала скройте индикатор загрузки, чтобы было видно сообщение об ошибке
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Обновить пустое состояние без сообщения об ошибке подключения
            mEmptyStateTextView.setText("No internet connection");
        }
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        Log.e("MyTAGS", "сработал метод onCreateLoader()" );
        return new BookLoader(this, result);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Очистите адаптер предыдущих данных книг
        bookAdapter.clear();
        // Если есть допустимый список {@link Book} s, добавьте их в адаптер
        // набор данных. Это приведет к обновлению ListView.
        if (books != null && !books.isEmpty()) {
            bookAdapter.addAll(books);
        }
        Log.e("MyTAGS", "сработал метод onLoadFinished()");
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Очистите адаптер предыдущих данных по книгам
        bookAdapter.clear();
        result = "";
        Log.e("MyTAGS", "сработал метод onLoaderReset()");
    }
}

