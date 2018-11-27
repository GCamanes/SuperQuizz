package fr.difinamic.formation.superquizz.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

import fr.difinamic.formation.superquizz.R;
import fr.difinamic.formation.superquizz.database.QuestionDataBaseHelper;
import fr.difinamic.formation.superquizz.model.Question;

public class ScoreFragment extends Fragment {

    private static final String ARG_SCORE = "score";
    private static final String ARG_SCOREMAX = "scoreMax";

    private Integer mScore;
    private Integer mScoreMax;

    private PieChart pieChart;
    private BarChart barChart;

    public ScoreFragment() {}

    public static ScoreFragment newInstance(int score, int scoreMax) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        args.putInt(ARG_SCOREMAX, scoreMax);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mScore = getArguments().getInt(ARG_SCORE);
            mScoreMax = getArguments().getInt(ARG_SCOREMAX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_score, container, false);

        pieChart = rootView.findViewById(R.id.pie_chart1);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleRadius(0f);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        pieChart.animateY(1400, Easing.EaseInOutQuad);

        Legend legendPieChart = pieChart.getLegend();
        legendPieChart.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legendPieChart.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legendPieChart.setOrientation(Legend.LegendOrientation.VERTICAL);
        legendPieChart.setDrawInside(false);
        legendPieChart.setXEntrySpace(7f);
        legendPieChart.setYEntrySpace(0f);
        legendPieChart.setYOffset(0f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);

        barChart = rootView.findViewById(R.id.bar_chart1);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend legendBarChart = barChart.getLegend();
        legendBarChart.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legendBarChart.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legendBarChart.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legendBarChart.setDrawInside(false);
        legendBarChart.setForm(Legend.LegendForm.SQUARE);
        legendBarChart.setFormSize(9f);
        legendBarChart.setTextSize(11f);
        legendBarChart.setXEntrySpace(4f);

        updateCharts();

        return rootView;
    }

    private void updatePieChart(List<Question> questions) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        int correctAnswersCount = 0;
        int wrongAnswersCount = 0;
        int unansweredQuestionCount = 0;

        for (Question q : questions) {
            String userAnswer = QuestionDataBaseHelper.getInstance(getContext()).getUserAnswer(q);
            if (userAnswer == null) {
                unansweredQuestionCount += 1;
            } else if (q.verifierReponse(userAnswer)) {
                correctAnswersCount += 1;
            } else {
                wrongAnswersCount += 1;
            }
        }

        int total = correctAnswersCount + wrongAnswersCount + unansweredQuestionCount;

        ArrayList<PieEntry> questionEntries = new ArrayList<>();

        ArrayList<Integer> colors = new ArrayList<>();

        if (unansweredQuestionCount > 0 ){
            questionEntries.add(new PieEntry((float)unansweredQuestionCount/(float)(total),"Pas de réponse"));
            colors.add(Color.LTGRAY);
        }
        if (correctAnswersCount > 0 ){
            questionEntries.add(new PieEntry((float)correctAnswersCount/(float)(total),"Bonnes réponses"));
            colors.add(Color.GREEN);
        }
        if (wrongAnswersCount > 0 ){
            questionEntries.add(new PieEntry((float)wrongAnswersCount/(float)(total),"Mauvaises réponses"));
            colors.add(Color.RED);
        }

        PieDataSet dataSet = new PieDataSet(questionEntries, "");

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);



        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private void updateBarChart(List<Question> questions) {

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(questions.size()+1);
        barChart.getXAxis().setLabelCount(questions.size());

        ArrayList<BarEntry> valuesGood = new ArrayList<>();
        ArrayList<BarEntry> valuesBad = new ArrayList<>();

        for (int i=0; i < questions.size(); i++) {
            String userAnswer = QuestionDataBaseHelper.getInstance(getContext()).getUserAnswer(questions.get(i));

            if(userAnswer != null) {
                if (questions.get(i).verifierReponse(userAnswer)) {
                    valuesGood.add(new BarEntry(i + 1, QuestionDataBaseHelper.getInstance(getContext()).getUserAnswerTime(questions.get(i))));
                } else {
                    valuesBad.add(new BarEntry(i + 1, QuestionDataBaseHelper.getInstance(getContext()).getUserAnswerTime(questions.get(i))));
                }
            }
        }

        BarDataSet setGood = new BarDataSet(valuesGood, "Bonnes réponses");
        setGood.setColor(Color.GREEN);
        BarDataSet setBad = new BarDataSet(valuesBad, "Mauvaises réponses");
        setBad.setColor(Color.RED);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(setGood);
        dataSets.add(setBad);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        barChart.setData(data);

    }

    private void updateCharts() {
        List<Question> questions = QuestionDataBaseHelper.getInstance(getContext()).getAllQuestions();
        updatePieChart(questions);
        updateBarChart(questions);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}