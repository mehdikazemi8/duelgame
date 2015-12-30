package com.mehdiii.duelgame.views.activities.home.fragments.duelhourtotal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.UserForRanklist;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mehdiii on 12/30/15.
 */
public class DuelHourTotalAdapter extends ArrayAdapter<UserForRanklist> {

    Context context;

    public DuelHourTotalAdapter(Context context, int resource, List<UserForRanklist> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.template_duel_hour_total, null);

            ViewHolder holder = new ViewHolder();

            holder.gold = (CustomTextView) convertView.findViewById(R.id.gold_cnt);
            holder.silver = (CustomTextView) convertView.findViewById(R.id.silver_cnt);
            holder.bronze = (CustomTextView) convertView.findViewById(R.id.bronze_cnt);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.name = (CustomTextView) convertView.findViewById(R.id.name);
            holder.province = (CustomTextView) convertView.findViewById(R.id.province);
            holder.rank = (CustomTextView) convertView.findViewById(R.id.rank);

            convertView.setTag(holder);
        }

        initViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initViews(UserForRanklist user, ViewHolder holder) {
        holder.gold.setText(String.valueOf(user.getCups().get(0)));
        holder.silver.setText(String.valueOf(user.getCups().get(1)));
        holder.bronze.setText(String.valueOf(user.getCups().get(2)));
        holder.name.setText(user.getName());
        holder.province.setText(ProvinceManager.get(context, user.getProvince()));
        Picasso.with(context).load(AvatarHelper.getResourceId(context, user.getAvatar())).into(holder.avatar);
        holder.rank.setText(String.valueOf(user.getRank()));
    }

    protected class ViewHolder {
        private CustomTextView gold;
        private CustomTextView silver;
        private CustomTextView bronze;
        private ImageView avatar;
        private CustomTextView province;
        private CustomTextView name;
        private CustomTextView rank;
    }
}
