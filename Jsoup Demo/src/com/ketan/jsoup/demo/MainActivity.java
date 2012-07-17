package com.ketan.jsoup.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String _AUTHPARAMS = "GoogleLogin auth=";
	private static final String _GOOGLE_LOGIN_URL = "https://www.google.com/accounts/ClientLogin";
	private static final String _READER_BASE_URL = "http://www.google.com/reader/";
	private static final String _API_URL = _READER_BASE_URL + "api/0/";
	private static final String _TOKEN_URL = _API_URL + "token";
	private static final String _USER_INFO_URL = _API_URL + "user-info";
	private static final String _USER_LABEL = "user/-/label/";
	private static final String _TAG_LIST_URL = _API_URL + "tag/list";
	private static final String _EDIT_TAG_URL = _API_URL + "tag/edit";
	private static final String _RENAME_TAG_URL = _API_URL + "rename-tag";
	private static final String _DISABLE_TAG_URL = _API_URL + "disable-tag";
	private static final String _SUBSCRIPTION_URL = _API_URL
			+ "subscription/edit";
	private static final String _SUBSCRIPTION_LIST_URL = _API_URL
			+ "subscription/list";
	private WindowManager windowManager;
	private ImageView google;
	private Button login;
	private EditText e_mail, pass;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		google = (ImageView) findViewById(R.id.image);
		login = (Button) findViewById(R.id.login);
		e_mail = (EditText) findViewById(R.id.e_mail_id);
		pass = (EditText) findViewById(R.id.pass);

		windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		int width = windowManager.getDefaultDisplay().getWidth();
		int height = windowManager.getDefaultDisplay().getHeight();
		Bitmap bm;
		bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.google_android), width, height / 3,
				true);
		google.setImageBitmap(bm);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				try {
					String out = getGoogleAuthKey(e_mail.getText().toString(),
							pass.getText().toString());
					Toast.makeText(MainActivity.this, out + " key ",
							Toast.LENGTH_SHORT).show();
					
//					System.out.println(getGoogleAuthKey(e_mail.getText().toString(),
//							pass.getText().toString()));
//					
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			
//				 
//				// Get Token
//				System.out.println(Func_JSOUP.getGoogleToken(_USERNAME, _PASSWORD));
//				 
//				// Get UserID
//				System.out.println(Func_JSOUP.getGoogleUserID(_USERNAME, _PASSWORD));
//				 
//				// Get Reader TAG TITLES
//				String[] _TAG_TITLES = Func_JSOUP.getTagList(_USERNAME, _PASSWORD);
//				for(int i = 0; i &lt; _TAG_TITLES.length; i++){
//				System.out.println(_TAG_TITLES[i]);
//				}
//				 
//				// Get Reader SUBSCRIPTION TITLES
//				String[] _SUB_TITLES = Func_JSOUP.getSubList(_USERNAME, _PASSWORD);
//				for(int i = 0; i &lt; _SUB_TITLES.length; i++){
//				System.out.println(_SUB_TITLES[i]);
//				}

			}
		});

	}

	/**
	 * Returns a Google Authentication Key Requires a Google Username and
	 * Password to be sent in the POST headers to
	 * http://www.google.com/accounts/ClientLogin
	 * 
	 * @param GoogleGoogle_Username
	 *            Google Username
	 * @param Google_Password
	 *            Google Password
	 * @return Google authorisation token
	 * @see getGoogleToken
	 */

	// Getting a Google Authentication Key Ð Returns an authentication key as a
	// string
	public static String getGoogleAuthKey(String _USERNAME, String _PASSWORD)
			throws UnsupportedEncodingException, IOException {
		Log.v("get user name and pass", _USERNAME + "  " + _PASSWORD);

		Document doc = Jsoup
				.connect(_GOOGLE_LOGIN_URL)
				.data("accountType", "GOOGLE", "Email", _USERNAME, "Passwd",
						_PASSWORD, "service", "reader", "source",
						"&lt;your app name&gt;")
				.userAgent("&lt;your app name&gt;").timeout(4000).post();

		// RETRIEVES THE RESPONSE TEXT inc SID and AUTH. We only want the AUTH
		// key.
		String _AUTHKEY = doc
				.body()
				.text()
				.substring(doc.body().text().indexOf("Auth="),
						doc.body().text().length());
		_AUTHKEY = _AUTHKEY.replace("Auth=", "");
		Log.v("Auth key ", _AUTHKEY);
		return _AUTHKEY;
	}

	// Getting a Google Reader Token Ð Returns a Google token as a string
	public static String getGoogleToken(String _USERNAME, String _PASSWORD)
			throws UnsupportedEncodingException, IOException {
		Document doc = Jsoup
				.connect(_TOKEN_URL)
				.header("Authorization",
						_AUTHPARAMS + getGoogleAuthKey(_USERNAME, _PASSWORD))
				.userAgent("&lt;your app name").timeout(4000).get();

		// RETRIEVES THE RESPONSE TOKEN
		String _TOKEN = doc.body().text();
		return _TOKEN;
	}

	// Retrieving Google Reader User Info
	public static String getUserInfo(String _USERNAME, String _PASSWORD)
			throws UnsupportedEncodingException, IOException {
		Document doc = Jsoup
				.connect(_USER_INFO_URL)
				.header("Authorization",
						_AUTHPARAMS + getGoogleAuthKey(_USERNAME, _PASSWORD))
				.userAgent("&lt;your app name&gt;").timeout(4000).get();

		// RETRIEVES THE RESPONSE USERINFO
		String _USERINFO = doc.body().text();
		return _USERINFO;
	}

	// Retrieving Google Reader User ID
	public static String getGoogleUserID(String _USERNAME, String _PASSWORD)
			throws UnsupportedEncodingException, IOException {
		/*
		 * USERINFO RETURNED LOOKS LIKE {"userId":"14577161871823252783",
		 * "userName"
		 * :"&lt;username&gt;","userProfileId":"&lt;21 numeric numbers",
		 * "userEmail":"&lt;username&gt;@gmail.com", "isBloggerUser":true,
		 * "signupTimeSec":1159535065}
		 */
		String _USERINFO = getUserInfo(_USERNAME, _PASSWORD);
		String _USERID = (String) _USERINFO.subSequence(11, 31);
		return _USERID;
	}

	
}
