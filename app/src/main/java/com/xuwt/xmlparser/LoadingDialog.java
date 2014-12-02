package com.xuwt.xmlparser;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * @author xiechunlei
 */
public class LoadingDialog extends Dialog {

	private String msg;
	
	private ProgressBar mProgressBar;
	
	private TextView tvMsg;
	
	/**
	 * @param context
	 * @param loadTip
	 */
	public LoadingDialog(Context context, String loadTip) {
		super(context);
		msg = loadTip;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().getDecorView()
				.setBackgroundColor(Color.TRANSPARENT);
		setContentView(R.layout.load_data);
		initView();
	}
	
	public void initView() {
		tvMsg = (TextView) findViewById(R.id.msg);
		tvMsg.setText(msg);
		mProgressBar = (ProgressBar)findViewById(R.id.circleProgressBar);
	}
	
	public ProgressBar getLoadingView() {
		return mProgressBar;
	}
	
	public void setMessage(String msg) {
		tvMsg.setText(msg);
	}
}
