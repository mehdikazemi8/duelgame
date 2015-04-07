package com.mehdiii.duelgame.views.activities.register.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.views.custom.AvatarViewer;
import com.squareup.picasso.Picasso;

/**
 * Created by omid on 4/6/2015.
 */
public class AvatarWaveFragment extends Fragment {

    public static String ARGS_START_INDEX = "ARGS_START_INDEX";

    LinearLayout container;
    ProgressBar progressBar;

    int screenW;
    int screenH;
    int rows = 3, columns = 2;

    int startIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_avatar_wave, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = (LinearLayout) view.findViewById(R.id.container_avatars);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        readArgs();
        loadImages();
    }

    private void readArgs() {
        Bundle bundle = getArguments();
        startIndex = bundle.getInt(ARGS_START_INDEX, 0);
    }


    private void loadImages() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
        int avatarHeight = screenH / 5;

        for (int i = 0; i < rows; i++) {
            LinearLayout row = new LinearLayout(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 20, 0, 20);
            row.setLayoutParams(params);
            row.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < columns; j++) {
                AvatarViewer viewer = new AvatarViewer(getActivity());
                viewer.setLayoutParams(new TableLayout.LayoutParams(0, avatarHeight, 1f));
                Picasso.with(getActivity()).load(AvatarHelper.getAvatarResource(getActivity(), startIndex * (rows * columns) + i * 3 + j + 1)).resize(screenW / 2, avatarHeight).centerInside().into(viewer);

                row.addView(viewer);
            }

            container.addView(row);
        }
    }
}
