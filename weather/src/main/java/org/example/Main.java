//package org.example;
//
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
////import java.lang.classfile.attribute.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Scanner;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
////TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
//// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//public class Main {
//    private static final String API_KEY = "c8d4d79bc902460f0a0df1afcecf636e";
//    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
//
//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Введите город: ");
//        String city = scanner.nextLine();
//
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            try {
//                String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=ru";
//                URL url = new URL(urlString);
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//
//                int responseCode = conn.getResponseCode();
//                if (responseCode == 404) {
//                    System.out.println("ERROR 404 \nГород не найден. Пожалуйста, попробуйте еще раз.");
//                    return;
//                }
//
//                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//                conn.disconnect();
//
//                JSONObject jsonResponse = new JSONObject(response.toString());
//                JSONObject main = jsonResponse.getJSONObject("main");
//                double temperature = main.getDouble( "temp");
//
//                System.out.println("Погода в " + city + ":");
//                System.out.println("Температура: " + temperature + "°C");
//
////                System.out.println(response);
//
//
//            }
//            catch (Exception e) {
//                System.out.println("Ошибка: " + e.getMessage());
//            }
//        });
//        future.get();
//    }
//}




package org.example;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    private static final String API_KEY = "45a6be3d263638fd4364dda24b4ea206";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String city;

        while (true) {
            System.out.print("Введите город (или 'Выход' для завершения): ");
            String cityInput = scanner.nextLine();

            if (cityInput.equalsIgnoreCase("Выход")) {
                break;
            }

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    String urlString = BASE_URL + "?q=" + cityInput + "&appid=" + API_KEY + "&units=metric&lang=ru";
                    URL url = new URL(urlString);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    int responseCode = conn.getResponseCode();
                    if (responseCode == 404) {
                        System.out.println("Город не найден. Пожалуйста, попробуйте еще раз.");
                        return;
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    conn.disconnect();

                    JSONObject jsonResponse = new JSONObject(response.toString());

                    if (jsonResponse.has("message")) {
                        System.out.println("Ошибка при запросе к API: " + jsonResponse.getString("message"));
                        return;
                    }

                    displayWeather(cityInput, jsonResponse);

                } catch (IOException | org.json.JSONException e) {
                    System.err.println("Ошибка: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            future.get();
        }

        scanner.close();
        System.out.println("Программа завершена.");
    }

    private static void displayWeather(String city, JSONObject jsonResponse) throws org.json.JSONException {
        JSONObject main = jsonResponse.getJSONObject("main");
        double temperature = main.getDouble("temp");
        double humidity = main.getDouble("humidity");
        double windSpeed = jsonResponse.getJSONObject("wind").getDouble("speed");
        String description = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");

        System.out.println("Погода в " + city + ":");
        System.out.println("Описание: " + description);
        System.out.println("Температура: " + temperature + "°C");
        System.out.println("Влажность: " + humidity + "%");
        System.out.println("Скорость ветра: " + windSpeed + " м/с");

    }
}