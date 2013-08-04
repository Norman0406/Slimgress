package com.norman0406.ingressex;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

public class InventoryList extends BaseExpandableListAdapter
{    
	public ArrayList<String> mGroupItem, mTempChild;
	public ArrayList<Object> mChildtem = new ArrayList<Object>();
	public LayoutInflater mInflater;
	public Activity mActivity;
	
	public InventoryList(ArrayList<String> grList, ArrayList<Object> childItem)
	{
		mGroupItem = grList;
		mChildtem = childItem;
	}
	
	public void setInflater(LayoutInflater inflater, Activity act)
	{
		mInflater = inflater;
		mActivity = act;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return null;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		mTempChild = (ArrayList<String>)mChildtem.get(groupPosition);
		TextView text = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.inventory_childrow, null);
		}
		text = (TextView)convertView.findViewById(R.id.agentlevel);
		text.setText(mTempChild.get(childPosition));
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mActivity, mTempChild.get(childPosition), Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition)
	{
		return ((ArrayList<String>)mChildtem.get(groupPosition)).size();
	}
	
	@Override
	public Object getGroup(int groupPosition)
	{
		return null;
	}
	
	@Override
	public int getGroupCount()
	{
		return mGroupItem.size();
	}
	
	@Override
	public void onGroupCollapsed(int groupPosition)
	{
		super.onGroupCollapsed(groupPosition);
	}
	
	@Override
	public void onGroupExpanded(int groupPosition)
	{
		super.onGroupExpanded(groupPosition);
	}
	
	@Override
	public long getGroupId(int groupPosition)
	{
		return 0;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.inventory_grouprow, null);
		}
		((CheckedTextView)convertView).setText(mGroupItem.get(groupPosition));
		((CheckedTextView)convertView).setChecked(isExpanded);
		return convertView;
	}
	
	@Override
	public boolean hasStableIds()
	{
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return false;
	}
}