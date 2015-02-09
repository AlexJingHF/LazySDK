package com.alex.lazysdk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.alex.lazysdk.activity.BaseActivity;
import com.alex.lazysdk.ioc.BaseIocView;

public class MainActivity extends BaseActivity
{
    /** Called when the activity is first created. */
    @BaseIocView(id = R.id.button,click = "btnClick") Button click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void btnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button:
            {
                click.setText("clicked");
            }
            break;

        }
    }
}
