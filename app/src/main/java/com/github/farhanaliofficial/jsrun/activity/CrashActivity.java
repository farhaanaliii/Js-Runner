package com.github.farhanaliofficial.jsrun.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import android.support.v7.app.AppCompatActivity;

public class CrashActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    public static final String EXTRA_CRASH_INFO = "crashInfo";
    public static String mLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_DeviceDefault);
        mLog = getIntent().getStringExtra(EXTRA_CRASH_INFO);
        //stt = mLog;
        setContentView: {
            ScrollView contentView = new ScrollView(this);
            contentView.setFillViewport(true);
            HorizontalScrollView hw = new HorizontalScrollView(this);
            TextView message = new TextView(this); {
                int padding = dp2px(16);
                message.setPadding(padding, padding, padding, padding);
                message.setText(mLog);
                message.setTextIsSelectable(true);
            }
            hw.addView(message);
            contentView.addView(hw, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            setContentView(contentView);


        }
    }

    @Override
    public void onBackPressed() {
        restart();
    }

    private void restart() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(getPackageName());
        if (intent != null) {
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(intent);
        }
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.copy: 
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(getPackageName(), mLog));
                break;
            case 1345:

                String[] recipients = {"officialofun@gmail.com"}; // recipient email address
                String subject = "Code Editor Crash Report"; // email subject
                String body = mLog; // email body

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);

// need this to prompts email client only
                emailIntent.setType("message/rfc822");

                startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));

                Toast.makeText(this, "Reported", 3).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, android.R.id.copy, 0, android.R.string.copy).setOnMenuItemClickListener(this)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 1345, 0, "Report").setOnMenuItemClickListener(this).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }


}
