package com.example.shan.week3;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class RateListActivity2 extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //super.onCreate(savedInstanceState);
        initListView();
        this.setListAdapter(listItemAdapter);
    }

    public void run() {
        Log.i("thread","run.....");
        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String
        try {
            Document doc = Jsoup.connect("http://www.usd-cny.com/icbc.htm").get()
            Elements tbs = doc.getElementsByClass("tableDataTable");
            Element table = tbs.get(0);
            Elements tds = table.getElementsByTag("td");
            for (int i = 6; i < tds.size(); i+=6) {
                Element td = tds.get(i);
                Element td2 = tds.get(i+3);
                String tdStr = td.text();
                String pStr = td2.text();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", tdStr);
                map.put("ItemDetail", pStr);
                rateList.add(map);
                Log.i("td",tdStr + "=>" + pStr);
            }
            marker = true;
        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage();
        msg.what = msgWhat;
        if(marker){
            msg.arg1 = 1;
        }else{
            msg.arg1 = 0;
        }
        msg.obj = rateList;
        handler.sendMessage(msg);
        Log.i("thread","sendMessage.....");
    }


    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate： " + i); // 标题文字
            map.put("ItemDetail", "detail" + i); // 详情描述
            listItems.add(map);
        }
// 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems
                R.layout.list_item, // ListItem的XML布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
        );
    }

    public void handleMessage(Message msg) {
        if(msg.what == msgWhat){
            List<HashMap<String, String>> retList = (List<HashMap<String, String>>) SimpleAdapter adapter = new SimpleAdapter(RateListActivity.this, retList,
                    R.layout.list_item, // ListItem的XML布局实现
                    new String[] { "ItemTitle", "ItemDetail" },
                    new int[] { R.id.itemTitle, R.id.itemDetail });
            setListAdapter(adapter);
            Log.i("handler","reset list...");
        }
        super.handleMessage(msg);
    }

    public class MyAdapter extends ArrayAdapter {
        private static final String TAG = "MyAdapter";
        public MyAdapter(Context context, int resource, ArrayList<HashMap<String,Stri
super(context, resource, list);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_it
        }
        Map<String,String> map = (Map<String, String>) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
        TextView detail = (TextView) itemView.findViewById(R.id.itemDetail);
        title.setText("Title:" + map.get("ItemTitle"));
        detail.setText("detail:" + map.get("ItemDetail"));
        return itemView;
    }
}


}
