package com.arvind;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BodyTemprature {
    public static void main(String[] args) {

        double[] bodyTempArray = new double[]{Integer.MAX_VALUE,Integer.MIN_VALUE};
        bodyTempArray = getBodyTemprature("Dr Arnold Bullock",2,bodyTempArray,1);
        if (bodyTempArray[0] == Integer.MAX_VALUE && bodyTempArray[1] == Integer.MIN_VALUE){
            System.out.println("BodyTemprature Error");
        }
        System.out.println("minTemp:"+bodyTempArray[0] + "MaxTemp:" + bodyTempArray[1]);
    }
    private static double[] getBodyTemprature(String docName, int diagnosisId, double[] bodyTempArray, int pageNum){
        try {
            String bodyTempJson = getBodyTempraturePerPage("https://jsonmock.hackerrank.com/api/medical_records", pageNum);
            JsonObject jsonResponse = new Gson().fromJson(bodyTempJson, JsonObject.class);
          //  System.out.println(jsonResponse);
            int totalPages = jsonResponse.get("total_pages").getAsInt();
            JsonArray jsonArray = jsonResponse.getAsJsonArray("data");
            for(JsonElement jsonElement: jsonArray) {
                JsonObject docDetails = jsonElement.getAsJsonObject().get("doctor").getAsJsonObject();
                JsonObject jsonObject = jsonElement.getAsJsonObject().get("vitals").getAsJsonObject();
                double bodytemp = jsonObject.get("bodyTemperature").getAsDouble();
                if ((docDetails.get("name").getAsString().equals(docName)) &&
                        (docDetails.get("id").getAsInt()== diagnosisId)) {
                    if (bodytemp < bodyTempArray[0]) {
                        bodyTempArray[0] = bodytemp;
                    }
                    if (bodytemp > bodyTempArray[1]) {
                        bodyTempArray[1] = bodytemp;
                    }
                }
            }
            return totalPages == pageNum ? bodyTempArray: getBodyTemprature(docName, diagnosisId,bodyTempArray,pageNum+1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bodyTempArray;
    }

    private static String getBodyTempraturePerPage(String endpoint, int page) throws IOException {
        URL url = new URL(endpoint+"?pageNum="+page);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Content-Type", "application/json");
        int status = connection.getResponseCode();
        if(status <200 || status >300){
            throw new IOException("error in reading data with the status"+status);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String response = "";

        if((response = br.readLine())!=null){
            sb.append(response);
        }
        return sb.toString();
    }

}