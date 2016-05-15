package com.mehdiii.duelgame.views.activities.flashcards.fragments;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.Card;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.PurchaseDone;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnFlashCardReceived;
import com.mehdiii.duelgame.utils.DeckManager;
import com.mehdiii.duelgame.utils.DeckPersister;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.flashcards.FlashCardActivity;

import de.greenrobot.event.EventBus;
import com.github.mikephil.charting.charts.PieChart;
import com.mehdiii.duelgame.views.dialogs.FlashCardSettingsDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Omid on 7/22/2015.
 */
public class OverviewFragment extends Fragment implements View.OnClickListener {
    public final static String BUNDLE_PARAM_FLASH_CARD = "bundle_param_flashcard";

    FlashCard card = null;

    int screenW;
    int screenH;

    TextView ownedTextView;
    TextView titleTextView;
    TextView priceTextView;
    TextView countTextView;
    Button goButton;
    Button purchaseButton;
    Button settingButton;
    Button statisticsButton;
    ProgressBar progressBar;
    private PieChart mChart;

    protected String[] mParties = new String[] {
            "دیده نشده", "۱بار دیده شده", "۲بار دیده شده", "۳بار دیده شده", "۴بار دیده شده", "۵بار دیده شده"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard_overview, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readArguments();

        // find controls
        ownedTextView = (TextView) view.findViewById(R.id.textView_owned);
        titleTextView = (TextView) view.findViewById(R.id.textView_title);
        priceTextView = (TextView) view.findViewById(R.id.textView_price);
        countTextView = (TextView) view.findViewById(R.id.textView_count);
        goButton = (Button) view.findViewById(R.id.button_go);
        purchaseButton = (Button) view.findViewById(R.id.button_purchase);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        statisticsButton = (Button) view.findViewById(R.id.button_statistics);
        settingButton = (Button) view.findViewById(R.id.button_settings);
        mChart = (PieChart) view.findViewById(R.id.chart);
        // configure controls
        goButton.setOnClickListener(this);
        purchaseButton.setOnClickListener(this);
        statisticsButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        FontHelper.setKoodakFor(getActivity(), ownedTextView, titleTextView, priceTextView, countTextView,
                goButton, purchaseButton, statisticsButton, settingButton);

        bindData();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;

        DeckPersister dp = new DeckPersister();
        if(dp.hasDeck(getActivity(), card.getId())) {
            configChart();
        } else {
            mChart.setVisibility(View.GONE);
        }
    }

    private void readArguments() {
        Bundle bundle = getArguments();
        if (bundle == null)
            return;
        card = FlashCard.deserialize(bundle.getString(BUNDLE_PARAM_FLASH_CARD), FlashCard.class);
    }

    private void bindData() {
        Log.d("TAG", "bindData OverviewFragment");

        if (card.getOwned() == 1)
            purchaseButton.setVisibility(View.GONE);

        this.ownedTextView.setText(card.getOwned() == 1 ? "OWNED" : "NOT OWNED");
        this.priceTextView.setText(String.valueOf((int) card.getPrice()) + " تومان");
        this.countTextView.setText(String.valueOf((int) card.getCount()) + " عدد فلش‌کارت");
        this.titleTextView.setText(card.getTitle());

        String buttonText = "بزن بریم";
        if (!DeckPersister.hasDeck(getActivity(), card.getId()))
            if (card.getOwned() == 1)
                buttonText = "دریافت";
            else {
                buttonText = "دریافت و امتحان مجانی";
                goButton.setPadding(10, 0, 10, 0);
            }
        else if (card.getOwned() == 1)
            buttonText = "بزن بریم";
        else if (card.getProgress() < card.getPercentFree())
            buttonText = "امتحان کنید";

        goButton.setText(buttonText);

        if(card.getOwned() != 1 || !DeckPersister.hasDeck(getActivity(), card.getId())) {
            settingButton.setEnabled(false);
            statisticsButton.setEnabled(false);
        }

        if(card.getOwned() == 1) {
            goButton.setEnabled(true);
        } else {
            if(card.getProgress() >= card.getPercentFree()) {
                goButton.setEnabled(false);
            } else {
                goButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_go:
                if (DeckPersister.hasDeck(getActivity(), card.getId()))
                    startPracticeIfPossible();
                else
                    startDownloadingDeck();
                break;
            case R.id.button_purchase:
                startPurchase();
                break;
            case R.id.button_statistics:
                getStatistics();
                break;
            case R.id.button_settings:
                openSettings();
                break;
        }
    }

    private void openSettings() {
        DeckPersister dp = new DeckPersister();
        int totalRemain = 0;
        if(dp.hasDeck(getActivity(), card.getId())){
            FlashCard fc = dp.getDeck(getActivity(), card.getId());
            DeckManager deckManager = new DeckManager(fc, fc.getSeen(), fc.getId());
            Map<Integer, Queue<Card>> deckStatistics = deckManager.getGroups();
            for (int i=0 ; i < deckStatistics.size() ; i++)
            {
                Queue<Card> c = deckStatistics.get(i);
                if(i!=5)
                    totalRemain += c.size();
            }
            FlashCardSettingsDialog flashCardSettingsDialog = new FlashCardSettingsDialog(getActivity(), totalRemain, fc);
            flashCardSettingsDialog.show();
        }

    }

    private void getStatistics() {
        Bundle bundle = new Bundle();
        bundle.putString(FlashCardStatisticsFragment.BUNDLE_DECK_ID, card.getId());
        Fragment fragment = Fragment.instantiate(getActivity(), FlashCardStatisticsFragment.class.getName(), bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom, R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom)
                .add(R.id.frame_wrapper, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void startPracticeIfPossible() {
        Log.d("TAG", "startPracticeIfPossible " + card.getProgress() + " " + card.getPercentFree());

        if (card.getProgress() < card.getPercentFree() || card.getOwned() == 1) {
            // open practice fragment
            Bundle bundle = new Bundle();
            bundle.putString(PracticeFragment.BUNDLE_DECK_ID, card.getId());
            Fragment fragment = Fragment.instantiate(getActivity(), PracticeFragment.class.getName(), bundle);
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom, R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom)
                    .add(R.id.frame_wrapper, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            DuelApp.getInstance().toast(R.string.message_buy_flashcard, Toast.LENGTH_LONG);
        }
    }

    private void startDownloadingDeck() {
        this.progressBar.setVisibility(View.VISIBLE);
        turnWaitingMode(true);
        DuelApp.getInstance().sendMessage(card.serialize(CommandType.SEND_GET_FLASH_CARD_REQUEST));
    }

    /**
     * on flash card received
     *
     * @param c receive notice
     */
    public void onEvent(OnFlashCardReceived c) {
        Log.d("TAG", "onEvent OverviewFragment");
        progressBar.setVisibility(View.GONE);
        turnWaitingMode(false);
        card = DeckPersister.getDeck(getActivity(), card.getId());
        bindData();
    }

    public void onEvent(PurchaseDone result) {
        // display success/fail message
        if (result.getPurchaseResult() == PurchaseDone.PurchaseResult.COMPLETED)
            DuelApp.getInstance().toast(R.string.message_purchase_successful, Toast.LENGTH_SHORT);
        else
            DuelApp.getInstance().toast(R.string.message_purchase_failed, Toast.LENGTH_SHORT);

        // remove this fragment and let it the user choose it again.
        // just an easy way for reloading this at easiest way possible.
        getFragmentManager().beginTransaction().remove(this).commit();

        // tell its mother activity to load again.
        ((FlashCardActivity) getActivity()).reloadAsync();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    private void startPurchase() {
        PurchaseManager.getInstance().startPurchase(9, card.getId());
    }

    private void turnWaitingMode(boolean waiting) {
        goButton.setEnabled(!waiting);
        purchaseButton.setEnabled(!waiting);

        Log.d("TAG", "turnWaitingMode " + (!waiting));
    }

    private void setData() {
        Map<Integer, Integer> hadSeen = new HashMap<Integer,Integer>();
        DeckPersister dp = new DeckPersister();
        int total = 0;
        if(dp.hasDeck(getActivity(), card.getId())){
            FlashCard fc = dp.getDeck(getActivity(), card.getId());
            DeckManager deckManager = new DeckManager(fc, fc.getSeen(), fc.getId());
            Map<Integer, Queue<Card>> deckStatistics = deckManager.getGroups();
            for (int i=0 ; i < deckStatistics.size() ; i++)
            {
                Queue<Card> c = deckStatistics.get(i);
                total += c.size();
                if(hadSeen.get(i)!=null){
                    hadSeen.put(i, hadSeen.get(i)+c.size());
                }else {
                    hadSeen.put(i, c.size());
                }
            }
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < 6; i++) {
            int number = hadSeen.get(i);
            float percent = ((float) number / total)*100;
            if (percent!=0)
                yVals1.add(new Entry(percent, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < 6; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "وضعیت کارت‌ها");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(getActivity().getResources().getColor(R.color.purple_sexy));
//        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);
        mChart.setDrawSliceText(!mChart.isDrawSliceTextEnabled());
        mChart.invalidate();
    }
    private void configChart() {
        mChart.setLayoutParams(new RelativeLayout.LayoutParams((int) (screenW), (int) (screenW)));
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setCenterTextTypeface(FontHelper.getKoodak(getActivity()));
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(getActivity().getResources().getColor(R.color.white));
        mChart.setTransparentCircleColor(getActivity().getResources().getColor(R.color.white));
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.PIECHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        setData();
    }
}
