package com.cjj.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cjj on 2016/12/2.
 */

public class CircleProgressBar extends View implements ValueAnimator.AnimatorUpdateListener{

    private int mWidth;//宽
    private int mHeight;//高

    private Paint mBcPaint;//背景圈的画笔

    private float mBcWidth;//背景圈的宽

    private int mBcColor;

    private Paint mRoPaint;//小圆点的画笔

    private Paint mProPaint;//进度条画笔

    private float mProgress = 0;//当前进度

    private int progressColor;//进度条颜色

    private int circleColor;//小球颜色

    private float smallRadius = 0;//小球半径

    private float progressSize;//进度条大小

    private OnProgressBarListener mListener;

    private float endProgress;//最终进度

    private float startProgress;//起始进度

    public CircleProgressBar(Context context) {
        this(context,null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundProgressBar, 0, 0);
//        mProgress = typedArray.getInteger(R.styleable.RoundProgressBar_progress, 0);
        mBcWidth = typedArray.getDimension(R.styleable.RoundProgressBar_rp_smallCircle, 4);
        mBcColor = typedArray.getColor(R.styleable.RoundProgressBar_rp_smallCircleColor, Color.WHITE);
        progressColor = typedArray.getColor(R.styleable.RoundProgressBar_rp_progressBarColor, Color.RED);
        circleColor = typedArray.getColor(R.styleable.RoundProgressBar_rp_ciricleColor, Color.RED);
        smallRadius = typedArray.getDimension(R.styleable.RoundProgressBar_rp_circleRadius, smallRadius);
        progressSize = typedArray.getDimension(R.styleable.RoundProgressBar_rp_progressSize, 8);
        typedArray.recycle();
        initPaint();
    }

    private void initPaint(){
        if(smallRadius!=0 && progressSize>smallRadius){
            throw new RuntimeException("radius can be less than progressSize");
        }
        mBcPaint = new Paint();
        mBcPaint.setStrokeWidth(mBcWidth);
        mBcPaint.setAntiAlias(true);
        mBcPaint.setStyle(Paint.Style.STROKE);
        mBcPaint.setColor(mBcColor);

        mRoPaint = new Paint();
        mRoPaint.setStyle(Paint.Style.FILL);
        mRoPaint.setAntiAlias(true);
        mRoPaint.setColor(circleColor);

        mProPaint= new Paint();
        mProPaint.setStyle(Paint.Style.STROKE);
        mProPaint.setStrokeWidth(progressSize);
        mProPaint.setAntiAlias(true);
        mProPaint.setColor(progressColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mWidth = measure(widthMeasureSpec);
        mHeight = measure(heightMeasureSpec);

        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackGround(canvas);
        drawProgress(canvas);
        drawRound(canvas);
    }


    /**
     * 画背景圆环
     */
    private void drawBackGround(Canvas canvas){
        if(smallRadius<progressSize){
            canvas.drawOval(new RectF(progressSize,progressSize,mWidth-progressSize,mHeight-progressSize),mBcPaint);
        }else {
            canvas.drawOval(new RectF(smallRadius, smallRadius, mWidth - smallRadius, mHeight - smallRadius), mBcPaint);
        }
    }

    /**
     * 画球
     * */
    private void drawRound(Canvas canvas){
        canvas.rotate(3.6f * mProgress, mWidth / 2, mHeight / 2);
        float x = mWidth / 2;
        float y = smallRadius;
        canvas.drawCircle(x, y, smallRadius, mRoPaint);
    }

    /**
     * 画进度条
     * */
    private void drawProgress(Canvas canvas){
        if(smallRadius<progressSize) {
            canvas.drawArc(new RectF(progressSize,progressSize,mWidth-progressSize,mHeight-progressSize), -90, 3.6f * mProgress, false,mProPaint);
        }else {
            canvas.drawArc(new RectF(smallRadius, smallRadius, mWidth - smallRadius, mHeight - smallRadius), -90, 3.6f * mProgress, false, mProPaint);
        }
        canvas.save();

    }

    private int measure(int measureSpec){
        int resule = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            resule = specSize;
        }else {
            resule = 480;
            if(specMode == MeasureSpec.AT_MOST){
                resule = Math.min(resule,specSize);
            }
        }
        return resule;
    }

    /**
     * 设置进度条动画，默认从0开始
     *
     * */
    public void setProgress(float progress){
        if(progress>100 || progress < 0){
            throw new IllegalStateException("The progress should be between 0 and 100!");
        }
        startProgress = 0;
        endProgress = progress;
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.setDuration(2000);
        animator.setFloatValues(progress);
        animator.start();
        if(mListener!=null)mListener.onStart();
    }

    /**
     * 设置进度条动画起点跟终点
     *
     * */
    public void setProgress(float startProgress,float endProgress){
        if(startProgress > endProgress){
            throw new RuntimeException("The startProgress can not more than endProgress!");
        }
        if(startProgress>100 || endProgress >100 || startProgress < 0 || endProgress < 0){
            throw new RuntimeException("The progress should be between 0 and 100!");
        }
        this.startProgress = startProgress;
        this.endProgress = endProgress;
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.setDuration(2000);
        animator.setFloatValues(endProgress-startProgress);
        animator.start();
        if(mListener!=null)mListener.onStart();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mProgress = (float) valueAnimator.getAnimatedValue()+startProgress;
        postInvalidate();
        if(mListener!=null){
            if(mProgress == endProgress) {
                mListener.onProgressing(mProgress/endProgress);
                mListener.onFinish();
            }else {
                mListener.onProgressing(mProgress/endProgress);
            }
        }
    }

    /**
     * 设置动画监听回调，回调中的percent代表的是动画完成百分比
     *
     * */
    public void setListener(OnProgressBarListener listener) {
        mListener = listener;
    }

    public interface OnProgressBarListener{

        void onStart();

        void onProgressing(float percent);

        void onFinish();
    }
}
