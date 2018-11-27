package com.bmh.trackchild.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;

import com.bmh.trackchild.R;
import com.bmh.trackchild.UI.Drawer;

public class ChildActivity extends AppCompatActivity
{
	Drawer drawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_child);
		drawer=new Drawer(this);

	}


}
