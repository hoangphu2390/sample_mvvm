package com.sample_mvvm.define.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sample_mvvm.util.NetworkUtils;

import static com.sample_mvvm.define.Constant.ACTION_CONNECTION;
import static com.sample_mvvm.define.Constant.ACTION_WIFI_CONECTION;

public class ConnectionService {

    private UpdateReceiver m_updateReceiver;
    private Context m_context;
    private postResultConnection m_listenner;
    private TextView tvConnection;
    private FrameLayout frameContainer;

    public interface postResultConnection {
        void postResultConnection(boolean isConnection);
    }

    // USE FOR LIST ACTIVITY IN APPLICATION.
    public ConnectionService(Context m_context, postResultConnection m_listenner) {
        this.m_context = m_context;
        this.m_listenner = m_listenner;
    }

    // USE FOR LIST FRAGMENT OF MAIN ACTIVITY.
    public ConnectionService(Context m_context, TextView tvConnection, FrameLayout frameContainer,
                             postResultConnection m_listenner) {
        this.m_context = m_context;
        this.tvConnection = tvConnection;
        this.frameContainer = frameContainer;
        this.m_listenner = m_listenner;
    }

    public void registerBroadcastConnection() {
        if (m_updateReceiver == null) {
            m_updateReceiver = new UpdateReceiver() {
                @Override
                public void handleConnection(int statusConnection) {
                    if (statusConnection == NetworkUtils.NETWORK_STATUS_NOT_CONNECTED) {
                        setupUI(false);
                    } else {
                        setupUI(true);
                    }
                }
            };
        }
        IntentFilter intentFilter = new IntentFilter(ACTION_WIFI_CONECTION);
        intentFilter.addAction(ACTION_CONNECTION);
        m_context.registerReceiver(m_updateReceiver, intentFilter);
    }


    public void unregisterBroadcastConnection(Context context) {
        if (m_updateReceiver != null) {
            context.unregisterReceiver(m_updateReceiver);
            m_updateReceiver = null;
        }
    }

    public static class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int statusConnection = NetworkUtils.getConnectivityStatusString(context);
            handleConnection(statusConnection);
        }

        public void handleConnection(int statusConnection) {
        }
    }

    private void setupUI(boolean isConnected) {
        if (tvConnection != null)
            tvConnection.setVisibility(isConnected ? View.GONE : View.VISIBLE);
        if (frameContainer != null)
            frameContainer.setVisibility(isConnected ? View.VISIBLE : View.GONE);
        m_listenner.postResultConnection(isConnected ? true : false);
    }
}
