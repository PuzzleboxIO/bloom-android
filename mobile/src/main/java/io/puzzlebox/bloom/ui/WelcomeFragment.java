package io.puzzlebox.bloom.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
//import android.support.v4.app.Fragment;


import io.puzzlebox.bloom.R;

/**
 * Created by sc on 5/9/15.
 */

public class WelcomeFragment extends io.puzzlebox.jigsaw.ui.WelcomeFragment {

	/**
	 * Configuration
	 */

	private VideoView mVideoView;
	private int position = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View v = inflater.inflate(io.puzzlebox.bloom.R.layout.fragment_welcome, container, false);


		// Background video
		mVideoView = (VideoView) v.findViewById(R.id.video_view);

		try {
			mVideoView.setVideoURI(Uri.parse("android.resource://" +
					  getActivity().getPackageName() +
					  "/" + R.raw.welcome_puzzlebox_bloom));

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}

		mVideoView.requestFocus();
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			// Close the progress bar and play the video
			public void onPrepared(MediaPlayer mp) {
				mVideoView.seekTo(position);
				if (position == 0) {
					mVideoView.start();
				} else {
					mVideoView.pause();
				}
			}
		});


		mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion (MediaPlayer mp) {
				position = 0;
				mVideoView.seekTo(position);
				mVideoView.start();
			}
		});


		return v;

	}

}