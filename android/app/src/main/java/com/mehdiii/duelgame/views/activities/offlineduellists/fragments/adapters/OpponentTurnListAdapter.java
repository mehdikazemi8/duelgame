package com.mehdiii.duelgame.views.activities.offlineduellists.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.OfflineDuel;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mehdiii on 2/4/16.
 */
public class OpponentTurnListAdapter extends ArrayAdapter<OfflineDuel> {

    public OpponentTurnListAdapter(Context context, int resource, List<OfflineDuel> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_opponent_turn, null);

            ViewHolder holder = new ViewHolder();
            holder.courseName = (CustomTextView) convertView.findViewById(R.id.course_name);
            holder.opponentName = (CustomTextView) convertView.findViewById(R.id.opponent_name);
            holder.opponentProvince = (CustomTextView) convertView.findViewById(R.id.opponent_province);
            holder.userDuelScore = (CustomTextView) convertView.findViewById(R.id.user_duel_score);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);

            convertView.setTag(holder);
        }

        initViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initViews(OfflineDuel offlineDuel, ViewHolder holder) {
        Picasso.with(getContext()).load(AvatarHelper.getResourceId(getContext(), offlineDuel.getOpponent().getAvatar())).into(holder.avatar);
        holder.courseName.setText(offlineDuel.getCourseName());
        holder.opponentName.setText(offlineDuel.getOpponent().getName());
        holder.opponentProvince.setText(ProvinceManager.get(getContext(), offlineDuel.getOpponent().getProvince()));
        holder.userDuelScore.setText(String.valueOf(offlineDuel.getUserDuelScore()));
    }

    protected class ViewHolder {
        private CustomTextView courseName;
        private CustomTextView opponentName;
        private CustomTextView opponentProvince;
        private CustomTextView userDuelScore;
        private ImageView avatar;
    }
}
