package com.mehdiii.duelgame.views.activities.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.DrawerItem;
import com.mehdiii.duelgame.utils.FontHelper;

import java.util.List;

/**
 * Created by mehdiii on 12/24/15.
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> items;

    public DrawerListAdapter(Context context, int resource, List<DrawerItem> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_drawer_item, null);
            ViewHolder holder = new ViewHolder();
            holder.icon = (TextView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }

        initViews(items.get(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initViews(DrawerItem item, ViewHolder holder) {
        FontHelper.setKoodakFor(context, holder.title);
        holder.icon.setTypeface(FontHelper.getIcons(context));
        holder.icon.setText(item.getIcon());
        holder.title.setText(item.getTitle());
    }

    protected class ViewHolder {
        private TextView icon;
        private TextView title;
    }
}
