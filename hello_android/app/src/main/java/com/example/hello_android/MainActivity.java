package com.example.hello_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hello_android.R;
import com.skyfishjy.library.RippleBackground;

public class MainActivity extends AppCompatActivity {
    private static boolean clicks =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clicks){
                    rippleBackground.stopRippleAnimation();
                    clicks=false;
                }else{
                    rippleBackground.startRippleAnimation();
                    clicks=true;
                }
            }
        });
    }
}

