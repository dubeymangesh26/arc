package com.arcltd.staff.activity.chat;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arcltd.staff.R;
import com.arcltd.staff.utility.ZoomImageView;
import com.bumptech.glide.Glide;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ZoomImageView imageView = findViewById(R.id.iv_full_screen);
        ImageButton btnClose = findViewById(R.id.btn_close);

        String imageUrl = getIntent().getStringExtra("imageUrl");

        if (!TextUtils.isEmpty(imageUrl)) {
            Object loadTarget;
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                loadTarget = imageUrl;
            } else {
                loadTarget = Uri.parse(imageUrl);
            }

            Glide.with(this)
                    .load(loadTarget)
                    .placeholder(R.drawable.arcround)
                    .error(R.drawable.arcround)
                    .into(imageView);
        } else {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
        }

        btnClose.setOnClickListener(v -> finish());
    }
}