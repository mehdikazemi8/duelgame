package com.mehdiii.duelgame.views.activities.offlineduellists.fragments.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mehdiii.duelgame.models.OfflineDuel;

import java.util.List;

/**
 * Created by mehdiii on 2/4/16.
 */
public class BaseOfflineDuelAdapter extends ArrayAdapter<OfflineDuel> {

    public BaseOfflineDuelAdapter(Context context, int resource, List<OfflineDuel> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
