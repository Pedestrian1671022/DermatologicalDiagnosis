package com.example.pedestrian.dermatologicaldiagnosis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

public class IllnessAdapter extends BaseAdapter {
    private Context context;
    private List<Illness> data;

    public IllnessAdapter(Context context, List<Illness> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Illness getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Illness illness = getItem(position);
        View view;
        Holder holder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.illnessitem, null);
            holder = new Holder();
            holder.textView = (TextView) view.findViewById(R.id.textView);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (Holder) convertView.getTag();
        }
        holder.textView.setText("诊断结果");
        return view;
    }

    class Holder {
        private TextView textView;
    }
}
