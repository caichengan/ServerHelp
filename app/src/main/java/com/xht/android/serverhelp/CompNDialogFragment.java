package com.xht.android.serverhelp;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CompNDialogFragment extends DialogFragment {

    private static final String TAG = "CompNDialogFragment";
    int mCurrentStep;
    ListView mListView;
    private ArrayList<String> mCompNames = new ArrayList<>();
    private int uid;
    static CompNDialogFragment newInstance(int compId,int uid) {
        CompNDialogFragment f = new CompNDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", compId);
        args.putInt("uid", uid);
        f.setArguments(args);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentStep = getArguments().getInt("num");
        uid = getArguments().getInt("uid");

        LogHelper.i(TAG,"-----"+mCurrentStep+"-"+uid);
        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style, theme);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comp_n_dialog, container, false);
        mListView = (ListView) view.findViewById(R.id.cn_l_view);
        getDataStepNum(mCurrentStep,uid);
        return view;
    }

    //获取当前步骤的客户信息 TODO
    private void getDataStepNum(int mCurrentStep,int uid) {

        VolleyHelpApi.getInstance().getStepData(uid,mCurrentStep, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONArray tempJA = ((JSONObject) result).optJSONArray("entity");
                LogHelper.i(TAG,"---------"+tempJA.toString());
                if (tempJA != null) {
                    int compJALength = tempJA.length();
                    for (int i = 0; i < compJALength; i++) {
                        JSONObject temp = tempJA.optJSONObject(i);
                        mCompNames.add(temp.optString("companyName"));
                    }

                    CNAdapter adapter = new CNAdapter(mCompNames);
                    mListView.setAdapter(adapter);
                } else {
                    App.getInstance().showToast("没有数据");
                }

            }

            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
            }
        });
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class CNAdapter extends ArrayAdapter<String> {

        public CNAdapter(ArrayList<String> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String item = getItem(position);
            holder.title.setTextColor(Color.BLUE);
            holder.title.setText(item);
            return convertView;
        }
    }

    class ViewHolder {
        public TextView title;
    }
}
