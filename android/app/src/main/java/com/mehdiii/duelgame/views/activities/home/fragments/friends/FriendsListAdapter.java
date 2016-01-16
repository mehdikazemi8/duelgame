package com.mehdiii.duelgame.views.activities.home.fragments.friends;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.dialogs.HeartLowDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendsListAdapter extends ArrayAdapter<Friend> {
    public interface OnUserDecisionIsMade {
        void onDuel(Friend request);

        void onApprove(Friend request);

        void onReject(Friend request);

        void onSelect(Friend friend);
    }

    private OnUserDecisionIsMade onUserDecisionIsMade;

    List<Friend> friends;
    private LayoutInflater layoutInflater;

    public FriendsListAdapter(Context context, int resource, List<Friend> friends) {
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
    public void clear() {
        super.clear();
        friends.clear();
    }

    @Override
    public Friend getItem(int position) {
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
            viewHolder.imageViewAvatar = (ImageView) convertView.findViewById(R.id.imageView_avatar);
            viewHolder.buttonPositive = (Button) convertView.findViewById(R.id.button_positive);
            viewHolder.buttonNegative = (Button) convertView.findViewById(R.id.button_negative);
            viewHolder.textViewStatus = (TextView) convertView.findViewById(R.id.textView_status);
            viewHolder.duelButton = (Button) convertView.findViewById(R.id.button_duel);
            viewHolder.onlineImageView = (ImageView) convertView.findViewById(R.id.imageView_online);
            viewHolder.container = (LinearLayout) convertView.findViewById(R.id.container_wrapper);

//            viewHolder.textViewOnline = (TextView) convertView.findViewById(R.id.textView_online);

            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final Friend friend, ViewHolder holder) {
        Picasso.with(getContext()).load(AvatarHelper.getResourceId(getContext(), friend.getAvatar())).into(holder.imageViewAvatar);
        holder.textViewTitle.setText(friend.getName());
        holder.textViewProvince.setText(ProvinceManager.get(getContext(), friend.getProvince()));
        // title is removed
//        holder.textViewLevel.setText(ScoreHelper.getTitle(friend.getScore()) + " از ");
        holder.imageViewAvatar.setImageResource(AvatarHelper.getResourceId(getContext(), friend.getAvatar()));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUserDecisionIsMade != null)
                    onUserDecisionIsMade.onSelect(friend);
            }
        });
        FontHelper.setKoodakFor(getContext(), holder.textViewLevel, holder.textViewProvince, holder.textViewTitle, holder.duelButton/*, holder.textViewOnline*/);

        holder.buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (friend.getStatus().equals("friend")) {
                    if (onUserDecisionIsMade != null)
                        onUserDecisionIsMade.onDuel(friend);
                } else */
                if (friend.getStatus().equals("request")) {
                    if (onUserDecisionIsMade != null)
                        onUserDecisionIsMade.onApprove(friend);
                }
            }
        });

        holder.duelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "duelButton");
                if (!HeartTracker.getInstance().canUseHeart()) {
                    HeartLowDialog dialog = new HeartLowDialog(getContext());
                    dialog.show();
                    return;
                }

                if (onUserDecisionIsMade != null) {
                    onUserDecisionIsMade.onDuel(friend);
                }
            }
        });

        holder.buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friend.getStatus().equals("request")) {
                    if (onUserDecisionIsMade != null)
                        onUserDecisionIsMade.onReject(friend);
                }
            }
        });

        holder.buttonPositive.setVisibility(View.GONE);
        holder.buttonNegative.setVisibility(View.GONE);
        holder.textViewStatus.setVisibility(View.GONE);
        holder.duelButton.setVisibility(View.GONE);

        if(friend.isBusy()) {
            holder.onlineImageView.setImageResource(R.drawable.circle_busy);
        } else if (friend.isOnline()) {
            holder.onlineImageView.setImageResource(R.drawable.circle_online);
        } else {
            holder.onlineImageView.setImageResource(R.drawable.circle_offline);
        }

        if (friend.getStatus().equals("pending")) {
            holder.textViewStatus.setVisibility(View.VISIBLE);
            holder.textViewStatus.setText("منتظر پاسخ");

            holder.buttonPositive.setTextColor(getContext().getResources().getColor(R.color.black));
            holder.buttonNegative.setTextColor(getContext().getResources().getColor(R.color.black));
        } else if (friend.getStatus().equals("friend")) {

            holder.duelButton.setVisibility(View.VISIBLE);
            holder.buttonPositive.setTypeface(FontHelper.getKoodak(getContext()));

            holder.buttonPositive.setTextColor(getContext().getResources().getColor(R.color.black));
            holder.buttonNegative.setTextColor(getContext().getResources().getColor(R.color.black));

        } else if (friend.getStatus().equals("request")) {

            holder.buttonPositive.setVisibility(View.VISIBLE);
            holder.buttonNegative.setVisibility(View.VISIBLE);

            holder.buttonPositive.setText("A");
            holder.buttonNegative.setText("B");

            Typeface icons = FontHelper.getIcons(getContext());
            holder.buttonNegative.setTypeface(icons);
            holder.buttonPositive.setTypeface(icons);

            holder.buttonPositive.setTextColor(getContext().getResources().getColor(R.color.green));
            holder.buttonNegative.setTextColor(getContext().getResources().getColor(R.color.red));
        }
    }

    public OnUserDecisionIsMade getOnUserDecisionIsMade() {
        return onUserDecisionIsMade;
    }

    public void setOnUserDecisionIsMade(OnUserDecisionIsMade onUserDecisionIsMade) {
        this.onUserDecisionIsMade = onUserDecisionIsMade;
    }

    protected class ViewHolder {
        LinearLayout container;
        private ImageView imageViewAvatar;
        private TextView textViewTitle;
        private TextView textViewLevel;
        private TextView textViewProvince;
        private TextView textViewStatus;
        private Button buttonPositive;
        private Button buttonNegative;
        private Button duelButton;
        private ImageView onlineImageView;
//        private TextView textViewOnline;
    }

}
