package com.sarath.m_easybuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AdPageActivity extends AppCompatActivity {
    TextView title,description,price,phone;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adpage);

        title = findViewById(R.id.textViewAdpageTitle);
        description = findViewById(R.id.textViewAdpageDescription);
        price = findViewById(R.id.textViewAdpagePrice);
        phone = findViewById(R.id.textViewAdpagePhone);

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        if(intent.getExtras() != null){
            adModel adItem = (adModel) intent.getSerializableExtra("data");

            title.setText(adItem.getTitle());
            description.setText(adItem.getDescription());
            price.setText("₹ "+adItem.getPrice());
            phone.setText(adItem.getPhone());
        }
    }
}
