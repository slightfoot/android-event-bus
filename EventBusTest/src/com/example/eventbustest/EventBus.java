package com.example.eventbustest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public class EventBus
{
	private static Object sLock = new Object();
	private Handler mHandler = new Handler(Looper.getMainLooper());
	private ArrayList<EventSubscriber> mSubscribers = new ArrayList<EventSubscriber>();
	private final String mName;
	
	
	public EventBus(String name)
	{
		mName = name;
	}
	
	public void subscribe(Object subscriber)
	{
		synchronized(sLock){
			for(Method method : subscriber.getClass().getDeclaredMethods()){
				if(method.getAnnotation(EventMethod.class) != null){
					Class<?>[] paramTypes = method.getParameterTypes();
					if(paramTypes.length != 1){
						throw new IllegalStateException("EventMethod must have only one paramter");
					}
					method.setAccessible(true);
					EventSubscriber eventSubscriber = new EventSubscriber(subscriber, method, paramTypes[0]);
					if(BuildConfig.DEBUG){
						logCall("subscribe", eventSubscriber);
					}
					mSubscribers.add(eventSubscriber);
				}
			}
		}
	}
	
	public void unsubscribe(Object subscriber)
	{
		synchronized(sLock){
			Iterator<EventSubscriber> iterator = mSubscribers.iterator();
			while(iterator.hasNext()){
				EventSubscriber eventSubscriber = iterator.next();
				if(subscriber.equals(eventSubscriber.subscriber)){
					if(BuildConfig.DEBUG){
						logCall("unsubscribe", eventSubscriber);
					}
					iterator.remove();
				}
			}
		}
	}
	
	public void send(final Object message)
	{
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				Class<?> type = message.getClass();
				synchronized(sLock){
					for(EventSubscriber eventSubscriber : mSubscribers){
						if(type.equals(eventSubscriber.type)){
							try{
								if(BuildConfig.DEBUG){
									logCall("send", eventSubscriber);
								}
								eventSubscriber.method.invoke(eventSubscriber.subscriber, message);
							}
							catch(IllegalAccessException e){
								e.printStackTrace();
							}
							catch(IllegalArgumentException e){
								e.printStackTrace();
							}
							catch(InvocationTargetException e){
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
	}
	
	private void logCall(String name, EventSubscriber subscriber)
	{
		Log.d(mName, name + ": " + 
			subscriber.subscriber.getClass().getSimpleName() + '.' + 
			subscriber.method.getName() + '(' + 
			subscriber.type.getSimpleName() + ')');
	}
	
	private static class EventSubscriber
	{
		public Object   subscriber;
		public Method   method;
		public Class<?> type;
		
		
		public EventSubscriber(Object subscriber, Method method, Class<?> type)
		{
			this.subscriber = subscriber;
			this.method     = method;
			this.type       = type;
		}
	}
	
	public static interface EventBusRetriever
	{
		public EventBus getEventBus();
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface EventMethod {
		
	}
}
