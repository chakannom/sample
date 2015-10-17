package kr.co.gtson.example.webview;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {


	/** ↓↓ WebView에서 처음 한 번만 호출되는 메쏘드 ↓↓
	 * 페이지 로딩이 시작된 것을 알립니다. 이 메쏘드가 각각의 main frame이 iframe에 
	 * 페이지를 로드하기 위해 한번 호출되거나  frameset이 main frame에 대해 이 메쏘드를 
	 * 한번 호출할 것 입니다. 이 메쏘드가 임베디드 프레임 내용이 변경되었을 때 호출되지
	 * 않는다는 것도 뜻합니다. 예를 들면, iframe이 있는 대상 링크를 클릭한 것 입니다.
	 */
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);

	}
	/**
	 * WebView가 주어진 URL로 지정된 리소스를 로드할 것이라고 알립니다.
	 * 페이지 로딩이 완료될 때까지 여러번 호출됩니다. 페이지가 나뉘어서 로딩되나 봅니다.
	 */
	@Override 
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
	}



	/**
	 * 방문한 링크를 데이터베이스에 업데이트한다고 알립니다.
	 */
	@Override
	public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) { 
		Log.i("WebView", "History: " + url );
		super.doUpdateVisitedHistory(view, url, isReload);
	}

	/* 결과 (url이 변할 때 마다)
	 * History: http://www.abc.com/djfk...
	 * History: http://www.abc.com/djfk.../fdfd
	 */

	/** ↓↓ WebView에서 처음 한 번만 호출되는 메쏘드 ↓↓
	 * 페이지 로딩이 완료된 것을 알립니다. 이 메쏘드는 메인 프레임에 대해서만 호출됩니다.
	 * 이 메쏘드가 호출되었을 때, picture rendering은 아직 업데이트되지 않을 수 있습니다.
	 * 새로운 Picture가 있다는 사실을 알기위해, onNewPicture(WebView, Picture) 메쏘드를
	 * 사용합니다.
	 */
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
	}



	/**
	 * * As the host application if the browser should resend data as the requested page
	 * was a result of a POST. 기본적으론 데이터를 재발송하지 않는 것입니다.
	 */
	@Override
	public void onFormResubmission(WebView view, Message dontResend, Message resend) {
		super.onFormResubmission(view, dontResend, resend);
	} 

	/**
	 * 호스트 응용 프로그램에게 오류를 보고합니다. 이러한 오류는 복구할 수 없습니다.
	 * (예, main resource를 사용할 수 없는 상태) errorCode 매개 변수는 
	 * WebViewClient.ERROR_* 상수 중 하나에 해당합니다.
	 */
	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		switch(errorCode) {
		case ERROR_AUTHENTICATION: break;               // 서버에서 사용자 인증 실패
		case ERROR_BAD_URL: break;                           // 잘못된 URL
		case ERROR_CONNECT: break;                          // 서버로 연결 실패
		case ERROR_FAILED_SSL_HANDSHAKE: break;    // SSL handshake 수행 실패
		case ERROR_FILE: break;                                  // 일반 파일 오류
		case ERROR_FILE_NOT_FOUND: break;               // 파일을 찾을 수 없습니다
		case ERROR_HOST_LOOKUP: break;           // 서버 또는 프록시 호스트 이름 조회 실패
		case ERROR_IO: break;                              // 서버에서 읽거나 서버로 쓰기 실패
		case ERROR_PROXY_AUTHENTICATION: break;   // 프록시에서 사용자 인증 실패
		case ERROR_REDIRECT_LOOP: break;               // 너무 많은 리디렉션
		case ERROR_TIMEOUT: break;                          // 연결 시간 초과
		case ERROR_TOO_MANY_REQUESTS: break;     // 페이지 로드중 너무 많은 요청 발생
		case ERROR_UNKNOWN: break;                        // 일반 오류
		case ERROR_UNSUPPORTED_AUTH_SCHEME: break; // 지원되지 않는 인증 체계
		case ERROR_UNSUPPORTED_SCHEME: break;          // URI가 지원되지 않는 방식
		}

	}

	/**
	 * 인증 요청을 처리한다고 알립니다. 기본 동작은 요청을 취소하는 것입니다.
	 */
	@Override
	public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
		super.onReceivedHttpAuthRequest(view, handler, host, realm);

	}

	/**
	 * WebView가 변화하기위해 scale이 적용된다고 알립니다.
	 */
	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {
		super.onScaleChanged(view, oldScale, newScale);
	}

	/**
	 * 이 메쏘드는 사용이 중지되었습니다. 이 메쏘드는 더이상 호출되지 않습니다. 
	 * WebView가 재시작된 루프를 발견하면, 로드가 취소됩니다
	 */
	@Override
	public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
		super.onTooManyRedirects(view, cancelMsg, continueMsg);
	}

	/** ↓↓ 잘못된 키 입력이 있을 경우 호출되는 메쏘드 ↓↓
	 * 키가 WebView에 의해 처리되지 않았음을 호스트 응용 프로그램에게 알림.
	 * 시스템 키를 제외하고, WebView는 shouldOverrideKeyEvent가 true를 반환하는 경우나
	 * 일반적인 flow에서 항상 키 이벤트를 처리합니다. 키 이벤트가 발생된 곳으로 부터 
	 * 비동기적으로 호출됩니다. 호스트 응용 프로그램에게 처리되지 않은 키 이벤트를 처리할 
	 * 기회를 제공합니다.
	 */
	@Override
	public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
		super.onUnhandledKeyEvent(view, event);
	}

	/** ↓↓ 잘못된 키 입력이 있을 경우 호출되는 메쏘드 ↓↓
	 * 호스트 응용 프로그램에게 동기적으로 키 이벤트를 처리할 기회를 줍니다. 예: 메뉴 바로 
	 * 가기 키 이벤트를 이런식으로 필터링해야합니다. true를 반환할 경우, WebView는 키 이벤트를
	 * 처리하지 않습니다. false를 반환할 경우, WebView 항상 키 이벤트를 처리합니다. So none
	 * of the super in the view chain will see the key event. 기본 동작은 false를 반환합니다.
	 */
	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		return super.shouldOverrideKeyEvent(view, event);
	}

	/**
	 * 새로운 URL이 현재 WebView에 로드되려고 할 때 호스트 응용 프로그램에게 컨트롤을 
	 * 대신할 기회를 줍니다. WebViewClient가 제공되지 않으면, 기본적으로 WebView는 URL에
	 * 대한 적절한 핸들러를 선택하려고 Activity Manager에게 물어봅니다. WebViewClient가 
	 * 제공되면, 호스트 응용 프로그램이 URL을 처리한다는 의미인 true를 반환거나 현재 
	 * WebView가 URL을 처리한다는 의미인 false를 반환합니다.
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		return super.shouldOverrideUrlLoading(view, url);

	}




}
