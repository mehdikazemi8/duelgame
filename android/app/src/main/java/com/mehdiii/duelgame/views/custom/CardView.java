package com.mehdiii.duelgame.views.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.animations.FlipAnimation;
import com.mehdiii.duelgame.models.Card;

/**
 * Created by Omid on 7/22/2015.
 */
public class CardView extends RelativeLayout {
    public CardView(Context context) {
        super(context);
    }

    LayoutInflater inflater;
    View front;
    View back;
    View rootLayout;
    TextView frontTextView;
    TextView backTextView;
    TextView weightTextView;

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.template_card_view, this, true);

        front = findViewById(R.id.card_face);
        back = findViewById(R.id.card_back);
        frontTextView = (TextView) findViewById(R.id.textView_front);
        backTextView = (TextView) findViewById(R.id.textView_back);
        weightTextView = (TextView) findViewById(R.id.textView_weight);
        rootLayout = findViewById(R.id.main_activity_root);

        front.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
    }

    boolean isBound = false;

    public void bind(Card card) {
        isBound = true;
        this.frontTextView.setText(card.getFront());
        this.backTextView.setText(card.getBack());
        this.weightTextView.setText(String.valueOf(card.getWeight()));
    }

    public boolean isBound() {
        return isBound;
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            FlipAnimation flipAnimation = new FlipAnimation(front, back);

            if (front.getVisibility() == View.GONE)
                flipAnimation.reverse();

            rootLayout.startAnimation(flipAnimation);
        }
    };

    public void swipeRight() {
        FlipAnimation flipAnimation = new FlipAnimation(front, back);

        if (front.getVisibility() == View.GONE)
            flipAnimation.reverse();

        rootLayout.startAnimation(flipAnimation);
    }

    public void swipeLeft() {
        FlipAnimation flipAnimation = new FlipAnimation(front, back);
        rootLayout.startAnimation(flipAnimation);
    }

    public void reset() {
        isBound = false;
        front.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
    }


}
