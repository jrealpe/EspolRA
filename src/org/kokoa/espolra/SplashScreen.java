package org.kokoa.espolra;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SplashScreen extends Activity {

	/**
	 * The thread to process splash screen events
	 */
	private Thread mSplashThread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Splash screen view
		setContentView(R.layout.splashscreen);

		final SplashScreen sPlashScreen = this;

		// The thread to wait for splash screen events
		mSplashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						// Wait given period of time or exit on touch
						wait(2000);
					}
				} catch (InterruptedException ex) {
				}

				finish();

				// Run next activity


				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);
				finish();

			}
		};
		final ImageView splashImageView = (ImageView) findViewById(R.id.SplashImageView);
		splashImageView.setBackgroundResource(R.drawable.kokoasplash);
		final AnimationDrawable frameAnimation = (AnimationDrawable) splashImageView
				.getBackground();
		splashImageView.post(new Runnable() {
			@Override
			public void run() {
				frameAnimation.start();
			}
		});
		mSplashThread.start();
	}

	/**
	 * Processes splash screen touch events
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mSplashThread) {
				mSplashThread.notifyAll();
			}
		}
		return true;
	}
}