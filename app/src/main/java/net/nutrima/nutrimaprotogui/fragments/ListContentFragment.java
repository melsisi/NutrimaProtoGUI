/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nutrima.nutrimaprotogui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.nutrima.aws.RestaurantMenuItem;
import net.nutrima.nutrimaprotogui.Business;
import net.nutrima.nutrimaprotogui.Globals;
import net.nutrima.nutrimaprotogui.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides UI for the view with List.
 */
public class ListContentFragment extends Fragment {

    static ContentAdapter adapter;
    static RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // TODO: Only do this after FM has been populated.

        return recyclerView;
    }

    public static void refreshListAfterAWS() {
        adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        adapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avator;
        public TextView name;
        public TextView description;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            avator = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);
        }
    }
    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = Globals.getInstance().getRestaurantFullMenuMap().size();
        private List<Business> businesses = null;
        private Map<Business, List<RestaurantMenuItem>> menuMap = null;

        public ContentAdapter(Context context) {
            if(Globals.getInstance().getRestaurantFullMenuMap() == null ||
                    Globals.getInstance().getRestaurantFullMenuMap().size() == 0)
                return;
            businesses = new ArrayList<>(Globals.getInstance().getRestaurantFullMenuMap().keySet());
            menuMap = Globals.getInstance().getRestaurantFullMenuMap();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(businesses == null || businesses.size() == 0)
                return;
            if(position==0)
                return;
            // TODO: Add place image
            //holder.avator.setImageDrawable(mPlaceAvators[position % mPlaceAvators.length]);
            holder.name.setText(businesses.get(0).getName());
            holder.description.setText(businesses.get(0).getAddress());
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
