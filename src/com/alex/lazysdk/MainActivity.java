package com.alex.lazysdk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.alex.lazysdk.activity.BaseActivity;
import com.alex.lazysdk.ioc.BaseIocView;
import com.alex.lazysdk.view.progress.CircleProgressBar;

public class MainActivity extends BaseActivity
{
    @BaseIocView(id = R.id.button,click = "btnClick") Button click;
    private CircleProgressBar progressBar;
    private int progress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progressBar = (CircleProgressBar) findViewById(R.id.circle_progress);
    }

    public void btnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button:
            {
                click.setText("clicked");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progress < 100){
                            progressBar.setProgress(progress);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progress++;
                        }
                    }
                }).start();
            }
            break;

        }
    }
}
