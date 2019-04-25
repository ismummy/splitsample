package com.muryno;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfView=findViewById(R.id.pdfView);
//        seperate("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2019/01/12/Pictures/_bf1657be-1631-11e9-a284-061f95944840.JPG");

        seperate("https://media.blackhat.com/ad-12/Hamon/bh-ad-12-malicious%20URI-Hamon-WP.pdf");
    }

    private void seperate(String uri){

        PhotoView photoView = findViewById(R.id.photo_view);
        if(uri.endsWith(".pdf")){
            pdfView.setVisibility(View.VISIBLE);
            photoView.setVisibility(View.GONE);

            new RetrivePDFStream().execute(uri);




        }else {
            photoView.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
            Glide
                    .with(this)
                    .load(uri)
                    .centerCrop()
                    .into(photoView);
        }


    }
    public class RetrivePDFStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL uri = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).password("Your Password").load();
        }
    }
}

