package com.zhw.swiplayout;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class MainActivity extends Activity {

    private Button mButton;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        mButton = findViewById(R.id.btn_show);
    }


    private void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification();
            }
        });
    }

    private void showNotification() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        View view = LayoutInflater.from(this).inflate(R.layout.view_notification, null, false);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim);
        final View decorView = this.getWindow().getDecorView();
        decorView.post(new Runnable() {
            @Override
            public void run() {
                mPopupWindow.showAtLocation(decorView, Gravity.TOP, 0, 0);
            }
        });
        autoDismissWindow(view);
    }


    private void autoDismissWindow(View decorView) {
        if (mPopupWindowRunnable != null) {
            decorView.removeCallbacks(mPopupWindowRunnable);
        }
        mPopupWindowRunnable = new PopupWindowRunnable();
        decorView.postDelayed(mPopupWindowRunnable, 6 * 1000);
    }

    private PopupWindowRunnable mPopupWindowRunnable;

    private class PopupWindowRunnable implements Runnable {

        PopupWindowRunnable() {
        }

        @Override
        public void run() {
            if (mPopupWindow == null) {
                return;
            }
            if (!mPopupWindow.isShowing()) {
                return;
            }
            if (isFinishing()) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (isDestroyed()) {
                    return;
                }
            }

            mPopupWindow.dismiss();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseWindow();
    }

    public void releaseWindow() {
        if (mPopupWindow == null) {
            return;
        }
        if (!mPopupWindow.isShowing()) {
            return;
        }
        if (isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (isDestroyed()) {
                return;
            }
        }
        mPopupWindow.dismiss();
        mPopupWindow = null;
    }
}
