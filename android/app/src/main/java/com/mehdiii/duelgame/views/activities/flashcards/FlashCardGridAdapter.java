package com.mehdiii.duelgame.views.activities.flashcards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Card;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.FlashCardList;
import com.mehdiii.duelgame.utils.DeckManager;
import com.mehdiii.duelgame.utils.DeckPersister;
import com.mehdiii.duelgame.utils.FontHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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

            DeckPersister dp = new DeckPersister();
            if(dp.hasDeck(getContext(), getItem(position).getId())){
                FlashCard fc = dp.getDeck(getContext(), getItem(position).getId());
                DeckManager deckManager = new DeckManager(fc, fc.getSeen(), fc.getId());
                Map<Integer, Queue<Card>> deckStatistics = deckManager.getGroups();
                int total = 0;
                int hadSeen = 0;

                for (int i=0 ; i < deckStatistics.size() ; i++)
                {
                    Log.d("TAG","deck:" + deckStatistics.get(i));

                    Queue<Card> c = deckStatistics.get(i);
                    total += c.size();

                    if(i!=0)
                    {
                        hadSeen += c.size();
                    }
                }
                int totalProgress = (int)(((float)hadSeen/total)*100);
                Log.d("TAG", "percentage: " + totalProgress);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(calendar.getTime());
                if( fc.getLastDay()!=null){
                    if(fc.getLastDay().before(calendar.getTime())){
                        fc.setLastDay(calendar);
                        fc.setDailyCount(20);
                        dp.saveDeck(getContext(), fc);
                    }
                }else {
                    fc.setLastDay(calendar);
                    fc.setDailyCount(20);
                    dp.saveDeck(getContext(), fc);
                }
                Log.d("TAG", "daily: " + fc.getDailyCount());
            }
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
