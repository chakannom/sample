package kr.co.gtson.example.webview;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;

public class CustomeWebChromeClient extends WebChromeClient {

	/**
	 * 페이지를 로딩하는 현재 진행 상황을 전해줍니다.
	 * newProgress  현재 페이지 로딩 진행 상황, 0과 100 사이의 정수로 표현.(0% ~ 100%)
	 */
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		Log.i("WebView", "Progress: " + String.valueOf(newProgress)); 
		super.onProgressChanged(view, newProgress);
	}

	/* 결과 : 
	Progress: 10
	Progress: 15
	Progress: 35
	...
	Progress: 76
	Progress: 100
	 */

	/**
	 * 현재 페이지에 새로운 favicon이 있다고 알립니다.
	 * icon 현재 페이지의 favicon이 들어있는 비트맵  
	 */
	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		super.onReceivedIcon(view, icon);
	}

	/* 
	favicon이란:
	일반적으로 웹 브라우저의 URL이 표시되기 전에 특정 웹사이트와 관련된 16 × 16 픽셀 아이콘 
	 */

	/**
	 * 문서 제목에 변경이 있다고 알립니다.
	 * title  문서의 새로운 타이틀이 들어있는 문자열  
	 */
	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
	}

	/*  아래처럼 title 태그 사이의 값을 가져옵니다.
	<title> 제목 </title>
	 */
	@Override
	public Bitmap getDefaultVideoPoster() {
		return super.getDefaultVideoPoster();
	}

	@Override
	public View getVideoLoadingProgressView() {
		return super.getVideoLoadingProgressView();
	}

	@Override

	public void getVisitedHistory(ValueCallback<String[]> callback) {
		super.getVisitedHistory(callback);
	}

	@Override
	public void onCloseWindow(WebView window) {
		super.onCloseWindow(window);

	}

	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		super.onConsoleMessage(message, lineNumber, sourceID);
	}

	@Override
	public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
		return super.onCreateWindow(view, dialog, userGesture, resultMsg);
	}

	@Override
	public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater) {
		super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota, estimatedSize, totalUsedQuota, quotaUpdater);
	}

	@Override
	public void onGeolocationPermissionsHidePrompt() {
		super.onGeolocationPermissionsHidePrompt();
	}

	@Override
	public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
		super.onGeolocationPermissionsShowPrompt(origin, callback);
	}

	@Override
	public void onHideCustomView() {
		super.onHideCustomView();
	}

	/**
	 * 자바 스크립트 경고 대화 상자를 디스플레이한다고 클라이언트에게 알려줍니다. 
	 * 클라이언트가 true를 반환할 경우, WebView는 클라이언트가 대화 상자를 처리할 수 있다고 
	 * 여깁니다. 클라이언트가 false를 반환할 경우, WebView는 실행을 계속합니다.
	 */
	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		return super.onJsAlert(view, url, message, result);
	}
	/**
	 * 현재 페이지에서 나와 탐색을 확정하는 대화 상자를 디스플레이한다고 클라이언트에게 
	 * 알려줍니다. 이것은 자바 스크립트 이벤트 onbeforeunload()의 결과입니다. 클라이언트가 
	 * true를 반환하는 경우, WebView는 클라이언트가 대화 상자를 처리하고 적절한 JsResult 
	 * 메쏘드를 호출할 것이라고 여깁니다. 클라이언트가 false를 반환하는 경우, true의 기본값은 
	 * 현재 페이지에서 나와 탐색하기를 수락하기 위한 자바 스크립트를 반환하게 될 것입니다. 
	 * 기본 동작은 false를 반환하는 것입니다. JsResult를 true로 설정한 것은 현재 페이지에서 나와 
	 * 탐색할 것이고 false로 설정한 것은 탐색을 취소할 것입니다.
	 */
	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
		return super.onJsBeforeUnload(view, url, message, result);
	}

	/**
	 * 사용자에게 확인 대화 상자를 디스플레이한다고 클라이언트에게 알려줍니다. 클라이언트가 
	 * true를 반환하는 경우, WebView는 클라이언트가 확인 대화 상자를 처리하고 적절한 
	 * JsResult 메쏘드를 호출할 수 있다고 여깁니다. 클라이언트가 false를 반환하는 경우 false의 
	 * 기본값은 자바 스크립트로 반환될 것 입니다. 기본 동작은 false를 반환하는 것입니다.
	 */
	@Override
	public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
		return super.onJsConfirm(view, url, message, result);
	}

	/**
	 * 사용자에게 프롬프트 대화 상자를 디스플레이한다고 클라이언트에게 알려줍니다. 
	 * 클라이언트가 true를 반환하는 경우, WebView는 클라이언트가 확인 대화 상자를 처리하고 
	 * 적절한 JsResult 메쏘드를 호출할 수 있다고 여깁니다. 클라이언트가 false를 반환하는 경우
	 * false의 기본값은 자바 스크립트로 반환될 것 입니다. 기본 동작은 false를 반환하는 것입니다.
	 */
	@Override
	public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
		return super.onJsPrompt(view, url, message, defaultValue, result);
	}

	/**
	 * 자바 스크립트 실행 제한 시간을 초과했다고 클라이언트에게 알려줍니다. 그리고 
	 * 클라이언트가 실행을 중단할지 여부를 결정할 수 있습니다. 클라이언트가 true를 반환하는
	 * 경우, 자바 스크립트가 중단됩니다. 클라이언트가 false를 반환하는 경우, 계속 실행됩니다. 
	 * 참고로 지속적인 실행 상태에서는 제한 시간 카운터가 재설정되고  스크립트가 다음 체크 
	 * 포인트에서 완료되지 않을 경우 계속 콜백되어질 것집니다.
	 */
	@Override
	public boolean onJsTimeout() {
		return super.onJsTimeout();
	}

	@Override
	public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater) {
		super.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
	}

	@Override
	public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
		super.onReceivedTouchIconUrl(view, url, precomposed);
	}

	@Override
	public void onRequestFocus(WebView view) {
		super.onRequestFocus(view);
	}

	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		super.onShowCustomView(view, callback);
	}
}