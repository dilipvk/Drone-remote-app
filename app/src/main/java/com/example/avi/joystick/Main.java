package com.example.avi.joystick;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main extends Activity {
	RelativeLayout layout_joystick,layout_joystick2;
	TextView textView1, textView2, textView3, textView4, textView5;
	Socket socket = null;
	ToggleButton start,capture;

	JoyStickClass js,js2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		MyClientTask myClientTask = new MyClientTask("192.168.137.120",9999,"mesage");
		myClientTask.execute();
		start = (ToggleButton) findViewById(R.id.Startbutton);
		capture = (ToggleButton) findViewById(R.id.Capturebutton);

		layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick1);
		layout_joystick2 = (RelativeLayout)findViewById(R.id.layout_joystick2);

        js = new JoyStickClass(getApplicationContext()
        		, layout_joystick, R.drawable.image_button);
	    js.setStickSize(100,100);
	    js.setLayoutSize(300, 300);
	    js.setLayoutAlpha(150);
	    js.setStickAlpha(100);
	    js.setOffset(50);
	    js.setMinimumDistance(50);


		js2 = new JoyStickClass(getApplicationContext(),layout_joystick2, R.drawable.image_button);
		js2.setStickSize(100,100);
		js2.setLayoutSize(300, 300);
		js2.setLayoutAlpha(150);
		js2.setStickAlpha(100);
		js2.setOffset(50);
		js2.setMinimumDistance(50);

		layout_joystick2.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				js2.drawStick(arg1);
				if (arg1.getAction() == MotionEvent.ACTION_DOWN
						|| arg1.getAction() == MotionEvent.ACTION_MOVE) {
					sendJsonObject();
				/*	textView1.setText("X : " + String.valueOf(js.getX()));
					textView2.setText("Y : " + String.valueOf(js.getY()));
					textView3.setText("Angle : " + String.valueOf(js.getAngle()));
					textView4.setText("Distance : " + String.valueOf(js.getDistance()));

					int direction = js.get8Direction();
					if(direction == JoyStickClass.STICK_UP) {
						textView5.setText("Direction : Up");
					} else if(direction == JoyStickClass.STICK_UPRIGHT) {
						textView5.setText("Direction : Up Right");
					} else if(direction == JoyStickClass.STICK_RIGHT) {
						textView5.setText("Direction : Right");
					} else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
						textView5.setText("Direction : Down Right");
					} else if(direction == JoyStickClass.STICK_DOWN) {
						textView5.setText("Direction : Down");
					} else if(direction == JoyStickClass.STICK_DOWNLEFT) {
						textView5.setText("Direction : Down Left");
					} else if(direction == JoyStickClass.STICK_LEFT) {
						textView5.setText("Direction : Left");
					} else if(direction == JoyStickClass.STICK_UPLEFT) {
						textView5.setText("Direction : Up Left");
					} else if(direction == JoyStickClass.STICK_NONE) {
						textView5.setText("Direction : Center");
					}
				*/
				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
/*					textView1.setText("X :");
					textView2.setText("Y :");
					textView3.setText("Angle :");
					textView4.setText("Distance :");
					textView5.setText("Direction :");
*/				}
				return true;
			}
		});
		layout_joystick.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				js.drawStick(arg1);
				if(arg1.getAction() == MotionEvent.ACTION_DOWN
						|| arg1.getAction() == MotionEvent.ACTION_MOVE) {
					sendJsonObject();
/*					textView1.setText("X : " + String.valueOf(js.getX()));
					textView2.setText("Y : " + String.valueOf(js.getY()));
					textView3.setText("Angle : " + String.valueOf(js.getAngle()));
					textView4.setText("Distance : " + String.valueOf(js.getDistance()));

					int direction = js.get8Direction();
					if(direction == JoyStickClass.STICK_UP) {
						textView5.setText("Direction : Up");
					} else if(direction == JoyStickClass.STICK_UPRIGHT) {
						textView5.setText("Direction : Up Right");
					} else if(direction == JoyStickClass.STICK_RIGHT) {
						textView5.setText("Direction : Right");
					} else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
						textView5.setText("Direction : Down Right");
					} else if(direction == JoyStickClass.STICK_DOWN) {
						textView5.setText("Direction : Down");
					} else if(direction == JoyStickClass.STICK_DOWNLEFT) {
						textView5.setText("Direction : Down Left");
					} else if(direction == JoyStickClass.STICK_LEFT) {
						textView5.setText("Direction : Left");
					} else if(direction == JoyStickClass.STICK_UPLEFT) {
						textView5.setText("Direction : Up Left");
					} else if(direction == JoyStickClass.STICK_NONE) {
						textView5.setText("Direction : Center");
					}
*/				} else if(arg1.getAction() == MotionEvent.ACTION_UP) {
				}
				return true;
			}
        });
    }

	private void sendJsonObject() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("X",js2.getX());
			jsonObject.put("Y",-js2.getY());
			jsonObject.put("Z",js.getX());
			jsonObject.put("height",-js.getY());
			jsonObject.put("running",start.isChecked());
			jsonObject.put("capture",capture.isChecked());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String message = jsonObject.toString();
		Log.e("sending message",message);
		MyClientTask myClientTask = new MyClientTask("192.168.137.120",9999,message);
		myClientTask.execute();
	}


	public class MyClientTask extends AsyncTask<Void, Void, Void> {

		String dstAddress;
		int dstPort;
		String response = "",message;

		MyClientTask(String addr, int port, String mesage){
			dstAddress = addr;
			dstPort = port;
			this.message = mesage;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				if(socket==null)
				{
					socket = new Socket(dstAddress, dstPort);
				}
				OutputStream os = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os);
				BufferedWriter bw = new BufferedWriter(osw);
				String sendMessage = message + "\n";
				bw.write(sendMessage);
				bw.flush();
				System.out.println("Message sent to the server : "+sendMessage);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.e("result",response);
			super.onPostExecute(result);
		}

	}
}
