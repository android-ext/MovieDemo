package com.zxxk.ext.moviedemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zxxk.ext.moviedemo.R;

/**
 * SurfaceView extends View
 * 其实View是在UI线程中进行绘制
 * SurfaceView是在一个子线程中对自己进行绘制，优势：避免造成UI线程阻塞
 * 其实。我们SurfaceView中包含一个专门用于绘制的Surface。Surface中包含一个Canvas
 * 如何获得Canvas?
 * getHolder -> SurfaceViewHolder
 * holder -> Canvas + 管理SurfaceView的生命周期
 * surfaceCreated
 * surfaceChanged
 * surfaceDestroyed
 *
 * Created by Ext on 2015/9/24.
 */
public class SurfaceViewTemplate extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    /**
     * 用语绘制的线程
     */
    private Thread t;
    /**
     * 线程控制的开关
     */
    private boolean isRunning;

    /**
     * 盘块的奖项
     */
    private String[] mStrs = new String[]{"单反相机", "IPAD", "恭喜发财", "IHPHONE", "服装一套" ,"THINKPAD"};
    /**
     * 盘块的图片
     */
    private int[] mImgs = new int[]{R.mipmap.danfan, R.mipmap.ipad, R.mipmap.xialian, R.mipmap.iphone, R.mipmap.meizi, R.mipmap.xialian};
    /**
     * 盘块的颜色
     */
    private int[] mColors = new int[]{0xffffc300, 0xffffa300, 0xfffcc300, 0xffbfc300, 0xfffbb300, 0xff99c300};
    /**
     * 盘块对应的bitmap数组
     */
    private Bitmap[] mImgsBitmap;

    private int mItemCount = 6;

    /**
     * 整个盘块的范围
     */
    private RectF mRange = new RectF();

    /**
     * 整个盘块的直径
     */
    private int mRadius;
    /**
     * 绘制盘块的画笔
     */
    private Paint mArcPaint;
    /**
     * 绘制文本的画笔
     */
    private Paint mTextPaint;

    /**
     * 盘块滚动的速度
     */
    private double mSpeed = 0;
    private volatile float mStartAngle = 0;
    /**
     * 判断是否点击了停止按钮
     */
    private boolean isShouldEnd;
    /**
     * 转盘的中心位置
     */
    private int mCenter;
    /**
     * padding直接以paddingLeft为准
     */
    private int mPadding;
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg2);

    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());


    public SurfaceViewTemplate(Context context) {
        this(context, null);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();
        mHolder.addCallback(this);
        // 可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置常量
        setKeepScreenOn(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = getPaddingLeft();
        // 直径
        mRadius = width - mPadding * 2;
        // 中心点
        mCenter = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // 初始化绘制盘块的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        // 如果不给透明度ff看起来像是显示在背景底部了
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);

        // 初始化盘块绘制的范围
        mRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);
        // 初始化图片
        mImgsBitmap = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),mImgs[i]);
        }

        // 开启子线程
        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        isRunning = false;

    }

    @Override
    public void run() {
        // 不断进行绘制
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();

            if (end - start < 50) {
                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {

        mCanvas = mHolder.lockCanvas();
        try {
            if (mCanvas != null) { // 判空的原因：可以当程序进入的时候用户刚好点击了返回键 或者service销毁了线程还在
                // 绘制背景
                drawBg();
                // 绘制盘块
                float tmpAngle = mStartAngle;
                float sweepAngle = 360 / mItemCount;
                for (int i = 0; i < mItemCount; i++) {
                    mArcPaint.setColor(mColors[i]);
                    // 绘制盘块
                    mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint);
                    // 绘制文本
                    drawText(tmpAngle, sweepAngle, mStrs[i]);
                    // 绘制盘块图片
                    drawIcon(tmpAngle, mImgsBitmap[i]);
                    tmpAngle += sweepAngle;
                }
                mStartAngle += mSpeed;
                // 如果点击了停止按钮 缓缓地停止
                if (isShouldEnd) {
                    mSpeed -= 1;
                }
                if (mSpeed <= 0) {
                    mSpeed = 0;
                    isShouldEnd = false;
                }
            }
        } finally {
            if(mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);// 释放mCanvas
            }
        }
    }

    /**
     * 点击启动旋转
     * index 为了控制点击停止的时候还要转多少圈
     */
    public void luckyStart(int index) {
        // 计算每一项的角度
        float angle = 360 / mItemCount;
        // 计算每一项中奖范围 (当前index) (中奖区左边到中奖指针的角度,中奖区右边到中奖指针的角度)
        // 1 -> 150 ~ 210
        // 2 -> 210 ~ 270
        float from = 270 - (index + 1) *angle;
        float end = from + angle;

        // 设置停下来需要旋转的距离 也就是求和公式的和(1+2+3+4+...+v1)
        float targetFrom = 5 * 360 + from;
        float targetEnd = 5 * 360 + end;

        /**
         * <pre>
         *  v1 -> 0    v1 + (v1 - 1) + (v1 - 2) + ... + 1 = targetFrom
         *  且每次-1
         *  (v1 + 0) * (v1 + 1) / 2 = targetFrom
         *  v1 * v1 + v1 - 2 * targetFrom = 0;
         *  a = 1, b = 1, c = 2 * targetFrom
         *  v1 = (-1 + Math.sqrt(1 + 8 * targetFrom)) / 2
         * </pre>
         */
        float v1 = (float) ((-1 + Math.sqrt(1 + 8 * targetFrom)) / 2);
        float v2 = (float) ((-1 + Math.sqrt(1 + 8 * targetEnd)) / 2);

        mSpeed = v1 + Math.random() * (v2 - v1);
        isShouldEnd = false;
    }

    public void luckyEnd() {
        mStartAngle = 0;
        isShouldEnd = true;
    }

    /**
     * 转盘是否在旋转
     * @return
     */
    public boolean isStart() {
        return mSpeed != 0;
    }

    public boolean isShouldEnd() {
        return isShouldEnd;
    }
    /**
     * 绘制盘块icon
     * @param tmpAngle
     * @param bitmap
     */
    private void drawIcon(float tmpAngle, Bitmap bitmap) {
        // 设置图片的宽度为直径的1/8
        int imgWidth = mRadius / 8;
        // 起始角度加上每个盘块一半的角度
        float angle = (float) ((tmpAngle + 360 / mItemCount / 2 ) * Math.PI / 180);
        int x = (int) (mCenter + mRadius / 2 / 2  * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2  * Math.sin(angle));

        // 确定图片的位置
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        mCanvas.drawBitmap(bitmap, null, rect, null);
    }

    /**
     * 绘制每个盘块的文本
     * @param tmpAngle
     * @param sweepAngle
     * @param mStr
     */
    private void drawText(float tmpAngle, float sweepAngle, String mStr) {

        Path path = new Path();
        // param0: 弧形的矩形范围 圆弧的位置由外接矩形mRange定义
        // param1: 起始的角度 等于0时的位置是矩形右边1/2高度
        // param2: 移动的度数
        path.addArc(mRange, tmpAngle, sweepAngle);

        // 利用水平偏移量让文字居中
        float textWidth = mTextPaint.measureText(mStr);
        int hOffset = (int)(mRadius * Math.PI / mItemCount - textWidth) / 2;
        int vOffset = mRadius / 2 / 6; // 垂直偏移量
        mCanvas.drawTextOnPath(mStr, path, hOffset, vOffset, mTextPaint);
    }

    /**
     * 绘制背景
     */
    private void drawBg() {
        // 如果不给透明度ff就是是黑色背景
        mCanvas.drawColor(0xffffffff);
        mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2), null);
    }
}
