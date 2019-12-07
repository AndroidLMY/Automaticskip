package com.androidlmy.automaticskip;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

/**
 * @功能:
 * @Creat 2019/12/2 10:16
 * @User Lmy
 * @Compony zaituvideo
 */
public class SkipService extends AccessibilityService {
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        //配置监听的事件类型为界面变化|点击事件
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_VIEW_CLICKED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        if (Build.VERSION.SDK_INT >= 16) {
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        }
        setServiceInfo(config);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final AccessibilityNodeInfo nodeInfo = event.getSource();//当前界面的可访问节点信息
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {//界面变化事件
            ComponentName componentName = new ComponentName(event.getPackageName().toString(), event.getClassName().toString());
            ActivityInfo activityInfo = tryGetActivity(componentName);
            boolean isActivity = activityInfo != null;

            if (isActivity) {
                Log.d("WindowChange", "当前运行包名" + nodeInfo.getPackageName());
                switch (nodeInfo.getPackageName().toString()) {
                    case "com.netease.cloudmusic":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                skip(nodeInfo.findAccessibilityNodeInfosByViewId("com.netease.cloudmusic:id/c3l"));
                            }
                        }, 500);//0.5秒后执行Runnable中的run方法
                        break;
                    case "com.sina.weibo":
                        break;
                    case "com.eric.ontheway":
                        skip(nodeInfo.findAccessibilityNodeInfosByViewId("com.eric.ontheway:id/tv_time"));
                        break;
                    case "com.walixiwa.flash.player":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                skip(nodeInfo.findAccessibilityNodeInfosByViewId("com.walixiwa.flash.player:id/tt_splash_skip_btn"));
                            }
                        }, 500);//0.5秒后执行Runnable中的run方法
                        break;
                    case "com.huawei.appmarket":
                        skip(nodeInfo.findAccessibilityNodeInfosByViewId("com.huawei.appmarket:id/skip_textview"));
                        break;
                    case "com.meizu.media.life":
                        skip(nodeInfo.findAccessibilityNodeInfosByViewId("com.meizu.media.life:id/tvTimer"));
                        break;
                    case "com.qihoo.appstore":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                skip(nodeInfo.findAccessibilityNodeInfosByViewId("com.qihoo.plugin.splash:id/ae"));
                            }
                        }, 2000);//0.5秒后执行Runnable中的run方法
                        break;
                    case "cn.xiaochuankeji.zuiyouLite":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                skip(nodeInfo.findAccessibilityNodeInfosByViewId("cn.xiaochuankeji.zuiyouLite:id/btn_skip"));
                            }
                        }, 2000);//0.5秒后执行Runnable中的run方法
                        break;
                    case "com.tencent.qqlive":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                List<AccessibilityNodeInfo> nodeInfoList = nodeInfo.findAccessibilityNodeInfosByText("跳过");
                                for (AccessibilityNodeInfo info : nodeInfoList) {
                                    CharSequence charSequence = info.getText();
                                    if (charSequence != null) {
                                        String msg = charSequence.toString();
                                        if (msg.contains("跳过")) {
                                            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            Toast.makeText(getApplicationContext(), "跳过广告", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }, 2500);//0.5秒后执行Runnable中的run方法
                        break;


                    case "com.eg.android.AlipayGphone":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                skip(nodeInfo.findAccessibilityNodeInfosByViewId("com.alipay.android.phone.openplatform:id/saoyisao_tv"));
                            }
                        }, 2000);//0.5秒后执行Runnable中的run方法
                        break;
                    default: {
                        List<AccessibilityNodeInfo> nodeInfoList = nodeInfo.findAccessibilityNodeInfosByText("跳过");
                        for (AccessibilityNodeInfo info : nodeInfoList) {
                            CharSequence charSequence = info.getText();
                            if (charSequence != null) {
                                String msg = charSequence.toString();
                                if (msg.contains("跳过")) {
                                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    Toast.makeText(this, "跳过广告", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void skip(List<AccessibilityNodeInfo> nodeInfoList) {
        Log.d("WindowChange", "数据" + nodeInfoList.size());
        if (nodeInfoList != null && nodeInfoList.size() > 0) {
            nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Toast.makeText(getApplicationContext(), "跳过广告", Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {
    }
}
