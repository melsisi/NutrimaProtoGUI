package net.nutrima.nutrimaprotogui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.nutrima.aws.RestaurantMenuItem;

import java.util.List;

/**
 * Created by melsisi on 4/21/2016.
 */
public class ListMenuItemAdapter extends ArrayAdapter<RestaurantMenuItem> {

    public ListMenuItemAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListMenuItemAdapter(Context context, int resource, int textViewResourceId, List<RestaurantMenuItem> menu) {
        super(context, resource, textViewResourceId, menu);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_list, null);
        }

        RestaurantMenuItem menuItem = getItem(position);

        if (menuItem != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.list_title);

            if (tt1 != null) {
                tt1.setText(menuItem.getItemName());
            }

            TextView tt2 = (TextView) v.findViewById(R.id.list_desc);
            tt2.setText(menuItem.getItemDescription());
        }

        return v;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
