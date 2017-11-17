package udacity.com.booksearch;

// Класс книга
public class Book {
    // Название книги
    private String title;

    // Автор книги
    private String author;

    // Цена книги
    private String price;

    // Ссылка
    private String url;

    // Конструктор
    public Book(String title, String author, String price, String url) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }
}
