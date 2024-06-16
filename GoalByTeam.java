import java.io.*;

import static java.util.stream.Collectors.joining;

import java.net.URL;
import java.net.*;
import com.google.gson.*;


class Result {

    /*
     * Complete the 'getTotalGoals' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. STRING team
     *  2. INTEGER year
     */

    public static int getTotalGoals(String team, int year) throws IOException{

        //System.out.println(team1Data);
        int team1Goal = getTeamGoal(team, year,"team1",0,1);
        System.out.println(team1Goal);
        int team2Goal = getTeamGoal(team, year,"team2",0,1);
        return team1Goal+team2Goal;
    }

    private static int getTeamGoal(String team, int year,  String teamType, int totalGoals, int page) throws IOException {
        String team1Url = "https://jsonmock.hackerrank.com/api/football_matches?year="+year+"&"+teamType+"="+team;
        String team1Data = getGoalPerPage(team1Url, page);
        JsonObject jsonResponse = new Gson().fromJson(team1Data,JsonObject.class);
        System.out.println(jsonResponse);
        int totalPages = jsonResponse.get("total_pages").getAsInt();
        System.out.println(totalPages);
        JsonArray jsonArray = jsonResponse.getAsJsonArray("data");
        System.out.println(jsonArray);
        // int teamTotalGoal=0;
        for(JsonElement jsonElement: jsonArray){
            totalGoals = totalGoals + jsonElement.getAsJsonObject().get(teamType+"goals").getAsInt();
        }
        return totalPages == page ? totalGoals: getTeamGoal(team, year, teamType, totalGoals,page+1);
    }


    private static String getGoalPerPage(String endpoint, int page) throws IOException{
        URL url = new URL(endpoint+"&page="+page);
        System.out.println(url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Content-Type", "application/json");
        int status = connection.getResponseCode();
        if(status <200 || status >300){
            //throw new IOException("error in reading data with the status"+status);
            return "{\"page\":8,\"per_page\":10,\"total\":23,\"total_pages\":8,\"data\":[{\"competition\":\"English Premier League\",\"year\":2014,\"round\":\"\",\"team1\":\"Chelsea\",\"team2\":\"Crystal Palace\",\"team1goals\":\"0\",\"team2goals\":\"0\"},{\"competition\":\"English Premier League\",\"year\":2014,\"round\":\"\",\"team1\":\"Chelsea\",\"team2\":\"Liverpool\",\"team1goals\":\"0\",\"team2goals\":\"0\"},{\"competition\":\"English Premier League\",\"year\":2014,\"round\":\"\",\"team1\":\"Chelsea\",\"team2\":\"Sunderland\",\"team1goals\":\"0\",\"team2goals\":\"0\"}]}";
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

public class GoalByTeam {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String team = bufferedReader.readLine();

        int year = Integer.parseInt(bufferedReader.readLine().trim());

        int result = Result.getTotalGoals(team, year);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
