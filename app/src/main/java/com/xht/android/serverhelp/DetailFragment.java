package com.xht.android.serverhelp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xht.android.serverhelp.model.CompleteCompanyBean;
import com.xht.android.serverhelp.model.EmployeeAdapter;
import com.xht.android.serverhelp.model.UserInfo;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONObject;

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
    private Button btnChange;

    private TextView textComName;//公司名
    private TextView editComName;//公司名
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
    private RwxqActivity mRwxqActivity;

    private List<CompleteCompanyBean> completeCompanyList;
    private boolean number=false;
    private String companyId;

    private static final String TAG = "DetailFragment";
    private CompleteCompanyBean completeCompanyBean;
    private String phone;
    private String contactName;

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
            companyId = getArguments().getString(ARG_PARAM1);
            LogHelper.i(TAG,"------companyId-onCreate--"+ companyId);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mRwxqActivity= (RwxqActivity) getActivity();
        phone = ((RwxqActivity) getActivity()).phone;
        contactName = ((RwxqActivity) getActivity()).contactName;
        userInfo = MainActivity.getInstance();
        uid = userInfo.getUid();
        LogHelper.i(TAG,"------companyId-onCreate--"+ companyId);
        completeCompanyList=new ArrayList<>();
        if (companyId!=null) {
            LogHelper.i(TAG,"------companyId-onCreate--"+ companyId+"---phone"+ phone);
            getCompanyDetial();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {

        btnChange = (Button) view.findViewById(R.id.btnChange);
        textComName = (TextView) view.findViewById(R.id.textComName);
        editComName = (EditText) view.findViewById(R.id.editComName);
        textClientName = (TextView) view.findViewById(R.id.textClientName);
        textPhone = (TextView) view.findViewById(R.id.textPhone);
        textSex = (TextView) view.findViewById(R.id.textSex);
        textMoney = (TextView) view.findViewById(R.id.textMoney);
        textGufeng = (TextView) view.findViewById(R.id.textGufeng);
        textStyle = (TextView) view.findViewById(R.id.textStyle);
        textRange = (TextView) view.findViewById(R.id.textRange);
        textAddress = (TextView) view.findViewById(R.id.textAddress);
        mEmployee = (ListView) view.findViewById(R.id.mEmployee);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.i(TAG,"------companyId-onCreate--"+ companyId);
                String companyName = editComName.getText().toString();
                if (!number){
                    textComName.setVisibility(View.GONE);
                    editComName.setVisibility(View.VISIBLE);
                    btnChange.setText("确认");

                    ChangeCompanyName();
                    number=true;
                }else{
                    textComName.setVisibility(View.VISIBLE);
                    textComName.setText(companyName);
                    editComName.setVisibility(View.GONE);
                    btnChange.setText("完成");

                }
            }
        });

    }

    //提交保存的公司名
    private void ChangeCompanyName() {

    }

    //获取公司人员的信息
    private void getCompanyDetial() {

        LogHelper.i(TAG,"-----getCompanyDetial-------1111----");
       VolleyHelpApi.getInstance().getCompamyDatas(companyId, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject object= (JSONObject) result;

                LogHelper.i(TAG,"-----getCompanyDetial------22222222-----");
                LogHelper.i(TAG,"----onResult---"+result.toString());

                analysisMethod(result.toString());

            }

            @Override
            public void onError(Object e) {

            }
        });

    }

    //解析数据
    private void analysisMethod(String object) {
       // JSONObject obj = object.optJSONObject("entity");

        LogHelper.i(TAG,"-----getCompanyDetial-----3333-----");
        Gson gson=new Gson();
        CompleteCompanyBean completeCompanyBean = gson.fromJson(object, CompleteCompanyBean.class);

        int size = completeCompanyBean.getEntity().getCompany().size();
        if (size>0) {
            CompleteCompanyBean.EntityBean.CompanyBean companyBean = completeCompanyBean.getEntity().getCompany().get(0);


            String companyName = companyBean.getCompanyName();
            String addressDetail = companyBean.getAddressDetail();
            String businessScope = companyBean.getBusinessScope();

            String compTypeName = companyBean.getCompTypeName();
            String registCapital = companyBean.getRegistCapital();

            textComName.setText(companyName);
            textPhone.setText(phone);
            textClientName.setText(contactName);
            textStyle.setText(compTypeName);
            textAddress.setText(addressDetail);
            textMoney.setText(registCapital);
            textRange.setText(businessScope);


            LogHelper.i(TAG, "-----registCapital--"+registCapital+"-----companyName---tel-" + companyName + "----contactName--"+contactName+"--compTypeName-"+compTypeName);
            // completeCompanyList.add(completeCompanyBean);

            List<CompleteCompanyBean.EntityBean.PersonsBean> persons = completeCompanyBean.getEntity().getPersons();
            completeDatas(persons);
        }

    }

    private void completeDatas(List<CompleteCompanyBean.EntityBean.PersonsBean> companyBean) {



        employeeAdapter = new EmployeeAdapter(getActivity(),companyBean);
        mEmployee.setAdapter(employeeAdapter);

    }

}
