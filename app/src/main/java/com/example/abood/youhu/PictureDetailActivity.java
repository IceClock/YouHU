package com.example.abood.youhu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PictureDetailActivity extends ActionBarActivity {

    private ImageView ivimage;
    private TextView picname;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_detail);



        // Showing and Enabling clicks on the Home/Up button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // setting up text views and stuff
        setUpUIViews();

        // recovering data from MainActivity, sent via intent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String json = bundle.getString("pictureModel"); // getting the model from MainActivity send via extras
            PictureModel pictureModel = new Gson().fromJson(json, PictureModel.class);

            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(pictureModel.getImage(), ivimage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            });

            picname.setText(pictureModel.getName());


        }

    }

    private void setUpUIViews() {
        ivimage = (ImageView)findViewById(R.id.picIcon);
        picname = (TextView) findViewById(R.id.pict);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}