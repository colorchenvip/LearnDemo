package com.colorchen.mvp.jspanel;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.colorchen.mvp.jspanel.interf.JSHandler;
import com.colorchen.mvp.jspanel.interf.JSResponseCallback;
import com.colorchen.mvp.jspanel.interf.JavascriptCallback;
import com.colorchen.mvp.jspanel.model.JSMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by color on 16/4/26 14:35.
 */
public class JSWebViewClient extends WebViewClient {
    private static final String kTag = "WVJB";
    private static final String kInterface = kTag + "Interface";
    private static final String kCustomProtocolScheme = "wvjbscheme";
    private static final String kQueueHasMessage = "__WVJB_QUEUE_MESSAGE__";

    private static boolean logging = false;
    protected WebView webView;

    private ArrayList<JSMessage> startupMessageQueue = null;//启动队列
    private Map<String, JSResponseCallback> responseCallbacks = null;//发送数据时的 回调集合
    private Map<String, JSHandler> messageHandlers = null;//注册的js事件集合
    private MyJavascriptInterface myInterface = new MyJavascriptInterface();
    private JSHandler messageHandler;//回调接口
    private long uniqueId = 0;

    public JSWebViewClient(WebView webView){
        this(webView,null);
    }

    public JSWebViewClient(WebView webView,JSHandler messageHandler){
        this.webView = webView;

        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.addJavascriptInterface(myInterface,kInterface);

        this.responseCallbacks = new HashMap<>();
        this.messageHandlers = new HashMap<>();
        this.startupMessageQueue = new ArrayList<>();
        this.messageHandler =messageHandler;
    }
    public void enableLogging(){
        logging = true;
    }

    public void registerHandler(String handlerName,JSHandler handler){
        if (handlerName == null || handlerName.length() == 0|| handler == null){
            return;
        }
        messageHandlers.put(handlerName,handler);
    }

    public void send(Object data){
        send(data, null);
    }

    public void send(Object data,JSResponseCallback responseCallback){
        sendData(data, responseCallback, null);
    }

    public void callHandler(String handlerName) {
        callHandler(handlerName, null, null);
    }

    public void callHandler(String handlerName, Object data) {
        callHandler(handlerName, data, null);
    }

    public void callHandler(String handlerName, Object data,
                            JSResponseCallback responseCallback) {
        sendData(data, responseCallback, handlerName);
    }

    private void sendData(Object data,JSResponseCallback responseCallback,String handlerName){
        //数据和handlerName不能同时为空 send必须有数据 callback 必须有handlerName
        if (data == null && (handlerName == null || handlerName.length() == 0)){
            return;
        }

        JSMessage message = new JSMessage();
        if (data != null){
            message.data = data;
        }
        if (responseCallback != null){
            String callbackId = "objc_cb"+(++uniqueId);
            //把每一个发送数据时 注册的注册的回调 编号放入队列responseCallbacks
            responseCallbacks.put(callbackId, responseCallback);
            message.callbackId = callbackId;
        }
        if (handlerName != null) {
            message.handlerName = handlerName;
        }
        queueMessage(message);
    }

    private void queueMessage(JSMessage message) {
        if (startupMessageQueue != null) {
            startupMessageQueue.add(message);
        } else {
            dispatchMessage(message);
        }
    }

    private void dispatchMessage(JSMessage message) {
        String messageJSON = message2JSONObject(message).toString()
                .replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"")
                .replaceAll("\'", "\\\\\'").replaceAll("\n", "\\\\\n")
                .replaceAll("\r", "\\\\\r").replaceAll("\f", "\\\\\f");

        log("SEND", messageJSON);

        executeJavascript("WebViewJavascriptBridge._handleMessageFromObjC('"
                + messageJSON + "');");
    }
    public void log(String action, Object json) {
        if (!logging)
            return;
        String jsonString = String.valueOf(json);
        if (jsonString.length() > 500) {
            Log.i(kTag, action + ": " + jsonString.substring(0, 500) + " [...]");
        } else {
            Log.i(kTag, action + ": " + jsonString);
        }
    }
    public void executeJavascript(String script) {
        executeJavascript(script, null);
    }

    public void executeJavascript(String script,
                                  final JavascriptCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(script, new ValueCallback<String>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onReceiveValue(String value) {
                    if (callback != null) {
                        if (value != null && value.startsWith("\"")
                                && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1)
                                    .replaceAll("\\\\", "");
                        }
                        callback.onReceiveValue(value);
                    }
                }
            });
        } else {
            if (callback != null) {
                myInterface.addCallback(++uniqueId + "", callback);
                webView.loadUrl("javascript:window." + kInterface
                        + ".onResultForScript(" + uniqueId + "," + script + ")");
            } else {
                webView.loadUrl("javascript:" + script);
            }
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        try {
            InputStream is = webView.getContext().getAssets()
                    .open("WebViewJavascriptBridge.js");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String js = new String(buffer);

            //在web加载完毕之后 开始初始化本地js方法
            executeJavascript(js);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (startupMessageQueue != null) {
            for (int i = 0; i < startupMessageQueue.size(); i++) {
                dispatchMessage(startupMessageQueue.get(i));
            }
            startupMessageQueue = null;
        }
        super.onPageFinished(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(kCustomProtocolScheme)) {
            if (url.indexOf(kQueueHasMessage) > 0) {
                flushMessageQueue();
            }
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    private void flushMessageQueue() {
        String script = "WebViewJavascriptBridge._fetchQueue()";
        executeJavascript(script, new JavascriptCallback() {
            public void onReceiveValue(String messageQueueString) {
                if (messageQueueString == null
                        || messageQueueString.length() == 0)
                    return;
                processQueueMessage(messageQueueString);
            }
        });
    }

    /**
     * 执行js返回的数据
     * @param messageQueueString
     */
    private void processQueueMessage(String messageQueueString) {
        try {
            JSONArray messages = new JSONArray(messageQueueString);
            for (int i = 0; i < messages.length(); i++) {
                JSONObject jo = messages.getJSONObject(i);

                log("RCVD", jo);

                JSMessage message = JSONObject2WVJBMessage(jo);
                if (message.responseId != null) {
                    //callback 返回的数据
                    JSResponseCallback responseCallback = responseCallbacks
                            .remove(message.responseId);
                    if (responseCallback != null) {
                        responseCallback.callback(message.responseData);
                    }
                } else {
                    //send方式 发送来的数据
                    JSResponseCallback responseCallback = null;
                    if (message.callbackId != null) {
                        final String callbackId = message.callbackId;
                        responseCallback = new JSResponseCallback() {
                            @Override
                            public void callback(Object data) {
                                JSMessage msg = new JSMessage();
                                msg.responseId = callbackId;
                                msg.responseData = data;
                                queueMessage(msg);
                            }
                        };
                    }

                    JSHandler handler;
                    if (message.handlerName != null) {
                        handler = messageHandlers.get(message.handlerName);
                    } else {
                        handler = messageHandler;
                    }

                    //  send方式: 回调的返回数据
                    if (handler != null) {
                        handler.request(message.data, responseCallback);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private JSMessage JSONObject2WVJBMessage(JSONObject jo) {
        JSMessage message = new JSMessage();
        try {
            if (jo.has("callbackId")) {
                message.callbackId = jo.getString("callbackId");
            }
            if (jo.has("data")) {
                message.data = jo.get("data");
            }
            if (jo.has("handlerName")) {
                message.handlerName = jo.getString("handlerName");
            }
            if (jo.has("responseId")) {
                message.responseId = jo.getString("responseId");
            }
            if (jo.has("responseData")) {
                message.responseData = jo.get("responseData");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    private JSONObject message2JSONObject(JSMessage message) {
        JSONObject jo = new JSONObject();
        try {
            if (message.callbackId != null) {
                jo.put("callbackId", message.callbackId);
            }
            if (message.data != null) {
                jo.put("data", message.data);
            }
            if (message.handlerName != null) {
                jo.put("handlerName", message.handlerName);
            }
            if (message.responseId != null) {
                jo.put("responseId", message.responseId);
            }
            if (message.responseData != null) {
                jo.put("responseData", message.responseData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
}
