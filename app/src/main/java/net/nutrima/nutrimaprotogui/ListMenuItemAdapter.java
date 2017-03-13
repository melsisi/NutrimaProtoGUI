package net.nutrima.nutrimaprotogui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.nutrima.aws.RestaurantMenuItem;

import java.text.DecimalFormat;
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
            TextView plateName = (TextView) v.findViewById(R.id.list_title);
            if (plateName != null) {
                if(menuItem.getItemName().length() < 35)
                    plateName.setText(menuItem.getItemName());
                else
                    plateName.setText(menuItem.getItemName().substring(0, 34) + "...");
            }

            TextView plateDesc = (TextView) v.findViewById(R.id.list_desc);
            if(plateDesc!= null)
                plateDesc.setText(menuItem.getItemDescription());

            TextView businessName = (TextView) v.findViewById(R.id.list_business);
            if(businessName != null)
                businessName.setText("");

            TextView nutrients = (TextView) v.findViewById(R.id.list_nutrient);
            if(nutrients!= null)
                nutrients.setText(menuItem.getCalories() + " cals");

            TextView distanceTextview = (TextView) v.findViewById(R.id.list_dist);
            if(menuItem.getBusiness() != null) {
                double distance = distance(FindHeavyOperations.mLastLocation.getLatitude(),
                        FindHeavyOperations.mLastLocation.getLongitude(),
                        menuItem.getBusiness().getCoordinates().getLatitude(),
                        menuItem.getBusiness().getCoordinates().getLongitude(),
                        "M");
                DecimalFormat df = new DecimalFormat("#.#");
                if (distanceTextview != null)
                    distanceTextview.setText(df.format(distance) + " miles");
            }
            else {
                if (distanceTextview != null)
                    distanceTextview.setText("");
            }
        }

        return v;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // TODO: Move to helpers
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
