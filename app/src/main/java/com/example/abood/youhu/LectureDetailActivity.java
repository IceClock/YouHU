package com.example.abood.youhu;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;

public class LectureDetailActivity extends ActionBarActivity {

    private VideoView ivideo;
    private TextView vidname;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecture_detail);

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
            String json = bundle.getString("lectureModel"); // getting the model from MainActivity send via extras
            LectureModel lectureModel = new Gson().fromJson(json, LectureModel.class);

            // Then later, when you want to display image
         ivideo.setVideoPath(lectureModel.getVideo());

            MediaController mediaController = new
                    MediaController(this);
            mediaController.setAnchorView(ivideo);
            ivideo.setMediaController(mediaController);

            mediaController.buildDrawingCache();


            ivideo.setOnPreparedListener(new
            MediaPlayer.OnPreparedListener()  {
         @Override
         public void onPrepared(MediaPlayer mp) {
             progressBar.setVisibility(View.GONE);

         }

            });
            ivideo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) ivideo.getLayoutParams();
                    params.width =  metrics.widthPixels;
                    params.height = metrics.heightPixels;
                    params.leftMargin = 0;
                    ivideo.setLayoutParams(params);
                }
            });

            ivideo.start();



            vidname.setText(lectureModel.getName());


        }

    }

    private void setUpUIViews() {
        ivideo = (VideoView) findViewById(R.id.ivIcon);
        vidname = (TextView) findViewById(R.id.vidt);
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