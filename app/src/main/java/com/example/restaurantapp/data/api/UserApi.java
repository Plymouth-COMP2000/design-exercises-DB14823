package com.example.restaurantapp.data.api;

import androidx.annotation.NonNull;

import com.example.restaurantapp.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserApi {

    // 1) Create your student DB on the server
    public void createStudent() throws Exception {
        String urlStr = ApiConfig.BASE_URL + "/create_student/" + ApiConfig.STUDENT_ID;
        postJson(urlStr, null); // no body required
    }

    // 2) Create a user in your student DB
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

    // 3) Read a user
    public User readUser(String username) throws Exception {
        String urlStr = ApiConfig.BASE_URL + "/read_user/" + ApiConfig.STUDENT_ID + "/" + username;

        JSONObject json = getJsonObject(urlStr);
        if (json == null) return null; // 404 -> not found

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

    // ---------- helpers ----------

    // GET JSON, return null if 404 (not found)
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

    // POST with optional JSON body (null body allowed)
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
        // Many APIs return 200/201 on success
        if (code != 200 && code != 201) {
            throw new Exception("HTTP " + code);
        }

        // consume response (optional but good practice)
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while (br.readLine() != null) { /* ignore */ }
        br.close();
    }
}
