package kr.co.gtson.example.customgalleryviewremoteimage.gallery;

import java.util.List;
import java.util.Map;

import kr.co.gtson.example.customgalleryviewremoteimage.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class CGVRIImageAdapter extends BaseAdapter {
	final Activity mContext;
	private CGVRIImageDownloader cgvriImageDownloader = null;
	private String pathUrl;
	private List<String> fileNameList;

	public CGVRIImageAdapter(Activity context, Map<String, Object> data) {
		this.mContext = context;
		this.fileNameList = (List<String>) data.get("fileNameList");
		this.pathUrl = (String) data.get("pathUrl");
	}

	public int getCount() {
		return (fileNameList == null) ? 0 : fileNameList.size();
	}

	public String getItem(int position) {
		return position + "";
	}

	public long getItemId(int position) {
		return 0L;
	}

	public View getView(final int position, final View view, final ViewGroup parent) {
		//Log.d("Debug", "position : " + position + ", view : " + view + ", parent : " + parent);
		View i = view;
		String fileName = fileNameList.get(position);

		if(i == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			i = layoutInflater.inflate(R.layout.image_view, parent, false);
		}

		if(fileName != null && !fileName.equals("")) {

			int imageViewId = R.id.imageView;
			int imageViewLinearLayoutId = R.id.imageViewLinearLayout;
			int imageViewRefreshButtonId = R.id.imageViewRefreshButton;
			int imageViewProgressBarId = R.id.imageViewProgressBar;

			ImageView imageView = (ImageView)i.findViewById(imageViewId);
			LinearLayout linearLayout = (LinearLayout)i.findViewById(imageViewLinearLayoutId);
			ImageButton imageButton = (ImageButton)i.findViewById(imageViewRefreshButtonId);
			ProgressBar progressBar = (ProgressBar)i.findViewById(imageViewProgressBarId);
			
			imageView.setVisibility(ImageView.INVISIBLE);
			linearLayout.setVisibility(LinearLayout.INVISIBLE);
			imageButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					getView(position, view, parent);
				}
			});
			progressBar.setVisibility(ProgressBar.VISIBLE);
			progressBar.setProgress(0);
			
			cgvriImageDownloader = new CGVRIImageDownloader(progressBar);

			cgvriImageDownloader.download(position, pathUrl + fileName, i, imageViewId, imageViewProgressBarId, imageViewLinearLayoutId, 0);
		}
		return i;
	}

	public CGVRIImageDownloader getCGVRIImageDownloader() {
		return cgvriImageDownloader;
	}
}
