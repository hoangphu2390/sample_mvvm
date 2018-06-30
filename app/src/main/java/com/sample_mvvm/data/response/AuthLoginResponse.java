package com.sample_mvvm.data.response;

/**
 * Created by HP on 01/12/2017.
 */

public class AuthLoginResponse {

    public boolean result;
    public String message;
    public User user;

    public static class User {
        public int id;
        public String name;
        public String email;
        public String phone;
        public String minimum_minutes;
        public String facebook_id;
        public String google_id;
        public String avatar;
        public String latitude;
        public String longitude;
        public String address;
        public String title;
        public String tag;
        public String session_id;
        public int status;
        public String device_token;
        public String api_token;
        public String price;
        public String created_at;
        public String updated_at;
    }
}
