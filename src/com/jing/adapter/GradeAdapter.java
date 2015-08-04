package com.jing.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scorelist.R;
import com.jing.bean.GradeInfo;

public class GradeAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<GradeInfo> gradeList;
	private int currentPosition = -1;

	public GradeAdapter(Context con, List<GradeInfo> gList) {
		this.context = con;
		this.gradeList = gList;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {

		return gradeList.size();
	}

	public Object getItem(int arg0) {
		return gradeList.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grade_list_item, parent,
					false);
			holder = new ViewHolder();
			holder.fileNameText = (TextView) convertView
					.findViewById(R.id.item_name);
			holder.fileinfoText = (TextView) convertView
					.findViewById(R.id.item_txtinfo);
			holder.fileScoreText = (TextView) convertView
					.findViewById(R.id.item_score);
			holder.fileCreditText = (TextView) convertView
					.findViewById(R.id.item_credit);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.layout_other);
			holder.infoLinearLayout = (LinearLayout) convertView
					.findViewById(R.id.item_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String strs = gradeList.get(position).getName();
		String num = gradeList.get(position).getCoursenumber();
		holder.fileNameText.setText(strs);

		holder.fileScoreText.setText(gradeList.get(position).getScorename()
				+ "   " + gradeList.get(position).getFinalscore());
		holder.fileCreditText.setText(gradeList.get(position).getCreditname()
				+ "   " + gradeList.get(position).getCredit());
		if (position == currentPosition) {
			holder.linearLayout.setVisibility(View.VISIBLE);
			holder.infoLinearLayout.setVisibility(View.VISIBLE);
			String temp = "";
			temp += "\n课  程  名:" + gradeList.get(position).getName() + "\n";
			temp += "课  程  号:" + gradeList.get(position).getCoursenumber()
					+ "\n";
			temp += "学        分:" + gradeList.get(position).getCredit() + "\n";
			temp += "实验成绩:" + gradeList.get(position).getExpscore() + "\n";
			temp += "平时成绩:" + gradeList.get(position).getComscore() + "\n";
			temp += "期末成绩:" + gradeList.get(position).getTestscore() + "\n";
			temp += "总  成  绩:" + gradeList.get(position).getFinalscore() + "\n\n";
			temp += "排        名 : " + gradeList.get(position).getRank() + "\n\n";
			temp += "最  高  分 : " + gradeList.get(position).getMax() + "\n";
			temp += "最  低  分 : " + gradeList.get(position).getMin() + "\n";		
			temp += "人        数 : " + gradeList.get(position).getChoosedNum() + "\n";
			holder.fileinfoText.setText(temp);
		} else {
			holder.linearLayout.setVisibility(View.GONE);
			holder.infoLinearLayout.setClickable(false);
		}

		return convertView;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	class ViewHolder {
		public TextView fileNameText;
		public TextView fileScoreText;
		public TextView fileCreditText;
		public TextView fileinfoText;
		public LinearLayout linearLayout;
		public LinearLayout infoLinearLayout;
	}
}
