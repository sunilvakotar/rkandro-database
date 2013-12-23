package com.ruby.rkandro.sync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.lmsa.cqkv143768.AdCallbackListener;
import com.lmsa.cqkv143768.AdView;
import com.lmsa.cqkv143768.AirPlay;
import com.ruby.rkandro.sync.adapter.StoryAdapter;
import com.ruby.rkandro.sync.db.StoryDataSource;
import com.ruby.rkandro.sync.pojo.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sunil Vakotar
 * Date: 12/9/13
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryList extends SherlockActivity implements AdCallbackListener.MraidCallbackListener {

    private ListView lv;

    private List<Story> storyList = new ArrayList<Story>();
    StoryAdapter storyAdapter;
    AirPlay airPlay;

    StoryDataSource dataSource;

    private String id;
    private String name;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_rk_list);
        //getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#333333'>" + getString(R.string.app_name) + "</font></b>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataSource = new StoryDataSource(this);
        dataSource.open();

        Bundle extra = getIntent().getExtras();

        id = extra != null ? extra.getString("ID") : " ";
        name = extra != null ? extra.getString("name") : " ";

        lv = (ListView) findViewById(R.id.lviRkList);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Story story = (Story) parent.getItemAtPosition(position);
                Intent i = new Intent(StoryList.this, StoryListDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", story.getId().toString());
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        if(!" ".equals(name) && !" ".equals(name)){
            if(name.contains(".")){
                name = name.substring(name.indexOf(".")+1);
            }
            getSupportActionBar().setTitle(name);
            storyList = dataSource.getStoryByCategory(Integer.parseInt(id));
            if (storyList.size() > 0) {
                storyAdapter = new StoryAdapter(StoryList.this, storyList);
                lv.setAdapter(storyAdapter);
                storyAdapter.notifyDataSetChanged();
            }

        }

        airPlay=new AirPlay(this, adCallbackListener, true);
        AdView adView=(AdView)findViewById(R.id.myAdView);
        adView.setAdListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return true;
    }

    AdCallbackListener adCallbackListener = new AdCallbackListener() {
        @Override
        public void onSmartWallAdShowing() {
            Toast.makeText(StoryList.this, "onAdCached", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onSmartWallAdClosed() {
            Toast.makeText(StoryList.this, "onSmartWallAdClosed", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onAdError(String s) {
            Toast.makeText(StoryList.this, "onAdError"+s, Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onSDKIntegrationError(String s) {
            Toast.makeText(StoryList.this, "onSDKIntegrationError", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onVideoAdFinished() {
            Toast.makeText(StoryList.this, "onVideoAdFinished", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onVideoAdShowing() {
            Toast.makeText(StoryList.this, "onVideoAdShowing", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onAdCached(AdType adType) {
            Toast.makeText(StoryList.this, "onAdCached", Toast.LENGTH_SHORT).show();
            //airPlay.showCachedAd(StoryList.this, AdType.interstitial);
        }
    };

    @Override
    public void onAdLoadingListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAdLoadedListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onErrorListener(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCloseListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAdExpandedListner() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAdClickListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void noAdAvailableListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
