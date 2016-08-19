package com.colorchen.mvp.view;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.colorchen.R;
import com.colorchen.mvp.jspanel.JSWebViewClient;
import com.colorchen.mvp.jspanel.interf.JSHandler;
import com.colorchen.mvp.jspanel.interf.JSResponseCallback;
import com.colorchen.ui.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by color on 16/4/25 19:43.
 */
public class TestJSDemo extends BaseActivity{
    @Bind(R.id.web_view_js)
    WebView webView;
    private JSWebViewClient webViewClient;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.web_js_demo;
    }

    @Override
    protected void initViews() {
        super.initViews();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/ExampleApp.html");

        webViewClient = new MyWebViewClient(webView);
        webViewClient.enableLogging();
        webView.setWebViewClient(webViewClient);

        doJS();
    }

    private void doJS() {
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                webViewClient.send("send公共数据", new JSResponseCallback() {

                    @Override
                    public void callback(Object data) {
                        Toast.makeText(TestJSDemo.this, "send公共数据 got response: " + data, Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                webViewClient.send("哈药集团秘密文件", new JSResponseCallback() {

                    @Override
                    public void callback(Object data) {
                        Toast.makeText(TestJSDemo.this, "本地 got response: " + data, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        /*findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    webViewClient.callHandler("testJavascriptHandler01", new JSONObject("{\"greetingFromObjC\": \"Hi there, JS!\" }"), new JSResponseCallback() {

                        @Override
                        public void callback(Object data) {
                            Toast.makeText(TestJSDemo.this, "Handler01 responded: " + data, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    webViewClient.callHandler("testJavascriptHandler02", new JSONObject("{\"greetingFromObjC\": \"Hi there, JS!\" }"), new JSResponseCallback() {

                        @Override
                        public void callback(Object data) {
                            Toast.makeText(TestJSDemo.this, "Handler02 responded: " + data, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyWebViewClient extends JSWebViewClient {

        public MyWebViewClient(WebView webView) {

            // support js send
            super(webView, new JSHandler() {

                @Override
                public void request(Object data, JSResponseCallback callback) {
                    Toast.makeText(TestJSDemo.this, "XXX--本地收到JS:" + data, Toast.LENGTH_LONG).show();

                    callback.callback("XXX收到js数据:本地响应一下）!");
                }
            });


			/*
			// not support js send
			super(webView);
			*/

            enableLogging();

            //注册本地毁回调方法 testObjcCallback01
            registerHandler("testObjcCallback01", new JSHandler() {

                @Override
                public void request(Object data, JSResponseCallback callback) {
                    Toast.makeText(TestJSDemo.this, "testObjcCallback---01:" + data, Toast.LENGTH_LONG).show();
                    callback.callback("Response from testObjcCallback!");
                }
            });

            //注册本地毁回调方法 testObjcCallback02
            registerHandler("testObjcCallback02", new JSHandler() {

                @Override
                public void request(Object data, JSResponseCallback callback) {
                    Toast.makeText(TestJSDemo.this, "testObjcCallback---02:" + data, Toast.LENGTH_LONG).show();
                    callback.callback("Response from testObjcCallback!");
                }
            });

            /*send("公共数据--000 js before WebView has loaded.", new JBResponseCallback() {

                @Override
                public void callback(Object data) {

                    Toast.makeText(MainActivity.this, "000--ObjC got response :" + data, Toast.LENGTH_LONG).show();
                }
            });

            try {
                callHandler("testJavascriptHandler01", new JSONObject("{\"testJavascriptHandler--01\":\"before ready\" }"), new JBResponseCallback() {

                    @Override
                    public void callback(Object data) {
                        Toast.makeText(MainActivity.this, "001 :" + data, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                callHandler("testJavascriptHandler02", new JSONObject("{\"testJavascriptHandler02--02\":\"before ready\" }"), new JBResponseCallback() {

                    @Override
                    public void callback(Object data) {
                        Toast.makeText(MainActivity.this, "002 :" + data, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

    }
}
