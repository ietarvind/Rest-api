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
import java.util.*;
import java.util.stream.Collectors;

public class AsteroidMonitoringStation {

    public static void main(String[] args) {
        List<AsteroidMonitoringStations> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        String[] finalOutput = getAsteroidMonitoringStation(2011,"Y",1,list, list2);
       for(String value: finalOutput) {
           System.out.println(value);
       }
       // System.out.println(finalOutput);

    }


    static String[] getAsteroidMonitoringStation(int year,String pha, int page,List<AsteroidMonitoringStations> asteroidMonitoringStationsList
            ,List<String> response1){
        try {
            String responseJson = getAsteroidMonitoringStationPerPage("https://jsonmock.hackerrank.com/api/asteroids/search", pha, page);
            JsonObject jsonResponse = new Gson().fromJson(responseJson, JsonObject.class);
            JsonArray jsonArray = jsonResponse.get("data").getAsJsonArray();
            int totalPages = jsonResponse.get("total_pages").getAsInt();
         //   System.out.println(totalPages);
            for (JsonElement jsonElement : jsonArray) {
                String designation = jsonElement.getAsJsonObject().get("designation").getAsString();
                String discoveryDate = jsonElement.getAsJsonObject().get("discovery_date").getAsString();
                String pha1 = jsonElement.getAsJsonObject().get("pha").getAsString();
                String periodYr = jsonElement.getAsJsonObject().get("period_yr") == null ? "":  jsonElement.getAsJsonObject().get("period_yr").getAsString();
                AsteroidMonitoringStations asteroidMonitoringStations = new AsteroidMonitoringStations(designation,
                        periodYr, discoveryDate,pha1);
                asteroidMonitoringStationsList.add(asteroidMonitoringStations);
            }
            Collections.sort(asteroidMonitoringStationsList,new AsteroidMonitoringStations() {});
            /*list = asteroidMonitoringStationsList
                    .stream()
                    .filter(AsteroidMonitoringStations-> AsteroidMonitoringStations.designation
                            .contains(String.valueOf(year)))
                    .collect(Collectors.toList());*/

            for(int i=0; i<asteroidMonitoringStationsList.size(); i++){
                if (asteroidMonitoringStationsList.get(i).discoveryDate
                        .contains(String.valueOf(year)) && pha.equals(asteroidMonitoringStationsList.get(i).pha)) {
                    if(asteroidMonitoringStationsList.get(i).designation != null) {
                        response1.add(asteroidMonitoringStationsList.get(i).designation);
                       // System.out.println(responseString[i]);
                    }
                }
            }
      //      Arrays.asList(response1).stream().filter(asteroidMonitoringStations-> asteroidMonitoringStations!=null).forEach(asteroidMonitoringStations -> System.out.println(asteroidMonitoringStations));
            return totalPages == page ? response1.toArray(new String[0]): getAsteroidMonitoringStation(year, pha,page+1,asteroidMonitoringStationsList,response1);
           //         highestStudentsInCity1,highestStudentsInCity2, university1,university2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{"0"};
    }

    static class AsteroidMonitoringStations implements Comparator<AsteroidMonitoringStations> {
        String designation;
        String periodYr;
        String discoveryDate;
        String pha;
        AsteroidMonitoringStations(){}

        AsteroidMonitoringStations(String designation, String periodYr, String discoveryDate, String pha) {
            this.designation = designation;
            this.periodYr = periodYr;
            this.discoveryDate = discoveryDate;
            this.pha = pha;
        }



        @Override
        public int compare(AsteroidMonitoringStations o1, AsteroidMonitoringStations o2) {
            if (o1.periodYr.equalsIgnoreCase(o2.periodYr)) {
                return o1.designation.compareTo(o2.designation);
            }
            return o1.periodYr.compareTo(o2.periodYr);
        }
    }

    static String getAsteroidMonitoringStationPerPage(String endpoint,String keyword, int page) throws Exception{
        URL url = new URL(endpoint+"?parameter="+keyword+"&page="+page);
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
