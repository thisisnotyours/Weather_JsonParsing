package com.suek.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter adapter;   //myadapter 만들어주기,
    ArrayList<String> items = new ArrayList<>();

    String apiKey = "UTX5iI2LfHnJ%2BV%2BSAYhYgVFE022u%2BZuKGj7XTqF36AVLLah6pJHNOjK6l6iP8ad66T06494rAGLJL8gdfl%2BcgA%3D%3D";
    //String StrArea= "[{'seoul':'108'},{'busan':'159'}]" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);




    }

    Handler handler= new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    };


    public void clickBtn(View view) {
        Toast.makeText(this, "click button", Toast.LENGTH_SHORT).show();

        Thread t= new Thread() {
            public void run() {
                Message msg= handler.obtainMessage();
                handler.sendMessage(msg);

                Log.i("run","aa");
                Date date = new Date();
                date.setTime(date.getTime() - (1000 * 60 * 60 * 24));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String dateStr = sdf.format(date);


                String addr = "http://apis.data.go.kr/1360000/AsosHourlyInfoService/getWthrDataList?"
                        + "serviceKey=" + apiKey
                        + "&dataCD=" + "ASOS"
                        + "&dateCD=" + dateStr
                        + "&startDt=" + "20200831"
                        + "&endDt=" + "20200901"
                        + "&startHh=" + "01"
                        + "&endHh=" + "01"
                        + "&stnlds=" + "108";

                addr= "http://apis.data.go.kr/1360000/AsosHourlyInfoService/getWthrDataList?serviceKey=UTX5iI2LfHnJ%2BV%2BSAYhYgVFE022u%2BZuKGj7XTqF36AVLLah6pJHNOjK6l6iP8ad66T06494rAGLJL8gdfl%2BcgA%3D%3D&numOfRows=10&pageNo=1&dataCd=ASOS&dateCd=HR&stnIds=108&endDt=20200310&endHh=01&startHh=01&startDt=20190120&dataType=JSON";

                try {
                    Log.i("run","bb");
                    URL url= new URL(addr);
                    Log.i("run","url");
                    InputStream is= url.openStream();
                    Log.i("run","is");
                    InputStreamReader isr= new InputStreamReader(is);
                    Log.i("run","isr");
                    BufferedReader reader= new BufferedReader(isr);
                    Log.i("run","reader");
                    StringBuffer buffer= new StringBuffer();
                    Log.i("run","buffer");
                    while (true){
                        Log.i("run","true");
                        String line= reader.readLine();
                        if(line==null) break;

                        buffer.append(line+'\n');
                    }
                    Log.i("run","break");


                    String jsonData= buffer.toString();
                    items.add(jsonData);
                    Log.i("run", ""+items.size());
                    Log.i("run","bb"+jsonData);
                    handler.sendEmptyMessage(0);


                } catch (MalformedURLException e) {
                    Log.i("run","fail");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("run","fail-2");
                    e.printStackTrace();
                }
            }//run
        };   //Thread
        t.start();
    }

}//MainActivity