package kr.co.gtson.example.customgalleryviewremoteimage;

import java.util.Map;

import kr.co.gtson.example.customgalleryviewremoteimage.gallery.CGVRIGallery;
import kr.co.gtson.example.customgalleryviewremoteimage.gallery.CGVRIImageAdapter;
import android.app.Activity;
import android.graphics.Matrix;
import android.view.View;
import android.widget.LinearLayout;

public class GalleryViewer {

	private CGVRIGallery mGallery;
	private LinearLayout galleryViewLinearLayout;

	public GalleryViewer(Activity context, Map<String, Object> data) {
		this.galleryViewLinearLayout = (LinearLayout) context.findViewById(R.id.galleryViewLinearLayout);
		this.mGallery = new CGVRIGallery(context);
		this.mGallery.setPaddingWidth(5);
		this.mGallery.setAdapter(new CGVRIImageAdapter(context, data), 0);
	}

	public View getView() {
		return galleryViewLinearLayout;
	}

	public void show() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		layoutParams.weight = 0f;

		galleryViewLinearLayout.addView(mGallery, layoutParams);
	}

	public boolean zoomIn() {
		Matrix matrix = mGallery.getOriginalMatrix();
		mGallery.getSavedMatrix().set(matrix);
		matrix.set(mGallery.getSavedMatrix());
		matrix.postScale((float) 1.5, (float) 1.5, mGallery.getMid().x, mGallery.getMid().y);
		mGallery.tuneMatrix(matrix);
		return true;
	}

	public boolean zoomOut() {
		Matrix matrix = mGallery.getOriginalMatrix();
		mGallery.getSavedMatrix().set(matrix);
		matrix.set(mGallery.getSavedMatrix());
		matrix.postScale((float) 0.5, (float) 0.5, mGallery.getMid().x, mGallery.getMid().y);
		mGallery.tuneMatrix(matrix);
		return true;
	}

	public boolean movePrev() {
		if (mGallery.getCurrentPosition() <= mGallery.getFirstPosition()) {
			return false;
		}
		mGallery.movePrevious();
		return true;
	}

	public boolean moveNext() {
		if (mGallery.getCurrentPosition() >= mGallery.getLastPosition()) {
			return false;
		}
		mGallery.moveNext();
		return true;
	}

	public boolean setPosition(int pageNo) {
		//setting default zoom
		Matrix matrix = mGallery.getOriginalMatrix();
		mGallery.getSavedMatrix().set(matrix);
		matrix.set(mGallery.getSavedMatrix());
		matrix.postScale(0, 0, mGallery.getMid().x, mGallery.getMid().y);
		mGallery.tuneMatrix(matrix);
		//change page
		if (pageNo == (getCurrentPage() - 1)) movePrev();
		else if (pageNo == (getCurrentPage() + 1)) moveNext();
		else mGallery.setPosition(pageNo-1);

		return true;
	}

	public int getCurrentPage() {
		return mGallery.getCurrentPosition() + 1;
	}

}