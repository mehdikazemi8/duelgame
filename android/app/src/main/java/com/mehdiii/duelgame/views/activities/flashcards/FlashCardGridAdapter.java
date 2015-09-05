package com.mehdiii.duelgame.views.activities.flashcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.FlashCardList;
import com.mehdiii.duelgame.utils.FontHelper;

import java.util.List;

/**
 * Created by Omid on 7/22/2015.
 */
public class FlashCardGridAdapter extends ArrayAdapter<FlashCard> {
    LayoutInflater inflater = null;
    List<FlashCard> flashCards;

    public FlashCardGridAdapter(Context context, int resource, FlashCardList list) {
        super(context, resource);
        inflater = LayoutInflater.from(context);
        this.flashCards = list.getList();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.flash_card_gridview_item, null);
            ViewHolder holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.textView_title);
            FontHelper.setKoodakFor(getContext(), holder.textView);
            view.setTag(holder);
        } else view = convertView;

        ViewHolder placeHolder = (ViewHolder) view.getTag();

        if (placeHolder != null) {
            placeHolder.textView.setText(getItem(position).getTitle());
            placeHolder.data = getItem(position);
        }

        return view;
    }

    public class ViewHolder {
        TextView textView;
        FlashCard data;
    }

    @Override
    public void clear() {
        super.clear();
        this.flashCards.clear();
    }

    @Override
    public FlashCard getItem(int position) {
        return flashCards.get(position);
    }

    @Override
    public int getCount() {
        return flashCards.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
