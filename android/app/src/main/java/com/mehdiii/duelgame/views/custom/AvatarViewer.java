package com.mehdiii.duelgame.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.mehdiii.duelgame.R;

/**
 * Created by omid on 4/6/2015.
 */
public class AvatarViewer extends ImageView {
    int position;

    public AvatarViewer(Context context) {
        super(context);
    }

    public AvatarViewer(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.AvatarViewer, 0, 0);
        this.position = arr.getInteger(R.styleable.AvatarViewer_position, 0);
        arr.recycle();
    }

    private void setDefaultAvatar() {
        setSelectedAvatar(this.position);
    }

    public void setSelectedAvatar(int position) {
//        setImageBitmap(AvatarHelper.getAvatar(getContext(), position));
//        Picasso.with(getContext()).load(R.drawable.av1).into(this);
    }

//    public void setPosition(int pos) {
//        this.position = pos;
//        setDefaultAvatar();
//    }

}
