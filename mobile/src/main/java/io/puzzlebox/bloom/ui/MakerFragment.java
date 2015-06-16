/**
 * Puzzlebox Jigsaw
 * Copyright 2015 Puzzlebox Productions, LLC
 * License: GNU Affero General Public License Version 3
 */

package io.puzzlebox.bloom.ui;

//import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.puzzlebox.bloom.R;
import io.puzzlebox.bloom.data.BloomSingleton;
import io.puzzlebox.jigsaw.data.SessionSingleton;
import io.puzzlebox.jigsaw.protocol.MuseService;
import io.puzzlebox.jigsaw.protocol.RBLGattAttributes;
import io.puzzlebox.jigsaw.protocol.RBLService;
import io.puzzlebox.jigsaw.protocol.ThinkGearService;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;

//@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MakerFragment extends Fragment
		  implements SeekBar.OnSeekBarChangeListener {

	/**
	 * TODO
	 * - Convert Bluetooth handling to support background control
	 * - Range cuts out during Bloom control with MindWave
	 * - Muse packets too fast for Bloom
	 */

	private final static String TAG = MakerFragment.class.getSimpleName();

	private OnFragmentInteractionListener mListener;

	/**
	 * Configuration
	 */
	int eegPower = 0;

	int bloomRange = 0;
	int bloomRangeMax = 128;
	int bloomServoPercentage = 0;
	int bloomColorRed = 0;
	int bloomColorGreen = 0;
	int bloomColorBlue = 0;


	/**
	 * UI
	 */
//	Configuration config;
//	Button connectButton;

	ProgressBar progressBarRange;

//	ImageView imageViewStatus;

	private Button connectBloom = null;

	private Button buttonOpen = null;
	private Button buttonClose = null;
	private Button buttonCycleServo = null;
	private Button buttonCycleRGB = null;
	private Button buttonDemo = null;

	//	private TextView rssiValue = null;
	private SeekBar servoSeekBar;

	private SeekBar redSeekBar;
	private SeekBar greenSeekBar;
	private SeekBar blueSeekBar;

	//	private static XYPlot sessionPlot1 = null;
	//	private static SimpleXYSeries sessionPlotSeries1 = null;
	//	private static XYPlot sessionPlot2 = null;
	//	private static SimpleXYSeries sessionPlotSeries2 = null;

	/**
	 * Bluetooth LE
	 */
	//	private BluetoothGattCharacteristic characteristicTx = null;
	//	private RBLService mBluetoothLeService;
	//	private BluetoothAdapter mBluetoothAdapter;
	//	private BluetoothDevice mDevice = null;
	//	private String mDeviceAddress;
	//
	//	private boolean flag = true;
	//	private boolean connState = false;
	//	private boolean scanFlag = false;
	//
	//	private byte[] data = new byte[3];
	//	private static final int REQUEST_ENABLE_BT = 1;
	//	private static final long SCAN_PERIOD = 2000;
	//
	//	final private static char[] hexArray = { '0', '1', '2', '3', '4', '5', '6',
	//			  '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };


	// ################################################################

	public static MakerFragment newInstance() {
		MakerFragment fragment = new MakerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}


	// ################################################################

	public MakerFragment() {
		// Required empty public constructor
	}


	// ################################################################

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

	}


	// ################################################################

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View v = inflater.inflate(R.layout.fragment_maker, container, false);

//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//		setContentView(R.layout.main);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);


		progressBarRange = (ProgressBar) v.findViewById(R.id.progressBarRange);
//		ShapeDrawable progressBarRangeDrawable = new ShapeDrawable(new RoundRectShape(roundedCorners, null,null));
		ShapeDrawable progressBarRangeDrawable = new ShapeDrawable();
//		String progressBarRangeColor = "#FF00FF";
		String progressBarRangeColor = "#990099";
		progressBarRangeDrawable.getPaint().setColor(Color.parseColor(progressBarRangeColor));
		ClipDrawable progressRange = new ClipDrawable(progressBarRangeDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
		progressBarRange.setProgressDrawable(progressRange);
		progressBarRange.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.progress_horizontal));

//		progressBarRange.setMax(128 + 127);
		progressBarRange.setMax(bloomRangeMax);


//		imageViewStatus = (ImageView) v.findViewById(R.id.imageViewStatus);


		servoSeekBar = (SeekBar) v.findViewById(R.id.ServoSeekBar);
		servoSeekBar.setEnabled(false);
//		servoSeekBar.setMax(180);
		servoSeekBar.setMax(100);
		servoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
			                              boolean fromUser) {

				byte[] buf = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x00};

				buf[1] = (byte) servoSeekBar.getProgress();

				BloomSingleton.getInstance().characteristicTx.setValue(buf);
				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
			}
		});


//		rssiValue = (TextView) v.findViewById(R.id.rssiValue);

		connectBloom = (Button) v.findViewById(R.id.connectBloom);


		connectBloom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (!BloomSingleton.getInstance().scanFlag) {
						scanLeDevice();

						Timer mTimer = new Timer();
						mTimer.schedule(new TimerTask() {

							@Override
							public void run() {
								if ((BloomSingleton.getInstance().mDevice != null) &&
										  (BloomSingleton.getInstance().mDevice.getAddress() != null) &&
										  (BloomSingleton.getInstance().mBluetoothLeService != null)) {
									BloomSingleton.getInstance().mDeviceAddress = BloomSingleton.getInstance().mDevice.getAddress();
									if (BloomSingleton.getInstance().mDeviceAddress != null)
										BloomSingleton.getInstance().mBluetoothLeService.connect(BloomSingleton.getInstance().mDeviceAddress);
									else {
										Toast toast = Toast
												  .makeText(
															 getActivity(),
															 "Error connecting to Puzzlebox Bloom",
															 Toast.LENGTH_SHORT);
										toast.setGravity(0, 0, Gravity.CENTER | Gravity.BOTTOM);
										toast.show();
									}
									BloomSingleton.getInstance().scanFlag = true;
								} else {
									getActivity().runOnUiThread(new Runnable() {
										public void run() {
											Toast toast = Toast
													  .makeText(
																 getActivity(),
																 "Error connecting to Puzzlebox Bloom",
																 Toast.LENGTH_SHORT);
											toast.setGravity(0, 0, Gravity.CENTER | Gravity.BOTTOM);
											toast.show();
										}
									});
								}
							}
						}, BloomSingleton.getInstance().SCAN_PERIOD);
					}
				} catch (Exception e) {
					Log.e(TAG, "Exception connecting to Bloom: " + e);
					Toast toast = Toast
							  .makeText(
										 getActivity(),
										 "Exception connecting to Puzzlebox Bloom",
										 Toast.LENGTH_SHORT);
					toast.setGravity(0, 0, Gravity.CENTER | Gravity.BOTTOM);
					toast.show();
				}

				System.out.println(BloomSingleton.getInstance().connState);
//				Log.e(TAG, connState);
//				if (connState == false) {
				if (!BloomSingleton.getInstance().connState && BloomSingleton.getInstance().mDeviceAddress != null) {
					BloomSingleton.getInstance().mBluetoothLeService.connect(BloomSingleton.getInstance().mDeviceAddress);
				} else {
					if (BloomSingleton.getInstance().mBluetoothLeService != null) {
						setBloomRGBOff();
						BloomSingleton.getInstance().mBluetoothLeService.disconnect();
						BloomSingleton.getInstance().mBluetoothLeService.close();
						setButtonDisable();
					}
				}
			}
		});





		redSeekBar = (SeekBar) v.findViewById(R.id.seekBarRed);
		redSeekBar.setEnabled(false);
		redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
			                              boolean fromUser) {

				bloomColorRed = redSeekBar.getProgress();

				byte[] buf = new byte[]{(byte) 0x0A, (byte) 0x00, (byte) bloomColorRed};

				BloomSingleton.getInstance().characteristicTx.setValue(buf);
				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

			}
		});




		greenSeekBar = (SeekBar) v.findViewById(R.id.seekBarGreen);
		greenSeekBar.setEnabled(false);
		greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
			                              boolean fromUser) {

				bloomColorGreen = greenSeekBar.getProgress();

				byte[] buf = new byte[]{(byte) 0x0A, (byte) 0x01, (byte) bloomColorGreen};

				BloomSingleton.getInstance().characteristicTx.setValue(buf);
				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

			}
		});



		blueSeekBar = (SeekBar) v.findViewById(R.id.seekBarBlue);
		blueSeekBar.setEnabled(false);
		blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
			                              boolean fromUser) {

				bloomColorBlue = blueSeekBar.getProgress();

				byte[] buf = new byte[]{(byte) 0x0A, (byte) 0x02, (byte) bloomColorBlue};

				BloomSingleton.getInstance().characteristicTx.setValue(buf);
				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

			}
		});



		buttonOpen = (Button) v.findViewById(R.id.buttonOpen);
		buttonOpen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] buf = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x00};
				BloomSingleton.getInstance().characteristicTx.setValue(buf);
				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
			}
		});
//		buttonOpen.setVisibility(View.GONE);

		buttonClose = (Button) v.findViewById(R.id.buttonClose);
		buttonClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] buf = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00 };
				BloomSingleton.getInstance().characteristicTx.setValue(buf);
				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
			}
		});
//		buttonClose.setVisibility(View.GONE);







		buttonCycleServo = (Button) v.findViewById(R.id.buttonDemoServo);
		buttonCycleServo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				byte[] buf = new byte[] { (byte) 0x03, (byte) 0x00, (byte) 0x00 }; // Cycle
				byte[] buf = new byte[] { (byte) 0x04, (byte) 0x00, (byte) 0x00 }; // CycleSlow
				BloomSingleton.getInstance().characteristicTx.setValue(buf);
				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
			}
		});




		buttonCycleRGB = (Button) v.findViewById(R.id.buttonDemoRGB);
		buttonCycleRGB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] buf = new byte[] { (byte) 0x06, (byte) 0x00, (byte) 0x00 };
				BloomSingleton.getInstance().characteristicTx.setValue(buf);
				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

				updateBloomRGB();

			}
		});




		buttonDemo = (Button) v.findViewById(R.id.buttonDemo);
		buttonDemo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] buf = new byte[] { (byte) 0x04, (byte) 0x00, (byte) 0x00 };
				BloomSingleton.getInstance().characteristicTx.setValue(buf);
				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
			}
		});



//		buttonDemo = (Button) v.findViewById(R.id.buttonDemo);
//		buttonDemo.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				byte[] buf;
////				if (! BloomSingleton.getInstance().demoActive) {
//				BloomSingleton.getInstance().demoActive = true;
//
//				// bloomOpen()
////				buf = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x00};
////				BloomSingleton.getInstance().characteristicTx.setValue(buf);
////				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
//
//				// loopRGB()
//				buf = new byte[]{(byte) 0x06, (byte) 0x00, (byte) 0x00};
//				BloomSingleton.getInstance().characteristicTx.setValue(buf);
//				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
//
//				// Set Red to 0
//				buf = new byte[]{(byte) 0x0A, (byte) 0x00, (byte) 0x00}; // R = 0
//				BloomSingleton.getInstance().characteristicTx.setValue(buf);
//				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
//
//				// bloomClose()
////				buf = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00};
////				BloomSingleton.getInstance().characteristicTx.setValue(buf);
////				BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
//
//
////				} else {
////					BloomSingleton.getInstance().demoActive = false;
//////					buf = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00};
//////					BloomSingleton.getInstance().characteristicTx.setValue(buf);
//////					BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
////					buf = new byte[]{(byte) 0x0A, (byte) 0x00, (byte) 0x00}; // R = 0
////					BloomSingleton.getInstance().characteristicTx.setValue(buf);
////					BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
//////					buf = new byte[]{(byte) 0x0A, (byte) 0x01, (byte) 0x00}; // G = 0
//////					BloomSingleton.getInstance().characteristicTx.setValue(buf);
//////					BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
//////					buf = new byte[]{(byte) 0x0A, (byte) 0x02, (byte) 0x00}; // B = 0
//////					BloomSingleton.getInstance().characteristicTx.setValue(buf);
//////					BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);
////				}
//			}
//		});


		if (!getActivity().getPackageManager().hasSystemFeature(
				  PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(getActivity(), "Bluetooth LE not supported", Toast.LENGTH_SHORT)
					  .show();
			getActivity().finish();
		}

		final BluetoothManager mBluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
		BloomSingleton.getInstance().mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (BloomSingleton.getInstance().mBluetoothAdapter == null) {
			Toast.makeText(getActivity(), "Bluetooth LE not supported", Toast.LENGTH_SHORT)
					  .show();
			getActivity().finish();
			return v;
		}

		Intent gattServiceIntent = new Intent(getActivity(),
				  RBLService.class);
//		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		getActivity().bindService(gattServiceIntent, mServiceConnection, getActivity().BIND_AUTO_CREATE);


		/**
		 * Update settings according to default UI
		 */

		updateScreenLayout();

//		updatePowerThresholds();
//		updatePower();

		if (BloomSingleton.getInstance().connState) {
			setButtonEnable();
			updateBloomRGB();
		}

		return v;

	}


	// ################################################################

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					  + " must implement OnFragmentInteractionListener");
		}
	}


	// ################################################################

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}


	// ################################################################

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}


	// ################################################################

	@Override
	public void onResume() {

		super.onResume();


//		updatePowerThresholds();
//		updatePower();


		if (!BloomSingleton.getInstance().mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					  BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, BloomSingleton.getInstance().REQUEST_ENABLE_BT);
		}

		getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

		Intent gattServiceIntent = new Intent(getActivity(),
				  RBLService.class);
		getActivity().bindService(gattServiceIntent, mServiceConnection, getActivity().BIND_AUTO_CREATE);

//		if (BloomSingleton.getInstance().connState)
//			setButtonText(R.id.connectBloom, "Disconnect Bloom");

//		updateSessionTime();

		LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(
				  mPacketReceiver, new IntentFilter("io.puzzlebox.jigsaw.protocol.thinkgear.packet"));


	}


	// ################################################################

	public void onPause() {

		Log.v(TAG, "onPause()");

		super.onPause();


		LocalBroadcastManager.getInstance(
				  getActivity().getApplicationContext()).unregisterReceiver(
				  mPacketReceiver);

		getActivity().unregisterReceiver(mGattUpdateReceiver);

		getActivity().unbindService(mServiceConnection);


		setBloomRGBOff();

		// Force disconnect from Bloom until BLE handling shifted into background service
		if (BloomSingleton.getInstance().mBluetoothLeService != null) {
			BloomSingleton.getInstance().mBluetoothLeService.disconnect();
			BloomSingleton.getInstance().mBluetoothLeService.close();
		}


		setButtonDisable();


	} // onPause


	// ################################################################

	@Override
	public void onDestroy() {
		super.onDestroy();

		try {
			if (mServiceConnection != null)
				getActivity().unbindService(mServiceConnection);
		} catch (IllegalArgumentException e) {
			Log.w(TAG, "Exception in onDestroy(): " + e);
		}

	}


	// ################################################################

//	@Override
//	public void onStop() {
//		super.onStop();
//
//		BloomSingleton.getInstance().flag = false;
//
//		getActivity().unregisterReceiver(mGattUpdateReceiver);
//
//	}


	// ################################################################

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		menu.add("Share")
				  .setOnMenuItemClickListener(this.mShareButtonClickListener)
				  .setIcon(android.R.drawable.ic_menu_share)
				  .setShowAsAction(SHOW_AS_ACTION_ALWAYS);

		super.onCreateOptionsMenu(menu, inflater);

	}


	// ################################################################

	MenuItem.OnMenuItemClickListener mShareButtonClickListener = new MenuItem.OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem item) {

			Intent i = SessionSingleton.getInstance().getExportSessionIntent(getActivity().getApplicationContext(), item);

			if (i != null) {
				startActivity(i);
			} else {
				Toast.makeText(getActivity().getApplicationContext(), "Error export session data for sharing", Toast.LENGTH_SHORT).show();
			}

			return false;
		}
	};


	// ################################################################

//	private void resetSession() {
//
//		Log.d(TAG, "SessionSingleton.getInstance().resetSession()");
//		SessionSingleton.getInstance().resetSession();
//
//		textViewSessionTime.setText( R.string.session_time );
//
//		Toast.makeText((getActivity().getApplicationContext()),
//				  "Session data reset",
//				  Toast.LENGTH_SHORT).show();
//
//	}


	// ################################################################

	private BroadcastReceiver mPacketReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

//			int eegAttention = Integer.valueOf(intent.getStringExtra("Attention"));
//			int eegMeditation = Integer.valueOf(intent.getStringExtra("Meditation"));
//			int eegSignal = Integer.valueOf(intent.getStringExtra("Signal Level"));
//
////			Log.e(TAG, "eegAttention: " + eegAttention);
//
//			progressBarAttention.setProgress(eegAttention);
//			progressBarMeditation.setProgress(eegMeditation);
//			progressBarSignal.setProgress(eegSignal);
//
//			updatePower();
//
//
////			updateSessionTime();
//
////			sessionPlotSeries1 = updateSessionPlotHistory(
////					  "Attention",
////					  SessionSingleton.getInstance().getSessionRangeValues(
////								 "Attention", 30),
////					  Color.RED,
////					  sessionPlot1,
////					  sessionPlotSeries1);
//////			updateSessionPlotHistory(
//////					  SessionSingleton.getInstance().getSessionRangeValues(
//////								 "Attention", 30));
////
//////			updateSessionPlotHistory2(
//////					  SessionSingleton.getInstance().getSessionRangeValues(
//////								 "Meditation", 30));
////
////			sessionPlotSeries2 = updateSessionPlotHistory(
////					  "Meditation",
////					  SessionSingleton.getInstance().getSessionRangeValues(
////								 "Meditation", 30),
////					  Color.BLUE,
////					  sessionPlot2,
////					  sessionPlotSeries2);


		}

	};


	// ################################################################

	private void displayData(String data) {
		if (data != null) {
//			rssiValue.setText(data);

			try{
//				progressBarRange.setProgress( Integer.parseInt(data) );

				// -128 to 127 [https://stackoverflow.com/questions/21609544/bluetooth-rssi-values-are-always-in-dbm-in-all-android-devices]
				bloomRange = Integer.parseInt(data);

				bloomRange = bloomRange + 128;

				if (bloomRange > bloomRangeMax)
					bloomRange = bloomRangeMax;

				progressBarRange.setProgress( bloomRange );

			} catch (Exception e) {
				Log.e(TAG, "Exception: displayData(" + data + ")" + e.toString());
			}

		}
	}


	// ################################################################

//	private void readAnalogInValue(byte[] data) {
//		for (int i = 0; i < data.length; i += 3) {
//			if (data[i] == 0x0A) {
//				if (data[i + 1] == 0x01)
//					digitalInBtn.setChecked(false);
//				else
//					digitalInBtn.setChecked(true);
//			} else if (data[i] == 0x0B) {
//				int Value;
//
//				Value = ((data[i + 1] << 8) & 0x0000ff00)
//						  | (data[i + 2] & 0x000000ff);
//
//				AnalogInValue.setText(Value + "");
//			}
//		}
//	}


	// ################################################################

	private void setButtonEnable() {
		BloomSingleton.getInstance().flag = true;
		BloomSingleton.getInstance().connState = true;

		connectBloom.setText("Disconnect Bloom");

		servoSeekBar.setEnabled(BloomSingleton.getInstance().flag);

		redSeekBar.setEnabled(BloomSingleton.getInstance().flag);
		blueSeekBar.setEnabled(BloomSingleton.getInstance().flag);
		greenSeekBar.setEnabled(BloomSingleton.getInstance().flag);

		buttonOpen.setEnabled(true);
		buttonClose.setEnabled(true);
		buttonCycleServo.setEnabled(true);
		buttonCycleRGB.setEnabled(true);
		buttonDemo.setEnabled(true);

	}


	// ################################################################

	private void setButtonDisable() {
		BloomSingleton.getInstance().flag = false;
		BloomSingleton.getInstance().connState = false;

		connectBloom.setText("Connect Bloom");

		servoSeekBar.setEnabled(BloomSingleton.getInstance().flag);

		redSeekBar.setEnabled(BloomSingleton.getInstance().flag);
		blueSeekBar.setEnabled(BloomSingleton.getInstance().flag);
		greenSeekBar.setEnabled(BloomSingleton.getInstance().flag);

		buttonOpen.setEnabled(false);
		buttonClose.setEnabled(false);
		buttonCycleServo.setEnabled(false);
		buttonCycleRGB.setEnabled(false);
		buttonDemo.setEnabled(false);

		progressBarRange.setProgress(0);
	}


	// ################################################################

	private void setBloomRGBOff() {

		if ((BloomSingleton.getInstance().mBluetoothLeService != null) &&
				  (BloomSingleton.getInstance().characteristicTx != null)) {

			// Set Red to 0
			byte[] buf = new byte[]{(byte) 0x0A, (byte) 0x00, (byte) 0x00}; // R = 0
			BloomSingleton.getInstance().characteristicTx.setValue(buf);
			BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

			// Set Green to 0
			buf = new byte[]{(byte) 0x0A, (byte) 0x01, (byte) 0x00}; // G = 1
			BloomSingleton.getInstance().characteristicTx.setValue(buf);
			BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

			// Set Blue to 0
			buf = new byte[]{(byte) 0x0A, (byte) 0x02, (byte) 0x00}; // B = 2
			BloomSingleton.getInstance().characteristicTx.setValue(buf);
			BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

		}

	}


	// ################################################################

	private void startReadRssi() {
		new Thread() {
			public void run() {

				while (BloomSingleton.getInstance().flag) {
					BloomSingleton.getInstance().mBluetoothLeService.readRssi();
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}


	// ################################################################

	private void getGattService(BluetoothGattService gattService) {
		if (gattService == null)
			return;

		setButtonEnable();

		updateBloomRGB();

		startReadRssi();

		BloomSingleton.getInstance().characteristicTx = gattService
				  .getCharacteristic(RBLService.UUID_BLE_SHIELD_TX);

		BluetoothGattCharacteristic characteristicRx = gattService
				  .getCharacteristic(RBLService.UUID_BLE_SHIELD_RX);
		BloomSingleton.getInstance().mBluetoothLeService.setCharacteristicNotification(characteristicRx,
				  true);
		BloomSingleton.getInstance().mBluetoothLeService.readCharacteristic(characteristicRx);
	}


	// ################################################################

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(RBLService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(RBLService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(RBLService.ACTION_GATT_RSSI);

		return intentFilter;
	}


	// ################################################################

	private void scanLeDevice() {
		new Thread() {

			@Override
			public void run() {
				BloomSingleton.getInstance().mBluetoothAdapter.startLeScan(mLeScanCallback);

				try {
					Thread.sleep(BloomSingleton.getInstance().SCAN_PERIOD);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				BloomSingleton.getInstance().mBluetoothAdapter.stopLeScan(mLeScanCallback);
			}
		}.start();
	}


	// ################################################################

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
		                     final byte[] scanRecord) {

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					byte[] serviceUuidBytes = new byte[16];
					String serviceUuid = "";
					for (int i = 32, j = 0; i >= 17; i--, j++) {
						serviceUuidBytes[j] = scanRecord[i];
					}
					serviceUuid = bytesToHex(serviceUuidBytes);
					if (stringToUuidString(serviceUuid).equals(
							  RBLGattAttributes.BLE_SHIELD_SERVICE
										 .toUpperCase(Locale.ENGLISH))) {
						BloomSingleton.getInstance().mDevice = device;
					}
				}
			});
		}
	};


	// ################################################################

	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = BloomSingleton.getInstance().hexArray[v >>> 4];
			hexChars[j * 2 + 1] = BloomSingleton.getInstance().hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}


	// ################################################################

	private String stringToUuidString(String uuid) {
		StringBuffer newString = new StringBuffer();
		newString.append(uuid.toUpperCase(Locale.ENGLISH).substring(0, 8));
		newString.append("-");
		newString.append(uuid.toUpperCase(Locale.ENGLISH).substring(8, 12));
		newString.append("-");
		newString.append(uuid.toUpperCase(Locale.ENGLISH).substring(12, 16));
		newString.append("-");
		newString.append(uuid.toUpperCase(Locale.ENGLISH).substring(16, 20));
		newString.append("-");
		newString.append(uuid.toUpperCase(Locale.ENGLISH).substring(20, 32));

		return newString.toString();
	}


	// ################################################################

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// User chose not to enable Bluetooth.
		if (requestCode == BloomSingleton.getInstance().REQUEST_ENABLE_BT
				  && resultCode == Activity.RESULT_CANCELED) {
			getActivity().finish();
			return;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}


	// ################################################################

	public void updateScreenLayout() {

//		switch(config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK){
//			case Configuration.SCREENLAYOUT_SIZE_SMALL:
//				Log.v(TAG, "screenLayout: small");
//				updateScreenLayoutSmall();
//				break;
//			case Configuration.SCREENLAYOUT_SIZE_NORMAL:
//				Log.v(TAG, "screenLayout: normal");
//				updateScreenLayoutSmall();
//				break;
//			case Configuration.SCREENLAYOUT_SIZE_LARGE:
//				Log.v(TAG, "screenLayout: large");
//				break;
//			case Configuration.SCREENLAYOUT_SIZE_XLARGE:
//				Log.v(TAG, "screenLayout: xlarge");
//				break;
//			case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
//				Log.v(TAG, "screenLayout: undefined");
//				updateScreenLayoutSmall();
//				break;
//		}

	} // updateScreenLayout


	// ################################################################

//	public void updateScreenLayoutSmall() {
//
////		String button_test_fly_small = getResources().getString(R.string.button_test_fly_small);
////		setButtonText(R.id.buttonTestFly, button_test_fly_small);
////
////		textViewLabelScores.setVisibility(View.VISIBLE);
////		viewSpaceScore.setVisibility(View.VISIBLE);
//
//
//		ViewGroup.LayoutParams layoutParams;
//
////		layoutParams = (android.view.ViewGroup.LayoutParams) viewSpaceScoreLast.getLayoutParams();
////		layoutParams.width = 10;
////		viewSpaceScoreLast.setLayoutParams(layoutParams);
////
////		layoutParams = (android.view.ViewGroup.LayoutParams) viewSpaceScoreHigh.getLayoutParams();
////		layoutParams.width = 10;
////		viewSpaceScoreHigh.setLayoutParams(layoutParams);
////
////
////		String labelScore = getResources().getString(R.string.textview_label_score_small);
////		textViewLabelScore.setText(labelScore);
////
////		String labelLastScore = getResources().getString(R.string.textview_label_last_score_small);
////		textViewLabelLastScore.setText(labelLastScore);
////
////		String labelHighScore = getResources().getString(R.string.textview_label_high_score_small);
////		textViewLabelHighScore.setText(labelHighScore);
//
//
////		// HTC Droid DNA - AndroidPlot has issues with OpenGL Render
////		if ((Build.MANUFACTURER.contains("HTC")) &&
////				  (Build.MODEL.contains("HTC6435LVW"))) {
////
////			Log.v(TAG, "Device detected: HTC Droid DNA");
////			hideEEGRawHistory();
////
////		}
//
//
//	} // updateScreenLayoutSmall


	// ################################################################

//	public void setButtonText(int buttonId, String text) {
//
//		/**
//		 * Shortcut for changing the text on a button
//		 */
//
//		Button button = (Button) v.findViewById(buttonId);
//		button.setText(text);
//
//	} // setButtonText


	// ################################################################

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

//		updatePowerThresholds();
//		//		updatePower();

	} // onProgressChanged


	// ################################################################

	public void onStartTrackingTouch(SeekBar seekBar) {

		/**
		 * Method required by SeekBar.OnSeekBarChangeListener
		 */


	} // onStartTrackingTouch


	// ################################################################

	public void onStopTrackingTouch(SeekBar seekBar) {

		Log.v(TAG, "onStopTrackingTouch()");


	} // onStopTrackingTouch


	// ################################################################



	// ################################################################

	public void updateServoPosition() {


		if (eegPower > 0)
			bloomServoPercentage = bloomServoPercentage + 3;
		else
			bloomServoPercentage = bloomServoPercentage - 1;

		if (bloomServoPercentage > 100)
			bloomServoPercentage = 100;

		if (bloomServoPercentage < 0)
			bloomServoPercentage = 0;


//		progressBarBloom.setProgress(bloomServoPercentage);

		servoSeekBar.setProgress(bloomServoPercentage);


//		if (characteristicTx != null) {
//
//			byte[] buf = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x00};
//
////		buf[1] = (byte) servoSeekBar.getProgress();
//			buf[1] = (byte) bloomServoPercentage;
//
//			characteristicTx.setValue(buf);
//			mBluetoothLeService.writeCharacteristic(characteristicTx);
//
//		}


	}


	// ################################################################

	public void updateBloomRGB() {

		bloomColorRed = redSeekBar.getProgress();
		bloomColorGreen = greenSeekBar.getProgress();
		bloomColorBlue = blueSeekBar.getProgress();


		if (BloomSingleton.getInstance().characteristicTx != null) {

			byte[] buf = new byte[]{(byte) 0x0A, (byte) 0x00, (byte) bloomColorRed};

			BloomSingleton.getInstance().characteristicTx.setValue(buf);
			BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

		}

		if (BloomSingleton.getInstance().characteristicTx != null) {

			byte[] buf = new byte[]{(byte) 0x0A, (byte) 0x01, (byte) bloomColorGreen};

			BloomSingleton.getInstance().characteristicTx.setValue(buf);
			BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

		}

		if (BloomSingleton.getInstance().characteristicTx != null) {

			byte[] buf = new byte[]{(byte) 0x0A, (byte) 0x02, (byte) bloomColorBlue};

			BloomSingleton.getInstance().characteristicTx.setValue(buf);
			BloomSingleton.getInstance().mBluetoothLeService.writeCharacteristic(BloomSingleton.getInstance().characteristicTx);

		}

	}


	// ################################################################

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
		                               IBinder service) {
			BloomSingleton.getInstance().mBluetoothLeService = ((RBLService.LocalBinder) service)
					  .getService();
			if (!BloomSingleton.getInstance().mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				getActivity().finish();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			BloomSingleton.getInstance().mBluetoothLeService = null;
		}
	};


	// ################################################################

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (RBLService.ACTION_GATT_DISCONNECTED.equals(action)) {
//				Toast.makeText(getApplicationContext(), "Disconnected",
				Toast.makeText(getActivity(), "Bloom Disconnected",
						  Toast.LENGTH_SHORT).show();
				setButtonDisable();
			} else if (RBLService.ACTION_GATT_SERVICES_DISCOVERED
					  .equals(action)) {
				Toast.makeText(getActivity(), "Bloom Connected",
						  Toast.LENGTH_SHORT).show();

				getGattService(BloomSingleton.getInstance().mBluetoothLeService.getSupportedGattService());
			} else if (RBLService.ACTION_DATA_AVAILABLE.equals(action)) {
				BloomSingleton.getInstance().data = intent.getByteArrayExtra(RBLService.EXTRA_DATA);

//				readAnalogInValue(data);
			} else if (RBLService.ACTION_GATT_RSSI.equals(action)) {
				displayData(intent.getStringExtra(RBLService.EXTRA_DATA));
			}
		}
	};


}
