package udacity.com.booksearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button searchButton;
    EditText requestEditText;

    static String requestString;

    public static final String LOG_TAG = "MyTAGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = (Button) findViewById(R.id.buttonSearch);
        requestEditText = (EditText) findViewById(R.id.editTextSearch);

        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonSearch) {
            requestString = requestEditText.getText().toString();
            if (TextUtils.isEmpty(requestString)) {
                return;
            }
            Intent intent = new Intent(this, ResultSearchActivity.class);
            startActivity(intent);
        }
    }
}
