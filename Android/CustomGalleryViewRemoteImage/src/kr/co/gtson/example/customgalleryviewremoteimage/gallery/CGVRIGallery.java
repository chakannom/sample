package kr.co.gtson.example.customgalleryviewremoteimage.gallery;

import kr.co.gtson.example.customgalleryviewremoteimage.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.FloatMath;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


// TODO:

// 1. In order to improve performance Cache screen bitmap and use for animation
// 2. Establish superfluous memory allocations and delay or replace with reused objects
//	  Probably need to make sure we are not allocating objects (strings, etc.) in loops

public class CGVRIGallery extends FrameLayout {
	// These matrices will be used to move and zoom image
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Matrix tuningMatrix = new Matrix();

	// We can be in one of these 3 states
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private static final int MAX_SCALE = 10; // 10배 이상 확대 하지 않도록 최대 배율 설정

	int mode = NONE;

	private PointF start = new PointF();
	private PointF mid = new PointF();

	private float oldDist = 1f;

	private float orgScaleX = 0f;
	private float orgScaleY = 0f;

	private float transX = 0f;

	private int scaleWidth = -1;
	private int scaleHeight = -1;

	// Constants
	private final int swipe_min_distance = 120;
	private final int swipe_max_off_path = 250;
	private final int swipe_threshold_veloicty = 400;

	// Properties
	private int mViewPaddingWidth = 0;
	private int mAnimationDuration = 250;
	private float mSnapBorderRatio = 0.5f;
	private boolean mIsGalleryCircular = false;

	// Members
	private int mGalleryWidth = 0;
	private boolean mIsTouched = false;
	private boolean mIsDragging = false;
	private float mCurrentOffset = 0.0f;
	private long mScrollTimestamp = 0;
	private int mFlingDirection = 0;
	private int mCurrentPosition = 0;
	private int mCurrentViewNumber = 0;

	private final Activity mContext;
	private CGVRIImageAdapter mAdapter;
	private FlingGalleryView[] mViews;
	private FlingGalleryAnimation mAnimation;
	private GestureDetector mGestureDetector;
	private Interpolator mDecelerateInterpolater;

	private int imageViewId;
	private int imageViewLinearLayoutId;
	int displayWidth = 0;

	public CGVRIGallery(Activity context) {
		super(context);

		this.imageViewId = R.id.imageView;
		this.imageViewLinearLayoutId = R.id.imageViewLinearLayout;

		mContext = context;
		mAdapter = null;

		mViews = new FlingGalleryView[3];
		mViews[0] = new FlingGalleryView(0, this);
		mViews[1] = new FlingGalleryView(1, this);
		mViews[2] = new FlingGalleryView(2, this);

		mAnimation = new FlingGalleryAnimation();
		mGestureDetector = new GestureDetector(new FlingGestureDetector());
		mDecelerateInterpolater = AnimationUtils.loadInterpolator(mContext, android.R.anim.decelerate_interpolator);

		setTitle(mCurrentPosition);
	}

	public void setTitle(int no) {
		mContext.setTitle("FileName");
	}

	public void setPaddingWidth(int viewPaddingWidth) {
		mViewPaddingWidth = viewPaddingWidth;
	}

	public void setAnimationDuration(int animationDuration) {
		mAnimationDuration = animationDuration;
	}

	public void setSnapBorderRatio(float snapBorderRatio) {
		mSnapBorderRatio = snapBorderRatio;
	}

	public void setIsGalleryCircular(boolean isGalleryCircular) {
		if (mIsGalleryCircular != isGalleryCircular) {
			mIsGalleryCircular = isGalleryCircular;

			if (mCurrentPosition == getFirstPosition()) {
				// We need to reload the view immediately to the left to change
				// it to circular view or blank
				mViews[getPrevViewNumber(mCurrentViewNumber)].recycleView(getPrevPosition(mCurrentPosition));
			}

			if (mCurrentPosition == getLastPosition()) {
				// We need to reload the view immediately to the right to change
				// it to circular view or blank
				mViews[getNextViewNumber(mCurrentViewNumber)].recycleView(getNextPosition(mCurrentPosition));
			}
		}
	}

	public int getGalleryCount() {
		return (mAdapter == null) ? 0 : mAdapter.getCount();
	}

	public int getFirstPosition() {
		return 0;
	}

	public int getLastPosition() {
		return (getGalleryCount() == 0) ? 0 : getGalleryCount() - 1;
	}

	private int getPrevPosition(int relativePosition) {
		int prevPosition = relativePosition - 1;

		if (prevPosition < getFirstPosition()) {
			prevPosition = getFirstPosition() - 1;

			if (mIsGalleryCircular == true) {
				prevPosition = getLastPosition();
			}
		}

		return prevPosition;
	}

	private int getNextPosition(int relativePosition) {
		int nextPosition = relativePosition + 1;

		if (nextPosition > getLastPosition()) {
			nextPosition = getLastPosition() + 1;

			if (mIsGalleryCircular == true) {
				nextPosition = getFirstPosition();
			}
		}

		return nextPosition;
	}

	private int getPrevViewNumber(int relativeViewNumber) {
		return (relativeViewNumber == 0) ? 2 : relativeViewNumber - 1;
	}

	private int getNextViewNumber(int relativeViewNumber) {
		return (relativeViewNumber == 2) ? 0 : relativeViewNumber + 1;
	}

	private int getPrevViewNumberToCurrentPosition(int relativePosition) {
		int prevPosition = getPrevPosition(relativePosition);
		if (prevPosition < 0) return 2;
		return prevPosition%3;
	}

	private int getNextViewNumberToCurrentPosition(int relativePosition) {
		int nextPosition = getNextPosition(relativePosition);
		return nextPosition%3;
	}
	
	private int getCurrentViewNumberToCurrentPosition(int relativePosition) {
		return relativePosition%3;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// Calculate our view width
		mGalleryWidth = right - left;

		if (changed == true) {
			// Position views at correct starting offsets
			mViews[0].setOffset(0, 0, mCurrentViewNumber);
			mViews[1].setOffset(0, 0, mCurrentViewNumber);
			mViews[2].setOffset(0, 0, mCurrentViewNumber);
		}

		initImageView(); // 이미지 뷰 설정


		Display display = ((WindowManager)mContext.getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();
		displayWidth = display.getWidth();

		mid.x = display.getWidth()/2;
		mid.y = display.getHeight()/2;
	}

	/**
	 * 현재 화면에 보여지는 view 인 경우에는 scaleType 을 fitCenter -> matrix 로 변경하고<br/>
	 * fitCenter 일 때의 이미지 사이즈와 위치를 그대로 설정함<br/>
	 * 화면에 보여지는 view 가 아닌 경우에는  fixCenter 로 다시 바꿔 원래 이미지로 돌린다.
	 * 
	 * @param viewNumber view
	 */
	public void initImageView() {

		for (int i = 0; i < mViews.length; i++) {

			ImageView imageView = (ImageView)mViews[i].mExternalView.findViewById(imageViewId);

			if (imageView != null && imageView.getDrawable() != null) {

				Drawable d = imageView.getDrawable();

				int instrinsicWidth = d.getIntrinsicWidth();
				int instrinsicHeight = d.getIntrinsicHeight();

				// 이미지 로딩이 완료된 이미지에 대해서만 처리
				if (instrinsicWidth != -1 && instrinsicHeight != -1) {

					// 현재 보고 있는 뷰에 대해서만 처리
					if (i == mCurrentViewNumber) {

						Matrix matrix = imageView.getImageMatrix();

						float[] values = new float[9];

						matrix.getValues(values);

						if (imageView.getScaleType() != ScaleType.MATRIX) {
							// 이미지 최소 확대를 위한 원래 이미지 scale 저장
							orgScaleX = values[Matrix.MSCALE_X];
							orgScaleY = values[Matrix.MSCALE_Y];
						}

						this.matrix.set(matrix);

						imageView.setScaleType(ScaleType.MATRIX);

						tuneMatrix(matrix);

					} else {
						// 그 외 이미지는 fitCenter 처리
						imageView.setScaleType(ScaleType.FIT_CENTER);
					}

				}
			}
		}
	}

	public void setAdapter(CGVRIImageAdapter adapter) {
		setAdapter(adapter, 0);
	}	

	public void setAdapter(CGVRIImageAdapter adapter, int position) {
		mAdapter = adapter;
		mCurrentPosition = position;
		mCurrentViewNumber = getCurrentViewNumberToCurrentPosition(mCurrentPosition);

		// Load the initial views from adapter
		mViews[getCurrentViewNumberToCurrentPosition(mCurrentPosition)].recycleView(mCurrentPosition);
		mViews[getNextViewNumberToCurrentPosition(mCurrentPosition)].recycleView(getNextPosition(mCurrentPosition));
		mViews[getPrevViewNumberToCurrentPosition(mCurrentPosition)].recycleView(getPrevPosition(mCurrentPosition));

		// Position views at correct starting offsets
		mViews[0].setOffset(0, 0, mCurrentViewNumber);
		mViews[1].setOffset(0, 0, mCurrentViewNumber);
		mViews[2].setOffset(0, 0, mCurrentViewNumber);
		
		CGVRIImageDownloader.relativePosition = mCurrentPosition;
	}

	public void setPosition(int position) {
		mCurrentPosition = position;
		mCurrentViewNumber = getCurrentViewNumberToCurrentPosition(mCurrentPosition);

		setTitle(mCurrentPosition);

		// Load the initial views from adapter
		mViews[getCurrentViewNumberToCurrentPosition(mCurrentPosition)].recycleView(mCurrentPosition);
		mViews[getNextViewNumberToCurrentPosition(mCurrentPosition)].recycleView(getNextPosition(mCurrentPosition));
		mViews[getPrevViewNumberToCurrentPosition(mCurrentPosition)].recycleView(getPrevPosition(mCurrentPosition));

		// Position views at correct starting offsets
		mViews[0].setOffset(0, 0, mCurrentViewNumber);
		mViews[1].setOffset(0, 0, mCurrentViewNumber);
		mViews[2].setOffset(0, 0, mCurrentViewNumber);

		CGVRIImageDownloader.relativePosition = mCurrentPosition;
	}

	private int getViewOffset(int viewNumber, int relativeViewNumber) {
		// Determine width including configured padding width
		int offsetWidth = mGalleryWidth + mViewPaddingWidth;

		// Position the previous view one measured width to left
		if (viewNumber == getPrevViewNumber(relativeViewNumber)) {
			return offsetWidth;
		}

		// Position the next view one measured width to the right
		if (viewNumber == getNextViewNumber(relativeViewNumber)) {
			return offsetWidth * -1;
		}

		return 0;
	}

	public void movePrevious() {
		// Slide to previous view
		mFlingDirection = 1;
		processGesture();
	}

	public void moveNext() {
		// Slide to next view
		mFlingDirection = -1;
		processGesture();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			movePrevious();
			return true;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			moveNext();
			return true;

		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.onGalleryTouchEvent(event);
	}

	public boolean onGalleryTouchEvent(MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			if (mIsTouched || mIsDragging) {
				processScrollSnap();
				processGesture();
			}
			break;
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);

			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:

			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}

			}

			break;
		}

		// 드래그 하거나 zoom 인 경우에만 Tune matrix
		if (mode == DRAG || mode == ZOOM) {
			tuneMatrix(matrix);
		}

		return mGestureDetector.onTouchEvent(event);
	}

	public void tuneMatrix(Matrix matrix) {

		ImageView imageView = (ImageView)mViews[mCurrentViewNumber].mExternalView.findViewById(imageViewId);

		if (imageView != null) {

			float[] value = new float[9];
			matrix.getValues(value);
			float[] savedValue = new float[9];
			tuningMatrix.getValues(savedValue);

			int width = imageView.getWidth();
			int height = imageView.getHeight();

			Drawable d = imageView.getDrawable();

			if (d == null)
				return;

			int imageWidth = d.getIntrinsicWidth();
			int imageHeight = d.getIntrinsicHeight();

			scaleWidth = (int)(imageWidth * value[Matrix.MSCALE_X]);
			scaleHeight = (int)(imageHeight * value[Matrix.MSCALE_Y]);

			// 이미지가 바깥으로 나가지 않도록.
			if (value[Matrix.MTRANS_X] < width - scaleWidth)
				value[Matrix.MTRANS_X] = width - scaleWidth;
			if (value[Matrix.MTRANS_Y] < height - scaleHeight)
				value[Matrix.MTRANS_Y] = height - scaleHeight;
			if (value[Matrix.MTRANS_X] > 0)
				value[Matrix.MTRANS_X] = 0;
			if (value[Matrix.MTRANS_Y] > 0)
				value[Matrix.MTRANS_Y] = 0;

			// 10배 이상 확대 하지 않도록
			if (value[Matrix.MSCALE_X] > MAX_SCALE || value[Matrix.MSCALE_Y] > MAX_SCALE) {
				value[Matrix.MSCALE_X] = savedValue[Matrix.MSCALE_X];
				value[Matrix.MSCALE_Y] = savedValue[Matrix.MSCALE_Y];
				value[Matrix.MTRANS_X] = savedValue[Matrix.MTRANS_X];
				value[Matrix.MTRANS_Y] = savedValue[Matrix.MTRANS_Y];
			}

			// 최소 사이즈 조정
			if (value[Matrix.MSCALE_X] < orgScaleX)
				value[Matrix.MSCALE_X] = orgScaleX;
			if (value[Matrix.MSCALE_Y] < orgScaleY)
				value[Matrix.MSCALE_Y] = orgScaleY;

			scaleWidth = (int)(imageWidth * value[Matrix.MSCALE_X]);
			scaleHeight = (int)(imageHeight * value[Matrix.MSCALE_Y]);

			if (scaleWidth < width) {
				value[Matrix.MTRANS_X] = (float)width / 2 - (float)scaleWidth / 2;
			}
			if (scaleHeight < height) {
				value[Matrix.MTRANS_Y] = (float)height / 2 - (float)scaleHeight / 2;
			}

			transX = value[Matrix.MTRANS_X];

			matrix.setValues(value);
			tuningMatrix.set(matrix);

			imageView.setImageMatrix(matrix);
		}
	}

	/**
	 * 두포인트 사이의 간격 계산
	 * 
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 두포인트의 가운데 포인트 계산
	 * 
	 * @param point
	 * @param event
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	void processGesture() {
		int newViewNumber = mCurrentViewNumber;
		int reloadViewNumber = 0;
		int reloadPosition = 0;

		mIsTouched = false;
		mIsDragging = false;

		if (mFlingDirection > 0) {
			if (mCurrentPosition > getFirstPosition() || mIsGalleryCircular == true) {
				// Determine previous view and outgoing view to recycle
				newViewNumber = getPrevViewNumber(mCurrentViewNumber);
				mCurrentPosition = getPrevPosition(mCurrentPosition);
				reloadViewNumber = getNextViewNumber(mCurrentViewNumber);
				reloadPosition = getPrevPosition(mCurrentPosition);
			}
		}

		if (mFlingDirection < 0) {
			if (mCurrentPosition < getLastPosition() || mIsGalleryCircular == true) {
				// Determine the next view and outgoing view to recycle
				newViewNumber = getNextViewNumber(mCurrentViewNumber);
				mCurrentPosition = getNextPosition(mCurrentPosition);
				reloadViewNumber = getPrevViewNumber(mCurrentViewNumber);
				reloadPosition = getNextPosition(mCurrentPosition);
			}
		}

		//		Log.d("Debug", getFirstPosition() + ", [CurrentViewNumber] : " + mCurrentViewNumber + 
		//				",  [newViewNumber] : " + newViewNumber + 
		//				",  [mCurrentPosition] : " + mCurrentPosition + 
		//				",  [reloadViewNumber] : " + reloadViewNumber + 
		//				",  [reloadPosition] : " + reloadPosition);

		if (newViewNumber != mCurrentViewNumber) {
			mCurrentViewNumber = newViewNumber;

			// If ImageButton is visible, Reload current view from adapter in current position
			LinearLayout linearLayout = (LinearLayout)mViews[mCurrentViewNumber].mExternalView.findViewById(imageViewLinearLayoutId);
			if (linearLayout.getVisibility() == LinearLayout.VISIBLE) {
				mViews[mCurrentViewNumber].recycleView(mCurrentPosition);
			}
			// Reload outgoing view from adapter in new position
			mViews[reloadViewNumber].recycleView(reloadPosition);
		}

		// Ensure input focus on the current view
		mViews[mCurrentViewNumber].requestFocus();

		// Run the slide animations for view transitions
		mAnimation.prepareAnimation(mCurrentViewNumber);
		this.startAnimation(mAnimation);

		mFlingDirection = 0; // Reset fling state
		setTitle(mCurrentPosition);
		CGVRIImageDownloader.relativePosition = mCurrentPosition;
	}

	void processScrollSnap() {
		// Snap to next view if scrolled passed snap position
		float rollEdgeWidth = mGalleryWidth * mSnapBorderRatio;
		int rollOffset = mGalleryWidth - (int)rollEdgeWidth;
		int currentOffset = mViews[mCurrentViewNumber].getCurrentOffset();

		if (currentOffset <= rollOffset * -1) {
			// Snap to previous view
			mFlingDirection = 1;
		}

		if (currentOffset >= rollOffset) {
			// Snap to next view
			mFlingDirection = -1;
		}
	}

	private class FlingGalleryView {
		private int mViewNumber;
		private FrameLayout mParentLayout;

		private FrameLayout mInvalidLayout = null;
		private LinearLayout mInternalLayout = null;
		private View mExternalView = null;

		public FlingGalleryView(int viewNumber, FrameLayout parentLayout) {
			mViewNumber = viewNumber;
			mParentLayout = parentLayout;

			// Invalid layout is used when outside gallery
			mInvalidLayout = new FrameLayout(mContext);
			mInvalidLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

			// Internal layout is permanent for duration
			mInternalLayout = new LinearLayout(mContext);
			mInternalLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

			mParentLayout.addView(mInternalLayout);
		}

		public void recycleView(int newPosition) {
			if (mExternalView != null) {
				mInternalLayout.removeView(mExternalView);
			}

			if (mAdapter != null) {
				if (newPosition >= getFirstPosition() && newPosition <= getLastPosition()) {
					if (mExternalView != null) {
						ImageView iv = (ImageView)mExternalView.findViewById(imageViewId);
						if(iv != null) {
							Drawable drawable = iv.getDrawable();
							if(drawable instanceof CGVRIDownloadedDrawable) {
								CGVRIDownloadedDrawable downloadedDrawable = (CGVRIDownloadedDrawable)drawable;
								downloadedDrawable.getBitmapDownloaderTask().cancel(true);
							}
						}
					}
					mExternalView = mAdapter.getView(newPosition, mExternalView, mInternalLayout);
				} else {
					LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					mExternalView = layoutInflater.inflate(R.layout.image_view, null, false);
					ProgressBar progressBar = (ProgressBar)mExternalView.findViewById(R.id.imageViewProgressBar);
					progressBar.setVisibility(ProgressBar.GONE);

					//mExternalView = mInvalidLayout;
				}
			}

			if (mExternalView != null) {
				mInternalLayout.addView(mExternalView, new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			}
		}

		public void setOffset(int xOffset, int yOffset, int relativeViewNumber) {
			// Scroll the target view relative to its own position relative to
			// currently displayed view
			mInternalLayout.scrollTo(getViewOffset(mViewNumber, relativeViewNumber) + xOffset, yOffset);
		}

		public int getCurrentOffset() {
			// Return the current scroll position
			return mInternalLayout.getScrollX();
		}

		public void requestFocus() {
			mInternalLayout.requestFocus();
		}
	}

	private class FlingGalleryAnimation extends Animation {
		private boolean mIsAnimationInProgres;
		private int mRelativeViewNumber;
		private int mInitialOffset;
		private int mTargetOffset;
		private int mTargetDistance;

		public FlingGalleryAnimation() {
			mIsAnimationInProgres = false;
			mRelativeViewNumber = 0;
			mInitialOffset = 0;
			mTargetOffset = 0;
			mTargetDistance = 0;
		}

		public void prepareAnimation(int relativeViewNumber) {
			// If we are animating relative to a new view
			if (mRelativeViewNumber != relativeViewNumber) {
				if (mIsAnimationInProgres == true) {
					// We only have three views so if requested again to animate
					// in same direction we must snap
					int newDirection = (relativeViewNumber == getPrevViewNumber(mRelativeViewNumber)) ? 1 : -1;
					int animDirection = (mTargetDistance < 0) ? 1 : -1;

					// If animation in same direction
					if (animDirection == newDirection) {
						// Ran out of time to animate so snap to the target
						// offset
						mViews[0].setOffset(mTargetOffset, 0, mRelativeViewNumber);
						mViews[1].setOffset(mTargetOffset, 0, mRelativeViewNumber);
						mViews[2].setOffset(mTargetOffset, 0, mRelativeViewNumber);
					}
				}

				// Set relative view number for animation
				mRelativeViewNumber = relativeViewNumber;
			}

			// Note: In this implementation the targetOffset will always be zero
			// as we are centering the view; but we include the calculations of
			// targetOffset and targetDistance for use in future implementations

			mInitialOffset = mViews[mRelativeViewNumber].getCurrentOffset();
			mTargetOffset = getViewOffset(mRelativeViewNumber, mRelativeViewNumber);
			mTargetDistance = mTargetOffset - mInitialOffset;

			// Configure base animation properties
			this.setDuration(mAnimationDuration);
			this.setInterpolator(mDecelerateInterpolater);

			// Start/continued animation
			mIsAnimationInProgres = true;
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation transformation) {
			// Ensure interpolatedTime does not over-shoot then calculate new
			// offset
			interpolatedTime = (interpolatedTime > 1.0f) ? 1.0f : interpolatedTime;
			int offset = mInitialOffset + (int)(mTargetDistance * interpolatedTime);

			for (int viewNumber = 0; viewNumber < 3; viewNumber++) {
				// Only need to animate the visible views as the other view will
				// always be off-screen
				if((mTargetDistance > 0 && viewNumber != getNextViewNumber(mRelativeViewNumber)) ||
						(mTargetDistance < 0 && viewNumber != getPrevViewNumber(mRelativeViewNumber))) {
					mViews[viewNumber].setOffset(offset, 0, mRelativeViewNumber);
				}
			}
		}

		@Override
		public boolean getTransformation(long currentTime, Transformation outTransformation) {
			if (super.getTransformation(currentTime, outTransformation) == false) {
				// Perform final adjustment to offsets to cleanup animation
				mViews[0].setOffset(mTargetOffset, 0, mRelativeViewNumber);
				mViews[1].setOffset(mTargetOffset, 0, mRelativeViewNumber);
				mViews[2].setOffset(mTargetOffset, 0, mRelativeViewNumber);

				// Reached the animation target
				mIsAnimationInProgres = false;

				return false;
			}

			// Cancel if the screen touched
			if(mIsTouched || mIsDragging) {
				// Note that at this point we still consider ourselves to be
				// animating
				// because we have not yet reached the target offset; its just
				// that the
				// user has temporarily interrupted the animation with a touch
				// gesture

				return false;
			}

			return true;
		}
	}

	private class FlingGestureDetector extends GestureDetector.SimpleOnGestureListener {
		boolean isFling = true;
		@Override
		public boolean onDown(MotionEvent e) {
			// Stop animation
			mIsTouched = true;

			// Reset fling state
			mFlingDirection = 0;
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if (e1 != null && e2 != null) {
				if (e2.getAction() == MotionEvent.ACTION_MOVE) {

					// 확대 상태라면 드래그시 이미지의 가장 좌/우측에 와있을 때만 움직이도록 한다.
					if (scaleWidth > mGalleryWidth) {
						if (transX != 0.0 && transX + (scaleWidth - mGalleryWidth) != 0.0) {
							return false;
						}
					}

					if (mIsDragging == false) {
						// Stop animation
						mIsTouched = true;

						// Reconfigure scroll
						mIsDragging = true;
						mFlingDirection = 0;
						mScrollTimestamp = System.currentTimeMillis();
						mCurrentOffset = mViews[mCurrentViewNumber].getCurrentOffset();
					}

					float maxVelocity = mGalleryWidth / (mAnimationDuration / 1000.0f);
					long timestampDelta = System.currentTimeMillis() - mScrollTimestamp;
					float maxScrollDelta = maxVelocity * (timestampDelta / 1000.0f);
					float currentScrollDelta = e1.getX() - e2.getX();

					if (currentScrollDelta < maxScrollDelta * -1)
						currentScrollDelta = maxScrollDelta * -1;
					if (currentScrollDelta > maxScrollDelta)
						currentScrollDelta = maxScrollDelta;
					int scrollOffset = Math.round(mCurrentOffset + currentScrollDelta);

					// We can't scroll more than the width of our own frame
					// layout
					if (scrollOffset >= mGalleryWidth)
						scrollOffset = mGalleryWidth;
					if (scrollOffset <= mGalleryWidth * -1)
						scrollOffset = mGalleryWidth * -1;

					mViews[0].setOffset(scrollOffset, 0, mCurrentViewNumber);
					mViews[1].setOffset(scrollOffset, 0, mCurrentViewNumber);
					mViews[2].setOffset(scrollOffset, 0, mCurrentViewNumber);

					if (Math.abs(scrollOffset) > (displayWidth/2)) {
						isFling = false;
					} else isFling = true;
				}
			}

			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (scaleWidth > mGalleryWidth) {
				mAnimation.prepareAnimation(mCurrentViewNumber);
				startAnimation(mAnimation);
			} else if (Math.abs(e1.getY() - e2.getY()) <= swipe_max_off_path) {
				if (e2.getX() - e1.getX() > swipe_min_distance && Math.abs(velocityX) > swipe_threshold_veloicty && isFling) {
					movePrevious();
				}

				if (e1.getX() - e2.getX() > swipe_min_distance && Math.abs(velocityX) > swipe_threshold_veloicty && isFling) {
					moveNext();
				}
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// Finalise scrolling
//			mFlingDirection = 0;
//			processGesture();

		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// Reset fling state
			mFlingDirection = 0;
			return false;
		}
		
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			matrix.postScale(0, 0, mid.x, mid.y);
			savedMatrix = matrix;
			return false;
		}
	}

	public int getCurrentPosition() {
		return mCurrentPosition;
	}

	public Matrix getOriginalMatrix() {
		return matrix;
	}

	public Matrix getSavedMatrix() {
		return savedMatrix;
	}

	public PointF getMid() {
		return mid;
	}

}
