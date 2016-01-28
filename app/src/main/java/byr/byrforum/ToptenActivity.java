package byr.byrforum;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import byr.byrforum.javabean.TopTenTopic;


public class ToptenActivity extends AppCompatActivity {
    String TAG  = "ToptenActivity";
    final String JSON_DATA = "jsondata";
    private boolean mContentLoaded;
    private View mContentView;
    private View mLoadingView;
    private View mRefreshView;
    private int mShortAnimationDuration;
    private ViewPager viewPager;
    private NetThread netThread;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topten);

        //viewpager
        LayoutInflater lf = LayoutInflater.from(this);
        View view1 = lf.inflate(R.layout.borads_layout, null);
        View view2 = lf.inflate(R.layout.top_ten_layout, null);
        View view3 = lf.inflate(R.layout.layout3, null);

        final List<View> viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        final List<String> titleList = new ArrayList<>();// 每个页面的Title数据
        titleList.add("wp");
        titleList.add("jy");
        titleList.add("jh");

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));

            }

            @Override
            public int getItemPosition(Object object) {

                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return titleList.get(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                Log.e(TAG,"change + " + position);
                if(mShortAnimationDuration == 0) {
                    mContentView = findViewById(R.id.top_ten_list);
                    mLoadingView = findViewById(R.id.loading_spinner);
                    mRefreshView  = findViewById(R.id.loading_refresh);
                    mContentView.setVisibility(View.GONE);
                    mRefreshView.setVisibility(View.GONE);
                    mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

                    netThread = new NetThread();
                    netThread.start();
                }
                return viewList.get(position);
            }
        };
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        Log.e(TAG,"setPager");
        viewPager.setCurrentItem(1);

        /* handler出现在接受消息的线程里 */
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 404) {
                    Log.e(TAG, "error");
                    Toast.makeText(ToptenActivity.this,"请检查网络是否连接",Toast.LENGTH_SHORT).show();
                    mLoadingView.setVisibility(View.GONE);
                    mRefreshView.setVisibility(View.VISIBLE);
                    mRefreshView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLoadingView.setVisibility(View.VISIBLE);
                            mRefreshView.setVisibility(View.GONE);
                            NetThread thread = new NetThread();
                            thread.start();
                        }
                    });

                }else if (msg.what == 123) {

                    mContentLoaded = true;
                    showContentOrLoadingIndicator(mContentLoaded);
                    String text = msg.getData().getString(JSON_DATA);

                    // 尝试用简单的方法 allTopten里面的arraylist包含toptentopic
                    Gson gson = new Gson();
                    TopTenTopic[] topics = gson.fromJson(text, TopTenTopic[].class);
                    final List<Map<String, String>> listItems2 = new ArrayList<>();

                    for(TopTenTopic topic : topics) {
                        Map<String, String> listItem = new HashMap<>();
                        listItem.put("title", topic.getTitle());
                        listItem.put("author", topic.getAuthor());
                        listItem.put("pubDate", topic.getPubDate());
                        listItem.put("link", topic.getLink());
                        listItems2.add(listItem);
                        Log.e(TAG, listItem.toString());
                    }
                    SimpleAdapter adapter = new SimpleAdapter(ToptenActivity.this, listItems2, R.layout.top_item,
                            new String[] {"title", "pubDate" , "author","link"},
                            new int[] {R.id.title, R.id.pubDate, R.id.author, R.id.link});
                    ListView list = (ListView) findViewById(R.id.top_ten_list);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            TextView link = (TextView) findViewById(R.id.link);
//                            String linkStr = link.getText().toString();
                            String linkStr = listItems2.get(position).get("link");
                            Log.e(TAG, linkStr);

                            Bundle bundle = new Bundle();
                            bundle.putString("linkStr", linkStr);
                            Intent intent = new Intent(ToptenActivity.this, TopicActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });


                }
            }
        };


    }

    private void showContentOrLoadingIndicator(boolean contentLoaded) {
        // Decide which view to hide and which to show.
        final View showView = contentLoaded ? mContentView : mLoadingView;
        final View hideView = contentLoaded ? mLoadingView : mContentView;

        // Set the "show" view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        // Animate the "show" view to 100% opacity, and clear any animation listener set on
        // the view. Remember that listeners are not limited to the specific animation
        // describes in the chained method calls. Listeners are set on the
        // ViewPropertyAnimator object for the view, which persists across several
        // animations.
        showView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the "hide" view to 0% opacity. After the animation ends, set its visibility
        // to GONE as an optimization step (it won't participate in layout passes, etc.)
        hideView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }

    //定义一个线程类用来联网
    class NetThread extends Thread {
        @Override
        public void run() {
            //必须写prepare
            Looper.prepare();
            //android不允许在主线程访问网络，把网络访问的代码放在这里
            Log.e(TAG,"new thread");
            String inputLine;
            URL url;
            String data = "";
            Properties p = new Properties();
            InputStream input = getResources().openRawResource(R.raw.properties);

            try {
                p.load(input);
                String serverUrl = p.getProperty("url");
                input.close();

                url = new URL(serverUrl+ "/topten");
                HttpURLConnection uc = (HttpURLConnection)url.openConnection();

                uc.setConnectTimeout(3500);
                uc.setReadTimeout(3500);
                Log.e(TAG, "uc = " + uc.toString());
                uc.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));

                while ((inputLine = reader.readLine()) != null) {
                    Log.e(TAG,inputLine);

                    data += inputLine;
                }
                reader.close();
            }  catch (IOException e) {
                Log.e(TAG, "IOException e" + e.toString());
                Message msg = new Message();
                msg.what = 404;
                handler.sendMessage(msg);
            }
            if(!data.equals("")) {
                //向主线程发消息
                Message msg = new Message();
                msg.what = 123;
                Bundle bundle = new Bundle();
                bundle.putString(JSON_DATA, data);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
                Looper.loop();
        }
    }
}
