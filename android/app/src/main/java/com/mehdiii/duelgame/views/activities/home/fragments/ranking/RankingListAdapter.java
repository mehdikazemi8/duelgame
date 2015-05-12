package com.mehdiii.duelgame.views.activities.home.fragments.ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.UserForRanklist;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by omid on 4/11/2015.
 */
public class RankingListAdapter extends ArrayAdapter<UserForRanklist> {

    List<UserForRanklist> users;
    private LayoutInflater layoutInflater;

    public RankingListAdapter(Context context, int resource, List<UserForRanklist> users) {
        super(context, resource);
        this.users = users;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (users != null)
            return users.size();
        else
            return 0;
    }

    @Override
    public void clear() {
        super.clear();
        users.clear();
    }

    @Override
    public UserForRanklist getItem(int position) {
        return users.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.template_ranklist, null);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.imageViewAvatar = (ImageView) convertView.findViewById(R.id.ranking_user_avatar);
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.ranking_user_name);
            viewHolder.textViewProvince = (TextView) convertView.findViewById(R.id.ranking_user_province);
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.ranking_user_title);
            viewHolder.textViewScore = (TextView) convertView.findViewById(R.id.ranking_user_score);
            viewHolder.textViewUserPosition = (TextView) convertView.findViewById(R.id.ranking_user_position);

            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final UserForRanklist user, ViewHolder holder) {
        Picasso.with(getContext()).load(AvatarHelper.getResourceId(getContext(), user.getAvatar())).into(holder.imageViewAvatar);
        holder.textViewTitle.setText(ScoreHelper.getTitle(user.getScore()));
        holder.textViewScore.setText(""+user.getScore());
        holder.textViewUserPosition.setText(""+user.getPlaceInRank());
        holder.textViewProvince.setText(ProvinceManager.get(getContext(), user.getProvince()));
        holder.textViewName.setText(user.getName());
        holder.imageViewAvatar.setImageResource(AvatarHelper.getResourceId(getContext(), user.getAvatar()));
        FontHelper.setKoodakFor(getContext(), holder.textViewName, holder.textViewProvince, holder.textViewTitle, holder.textViewScore, holder.textViewUserPosition);
    }

    protected class ViewHolder {
        private ImageView imageViewAvatar;
        private TextView textViewName;
        private TextView textViewProvince;
        private TextView textViewTitle;
        private TextView textViewScore;
        private TextView textViewUserPosition;
    }
}
