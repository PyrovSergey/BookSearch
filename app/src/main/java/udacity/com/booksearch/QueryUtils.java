package udacity.com.booksearch;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static udacity.com.booksearch.MainActivity.LOG_TAG;

// Класс отвечающий за запросы
public final class QueryUtils {

    private QueryUtils() {
    }
    // Обощающий публичный метод, который делает запрос на сервер, получает ответ - строку (JSON)
    // и возвращает список объектов Earthquake
    public static List<Book> fetchBooksData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponce = null;
        try {
            jsonResponce = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Book> books = extractFeatureFromJson(jsonResponce);
        Log.e("QueryUtils", "сработал метод fetchEarthquakeData()");

        return books;
    }

    // Возвращаем список объектов {@link Earthquake}, которые были созданы из разбора JSON-ответа.
    public static List<Book> extractFeatureFromJson(String booksRequestJSON) {
        // Если переданная строка пустая или равна null - возвращаем null и дальше ничего не делаем
        if (TextUtils.isEmpty(booksRequestJSON)) {
            return null;
        }
        // Создайем пустой List, чтобы мы могли начать добавлять землетрясения к
        List<Book> books = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(booksRequestJSON);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                String title = "";
                String autor = "";
                String url = "";
                String price = "н/д";
                JSONObject currentBook = items.optJSONObject(i);
                if (currentBook == null) {
                    continue;
                }
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                title = volumeInfo.getString("title");
                url = volumeInfo.getString("canonicalVolumeLink");
                JSONArray authors = volumeInfo.optJSONArray("authors");
                if (authors != null)
                    autor = authors.getString(0);
                else
                    autor = "Автор не указан";
                JSONObject saleInfo = currentBook.getJSONObject("saleInfo");
                String saleability = saleInfo.getString("saleability");
                if ("FOR_SALE".equals(saleability)) {
                    JSONObject retailPrice = saleInfo.getJSONObject("retailPrice");
                    price = retailPrice.getString("amount");
                }
                books.add(new Book(title, autor, price, url));
            }
        } catch (JSONException e) {
            Log.e("MyTAGS", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }

    // Сделайте HTTP-запрос к указанному URL-адресу и верните строку (JSON) как ответ.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Чтение строки из входящего потока, которая содержит весь ответ JSON с сервера.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Возвращает новый URL-объект из заданного строкового URL-адреса.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
}
