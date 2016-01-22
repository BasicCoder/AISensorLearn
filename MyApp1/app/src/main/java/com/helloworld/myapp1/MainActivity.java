package com.helloworld.myapp1;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.util.List;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private long initTime = 0;
    private long lastTime = 0;
    private long curTime = 0;
    private long duration = 0;

    private float last_x = 0.0f;
    private float last_y = 0.0f;
    private float last_z = 0.0f;

    private float shake = 0.0f;
    private float totalShake = 0.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final TextView tx1 = (TextView)findViewById(R.id.TextView01);
        SensorManager sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
        tx1.setText("This phone have " + allSensors.size() + " sensor,they are: \n");
        for (Sensor s: allSensors){
            String tempString = "\n" + "  Sensor name: " + s.getName() + "\n" + "  Sensor version:" + s.getVersion() + "\n" +"  Sensor provider:"
                    + s.getVendor() +"\n";

            switch(s.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    tx1.setText(tx1.getText().toString()+ s.getType() + " 加速度传感器accelerometer" + tempString);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 陀螺仪传感器gyroscope" + tempString);
                    break;
                case Sensor.TYPE_LIGHT:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 环境光线传感器light" + tempString);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 电磁场传感器magnetic field" + tempString);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 方向传感器orientation" + tempString);
                    break;
                case Sensor.TYPE_PRESSURE:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 压力传感器pressure" + tempString);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 距离传感器proximity" + tempString);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 温度传感器temperature" + tempString);
                    break;
                default:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 未知传感器" + tempString);
                    break;
            }
        }
        final int OldStringSize = tx1.getText().toString().length();
        Sensor acceleromererSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener acceleromererListener = new SensorEventListener(){
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy){

            }
            @Override
            public void onSensorChanged(SensorEvent event){
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                curTime = System.currentTimeMillis();
                if((curTime - lastTime) > 100){
                    duration = curTime - lastTime;
                    if(last_x == 0.0 && last_y == 0.0f && last_z == 0.0f){
                        initTime = System.currentTimeMillis();
                    }
                    else{
                        shake = (Math.abs(x - last_x) + Math.abs(y - last_y) + Math.abs(z - last_z)) / duration * 100;
                    }
                    totalShake += shake;
                    if(totalShake > 10 && totalShake / (curTime - initTime) * 1000 > 10){
                        //startRecord();
                        //initShake();
                    }

                    tx1.setText(tx1.getText().toString().substring(0,OldStringSize) +
                            "\nx="+ Float.toString(x)+
                            "\ny="+ Float.toString(y)+
                            "\nz="+ Float.toString(z)+
                            "\nshake="+ Float.toString(shake)+
                            "\n总体晃动幅度=" + totalShake +
                            "\n平均晃动幅度=" + totalShake / (curTime - initTime) * 1000 );
                }
                last_x = x;
                last_y = y;
                last_z = z;
                lastTime = curTime;
            }
        };
        sm.registerListener(acceleromererListener, acceleromererSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
