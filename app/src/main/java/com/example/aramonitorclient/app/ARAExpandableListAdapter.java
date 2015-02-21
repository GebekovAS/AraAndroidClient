package com.example.aramonitorclient.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by GebekovAS on 19.02.2015.
 */
public class ARAExpandableListAdapter implements ExpandableListAdapter {

    private List<Map<String,String>> gGroupList;
    private List<List<Map<String,String>>> gChildList;
    private Context gOwnerContext;
    private int gGroupLayoutId;
    private int gChildLayoutId;
    private int[] gGroupTextViewsIdArray;
    private int[] gChildTextViewsIdArray;
    private String[] gGroupKeyNamesArray;
    private String[] gChildKeyNamesArray;

    private List<String> gGroupColors;

    //Конструктор объекта
    public ARAExpandableListAdapter (
            Context ownerContext,
            List<Map<String,String>> groupList,
            int groupLayoutId,
            String[] groupKeyNamesArray,
            int[] groupTextViewsIdArray,
            List<List<Map<String,String>>> childList,
            int childLayoutId,
            String[] childKeyNamesArray,
            int[] childTextViewsIdArray,
            List<String> colorList ) {

        gGroupList=groupList;
        gChildList=childList;
        gOwnerContext= ownerContext;
        gGroupLayoutId=groupLayoutId;
        gChildLayoutId=childLayoutId;
        gGroupTextViewsIdArray=groupTextViewsIdArray;
        gChildTextViewsIdArray=childTextViewsIdArray;
        gGroupKeyNamesArray=groupKeyNamesArray;
        gChildKeyNamesArray=childKeyNamesArray;
        gGroupColors=colorList;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return gGroupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return gChildList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return gGroupList.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return gChildList.get(i).get(i2);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view==null) {
            LayoutInflater inflater = (LayoutInflater) gOwnerContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(gGroupLayoutId,null);
        }

        //Закрашиваем Item'сы
        if (b) {
            //Если группа раскрыта
            view.setBackgroundColor(Color.parseColor("#AAAAAA"));
        }
        else {
            //если группа скрыта
            view.setBackgroundColor(Color.parseColor(gGroupColors.get(i)));
        }

        TextView textGroup=(TextView)view.findViewById(gGroupTextViewsIdArray[0]);
        TextView subStringGroup=(TextView)view.findViewById(gGroupTextViewsIdArray[1]);

        textGroup.setText(gGroupList.get(i).get(gChildKeyNamesArray[0]));
        subStringGroup.setText(gGroupList.get(i).get(gChildKeyNamesArray[1]));

        return view;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        if (view==null) {
            LayoutInflater inflater = (LayoutInflater) gOwnerContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(gChildLayoutId,null);
        }

        if (b) {
            //Если группа раскрыта
        }
        else {
            //если группа скрыта
        }

        TextView textChild=(TextView)view.findViewById(gChildTextViewsIdArray[0]);
        TextView subStringChild=(TextView)view.findViewById(gChildTextViewsIdArray[1]);

        textChild.setText(gChildList.get(i).get(i2).get(gChildKeyNamesArray[0]));
        subStringChild.setText(gChildList.get(i).get(i2).get(gChildKeyNamesArray[1]));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l2) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
}
