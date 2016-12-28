package com.xht.android.serverhelp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xht.android.serverhelp.model.CompleteCompanyBean;
import com.xht.android.serverhelp.model.EmployeeAdapter;
import com.xht.android.serverhelp.model.UserInfo;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 * <br>
 *     办证中的列表项的详细
 */
public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button editButton;
    private Button saveButton;

    private TextView textComName;//公司名
    private TextView textClientName;//客户名
    private TextView textPhone;//电话号码
    private TextView textSex;//性别
    private TextView textMoney;//注册资金
    private TextView textGufeng;//股份
    private TextView textStyle;//类型
    private TextView textRange;//范围
    private TextView textAddress;//地址
    private ListView mEmployee;//员工信息
    private EmployeeAdapter employeeAdapter;
    private UserInfo userInfo;
    private int uid;

    private List<CompleteCompanyBean> completeCompanyList;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        userInfo = MainActivity.getInstance();
        uid = userInfo.getUid();
        completeCompanyList=new ArrayList<>();
        getCompanyDetial();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {

        editButton = (Button) view.findViewById(R.id.editButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        textComName = (TextView) view.findViewById(R.id.textComName);
        textClientName = (TextView) view.findViewById(R.id.textClientName);
        textPhone = (TextView) view.findViewById(R.id.textPhone);
        textSex = (TextView) view.findViewById(R.id.textSex);
        textMoney = (TextView) view.findViewById(R.id.textMoney);
        textGufeng = (TextView) view.findViewById(R.id.textGufeng);
        textStyle = (TextView) view.findViewById(R.id.textStyle);
        textRange = (TextView) view.findViewById(R.id.textRange);
        textAddress = (TextView) view.findViewById(R.id.textAddress);
        mEmployee = (ListView) view.findViewById(R.id.mEmployee);



        completeDatas();


    }

    //获取公司人员的信息
    private void getCompanyDetial() {

        VolleyHelpApi.getInstance().getCompamyDatas(uid, new APIListener() {
            @Override
            public void onResult(Object result) {

            }

            @Override
            public void onError(Object e) {

            }
        });

    }

    private void completeDatas() {

        employeeAdapter = new EmployeeAdapter(getActivity(),completeCompanyList);
        mEmployee.setAdapter(employeeAdapter);

    }

}
