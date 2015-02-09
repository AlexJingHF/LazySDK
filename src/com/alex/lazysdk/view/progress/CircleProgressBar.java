package com.alex.lazysdk.view.progress;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.alex.lazysdk.utils.AbLogUtil;

/**
 * Created by alex on 15-2-6.
 */
public class CircleProgressBar extends View {

    /**
     * 默认颜色
     */
    private int mBorderColor = 0XFFFFFFFF;
    private int mPathColor = 0XFF000000;
    private int mFillColor = 0XFF345678;
    private int mBGColor = 0XFF2B2B2B;
    /**
     * 默认数值
     */
    private int mBorderWidth = 16;
    /**
     * 浮雕效果
     */
    private EmbossMaskFilter embossMaskFilter = null;
    /**光源方向*/
    private float[] direction = new float[]{1,1,1};
    /**
     * 环境光源权重
     */
    private float ambient = 0.4f;
    /**
     * 镜面反射系数
     */
    private float specular = 6;
    /**
     * 模糊半径
     */
    private float blurRadius = 3.5f;

    private float height;

    private float width;

    private float radius;

    private Paint mPathPaint;

    private Paint mFillPaint;

    private RectF mOval;

    private boolean isReset = false;

    private int progress = 59;
    /** The max. */
    private int max = 100;

    public CircleProgressBar(Context context) {
        super(context);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);//抗锯齿
        mPathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        mPathPaint.setDither(true);//抖动
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);//线条更圆滑

        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);//抗锯齿
        mFillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        mFillPaint.setDither(true);//抖动
        mFillPaint.setStyle(Paint.Style.STROKE);
        mFillPaint.setStrokeJoin(Paint.Join.ROUND);//线条更圆滑

        mOval = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isReset){
            canvas.drawColor(Color.TRANSPARENT);
            isReset = false;
        }
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        radius = width/2 - mBorderWidth;

        mPathPaint.setColor(mPathColor);
        mPathPaint.setStrokeWidth(mBorderWidth);
        canvas.drawCircle(width/2,height/2,radius,mPathPaint);
        //边线
        mPathPaint.setStrokeWidth(0.5f);
        mPathPaint.setColor(mBorderColor);
        canvas.drawCircle(this.width/2, this.height/2, radius+mBorderWidth/2+0.5f, mPathPaint);
        canvas.drawCircle(this.width/2, this.height/2, radius-mBorderWidth/2-0.5f, mPathPaint);

        mFillPaint.setColor(mFillColor);
        mFillPaint.setStrokeCap(Paint.Cap.ROUND);
        mFillPaint.setStrokeWidth(mBorderWidth);

        mOval.set(this.width/2 - radius, this.height/2 - radius, this.width/2 + radius, this.height/2 + radius);
        canvas.drawArc(mOval,-90,(float)progress/max * 360,false,mFillPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        AbLogUtil.d(getClass(),"w:"+w+"\th:"+h);
        setMeasuredDimension(w, h);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.postInvalidate();
    }
}
