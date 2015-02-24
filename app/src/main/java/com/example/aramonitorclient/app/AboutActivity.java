package com.example.aramonitorclient.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by GebekovAS on 23.02.2015.
 */
public class AboutActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tv=(TextView) findViewById(R.id.aboutTextView);
        String str= getResources().getString(R.string.about_text);
        tv.setLinksClickable(true);
        tv.setText(Html.fromHtml(str));

        Button btnok=(Button) findViewById(R.id.about_buttonOk);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
