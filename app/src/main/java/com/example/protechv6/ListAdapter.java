package com.example.protechv6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Sensors> {


    public ListAdapter(Context context, ArrayList<Sensors> userArrayList){

        super(context,R.layout.list_item,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Sensors sensors = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

        }

        ImageView imageView = convertView.findViewById(R.id.sensor_pic);
        TextView sensorText = convertView.findViewById(R.id.sensor_text);
        TextView sensorStatus = convertView.findViewById(R.id.sensor_status);
        TextView lastTriggered = convertView.findViewById(R.id.last_triggered);

        imageView.setImageResource(sensors.imageId);
        sensorText.setText(sensors.sensor);
        sensorStatus.setText(sensors.status);
        lastTriggered.setText(sensors.lastTriggered);


        return convertView;
    }
}