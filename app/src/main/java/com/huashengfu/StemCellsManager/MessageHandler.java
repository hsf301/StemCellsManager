package com.huashengfu.StemCellsManager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MessageHandler extends Handler {

	private Context context;
	
	public static final int SHOW_TOAST = 1;
	
	private Toast toast;
	
	public MessageHandler(Context context){
		super();
		this.context = context;
	}
	
	@Override
	public void handleMessage(final Message msg) {
		switch (msg.what) {
		case SHOW_TOAST:
			post(new Runnable() {
				
				@Override
				public void run() {
					String text = msg.getData().getString("text");
					int duration = msg.getData().getInt("duration");

					if(toast != null)
						toast.cancel();
					
					toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			});			
			break;

		default:
			break;
		}
	}

	public void showMessage(String text, int duration){
		Message msg = new Message();
		
		Bundle data = new Bundle();
		data.putString("text", text);
		data.putInt("duration", duration);
		
		msg.setData(data);
		msg.what = SHOW_TOAST;
		
		handleMessage(msg);
	}
	
	public void showMessage(String text){
		showMessage(text, Toast.LENGTH_SHORT);
	}
	
	public void showMessage(int resourceId){
		showMessage(context.getResources().getString(resourceId));
	}
	
	public void showMessage(Exception e){
		showMessage(e.getMessage(), Toast.LENGTH_SHORT);
	}
}
