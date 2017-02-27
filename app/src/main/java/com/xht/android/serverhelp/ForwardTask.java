package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xht.android.serverhelp.model.TaskPeople;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */
public class ForwardTask extends Activity {

    private static final String TAG = "ForwardTask";
    private int mUId;
    private int orderId;
    private String result;

    private List<TaskPeople> mListTask;
    private ListView mListView;
    private TaskAdapter.ViewHolder holder;
    private TaskAdapter mTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forward);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        mUId = bundle.getInt("uid",-1);
        orderId = bundle.getInt("orderId",-1);
        result = bundle.getString("result");
        LogHelper.i(TAG,"------ForwardTask--uid--"+ mUId+"---ord---"+orderId+"-----result--"+ result);

        //[{"eTel":"13652365101","eName":"祝飞","eId":1},{"eTel":"13652365101","eName":"ZHUFEI","eId":2},{"eTel":"13531833516","eName":"韦继胜","eId":3},
        // {"eTel":"13531829360","eName":"蔡成安","eId":4},
        // {"eTel":"13421411253","eName":"覃源恒","eId":5},{"eTel":null,"eName":"孙权","eId":10},{"eTel":null,"eName":"刘备","eId":11}]
        mListTask=new ArrayList<TaskPeople>();
        mListView= (ListView) findViewById(R.id.forward_listview);
        try {
            JSONArray obj=new JSONArray(result);
            LogHelper.i(TAG,"-----obj--"+obj.get(1));
            int len=obj.length();
            LogHelper.i(TAG,"-----len--"+len);
            for (int i=0;i<len;i++){
                TaskPeople ItemList=new TaskPeople();
                JSONObject item = (JSONObject) obj.get(i);
                LogHelper.i(TAG,"-----obj--i--"+obj.get(i));
               String telPhone= item.optString("eTel");
                String telName=item.optString("eName");
                String telId=item.optString("eId");

                LogHelper.i(TAG,"-------i--"+telPhone+"--"+telName+"--"+telId);
                ItemList.seteTel(telPhone);
                ItemList.seteName(telName);
                ItemList.seteId(telId);

                mListTask.add(ItemList);

            }
            mTaskAdapter = new TaskAdapter();
            mListView.setAdapter(mTaskAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }


      /*  mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskPeople taskPeople = mListTask.get(position);
                String name = taskPeople.geteName();
                String tel = taskPeople.geteTel();
                String telId = taskPeople.geteId();

                App.getInstance().showToast("点击了"+position+"--"+name+tel+telId);
            }
        });*/


    }

    class  TaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mListTask.size()>0){
                return mListTask.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mListTask.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mListTask.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView==null){

               /* holder=new ViewHolder();
                convertView=View.inflate(ForwardTask.this,R.layout.item_select_people,null);
                holder. mRelatPeople= (RelativeLayout) convertView.findViewById(R.id.eRelatPeople);
                holder.mName= (TextView) convertView. findViewById(R.id.eName);
                holder.mCheck= (CheckBox)  convertView.findViewById(R.id.eCheckBox);*/

                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }

          /*  holder. mRelatPeople.setOnClickListener(this);
            holder.mName.setOnClickListener(this);
            holder.mCheck.setOnClickListener(this);*/
            TaskPeople people = mListTask.get(position);
            String name = people.geteName();
            String tel = people.geteTel();

            LogHelper.i(TAG,"----------"+name+tel);

            if (!TextUtils.isEmpty(name)) {

                holder.mName.setText(name);
            }else{
                holder.mPhone.setText(tel);
            }


            return convertView;
        }



        class  ViewHolder{
            RelativeLayout mRelatPeople;
            TextView mName;
            TextView mPhone;
            CheckBox mCheck;

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
