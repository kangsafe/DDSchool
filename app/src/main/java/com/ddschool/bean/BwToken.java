package com.ddschool.bean;

public class BwToken extends BwResult {
    private Token data;

    public Token getData() {
        return data;
    }

    public void setData(Token data) {
        this.data = data;
    }

    public class Token{
        //token
        private String access_token;
        //时长
        private int expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}
