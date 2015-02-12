package com.archcra.plugin.mob.auth;
import java.text.SimpleDateFormat;

import java.util.HashMap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.archcra.plugin.mob.auth.OnLoginListener;
import com.archcra.plugin.mob.auth.ThirdPartyLogin;
import com.archcra.plugin.mob.auth.UserInfo;

import cn.sharesdk.framework.ShareSDK;
import android.widget.Toast;


public class MobAuth extends CordovaPlugin{
	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
         Context context = this.cordova.getActivity().getApplicationContext();

        if (action.equals("auth")) {
             Log.i("MyPlugin", "start to open the auth window...");
            auth(callbackContext);
        }
        else {
            return false;
        }
        return true;
    }
    
private void auth(CallbackContext callbackContext) {
	   final Context context = this.cordova.getActivity().getApplicationContext();
	   final CallbackContext cb = callbackContext;

        cordova.getActivity().runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
                ThirdPartyLogin tpl = new ThirdPartyLogin(context, cb);
                		tpl.setOnLoginListener(new OnLoginListener() {
                			public boolean onSignin(String platform, HashMap<String, Object> res) {
                				// 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
                                Toast.makeText(cordova.getActivity(), "Signed in, should call back ...", Toast.LENGTH_SHORT).show();
                				// 此处全部给回需要注册
                				return true;
                			}
                			public boolean onSignUp(UserInfo info) {
                				// 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                				return true;
                			}
                		});
            tpl.show(context);
	        }
	    });
	}
}
