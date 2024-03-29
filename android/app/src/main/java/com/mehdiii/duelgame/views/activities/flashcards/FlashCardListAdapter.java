package com.mehdiii.duelgame.views.activities.flashcards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.DateManager;
import com.mehdiii.duelgame.managers.FlashCardIdManager;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.Card;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.FlashCardList;
import com.mehdiii.duelgame.models.FlashCardSetting;
import com.mehdiii.duelgame.models.FlashCardsSettings;
import com.mehdiii.duelgame.utils.DeckManager;
import com.mehdiii.duelgame.utils.DeckPersister;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.custom.IconTextView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by frshd on 4/27/16.
 */
public class FlashCardListAdapter extends ArrayAdapter<FlashCard> {

    LayoutInflater inflater = null;
    List<FlashCard> flashCards;
    int totalProgress = 0;

    public FlashCardListAdapter(Context context, int resource, FlashCardList list) {
        super(context, resource);
        inflater = LayoutInflater.from(context);
        this.flashCards = list.getList();

        for(FlashCard flashCard : flashCards) {
            Log.d("TAG", "xxx 1 " + flashCard.getOwned());
        }
        Collections.sort(flashCards, new Comparator<FlashCard>() {
            @Override
            public int compare(FlashCard x, FlashCard y) {
                if(x.getOwned() != y.getOwned()) {
                    return x.getOwned() > y.getOwned() ? -1 : 1;
                } else {
                    return x.getTitle().compareTo(y.getTitle());
                }
            }
        });
        for(FlashCard flashCard : flashCards) {
            Log.d("TAG", "xxx 2 " + flashCard.getOwned());
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.template_deck_progress, null);
            ViewHolder holder = new ViewHolder();
            holder.deckTitle = (CustomTextView) convertView.findViewById(R.id.deck_name);
            holder.deckAlert = (CustomTextView) convertView.findViewById(R.id.deck_alert);
            holder.alertIcon = (IconTextView) convertView.findViewById(R.id.alert_icon);
            holder.circleProgress = (CircleProgress) convertView.findViewById(R.id.circle_progress);
            FontHelper.setKoodakFor(getContext(), holder.deckAlert, holder.deckTitle);
            convertView.setTag(holder);
        }

        ViewHolder placeHolder = (ViewHolder) convertView.getTag();

        if (placeHolder != null) {
            placeHolder.deckTitle.setText(getItem(position).getTitle());
            placeHolder.data = getItem(position);
            DeckPersister deckPersister = new DeckPersister();
            if(deckPersister.hasDeck(getContext(), getItem(position).getId())){
                FlashCard flashCard = deckPersister.getDeck(getContext(), getItem(position).getId());
                calculateProgress(flashCard);
                circleProgressSet(placeHolder);
                setDailyGoal(flashCard, deckPersister);
                setDeckAlert(flashCard, deckPersister, placeHolder);
                FlashCardIdManager.saveFlashCardId(getContext(), flashCard);
            } else {
                placeHolder.deckAlert.setText("شروع نکرده‌اید");
                placeHolder.alertIcon.setText("V");
                placeHolder.alertIcon.setTextColor(getContext().getResources().getColor(R.color.blue_dark));
                placeHolder.circleProgress.setUnfinishedColor(getContext().getResources().getColor(R.color.gray_light));
                placeHolder.circleProgress.setFinishedColor(getContext().getResources().getColor(R.color.gray_light));
                placeHolder.circleProgress.setTextSize(getContext().getResources().getDimension(R.dimen.progress_font_size_small));
                placeHolder.circleProgress.setProgress(0);
            }
        }
        return convertView;
    }

    private void setDeckAlert(FlashCard flashCard, DeckPersister deckPersister, ViewHolder placeHolder) {

        int remain = getDailyCount(flashCard.getId())-flashCard.getDailyCount();
        placeHolder.deckAlert.setText(String.format("%d کلمه از %d", remain, getDailyCount(flashCard.getId())) + "باقی مانده");
        placeHolder.alertIcon.setText("B");
        placeHolder.alertIcon.setTextColor(getContext().getResources().getColor(R.color.red));
        if(remain<=0){
            placeHolder.deckAlert.setText(String.format("امروز  %d کلمه را مرور کرده‌اید.", flashCard.getDailyCount()));
            placeHolder.alertIcon.setText("A");
            placeHolder.alertIcon.setTextColor(getContext().getResources().getColor(R.color.green));
        }
        Log.d("TAG", "daily: " + flashCard.getDailyCount());
    }

    public void calculateProgress(FlashCard flashCard){
        DeckManager deckManager = new DeckManager(flashCard, flashCard.getSeen(), flashCard.getId());
        Map<Integer, Queue<Card>> deckStatistics = deckManager.getGroups();
        int total = 0;
        int hadSeen = 0;
        for (int i=0 ; i < deckStatistics.size() ; i++)
        {
            Queue<Card> cardQueue = deckStatistics.get(i);
            total += cardQueue.size();
            hadSeen += cardQueue.size()*i;
        }
        totalProgress = (hadSeen*20/total);
    }

    public void circleProgressSet(ViewHolder placeHolder){
        CircleProgress circleProgress = placeHolder.circleProgress;
        circleProgress.setFinishedColor(getContext().getResources().getColor(R.color.purple_sexy));
        circleProgress.setUnfinishedColor(getContext().getResources().getColor(R.color.purple_sexy_trans));
        circleProgress.setProgress(totalProgress);
        circleProgress.setTextColor(getContext().getResources().getColor(R.color.white));
        circleProgress.setTextSize(getContext().getResources().getDimension(R.dimen.progress_font_size_large));
    }

    public void setDailyGoal(FlashCard flashCard, DeckPersister deckPersister){
        Calendar calendar = Calendar.getInstance();
        if( flashCard.getLastDay()!=null){
            if(!DateManager.isToday(flashCard.getLastDay())){
                flashCard.setLastDay(calendar);
                flashCard.setDailyCount(0);
                deckPersister.saveDeck(getContext(), flashCard);
            }
        } else {
            flashCard.setLastDay(calendar);
            flashCard.setDailyCount(0);
            deckPersister.saveDeck(getContext(), flashCard);
        }
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

    public int getDailyCount(String cardId){

        String json = GlobalPreferenceManager.readString(getContext(), "FlashCardSettings", "");
        if (json == null || json.isEmpty()) {

            return 20;

        } else {

            FlashCardsSettings flashCardsSettings = FlashCardsSettings.deserialize(json, FlashCardsSettings.class);
            List<FlashCardSetting> oldFlashCardSettingList= flashCardsSettings.getFlashCardSettings();

            for (int i=0 ; i<oldFlashCardSettingList.size() ; i++) {

                if(oldFlashCardSettingList.get(i).getCardId().equals(cardId)) {

                    return oldFlashCardSettingList.get(i).getDailyGoal();

                }
            }

            return 20;

        }
    }

    public class ViewHolder {
        CustomTextView deckTitle;
        CustomTextView deckAlert;
        IconTextView alertIcon;
        CircleProgress circleProgress;
        FlashCard data;
    }
}
