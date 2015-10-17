package kr.co.gtson.example.progressdialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ProgressDialogActivity extends Activity {
	
	private static final int MAX_PROGRESS = 100;
	 
	private ProgressDialog mProgressDialog;
	private int mProgress;
    private Handler mProgressHandler;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mProgressDialog = new ProgressDialog(ProgressDialogActivity.this);
        mProgressDialog.setTitle("ProgressDialog");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(MAX_PROGRESS);
        
        Button progressButton = (Button) findViewById(R.id.progress_button);
        progressButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mProgressDialog.show();
				mProgress = 0;
			    mProgressDialog.setProgress(0);
			    mProgressHandler.sendEmptyMessage(0);
			}
        	
        });
       
        mProgressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mProgress >= 100) {
                    mProgressDialog.dismiss();
                } else {
                    mProgress++;
                    mProgressDialog.incrementProgressBy(1);
                    mProgressHandler.sendEmptyMessageDelayed(0, 100);
                }
            }
        };
    }
    @Override
    protected void onStart() {
    	super.onStart();
		mProgressDialog.show();
		mProgress = 0;
	    mProgressDialog.setProgress(0);
	    mProgressHandler.sendEmptyMessage(0);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    
}