/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.policy;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.CharacterStyle;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.BatteryManager;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.systemui.R;

public class BatteryController extends LinearLayout {
    private static final String TAG = "StatusBar.BatteryController";

    private Context mContext;
    private ArrayList<ImageView> mIconViews = new ArrayList<ImageView>();
    private ArrayList<TextView> mLabelViews = new ArrayList<TextView>();

    private ImageView mBatteryIcon;

    private static int mBatteryStyle;


    private int mLevel = -1;
    private boolean mPlugged = false;

    public static final int STYLE_STOCK = 0;
    public static final int STYLE_ICS = 1;
    public static final int STYLE_VERTICAL = 2;
    public static final int STYLE_BALL = 3;
    public static final int STYLE_CIRCLE = 4;
    public static final int STYLE_ANIMATED = 5;
    public static final int STYLE_HIDE = 6;

    public BatteryController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
        mBatteryIcon = (ImageView) findViewById(R.id.battery);
        addIconView(mBatteryIcon);

        SettingsObserver settingsObserver = new SettingsObserver(new Handler());
        settingsObserver.observe();
        updateSettings(); // to initialize values

    }

    private void init() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(mBatteryBroadcastReceiver, filter);
    }

    public void addIconView(ImageView v) {
        mIconViews.add(v);
    }

    public void addLabelView(TextView v) {
        mLabelViews.add(v);
    }

    private BroadcastReceiver mBatteryBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                final int level = intent.getIntExtra(
                        BatteryManager.EXTRA_LEVEL, 0);
                final boolean plugged = intent.getIntExtra(
                        BatteryManager.EXTRA_PLUGGED, 0) != 0;
                setBatteryIcon(level, plugged);
            }
        }
    };

    private void setBatteryIcon(int level, boolean plugged) {
        mLevel = level;
        mPlugged = plugged;
        ContentResolver cr = mContext.getContentResolver();
        mBatteryStyle = Settings.System.getInt(cr,
                Settings.System.BATTERY_ICON, 0);
        int icon;
        if (mBatteryStyle == STYLE_STOCK) {
            icon = plugged ? R.drawable.stat_sys_battery_charge
                    : R.drawable.stat_sys_battery;
        } else if (mBatteryStyle == STYLE_ICS) {
            icon = plugged ? R.drawable.stock_sys_battery_charge
                    : R.drawable.stock_sys_battery;
        } else if (mBatteryStyle == STYLE_VERTICAL) {
            icon = plugged ? R.drawable.stat_vertical_battery_charge
                    : R.drawable.stat_vertical_battery;
        } else if (mBatteryStyle == STYLE_BALL) {
            icon = plugged ? R.drawable.stat_ball_battery_charge
                    : R.drawable.stat_ball_battery;
        } else if (mBatteryStyle == STYLE_CIRCLE) {
            icon = plugged ? R.drawable.stat_bcircle_battery_charge
                    : R.drawable.stat_bcircle_battery;
        } else if (mBatteryStyle == STYLE_ANIMATED) {
            icon = plugged ? R.drawable.stat_animate_battery_charge
                    : R.drawable.stat_animate_battery;
       } else {
            icon = plugged ? R.drawable.stat_sys_battery_charge
                    : R.drawable.stat_sys_battery;
        }
        int N = mIconViews.size();
        for (int i = 0; i < N; i++) {
            ImageView v = mIconViews.get(i);
            v.setImageResource(icon);
            v.setImageLevel(level);
            v.setContentDescription(mContext.getString(
                    R.string.accessibility_battery_level, level));
        }
        N = mLabelViews.size();
        for (int i = 0; i < N; i++) {
            TextView v = mLabelViews.get(i);
            v.setText(mContext.getString(
                    R.string.status_bar_settings_battery_meter_format, level));
        }
    }

    class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(Settings.System
                    .getUriFor(Settings.System.BATTERY_ICON), false,
                    this);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateSettings();
        }
    }

    private void updateSettings() {
        // Slog.i(TAG, "updated settings values");
        ContentResolver cr = mContext.getContentResolver();
        mBatteryStyle = Settings.System.getInt(cr,
                Settings.System.BATTERY_ICON, 0);

        switch (mBatteryStyle) {
            case STYLE_STOCK:;
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_ICS:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_VERTICAL:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_BALL:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_CIRCLE:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_ANIMATED:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_HIDE:
                mBatteryIcon.setVisibility(View.GONE);
                setVisibility(View.GONE);
                break;
            default:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
        }

        setBatteryIcon(mLevel, mPlugged);

    }
}
