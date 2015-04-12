package com.mehdiii.duelgame.views.activities.home.fragments.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.home.fragments.FlipableFragment;

/**
 * Created by omid on 4/5/2015.
 */
public class FriendsFragment extends FlipableFragment implements View.OnClickListener {
    //    TabPageIndicator indicator;
    ListView listView;
    private LinearLayout containerHeader;
    private TextView textViewCode;
    private Button buttonAddFriend;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        find(view);
        configure();
        bindViewData();
    }

    private void find(View view) {
        this.listView = (ListView) view.findViewById(R.id.listView_friends);
        containerHeader = (LinearLayout) view.findViewById(R.id.container_header);
        buttonAddFriend = (Button) view.findViewById(R.id.button_add_friend);
        textViewCode = (TextView) view.findViewById(R.id.textView_code);
    }

    private void configure() {
        buttonAddFriend.setOnClickListener(this);
        FontHelper.setKoodakFor(getActivity(), textViewCode, buttonAddFriend);
    }

    private void bindViewData() {
        User currentUser = AuthManager.getCurrentUser();
        textViewCode.setText("کد شما" + currentUser.getId());
        getFriendListing();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_friend:
                break;
        }
    }

    private void getFriendListing() {
        User user = AuthManager.getCurrentUser();
//        wsc.sendTextMessage(query.toString());
    }
}
