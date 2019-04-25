package com.bupt.colorfulroute.runningapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.entity.RouteInfo;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * @author CheatGZ
 * @date 2019/4/6.
 * description：
 */
public class AnalysisActivity extends BaseActivity {

    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_button)
    ImageView rightButton;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    @BindView(R.id.analysis_number)
    TextView analysisNumber;

    private LineChartView lineChart;
    private PieChartView pieChart;
    private TextView lengthAll;
    private TextView timeAll;
    private TextView bestLength;
    private TextView bestTime;

    private UserInfo userInfo1 = null;
    private List<RouteInfo> routeList = new ArrayList<>();
    private Message msg = null;

    //折线图数据数组
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();
    private List<SliceValue> values = new ArrayList<SliceValue>();
    private PieChartData data;         //存放数据\

    //饼图属性
    private boolean hasLabels = true;                   //是否有标签
    private boolean hasLabelsOutside = false;           //标签是否在扇形外面
    private boolean hasCenterCircle = true;            //是否有中心圆
    private boolean hasCenterText1 = true;             //是否有中心的文字
    private boolean hasCenterText2 = false;             //是否有中心的文字2
    private boolean isExploded = true;                  //是否是炸开的图像
    private boolean hasLabelForSelected = false;         //选中的扇形显示标签

    //线程使用的handler  创建一个线程来进行实时显示
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
            switch (msg.what) {
                case 1:
                    double h, m;
                    h = (double) (userInfo1.getTotalTime() / 1000 / 3600 % 24);
                    m = (double) (userInfo1.getTotalTime() / 1000 / 60 % 60);
                    lengthAll.setText("" + df.format(userInfo1.getTotalLength() / 1000));
                    analysisNumber.setText("累计跑步 " + userInfo1.getNumber().toString() + " 次");
                    timeAll.setText("" + df.format(h + m / 60));
                    break;
                case 2:
                    //最远距离和最长时间
                    Double temp_length = 0.0;
                    Long temp_time = 0L;
                    for (int i = 0; i < routeList.size(); i++) {
                        if (temp_length < routeList.get(i).getLength()) {
                            temp_length = routeList.get(i).getLength();
                        }
                        if (temp_time < routeList.get(i).getTime()) {
                            temp_time = routeList.get(i).getTime();
                        }
                    }
                    bestLength.setText(df.format(temp_length / 1000) + " 公里");
                    bestTime.setText(df.format(temp_time / (1000 * 60)) + " 分钟");
                    intiLineChart();
                    initPieChart();
                    break;
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_layout:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "e834b45389cad785bed5c43e2942b606");
        setContentView(R.layout.activity_analysis);
        ButterKnife.bind(this);
        lineChart = findViewById(R.id.line_chart);
        pieChart=findViewById(R.id.pie_chart);
        lengthAll = findViewById(R.id.length_all);
        timeAll = findViewById(R.id.time_all);
        bestLength = findViewById(R.id.best_length);
        bestTime = findViewById(R.id.best_time);
        backButton.setBackgroundResource(R.mipmap.back);
        titleText.setText("数据统计");

        leftLayout.setOnClickListener(onClickListener);
        updateStatisticData();
        updateHistoryData();
    }

    private void initPieChart() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#");
        int pienumber=3;
        int[] length={0,0,0};//1-3km，3-6km，6-9km，
        int[] color={getResources().getColor(R.color.pie_orange)
                ,getResources().getColor(R.color.pie_blue)
        ,getResources().getColor(R.color.pie_red)};
        //初始化数据


        for(int i=0;i<routeList.size();i++){
            if( Integer.parseInt(df.format(routeList.get(i).getLength() / 1000))<3){
                length[0]++;
            }else if(Integer.parseInt(df.format(routeList.get(i).getLength() / 1000))>=6){
                length[2]++;
            }else {
                length[1]++;
            }
        }
        for(int i =0;i<pienumber;i++){
            SliceValue sliceValue = new SliceValue(length[i], color[i]);
            values.add(sliceValue);

            data = new PieChartData(values);
            data.setHasLabels(hasLabels);
            data.setHasLabelsOnlyForSelected(hasLabelForSelected);
            data.setHasLabelsOutside(hasLabelsOutside);
            data.setHasCenterCircle(hasCenterCircle);

            if (isExploded) {
                data.setSlicesSpacing(0);
            }

            if (hasCenterText1) {
                data.setCenterText1("跑步距离分布图");//设置中心文字1
                data.setCenterText1FontSize(10);//设置文本大小
                data.setCenterText1Color(getResources().getColor(R.color.icons));
            }

            if (hasCenterText2) {
                data.setCenterText2("");//设置中心文字2
            }
            pieChart.setViewportCalculationEnabled(true);
            pieChart.setChartRotationEnabled(false);
            pieChart.setPieChartData(data);
        }
    }

    //初始化折线图统计数据
    private void intiLineChart() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#");
        for (int i = 0; i < routeList.size(); i++) {
            //横坐标
            mAxisXValues.add(new AxisValue(i).setLabel(routeList.get(i).getStartTime().substring(5, 16)));
            //数据点
            mPointValues.add(new PointValue(i, Integer.parseInt(df.format(routeList.get(i).getLength() / 1000))));
        }

        Line line = new Line(mPointValues).setColor(getResources().getColor(R.color.color_red_dark));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
//        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setName("跑步历史分布图");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(false); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("里程(公里)");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.WHITE);
//        data.setAxisYLeft(axisY);  //Y轴设置在左边
        data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    private void updateStatisticData() {
        SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String objectId = sp.getString("objectId", "");
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if (e == null) {
                    userInfo1 = userInfo;
                    msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private void updateHistoryData() {
        SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String account = sp.getString("account", "");
        BmobQuery<RouteInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", account);
        bmobQuery.order("-startTime");
        bmobQuery.findObjects(new FindListener<RouteInfo>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void done(List<RouteInfo> list, BmobException e) {
                if (e == null) {
                    routeList = new ArrayList<>();
                    routeList.addAll(list);
                    msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } else {
                    System.out.println("analysis bmob fail!" + e.getMessage());
                }
            }
        });
    }

}
