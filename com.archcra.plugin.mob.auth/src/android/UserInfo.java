package com.archcra.plugin.mob.auth;

import org.json.JSONObject;
import android.util.Log;


public class UserInfo {
	private String userIcon;
	private String userName;
	private Gender userGender;
	private String userNote;
	

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Gender getUserGender() {
		return userGender;
	}

	public void setUserGender(Gender userGender) {
		this.userGender = userGender;
	}

	public String getUserNote() {
		return userNote;
	}

	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}

	public static enum Gender {BOY, GIRL}

	public JSONObject toJson(){
        JSONObject result = null;
       try{
		 result = new JSONObject();
		result.put("userIcon",this.userIcon);
		result.put("userName",this.userName);
		result.put("userGender",this.userGender);
		result.put("userId",this.userNote);
  }catch(Exception e){
            Log.i("MyPlugin",e.toString());
        }
		return result;
	}


}
