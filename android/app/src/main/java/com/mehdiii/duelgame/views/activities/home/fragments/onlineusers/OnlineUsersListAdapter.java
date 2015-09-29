package com.mehdiii.duelgame.views.activities.home.fragments.onlineusers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.models.FriendRequest;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.dialogs.HeartLowDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mehdiii on 9/29/15.
 */
public class OnlineUsersListAdapter extends ArrayAdapter<Friend> {
    List<Friend> users;
    private LayoutInflater layoutInflater;
    ProgressBar progressBar;

    public OnlineUsersListAdapter(Context context, int resource, List<Friend> users, ProgressBar progressBar) {
        super(context, resource);
        this.users = users;
        this.layoutInflater = LayoutInflater.from(context);
        this.progressBar = progressBar;
    }

    @Override
    public int getCount() {
        if(users == null)
            return 0;
        return users.size();
    }

    @Override
    public void clear() {
        super.clear();
        users.clear();
    }

    @Override
    public Friend getItem(int position) {
        return users.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.template_online_users_list, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.textView_title);
            viewHolder.textViewProvince = (TextView) convertView.findViewById(R.id.textView_province);
            viewHolder.imageViewAvatar = (ImageView) convertView.findViewById(R.id.imageView_avatar);
            viewHolder.onlineImageView = (ImageView) convertView.findViewById(R.id.imageView_online);
            viewHolder.container = (LinearLayout) convertView.findViewById(R.id.container_wrapper);
            viewHolder.addFriend = (Button) convertView.findViewById(R.id.button_add_friend);
            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final Friend friend, ViewHolder holder) {
        Picasso.with(getContext()).load(AvatarHelper.getResourceId(getContext(), friend.getAvatar())).into(holder.imageViewAvatar);
        holder.textViewTitle.setText(friend.getName());
        holder.textViewProvince.setText(ProvinceManager.get(getContext(), friend.getProvince()));
        holder.imageViewAvatar.setImageResource(AvatarHelper.getResourceId(getContext(), friend.getAvatar()));
        FontHelper.setKoodakFor(getContext(), holder.textViewProvince, holder.textViewTitle, holder.addFriend);
        holder.onlineImageView.setImageResource(R.drawable.circle_online);

        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendRequest request = new FriendRequest(friend.getId());
                DuelApp.getInstance().sendMessage(request.serialize(CommandType.SEND_ADD_FRIEND));
                clear();
                notifyDataSetChanged();
                if(progressBar != null)
                    progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    protected class ViewHolder {
        LinearLayout container;
        private ImageView imageViewAvatar;
        private TextView textViewTitle;
        private TextView textViewProvince;
        private ImageView onlineImageView;
        private Button addFriend;
    }
}
