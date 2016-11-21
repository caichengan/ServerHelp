package com.xht.android.serverhelp;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.xht.android.serverhelp.model.SortAdapter;
import com.xht.android.serverhelp.model.SortModel;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.CharacterParser;
import com.xht.android.serverhelp.util.LogHelper;
import com.xht.android.serverhelp.util.PinyinComparator;
import com.xht.android.serverhelp.view.ClearEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016-10-25.
 */

public class ClientFragment extends Fragment {

/*     * 汉字转换成拼音的类
     *//**//**/

    private CharacterParser characterParser;
     /** 根据拼音来排列ListView里面的数据类
     **/

    private PinyinComparator pinyinComparator;

    private static final String TAG = "ClientFragment";
    private List<SortModel> mClientContacts;
    private ClearEditText mClientEdit;
    private ListView mClientList;

    private SortAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client, null);

        mClientEdit = (ClearEditText) view.findViewById(R.id.mClientEdit);
        mClientList = (ListView)view.findViewById(R.id.mClientList);


        mClientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });

        //TODO
        int len=mClientContacts.size();
        String [] date=new String[len];
        Log.i(TAG,"----len-"+len);
        for (int i=0;i<len;i++){
            date[i]=mClientContacts.get(i).getName();
        }

        mClientContacts = filledData(date);

        // 根据a-z进行排序源数据
        Collections.sort(mClientContacts, pinyinComparator);
        adapter = new SortAdapter(getActivity(), mClientContacts);


        mClientList.setAdapter(adapter);


        // mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        //根据输入框输入值的改变来过滤搜索
        mClientEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return  view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClientContacts=new ArrayList<>();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        getTXLBarData();

    }

    //访问通讯录数据
    private void getTXLBarData() {
        VolleyHelpApi.getInstance().getTXLData(new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, "-----所有信息--" + result.toString());
                JSONArray JsonAy = null;
                try {
                    JsonAy = ((JSONObject) result).getJSONArray("entity");
                    int JsonArryLenth=JsonAy.length();
                    for (int i=0;i<JsonArryLenth;i++){
                        SortModel item=new SortModel();
                        JSONObject JsonItem = (JSONObject) JsonAy.get(i);
                        String contactName = JsonItem.optString("contactName");
                        String telephone = JsonItem.optString("telephone");//起始步骤
                        item.setName(contactName);
                        item.setPhoneNum(telephone);

                        mClientContacts.add(item);
                        LogHelper.i(TAG, "-----t通讯录--" +i+"--"+contactName+telephone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
            }
        });
    }





  /*   * 为ListView填充数据
     * @param date
     * @return
     **/

    private List<SortModel> filledData(String [] date){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }



    /* * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     **/

    private void filterData(String filterStr){
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = mClientContacts;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : mClientContacts){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }


}
