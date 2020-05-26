package com.sarath.m_easybuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AdPageActivity extends AppCompatActivity {
    TextView title,description,price,phone,publisherName;
    ImageView backbutton,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adpage);

        title = findViewById(R.id.textViewAdpageTitle);
        description = findViewById(R.id.textViewAdpageDescription);
        price = findViewById(R.id.textViewAdpagePrice);
        phone = findViewById(R.id.textViewAdpagePhone);
        publisherName = findViewById(R.id.textViewAdpageUsername);
        image = findViewById(R.id.imageViewAdpageImage);

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
            publisherName.setText(adItem.getPublisherName());
            if(!adItem.getImage().equals("noimage")){
            Picasso.get().load(adItem.getImage()).into(image); }
        }

        final String phNo = phone.getText().toString().trim();

        Button callButton = findViewById(R.id.ButtonContactSeller);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phNo));
                startActivity(intent);
            }
        });

    }

}
