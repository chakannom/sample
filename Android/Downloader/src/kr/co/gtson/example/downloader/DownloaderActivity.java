package kr.co.gtson.example.downloader;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DownloaderActivity extends Activity {
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private Button startBtn;
	private ProgressDialog progressDialog;
	private TextView textView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView)findViewById(R.id.textView1);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Downloading file..");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
        
		startDownload();

        startBtn = (Button)findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startDownload();
			}
		});

    }

	protected void startDownload() {
		String url = "http://gts.gtson.co.kr";
		new DownloadFileAsync().execute(url);
	}
	
	class DownloadFileAsync extends AsyncTask<String, String, String> {
		private StringBuilder sb;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setProgress(0);
			progressDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			int count;
			
			try {
				URL url = new URL(params[0]);
				URLConnection conn = url.openConnection();
				conn.connect();
				
				int lengthOfFile = conn.getContentLength();
				Log.d("ANDRO_ASYNC", "Length of file: " + lengthOfFile);
				
				InputStream input = new BufferedInputStream(conn.getInputStream());
				OutputStream output = new FileOutputStream("/sdcard/some_photo_from_gdansk_poland.jpg");
				
				byte[] data = new byte[1024];
				
				long total = 0;
				
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				
				while((count = input.read(data)) != -1) {
					total += count;
					publishProgress(""+(int)((total*100)/lengthOfFile));
					output.write(data, 0, count);
					baf.append(data, 0, count);
				}
				
				Log.d("baf", String.valueOf(baf.length()));
				
				sb = new StringBuilder(new String(baf.toByteArray()));
				
				
				output.flush();
				output.close();
				input.close();
				
			} catch (Exception e) {}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			Log.d("ANDRO_ASYNC", values[0]);
			progressDialog.setProgress(Integer.parseInt(values[0]));
		}
		
		@Override
		protected void onPostExecute(String result) {
			progressDialog.setProgress(100);
			textView.setText(sb.toString());
			progressDialog.dismiss();
		}
	}
}