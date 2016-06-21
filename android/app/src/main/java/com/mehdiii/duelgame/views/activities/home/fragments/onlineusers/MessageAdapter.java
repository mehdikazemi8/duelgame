package com.mehdiii.duelgame.views.activities.home.fragments.onlineusers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.chat.Message;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mehdiii on 6/13/16.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_message, null);

            ViewHolder holder = new ViewHolder();
            holder.messageText = (CustomTextView) convertView.findViewById(R.id.message_text);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.name = (CustomTextView) convertView.findViewById(R.id.name);

            convertView.setTag(holder);
        }

        initViews(position, (ViewHolder) convertView.getTag());

        return convertView;
    }

    private static String BACKEND = "backend";

    private void initViews(int position, ViewHolder holder) {
        holder.messageText.setText(getItem(position).getText());

        if(getItem(position).getSender().equals(BACKEND)) {
            holder.name.setText("@DuelKonkoor");
            Picasso.with(getContext()).load(R.drawable.ic_launcher).into(
                    holder.avatar
            );
        } else {
            holder.name.setText(getItem(position).getName());
            Picasso.with(getContext()).load(AvatarHelper.getResourceId(getContext(), getItem(position).getAvatar())).into(
                    holder.avatar
            );
        }
    }

    protected class ViewHolder {
        private CustomTextView messageText;
        private ImageView avatar;
        private CustomTextView name;

    }
}
