package org.kokoa.espolra;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WView extends Activity{
	String url="http://kokoa.espol.edu.ec:9090/map";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.webview);
 
        loadMap();
 
    }
	

	@SuppressLint("NewApi")
	public void loadMap(){
		final WebView webView = (WebView) findViewById(R.id.webView);
		
		/*DialogInterface.OnCancelListener dialogCancel = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Toast.makeText(getActivity().getBaseContext(),
						"No hay datos en la fuente de informacion.",
						Toast.LENGTH_LONG).show();
			}
		};*/
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new GeoWebViewClient());
		       // Below required for geolocation
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.setWebChromeClient(new GeoWebChromeClient());

		final ProgressDialog pd = ProgressDialog.show(WView.this, "Cargando Ubicacion ...",
				"Gracias por su espera", true, true);
				

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				pd.dismiss();
				webView.loadUrl("javascript:(function() { "
						+ "document.getElementsByTagName('header')[0].style.display='none';document.getElementsByTagName('anio')[0].style.display='none'; "
						+ "})()");
			}
		});

		webView.loadUrl(url);
	}
	
	/**
	 * WebViewClient subclass loads all hyperlinks in the existing WebView
	 */
	public class GeoWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        // When user clicks a hyperlink, load in the existing WebView
	        view.loadUrl(url);
	        return true;
	    }
	}

	/**
	 * WebChromeClient subclass handles UI-related calls
	 * Note: think chrome as in decoration, not the Chrome browser
	 */
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public class GeoWebChromeClient extends WebChromeClient {
	    @Override
	    public void onGeolocationPermissionsShowPrompt(String origin,
	            GeolocationPermissions.Callback callback) {
	        // Always grant permission since the app itself requires location
	        // permission and the user has therefore already granted it
	        callback.invoke(origin, true, false);
	    }
	}
}
/*	
public void loadMap(){
		
		url = "http://104.131.65.227:9000/posicion";
		
		webView = (WebView) rootView.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new GeoWebViewClient());
		       // Below required for geolocation
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.setWebChromeClient(new GeoWebChromeClient());
		
		
		DialogInterface.OnCancelListener dialogCancel = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Toast.makeText(getActivity().getBaseContext(),
						"No hay datos en la fuente de informacion.",
						Toast.LENGTH_LONG).show();
			}
		};
		
		pd = ProgressDialog.show(getActivity(), "Cargando Ubicacion ...",
				"Gracias por su espera", true, true, dialogCancel);
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				pd.dismiss();
				webView.loadUrl("javascript:(function() { "
						+ "document.getElementsByTagName('header')[0].style.display='none';document.getElementsByTagName('anio')[0].style.display='none'; "
						+ "})()");
			}
		});

		webView.loadUrl(url);
	}
}

*/
	
	