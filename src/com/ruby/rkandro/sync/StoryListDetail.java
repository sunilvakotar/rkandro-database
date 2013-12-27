package com.ruby.rkandro.sync;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.lmsa.cqkv143768.AdCallbackListener;
import com.lmsa.cqkv143768.AirPlay;
import com.ruby.rkandro.sync.db.StoryDataSource;
import com.ruby.rkandro.sync.pojo.Story;

import java.util.Timer;
import java.util.TimerTask;

public class StoryListDetail extends SherlockActivity implements AdCallbackListener.MraidCallbackListener{

    public static final long NOTIFY_INTERVAL = 240 * 1000; // 240 seconds, 4 minutes
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private ProgressDialog progressDialog;

	private String description;
    private String id;
    private String name;
	
	TextView textDescription;
    AirPlay airPlay;

    StoryDataSource dataSource;

    AdCallbackListener adCallbackListener = new AdCallbackListener() {
        @Override
        public void onSmartWallAdShowing() {
            //Toast.makeText(StoryListDetail.this, "onAdCached", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSmartWallAdClosed() {
            //Toast.makeText(StoryListDetail.this, "onSmartWallAdClosed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdError(String s) {
            //Toast.makeText(StoryListDetail.this, "onAdError:"+s, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSDKIntegrationError(String s) {
            //Toast.makeText(StoryListDetail.this, "onSDKIntegrationError", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVideoAdFinished() {
            //Toast.makeText(StoryListDetail.this, "onVideoAdFinished", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVideoAdShowing() {
            //Toast.makeText(StoryListDetail.this, "onVideoAdShowing", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdCached(AdType adType) {
            //Toast.makeText(StoryListDetail.this, "onAdCached", Toast.LENGTH_SHORT).show();
        }
    };

	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rk_description);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = getIntent().getExtras();

        id = extra != null ? extra.getString("ID") : " ";

        dataSource = new StoryDataSource(this);
        dataSource.open();
        Story story = dataSource.getStoryById(Integer.parseInt(id));
        getSupportActionBar().setTitle(story.getStoryName());

        textDescription = (TextView) findViewById(R.id.TextDescription);
        textDescription.setText(Html.fromHtml(story.getStoryDesc()));

        airPlay = new AirPlay(this, adCallbackListener, true);
        airPlay.startSmartWallAd();
        airPlay.showRichMediaInterstitialAd();


        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new PopupDisplayTimerTask(), NOTIFY_INTERVAL, NOTIFY_INTERVAL);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onAdLoadingListener() {
        //Toast.makeText(StoryListDetail.this, "onAdLoadingListener", Toast.LENGTH_SHORT).show();
    }

    private boolean loadOnStart = true;
    @Override
    public void onAdLoadedListener() {
        if(loadOnStart){
            airPlay.showCachedAd(StoryListDetail.this, AdCallbackListener.AdType.smartwall);
            loadOnStart = false;
        }
    }

    @Override
    public void onErrorListener(String s) {
        //Toast.makeText(StoryListDetail.this, "onErrorListener:"+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCloseListener() {
        //Toast.makeText(StoryListDetail.this, "onCloseListener", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdExpandedListner() {
        //Toast.makeText(StoryListDetail.this, "onAdExpandedListner", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClickListener() {
        //Toast.makeText(StoryListDetail.this, "onAdClickListener", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noAdAvailableListener() {
        //Toast.makeText(StoryListDetail.this, "noAdAvailableListener", Toast.LENGTH_SHORT).show();
    }

    private boolean smartAd = false;
    class PopupDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if(smartAd){
                        airPlay.showRichMediaInterstitialAd();
                        airPlay.showCachedAd(StoryListDetail.this, AdCallbackListener.AdType.smartwall);
                        smartAd = false;
                    }else {
                        airPlay.startSmartWallAd();
                        airPlay.showCachedAd(StoryListDetail.this, AdCallbackListener.AdType.interstitial);
                        smartAd = true;
                    }
                }

            });
        }

    }

}
