package com.deadswine.colorbars;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Adam Frêœko
 *         <p>
 *         Copyright
 *         <p>
 *         Deadswine Studio 17 czerwiec 2014
 */
public class ColorBars {
	private static String TAG = "TAG - ColorBars";

	private Activity mActivity;
	private Context mContext;
	private ActionBar mActionBar;

	private static boolean ORIENTATION_LANDSCAPE = false;
	private static boolean ORIENTATION_PORTRAIT = true;
	private static Boolean mOrientation = false;

	private static SystemBarTintManager mTintManager;
	private static SystemBarTintManager.SystemBarConfig mConfig;

	private int currentColor = 0xFF5161BC;

	private final Handler handler = new Handler();

	private View mView;
	
	static int B=0;

	static int T=0;

	public static class orientationLandscape {

		private static Drawable oldBackgroundActionBar = null;
		private static Drawable oldBackgroundStatusBar = null;
		private static Drawable oldBackgroundNavigationBar = null;

	}

	public static class orientationPortrait {

		private static Drawable oldBackgroundActionBar = null;
		private static Drawable oldBackgroundStatusBar = null;
		private static Drawable oldBackgroundNavigationBar = null;

	}

	public ColorBars(Activity activity, View view, int PaddingBotom,int PaddingTop) {
		mActivity = activity;
		mActionBar = activity.getActionBar();
		mContext = mActivity.getApplicationContext();
		mTintManager = new SystemBarTintManager(mActivity);

		B = PaddingBotom;
		T = PaddingTop;
		
		
		mView = view;

		// TODO
		// mTintManager = new SystemBarTintManager(activity);
		// mTintManager.setStatusBarTintEnabled(true);
		// mTintManager.setNavigationBarTintEnabled(true);

		int ot = mContext.getResources().getConfiguration().orientation;
		switch (ot) {

		case Configuration.ORIENTATION_LANDSCAPE:
			Log.d("my orient", "ORIENTATION_LANDSCAPE");
			mOrientation = false;

			break;

		case Configuration.ORIENTATION_PORTRAIT:
			Log.d("my orient", "ORIENTATION_PORTRAIT");
			mOrientation = true;

			break;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mOrientation) {
			setTranslucentStatus(true);
		}
		
		mTintManager = new SystemBarTintManager(mActivity);
		
		if (mOrientation) {
			
			mTintManager.setStatusBarTintEnabled(true);
			
			mTintManager.setNavigationBarTintEnabled(true);
		}
		
		changeColor(currentColor);
		
		if (mOrientation) {
			setInsets(mActivity, mView, true, true);
		}



	}

	public static void setInsets(Activity context, View view,
			boolean includeTop, boolean includeBottom) {
		setInsets(context, view, includeTop, includeBottom, false);
	}

	public static void setInsets(Activity context, View view,
			boolean includeTop, boolean includeBottom, boolean noTranslucencyTop) {
		int topPadding = 0;
		int bottomPadding = 0;
		int rightPadding = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// SystemBarTintManager tintManager = new
			// SystemBarTintManager(context);
			mConfig = mTintManager.getConfig();
			if (!noTranslucencyTop) {
				topPadding = mConfig.getPixelInsetTop(true) + T;
				Log.d(TAG,
						"config.getPixelInsetTop(true); = "
								+ mConfig.getPixelInsetTop(true));
			}

			bottomPadding = mConfig.getPixelInsetBottom() - B;// -
															// (mConfig.getStatusBarHeight()/2);//
															// Math.round(pxFromDp(config.getPixelInsetBottom()));
			// //Math.round(dpFromPx(config.getPixelInsetBottom()));

			Log.d(TAG,
					"config.getPixelInsetBottom; = "
							+ mConfig.getPixelInsetBottom());

			rightPadding = mConfig.getPixelInsetRight();
			Log.d(TAG,
					"config.getPixelInsetRight; = "
							+ mConfig.getPixelInsetRight());

			if (mOrientation == ORIENTATION_LANDSCAPE) {

				bottomPadding = bottomPadding - topPadding;
			}

		}
		view.setPadding(0, includeTop ? topPadding : 0, rightPadding,
				includeBottom ? bottomPadding : 0);
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = mActivity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);

		Window w = mActivity.getWindow(); // in Activity's onCreate() for
											// instance
		w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	public void changeColor(int newColor) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = mContext.getResources().getDrawable(
					R.drawable.actionbar_bottom);
			LayerDrawable ld1 = new LayerDrawable(new Drawable[] {
					colorDrawable, bottomDrawable }); //
			LayerDrawable ld2 = new LayerDrawable(
					new Drawable[] { colorDrawable }); //
			LayerDrawable ld3 = new LayerDrawable(
					new Drawable[] { colorDrawable }); //

			if (mOrientation == ORIENTATION_PORTRAIT) {

				if (orientationPortrait.oldBackgroundActionBar == null) {

					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						ld1.setCallback(drawableCallback);
					} else {

						mActionBar.setBackgroundDrawable(ld1);

					}

					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						ld2.setCallback(drawableCallback2);
					} else {

						Log.d(TAG,
								"ld2.getIntrinsicHeight() = "
										+ ld2.getIntrinsicHeight());
						Log.d(TAG,
								"ld2.getIntrinsicWidth()  = "
										+ ld2.getIntrinsicWidth());

						mTintManager.setStatusBarTintDrawable(ld2);

					}

					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						ld3.setCallback(drawableCallback3);
					} else {

						mTintManager.setNavigationBarTintDrawable(ld3);

					}

				} else {

					TransitionDrawable td1 = new TransitionDrawable(
							new Drawable[] {
									orientationPortrait.oldBackgroundActionBar,
									ld1 });
					TransitionDrawable td2 = new TransitionDrawable(
							new Drawable[] {
									orientationPortrait.oldBackgroundStatusBar,
									ld2 });
					TransitionDrawable td3 = new TransitionDrawable(
							new Drawable[] {
									orientationPortrait.oldBackgroundNavigationBar,
									ld3 });

					// workaround for broken ActionBarContainer drawable
					// handling on
					// pre-API 17 builds
					// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						td1.setCallback(drawableCallback);
					} else {

						mActionBar.setBackgroundDrawable(td1);

					}
					// workaround for broken ActionBarContainer drawable
					// handling on
					// pre-API 17 builds
					// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						td1.setCallback(drawableCallback2);
					} else {

						Log.d(TAG,
								"td2.getIntrinsicHeight() = "
										+ td2.getIntrinsicHeight());
						Log.d(TAG,
								"td2.getIntrinsicWidth()  = "
										+ td2.getIntrinsicWidth());
						mTintManager.setStatusBarTintDrawable(td2);

					}
					// workaround for broken ActionBarContainer drawable
					// handling on
					// pre-API 17 builds
					// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						td1.setCallback(drawableCallback3);
					} else {

						mTintManager.setNavigationBarTintDrawable(td3);

					}

					td1.startTransition(200);

					td2.startTransition(200);

					td3.startTransition(200);

				}

				orientationPortrait.oldBackgroundActionBar = ld1;
				orientationPortrait.oldBackgroundStatusBar = ld2;
				orientationPortrait.oldBackgroundNavigationBar = ld3;

				// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
				// getActionBar().setDisplayShowTitleEnabled(false);
				mActionBar.setDisplayShowTitleEnabled(false);
				mActionBar.setDisplayShowTitleEnabled(true);
			} else {
				if (orientationLandscape.oldBackgroundActionBar == null) {

					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						ld1.setCallback(drawableCallback);
					} else {

						mActionBar.setBackgroundDrawable(ld1);

					}

					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						ld2.setCallback(drawableCallback2);
					} else {

						Log.d(TAG,
								"ld2.getIntrinsicHeight() = "
										+ ld2.getIntrinsicHeight());
						Log.d(TAG,
								"ld2.getIntrinsicWidth()  = "
										+ ld2.getIntrinsicWidth());

						// mTintManager.setStatusBarTintDrawable(ld2);

					}

					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						ld3.setCallback(drawableCallback3);
					} else {

						mTintManager.setNavigationBarTintDrawable(ld3);

					}

				} else {

					TransitionDrawable td1 = new TransitionDrawable(
							new Drawable[] {
									orientationLandscape.oldBackgroundActionBar,
									ld1 });
					TransitionDrawable td2 = new TransitionDrawable(
							new Drawable[] {
									orientationLandscape.oldBackgroundStatusBar,
									ld2 });
					TransitionDrawable td3 = new TransitionDrawable(
							new Drawable[] {
									orientationLandscape.oldBackgroundNavigationBar,
									ld3 });

					// workaround for broken ActionBarContainer drawable
					// handling on
					// pre-API 17 builds
					// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						td1.setCallback(drawableCallback);
					} else {

						mActionBar.setBackgroundDrawable(td1);

					}
					// workaround for broken ActionBarContainer drawable
					// handling on
					// pre-API 17 builds
					// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						td1.setCallback(drawableCallback2);
					} else {

						Log.d(TAG,
								"td2.getIntrinsicHeight() = "
										+ td2.getIntrinsicHeight());
						Log.d(TAG,
								"td2.getIntrinsicWidth()  = "
										+ td2.getIntrinsicWidth());
						mTintManager.setStatusBarTintDrawable(td2);

					}
					// workaround for broken ActionBarContainer drawable
					// handling on
					// pre-API 17 builds
					// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
						td1.setCallback(drawableCallback3);
					} else {

						mTintManager.setNavigationBarTintDrawable(td3);

					}

					td1.startTransition(200);

					// td2.startTransition(200);

					td3.startTransition(200);

				}

				orientationLandscape.oldBackgroundActionBar = ld1;
				orientationLandscape.oldBackgroundStatusBar = ld2;
				orientationLandscape.oldBackgroundNavigationBar = ld3;

				// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
				// getActionBar().setDisplayShowTitleEnabled(false);
				mActionBar.setDisplayShowTitleEnabled(false);
				mActionBar.setDisplayShowTitleEnabled(true);
			}
		}

		currentColor = newColor;

	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			mActionBar.setBackgroundDrawable(who);

		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	private Drawable.Callback drawableCallback2 = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			mTintManager.setStatusBarTintDrawable(who);

		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};
	private Drawable.Callback drawableCallback3 = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			mTintManager.setNavigationBarTintDrawable(who);

		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

}
