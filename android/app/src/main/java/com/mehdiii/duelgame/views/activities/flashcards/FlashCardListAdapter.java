package com.mehdiii.duelgame.views.activities.flashcards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.android.gms.maps.model.Circle;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.DateManager;
import com.mehdiii.duelgame.models.Card;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.FlashCardList;
import com.mehdiii.duelgame.utils.DeckManager;
import com.mehdiii.duelgame.utils.DeckPersister;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.custom.IconTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by frshd on 4/27/16.
 */
public class FlashCardListAdapter extends ArrayAdapter<FlashCard> {

    LayoutInflater inflater = null;
    List<FlashCard> flashCards;

    public FlashCardListAdapter(Context context, int resource, FlashCardList list) {
        super(context, resource);
        inflater = LayoutInflater.from(context);
        this.flashCards = list.getList();
        }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.template_deck_progress, null);
            ViewHolder holder = new ViewHolder();
            holder.deckTitle = (CustomTextView) view.findViewById(R.id.deck_name);
            holder.deckAlert = (CustomTextView) view.findViewById(R.id.deck_alert);
            holder.alertIcon = (IconTextView) view.findViewById(R.id.alert_icon);
            holder.circleProgress = (CircleProgress) view.findViewById(R.id.circle_progress);
            FontHelper.setKoodakFor(getContext(), holder.deckAlert, holder.deckTitle);
            view.setTag(holder);
        } else view = convertView;

        ViewHolder placeHolder = (ViewHolder) view.getTag();

        if (placeHolder != null) {
            placeHolder.deckTitle.setText(getItem(position).getTitle());

            placeHolder.data = getItem(position);

            DeckPersister dp = new DeckPersister();
            if(dp.hasDeck(getContext(), getItem(position).getId())){
                FlashCard fc = dp.getDeck(getContext(), getItem(position).getId());
            if(fc.getDailyFlashCardStatistics()!= null){

                for(int i = 0 ; i<fc.getDailyFlashCardStatistics().size(); i++ ){
                    Log.d("TAG","salammm " + fc.getDailyFlashCardStatistics().get(i).getNumber()+ fc.getDailyFlashCardStatistics().get(i).getDate());
                }
            }

                Log.d("TAG", "fc: " +fc.serialize());
                DeckManager deckManager = new DeckManager(fc, fc.getSeen(), fc.getId());
                Map<Integer, Queue<Card>> deckStatistics = deckManager.getGroups();
                int total = 0;
                int hadSeen = 0;



                for (int i=0 ; i < deckStatistics.size() ; i++)
                {
                    Log.d("TAG", "deck:" + deckStatistics.get(i));

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
                if( fc.getLastDay()!=null){
                    if(!DateManager.isToday(fc.getLastDay())){
                        fc.setLastDay(calendar);
                        fc.setDailyCount(20);
                        dp.saveDeck(getContext(), fc);
                    }
                }else {
                    fc.setLastDay(calendar);
                    fc.setDailyCount(20);
                    dp.saveDeck(getContext(), fc);
                }

                placeHolder.deckAlert.setText(String.format("%d کلمه از %d", fc.getDailyCount(), 20)+"باقی مانده");
                placeHolder.alertIcon.setText("B");
                placeHolder.alertIcon.setTextColor(getContext().getResources().getColor(R.color.red));
                if(fc.getDailyCount()==0){
                    placeHolder.deckAlert.setText(String.format("امروز  %d کلمه را مرور کرده‌اید.", 20));
                    placeHolder.alertIcon.setText("A");
                    placeHolder.alertIcon.setTextColor(getContext().getResources().getColor(R.color.green));
                }
                CircleProgress circleProgress = placeHolder.circleProgress;
                circleProgress.setFinishedColor(getContext().getResources().getColor(R.color.purple_sexy));
                circleProgress.setUnfinishedColor(getContext().getResources().getColor(R.color.purple_sexy_trans));
                circleProgress.setProgress(totalProgress);
                circleProgress.setTextColor(getContext().getResources().getColor(R.color.white));

                circleProgress.setTextSize(getContext().getResources().getDimension(R.dimen.progress_font_size));
                Log.d("TAG", "daily: " + fc.getDailyCount());
            }else {
                placeHolder.deckAlert.setText("شروع نکرده‌اید");
            }

        }

        return view;
    }

    public class ViewHolder {
        CustomTextView deckTitle;
        CustomTextView deckAlert;
        IconTextView alertIcon;
        CircleProgress circleProgress;
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
