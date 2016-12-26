package com.cjj.circleprogress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cjj.library.CircleProgressBar;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button mButton;
    private CircleProgressBar mProgressBar;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initEvent();
    }

    private void initEvent() {
        mButton = (Button) findViewById(R.id.btn_click);
        mProgressBar = (CircleProgressBar) findViewById(R.id.cpb_main);
        mTextView = (TextView) findViewById(R.id.tv_num);

        mProgressBar.setListener(new CircleProgressBar.OnProgressBarListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgressing(float percent) {
                mTextView.setText(String.valueOf((int)(1000*percent)));
            }

            @Override
            public void onFinish() {

            }
        });

//        mProgressBar.setProgress(20,30);
    }

    public void onClick(View view){
        int progress = (int) (Math.random()*(99)+1);
        mProgressBar.setProgress(progress);
    }
}
