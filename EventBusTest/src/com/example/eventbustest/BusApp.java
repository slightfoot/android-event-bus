package com.example.eventbustest;

import com.example.eventbustest.EventBus.EventBusRetriever;

import android.app.Application;
import android.os.Binder;
import android.os.Handler;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;


public class BusApp extends Application
	implements EventBusRetriever
{
	private static final String TAG = BusApp.class.getSimpleName();
	
	private static BusApp instance;
	
	private EventBus mEventBus;
	
	private Messenger mMessenger;
	private TestBinder mTestBinder;
	private Handler mHandler;
	
	public static BusApp get()
	{
		return instance;
	}
	
	public BusApp()
	{
		super();
		instance = this;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.e(TAG, "onCreate() " + this.hashCode());
		mEventBus = new EventBus("App EventBus");
	}
	
	
	@Override
	public EventBus getEventBus()
	{
		return mEventBus;
	}
	
	private static class TestBinder extends Binder
	{
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException
		{
			return super.onTransact(code, data, reply, flags);
		}
	}
}
