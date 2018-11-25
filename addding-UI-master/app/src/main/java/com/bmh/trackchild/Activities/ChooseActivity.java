package com.bmh.trackchild.Activities;
import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.StaticValues;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class ChooseActivity extends AppCompatActivity{
    Intent intent;
    Switch swc1,swc2,swc3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        swc1 = (Switch) findViewById(R.id.switch1);
        swc2 = (Switch) findViewById(R.id.switch2);
        swc3 = (Switch) findViewById(R.id.switch3);




        swc1.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){//블루투스 기능
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str=String.valueOf(isChecked);
                // Toast.makeText(getApplicationContext(),"체크상태 = "+isChecked,Toast.LENGTH_SHORT);

                if(isChecked){
                    Toast.makeText(getApplicationContext(),"on",Toast.LENGTH_SHORT).show();
                    Log.d("test","on");
                    Intent intent = new Intent(
                            getApplicationContext(),//현재제어권자
                            ChildDeviceActivity.class); // 이동할 컴포넌트
                    startActivity(intent); // 서비스 시작


                }
                else{
                    Toast.makeText(getApplicationContext(),"off",Toast.LENGTH_SHORT).show();

                }
            }
        });

        swc2.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = String.valueOf(isChecked);
                // Toast.makeText(getApplicationContext(),"체크상태 = "+isChecked,Toast.LENGTH_SHORT);

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_SHORT).show();
                    Log.d("test", "on");
                    Intent intent = new Intent(
                            getApplicationContext(),//현재제어권자
                            BatteryService.class); // 이동할 컴포넌트
                    startService(intent); // 서비스 시작


                } else {
                    Toast.makeText(getApplicationContext(), "off", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(
                            getApplicationContext(),//현재제어권자
                            BatteryService.class); // 이동할 컴포넌트
                    stopService(intent); // 서비스 종료
                }
            }
        });


        swc3.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){//충격감지 기능
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str=String.valueOf(isChecked);
                // Toast.makeText(getApplicationContext(),"체크상태 = "+isChecked,Toast.LENGTH_SHORT);

                if(isChecked){
                    Toast.makeText(getApplicationContext(),"on",Toast.LENGTH_SHORT).show();
                    Log.d("test","on");
                    Intent intent = new Intent(
                            getApplicationContext(),//현재제어권자
                            MyService.class); // 이동할 컴포넌트
                    startService(intent); // 서비스 시작


                }
                else{
                    Toast.makeText(getApplicationContext(),"off",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(
                            getApplicationContext(),//현재제어권자
                            MyService.class); // 이동할 컴포넌트
                    stopService(intent); // 서비스 종료
                }
            }
        });
    }
   /* @Override
        public void onClick (View view) {
        if (R.id.switch1 == view.getId()) {
            Intent intent = new Intent(this, ChildDeviceActivity.class);
            startActivity(intent);
        } else if (R.id.switch2 == view.getId()){
            Intent intent = new Intent(this, ChildDeviceActivity.class);
        startActivity(intent);
    }
    finish();
        }*/


}
