package com.example.circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by pz on 2016/8/12.
 *
 * 圆形进度条
 */
public class CircleProgressBar extends View {

    /**
     * 画笔
     */
    private Paint mPaint;
    // 画笔的粗细（默认为40f, 在 onLayout 已修改）
    private float mStrokeWidth = 40f;

    private int mWidth;
    private int mHeight;

    /**
     * 内圆颜色 (可自定义)
     */
    private int circleColor;

    /**
     * 圆环0进度颜色 (可自定义)
     */
    private int ringNormalColor;

    /**
     * 圆环进度颜色 (可自定义)
     */
    private int ringProgressColor;

    /**
     * 圆环的宽度 (可自定义)
     */
    private float roundWidth;

    private float progress = 0f;
    private final float maxProgress = 100f; // 不可以修改的最大值

    public CircleProgressBar(Context context) {
        this(context,null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

            circleColor = a.getColor(R.styleable.CircleProgressBar_roundColor, getResources().getColor(android.R.color.darker_gray));
            ringNormalColor = a.getColor(R.styleable.CircleProgressBar_circleNormalColor, getResources().getColor(android.R.color.holo_blue_dark));
            ringProgressColor = a.getColor(R.styleable.CircleProgressBar_circleProcessColor, getResources().getColor(android.R.color.holo_red_dark));
            roundWidth = a.getDimension(R.styleable.CircleProgressBar_roundWidth, 5);

        } finally {
            // 注意，别忘了 recycle
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        initValue();
        setupPaint();
    }

    /**
     * 初始化各种值
     */
    private void initValue() {
        // 画笔的粗细为总宽度的 1 / 15
        mStrokeWidth = mWidth / 15f;
    }

    /**
     * 设置圆环画笔
     */
    private void setupPaint() {
        // 创建圆形画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(circleColor);
        mPaint.setStyle(Paint.Style.STROKE); // 边框风格
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制圆形
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(circleColor);

        float centerX = mWidth / 2.0f;
        float centerY = mHeight / 2.0f;
        float radius = mWidth / 2.0f - mStrokeWidth / 2.0f;
        canvas.drawCircle(centerX, centerY, radius, mPaint);

        //绘制进度为0的圆环
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setColor(ringNormalColor);
        RectF oval = new RectF(0 + mStrokeWidth / 2, 0 + mStrokeWidth / 2,
                mWidth - mStrokeWidth / 2, mHeight - mStrokeWidth / 2);
        canvas.drawArc(oval, 0, 360, false, mPaint);

        //绘制进度为progress的圆环
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setColor(ringProgressColor);
        canvas.drawArc(oval, 0, progress / maxProgress * 360, false, mPaint);
    }

    /**
     * 设置当前显示的进度条
     *
     * @param progress
     */
    public synchronized void setProgress(float progress) {
        this.progress = progress;

        // 使用 postInvalidate 比 postInvalidat() 好，线程安全
        postInvalidate();
    }
}
