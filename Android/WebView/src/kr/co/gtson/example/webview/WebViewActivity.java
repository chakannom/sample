package kr.co.gtson.example.webview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
	/** Called when the activity is first created. */

	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.webView = (WebView) this.findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true); // javascript를 실행할 수 있도록 설정
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // javascript가 window.open()을 사용할 수 있도록 설정
		webSettings.setSupportZoom(true); // 확대,축소 기능을 사용할 수 있도록 설정
		webSettings.setBuiltInZoomControls(true); // 안드로이드에서 제공하는 줌 아이콘을 사용할 수 있도록 설정
		webSettings.setLoadsImagesAutomatically(true); // 웹뷰가 앱에 등록되어 있는 이미지 리소스를 자동으로 로드하도록 설정
		webSettings.setUseWideViewPort(false); // wide viewport를 사용하도록 설정
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 웹뷰가 캐시를 사용하지 않도록 설정
		webSettings.setDefaultTextEncodingName("UTF-8");
		webSettings.setSaveFormData(false);

		webView.setWebViewClient(new CustomWebViewClient());
		webView.setWebChromeClient(new CustomeWebChromeClient());

		webView.loadUrl("http://gts.gtson.co.kr");
	}
}