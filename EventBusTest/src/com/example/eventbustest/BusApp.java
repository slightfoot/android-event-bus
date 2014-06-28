package com.example.eventbustest;

import com.example.eventbustest.EventBus.EventBusRetriever;

import android.app.Application;


public class BusApp extends Application
	implements EventBusRetriever
{
	private static BusApp instance;
	
	private EventBus mEventBus;
	
	
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
		mEventBus = new EventBus("App EventBus");
	}
	
	
	@Override
	public EventBus getEventBus()
	{
		return mEventBus;
	}
}
