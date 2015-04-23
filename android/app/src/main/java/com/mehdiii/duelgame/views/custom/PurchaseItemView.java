package com.mehdiii.duelgame.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.PurchaseItem;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by omid on 4/23/2015.
 */
public class PurchaseItemView extends LinearLayout {
    LayoutInflater inflater;

    public PurchaseItemView(Context context) {
        super(context);
    }

    TextView textView;
    TextView priceTextView;
    ImageView imageView;
    int type;
    String caption;
    String price;
    PurchaseItem tag;

    public PurchaseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_purchase_item, this, true);

        textView = (TextView) findViewById(R.id.textView);
        priceTextView = (TextView) findViewById(R.id.textView_price);
        imageView = (ImageView) findViewById(R.id.imageView);

        FontHelper.setKoodakFor(context, priceTextView, textView);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.PurchaseItemView, 0, 0);
            type = a.getInt(R.styleable.PurchaseItemView_type, 0);
            caption = a.getString(R.styleable.PurchaseItemView_caption);

            a.recycle();
        }

        bindViewData();
    }

    private void bindViewData() {
        setType(type);
        setCaption(caption);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        int resourceId = R.drawable.heart_blank;
        switch (type) {
            case 1:
                resourceId = R.drawable.store_heart;
                break;
            case 2:
                resourceId = R.drawable.store_diamond;
                break;
            case 3:
                resourceId = R.drawable.store_point;
                break;
        }
        if (imageView != null)
            imageView.setImageResource(resourceId);
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        if (textView != null)
            textView.setText(caption);
        this.caption = caption;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        if (priceTextView != null)
            priceTextView.setText(price);
        this.price = price;
    }

    @Override
    public PurchaseItem getTag() {
        return tag;
    }

    public void setTag(PurchaseItem tag) {
        this.tag = tag;
    }
}
