package com.sample_mvvm.define;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;

import com.sample_mvvm.R;
import com.sample_mvvm.util.CropperActivity;

import static com.sample_mvvm.define.Constant.REQUEST_CODE_VIDEO_CAPTURE;
import static com.sample_mvvm.define.Constant.REQUEST_PERMISSION_SETTING;
import static com.sample_mvvm.define.Constant.USER_AVATAR_CIRCLE_CROP;


public class Navigator {

    // TODO ******************************* GENERAL NAVIGATOR *************************************

    public static void openActivity(Activity activity, Class destination) {
        Intent intent = new Intent(activity, destination);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        activity.finish();
    }

    public static void openActivity(Activity activity, Class destination, boolean notFinish) {
        Intent intent = new Intent(activity, destination);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static void openActivityPassBundle(Activity activity, Class destination, Bundle bundle) {
        Intent intent = new Intent(activity, destination);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        activity.finish();
    }

    public static void openActivityForResult(Activity activity, Class destination, int requestCode) {
        Intent intent = new Intent(activity, destination);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    // TODO ******************************* OTHER NAVIGATOR ***************************************

    public static void openCameraActivity(Activity activity, Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        activity.startActivityForResult(intent, REQUEST_CODE_VIDEO_CAPTURE);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void openCropperActivity(Activity activity, String avatarPath) {
        Intent intent = new Intent(activity, CropperActivity.class);
        intent.putExtra("path", avatarPath);
        activity.startActivityForResult(intent, USER_AVATAR_CIRCLE_CROP);
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static void openAppSettingPermission(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }


}
