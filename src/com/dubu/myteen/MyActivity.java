package com.dubu.myteen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

public class MyActivity extends Activity {

    public static final int HOME_ID = Menu.FIRST;
    private static final int RELOAD_ID = Menu.FIRST + 1;
    private static final int EXIT_ID = Menu.FIRST + 2;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setLayout();

        // 웹뷰에서 자바스크립트실행가능
        mWebView.getSettings().setJavaScriptEnabled(true);
        // url 지정
        mWebView.loadUrl("http://m.my.kids.daum.net");
        // WebViewClient 지정
        mWebView.setWebViewClient(new WebViewClientClass());

        // 구글에서 제공하는 크롬클라이언트를 생성한다.
        WebChromeClient testChromeClient = new WebChromeClient();

        //생성한 크롬 클라이언트를 웹뷰에 셋한다
        mWebView.setWebChromeClient(testChromeClient);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String overrideUrl) {
                if (overrideUrl.startsWith("myp://")) {
                    toastNotUse();
                    return true;
                }
                if (overrideUrl.startsWith("kakaolink:")) {
                    toastNotUse();
                    return true;
                }
                if (overrideUrl.startsWith("daummail:")) {
                    toastNotUse();
                    return true;
                }
                if (overrideUrl.startsWith("storylink:")) {
                    toastNotUse();
                    return true;
                }
                if (overrideUrl.startsWith("bandapp:")) {
                    toastNotUse();
                    return true;
                }

                if (overrideUrl.startsWith("intent://")) {
                    toastNotUse();
                    return true;
                }

                if (overrideUrl.startsWith("chaton:")) {
                    toastNotUse();
                    return true;
                }

                if (overrideUrl.startsWith("market:")) {
                    toastNotUse();
                    return true;
                }

                return false;
            }
        });
    }

    private void toastNotUse() {
        Toast toast = Toast.makeText(getApplicationContext(), "앱에서는 지원하지 않습니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }else if((keyCode == KeyEvent.KEYCODE_MENU)){
             //todo

        }else {
            String alertTitle = getResources().getString(R.string.app_name);
            String buttonMessage = getResources().getString(R.string.alert_msg_exit);
            String buttonYes = getResources().getString(R.string.button_yes);
            String buttonNo = getResources().getString(R.string.button_no);

            new AlertDialog.Builder(MyActivity.this)
                    .setTitle(alertTitle)
                    .setMessage(buttonMessage)
                    .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                            finish();
                        }
                    })
                    .setNegativeButton(buttonNo, null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, HOME_ID, 0, R.string.home);
        menu.add(0, RELOAD_ID, 0, R.string.reload);
        menu.add(0, EXIT_ID, 0, R.string.exit);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case HOME_ID:
                mWebView.loadUrl("http://m.my.kids.daum.net");
                return true;
            case RELOAD_ID:
                mWebView.reload();
                return true;
            case EXIT_ID:
                moveTaskToBack(true);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * Layout
     */
    private void setLayout() {
        mWebView = (WebView) findViewById(R.id.webview);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}