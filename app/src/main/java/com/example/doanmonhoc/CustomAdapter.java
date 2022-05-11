package com.example.doanmonhoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Weather> weatherArrayList;

    public CustomAdapter(Context context, ArrayList<Weather> weatherArrayList) {
        this.context = context;
        this.weatherArrayList = weatherArrayList;
    }

    @Override
    public int getCount() {
        // trả về kích thước của arraylist
        return weatherArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return weatherArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_listview, null);

        Weather weather = weatherArrayList.get(i);

        // ánh xạ các view trong listview mà mình đã custom
        TextView txtDay = (TextView) view.findViewById(R.id.textviewNgay);
        TextView txtStatus = (TextView) view.findViewById(R.id.textviewTrangthai);
        TextView txtMaxtemp = (TextView) view.findViewById(R.id.textviewMax);
        TextView txtMintemp = (TextView) view.findViewById(R.id.textviewMin);
        ImageView imgStatus = (ImageView) view.findViewById(R.id.imageviewTrangthai);

        // gán giá trị cho textview
        txtDay.setText(weather.day);
        txtStatus.setText(weather.Status);
        txtMaxtemp.setText(weather.MaxTemp + "ºC");
        txtMintemp.setText(weather.MinTemp + "ºC");

        // dùng hàm picasso để lấy dữ liệu về từ internet
        Picasso.with(context).load("http://openweathermap.org/img/w/" + weather.Images +".png").into(imgStatus);
        return view;
    }
}
