package com.huawei.hapticskitdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.huawei.devices.hapticskit.HapticsKit;
import com.huawei.devices.hapticskit.HapticsKitAdapter;
import com.huawei.devices.utils.HapticsKitConstant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    HapticsKit mHapkit;
    HapticsKitAdapter mHapticsAdapter;
    private ListView mListView;
    private List<HapticsData> mHapticsDataList = new ArrayList<>();
    private Button query_btn;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        query_btn = (Button) findViewById(R.id.query_btn);
        text = (TextView) findViewById(R.id.textView);
        text.setText(this.getPackageName());
        mHapkit = new HapticsKit(this);
        mHapticsAdapter = mHapkit.initialize(1);
        query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = mHapticsAdapter.getParameter(HapticsKitConstant.HW_HAPTIC_DIRECTION_VALUE);
                text.setText(result);
            }
        });
        initData();
        mListView = (ListView) findViewById(R.id.set_list_view);

        ListViewAdapter viewAdapter = new ListViewAdapter(MainActivity.this, R.layout.item_layout, mHapticsDataList);
        mListView.setAdapter(viewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HapticsData bean = mHapticsDataList.get(position);
                Log.e("MainActity", bean.getHapticsType());
                mHapticsAdapter.setParameter(bean.getHapticsType());
                text.setText(bean.getHapticsType());
            }
        });
    }

    private void initData() {
        addData("Camera_enum.CLICK", HapticsKitConstant.CameraTypeEnum.CLICK.getType());
        addData("Camera_enum.CLICK_UP", HapticsKitConstant.CameraTypeEnum.CLICK_UP.getType());
        addData("Camera_enum.FOCUS", HapticsKitConstant.CameraTypeEnum.FOCUS.getType());
        addData("Camera_enum.GEAR_SLIP", HapticsKitConstant.CameraTypeEnum.GEAR_SLIP.getType());
        addData("Camera_enum.LONG_PRESS", HapticsKitConstant.CameraTypeEnum.LONG_PRESS.getType());
        addData("Camera_enum.MODE_SWITCH", HapticsKitConstant.CameraTypeEnum.MODE_SWITCH.getType());
        addData("Camera_enum.PORTAIT_SWITH", HapticsKitConstant.CameraTypeEnum.PORTAIT_SWITH.getType());
        addData("Battery.CHARGING", HapticsKitConstant.BatteryTypeEnum.CHARGING.getType());
        addData("Calculator.DELETE", HapticsKitConstant.CalculatorTypeEnum.DELETE_LONG_PRESS.getType());
        addData("Calculator.VITUAL_TASK", HapticsKitConstant.CalculatorTypeEnum.VITUAL_TASK.getType());
        addData("Clock.STOPWATCH", HapticsKitConstant.ClockTypeEnum.STOPWATCH.getType());
        addData("Clock.Timer", HapticsKitConstant.ClockTypeEnum.TIMER.getType());
        addData("Contact.DELTE", HapticsKitConstant.DiallerTypeEnum.DELTE.getType());
        addData("Contact.LETTERS_INDEX", HapticsKitConstant.DiallerTypeEnum.LETTERS_INDEX.getType());
        addData("Contact.LONGPRESS", HapticsKitConstant.DiallerTypeEnum.LONGPRESS.getType());
        addData("Contact.CLICK", HapticsKitConstant.DiallerTypeEnum.CLICK.getType());
        addData("Control.SEARCH_LONG_PRESS", HapticsKitConstant.ControlTypeEnum.SEARCH_LONG_PRESS.getType());
        addData("Control.TEXT_CHOSE", HapticsKitConstant.ControlTypeEnum.TEXT_CHOSE_CURSOR_MOVE.getType());
        addData("Control.TEXT_EDIT", HapticsKitConstant.ControlTypeEnum.TEXT_EDIT.getType());
        addData("Control.WIDGET", HapticsKitConstant.ControlTypeEnum.WIDGET_OPERATION.getType());
        addData("Desktop.LONG_PRESS", HapticsKitConstant.DesktopTypeEnum.LONG_PRESS.getType());
        addData("FingerPrint.INPUT", HapticsKitConstant.FingerPrintTypeEnum.INPUT_LONG_PRESS.getType());
        addData("FingerPrint.UNLOCK_FAILE", HapticsKitConstant.FingerPrintTypeEnum.UNLOCK_FAILE.getType());
        addData("Gallery.ALBUMS", HapticsKitConstant.GalleryTypeEnum.ALBUMS_LONG_PRESS.getType());
        addData("Gallery.PHOTO", HapticsKitConstant.GalleryTypeEnum.PHOTOS_LONG_PRESS.getType());
        addData("Gallery.UPGLIDES", HapticsKitConstant.GalleryTypeEnum.UPGLIDE_RELATED.getType());
        addData("Home.CLICK_BACK.", HapticsKitConstant.NavigationTypeEnum.CLICK_BACK.getType());
        addData("Home.CLICK_HOME.", HapticsKitConstant.NavigationTypeEnum.CLICK_HOME.getType());
        addData("Home.CLICK_MULTITASK.", HapticsKitConstant.NavigationTypeEnum.CLICK_MULTITASK.getType());
        addData("LockScreen.FACE_UNLOCK_FAIL.", HapticsKitConstant.LockScreenTypeEnum.FACE_UNLOCK_FAIL.getType());
        addData("LockScreen.FACE_UNLOCK_RETRY.", HapticsKitConstant.LockScreenTypeEnum.FACE_UNLOCK_RETRY.getType());
        addData("LockScreen.NUMBER_UNLOCK_FAIL.", HapticsKitConstant.LockScreenTypeEnum.NUMBER_UNLOCK_FAIL.getType());
        addData("LockScreen.UNLOCK_CLICK.", HapticsKitConstant.LockScreenTypeEnum.UNLOCK_CLICK.getType());
        addData("LockScreen.ONEHAND.", HapticsKitConstant.LockScreenTypeEnum.ONEHAND_KEYBOARD_SWITCH.getType());
        addData("LockScreen.UNLOCK_SLIP.", HapticsKitConstant.LockScreenTypeEnum.UNLOCK_SLIP.getType());
        addData("LockScreen.UPGLIDE_SWITCH.", HapticsKitConstant.LockScreenTypeEnum.UPGLIDE_SWITCH.getType());
        addData("SystemUI.EXPAND.", HapticsKitConstant.SystemuiTypeEnum.EXPAND.getType());
        addData("SystemUI.MOVE.", HapticsKitConstant.SystemuiTypeEnum.MOVE.getType());
        addData("SystemUI.NO_LONG_PRESS.", HapticsKitConstant.SystemuiTypeEnum.NOTIFICATIONS_LONG_PRESS.getType());
        addData("SystemUI.SCREEN_RECORD.", HapticsKitConstant.SystemuiTypeEnum.SCREEN_RECORD_STOP.getType());
        addData("SystemUI.SWITCH_PRESS.", HapticsKitConstant.SystemuiTypeEnum.SWITCH_LONG_PRESS.getType());
        addData("SystemUI.SWITCH_SORT_PRESS.", HapticsKitConstant.SystemuiTypeEnum.SWITCH_SORT_LONG_PRESS.getType());
        addData("Wallet.TIME_SCROLL.", HapticsKitConstant.WalletTypeEnum.TIME_SCROLL.getType());

        addData("Keyboard input strength 1", HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH1);
        addData("Keyboard input strength 2", HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH2);
        addData("Keyboard input strength 3", HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH3);
        addData("Keyboard input strength 4", HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH4);
        addData("Keyboard input strength 5", HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH5);
    }

    private void addData(String name, String type) {
        HapticsData data = new HapticsData(name, type);
        mHapticsDataList.add(data);
    }

    private class HapticsData {
        private String mHapticsType;
        private String mName;

        public HapticsData(String name, String type) {
            this.mHapticsType = type;
            this.mName = name;
        }

        public String getHapticsType() {
            return mHapticsType;
        }

        public String getName() {
            return mName;
        }
    }

    private class ListViewAdapter extends ArrayAdapter<HapticsData> {
        private int mResourceId;

        public ListViewAdapter(Context context, int resourceId, List<HapticsData> dataList) {
            super(context, resourceId, dataList);
            mResourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HapticsData data = getItem(position);
            View view;
            HapticsHolder holder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(mResourceId, parent, false);
                holder = new HapticsHolder();
                holder.hapticBtn = view.findViewById(R.id.hapticBtn);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (HapticsHolder) view.getTag();
            }
            holder.hapticBtn.setText(data.getHapticsType());
            return view;
        }

        class HapticsHolder {
            Button hapticBtn;
        }
    }
}
