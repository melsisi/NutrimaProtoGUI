package net.nutrima.nutrimaprotogui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melsisi on 4/21/2016.
 */
public class LogMealItemAdapter extends ArrayAdapter<ArrayList<String>> {

    public LogMealItemAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public LogMealItemAdapter(Context context, int resource, int textViewResourceId,
                              List<ArrayList<String>> plates) {
        super(context, resource, textViewResourceId, plates);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.log_list_item, null);
        }

        String amount = getItem(position).get(0);
        String measure = getItem(position).get(1);
        String item = getItem(position).get(2);
        //String brand = getItem(position).get(3);

        if (amount != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.amount_textview);
            tt1.setTextSize(20);
            tt1.setText(amount);

            TextView tt2 = (TextView) v.findViewById(R.id.measure_textview);
            tt2.setTextSize(20);
            tt2.setText(measure);

            TextView tt3 = (TextView) v.findViewById(R.id.item_textview);
            tt3.setTextSize(20);
            tt3.setText(item);

            // TODO: Handle brand
            /*TextView tt4 = (TextView) v.findViewById(R.id.brand_textview);
            tt4.setTextSize(20);
            tt4.setText(brand);*/
        }

        return v;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
