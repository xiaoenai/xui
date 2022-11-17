package com.xiaoenai.xui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiaoenai.xui.tools.SizeUtils;
import com.xiaoenai.xui.tools.StatusBarUtil;

/**
 * @Description: 可移动吸边的FL
 * @ClassName: MoveFrameLayout
 */
public class MoveFrameLayout extends FrameLayout {

    public MoveFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public MoveFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private boolean isAttach;// 是否自动吸边
    private boolean isDrag;// 是否可拖动
    private int minY;// 顶部限制
    private int maxY;// 底部限制

    /**
     * 初始化自定义属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray mTypedAttay = context.obtainStyledAttributes(attrs, R.styleable.MoveFrameLayout);
        isAttach = mTypedAttay.getBoolean(R.styleable.MoveFrameLayout_IsAttach, true);
        isDrag = mTypedAttay.getBoolean(R.styleable.MoveFrameLayout_IsDrag, true);
        minY = mTypedAttay.getInteger(R.styleable.MoveFrameLayout_MinY, 0);
        minY = SizeUtils.dp2px(context, minY);
        maxY = mTypedAttay.getInteger(R.styleable.MoveFrameLayout_MaxY, 0);
        minY += StatusBarUtil.getStatusBarHeight(context);
        mTypedAttay.recycle();
    }

    private int mParentWidth = 0;//父控件的宽
    private int mParentHeight = 0;//父控件的高

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取父控件宽高
        ViewGroup mViewGroup = (ViewGroup) getParent();
        if (mViewGroup != null) {
            int[] location = new int[2];
            mViewGroup.getLocationOnScreen(location);
            //获取父布局的高度
            mParentHeight = mViewGroup.getMeasuredHeight();
            mParentWidth = mViewGroup.getMeasuredWidth();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    private float mLastX;//按下位置x
    private float mLastY;//按下位置Y
    private boolean isDrug = true;//是否是拖动

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDrag) {
            //请求父控件将事件交由自己处理
            getParent().requestDisallowInterceptTouchEvent(true);
            float mX = ev.getX();
            float mY = ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //重置拖动状态
                    isDrug = true;
                    //记录按下的位置
                    mLastX = mX;
                    mLastY = mY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    //手指X轴滑动距离
                    float differenceValueX = mX - mLastX;
                    //手指Y轴滑动距离
                    float differenceValueY = mY - mLastY;
                    //判断是否为拖动操作
                    if (!isDrug) {
                        if (Math.sqrt(differenceValueX * differenceValueX + differenceValueY * differenceValueY) < 2) {
                            isDrug = false;
                        } else {
                            isDrug = true;
                        }
                    }
                    //获取手指按下的距离与控件本身X轴的距离
                    float ownX = getX();
                    //获取手指按下的距离与控件本身Y轴的距离
                    float ownY = getY();
                    //理论中X轴拖动的距离
                    float endX = ownX + differenceValueX;
                    //理论中Y轴拖动的距离
                    float endY = ownY + differenceValueY;
                    //X轴可以拖动的最大距离
                    float maxX = mParentWidth - getWidth();
                    //Y轴可以拖动的最大距离
                    float dMaxY = mParentHeight - getHeight() - SizeUtils.dp2px(getContext(), maxY);
                    //X轴边界限制
                    endX = endX < 0 ? 0 : endX > maxX ? maxX : endX;
                    //Y轴边界限制
                    endY = endY < 0 ? 0 : endY > dMaxY ? dMaxY : endY;
                    //Y轴边界限制 TOP
                    endY = endY < minY ? minY : endY;
                    //开始移动
                    setX(endX);
                    setY(endY);
                    break;
                case MotionEvent.ACTION_UP:
                    // 根据自定义属性判断是否需要贴边
                    if (isAttach) {
                        // 判断是否为点击事件
                        float center = mParentWidth / 2;
                        // 自动贴边
                        if (ev.getRawX() <= center) {
                            // 向左贴边
                            animate()
                                    .setInterpolator(new OvershootInterpolator())
                                    .setDuration(400)
                                    .x(0)
                                    .start();
                        } else {
                            // 向右贴边
                            animate()
                                    .setInterpolator(new OvershootInterpolator())
                                    .setDuration(400)
                                    .x(mParentWidth - getWidth() - 0)
                                    .start();
                        }
                    }
                    break;
            }
        }
        return isDrug ? true : super.onTouchEvent(ev);
    }
}
