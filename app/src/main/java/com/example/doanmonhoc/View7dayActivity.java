package com.example.doanmonhoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

public class View7dayActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView txtCityname;
    ListView lvView7day;
    CustomAdapter customAdapter;
    ArrayList<Weather> weatherArray;

    String cityname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view7day);

        // ánh xạ
        imgBack = (ImageView)findViewById(R.id.imageviewBack);
        txtCityname = (TextView)findViewById(R.id.textviewTemTp);
        lvView7day = (ListView)findViewById(R.id.listview7day);
        weatherArray = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(View7dayActivity.this, weatherArray);
        lvView7day.setAdapter(customAdapter);

        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("name", "dữ liệu truyền qua: " + city);

        // nếu chưa nhập dữ liệu thì dữ liệu sẽ mặc định là saigon, còn ngược lại thì sẽ là dữ liệu mình nhập
        if (city.equals("")) {
            cityname = "saigon";
            get7dayData(cityname);
        }else {
            cityname = city;
            get7dayData(cityname);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void get7dayData(String data) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&cnt=7&appid=8b0ea62f84529fdeae4547e2960e6aa0";
        RequestQueue requestQueue = Volley.newRequestQueue(View7dayActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // khởi tạo jsonobject và lấy dữ liệu từ trong biến response
                    JSONObject jsonObject = new JSONObject(response);
                    // lấy biến city từ trong jsonobject
                    JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                    // tạo biến String name để hứng giá trị "name" trong jsonobject
                    String name = jsonObjectCity.getString("name");
                    // set biến "name" vừa hứng được cho textview
                    txtCityname.setText(name);

                    // khởi tạo jsonarraylist để lấy dữ liệu từ trong jsonarray
                    // dữ liệu của list nằm ở bên trong nên để lấy được chúng ta đi từ ngoài cùng vào => đi từ jsonobject để lấy
                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                    // dùng vòng lặp for để đọc hết những giá trị ở trong jsonarray
                    // biến i chạy từ vị trí thứ 0 cho tới hết độ dài của mảng
                    for (int i = 0; i < jsonArrayList.length(); i++) {
                        // khởi tạo jsonobjectList, biến này đi từ jsonarraylist vào
                        // truyền vào i để cho vị trí tự tăng dần theo ngày
                        JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                        // khởi tạo biến String ngày để hứng giá trị "dt" từ trong jsonobjectlist
                        String ngay = jsonObjectList.getString("dt");

                        // chuyển đổi định dạng ngày cho giá trị vừa lấy được từ trong jsonobjectlist
                        // chuyển đổi định dạng ngay ở trên từ kiểu String sang kiểu long
                        long l = Long.parseLong(ngay);
                        // vì kiểu dữ liệu chúng ta lấy về là giây, còn format là mili giây nên chúng ta sẽ lấy giá trị của biến long nhân cho 1000L để chuyển về dạng mili giây
                        Date date = new Date(1 * 1000L);
                        /** khởi tạo một simpleformat và truyền vào những tham số để format
                        * EEEE => trả về thứ
                        * yyyy => trả về năm ở dạng 4 số
                        * MM => trả về tháng ở dạng số -> mm => trả về ngày ở dạng chữ
                        * dd => trả về ngày ở dạng số
                        */
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                        // tạo biến String để hứng giá trị sau khi đã format thành công
                        String Day = simpleDateFormat.format(date);

                        // khởi tạo biến để lấy nhiệt độ từ trong jsonobject
                        // nhiệt độ nằm ở trong phần "main" nên chúng ta lấy phần này thì mới lấy được nhiệt độ
                        // biến được lấy từ ngoài vào => chúng ta sẽ dùng jsonobjectList để lấy "main"
                        JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
                        // tạo 2 biến để lấy giá trị max và min từ trong jsonobjecttemp mà mình vừa tạo bên trên
                        String max = jsonObjectTemp.getString("temp_max");
                        String min = jsonObjectTemp.getString("temp_min");

                        /**
                         * hàm dùng để làm tròn nhiệt độ
                         * hàm sẽ đổi từ kiểu string về double, sau đó từ double chuyển về kiểu int
                         * sau đó sẽ chuyển lại về dạng chuỗi
                         */
                        Double a = Double.valueOf(max);
                        Double b = Double.valueOf(min);
                        String NhietdoMax = String.valueOf(a.intValue());
                        String NhietdoMin = String.valueOf(b.intValue());

                        // gọi jsonarray và đặt tên là jsonarrayweather
                        // do đi từ jsonobject vào nên chúng ta sẽ gọi nó và lấy từ jsonarray và truyền vào string "weather"
                        JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                        // từ jsonarray chúng ta sẽ đi vào cặp thẻ của jsonobject
                        // khởi tạo jsonobject và đặt tên là jsonobjectweather
                        // do bên trong jsonarray này chỉ có 1 cặp thẻ duy nhất nên chúng ta sẽ truyền vị trí của cặp thẻ đó - chính là vị trí thứ 0
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        // khởi tạo status để lấy dữ liệu của "description" từ jsonobjectweather
                        String status = jsonObjectWeather.getString("description");
                        // tương tự như vậy với icon
                        String icon = jsonObjectWeather.getString("icon");

                        // gọi mảng ra và gán những giá trị vào mảng
                        weatherArray.add(new Weather(Day, status, icon, max, min));
                    }
                    // nếu có dữ liệu mới thì sẽ cập nhật lại
                    customAdapter.notifyDataSetChanged();
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