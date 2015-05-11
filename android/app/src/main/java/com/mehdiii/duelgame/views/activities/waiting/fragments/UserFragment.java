package com.mehdiii.duelgame.views.activities.waiting.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;

/**
 * Created by MeHdi on 5/11/2015.
 */
public class UserFragment extends Fragment {

    private TextView titleTextView;
    private TextView nameTextView;
    private TextView provinceTextView;
    private ImageView avatarImageView;
    private User user = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getArguments().getInt("layout");
        user = User.deserialize(getArguments().getString("user"), User.class);

        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        FontHelper.setKoodakFor(getActivity(), titleTextView, nameTextView, provinceTextView);
        configure();
    }

    private void configure() {
        nameTextView.setText(user.getName());
        avatarImageView.setImageResource(AvatarHelper.getResourceId(getActivity(), user.getAvatar()));
        provinceTextView.setText(ProvinceManager.get(getActivity(), user.getProvince()));
        titleTextView.setText(ScoreHelper.getTitle(user.getScore()));
    }

    private void find(View view) {
        titleTextView = (TextView) view.findViewById(R.id.waiting_title);
        nameTextView = (TextView) view.findViewById(R.id.waiting_name);
        provinceTextView = (TextView) view.findViewById(R.id.waiting_province);
        avatarImageView = (ImageView) view.findViewById(R.id.waiting_avatar);
    }
}
