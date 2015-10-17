/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.co.gtson.example.customgalleryviewremoteimage.gallery;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import kr.co.gtson.example.customgalleryviewremoteimage.common.CommonConstants;
import kr.co.gtson.example.customgalleryviewremoteimage.common.Utils;

import org.apache.http.util.ByteArrayBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


/**
 * This helper class download images from the Internet and binds those with the
 * provided ImageView.
 * 
 * <p>
 * It requires the INTERNET permission, which should be added to your
 * application's manifest file.
 * </p>
 * 
 * A local cache of downloaded images is maintained internally to improve
 * performance.
 */

public class CGVRIImageDownloader {
	final int BUFFER_LENGTH = 1024 * 8;

	public static int relativePosition = -1;
	private boolean isShowNumber = false;
	final ProgressBar detailProgress;
	boolean isValid = true;

	public CGVRIImageDownloader(ProgressBar detailProgress) {
		this.detailProgress = detailProgress;
	}

	/**
	 * Download the specified image from the Internet and binds it to the
	 * provided ImageView. The binding is immediate if the image is found in the
	 * cache and will be done asynchronously otherwise. A null bitmap will be
	 * associated to the ImageView if an error occurs.
	 * 
	 * @param url
	 *            The URL of the image to download.
	 * @param imagePage 
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void download(int position, String url, View view, int viewId, int progressBarId, int linearLayoutId, int sampleOption) {
		if(view != null) {
			resetPurgeTimer();
			Bitmap bitmap = getBitmapFromCache(url);

			if(bitmap == null) {
				forceDownload(position, url, view, viewId, progressBarId, linearLayoutId, sampleOption);
			} else {
				// No need progress.
				ImageView iv = (ImageView)view.findViewById(viewId);
				LinearLayout ll = (LinearLayout)view.findViewById(linearLayoutId);
				ProgressBar ivProgress = (ProgressBar)view.findViewById(progressBarId);

				cancelPotentialDownload(url, iv);

				iv.setVisibility(ImageView.VISIBLE);
				ll.setVisibility(ImageButton.INVISIBLE);
				ivProgress.setVisibility(ProgressBar.INVISIBLE);

				iv.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
				iv.setImageBitmap(bitmap);
			}
		} else {
			Log.d("CGVRIImageDownloader-download", "ImageDownloader.download > imageView is NULL");
		}
	}

	/*
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear. private
	 * void forceDownload(String url, ImageView view) { forceDownload(url, view,
	 * null); }
	 */

	/**
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear.
	 */
	private void forceDownload(int position, String url, View view, int viewId, int progressBarId, int linearLayoutId, int sampleOption) {
		// State sanity: url is guaranteed to never be null in
		// DownloadedDrawable and cache keys.
		ImageView iv = (ImageView)view.findViewById(viewId);

		if(url == null) {
			iv.setImageDrawable(null);
			return;
		}

		if(cancelPotentialDownload(url, iv)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(position, view, viewId, progressBarId, linearLayoutId, sampleOption);
			CGVRIDownloadedDrawable downloadedDrawable = new CGVRIDownloadedDrawable(task);
			iv.setImageDrawable(downloadedDrawable);

			task.execute(url);
		}
	}

	/**
	 * Returns true if the current download has been canceled or if there was no
	 * download in progress on this image view. Returns false if the download in
	 * progress deals with the same url. The download is not stopped in that
	 * case.
	 */
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if(bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active download task (if any) associated
	 *         with this imageView. null if there is no such task.
	 */
	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if(imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if(drawable instanceof CGVRIDownloadedDrawable) {
				CGVRIDownloadedDrawable downloadedDrawable = (CGVRIDownloadedDrawable)drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	synchronized Bitmap downloadBitmap(int position, String url, int sampleOption) {
//		Log.d("GTS:downloadBitmap", "position:"+position);
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		ByteArrayBuffer baf = new ByteArrayBuffer(CommonConstants.MAX_BYTE_LENGTH);
		try {
			urlConn = Utils.getURLConnection(url);
			
			if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
			inputStream = new BufferedInputStream(urlConn.getInputStream(), BUFFER_LENGTH);

			long contentLength = urlConn.getContentLength();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = sampleOption;

			byte[] data = new byte[BUFFER_LENGTH];
			int count = -1;
			long total = 0;
			while (((count = inputStream.read(data)) != -1) && isValid) {
				total += count;
				detailProgress.setProgress((int)((total*100)/contentLength));
				baf.append(data, 0, count);
			}
			
			Bitmap bitmap = null;
			if(isValid) {
				bitmap = decodeResizeStream(baf.toByteArray(), 1);
			} else {
				detailProgress.setProgress(0);
			}
			inputStream.close();

			return bitmap;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch(IOException iex) {}
			}
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}finally {}
		return null;
	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while(totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if(bytesSkipped == 0L) {
					int b = read();
					if(b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */
	public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		public final WeakReference<View> imageViewReference;

		private int position = -1;
		private int viewId = 0;
		private int progressBarId = 0;
		private int linearLayoutId = 0;
		private int sampleOption = 0;

		public BitmapDownloaderTask(int position, View view, int viewId, int progressBarId, int linearLayoutId, int sampleOption) {
			imageViewReference = new WeakReference<View>(view);
			this.position = position;
			this.viewId = viewId;
			this.progressBarId = progressBarId;
			this.linearLayoutId = linearLayoutId;
			this.sampleOption = sampleOption;
		}

		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			Bitmap bitmap = null;

			try {
				if(imageViewReference != null) {
					bitmap = downloadBitmap(position, url, sampleOption);
				}
			} catch(Exception ex) {
				Log.e("CGVRIImageDownloader-BitmapDownloaderTask", "EX in doInBackground : " + ex);
			}

			return bitmap;
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if(isCancelled()) {
				bitmap = null;
			}

			addBitmapToCache(url, bitmap);
			if(imageViewReference != null) {
				View view = imageViewReference.get();
				if(view != null) {
					ImageView iv = (ImageView)view.findViewById(viewId);
					LinearLayout ivll = (LinearLayout)view.findViewById(linearLayoutId);
					ProgressBar ivProgress = (ProgressBar)view.findViewById(progressBarId);

					BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(iv);
					// Change bitmap only if this process is still associated
					// with
					// it
					// Or if we don't use any bitmap to task association
					// (NO_DOWNLOADED_DRAWABLE mode)
					if((this == bitmapDownloaderTask)) {
						if (bitmap != null) {
							iv.setVisibility(ImageView.VISIBLE);
							ivll.setVisibility(LinearLayout.INVISIBLE);
						} else {
							iv.setVisibility(ImageView.INVISIBLE);
							ivll.setVisibility(LinearLayout.VISIBLE);
						}
						ivProgress.setVisibility(ProgressBar.INVISIBLE);

						iv.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
						iv.setImageBitmap(bitmap);
					}
				}
			}
		}

		@Override
		protected void onCancelled() {
			imageViewReference.clear();
			isValid = false;
		}
	}

	/*
	 * Cache-related fields and methods.
	 * 
	 * We use a hard and a soft cache. A soft reference cache is too
	 * aggressively cleared by the Garbage Collector.
	 */

	private static final int HARD_CACHE_CAPACITY = 2;
	private static final int DELAY_BEFORE_PURGE = 1 * 1000; // in milliseconds

	// Hard cache, with a fixed maximum capacity and a life duration
	private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		/**
		 * UID
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
			if(size() > HARD_CACHE_CAPACITY) {
				// Entries push-out of hard reference cache are transferred to
				// soft reference cache
				sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			}
			else
				return false;
		}
	};

	// Soft cache for bitmaps kicked out of hard cache
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

	private final Handler purgeHandler = new Handler();

	private final Runnable purger = new Runnable() {
		public void run() {
			clearCache();
		}
	};

	/**
	 * Adds this bitmap to the cache.
	 * 
	 * @param bitmap
	 *            The newly downloaded bitmap.
	 */
	private void addBitmapToCache(String url, Bitmap bitmap) {
		if(bitmap != null) {
			synchronized(sHardBitmapCache) {
				sHardBitmapCache.put(url, bitmap);
			}
		}
	}

	/**
	 * @param url
	 *            The URL of the image that will be retrieved from the cache.
	 * @return The cached bitmap or null if it was not found.
	 */
	private Bitmap getBitmapFromCache(String url) {
		// First try the hard reference cache
		synchronized(sHardBitmapCache) {
			final Bitmap bitmap = sHardBitmapCache.get(url);
			if(bitmap != null) {
				// Bitmap found in hard cache
				// Move element to first position, so that it is removed last
				sHardBitmapCache.remove(url);
				sHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}

		// Then try the soft reference cache
		SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
		if(bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if(bitmap != null) {
				// Bitmap found in soft cache
				return bitmap;
			} else {
				// Soft reference has been Garbage Collected
				sSoftBitmapCache.remove(url);
			}
		}

		return null;
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that
	 * for memory efficiency reasons, the cache will automatically be cleared
	 * after a certain inactivity delay.
	 */
	public void clearCache() {
		sHardBitmapCache.clear();
		sSoftBitmapCache.clear();
	}

	/**
	 * Allow a new delay before the automatic cache clear is done.
	 */
	private void resetPurgeTimer() {
		purgeHandler.removeCallbacks(purger);
		purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
	}


	public boolean isShowNumber() {
		return isShowNumber;
	}

	public void setShowNumber(boolean isShowNumber) {
		this.isShowNumber = isShowNumber;
	}

	/**
	 * Bitmap resize
	 */
	private Bitmap decodeResizeStream(byte[] buffer, int scale) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			if (scale > 2) {
				o.inDither = true;
				o.inPurgeable = true;
			}
			o.inSampleSize = scale;
			bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, o);
		} catch (OutOfMemoryError e) {
			bitmap = decodeResizeStream(buffer, scale*2);
		}
		return bitmap;
	}
}
