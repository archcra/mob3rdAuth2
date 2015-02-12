package com.archcra.plugin.mob.auth;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import org.apache.cordova.CallbackContext;

import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.sharesdk.framework.FakeActivity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import android.util.Log;
import com.synconset.FakeR;

import org.json.JSONObject;


/** 中文注释
 * ShareSDK 官网地址 ： http://www.mob.com </br>
 *1、这是用2.38版本的sharesdk，一定注意  </br>
 *2、如果要咨询客服，请加企业QQ 4006852216 </br>
 *3、咨询客服时，请把问题描述清楚，最好附带错误信息截图 </br>
 *4、一般问题，集成文档中都有，请先看看集成文档；减少客服压力，多谢合作  ^_^</br></br></br>
 *
 *The password of demokey.keystore is 123456
 **ShareSDK Official Website ： http://www.mob.com </br>
 *1、Be carefully, this sample use the version of 2.11 sharesdk  </br>
 *2、If you want to ask for help，please add our QQ whose number is 4006852216 </br>
 *3、Please describe detail of the question , if you have the picture of the bugs or the bugs' log ,that is better </br>
 *4、Usually, the answers of some normal questions is exist in our user guard pdf, please read it more carefully,thanks  ^_^
*/
public class ThirdPartyLogin extends FakeActivity implements OnClickListener, Callback, PlatformActionListener {
	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR= 3;
	private static final int MSG_AUTH_COMPLETE = 4;
    
	private OnLoginListener signupListener;
	private Handler handler;
	//短信验证的对话框
	private Dialog msgLoginDlg;
	private CallbackContext callbackContext;
 private FakeR fakeR;

	/** 设置授权回调，用于判断是否进入注册 */
	public void setOnLoginListener(OnLoginListener l) {
		this.signupListener = l;
	}
    
    public ThirdPartyLogin(Context context, CallbackContext callbackContext){
    	this.callbackContext = callbackContext;
        // Contrustion, to init the fakeR
         try{
        fakeR = new FakeR(context);
             }catch(Exception e){
            Log.i("MyPlugin","fakeR init exception:"+e);
        }
    }
	
	public void onCreate() {
        try{
        Log.i("MyPlugin","onCreate of ThirdPartyLogin");
		// 初始化ui
		handler = new Handler(this);
 activity.setContentView(fakeR.getId("layout", "tpl_login_page"));
         (activity.findViewById(fakeR.getId("id", "tvWeibo") )).setOnClickListener(this);                
                         
                     
		(activity.findViewById(fakeR.getId("id", "tvQq"))).setOnClickListener(this);

        }catch(Exception e){
            Log.i("MyPlugin","Exception in onCreate of ThirdPartyLogin:"+e);

        }
	}
	
	public void onDestroy() {
		}

	public void onClick(View v) {
        try{
         final   int wb = fakeR.getId("id", "tvWeibo");
       final int qq = fakeR.getId("id", "tvQq");
    
        if(v.getId() == wb){
            //新浪微博
				  Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
				authorize(sina);         
        }else if(v.getId() == qq){
                    //QQ
             Platform pfQq = ShareSDK.getPlatform(QQ.NAME);
				authorize(pfQq); 
        }else{
            Toast.makeText(activity, "Not supported yet.", Toast.LENGTH_SHORT).show();
            
        }
        }catch(Exception e){
             Log.i("MyPlugin","Exception in onClick of view:"+e);
            
        }
	}
	
	//执行授权,获取用户信息
	//文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
	private void authorize(Platform plat) {
		if (plat == null) {
            Toast.makeText(activity, "Not supported yet.", Toast.LENGTH_SHORT).show();
			return;
		}
		plat.setPlatformActionListener(this);
		//关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}
	
	
	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] {platform.getName(), res};
			handler.sendMessage(msg);
		}
	}
	
	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}
	
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case MSG_AUTH_CANCEL: {
				//取消授权
                Toast.makeText(activity, "Canceled", Toast.LENGTH_SHORT).show();
                
			} break;
			case MSG_AUTH_ERROR: {
				//授权失败
                Toast.makeText(activity, "Auth failed", Toast.LENGTH_SHORT).show();
			} break;
			case MSG_AUTH_COMPLETE: {
				//授权成功
			        Toast.makeText(activity, "Auth passed!", Toast.LENGTH_SHORT).show();
                
                // Get user info:
             UserInfo userInfo = new UserInfo();
                Object[] objs = (Object[]) msg.obj;
				String platformStr = (String) objs[0];
                Platform platform = ShareSDK.getPlatform(platformStr);
                
              		String gender = "";
                		gender = platform.getDb().getUserGender();
			if(gender.equals("m")){
				userInfo.setUserGender(UserInfo.Gender.BOY);
			}else{
				userInfo.setUserGender(UserInfo.Gender.GIRL);
			}
			userInfo.setUserIcon(platform.getDb().getUserIcon());
			userInfo.setUserName(platform.getDb().getUserName());
			userInfo.setUserNote(platform.getDb().getUserId());
                /*
                // Show info
                Toast.makeText(activity, "Gender:" + userInfo.getUserGender(), Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "UserIcon:" + userInfo.getUserIcon(), Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "name:" + userInfo.getUserName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "id:" + userInfo.getUserNote(), Toast.LENGTH_SHORT).show();
                */
                
                 try{
                      Log.i("MyPlugin","Before set result");
		  JSONObject result = new JSONObject();
            result.put("status", "COMPLETE");
            result.put("userInfo", userInfo.toJson());
			callbackContext.success(result);
             Log.i("MyPlugin","After set result.");
             }catch(Exception e){
            Log.i("MyPlugin",e.toString());
        }
                
			}break;
		}
		return false;
	}
	
	public void show(Context context) {
        Log.i("MyPlugin","show of ThirdPartyLogin");
		initSDK(context);
		super.show(context, null);
	}
	
	private void initSDK(Context context) {
		//初始化sharesdk,具体集成步骤请看文档：	//http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
		ShareSDK.initSDK(context);
                Log.i("MyPlugin","after ShareSDK init");
	}
}