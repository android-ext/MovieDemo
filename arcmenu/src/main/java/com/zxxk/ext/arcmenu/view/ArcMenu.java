package com.zxxk.ext.arcmenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.zxxk.ext.arcmenu.R;

/**
 * 1.动画
 * 2.自定义ViewGroup
 *   自定义属性
 *      a.attr.xml
 *      b.在布局文件中使用
 *      c.在自定义控件中进行读取
 *    onMeasure
 *    onLayout
 *    设置主按钮的旋转动画
 *    为menuItem添加平移动画和旋转动画
 *    实现menuItem的点击动画
 *
 * 3.菜单按钮一开始（在onLayout()中设置布局位置）就位于展开后的圆弧位置了，只是在没有展开前给隐藏了，
 * 在展开的时候设置可见，通过代码设置相对于当前位置的偏移量的改变的同时改变透明度(自始至终菜单按钮的位置是没有改变的)
 * Created by Ext on 2015/9/30.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {

    private static final int POS_LEFT_TOP = 0;
    private static final int POS_LEFT_BOTTOM = 1;
    private static final int POS_RIGHT_TOP = 2;
    private static final int POS_RIGHT_BOTTOM = 3;

    private Position mPosition = Position.LEFT_BOTTOM;
    private int mRadius;
    /** 菜单的状态 */
    private Status mCurrentStatus = Status.CLOSE;
    /** 菜单的主按钮 */
    private View mCButton;
    private OnMenuItemClickListener mMenuItemClickListener;

    public enum Status {
        OPEN, CLOSE
    }

    /**
     * 菜单的位置枚举类
     */
    public enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    public void setmMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }

    /** 点击子菜单项的回调接口 */
    public interface OnMenuItemClickListener {
        void onClick(View view, int pos);
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);

        int pos = a.getInt(R.styleable.ArcMenu_position, 3);
        switch (pos) {
            case POS_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            // 测量child
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed) {
            layoutCButton();

            int count = getChildCount();
            for (int i = 0; i < count -1; i++) {
                View child = getChildAt(i + 1); // 0是中间view
                child.setVisibility(INVISIBLE);
                int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
                int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();
                // 如果菜单位置在底部 左下，右下
                if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {
                    ct = getMeasuredHeight() - cHeight - ct;
                }
                // 如果菜单右上，右下
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                    cl = getMeasuredWidth() - cWidth - cl;
                }

                child.layout(cl, ct, cl + cWidth, ct + cHeight);
            }
        }
    }

    /**
     * 定位主菜单按钮
     */
    private void layoutCButton() {
        mCButton = getChildAt(0);
        mCButton.setOnClickListener(this);

        int l = 0;
        int t = 0;
        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();

        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
            break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
            break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
            break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
            break;
        }
        mCButton.layout(l, t, l + width, t + height);
    }

    @Override
    public void onClick(View v) {

        mCButton = findViewById(R.id.id_button);
        rotateCButton(v, 0f, 360f, 300);
        toggleMenu(300);
    }

    /**
     * 切换菜单
     */
    public void toggleMenu(int duration) {
        // 为menuItem添加平移动画和旋转动画
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i + 1); // 中间那个主按钮下标为0
            childView.setVisibility(VISIBLE);
            // end 0,0
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
            int xflag = 1;
            int yflag = 1;
            if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
                xflag = -1;
            }
            if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
                yflag = -1;
            }
            AnimationSet animSet = new AnimationSet(true);
            Animation transAnim = null;
            // to open
            if (mCurrentStatus == Status.CLOSE) {
                // (float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
                // fromXDelta动画开始的点离当前View X坐标上的差值  toXDelta动画结束的点离当前View X坐标上的差值
                transAnim = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                childView.setClickable(true);
                childView.setFocusable(true);
            } else { // to close
                transAnim = new TranslateAnimation(0, xflag * cl, 0, yflag * ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            transAnim.setFillAfter(true);
            transAnim.setDuration(duration);
            // 设置一个动画执行的启动时间
            transAnim.setStartOffset(i * 100 / count);
            transAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE) {
                        childView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            // 旋转动画
            RotateAnimation rotateAnim = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(duration);
            rotateAnim.setFillAfter(true);
            animSet.addAnimation(rotateAnim);
            animSet.addAnimation(transAnim);
            childView.startAnimation(animSet);

            final int pos = i +1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMenuItemClickListener != null) {
                        mMenuItemClickListener.onClick(v, pos);
                    }

                    menuItemAnim(pos - 1);
                    changeStatus();
                }
            });
        }
        // 切换菜单状态
        changeStatus();
    }

    /**
     * 添加menuItem的点击动画
     * @param pos
     */
    private void menuItemAnim(int pos) {
        for (int  i = 0, len = getChildCount() - 1; i < len; i++) {
            View childView = getChildAt(i+1);
            if (i == pos) {
                childView.startAnimation(scaleBigAnim(300));
            } else {
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);
        }
    }
    /**
     * 为当前点击的item设置缩小和透明度降低的动画
     * @param duration
     * @return
     */
    private Animation scaleSmallAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    /**
     * 为当前点击的item设置变大和透明度降低的动画
     * @param duration
     * @return
     */
    private Animation scaleBigAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);
        // (float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) pivot 中心点
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    // 切换菜单状态
    private void changeStatus() {
        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
    }

    /**
     * 中心按钮的旋转
     * @param v
     * @param start 旋转的开始角度
     * @param end 旋转的结束角度
     * @param duration
     */
    private void rotateCButton(View v, float start, float end, int duration) {
        // X轴的伸缩模式(相对于自身 ) X坐标的伸缩值 Y轴的伸缩模式 Y坐标的伸缩值 (0.5f, 0.5f：变化点在中心)
        RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true); // 动画执行完毕后保留在终止位置
        v.startAnimation(anim);
    }

    public boolean isOpen() {
        return mCurrentStatus == Status.OPEN;
    }

}
