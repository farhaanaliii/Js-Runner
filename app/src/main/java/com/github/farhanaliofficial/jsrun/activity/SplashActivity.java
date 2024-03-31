package com.github.farhanaliofficial.jsrun.activity;
import android.app.Activity;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.farhanaliofficial.jsrun.R;
import android.os.Handler;
import com.github.farhanaliofficial.jsrun.Handler.CopyAssets;
import com.github.farhanaliofficial.jsrun.Utils.Utils;
import android.content.Intent;
import com.github.farhanaliofficial.jsrun.activity.MainActivity;
import android.view.WindowManager;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    LinearLayout layout;
    public boolean fileExtracted;
    CopyAssets copy_assets = new CopyAssets();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(getColor(R.color.colorPrimary));

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.icon2);

        layout.addView(imageView);
        
        setContentView(layout);
        fileExtracted = Utils.getBoolean(this, false);
        if(!fileExtracted) {
            new Thread(new Runnable(){
                    @Override
                    public void run() {
                        copy_assets.setCtx(getApplicationContext());
                        copy_assets.setFromPathDirectoryAssets("js");
                        copy_assets.setToDirectoryTarget(getFilesDir().getPath());
                        copy_assets.start();
                    }
                }).start();
            fileExtracted = true;
            Utils.putBoolean(this, fileExtracted);
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
       }, 3000);

    }
}
