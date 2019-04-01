package com.bupt.colorfulroute.runningapp.uicomponent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bupt.colorfulroute.R;

/**
 * Created by AmosShi on 2016/10/27.
 * <p>
 * Description:仿支付宝信用分表盘控件，自动更新进度，已做好屏幕适配工作，见Demo
 * <p>
 * Email:shixiuwen1991@yeah.net
 * <p>
 * 分析：
 * 3个层级：
 * 1.无需更新的边框刻度以及分数文字刻度等
 * 2.随刻度移动而变化的文字部分
 * 3.移动更新的刻度点
 */

public class DashBoardProgressView extends FrameLayout {

    private Paint paint;
    private Paint paintIndex;
    private Paint textPaint38;
    private Paint textPaint20;
    private Paint textPaint60;
    private RectF rectF;
    private RectF rectF2;

    private float rotation = -90;
    private double score = 0;
    private int scoreLever = 0;

    private PointView pointView;
    private ScoreTextView scoreTextView;
    private DashBoardView dashBoardView;

    private float reguSizeX = 0;   //以此为参考缩放控件以得到合适大小
    private float reguSizeY = 0;

    private float width;
    private float height;

    private Context mContext;

    public DashBoardProgressView(Context context) {
        this(context,null);
        this.mContext = context;
        initPaint();
        initPaintIndex();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dashBoardView = new DashBoardView(context, null);
        addView(dashBoardView, layoutParams);
        pointView = new PointView(context, null);
        addView(pointView, layoutParams);
        scoreTextView = new ScoreTextView(context, null);
        addView(scoreTextView, layoutParams);
//        initRectF();
    }



    public DashBoardProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
        initPaintIndex();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dashBoardView = new DashBoardView(context, attrs);
        addView(dashBoardView, layoutParams);
        pointView = new PointView(context, attrs);
        addView(pointView, layoutParams);
        scoreTextView = new ScoreTextView(context, attrs);
        addView(scoreTextView, layoutParams);
//        initRectF();
    }

    public DashBoardProgressView(Context context, AttributeSet attrs,int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        this.mContext = context;
        initPaint();
        initPaintIndex();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dashBoardView = new DashBoardView(context, attrs);
        addView(dashBoardView, layoutParams);
        pointView = new PointView(context, attrs);
        addView(pointView, layoutParams);
        scoreTextView = new ScoreTextView(context, attrs);
        addView(scoreTextView, layoutParams);
//        initRectF();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.icons));
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initPaintIndex() {
        paintIndex = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paintIndex.setColor(getResources().getColor(R.color.color_red_dark));
        paintIndex.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initTextPaint38() {
        textPaint38 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint38.setColor(getResources().getColor(R.color.icons));
        textPaint38.setStrokeCap(Paint.Cap.ROUND);
        textPaint38.setTextSize(38 / 306f * reguSizeY);
    }

    private void initTextPaint20() {
        textPaint20 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint20.setColor(getResources().getColor(R.color.icons));
        textPaint20.setStrokeCap(Paint.Cap.ROUND);
        textPaint20.setTextSize(20 / 306f * reguSizeY);
    }

    private void initTextPaint60() {
        textPaint60 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint60.setColor(getResources().getColor(R.color.icons));
        textPaint60.setStrokeCap(Paint.Cap.ROUND);
        textPaint60.setTextSize(60 / 306f * reguSizeY);
    }

    private void initRectF() {
        Rect rect = new Rect((int) (10 / 576f * reguSizeX), (int) (10 / 306f * reguSizeY), (int) (566 / 576f * reguSizeX), (int) (566 / 306f * reguSizeY));
        rectF = new RectF(rect);

        Rect rect2 = new Rect((int) (42 / 576f * reguSizeX), (int) (42 / 306f * reguSizeY), (int) (534 / 576f * reguSizeX), (int) (534 / 306f * reguSizeY));
        rectF2 = new RectF(rect2);
    }

    /**
     * 模拟刷新小圆点的位置
     */
    public void refreshScore(final int refreshToScore) {
        score = 0;
        rotation = -90;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (score < refreshToScore) {
                        Thread.sleep(50);
                        rotation += 1;
                        score += 0.55;
                        if (score >= refreshToScore - 1) {  //该判断非常重要，防止每次加score后超出边界
                            score = refreshToScore;
                        }
                        pointView.postInvalidate();
                        scoreTextView.postInvalidate();
                        double tempScore = score + 0.5;
                        if (tempScore < 25) {
                            scoreLever = 0;
                            dashBoardView.postInvalidate();
                        } else if (tempScore >= 25 && tempScore <= 25.55) {
                            scoreLever = 1;
                            dashBoardView.postInvalidate();
                        } else if (tempScore >= 50 && tempScore <= 50.55) {
                            scoreLever = 2;
                            dashBoardView.postInvalidate();
                        } else if (tempScore >= 75 && tempScore <= 75.55) {
                            scoreLever = 3;
                            dashBoardView.postInvalidate();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int getMeasuredSize(int length, boolean isWidth) {
        int mode = MeasureSpec.getMode(length);
        int size = MeasureSpec.getSize(length);
        int resSize = 0;
        if (mode == MeasureSpec.EXACTLY) {
            resSize = size;
        } else {
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    resSize = 576;
                } else {
                    resSize = 306;
                }
            }
        }
        return resSize;
    }

    private class DashBoardView extends View {

        public DashBoardView(Context context) {
//            super(context);
            this(context, null);
        }

        public DashBoardView(Context context, AttributeSet attrs) {
            super(context, attrs);
            /*
             * <p>Causes the Runnable to be added to the message queue.
             * The runnable will be run on the user interface thread.</p>
             * 使用了post后，run()中的代码会提前加载到message queue,提前于onDraw()方法的
             * 执行,以初始化一些数据，有些数据数据是onMeasure()方法中返回的，不这么做的话无法
             * 计算比例以适配大小
             * */
            post(new Runnable() {
                @Override
                public void run() {
                    initPaint();
                    initPaintIndex();
                    initTextPaint38();
                    initTextPaint20();
                    initRectF();
                }
            });

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            initPaint();
            initPaintIndex();
            initTextPaint38();
            initTextPaint20();
            initRectF();

            //转移画布使绘制的内容在所给区域的中央
            if (306 / 576F > height / width) {  //给出的大小可能是不和我们的控件匹配的
                canvas.translate(Math.abs(width - reguSizeX) / 2, 0);
            } else {
                canvas.translate(0, Math.abs(height - reguSizeY) / 2);
            }

            //绘制文字
            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288 / 306f * reguSizeY);
            canvas.drawText("50", -15 / 576f * reguSizeX, -168 / 306f * reguSizeY, textPaint20);
            canvas.restore();

            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288 / 306f * reguSizeY);
            canvas.rotate(45);
            canvas.drawText("75", -15 / 576f * reguSizeX, -168 / 306f * reguSizeY, textPaint20);
            canvas.restore();

            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288 / 306f * reguSizeY);
            canvas.rotate(-45);
            canvas.drawText("25", -15 / 576f * reguSizeX, -168 / 306f * reguSizeY, textPaint20);
            canvas.restore();

            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288 / 306f * reguSizeY);
            canvas.drawText("0", -184 / 576f * reguSizeX, 8 / 306f * reguSizeY, textPaint20);
            canvas.restore();

            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288 / 306f * reguSizeY);
            canvas.drawText("100+", 148 / 576f * reguSizeX, 8 / 306f * reguSizeY, textPaint20);
            canvas.restore();

//            if (scoreLever == 0) {
//                canvas.drawText("信用较差", 213 / 576f * reguSizeX, 196 / 306f * reguSizeY, textPaint38);
//            } else if (scoreLever == 1) {
//                canvas.drawText("信用一般", 213 / 576f * reguSizeX, 196 / 306f * reguSizeY, textPaint38);
//            } else if (scoreLever == 2) {
//                canvas.drawText("信用较好", 213 / 576f * reguSizeX, 196 / 306f * reguSizeY, textPaint38);
//            } else if (scoreLever == 3) {
//                canvas.drawText("信用极好", 213 / 576f * reguSizeX, 196 / 306f * reguSizeY, textPaint38);
//            }
            canvas.drawText("本月跑步", 213 / 576f * reguSizeX, 196 / 306f * reguSizeY, textPaint38);
            //绘制最外框
            paint.setStrokeWidth(8 / 306f * reguSizeY);
            canvas.drawArc(rectF, 175, 190, false, paint);

            //绘制内边框1,2,3,4,5,6分段(带有断点的内边框)
            paint.setStrokeCap(Paint.Cap.BUTT);
            paint.setStrokeWidth(16 / 306f * reguSizeY);
            canvas.drawArc(rectF2, 175, 4, false, paint);
            canvas.drawArc(rectF2, 181, 43, false, paint);
            canvas.drawArc(rectF2, 226, 43, false, paint);
            canvas.drawArc(rectF2, 271, 43, false, paint);
            canvas.drawArc(rectF2, 316, 43, false, paint);
            canvas.drawArc(rectF2, 1, 4, false, paint);

            //绘制内刻度大圆点
            paint.setStrokeWidth(6 / 306f * reguSizeY);
            canvas.save();
            canvas.drawLine(288 / 576f * reguSizeX, 96 / 306f * reguSizeY, 288 / 576f * reguSizeX, 76 / 306f * reguSizeY, paint);
            canvas.restore();
            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288 / 306f * reguSizeY);
            canvas.rotate(45);
            canvas.drawLine(0, -192 / 306f * reguSizeY, 0, -212 / 306f * reguSizeY, paint);
            canvas.restore();
            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288);
            canvas.rotate(90);
            canvas.drawLine(0, -192 / 306f * reguSizeY, 0, -212 / 306f * reguSizeY, paint);
            canvas.restore();
            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288 / 306f * reguSizeY);
            canvas.rotate(-45);
            canvas.drawLine(0, -192 / 306f * reguSizeY, 0, -212 / 306f * reguSizeY, paint);
            canvas.restore();
            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288);
            canvas.rotate(-90);
            canvas.drawLine(0, -192 / 306f * reguSizeY, 0, -212 / 306f * reguSizeY, paint);
            canvas.restore();

            //绘制内刻度小圆点
            paint.setStrokeCap(Paint.Cap.ROUND);
            for (int i = -19; i < 20; i++) {
                canvas.save();
                canvas.translate(288 / 576f * reguSizeX, 288 / 306f * reguSizeY);
                canvas.rotate(4.5f * i);
                canvas.drawLine(0, -192 / 306f * reguSizeY, 0, -202 / 306f * reguSizeY, paint);
                canvas.restore();
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            setMeasuredDimension(576, 306);
            width = getMeasuredSize(widthMeasureSpec, true);
            height = getMeasuredSize(heightMeasureSpec, false);
            setMeasuredDimension((int) width, (int) height);
            if (306 / 576F > height / width) {  //给出的大小可能是不和我们的控件匹配的
                reguSizeX = height * 576 / 306;
                reguSizeY = height;
            } else {
                reguSizeX = width;
                reguSizeY = width * 306 / 576;
            }
        }
    }

    private class PointView extends View {

        public PointView(Context context) {
//            super(context);
            this(context, null);
        }

        public PointView(Context context, AttributeSet attrs) {
            super(context, attrs);
            /*
             * <p>Causes the Runnable to be added to the message queue.
             * The runnable will be run on the user interface thread.</p>
             * */
            post(new Runnable() {
                @Override
                public void run() {
                    initPaint();
                    initPaintIndex();
                }
            });
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            initPaintIndex();
            //转移画布使绘制的内容在所给区域的中央
            if (306 / 576F > height / width) {  //给出的大小可能是不和我们的控件匹配的
                canvas.translate(Math.abs(width - reguSizeX) / 2, 0);
            } else {
                canvas.translate(0, Math.abs(height - reguSizeY) / 2);
            }

            //绘制圆点
            canvas.save();
            canvas.translate(288 / 576f * reguSizeX, 288 / 306f * reguSizeY);
            canvas.rotate(rotation);
            canvas.drawCircle(0, -278 / 576f * reguSizeX, 10 / 306f * reguSizeY, paintIndex);
            canvas.restore();
        }
    }

    private class ScoreTextView extends View {

        public ScoreTextView(Context context) {
//            super(context);
            this(context, null);
        }

        public ScoreTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            /*
             * <p>Causes the Runnable to be added to the message queue.
             * The runnable will be run on the user interface thread.</p>
             * */
            post(new Runnable() {
                @Override
                public void run() {
                    initTextPaint60();
                }
            });
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            initTextPaint60();
            //转移画布使绘制的内容在所给区域的中央
            if (306 / 576F > height / width) {  //给出的大小可能是不和我们的控件匹配的
                canvas.translate(Math.abs(width - reguSizeX) / 2, 0);
            } else {
                canvas.translate(0, Math.abs(height - reguSizeY) / 2);
            }
            //优化，防止绘制脏布局
            canvas.clipRect(213 / 576f * reguSizeX, 280 / 306f * reguSizeY - textPaint60.getTextSize()
                    , 232 / 576f * reguSizeX + textPaint60.measureText(String.valueOf((int) score) + " km"), 280 / 306f * reguSizeY + 10);

            canvas.drawText(String.valueOf((int) score) + " km", 213 / 576f * reguSizeX, 280 / 306f * reguSizeY, textPaint60);
        }
    }

}
