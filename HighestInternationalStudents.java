package com.arvind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class HighestInternationalStudents {

    public static void main(String[] args) {
        int highestStudentsInCity1 = Integer.MIN_VALUE;
        int highestStudentsInCity2 = Integer.MIN_VALUE;
        String university1 = null;
        String university2 = null;
        String finalUniversityName = getInternationStudent("Singapore","New York City",1,highestStudentsInCity1,
                highestStudentsInCity2, university1, university2);
        System.out.println(finalUniversityName);
    }

    static String getInternationStudent(String city1, String city2, int page,
                                        int highestStudentsInCity1,int highestStudentsInCity2, String university1,String university2) {
        try {
            String internationStudentJson = getInternationStudentPerPage("https://jsonmock.hackerrank.com/api/universities", page);
            JsonObject jsonResponse = new Gson().fromJson(internationStudentJson,JsonObject.class);
            JsonArray jsonArray = jsonResponse.get("data").getAsJsonArray();
            int totalPages = jsonResponse.get("total_pages").getAsInt();
            for(JsonElement jsonElement: jsonArray) {
                String universityFromResponse = jsonElement.getAsJsonObject().get("university").getAsString();

                int internationalStudentsCount = Integer.parseInt(jsonElement.getAsJsonObject().get("international_students").getAsString().replaceAll(",",""));;
                JsonObject locationJsonObject = jsonElement.getAsJsonObject().get("location").getAsJsonObject();
                String city = locationJsonObject.get("city").getAsString();
                if (city.equalsIgnoreCase(city1) &&
                        (universityFromResponse!= null && !universityFromResponse.equalsIgnoreCase("")
                              //  || internationalStudentsCount!=Integer.MIN_VALUE
                        )){
                    if (internationalStudentsCount > highestStudentsInCity1) {
                        highestStudentsInCity1 = internationalStudentsCount;
                        university1 = universityFromResponse;
                    }
                } else if (city.equalsIgnoreCase(city2) &&
                        (universityFromResponse!= null && !universityFromResponse.equalsIgnoreCase(""))){
                    if (internationalStudentsCount > highestStudentsInCity2) {
                        highestStudentsInCity2 = internationalStudentsCount;
                        university2 = universityFromResponse;
                    }
                }
            }
            return totalPages == page ? (university1 != null ? university1: university2): getInternationStudent(city1, city2, page+1,
                    highestStudentsInCity1,highestStudentsInCity2, university1,university2);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getInternationStudentPerPage(String endpoint, int page) throws Exception{
        URL url = new URL(endpoint+"?page="+page);
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
