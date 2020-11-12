package com.example.hp.myapplication1.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

import com.example.hp.myapplication1.Chart.XYMarkerView;
import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.Chart.AppInformation;
import com.example.hp.myapplication1.Chart.StatisticsInfo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;


public class ContentFragment extends Fragment implements ScreenShotable, SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {
    public static final String CLOSE = "Close";
    public static final String FIRST = "Building";
    public static final String SECOND = "Book";
    public static final String THIRD = "Paint";
    public static final String FOURTH = "Case";
    public static final String FIFTH = "Shop";
    public static final String SIXTH = "Party";
    public static final String SEVENTH = "Movie";

    private View               containerView;
    protected ImageView        mImageView;
    protected int              res;
    private Bitmap             bitmap;
    protected ListView         mListView;
    private int                recentDays;

    private String type ="";
    private View rootView;
    private int style = StatisticsInfo.DAY;
    protected BarChart barChart;
    protected PieChart mChart;
    protected RectF mOnValueSelectedRectF = new RectF();
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    ArrayList<AppInformation> ShowList;
    boolean usehour = false;
    private Typeface tf;
    private long totaltime;

    public static ContentFragment newInstance(int resId, String type, int recentDays) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        contentFragment.setType(type);
        contentFragment.setRecentDays(recentDays);
        return contentFragment;
    }

    public static ContentFragment newInstance(int resId) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRecentDays(int recentDays) {
        this.recentDays = recentDays;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getArguments().getInt(Integer.class.getName());
        mTfRegular = Typeface.createFromAsset(this.getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(this.getActivity().getAssets(), "OpenSans-Light.ttf");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.type.equals(ContentFragment.FIFTH)){
            SetButtonColor();

            StatisticsInfo statisticsInfo = new StatisticsInfo(this.getContext(),style);
            ShowList = statisticsInfo.getShowList();
            usehour = getLagerestTime() > 300;

            barChart = (BarChart) rootView.findViewById(R.id.barchart);
            barChart.setOnChartValueSelectedListener(this);

            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);

            barChart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            barChart.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            barChart.setPinchZoom(false);

            barChart.setDrawGridBackground(false);
            // mChart.setDrawYLabels(false);

            IAxisValueFormatter xAxisFormatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    int i = (int)value;
                    if(ShowList.size() > i) {
                        if(i >= 6)
                            return "其他应用";
                        else {
                            String str = ShowList.get(i).getLabel();
                            if (str.length() <= 4)
                                return str;
                            else return (str.substring(0, 3) + "..");
                        }
                    }
                    else return "";
                }
            };
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTypeface(mTfLight);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
            xAxis.setValueFormatter(xAxisFormatter);

            IAxisValueFormatter custom = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
//                long i = (long)value;
//                i = i * ((YLength  / 1000 / 60 + 20) / 60 ) * 10;
                    if(usehour)
                        return (int)value + "hour";
                    else return (int)value + "min";
                }
            };

            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setTypeface(mTfLight);
            leftAxis.setLabelCount(8, false);
            leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setDrawGridLines(false);
            rightAxis.setTypeface(mTfLight);
            rightAxis.setLabelCount(8, false);
            rightAxis.setValueFormatter(custom);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);
            // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
            // "def", "ghj", "ikl", "mno" });
            // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
            // "def", "ghj", "ikl", "mno" });

            XYMarkerView mv = new XYMarkerView(this.getContext(), xAxisFormatter);
            mv.setChartView(barChart); // For bounds control
            barChart.setMarker(mv); // Set the marker to the chart

            setDataBarChart();


            // mChart.setDrawLegend(false);
        }
        if(this.type.equals(ContentFragment.SIXTH)){
            SetButtonColor();

            mChart = (PieChart) rootView.findViewById(R.id.chart1);
            mChart.setUsePercentValues(true);
            mChart.getDescription().setEnabled(false);
            mChart.setExtraOffsets(5, 10, 5, 5);

            mChart.setDragDecelerationFrictionCoef(0.95f);

            tf = Typeface.createFromAsset(this.getActivity().getAssets(), "OpenSans-Regular.ttf");

            mChart.setCenterTextTypeface(Typeface.createFromAsset(this.getActivity().getAssets(), "OpenSans-Light.ttf"));
            mChart.setCenterText(generateCenterSpannableText(style));

            mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

            mChart.setDrawHoleEnabled(true);
            mChart.setHoleColor(Color.WHITE);

            mChart.setTransparentCircleColor(Color.WHITE);
            mChart.setTransparentCircleAlpha(110);

            mChart.setEntryLabelColor(R.color.dimgrey);

            //设置内圈半径的角度
            mChart.setHoleRadius(58f);
            mChart.setTransparentCircleRadius(61f);

            mChart.setDrawCenterText(true);

            mChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            mChart.setRotationEnabled(true);
            mChart.setHighlightPerTapEnabled(true);

            // mChart.setUnit(" €");
            // mChart.setDrawUnitsInChart(true);

            // add a selection listener
            mChart.setOnChartValueSelectedListener(this);

            setData(style);

            mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            // mChart.spin(2000, 0, 360);

            Legend l = mChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setEnabled(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(this.type.equals(ContentFragment.FIFTH)){
            TextView tv = this.getActivity().findViewById(R.id.fragment_label);
            tv.setText(">柱状统计");
            rootView = inflater.inflate(R.layout.activity_bar_chart, container, false);
            Button buttonday = (Button) rootView.findViewById(R.id.daybuttonchart2);
            buttonday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(style != StatisticsInfo.DAY) {
                        style = StatisticsInfo.DAY;
                        onResume();
                    }
                }
            });
            Button buttonweek = (Button) rootView.findViewById(R.id.weekbuttonchart2);
            buttonweek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(style != StatisticsInfo.WEEK) {
                        style = StatisticsInfo.WEEK;
                        onResume();
                    }
                }
            });
            Button buttonmonth = (Button) rootView.findViewById(R.id.monthbuttonchart2);
            buttonmonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(style != StatisticsInfo.MONTH) {
                        style = StatisticsInfo.MONTH;
                        onResume();
                    }
                }
            });
            Button buttonyear = (Button) rootView.findViewById(R.id.yearbuttonchart2);
            buttonyear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(style != StatisticsInfo.YEAR) {
                        style = StatisticsInfo.YEAR;
                        onResume();
                    }
                }
            });
            return rootView;
        }
        if(this.type.equals(ContentFragment.SIXTH)){
            TextView tv = this.getActivity().findViewById(R.id.fragment_label);
            tv.setText(">饼状统计");
            rootView = inflater.inflate(R.layout.activity_pie_polyline_chart, container, false);
            Button buttonday = (Button) rootView.findViewById(R.id.daybuttonchart1);
            buttonday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(style != StatisticsInfo.DAY) {
                        style = StatisticsInfo.DAY;
                        onResume();
                    }
                }
            });
            Button buttonweek = (Button) rootView.findViewById(R.id.weekbuttonchart1);
            buttonweek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(style != StatisticsInfo.WEEK) {
                        style = StatisticsInfo.WEEK;
                        onResume();
                    }
                }
            });
            Button buttonmonth = (Button) rootView.findViewById(R.id.monthbuttonchart1);
            buttonmonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(style != StatisticsInfo.MONTH) {
                        style = StatisticsInfo.MONTH;
                        onResume();
                    }
                }
            });
            Button buttonyear = (Button) rootView.findViewById(R.id.yearbuttonchart1);
            buttonyear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(style != StatisticsInfo.YEAR) {
                        style = StatisticsInfo.YEAR;             //决定的是显示哪一个页面
                        onResume();
                    }
                }
            });
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mImageView = rootView.findViewById(R.id.image_content);
        mImageView.setClickable(true);
        mImageView.setFocusable(true);
        mImageView.setImageResource(res);
        if(!type.equals("")){
            mListView = rootView.findViewById(R.id.list_dropdownview);
            new MyDropDownListView(this.getActivity(),mListView, type, recentDays);
            return rootView;
        }
        return rootView;
    }

    @Override
    public void takeScreenShot() {
        Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                containerView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        containerView.draw(canvas);
        ContentFragment.this.bitmap = bitmap;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        if(this.type.equals(ContentFragment.FIFTH)){
            RectF bounds = mOnValueSelectedRectF;
            barChart.getBarBounds((BarEntry) e, bounds);
            MPPointF position = barChart.getPosition(e, YAxis.AxisDependency.LEFT);

            Log.i("bounds", bounds.toString());
            Log.i("position", position.toString());

            Log.i("x-index",
                    "low: " + barChart.getLowestVisibleX() + ", high: "
                            + barChart.getHighestVisibleX());

            MPPointF.recycleInstance(position);
        }
        if(this.type.equals(ContentFragment.FIFTH)){
            Log.i("VAL SELECTED",
                    "Value: " + e.getY() + ", xIndex: " + e.getX()
                            + ", DataSet index: " + h.getDataSetIndex());
        }

    }

    @Override
    public void onNothingSelected() { }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        barChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    private void SetButtonColor() {
        if(this.type.equals(ContentFragment.FIFTH)){
            Button buttonday = (Button) rootView.findViewById(R.id.daybuttonchart2);
            Button buttonmonth = (Button)  rootView.findViewById(R.id.monthbuttonchart2);
            Button buttonyear = (Button)  rootView.findViewById(R.id.yearbuttonchart2);
            Button buttonweek = (Button)  rootView.findViewById(R.id.weekbuttonchart2);

            buttonday.setTextColor(Color.WHITE);
            buttonmonth.setTextColor(Color.WHITE);
            buttonweek.setTextColor(Color.WHITE);
            buttonyear.setTextColor(Color.WHITE);

            switch (style) {
                case StatisticsInfo.DAY:
                    buttonday.setTextColor(Color.GREEN);
                    break;
                case StatisticsInfo.MONTH:
                    buttonmonth.setTextColor(Color.GREEN);
                    break;
                case StatisticsInfo.WEEK:
                    buttonweek.setTextColor(Color.GREEN);
                    break;
                case StatisticsInfo.YEAR:
                    buttonyear.setTextColor(Color.GREEN);
                    break;
            }
        }
        if(this.type.equals(ContentFragment.SIXTH)){
            Button buttonday = (Button) rootView.findViewById(R.id.daybuttonchart1);
            Button buttonmonth = (Button) rootView.findViewById(R.id.monthbuttonchart1);
            Button buttonyear = (Button) rootView.findViewById(R.id.yearbuttonchart1);
            Button buttonweek = (Button) rootView.findViewById(R.id.weekbuttonchart1);
            buttonday.setTextColor(Color.WHITE);
            buttonmonth.setTextColor(Color.WHITE);
            buttonweek.setTextColor(Color.WHITE);
            buttonyear.setTextColor(Color.WHITE);

            switch (style) {
                case StatisticsInfo.DAY:
                    buttonday.setTextColor(Color.GREEN);
                    break;
                case StatisticsInfo.MONTH:
                    buttonmonth.setTextColor(Color.GREEN);
                    break;
                case StatisticsInfo.WEEK:
                    buttonweek.setTextColor(Color.GREEN);
                    break;
                case StatisticsInfo.YEAR:
                    buttonyear.setTextColor(Color.GREEN);
                    break;
            }
        }
    }

    private double getLagerestTime() {
        double time = 0;
        for(AppInformation appinformation: ShowList) {
            if(1.0 * appinformation.getUsedTimebyDay() /1000 / 60 > time)
                time = 1.0 * appinformation.getUsedTimebyDay() / 1000 / 60;
        }
        return time;
    }

    private void setDataBarChart() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        if(ShowList.size() < 6) {
            for (int i = 0; i < ShowList.size(); i++) {
                float value;
                if(usehour)
                    value = (float)(1.0 * ShowList.get(i).getUsedTimebyDay() / 1000 / 60 / 60);
                else value = (float)(1.0 * ShowList.get(i).getUsedTimebyDay() / 1000 / 60);
                yVals1.add(new BarEntry(i, value));
            }
        }
        else {
            for(int i = 0;i < 6;i++) {
                float value;
                if(usehour)
                    value = (float)(1.0 * ShowList.get(i).getUsedTimebyDay() / 1000 / 60 /60);
                else value = (float)(1.0 * ShowList.get(i).getUsedTimebyDay() / 1000 / 60);
                yVals1.add(new BarEntry(i, value));
            }
            long otherTime = 0;
            for(int i=6;i<ShowList.size();i++) {
                otherTime += ShowList.get(i).getUsedTimebyDay();
            }
            if(usehour)
                yVals1.add(new BarEntry(6,(float)(1.0 * otherTime / 1000 / 60 / 60)));
            else
                yVals1.add(new BarEntry(6,(float)(1.0 * otherTime / 1000 / 60)));
        }
        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Different APPs");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            barChart.setData(data);
        }
        barChart.invalidate();
    }

    private SpannableString generateCenterSpannableText(int style) {

        String s1 = "应用数据统计";
        String s2;
        if(style == StatisticsInfo.WEEK) {
            s2 = "一周内应用使用情况";
        }
        else if(style == StatisticsInfo.MONTH)
            s2 = "30天应用使用情况";
        else if(style == StatisticsInfo.YEAR)
            s2 = "一年应用使用情况";
        else s2 = "当天应用使用情况";

        SpannableString s = new SpannableString(s1 + "\n" + s2);
        s.setSpan(new RelativeSizeSpan(1.5f), 0, s1.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);

//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - s2.length(), s.length(), 0);
        return s;
    }

    private void setData(int style) {
        StatisticsInfo statisticsInfo = new StatisticsInfo(this.getContext(),style);
        ArrayList<AppInformation> ShowList = statisticsInfo.getShowList();

        totaltime = statisticsInfo.getTotalTime();
        TextView textView =(TextView) rootView.findViewById(R.id.textViewchart);

        SpannableString sp = new SpannableString("已使用总时间: " + DateUtils.formatElapsedTime(totaltime / 1000));
        sp.setSpan(new RelativeSizeSpan(1.35f), 0, sp.length(), 0);
        sp.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0, sp.length(), 0);
        textView.setText(sp);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        if(ShowList.size() < 6) {
            for (int i = 0; i < ShowList.size(); i++) {
                float apptime = (float)ShowList.get(i).getUsedTimebyDay() / 1000;
                if(apptime / totaltime * 1000 >= 0.001)
                    entries.add(new PieEntry(apptime, ShowList.get(i).getLabel()));
            }
        }
        else {
            for(int i = 0;i < 6;i++) {
                float apptime = (float)ShowList.get(i).getUsedTimebyDay() / 1000;
                if(apptime / totaltime * 1000 >= 0.001)
                    entries.add(new PieEntry(apptime, ShowList.get(i).getLabel()));
            }
            long otherTime = 0;
            for(int i=6;i<ShowList.size();i++) {
                otherTime += ShowList.get(i).getUsedTimebyDay() / 1000;
            }
            if(1.0 * otherTime / totaltime * 1000 >= 0.001)
                entries.add(new PieEntry((float)otherTime, "其他应用"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
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


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }
}

