package com.xht.android.serverhelp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xht.android.serverhelp.R;
import com.xht.android.serverhelp.util.LogHelper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private static final String TAG ="SortAdapter" ;
	private List<SortModel> list = null;
	private Context mContext;
	private TextView catalog;
	private ImageView headimgview;
	private TextView title;
	private TextView mPhone;
	private String group;
	private String phone;
	private String subname;

	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);


		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_sort_listview, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.tvPhone = (TextView) view.findViewById(R.id.mPhone);
			viewHolder.imHead = (ImageView) view.findViewById(R.id.head_imgview);
			view.setTag(viewHolder);
		} else {

			viewHolder = (ViewHolder) view.getTag();
		}
		//根据position获取分类的首字母的Char ascii值
				int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
			//viewHolder.tvLetter.setText(mContent.getName());


		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}


		//区分出汉字与数字正则表达式
		String name = this.list.get(position).getName();
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
		Matcher m = p.matcher( name );
		while ( m.find() ) {
			subname = m.group();
			boolean isNum = subname.matches("[0-9]+");
			if (!isNum){
				viewHolder.tvTitle.setText(subname);
				LogHelper.i(TAG,"-------====="+subname+"---"+ subname);
				return view;
			}
		}

		viewHolder.tvTitle.setText(subname);
		return view;
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		TextView tvPhone;
		ImageView imHead;
	}
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}
	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
	@Override
	public Object[] getSections() {
		return null;
	}
}