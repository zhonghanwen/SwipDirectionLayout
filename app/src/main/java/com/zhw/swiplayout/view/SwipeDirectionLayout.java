package com.zhw.swiplayout.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Created by zhonghanwen on 2019-01-23.
 */
public class SwipeDirectionLayout extends ConstraintLayout {

    private int mTouchSlop;
    private Scroller mScroller;
    private ScrollListener mListener;

    private int downX;
    private int downY;
    private int tempX;
    private int tempY;

    private int viewWidth;
    private int viewHeight;
    private boolean isSilding;
    private boolean isFinish;

    /**
     * 当前正在水平拖拽的标记
     */
    private boolean mIsHorizontalDrag = false;

    /**
     * 当前正在竖直拖拽的标记
     */
    private boolean mIsVerticalDrag = false;

    public SwipeDirectionLayout(Context context) {
        super(context);
    }

    public SwipeDirectionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);

    }


    private void resetFlags() {
        mIsHorizontalDrag = false;
        mIsVerticalDrag = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                downY = tempY = (int) ev.getRawY();
                resetFlags();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                int moveY = (int) ev.getRawY();
                if (Math.abs(moveX - downX) > mTouchSlop) {
                    return true;
                }

                if (downY - moveY > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int moveY = (int) event.getRawY();
                int deltaX = tempX - moveX;
                int deltaY = tempY - moveY;
                tempX = moveX;
                tempY = moveY;

                if (mIsVerticalDrag) {
                    if (downY - moveY > mTouchSlop) {
                        isSilding = true;
                    }
                    if (downY - moveY >= 0 && isSilding) {
                        scrollBy(0, deltaY);
                    }
                } else if (mIsHorizontalDrag) {
                    if (Math.abs(moveX - downX) > mTouchSlop) {
                        isSilding = true;
                    }

                    if (Math.abs(moveX - downX) >= 0 && isSilding) {
                        scrollBy(deltaX, 0);
                    }
                } else {
                    //滑动冲突处理，水平滑动距离跟垂直距离的比较
                    if (Math.abs(deltaY) > Math.abs(deltaX)
                            && downY - moveY > mTouchSlop && downY - moveY >= 0) {
                        mIsVerticalDrag = true;
                        scrollBy(0, deltaY);
                    } else if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(moveX - downX) > mTouchSlop
                            && Math.abs(moveX - downX) >= 0) {
                        mIsHorizontalDrag = true;
                        scrollBy(deltaX, 0);
                    } else {
                        Log.d("sss", "don't drag.");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isSilding = false;
                if (getScrollX() <= -viewWidth / 3) {
                    isFinish = true;
                    scrollRight();
                } else if (getScrollX() >= viewWidth / 3) {
                    isFinish = true;
                    scrollLeft();
                } else if (getScrollY() >= viewHeight / 4) {
                    isFinish = true;
                    scrollTop();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }

        return true;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            viewWidth = this.getWidth();
            viewHeight = this.getHeight();
        }
    }

    /**
     * scrollTop
     */
    private void scrollTop() {
        final int delta = (viewHeight - getScrollY());
        if (mScroller != null) {
            mScroller.startScroll(0, getScrollY(), 0, delta - 1, Math.abs(delta));
        }
        postInvalidate();
    }

    /**
     * scrollLeft
     */
    private void scrollLeft() {
        final int delta = (viewWidth - getScrollX());
        if (mScroller != null) {
            mScroller.startScroll(getScrollX(), 0, delta - 1, 0, Math.abs(delta));
        }
        postInvalidate();
    }

    /**
     * scrollRight
     */
    public void scrollRight() {
        final int delta = (viewWidth + getScrollX());
        if (mScroller != null)
            mScroller.startScroll(getScrollX(), 0, -delta + 1, 0,
                    Math.abs(delta));

        postInvalidate();
    }

    /**
     * scrollOriginal
     */
    public void scrollOrigin() {
        int delta = getScrollX();
        if (mScroller != null)
            mScroller.startScroll(getScrollX(), 0, -delta, 0,
                    Math.abs(delta));

        postInvalidate();
    }


    @Override
    public void computeScroll() {
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                postInvalidate();

                if (mScroller.isFinished() && isFinish) {
                    //Log.w("REX", "[computeScroll] finished");
                    if (mListener != null)
                        mListener.onScrollFinished();
                }
            }
        }
    }

    public void setScrollListener(ScrollListener listener) {
        mListener = listener;
    }

    public interface ScrollListener {
        void onScrollFinished();
    }

}
