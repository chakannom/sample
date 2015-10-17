package kr.co.gtson.example.customgalleryviewremoteimage.common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Utils {

	/**
	 * Get URLConnection(HTTP or HTTPS Protocol)
	 * @param location
	 * @return
	 * @throws MalformedURLException
	 * @throws SocketTimeoutException
	 * @throws IOException
	 */
	public static HttpURLConnection getURLConnection(String location) throws MalformedURLException, SocketTimeoutException, SocketException, IOException {
		URL url = new URL(location);
		HttpURLConnection urlConn = null;
		if (url.getProtocol().toUpperCase().equals("HTTPS")) {
			trustAllHosts();
			HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url.openConnection();
			httpsUrlConn.setHostnameVerifier(hostnameVerifier);
			urlConn = httpsUrlConn;
		} else {
			urlConn = (HttpURLConnection) url.openConnection();
		}
		urlConn.setConnectTimeout(CommonConstants.HTTP_CONNECTION_TIMEOUT);
		urlConn.setReadTimeout(CommonConstants.HTTP_READ_TIMEOUT);
		urlConn.connect();
		return urlConn;
	}

	/**
	 * Trust SSL's all hosts
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains 
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() { 
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException { 
			}
 
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException { 
			}
		}};

		// Install the all-trusting trust manager 
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
}
