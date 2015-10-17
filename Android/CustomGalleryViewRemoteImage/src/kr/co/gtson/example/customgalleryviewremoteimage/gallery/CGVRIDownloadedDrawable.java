package kr.co.gtson.example.customgalleryviewremoteimage.gallery;

import java.lang.ref.WeakReference;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class CGVRIDownloadedDrawable extends ColorDrawable {
	private final WeakReference<CGVRIImageDownloader.BitmapDownloaderTask> bitmapDownloaderTaskReference;

	public CGVRIDownloadedDrawable(CGVRIImageDownloader.BitmapDownloaderTask bitmapDownloaderTask) {
		super(Color.BLACK);
		bitmapDownloaderTaskReference = new WeakReference<CGVRIImageDownloader.BitmapDownloaderTask>(bitmapDownloaderTask);
	}

	public CGVRIImageDownloader.BitmapDownloaderTask getBitmapDownloaderTask() {
		return bitmapDownloaderTaskReference.get();
	}

}
