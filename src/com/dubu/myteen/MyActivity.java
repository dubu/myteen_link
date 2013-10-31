package com.dubu.myteen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MyActivity extends Activity {

    public static final String MYTEEN_HOME = "http://m.my.kids.daum.net";
    public static final String GROUP_TALK_URL = MYTEEN_HOME + "/myteen/do/mobile/group_talk";
    public static final String BATTLE_URL = MYTEEN_HOME + "/myteen/do/mobile/battle/rank";
    public static final String NOVEL_URL = MYTEEN_HOME + "/myteen/do/mobile/novel";
    public static final int HOME_ID = Menu.FIRST;
    private static final int RELOAD_ID = Menu.FIRST + 1;
    private static final int EXIT_ID = Menu.FIRST + 2;
    private static final int GROUP_TALK_ID = Menu.FIRST + 3;
    private static final int BATTLE_ID = Menu.FIRST + 4;
    private static final int NOVEL_ID = Menu.FIRST + 5;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private WebView mWebView;
    private ValueCallback<Uri> mUploadMessage;

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
        WebChromeClient testChromeClient = new MyWebChromeClient();

        //생성한 크롬 클라이언트를 웹뷰에 셋한다
        mWebView.setWebChromeClient(new WebChromeClient() {
            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MyActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MyActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MyActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MyActivity.FILECHOOSER_RESULTCODE);

            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    //todo

                }else if(url.startsWith("intent://createShortcut")){
                    toastNotUse();
                    return true;
                }else{
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        MyActivity.this.startActivity(intent);

                    } catch (ActivityNotFoundException e) {
                        Log.e("dubu", "Could not load url" + url);
                    }
                    return true;

                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        //web view enable zoomin/out
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(true);

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
        } else if ((keyCode == KeyEvent.KEYCODE_MENU)) {
            //todo

        } else {
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
        menu.add(0, NOVEL_ID, 0, R.string.novel);
        menu.add(0, GROUP_TALK_ID, 0, R.string.talk);
        menu.add(0, BATTLE_ID, 0, R.string.battle);
        menu.add(0, RELOAD_ID, 0, R.string.reload);
        menu.add(0, EXIT_ID, 0, R.string.exit);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case HOME_ID:
                mWebView.loadUrl(MYTEEN_HOME);
                return true;
            case GROUP_TALK_ID:
                mWebView.loadUrl(GROUP_TALK_URL);
                return true;
            case BATTLE_ID:
                mWebView.loadUrl(BATTLE_URL);
                return true;
            case NOVEL_ID:
                mWebView.loadUrl(NOVEL_URL);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
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

    class MyWebChromeClient extends WebChromeClient {
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            MyActivity.this.startActivityForResult(
                    Intent.createChooser(i, "Image Browser"),
                    FILECHOOSER_RESULTCODE);
        }
    }
}