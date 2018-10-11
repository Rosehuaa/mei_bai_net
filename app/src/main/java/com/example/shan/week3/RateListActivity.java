package com.example.shan.week3;

import android.app.ListActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{

    private String[] list_data = {"one","tow","three","four"};
    int msgWhat = 3;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);

        ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.activity_list_item,list_data);
        setListAdapter(adapter);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 5){
                List<String> retList = (List<String>) msg.obj;
                ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.activity_list_item,list_data);
                setListAdapter(adapter);
                Log.i("handler","reset list...");
            }
                super.handleMessage(msg);
            }
        };


    }


    @Override
    public void run() {
        Log.i("thread","run.....");
        List<String> rateList = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("http://www.usd-cny.com/icbc.htm").get();
            Elements tbs = doc.getElementsByClass("tableDataTable");
            Element table = tbs.get(0);
            Elements tds = table.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i+=5) {
                Element td = tds.get(i);
                Element td2 = tds.get(i+3);
                String tdStr = td.text();
                String pStr = td2.text();
                rateList.add(tdStr + "=>" + pStr);
                Log.i("td",tdStr + "=>" + pStr);
            }
        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(5);
        msg.obj = rateList;
        handler.sendMessage(msg);
        Log.i("thread","sendMessage.....");
    }

}
