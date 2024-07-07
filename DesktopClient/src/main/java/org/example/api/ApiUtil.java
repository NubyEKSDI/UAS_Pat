package org.example.api;

import org.example.model.Equipment;
import org.example.model.User;
import org.example.model.BorrowRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ApiUtil {
    private static final String BASE_URL = "http://192.168.1.10:8000";
    private static final Gson gson = new Gson();

    public static User login(String username, String password) throws IOException {
        HttpPost post = new HttpPost(BASE_URL + "/login");
        String json = gson.toJson(new User(username, password));
        post.setEntity(new StringEntity(json));
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            return gson.fromJson(responseBody, User.class);
        }
    }

    public static void signUp(String username, String password) throws IOException {
        HttpPost post = new HttpPost(BASE_URL + "/signup_admin");
        String json = gson.toJson(new User(username, password));
        post.setEntity(new StringEntity(json));
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {
            EntityUtils.consume(response.getEntity());
        }
    }

    public static List<Equipment> getEquipment() throws IOException {
        HttpGet get = new HttpGet(BASE_URL + "/equipment");
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(get)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            Type listType = new TypeToken<List<Equipment>>() {}.getType();
            return gson.fromJson(responseBody, listType);
        }
    }

    public static void addEquipment(String name, int quantity) throws IOException {
        HttpPost post = new HttpPost(BASE_URL + "/add-equipment");
        String json = gson.toJson(new Equipment(name, quantity));
        post.setEntity(new StringEntity(json));
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {
            EntityUtils.consume(response.getEntity());
        }
    }

    public static void updateEquipmentQuantity(int equipmentId, int quantity, boolean add) throws IOException {
        String url = add ? "/add-equipment-quantity" : "/reduce-equipment-quantity";
        HttpPost post = new HttpPost(BASE_URL + url);
        String json = "{\"equipment_id\":" + equipmentId + ", \"quantity\":" + quantity + "}";
        post.setEntity(new StringEntity(json));
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {
            EntityUtils.consume(response.getEntity());
        }
    }

    public static void deleteEquipment(int equipmentId) throws IOException {
        HttpDelete delete = new HttpDelete(BASE_URL + "/delete-equipment/" + equipmentId);
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(delete)) {
            EntityUtils.consume(response.getEntity());
        }
    }

    public static List<BorrowRecord> getBorrowRecords() throws IOException {
        HttpGet get = new HttpGet(BASE_URL + "/borrow_records");
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(get)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            Type listType = new TypeToken<List<BorrowRecord>>() {}.getType();
            return gson.fromJson(responseBody, listType);
        }
    }
}
