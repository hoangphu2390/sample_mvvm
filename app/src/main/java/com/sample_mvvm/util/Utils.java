package com.sample_mvvm.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.MailTo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sample_mvvm.R;
import com.sample_mvvm.define.MyApplication;
import com.sample_mvvm.define.service.ConnectionService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class Utils {
    private static int screenWidth = 0;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static void hideSoftKeyboard(Activity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(Activity context, View aView) {
        new Handler().postDelayed(() -> {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(aView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }, 300);
    }

    public static void hideKeyboard(Activity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static boolean isCheckShowSoftKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    //CHECK EMAIL IS VALID
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void loadAvatarFromURL(Context context, String image_path, CircleImageView imageView) {
        if (image_path == null || image_path.isEmpty()) {
            Picasso.with(context).load(R.drawable.demo_avatar).into(imageView);
            return;
        }
        try {
            Picasso.with(context).load(image_path)
                    .placeholder(R.drawable.demo_upload)
                    .error(R.drawable.demo_upload).fit().centerInside().into(imageView);
        } catch (IllegalArgumentException ex) {

        }
    }

    public static void loadImageFromURL(Context context, String image_path, ImageView imageView) {
        if (image_path == null || image_path.isEmpty()) return;
        Picasso.with(context).load(image_path)
                .placeholder(R.color.color_gray09)
                .error(R.color.color_gray09).fit().centerInside().into(imageView);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isEmailFormat(String emailString) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
    }

    public static ConnectionService initConnectionService(Context context, TextView tv_connection,
                                                          FrameLayout frame_container,
                                                          ConnectionService.postResultConnection listener) {
        ConnectionService connectionService = new ConnectionService(context, tv_connection, frame_container, listener);
        connectionService.registerBroadcastConnection();
        return connectionService;
    }


    public static ConnectionService initConnectionService(Context context, ConnectionService.postResultConnection listener) {
        ConnectionService connectionService = new ConnectionService(context, listener);
        connectionService.registerBroadcastConnection();
        return connectionService;
    }

    public static void destroyConnectionService(Context context, ConnectionService connectionService) {
        if (connectionService == null) return;
        connectionService.unregisterBroadcastConnection(context);
    }

    public static void setTimeOutDialog(Dialog dialog, int time_out) {
        Handler h = new Handler();
        h.postDelayed(() -> dialog.dismiss(), time_out);
    }

    public static String getDurationVideo(Context context, Uri uri) {
        MediaPlayer mp = MediaPlayer.create(context, uri);
        int duration = mp.getDuration();
        mp.release();
        return String.format("%d", TimeUnit.MILLISECONDS.toSeconds(duration));
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static double getDistanceLocation(Location source, Location destination) {
        return (source.distanceTo(destination)) * 0.000621371192;
    }

    private static String getAdressFromLocation(Context context, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> list_address;
        String address = "";
        try {
            geocoder = new Geocoder(context, Locale.getDefault());
            list_address = geocoder.getFromLocation(latitude, longitude, 1);
            if (list_address != null && list_address.size() > 0) {
                address = list_address.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static class MyWebViewClient extends WebViewClient {
        private final WeakReference<Activity> m_activityRef;
        private ProgressBar m_progressBar;
        private WebView m_webView;

        public MyWebViewClient(Activity activity, WebView webView, ProgressBar progressBar) {
            m_activityRef = new WeakReference<>(activity);
            this.m_progressBar = progressBar;
            this.m_webView = webView;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            m_progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            m_progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("tel:")) {
                final Activity activity = m_activityRef.get();
                if (activity != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    activity.startActivity(intent);
                    view.reload();
                    return true;
                }
            }

            if (url.startsWith("mailto:")) {
                final Activity activity = m_activityRef.get();
                if (activity != null) {
                    MailTo mt = MailTo.parse(url);
                    Intent i = newEmailIntent(activity, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                    activity.startActivity(i);
                    view.reload();
                    return true;
                }
            } else {
                view.loadUrl(url);
            }
            return true;
        }


        private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", address, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
            return emailIntent;
        }
    }

    public static Bitmap getBitmapFromUrl(String link) {
        try {
            URL url = new URL(link);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bitmap;
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    private static double latRad(double lat) {
        double sin = Math.sin(lat * Math.PI / 180);
        double radX2 = Math.log((1 + sin) / (1 - sin)) / 2;
        return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;
    }

    private static double zoom(double mapPx, double worldPx, double fraction) {
        final double LN2 = .693147180559945309417;
        return (Math.log(mapPx / worldPx / fraction) / LN2);
    }

    public static Bitmap resizeMapIcons(Context context, int width, int height, String image_name) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(),
                context.getResources().getIdentifier(image_name, "drawable", context.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public static int getSoftButtonsBarSizePort(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    public static void checkSoftButtonsBar(Activity activity) {
        int height = Utils.getSoftButtonsBarSizePort(activity);
        if (height == 0) return;
        Window w = activity.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public static void setFitsSystemWindows(Activity activity, View view) {
        if (Utils.getSoftButtonsBarSizePort(activity) > 0) {
            view.setFitsSystemWindows(true);
        }
    }

    public static void showToast(String message) {
        Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_LONG).show();
    }
}

