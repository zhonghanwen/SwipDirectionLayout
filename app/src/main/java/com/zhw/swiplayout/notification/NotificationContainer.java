package com.zhw.swiplayout.notification;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zhw.swiplayout.R;

/**
 * Created by zhonghanwen on 2019-01-23.
 */
public class NotificationContainer extends ConstraintLayout {

    private View mRootView;

    public NotificationContainer(Context context) {
        this(context, null);
    }

    public NotificationContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.view_notification_item, this);
    }
}
