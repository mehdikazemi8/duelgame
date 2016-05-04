package com.mehdiii.duelgame.views.activities.flashcards.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.DateManager;
import com.mehdiii.duelgame.models.DailyFlashCardStatistics;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.utils.DeckPersister;
import com.mehdiii.duelgame.utils.FontHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by frshd on 5/1/16.
 */
public class FlashCardStatisticsFragment extends Fragment {

    public static final String BUNDLE_DECK_ID = "bundle_deck_id";
    String cardId = "";

    int screenW;
    int screenH;

    private BarChart mChart2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readArguments();

        mChart2 = (BarChart) view.findViewById(R.id.chart2);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
        mChart2.setLayoutParams(new LinearLayout.LayoutParams((int) (screenW), (int) (screenW)));
        mChart2.setDescription("آمار عملکرد در روزهای مختلف");
        mChart2.setDescriptionPosition(300, 20);
        mChart2.setDescriptionTypeface(FontHelper.getKoodak(getActivity()));
        mChart2.setMaxVisibleValueCount(60);
        mChart2.setPinchZoom(false);
        mChart2.setDrawBarShadow(false);
        mChart2.setDrawGridBackground(false);
        XAxis xAxis = mChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);
        mChart2.getAxisLeft().setDrawGridLines(false);
        mChart2.animateY(2500);
        mChart2.getLegend().setEnabled(false);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        DeckPersister dp = new DeckPersister();
        if (dp.hasDeck(getActivity(), cardId)) {
            FlashCard fc = dp.getDeck(getActivity(), cardId);
            List<DailyFlashCardStatistics> dailyFlashCardStatisticses = fc.getDailyFlashCardStatistics();
            if (dailyFlashCardStatisticses!=null) {
                Calendar currentWeekBegin = Calendar.getInstance();
                int toDayInt = currentWeekBegin.get(Calendar.DAY_OF_WEEK);
                if (toDayInt != 7)
                    currentWeekBegin.add(Calendar.DATE, -toDayInt);
                Log.d("TAG", "today int:" + toDayInt + "curr - today:" + currentWeekBegin.get(Calendar.DAY_OF_WEEK));
                for (int i = 0; i <= toDayInt; i++) {
                    Log.d("TAG", "curr + i:" + currentWeekBegin.get(Calendar.DAY_OF_WEEK));
                    for (DailyFlashCardStatistics day : dailyFlashCardStatisticses) {
                        if (DateManager.isSameDay(currentWeekBegin, day.getDate())) {
                            yVals1.add(new BarEntry(day.getNumber(), i));
                            break;
                        }
                    }
                    currentWeekBegin.add(Calendar.DATE, 1);
                }
            }else{
                mChart2.setNoDataText("هنوز آماری وجود ندارد.");
                return;}
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            if (i == 0)
                xVals.add("شنبه");
            else if (i == 6)
                xVals.add("جمعه");
            else
                xVals.add(i + "شنبه");
        }
        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "Data Set");

        int[] colors = new int[7];
        for (int i = 0; i < yVals1.size() - 1; i++)
            colors[i] = getActivity().getResources().getColor(R.color.purple);
        colors[yVals1.size() - 1] = getActivity().getResources().getColor(R.color.green_light);
        set1.setColors(colors);

        set1.setDrawValues(false);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(xVals, dataSets);
        data.setValueTypeface(FontHelper.getKoodak(getActivity()));
        mChart2.setData(data);
        mChart2.invalidate();
    }

    private void readArguments() {
        Bundle bundle = getArguments();
        if (bundle == null)
            return;
        cardId = bundle.getString(BUNDLE_DECK_ID);
    }
    //          INITIAL FAKE DATA FOR DAILY STATS
//            dailyFlashCardStatisticses.clear();
//            fc.setDailyFlashCardStatistics(dailyFlashCardStatisticses);
//            DailyFlashCardStatistics dfc = new DailyFlashCardStatistics();
//            for(int i=30; i>0; i--){
//                dfc = new DailyFlashCardStatistics();
//                Calendar cc = Calendar.getInstance();
//                cc.add(Calendar.DATE, -i);
//                dfc.setDate(cc);
//                dfc.setNumber((10 + i) % 7 + 1);
//                fc.addStat(dfc);
//                dp.saveDeck(getActivity(), fc);
//            }
}
