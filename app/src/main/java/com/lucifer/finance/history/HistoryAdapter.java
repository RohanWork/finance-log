package com.lucifer.finance.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.lucifer.finance.R;
import com.lucifer.finance.smsfunctionality.SmsReceiver;

import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Map.Entry<String, SmsReceiver.DailyAmount>> entries;

    public HistoryAdapter(Context context, List<Map.Entry<String, SmsReceiver.DailyAmount>> entries) {
        this.context = context;
        this.entries = entries;
    }

    @Override
    public int getGroupCount() {
        return entries.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1 + entries.get(groupPosition).getValue().bankTransactionCount.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return entries.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (childPosition == 0) {
            return entries.get(groupPosition).getValue();
        } else {
            List<Map.Entry<String, Integer>> bankEntries = new ArrayList<>(entries.get(groupPosition).getValue().bankTransactionCount.entrySet());
            return bankEntries.get(childPosition - 1);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_group, parent, false);
        }

        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView amountTextView = convertView.findViewById(R.id.amountTextView);

        Map.Entry<String, SmsReceiver.DailyAmount> entry = entries.get(groupPosition);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date parsedDate = inputFormat.parse(entry.getKey());
            if (parsedDate != null) {
                String formattedDate = outputFormat.format(parsedDate);
                dateTextView.setText(formattedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        amountTextView.setText("Total Amount Flow :  ₹" + entry.getValue().total());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_child, parent, false);
        }

        TextView childTextView = convertView.findViewById(R.id.childTextView);

        if (childPosition == 0) {
            SmsReceiver.DailyAmount amount = entries.get(groupPosition).getValue();
            childTextView.setText("Credited Amount    :  ₹" + amount.credited +
                                  "\nDebited Amount     :  ₹" + amount.debited);
        } else {
            List<Map.Entry<String, Integer>> bankEntries = new ArrayList<>(entries.get(groupPosition).getValue().bankTransactionCount.entrySet());
            Map.Entry<String, Integer> bankEntry = bankEntries.get(childPosition - 1);
            childTextView.setText("Transactions : "+bankEntry.getValue()+" "+bankEntry.getKey());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}