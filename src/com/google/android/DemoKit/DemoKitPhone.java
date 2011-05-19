package com.google.android.DemoKit;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DemoKitPhone extends BaseActivity implements OnClickListener {
	static final String TAG = "DemoKitPhone";
	/** Called when the activity is first created. */
	TextView mInputLabel;
    TextView mOutputLabel;
    TextView mTiltLabel;
	LinearLayout mInputContainer;
    LinearLayout mOutputContainer;
    LinearLayout mTiltContainer;
	Drawable mFocusedTabImage;
	Drawable mNormalTabImage;
	OutputController mOutputController;

	@Override
	protected void hideControls() {
		super.hideControls();
		mOutputController = null;
	}

	public void onCreate(Bundle savedInstanceState) {
		mFocusedTabImage = getResources().getDrawable(
				R.drawable.tab_focused_holo_dark);
		mNormalTabImage = getResources().getDrawable(
				R.drawable.tab_normal_holo_dark);
		super.onCreate(savedInstanceState);
	}

	protected void showControls() {
		super.showControls();

		mOutputController = new OutputController(this, false);
		mOutputController.accessoryAttached();
		
		mInputLabel = (TextView) findViewById(R.id.inputLabel);
        mOutputLabel = (TextView) findViewById(R.id.outputLabel);
        mTiltLabel = (TextView) findViewById(R.id.tiltLabel);
		mInputContainer = (LinearLayout) findViewById(R.id.inputContainer);
        mOutputContainer = (LinearLayout) findViewById(R.id.outputContainer);
        mTiltContainer = (LinearLayout) findViewById(R.id.tiltContainer);
		mInputLabel.setOnClickListener(this);
        mOutputLabel.setOnClickListener(this);
        mTiltLabel.setOnClickListener(this);

		showTabContents(R.id.inputLabel);
	}

	void showTabContents(int id) {
		if (id == R.id.inputLabel) {
			mInputContainer.setVisibility(View.VISIBLE);
			mInputLabel.setBackgroundDrawable(mFocusedTabImage);
            mOutputContainer.setVisibility(View.GONE);
            mOutputLabel.setBackgroundDrawable(mNormalTabImage);
            mTiltContainer.setVisibility(View.GONE);
            mTiltLabel.setBackgroundDrawable(mNormalTabImage);
            mTiltController.accessoryDetached();
		} else if (id == R.id.outputLabel) {
            mInputContainer.setVisibility(View.GONE);
            mInputLabel.setBackgroundDrawable(mNormalTabImage);
            mOutputContainer.setVisibility(View.VISIBLE);
            mOutputLabel.setBackgroundDrawable(mFocusedTabImage);
            mTiltContainer.setVisibility(View.GONE);
            mTiltLabel.setBackgroundDrawable(mNormalTabImage);
            mTiltController.accessoryDetached();
        } else if (id == R.id.tiltLabel) {
            mInputContainer.setVisibility(View.GONE);
            mInputLabel.setBackgroundDrawable(mNormalTabImage);
            mOutputContainer.setVisibility(View.GONE);
            mOutputLabel.setBackgroundDrawable(mNormalTabImage);
            mTiltContainer.setVisibility(View.VISIBLE);
            mTiltLabel.setBackgroundDrawable(mFocusedTabImage);
            mTiltController.accessoryAttached();
        }
	}

	public void onClick(View v) {
	    showTabContents(v.getId());
	}

}