package com.example.restaurantapp.data.api;

import com.example.restaurantapp.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserApi {

    public void createStudent() throws Exception {
        String urlStr = ApiConfig.BASE_URL + "/create_student/" + ApiConfig.STUDENT_ID;
        postJson(urlStr, null);
    }

    public void createUser(User user) throws Exception {
        String urlStr = ApiConfig.BASE_URL + "/create_user/" + ApiConfig.STUDENT_ID;

        JSONObject body = new JSONObject();
        body.put("username", user.username);
        body.put("password", user.password);
        body.put("firstname", user.firstname);
        body.put("lastname", user.lastname);
        body.put("email", user.email);
        body.put("contact", user.contact);
        body.put("usertype", user.usertype);

        postJson(urlStr, body);
    }

    public User readUser(String username) throws Exception {
        String urlStr = ApiConfig.BASE_URL + "/read_user/" + ApiConfig.STUDENT_ID + "/" + username;

        JSONObject root = getJsonObject(urlStr);
        if (root == null) return null;


        JSONObject json = root.optJSONObject("user");
        if (json == null) json = root;

        User user = new User();
        user.username = json.optString("username");
        user.password = json.optString("password");
        user.firstname = json.optString("firstname");
        user.lastname = json.optString("lastname");
        user.email = json.optString("email");
        user.contact = json.optString("contact");
        user.usertype = json.optString("usertype");

        return user;
    }



    private static JSONObject getJsonObject(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        int code = conn.getResponseCode();
        if (code == 404) return null;
        if (code != 200) throw new Exception("HTTP " + code);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return new JSONObject(sb.toString());
    }

    private static void postJson(String urlStr, JSONObject body) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoOutput(true);

        if (body != null) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(body.toString());
            bw.flush();
            bw.close();
        }

        int code = conn.getResponseCode();
        if (code != 200 && code != 201) {
            throw new Exception("HTTP " + code);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while (br.readLine() != null) {}
        br.close();
    }
}
