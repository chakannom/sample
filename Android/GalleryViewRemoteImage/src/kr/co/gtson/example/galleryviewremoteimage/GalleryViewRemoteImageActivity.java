package kr.co.gtson.example.galleryviewremoteimage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryViewRemoteImageActivity extends Activity {
	private String baseUrl = "http://gts.gtson.co.kr/wp/wp-content/gallery/20110907-13/";
	private String[] resources = {"001.JPG","002.JPG","003.JPG","004.JPG","005.JPG","006.JPG","007.JPG","008.JPG","009.JPG","010.JPG"};
	
	private Gallery gallery;
	Matrix matrix = new Matrix(); 
	Matrix savedMatrix = new Matrix();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        gallery = (Gallery) findViewById(R.id.gallery1);

		gallery.setSpacing(10);

		gallery.setAdapter(new ImageAdapter(this));
		
		gallery.setHorizontalFadingEdgeEnabled(false);
    }
    
	public class ImageAdapter extends BaseAdapter {
		private Context context;

		public ImageAdapter(Context c) {
			context = c;
		}

		public int getCount() {
			return resources.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);

			URL aURL;
			try {
				Log.d("GTS", baseUrl + resources[position]);
				aURL = new URL(baseUrl + resources[position]);
				URLConnection conn = aURL.openConnection(); 
				conn.connect(); 
				InputStream is = conn.getInputStream(); 
				BufferedInputStream bis = new BufferedInputStream(is); 
				Bitmap bm = BitmapFactory.decodeStream(bis); 
				bis.close();
				is.close(); 

				imageView.setImageBitmap(bm);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.FILL_PARENT));
			imageView.setScaleType(ImageView.ScaleType.MATRIX);

			return imageView;
		}
	}
}