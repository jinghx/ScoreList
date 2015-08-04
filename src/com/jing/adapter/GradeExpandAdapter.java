package com.jing.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scorelist.R;
import com.jing.bean.GradeInfo;

public class GradeExpandAdapter extends BaseExpandableListAdapter {

	private Context context;
	private LayoutInflater inflater;
	private String[] groupStrs;
	private String[] groupCredit;
	private List<ArrayList<GradeInfo>> datas;
	private int gPosition = -1;
	private int cPosition = -1;

	public GradeExpandAdapter(Context con, List<ArrayList<GradeInfo>> items) {
		this.context = con;
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// groupStrs = context.getResources().getStringArray(id);
		this.groupStrs = new String[] { "必修", "限选", "通选", "大一上", "大一下",
				"大一小学期", "大二上", "大二下", "大二小学期", "大三上", "大三下", "大三小学期", "大四上",
				"大四下" };
		this.groupCredit = new String[] { "", "", "", "", "", "", "", "", "",
				"", "", "", "", "" };
		this.datas = items;
		for (int i = 0; i < datas.size(); i++) {
			double c = 0;
			for (int j = 0; j < datas.get(i).size(); j++) {
				c += Double.parseDouble(datas.get(i).get(j).getCredit().trim());
			}
			groupCredit[i] = c + "";
		}

	}

	public void setData(List<ArrayList<GradeInfo>> items) {
		this.datas = items;
	}

	@Override
	public GradeInfo getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return datas.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grade_list_item, parent,
					false);
			holder = new ViewHolder();
			holder.childNameText = (TextView) convertView
					.findViewById(R.id.item_name);
			holder.childinfoText = (TextView) convertView
					.findViewById(R.id.item_txtinfo);
			holder.childScoreText = (TextView) convertView
					.findViewById(R.id.item_score);
			holder.childCreditText = (TextView) convertView
					.findViewById(R.id.item_credit);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.layout_other);
			holder.infoLinearLayout = (LinearLayout) convertView
					.findViewById(R.id.item_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String strs = getChild(groupPosition, childPosition).getName();
		String num = getChild(groupPosition, childPosition).getCoursenumber();
		holder.childNameText.setText(strs);

		holder.childScoreText.setText(getChild(groupPosition, childPosition)
				.getScorename()
				+ "   "
				+ getChild(groupPosition, childPosition).getFinalscore());
		holder.childCreditText.setText(getChild(groupPosition, childPosition)
				.getCreditname()
				+ "   "
				+ getChild(groupPosition, childPosition).getCredit());
		if (childPosition == cPosition && groupPosition == gPosition) {
			if (!num.equals("title") && num != "title") {// 标题不响应
				holder.linearLayout.setVisibility(View.VISIBLE);
				holder.infoLinearLayout.setVisibility(View.VISIBLE);
			}
			String temp = "";
			temp += "课  程  名:"
					+ getChild(groupPosition, childPosition).getName() + "\n";
			temp += "课  程  号:"
					+ getChild(groupPosition, childPosition).getCoursenumber()
					+ "\n";
			temp += "学        分:"
					+ getChild(groupPosition, childPosition).getCredit() + "\n";
			temp += "实验成绩:"
					+ getChild(groupPosition, childPosition).getExpscore()
					+ "\n";
			temp += "平时成绩:"
					+ getChild(groupPosition, childPosition).getComscore()
					+ "\n";
			temp += "期末成绩:"
					+ getChild(groupPosition, childPosition).getTestscore()
					+ "\n";
			temp += "总  成  绩:"
					+ getChild(groupPosition, childPosition).getFinalscore()
					+ "\n";
			holder.childinfoText.setText(temp);
		} else {
			holder.linearLayout.setVisibility(View.GONE);
			holder.infoLinearLayout.setClickable(false);
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return datas.get(groupPosition).size();
	}

	@Override
	public List<GradeInfo> getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return datas.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grade_list_group, null);
		}
		GroupViewHolder holder = new GroupViewHolder();
		// holder.groupIcon = (ImageView) convertView.findViewById(R.id);
		// holder.groupIcon.setBackgroundResource(R.drawable.ic_arrow_up);
		// if(isExpanded){
		// holder.groupIcon.setBackgroundResource(R.drawable.ic_arrow_down);
		// }
		holder.groupName = (TextView) convertView
				.findViewById(R.id.tv_group_name);
		holder.groupName.setText(groupStrs[groupPosition]);
		holder.groupCount = (TextView) convertView
				.findViewById(R.id.tv_group_count);
		holder.groupCount.setText(groupCredit[groupPosition] + "  / "
				+ datas.get(groupPosition).size() + "");

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private class GroupViewHolder {
		ImageView groupIcon;
		TextView groupName;
		TextView groupCount;
	}

	class ViewHolder {
		public TextView childNameText;
		public TextView childScoreText;
		public TextView childCreditText;
		public TextView childinfoText;
		public LinearLayout linearLayout;
		public LinearLayout infoLinearLayout;
	}

	public void setPosition(int g, int c) {
		this.cPosition = c;
		this.gPosition = g;
	}

}
