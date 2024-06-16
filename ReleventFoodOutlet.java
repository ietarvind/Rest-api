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
import java.util.ArrayList;
import java.util.List;

public class ReleventFoodOutlet {

    public static void main(String[] args) {
        List<String> outlets = new ArrayList<>();
        List<String> outletResponse =getReleventOutlet("Denver",50,1,outlets);
        System.out.println(outletResponse);
    }

   public static List<String> getReleventOutlet(String city, int maxcost, int page,List<String> outlets){

        try {
            String response = getReleventOutletPerPage("https://jsonmock.hackerrank.com/api/food_outlets?city="+city,page);
            JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
            //  System.out.println(jsonResponse);
            int totalPages = jsonResponse.get("total_pages").getAsInt();

            JsonArray jsonArray = jsonResponse.getAsJsonArray("data");
            for(JsonElement jsonElement: jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String cityFromResponse = jsonObject.get("city").getAsString();
                String outletName = jsonObject.get("name").getAsString();
                int maxCostFromResponse = jsonObject.get("estimated_cost").getAsInt();
                if(cityFromResponse.equalsIgnoreCase(city) && maxCostFromResponse <= maxcost){
                    outlets.add(outletName);
                }
            }
            return totalPages == page ? outlets: getReleventOutlet(city, maxcost,page+1,outlets);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }


        return outlets;
    }
    public static String getReleventOutletPerPage(String endpoint,int page) throws IOException{
        URL url = new URL(endpoint+"&page="+page);
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
