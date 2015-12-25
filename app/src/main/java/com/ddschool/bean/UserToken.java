package com.ddschool.bean;

public class UserToken {
	private static String AccessToken;
	private static int Timeout;
	public static String getAccessToken() {
		return AccessToken;
	}
	public static void setAccessToken(String accessToken) {
		AccessToken = accessToken;
	}
	public static int getTimeout() {
		return Timeout;
	}
	public static void setTimeout(int timeout) {
		Timeout = timeout;
	}
}
