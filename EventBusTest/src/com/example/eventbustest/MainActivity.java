package com.example.eventbustest;

import com.example.eventbustest.EventBus.EventBusRetriever;
import com.example.eventbustest.EventBus.EventMethod;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(savedInstanceState == null){
			getFragmentManager().beginTransaction()
				.add(R.id.container, ExampleFragment1.newInstance(Color.RED))
				.add(R.id.container, ExampleFragment2.newInstance(Color.BLUE))
				.commit();
		}
	}
	
	
	public static class ExampleFragment1 extends ExampleFragment
	{
		public static ExampleFragment1 newInstance(int color)
		{
			ExampleFragment1 frag = new ExampleFragment1();
			Bundle args = new Bundle();
			args.putInt(ARG_COLOR, color);
			frag.setArguments(args);
			return frag;
		}
		
		@Override
		public void onClick(View v)
		{
			if(v.getId() == R.id.go1){
				mEventBus.send(new Command(1, "PIMP ME"));
			}
			else if(v.getId() == R.id.go2){
				mEventBus.send(new Command(2, "MUMMAAAAA"));
			}
		}
		
		@EventMethod
		private void onLaunchCommand(MissileLaunch command)
		{
			if(command.code == 0x34FA56C9){
				Toast t = Toast.makeText(getActivity(), "LAUNCH ZEE MISSILES!", Toast.LENGTH_SHORT);
				t.getView().setBackgroundColor(getArguments().getInt(ARG_COLOR));
				t.show();
			}
		}
	}
	
	public static class ExampleFragment2 extends ExampleFragment
	{
		public static ExampleFragment2 newInstance(int color)
		{
			ExampleFragment2 frag = new ExampleFragment2();
			Bundle args = new Bundle();
			args.putInt(ARG_COLOR, color);
			frag.setArguments(args);
			return frag;
		}
		
		@Override
		public void onClick(View v)
		{
			if(v.getId() == R.id.go2){
				mEventBus.send(new MissileLaunch(0x34FA56C9));
			}
		}
		
		@EventMethod
		private void onUITextUpdatedWhenGoButtonPressed(MessageText message)
		{
			mInputText.setText(message.text);
		}
		
		@EventMethod
		private void recieveCommand(Command command)
		{
			if(command.command == 1){
				Toast.makeText(getActivity(), "The Number: " + command.message, Toast.LENGTH_SHORT).show();
			}
			else if(command.command == 2){
				Toast.makeText(getActivity(), "Mamma's Number: " + command.message, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	abstract static class ExampleFragment extends Fragment
		implements View.OnClickListener
	{
		protected static final String ARG_COLOR = "argColor";
		
		protected EventBus mEventBus;
		protected EditText mInputText;
		
		
		public ExampleFragment()
		{
			// Blank constructor required by platform
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			mEventBus = BusApp.get().getEventBus();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			rootView.setBackgroundColor(getArguments().getInt(ARG_COLOR));
			return rootView;
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState)
		{
			super.onViewCreated(view, savedInstanceState);
			mInputText = (EditText)view.findViewById(R.id.input);
			view.findViewById(R.id.go1).setOnClickListener(this);
			view.findViewById(R.id.go2).setOnClickListener(this);
		}
		
		@Override
		public void onStart()
		{
			super.onStart();
			mEventBus.subscribe(this);
		}
		
		@Override
		public void onClick(View v)
		{
			throw new IllegalStateException("Must be overridden");
		}
		
		@Override
		public void onStop()
		{
			super.onStop();
			mEventBus.unsubscribe(this);
		}
		
		public static class MessageText
		{
			public final String text;
			
			public MessageText(String text)
			{
				this.text = text;
			}
		}
		
		public static class MissileLaunch
		{
			public final long code;
			
			public MissileLaunch(long code)
			{
				this.code = code;
			}
		}
	}
	
	public static class Command
	{
		public final int    command;
		public final String message;
		
		
		public Command(int command, String message)
		{
			this.command = command;
			this.message = message;
		}
	}
}
