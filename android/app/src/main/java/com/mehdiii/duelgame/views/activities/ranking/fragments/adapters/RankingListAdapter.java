package com.mehdiii.duelgame.views.activities.ranking.fragments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.UserForRanklist;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RankingListAdapter extends ArrayAdapter<UserForRanklist> {

    List<UserForRanklist> users;
    private LayoutInflater layoutInflater;
    Context context;

    public RankingListAdapter(Context context, int resource, List<UserForRanklist> users) {
        super(context, resource);
        this.users = users;
        this.context = context;
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
        Log.d("TAG", "" + position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.template_ranklist, null);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.container = (LinearLayout) convertView.findViewById(R.id.container_wrapper);
            viewHolder.imageViewAvatar = (ImageView) convertView.findViewById(R.id.ranking_user_avatar);
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.ranking_user_name);
            viewHolder.textViewProvince = (TextView) convertView.findViewById(R.id.ranking_user_province);
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.ranking_user_title);
            viewHolder.textViewScore = (TextView) convertView.findViewById(R.id.ranking_user_score);
            viewHolder.textViewUserPosition = (TextView) convertView.findViewById(R.id.ranking_user_position);
            viewHolder.cupPositionHolder = (FrameLayout) convertView.findViewById(R.id.cup_position_holder);
            viewHolder.imageViewCup = (ImageView) convertView.findViewById(R.id.ranking_cup);

            convertView.setTag(viewHolder);
        }
        initializeViews(position, getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private boolean isGold(int p) {
        return 0 <= p && p < 4;
    }

    private boolean isSilver(int p) {
        return 4 <= p && p < 8;
    }

    private boolean isBronze(int p) {
        return 8 <= p && p < 12;
    }

    int getContainerBackgroundColor(int p) {
        if(isGold(p))
            return getContext().getResources().getColor(R.color.gold);
        else if(isSilver(p))
            return getContext().getResources().getColor(R.color.silver);
        else if(isBronze(p))
            return getContext().getResources().getColor(R.color.bronze);
        else
            return getContext().getResources().getColor(R.color.white);
    }

    private void initializeViews(final int position, final UserForRanklist user, ViewHolder holder) {
        Picasso.with(getContext()).load(AvatarHelper.getResourceId(getContext(), user.getAvatar())).into(holder.imageViewAvatar);
        holder.textViewTitle.setText(ScoreHelper.getTitle(user.getScore()));
        holder.textViewScore.setText(""+user.getScore());
        holder.textViewUserPosition.setText("" + user.getRank());
        holder.textViewProvince.setText(ProvinceManager.get(getContext(), user.getProvince()));
        holder.textViewName.setText(user.getName());
        holder.imageViewAvatar.setImageResource(AvatarHelper.getResourceId(getContext(), user.getAvatar()));

        int containerBackgroundColor = getContainerBackgroundColor(position);
        holder.container.setBackgroundColor(containerBackgroundColor);

        // setting cup for the first 12 persons in rank
        if(isGold(position)) {
            holder.imageViewCup.setVisibility(View.VISIBLE);
            holder.imageViewCup.setImageDrawable(context.getResources().getDrawable(R.drawable.cup_gold));
        } else if(isSilver(position)) {
            holder.imageViewCup.setVisibility(View.VISIBLE);
            holder.imageViewCup.setImageDrawable(context.getResources().getDrawable(R.drawable.cup_silver));
        } else if(isBronze(position)) {
            holder.imageViewCup.setVisibility(View.VISIBLE);
            holder.imageViewCup.setImageDrawable(context.getResources().getDrawable(R.drawable.cup_bronze));
        } else {
            holder.imageViewCup.setVisibility(View.INVISIBLE);
        }

        if(user.getId().equals(AuthManager.getCurrentUser().getId())) {
            holder.cupPositionHolder.setBackgroundColor(getContext().getResources().getColor(R.color.green));
//            holder.textViewUserPosition.setBackgroundColor(getContext().getResources().getColor(R.color.green));
        } else {
            holder.cupPositionHolder.setBackgroundColor(getContext().getResources().getColor(R.color.white));
//            holder.textViewUserPosition.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            // holder.textViewUserPosition.setBackgroundColor(containerBackgroundColor);
        }

        FontHelper.setKoodakFor(getContext(), holder.textViewName, holder.textViewProvince, holder.textViewTitle, holder.textViewScore, holder.textViewUserPosition);
    }

    protected class ViewHolder {
        private LinearLayout container;
        private FrameLayout cupPositionHolder;
        private ImageView imageViewAvatar;
        private TextView textViewName;
        private TextView textViewProvince;
        private TextView textViewTitle;
        private TextView textViewScore;
        private TextView textViewUserPosition;
        private ImageView imageViewCup;
    }
}
