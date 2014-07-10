package com.example.eventbustest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class OtherActivity extends Activity
{
	private static final String TAG = OtherActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setTitle(getString(R.string.app_name) + " pid{" + android.os.Process.myPid() + 
			"} uid{" + android.os.Process.myUid() + "}");
		
		Log.e(TAG, "Application:" + System.identityHashCode(BusApp.get()));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.id.send_message){
		
		}
		return super.onOptionsItemSelected(item);
	}
}
