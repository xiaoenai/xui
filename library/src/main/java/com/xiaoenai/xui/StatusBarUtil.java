package com.xiaoenai.xui;

import android.content.Context;
import android.os.Build;

public class StatusBarUtil {

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {

            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25)
                    * context.getResources().getDisplayMetrics().density);
        }
    }
}
