package com.example.simpletodo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* Responsible for displaying data from the model into a row in the recycler view. */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    List<String> items;

    /* Constructor for the items adapter, */
    public ItemsAdapter(List<String> items) {
        this.items = items;
    }

    /* Uses layout inflater to inflate a View, wraps it inside
     * a ViewHolder, and returns the ViewHolder. */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate(
                android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todoView);
    }

    /* Responsible for binding data to a particular view holder. */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = items.get(position); // grab the item at the position
        holder.bind(item); // bind the item to the specific ViewHolder
    }

    /* Tells the RecyclerView how many items are in the list. */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /* Container to provide easy access to views that represent each row of the list. */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;

        /* Constructor for the view holder. */
        public ViewHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        /* Updates the view inside of the view holder with this data. */
        public void bind(String item) {
            tvItem.setText(item);
        }
    }
}
