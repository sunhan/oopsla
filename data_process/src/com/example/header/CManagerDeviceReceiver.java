package com.example.header;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import com.example.data_process.MainActivity;

import android.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class CManagerDeviceReceiver {

	// 퍼미션을 위한 변수들
	private final Context mApplicationContext;
	private final UsbManager mUsbManager;
	protected static final String ACTION_USB_PERMISSION = "ch.serverbox.android.USB";

	private final int ID_SIZE = 5;
	private final int RECEIVE_MODE = 1;
	private final int COMMAND_SIZE = 3;
	private final int PACKET_SIZE = 18;
	private final char ID_ADDRESS = 0x00; //송신기 아이디
	private final char ID_ADDRESS1 = 0x01;
	private final char ID_ADDRESS2 = 0x02;
	private final char ID_ADDRESS3 = 0x03;
	
	private final int VID = 0x10c4;
	private final int PID = 0xea61;
	// private final int ID_ADDRESS_1 = 1158001;
	private UsbDevice device;
	private char[] ucWriteBuffer;

	private HCDATA m_sHC; // 수신 데이터 구조

	// 패킷 전송을 위해 필요한 변수들
	private UsbDeviceConnection conn;
	private UsbEndpoint epIN;
	private UsbEndpoint epOUT;
	
	private SendMassgeHandler mMainHandler = null;

	protected CUCareReceiveBufferList m_cReceiveBufferList;

	// 디비로부터 디바이스 정보를 가져올 수 있도록 메소드 구성.

	// 경보 쓰레드

	// 저장할 수 있스레드

	// 움직임 판단 스레드

	/*
	 * 연결된 usb 장치 초기화.
	 */

	public CManagerDeviceReceiver(Activity parentActivity) {
		mApplicationContext = parentActivity.getApplicationContext();
		mUsbManager = (UsbManager) mApplicationContext
				.getSystemService(Context.USB_SERVICE);
		ucWriteBuffer = new char[80];
		mMainHandler = new SendMassgeHandler();
		
		this.epIN = null;
		this.epOUT = null;

		init();
	}

	private void init() {
		enumerate(new IPermissionListener() {
			@Override
			public void onPermissionDenied(UsbDevice d) {
				// 권한 등록
				UsbManager usbman = (UsbManager) mApplicationContext
						.getSystemService(Context.USB_SERVICE);
				PendingIntent pi = PendingIntent.getBroadcast(
						mApplicationContext, 0, new Intent(
								ACTION_USB_PERMISSION), 0);
				mApplicationContext.registerReceiver(mPermissionReceiver,
						new IntentFilter(ACTION_USB_PERMISSION));
				usbman.requestPermission(d, pi);
			}
		});
	}

	private void enumerate(IPermissionListener listener) {
		Log.e("enumerating", "enumerating : 시작");
		HashMap<String, UsbDevice> devlist = mUsbManager.getDeviceList();
		Iterator<UsbDevice> deviter = devlist.values().iterator();
		while (deviter.hasNext()) {
			device = deviter.next();
			if (device.getVendorId() == VID && device.getProductId() == PID) {
				// 장비 연결 하고 쓰레드.
				Log.e("enumerating", "usb deviceID : " + device.getDeviceId());
				if (!mUsbManager.hasPermission(device)) {
					// 권한이 없을 경우 호출
					listener.onPermissionDenied(device);
				} else {
					// 권한이 있었을 경우 스레드 실행 , 권한 허용된 상태에서 재시작시에 이쪽으로 들어감
					Log.e("enumerating", "usb 접근 권한 획득2");
					startHandler();
				}
			}
		}
	}

	private BroadcastReceiver mPermissionReceiver = new PermissionReceiver(
			new IPermissionListener() {
				@Override
				public void onPermissionDenied(UsbDevice d) {
					Log.e("BroadcastReceiver Permission denied on",
							"usb deviceID : " + d.getDeviceId());
				}
			});

	private static interface IPermissionListener {
		void onPermissionDenied(UsbDevice d);
	}

	private class PermissionReceiver extends BroadcastReceiver {
		private final IPermissionListener mPermissionListener;

		public PermissionReceiver(IPermissionListener permissionListener) {
			mPermissionListener = permissionListener;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			mApplicationContext.unregisterReceiver(this);
			if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
				if (!intent.getBooleanExtra(
						UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
					mPermissionListener.onPermissionDenied((UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE));
				} else {
					Log.e("PermissionReceiver", "Permission granted");
					UsbDevice dev = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (dev != null) {
						if (dev.getVendorId() == VID
								&& dev.getProductId() == PID) {
							// 쓰레드.
							Log.e("PermissionReceiver", "브로드 캐스트 리시버 여기서 부터");
							startHandler();// 권한을 요구하는 브로드 캐스트 리시버 호출 되면서 쓰레드
											// 시작함
						}
					} else {
						Log.e("PermissionReceiver", "device not present!");
					}
				}
			}
		}
	}
	// Handler 클래스
		class SendMassgeHandler extends Handler {

			@Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	             
	            switch (msg.what) {
	            case 0:
	            	MainActivity.t1.setText("WRITE: "+msg.arg1);	
	            	MainActivity.t2.setText(("WRITE: "+msg.arg2));
	            	MainActivity.t3.setText((CharSequence) msg.obj);
	            default:
	                break;
	            }
	        }
		};

	// 디바이스 디비로 부터 가져오기
	// 데이터 요청 패킷 설정
	// 데이터 요청 패킷 전송
	// 상태 패킷 수신
	// 상태 분석
	// 데이터 수신 스레드 루프. -> 버퍼에 저장 심전도 데이터 수신해서 버퍼에 저장
	// 그래프를 그림.//thread 설정 및 시작.

	private UsbRunnable write_run;
	private Thread mUsbThread;
	private ObjectOutputStream os;

	private void startHandler() {
		// 엔드포인트 입출력 포인트를 초기화함
		endpoint_in_out();

		write_run = new UsbRunnable();
		mUsbThread = new Thread(write_run);
		mUsbThread.start();
	}

	/*
	 * usb로부터 endpoint_in_out
	 */
	public void endpoint_in_out() {
		try {
			// TODO Auto-generated method stub
			conn = mUsbManager.openDevice(device);
			if (!conn.claimInterface(device.getInterface(0), true)) {
				Log.e("endpoint_in_out", "claimInterface error");
			}

			/*
			 * conn.controlTransfer(0x21, 34, 0, 0, null, 0, 0);
			 * conn.controlTransfer(0x21, 32, 0, 0, new byte[] { (byte) 0x80,
			 * 0x25, 0x00, 0x00, 0x00, 0x00, 0x08 }, 7, 0);
			 */

			UsbInterface usbIf = device.getInterface(0);
			Log.e("endpoint_in_out",
					"EndpointCount = " + usbIf.getEndpointCount());
			for (int i = 0; i < usbIf.getEndpointCount(); i++) {
				if (usbIf.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
					if (usbIf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN) {
						epIN = usbIf.getEndpoint(i);
						Log.e("endpoint_in_out","epIN address : "+ epIN.getAddress());
						
						Log.e("endpoint_in_out", "epIN index : " + i);
					} else {
						epOUT = usbIf.getEndpoint(i);
						Log.e("endpoint_in_out","epOUT address : "+ epOUT.getAddress());
						Log.e("endpoint_in_out", "epIN index : " + i);
					}
				}
			}
		} catch (Exception e) {
			Log.e("endpoint_in_out", "endpoint_in_out: " + e.getMessage());
		}
	}

	/*
	 * 데이터 용청을 위한 패킷 전송
	 */
	class UsbRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			byte[] byteBuffer;
			int write1 = 0, write2 = 0, write3 = 0;
			
			// 데이터 전송을 위한 설정.
			ucWriteBuffer[0] = 'y';
			ucWriteBuffer[1] = 0xff; // ?
			ucWriteBuffer[2] = 0xff; // ?

			try{
				byteBuffer = new String(ucWriteBuffer).getBytes();
	
				int writec = conn.bulkTransfer(epOUT, byteBuffer,
						COMMAND_SIZE, 1000);
				Log.e("Run ???????????", "Write Data Bytes : " + writec);
			}
			catch(Exception e){
				Log.e("1. Runnable Set ID Size",
						" exception error : " + e.getMessage());
			}
			
			
			// 1. Set ID Size
			ucWriteBuffer[0] = 'I';
			ucWriteBuffer[1] = RECEIVE_MODE;
			ucWriteBuffer[2] = ID_SIZE;
				
			Log.e("1. Runnable Set ID Size", "시작");
			Log.e("1. Runnable Set ID Size", "ucWriteBuffer[0] : "
					+ ucWriteBuffer[0]);
			Log.e("1. Runnable Set ID Size", "ucWriteBuffer[1] : "
					+ ucWriteBuffer[1]);
			Log.e("1. Runnable Set ID Size", "ucWriteBuffer[2] : "
					+ ucWriteBuffer[2]);

			try {
				byteBuffer = new String(ucWriteBuffer).getBytes();
				write1 = conn.bulkTransfer(epOUT, byteBuffer,
					COMMAND_SIZE, 1000);

				//Log.e("1. Runnable Set ID Size", "Write Data Bytes : " + writec);
			} catch (Exception e) {
				Log.e("1. Runnable Set ID Size",
						" exception error : " + e.getMessage());
			}

			Log.e("1. Runnable Set ID Size", "끝");

			// 2. Set ID Address
			ucWriteBuffer[0] = 'I';
			ucWriteBuffer[1] = ID_ADDRESS; //
			ucWriteBuffer[2] = ID_ADDRESS1;
			ucWriteBuffer[3] = ID_ADDRESS2;
			ucWriteBuffer[4] = ID_ADDRESS3;
			
			Log.e("2. Runnable Set ID Address", "시작");
			Log.e("2. Runnable Set ID Address", "ucWriteBuffer[0] : "
					+ ucWriteBuffer[0]);
			Log.e("2. Runnable Set ID Address", "ucWriteBuffer[1] : "
					+ ucWriteBuffer[1]);
			Log.e("2. Runnable Set ID Address", "ucWriteBuffer[2] : "
					+ ucWriteBuffer[2]);						

			try {
				byteBuffer = new String(ucWriteBuffer).getBytes();

				write2 = conn.bulkTransfer(epOUT, byteBuffer,
						COMMAND_SIZE, 1000);
	
				
			//	Log.e("2. Runnable Set ID Address", "Write Data Bytes : "
			//			+ write1);
			} catch (Exception e) {
				Log.e("2. Runnable Set ID Address",
						"exception error : " + e.getMessage());
			}
			Log.e("2. Runnable Set ID Address", "끝");

			// 3. Run with Channel
			ucWriteBuffer[0] = 'R';
			ucWriteBuffer[1] = 20; // 1ch
			ucWriteBuffer[2] = PACKET_SIZE;
			
			Log.e("3. Runnable Run with Channel", "시작");
			Log.e("3. Runnable Run with Channel", "ucWriteBuffer[0] : "
					+ ucWriteBuffer[0]);
			Log.e("3. Runnable Run with Channel", "ucWriteBuffer[1] : "
					+ ucWriteBuffer[1]);
			Log.e("3. Runnable Run with Channel", "ucWriteBuffer[2] : "
					+ ucWriteBuffer[2]);

			try {
				byteBuffer = new String(ucWriteBuffer).getBytes();

				write3 = conn.bulkTransfer(epOUT, byteBuffer,
						COMMAND_SIZE, 500);
			//	Log.e("3. Runnable Run with Channel", "Write Data Bytes : "
			//			+ writec);
			} catch (Exception e) {
				Log.e("3. Runnable Run with Channel",
						"exception error : " + e.getMessage());
			}
			Log.e("3. Runnable Run with Channel", "끝");

			// 데이터를 가져옴

			/*
			 * //데이터를 가져옴 UsbRead reader = new UsbRead(); Thread tr = new
			 * Thread(reader); tr.start();
			 */
			
			Message msg = mMainHandler.obtainMessage();
			msg.what = 0;
			msg.arg1 = write1;
			msg.arg2 = write2;
			
			msg.obj =write1+","+write2+","+write3;
			
			mMainHandler.sendMessage(msg);
		}
	}

	public void mByteOrder(HCDATA m_sHC) {

		// tByteOrder;
	}
}

class HCDATA {
	int ltime;
	byte mode;
	byte stat;
	short ecg0; // ECG0 12Bit Snap Button electrode
	short ecg1; // ECG1 12Bit External electrode
	short adx; // 10Bit 333mV = 1g REFV = 3.0V
	short ady; // 10Bit 333mV = 1g REFV = 3.0V
	short adz; // 10Bit 333mV = 1g REFV = 3.0V
	short tmp; // 섭씨온도*10 25.2 = 252
}
