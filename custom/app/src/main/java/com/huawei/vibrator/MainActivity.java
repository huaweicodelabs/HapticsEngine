/*
 *Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.huawei.vibrator;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.devices.hapticsengine.HapticAttributes;
import com.huawei.devices.hapticsengine.HapticChannel;
import com.huawei.devices.hapticsengine.HapticCurve;
import com.huawei.devices.hapticsengine.HapticPlayer;
import com.huawei.devices.hapticsengine.HapticWave;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class show the haptic demo.
 *
 * @since 2021-01-16
 */
public class MainActivity extends AppCompatActivity implements HapticPlayer.OnPlayStateListener {
    private static final String TAG = "DemoVibrator";

    private boolean isLooping = false;

    private boolean isPlaying = false;

    private Button btnJsonParse;

    private Button btnDefine;

    private TextView textParse;

    private TextView textDefine;

    private HapticPlayer mHapticPlayer;

    private Button btnPlay;

    private Button btnLooping;

    private TextView textPlay;

    private Button btnSetDynamic;

    private TextView textDynamicParaResult;

    private Button btnIntensity;

    private Button btnSharpness;

    private EditText etIntensityTime;

    private EditText etIntensityPara;

    private EditText etSharpnessTime;

    private EditText etSharpnessPara;

    private Button btnQueryLoop;

    private Button btnQueryPlay;

    private TextView tvLooping;

    private Button btnDuration;

    private List<String> intensityParaList = new ArrayList<>();

    private List<String> sharpnessParaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        mHapticPlayer = new HapticPlayer(this);

        mHapticPlayer.setOnPlayStateListener(this);

        // JSON模式
        btnJsonParse.setOnClickListener(view -> {
            boolean parseResult = parseStream("demo.json");
            textParse.setText(String.valueOf(parseResult));
        });

        // 自定义模式
        btnDefine.setOnClickListener(view -> {
            boolean setWaveResult = defineWave();
            textDefine.setText(String.valueOf(setWaveResult));
        });

        // 播放波形
        btnPlay.setOnClickListener(view -> {
            if (!isPlaying) {
                execPlay();
                btnPlay.setText("停止");
                isPlaying = true;
            } else {
                mHapticPlayer.stop();
                btnPlay.setText("播放");
                isPlaying = false;
            }
        });

        // 循环播放
        btnLooping.setOnClickListener(view -> {
            setLooping();
        });

        // 查询播放
        btnQueryLoop.setOnClickListener(view -> {
            boolean isLoop = mHapticPlayer.isLooping();
            tvLooping.setText(String.valueOf(isLoop));
        });

        // 查询状态
        btnQueryPlay.setOnClickListener(view -> {
            boolean isPlay = mHapticPlayer.isPlaying();
            textPlay.setText(String.valueOf(isPlay));
        });

        // 查询时长
        btnDuration.setOnClickListener(view -> {
            searchLastTime();
        });

        // 设置动态强度曲线
        btnIntensity.setOnClickListener(view -> {
            setIntensity();
        });

        // 设置动态锐度曲线
        btnSharpness.setOnClickListener(view -> {
            setSharpness();
        });

        // 执行动态调节
        btnSetDynamic.setOnClickListener(view -> {
            setDynamic();
        });
    }

    private void setLooping() {
        if (!isLooping) {
            btnLooping.setText("单次");
            isLooping = true;
        } else {
            btnLooping.setText("循环");
            isLooping = false;
        }
        mHapticPlayer.setLooping(isLooping);
    }

    private void searchLastTime() {
        int durationResult = mHapticPlayer.getDuration();
        Toast toast = Toast.makeText(getApplicationContext(), "振动时长: " + durationResult, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void setIntensity() {
        String intensityTime = etIntensityTime.getText().toString();
        String intensityPara = etIntensityPara.getText().toString();
        if ("".equals(intensityTime) || "".equals(intensityPara)) {
            Toast.makeText(getApplicationContext(), "输入的参数是空！", Toast.LENGTH_SHORT).show();
            return;
        }
        intensityParaList.add(intensityTime);
        intensityParaList.add(intensityPara);
        Toast.makeText(getApplicationContext(),
                        "time: " + intensityTime + " intensity parameter: " + intensityPara + " 参数列表长度: "
                                + intensityParaList.size() / 2,
                        Toast.LENGTH_SHORT).show();
    }

    private void setSharpness() {
        String sharpnessTime = etSharpnessTime.getText().toString();
        String sharpnessPara = etSharpnessPara.getText().toString();
        if ("".equals(sharpnessTime) || "".equals(sharpnessPara)) {
            Toast.makeText(getApplicationContext(), "输入的参数是空！", Toast.LENGTH_SHORT).show();
            return;
        }
        sharpnessParaList.add(sharpnessTime);
        sharpnessParaList.add(sharpnessPara);
        Toast.makeText(getApplicationContext(),
                        "time: " + sharpnessTime + " sharpness parameter: " + sharpnessPara + " 参数列表长度: "
                                + sharpnessParaList.size() / 2,
                        Toast.LENGTH_SHORT).show();
    }

    private void setDynamic() {
        HapticCurve.Builder curveIntensityBuilder = new HapticCurve.Builder();
        for (int index = 0; index < intensityParaList.size(); index += 2) {
            int intensityTime = Integer.parseInt(intensityParaList.get(index));
            float intensityPara = Float.parseFloat(intensityParaList.get(index + 1));
            HapticCurve.AdjustPoint adjustPoint = new HapticCurve.AdjustPoint(intensityTime, intensityPara);
            curveIntensityBuilder.addAdjustPoint(adjustPoint);
            Log.i(TAG, "intensity time: " + intensityTime + " parameter: " + intensityPara);
        }
        HapticCurve.Builder curveSharpnessBuilder = new HapticCurve.Builder();
        for (int index = 0; index < sharpnessParaList.size(); index += 2) {
            int sharpnessTime = Integer.parseInt(sharpnessParaList.get(index));
            float sharpnessPara = Float.parseFloat(sharpnessParaList.get(index + 1));
            HapticCurve.AdjustPoint adjustPoint = new HapticCurve.AdjustPoint(sharpnessTime, sharpnessPara);
            curveSharpnessBuilder.addAdjustPoint(adjustPoint);
            Log.i(TAG, "sharpness time: " + sharpnessTime + " parameter: " + sharpnessPara);
        }
        boolean isIntensity = false;
        boolean isSharpness = false;
        if (curveIntensityBuilder != null) {
            HapticCurve curveIntensity = curveIntensityBuilder.build();
            isIntensity = mHapticPlayer.setDynamicCurve(HapticCurve.CURVE_TYPE_INTENSITY,
                    HapticChannel.HAPTIC_CHANNEL_A, curveIntensity);
        }

        if (curveSharpnessBuilder != null) {
            HapticCurve curveSharpness = curveSharpnessBuilder.build();
            isSharpness = mHapticPlayer.setDynamicCurve(HapticCurve.CURVE_TYPE_SHARPNESS,
                    HapticChannel.HAPTIC_CHANNEL_A, curveSharpness);
        }
        Toast.makeText(getApplicationContext(),
                        "动态强度调节结果: " + isIntensity + System.lineSeparator() +
                                "动态锐度调节结果: " + isSharpness, Toast.LENGTH_SHORT).show();
    }

    private boolean defineWave() {
        HapticChannel channel = new HapticChannel.Builder()
                .addSlice(0, HapticChannel.SLICE_TYPE_CONTINUOUS, 500, 0.25f, 0.3f)
                .addSlice(1000, HapticChannel.SLICE_TYPE_CONTINUOUS, 200, 0.25f, 0.3f)
                .addSlice(3000, HapticChannel.SLICE_TYPE_CONTINUOUS, 500, 0.45f, 0.9f)
            .setIntensityCurve(new HapticCurve.Builder().addAdjustPoint(300, 0.9f).addAdjustPoint(1000, 0.2f).build())
            .setSharpnessCurve(new HapticCurve.Builder().addAdjustPoint(300, 0.9f).addAdjustPoint(1000, 0.2f).build())
            .setChannelId(HapticChannel.HAPTIC_CHANNEL_A)
            .build();
        HapticWave wave = new HapticWave();
        wave.addHapticChannel(channel);
        HapticAttributes attr = new HapticAttributes();
        attr.setUsage(HapticAttributes.USAGE_GAME);
        return mHapticPlayer.setHapticWave(attr, wave);
    }

    private void execPlay() {
        int ret = mHapticPlayer.play();
        Toast.makeText(getApplicationContext(), "开始播放" + ret, Toast.LENGTH_SHORT).show();
    }

    private void findView() {
        btnJsonParse = findViewById(R.id.btnParseJson);
        btnDefine = findViewById(R.id.btnDefine);
        textParse = findViewById(R.id.tv_parseResult);
        textDefine = findViewById(R.id.tv_defineResult);
        btnPlay = findViewById(R.id.btn_play);
        btnLooping = findViewById(R.id.btn_looping);
        textPlay = findViewById(R.id.tv_play);
        btnSetDynamic = findViewById(R.id.btnSetDynamic);
        textDynamicParaResult = findViewById(R.id.tv_dynamicResult);
        btnIntensity = findViewById(R.id.btnIntensity);
        btnSharpness = findViewById(R.id.btnSharpness);
        etIntensityTime = findViewById(R.id.et_intensityTime);
        etIntensityPara = findViewById(R.id.et_intensityPara);
        etSharpnessTime = findViewById(R.id.et_sharpnessTime);
        etSharpnessPara = findViewById(R.id.et_sharpnessPara);
        btnQueryLoop = findViewById(R.id.btn_queryLoop);
        tvLooping = findViewById(R.id.tv_Looping);
        btnQueryPlay = findViewById(R.id.btn_queryPlay);
        btnDuration = findViewById(R.id.btnDuration);
    }

    private boolean parseStream(String fileName) {
        boolean ret = false;
        AssetManager assetManager = getAssets();
        if (assetManager != null) {
            try {
                InputStream inputStream = assetManager.open(fileName);
                HapticAttributes attr = new HapticAttributes();
                attr.setUsage(HapticAttributes.USAGE_GAME);
                ret = mHapticPlayer.setHapticWave(attr, inputStream);
            } catch (IOException e) {
                Log.e(TAG, "Failed to open file");
                return false;
            }
        }
        return ret;
    }

    @Override
    public void onState(int state, int errorCode) {
        Log.i(TAG, "OnPlayStateListener state: " + state + " errorCode: " + errorCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHapticPlayer.stop();
    }
}