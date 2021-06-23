package com.example.simpletodo;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setTitle("Edit Item");

        // Get the components ot the activity
        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);
        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // When the user is done editing, they click the save button
        btnSave.setOnClickListener(v -> {
            String newItem = etItem.getText().toString();
            if (newItem.length() > 0) { // ignore empty item inputs
                // Create intent which will contain the results
                Intent intent = new Intent();

                // Pass the data (results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, newItem);
                intent.putExtra(MainActivity.KEY_ITEM_POSITION,
                        getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // Set the result of the intent, finish activity, close the screen and go back
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}