package com.mehdiii.duelgame.views.activities.home.fragments.friends;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.MutualCourseStat;
import com.mehdiii.duelgame.utils.FontHelper;

import java.util.List;

/**
 * Created by mehdiii on 12/8/15.
 */
public class StatisticsListAdapter extends ArrayAdapter<MutualCourseStat> {
    List<MutualCourseStat> items;

    public StatisticsListAdapter(Context context, int resource, List<MutualCourseStat> objects) {
        super(context, resource, objects);
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_mutual_course_statistics, null);
            ViewHolder holder = new ViewHolder();

            holder.container = (LinearLayout) convertView.findViewById(R.id.container_wrapper);
            holder.courseTitle = (TextView) convertView.findViewById(R.id.course_title);
            holder.winCounter = (TextView) convertView.findViewById(R.id.win_counter);
            holder.loseCounter = (TextView) convertView.findViewById(R.id.lose_counter);
            holder.drawCounter = (TextView) convertView.findViewById(R.id.draw_counter);

            convertView.setTag(holder);
        }

        initViews(getItem(position), (ViewHolder) convertView.getTag());

        return convertView;
    }

    private void initViews(MutualCourseStat stat, ViewHolder holder) {
        holder.drawCounter.setText(String.valueOf(stat.getDraw()));
        holder.loseCounter.setText(String.valueOf(stat.getLose()));
        holder.winCounter.setText(String.valueOf(stat.getWin()));
        holder.courseTitle.setText(stat.getCourseName());
        FontHelper.setKoodakFor(getContext(), holder.drawCounter, holder.winCounter, holder.loseCounter, holder.courseTitle);
    }

    protected class ViewHolder {
        LinearLayout container;
        private TextView winCounter;
        private TextView loseCounter;
        private TextView drawCounter;
        private TextView courseTitle;
    }
}
