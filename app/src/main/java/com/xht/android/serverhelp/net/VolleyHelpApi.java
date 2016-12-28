package com.xht.android.serverhelp.net;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xht.android.serverhelp.App;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class VolleyHelpApi extends BaseApi{
	private static final String TAG = "VolleyHelpApi";
	
	private static VolleyHelpApi sVolleyHelpApi;
	public static synchronized VolleyHelpApi getInstance() {
		if (sVolleyHelpApi == null) {
			sVolleyHelpApi = new VolleyHelpApi();
		}
		return sVolleyHelpApi;
	}
	private VolleyHelpApi() {}
	/**
	 * 检查版本更新 getCheckVersion
	 * @param apiListener 回调监听器
	 */
	public void getCheckVersion(final APIListener apiListener) {
		String urlString = MakeURL(CHECK_VERSION_URL, new LinkedHashMap<String, Object>() {{
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG,"-----------"+ response.toString());
					JSONObject jsonObject = response.optJSONObject("entity");
					apiListener.onResult(response);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，请稍后再试");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 获取办证的列表的数据
	 * @param uId
	 * @param apiListener
     */
	public void getBZItems(final int uId, final APIListener apiListener) {
		String urlS = MakeURL(BZ_ITEMS_URL, new LinkedHashMap<String, Object>() {
			{put("userid", uId);}
		});
		LogHelper.i(TAG, urlS);
		JsonObjectRequest req = new JsonObjectRequest(urlS, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, "-------"+response.toString());//{"message":"系统错误","result":"error","entity":null,"code":"0"}
					apiListener.onResult(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 获取办证进度标签下的数据
	 * @param ordId
	 * @param apiListener
	 */
	public void getBZProcs(final int ordId, final APIListener apiListener) {
		String urlS = MakeURL(BZ_ProsInit_URL, new LinkedHashMap<String, Object>() {
			{put("orderid", ordId);}
		});
		LogHelper.i(TAG, urlS);
		JsonObjectRequest req = new JsonObjectRequest(urlS, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					JSONArray jsonArray = response.optJSONArray("entity");

					apiListener.onResult(jsonArray);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("获取办证进度标签下的数据出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 更新进度提交数据
	 * @param jsonObject
	 * @param apiListener
	 */
	public void postRefleshPro(JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(POST_ProsREF_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("更新进度提交数据出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}


	/**
	 * 办证中获取成员数据
	 * @param
	 * @param apiListener
	 */
	public void getDataBZChengYuan(final int mUid, final APIListener apiListener) {
		String urlS = MakeURL(BZ_ChengYuan_URL, new LinkedHashMap<String, Object>() {
			{put("employeeId", mUid);}

			//"entity":{"employeeName":"蔡成安"},"code":"1"}
		});
		LogHelper.i(TAG, urlS);
		JsonObjectRequest req = new JsonObjectRequest(urlS, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("办证中获取成员数据错误");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 取消任务接口
	 *
	 */
		public void postCancelTask(JSONObject jsonObject, final APIListener apiListener) {
			JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BZ_CANCEL_URL, jsonObject, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					LogHelper.i(TAG, response.toString());

						apiListener.onResult(response);

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					int type = VolleyErrorHelper.getErrType(error);
					switch (type) {
						case 1:
							LogHelper.i(TAG, "超时");
							break;
						case 2:
							LogHelper.i(TAG, "服务器问题");
							break;
						case 3:
							LogHelper.i(TAG, "网络问题");
							break;
						default:
							LogHelper.i(TAG, "未知错误");
					}
					apiListener.onError("提交订单出错");
				}
			}) {

			};
			App.getInstance().addToRequestQueue(req, TAG);
		}



	/**
	 *
	 * 修改密码
	 *
	 */
	public void getDataChangMiMa(JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, CHANGMIMA_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				   //成功返回：{ message: "密码修改成功", result: "success", entity: null, code: "1" }
				// 失败返回：{ message: "新旧密码不能一致", result: "error", entity: null, code: "0" }

					apiListener.onResult(response);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("提交订单出错");
			}
		}) {

		};
		App.getInstance().addToRequestQueue(req, TAG);
	}


	/**
	 *  继续任务
	 * @param object
	 * @param apiListener
     */
	public void postContinusTask(JSONObject object, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BZ_CONTINUS_URL, object, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {


					apiListener.onResult(response);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("继续任务出错");
			}
		}) {

		};
		App.getInstance().addToRequestQueue(req, TAG);
	}


	/**
	 * * 获取转交任务人员列表

	 * @param apiListener
     */
	public void getForwardTask(final APIListener apiListener) {
		String urlS = MakeURL(BZ_FORWARD_URL, new LinkedHashMap<String, Object>() {

		});
		LogHelper.i(TAG, urlS);
		JsonObjectRequest req = new JsonObjectRequest(urlS, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					JSONArray jsonArray = response.optJSONArray("entity");

					apiListener.onResult(jsonArray);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("获取办证进度标签下的数据出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 转交任务保存
	 * @param object
	 * @param apiListener
     */
	public void postSaveForward(JSONObject object, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BZ_FORWARDSAVE_URL, object, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
//{"message":"任务转交成功",
					//"result":"success","entity":{"transferOutId":120,"transferInId":122,},"code":"1"

					apiListener.onResult(response);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("继续任务出错");
			}
		}) {

		};
		App.getInstance().addToRequestQueue(req, TAG);
	}



	private boolean isResponseError(JSONObject jb){
		String errorCode = jb.optString("code","0");

		if(errorCode.equals("1")){
			return false;
		}		
		return true;
	}


	/**
	 * 登录提交
	 */
	public void postLogin( JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					JSONObject jsonObject = response.optJSONObject("entity");
					apiListener.onResult(jsonObject);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙,请稍后再试...");
			}
		}) {

		};
		App.getInstance().addToRequestQueue(req, TAG);
	}




	
	public static String MakeURL(String p_url, LinkedHashMap<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
		}
		String temStr = url.toString().replace("?&", "?");
		
		return temStr;
	}
	
	/**
	 * 首页访问数据
	 * @param apiListener
     */
	public void getMainData(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(URL_Main, new LinkedHashMap<String, Object>() {{
			put("userid", uid);

		}});
		LogHelper.i(TAG,"---------getMainData-uid-----"+uid);
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());

					LogHelper.i(TAG, "------===首页的所有信息--" + response.toString());
					apiListener.onResult(response);


			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 *  获取任务池中的数据
	 * @param apiListener
     */
	public void getTaskBarData( final APIListener apiListener) {
		String urlString = MakeURL(URL_TaskBar, new LinkedHashMap<String, Object>() {{
			//put("ordContactId", uid);

		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				LogHelper.i(TAG, "-----message-----"+response.toString());
					apiListener.onResult(response);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogHelper.i(TAG, "-----message-----"+error.toString());
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 *  搜索功能获取任务池中的数据
	 * @param apiListener
	 */

	public void getTaskAddData( JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL_ADDBar, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, "-----message-----"+response.toString());
				apiListener.onResult(response);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogHelper.i(TAG, "-----message-----"+error.toString());
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}


	/**
	 * 接受任务
	 */
	public void postTaskAccept(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Task_ITEM_POST_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}


	/**
	 * 访问个人绩效数据
	 * @param apiListener
	 */
	public void getPersonalData(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(PERSONAL_GET_URL, new LinkedHashMap<String, Object>() {{
			put("employeeId", uid);

		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					//{"message":"数据加载成功","result":"success","entity":{"3":2,"2":2,"1":2,"6":2,"5":3,"4":2,"commission":1190},"code":"1"}
					LogHelper.i(TAG, "----个人绩效的所有信息--" + response.toString());
					apiListener.onResult(response);

				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}


	/**
	 * 根据步骤id和用户id获取办过此步骤的公司信息
	 * @param employeeId
	 * @param flowId
	 * @param apiListener
     */
	public void getStepData(final int employeeId, final int flowId, final APIListener apiListener) {
		String urlString = MakeURL(STEPDATA_GET_URL, new LinkedHashMap<String, Object>() {{
			put("employeeId", employeeId);
			put("flowId", flowId);//employeeId=4&flowId=1

		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					LogHelper.i(TAG, "----公司信息--" + response.toString());
					apiListener.onResult(response);

				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 访问预警数据
	 * @param apiListener
	 */
	public void getWarningData(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(WARNING_POST_URL, new LinkedHashMap<String, Object>() {{
			put("userid", uid);
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
					LogHelper.i(TAG, "-----预警-的所有信息--" + response.toString());
					apiListener.onResult(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 访问客户通讯录数据
	 * @param apiListener
	 */
	public void getTXLData( final APIListener apiListener) {
		String urlString = MakeURL(CONTACTSPOST_URL, new LinkedHashMap<String, Object>() {{
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					LogHelper.i(TAG, "----的所有信息--" + response.toString());
					apiListener.onResult(response);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}
	/**
	 * 访问内部通讯录数据
	 * @param apiListener
	 */
	public void getTXLInsideData( final APIListener apiListener) {
		String urlString = MakeURL(CONTACTS_GONGSI_URL, new LinkedHashMap<String, Object>() {{
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					LogHelper.i(TAG, "----的所有信息--" + response.toString());
					apiListener.onResult(response);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 访问公司名称
	 * @param mId
     */
	public void getCompanyName(final String mId, final APIListener apiListener) {
		String urlString = MakeURL(COMPANY_GONGSI_URL, new LinkedHashMap<String, Object>() {{
		put("userid", mId);
	}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
					LogHelper.i(TAG, "----的所有信息--" + response.toString());
					apiListener.onResult(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 获取公司信息和人员信息
	 * @param uid
	 * @param apiListener
     */
	public void getCompamyDatas(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(COMPLETE_NAME_URL, new LinkedHashMap<String, Object>() {{
			put("userid", uid);
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				LogHelper.i(TAG, "----的所有信息--" + response.toString());
				apiListener.onResult(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("服务器繁忙，稍后再试...");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}
}
