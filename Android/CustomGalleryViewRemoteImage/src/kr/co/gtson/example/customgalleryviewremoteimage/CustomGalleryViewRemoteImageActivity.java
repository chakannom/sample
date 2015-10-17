package kr.co.gtson.example.customgalleryviewremoteimage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Process;

public class CustomGalleryViewRemoteImageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pathUrl", "http://gts.gtson.co.kr/wp/wp-content/gallery/20110428/");
		List<String> fileNameList = new ArrayList<String>();
		fileNameList.add("img_7344.jpg");
		fileNameList.add("img_7351.jpg");
		fileNameList.add("img_7356.jpg");
		fileNameList.add("img_7357.jpg");
		fileNameList.add("img_7363.jpg");
		fileNameList.add("img_7364.jpg");
		fileNameList.add("img_7365.jpg");
		fileNameList.add("img_7392.jpg");
		fileNameList.add("img_7408.jpg");
		fileNameList.add("img_7417.jpg");
		fileNameList.add("img_7419.jpg");
		data.put("fileNameList", fileNameList);
		
		GalleryViewer galleryViewer = new GalleryViewer(this, data);
		galleryViewer.show();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Process.killProcess(Process.myPid());
	}

}