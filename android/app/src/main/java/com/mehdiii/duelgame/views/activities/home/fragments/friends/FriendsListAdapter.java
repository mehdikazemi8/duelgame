package com.mehdiii.duelgame.views.activities.home.fragments.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.views.custom.AvatarViewer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by omid on 4/11/2015.
 */
public class FriendsListAdapter extends ArrayAdapter<User> {
    List<User> friends;
    private LayoutInflater layoutInflater;

    public FriendsListAdapter(Context context, int resource, List<User> friends) {
        super(context, resource);
        this.friends = friends;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (friends != null)
            return friends.size();

        else
            return 0;
    }

    @Override
    public User getItem(int position) {
        return friends.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.template_friends_list, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.textView_title);
            viewHolder.textViewLevel = (TextView) convertView.findViewById(R.id.textView_level);
            viewHolder.textViewProvince = (TextView) convertView.findViewById(R.id.textView_province);
            viewHolder.imageViewAvatar = (AvatarViewer) convertView.findViewById(R.id.imageView_avatar);

            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(User friend, ViewHolder holder) {
        Picasso.with(getContext()).load(AvatarHelper.getResourceId(getContext(), friend.getAvatar())).into(holder.imageViewAvatar);
        holder.textViewTitle.setText(friend.getName());
        holder.textViewProvince.setText(friend.getProvince());
        holder.textViewLevel.setText("lvl" + friend);
    }

    protected class ViewHolder {
        private AvatarViewer imageViewAvatar;
        private TextView textViewTitle;
        private TextView textViewLevel;
        private TextView textViewProvince;
    }

}
