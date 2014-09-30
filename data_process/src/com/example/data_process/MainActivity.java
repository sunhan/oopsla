package com.example.data_process;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.example.header.CManagerDeviceReceiver;

public class MainActivity extends Activity{

	private static final String TAG = "MainActivity";
	private static final int VID = 0x10c4;
	private static final int PID = 0xea61;//I believe it is 0x0000 for the Arduino Megas
	private static CManagerDeviceReceiver mangerReceiver;
	
	public static TextView t1, t2, t3;	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		t1 = (TextView)findViewById(R.id.data_check1);
		t2 = (TextView)findViewById(R.id.data_check2);
		t3 = (TextView)findViewById(R.id.data_check3);
		
		Log.e(TAG,"mainactivity ¤µ¤²");
		mangerReceiver = new CManagerDeviceReceiver(this);
	/*	
		int num = 3;
		GraphView.GraphViewData[] data = new GraphView.GraphViewData[num];
		double v = 0;

		for (int i = 0; i < num; i++) {
			v += 0.2;
			data[i] = new GraphView.GraphViewData(i, Math.sin(v));
		}
		GraphView graphView = new LineGraphView(this, "GraphViewDemo");
		graphView.addSeries(new GraphViewSeries(data));
		graphView.setViewPort(2, 40);
		graphView.setScalable(true);
		graphView.setScrollable(true);

		LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
		layout.addView(graphView);*/

	}
}



