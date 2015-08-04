package com.jing.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.scorelist.R;
import com.jing.bean.GradeInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AvgAdapter extends BaseAdapter {

	// 填充数据的list
	private List<GradeInfo> list;
	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;
	// 上下文
	private Context context;
	// 用来导入布局
	private LayoutInflater inflater = null;

	public AvgAdapter(Context con, List<GradeInfo> list) {
		this.context = con;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		AvgAdapter.isSelected = new HashMap<Integer, Boolean>();
		initSelectedData();
	}

	// 初始化isSelected的数据
	private void initSelectedData() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> select) {
		AvgAdapter.isSelected = select;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.grade_avg_item, parent,
					false);
			holder.fileNameText = (CheckBox) convertView
					.findViewById(R.id.avg_item_name);
			holder.fileCreditText = (TextView) convertView
					.findViewById(R.id.avg_item_credit);
			holder.fileScoreText = (TextView) convertView
					.findViewById(R.id.avg_item_score);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.fileNameText.setText(list.get(position).getName());
		holder.fileNameText.setChecked(getIsSelected().get(position));
		holder.fileCreditText.setText(list.get(position).getCreditname() + " "
				+ list.get(position).getCredit());
		holder.fileScoreText.setText(list.get(position).getScorename() + " "
				+ list.get(position).getFinalscore());

		return convertView;
	}

	public class ViewHolder {
		public CheckBox fileNameText;
		public TextView fileScoreText;
		public TextView fileCreditText;
	}

}
