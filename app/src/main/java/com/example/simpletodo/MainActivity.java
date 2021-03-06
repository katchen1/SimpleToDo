package com.example.simpletodo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the components of the activity
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        // Load the to-do list from the file system
        loadItems();

        // Control behavior for when an item is short clicked
        ItemsAdapter.OnClickListener onClickListener = position -> {
            Intent i = new Intent(MainActivity.this, EditActivity.class);
            i.putExtra(KEY_ITEM_TEXT, items.get(position));
            i.putExtra(KEY_ITEM_POSITION, position);
            startActivityForResult(i, EDIT_TEXT_CODE);
        };

        // Control behavior for when an item is long clicked
        ItemsAdapter.OnLongClickListener onLongClickListener = position -> {
            items.remove(position); // delete the item from the model
            itemsAdapter.notifyItemRemoved(position); // notify the adapter
            Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
            saveItems(); // write to the file system
        };

        // Create the items adapter for the RecyclerView
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Create on-click listener for the Button
        btnAdd.setOnClickListener(v -> {
            String todoItem = etItem.getText().toString();
            if (todoItem.length() > 0) { // ignore empty item inputs
                items.add(todoItem); // add item to the model
                itemsAdapter.notifyItemInserted(items.size() - 1); // notify adapter of insertion
                etItem.setText(""); // clear the edit text
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems(); // write to the file system
            }
        });
    }

    /* Handles the result of the edit activity. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the updated text value and original position of the edited item
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            items.set(position, itemText); // update the list model with the new item txt
            itemsAdapter.notifyItemChanged(position); // notify the adapter
            saveItems(); // persist the changes
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    /* Retrieves the file where the to-do list is stored. */
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    /* Loads items by reading every line of the data file. */
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    /* Saves items by writing them into the data file. */
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}