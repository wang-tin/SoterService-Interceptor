package com.jiangnan.guardian;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleText = findViewById(R.id.title_text);
        TextView infoText = findViewById(R.id.info_text);
        TextView warningText = findViewById(R.id.warning_text);
        Button statusButton = findViewById(R.id.status_button);

        titleText.setText("江南护法");
        infoText.setText("SoterService拦截器模块\n作者：王听\n版本：v1.0");
        warningText.setText("⚠️ 警告：该模块仅用于学习研究，请勿用于违法行为，出现问题自负。");

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "模块已激活！请确保Xposed框架已启用此模块。", Toast.LENGTH_LONG).show();
            }
        });
    }
}
