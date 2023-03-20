package com.example.a5dayappchat2.Activity;


import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a5dayappchat2.R;
import com.squareup.picasso.Picasso;

public class ImageViewActivity extends AppCompatActivity {

    ImageView imageView,btDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);


        imageView = findViewById(R.id.imageView);
        btDownload = findViewById(R.id.btDownload);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String url = getIntent().getStringExtra("url");
        Picasso.get().load(url).into(imageView);

        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadImage( url);
            }
        });

    }

    private void DownloadImage(String url) {
        Uri uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalFilesDir(this,DIRECTORY_DOWNLOADS,"myFileName.jpg");
        downloadManager.enqueue(request);


    }
}