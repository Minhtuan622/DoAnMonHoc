package com.example.doanmonhoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {

    EditText edtTimKiem;
    Button btnTimKiem, btnView7Day;
    TextView txtCountry, txtCity, txtDayUpdate, txtNhietDo, txtTrangThai, txtDoAm, txtMay, txtGio;
    ImageView imgIcon;

    String City = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // ánh xạ
        edtTimKiem = (EditText) findViewById(R.id.editTextTimkiem);
        btnTimKiem = (Button) findViewById(R.id.buttonTimkiem);
        btnView7Day = (Button) findViewById(R.id.button7day);
        txtCity = (TextView) findViewById(R.id.textViewthanhpho);
        txtCountry = (TextView) findViewById(R.id.textViewquocgia);
        txtGio = (TextView) findViewById(R.id.textViewGio);
        txtMay = (TextView) findViewById(R.id.textViewMay);
        txtDoAm = (TextView) findViewById(R.id.textViewDoAm);
        txtNhietDo = (TextView) findViewById(R.id.textViewNhietdo);
        txtTrangThai = (TextView) findViewById(R.id.textViewTrangthai);
        txtDayUpdate = (TextView) findViewById(R.id.textViewNgayCapNhat);
        imgIcon = (ImageView) findViewById(R.id.imghinh);

        getCurrentWeatherData("saigon");

        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtTimKiem.getText().toString().trim();

                // nếu chưa nhập dữ liệu thì mặc định sẽ là sài gòn, còn ngược lại thì sẽ tìm theo dữ liệu mình nhập
                if (city.equals("")){
                    City = "saigon";
                    getCurrentWeatherData(City);
                }else {
                    City = city;
                    getCurrentWeatherData(City);
                }
            }
        });
        btnView7Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtTimKiem.getText().toString().trim();
                Intent intent = new Intent(WeatherActivity.this, View7dayActivity.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }

    public void getCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&appid=8b0ea62f84529fdeae4547e2960e6aa0&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String day = jsonObject.getString("dt");
                    String name = jsonObject.getString("name");
                    txtCity.setText(name);

                    long l = Long.parseLong(day);
                    Date date = new Date(1 * 1000L);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                    String Day = simpleDateFormat.format(date);

                    txtDayUpdate.setText(Day);
                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String status = jsonObjectWeather.getString("main");
                    String icon = jsonObjectWeather.getString("icon");

                    Picasso.with(WeatherActivity.this).load("http://openweathermap.org/img/w/" + icon +".png").into(imgIcon);
                    txtTrangThai.setText(status);

                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    String nhietdo = jsonObjectMain.getString("temp");
                    String doam = jsonObjectMain.getString("humidity");

                    Double a = Double.valueOf(nhietdo);
                    String Nhietdo = String.valueOf(a.intValue());

                    txtNhietDo.setText(Nhietdo + " độ C");
                    txtDoAm.setText(doam + "%");

                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                    String gio = jsonObjectWind.getString("speed");
                    txtGio.setText(gio + " m/s");

                    JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                    String may = jsonObjectCloud.getString("all");
                    txtMay.setText(may + "%");

                    JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                    String country = jsonObjectSys.getString("country");
                    txtCountry.setText(country);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }
}