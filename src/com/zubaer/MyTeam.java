package com.zubaer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyTeam {
    private Map<String, Player> player = new HashMap<>();
    //private static int teamPoint;
    private String teamName;

    public MyTeam(String teamName) {
        this.teamName = teamName;
    }

    public void calculateAll() {
        playerListRead();
        runCalculate();
        wicketCalculate();
        catchCalculate();
        extraPoint();
        pointWrite();
    }

    public void playerListRead() {

        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader("Player_in_all_team.csv"));
            scanner.useDelimiter(";");
            while (scanner.hasNextLine()) {
                String name = scanner.next().trim();
                String role = scanner.nextLine().trim();
                Player createPlayer = new Player(name, role);
                player.put(name, createPlayer);
                //System.out.println(createPlayer);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }


    public void runCalculate() {

        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader("ScoreCard.csv"));
            while (scanner.hasNextLine()) {
                String[] line1 = scanner.nextLine().split(";");//run info
                if (line1.length == 6) {
                    String name = line1[0].trim();

                    for (String key : player.keySet()) {
                        if (name.trim().equalsIgnoreCase(key.trim())) {
                            double run = Double.parseDouble(line1[1].trim());
                            double strikeRate = Double.parseDouble(line1[5].trim());
                            double fours = Double.parseDouble(line1[3].trim());
                            double sixes = Double.parseDouble(line1[4].trim());
                            Player player1 = player.get(name.trim());
                            //double strikeRateBonus=0;
                            try {
                                if (run == 0) {
                                    player1.addPoint(-2);
                                } else {
                                    player1.addPoint(run / 5);
                                    player1.addPoint(fours * 1 + sixes * 1.5);

                                    int multiplier = 10;
                                    int count = 0;
                                    int milestoneBonus = 0;
                                    for (int i = 50; i <= run; i = i + 50) {
                                        count++;
                                    }
                                    for (int i = 1; i < count; i++) {
                                        multiplier += 5;
                                    }
                                    milestoneBonus = count * multiplier;
                                    player1.addPoint(milestoneBonus);
                                }
                                if (run > 50 && strikeRate > 120) {
                                    player1.addPoint(5);
                                }
                                if (run > 100 && strikeRate > 120) {
                                    player1.addPoint(10);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

    }

    public void wicketCalculate() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader("ScoreCard.csv"));

            while (scanner.hasNextLine()) {
                String[] line1 = scanner.nextLine().split(";");

                if (line1.length == 8) {
                    String name = line1[0].trim();
                    for (String key : player.keySet()) {
                        if (name.trim().equalsIgnoreCase(key.trim())) {//ignore case !!! try russel/Russel
                            int wicket = Integer.parseInt(line1[4].trim());
                            int maindenOver = Integer.parseInt(line1[2].trim());
                            Player player1 = player.get(name);
                            int wicketPoint = maindenOver * 5;
                            if (wicket < 5) {
                                wicketPoint = wicket * 10;
                            }
                            if (wicket >= 5) {
                                wicketPoint += (25 + (5 * 10) + (wicket - 5) * 15);
                            }
                            player1.addPoint(wicketPoint);
                            if ((Double.parseDouble(line1[7]) <= 3.5) && (Double.parseDouble(line1[1])>=3)) {
                                player1.addPoint(10);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public void catchCalculate() {
        Scanner scanner2 = null;

        try {
            scanner2 = new Scanner(new FileReader("ScoreCard.csv"));
            while (scanner2.hasNextLine()) {
                String str = "";
                String[] line1 = scanner2.nextLine().split(";");
                if (line1.length == 1) {
                    String[] info = line1[0].trim().split(" ");
                    int length = info.length;
                    //c
                    if ((info[0].trim().equalsIgnoreCase("c") ||
                            info[0].trim().equalsIgnoreCase("st")) && !info[1].trim().equalsIgnoreCase("&")) {
                        for (int i = 1; i < length; i++) {
                            if (!(info[i].trim().equalsIgnoreCase("b"))) {
                                str = str + " " + info[i].trim();
                            } else if ((info[i].trim().equalsIgnoreCase("b"))) {
                                break;
                            }
                        }
                    }
                    //c & b
                    if ((info[0].trim().equalsIgnoreCase("c") ||
                            info[0].trim().equalsIgnoreCase("st")) && info[1].trim().equalsIgnoreCase("&")) {
                        for (int i = 3; i < length; i++) {
                            str = str + " " + info[i].trim();
                        }
                    }
                    //run out:
                    if (info[0].trim().equalsIgnoreCase("run")) {
                        for (int i = 2; i < length; i++) {
                            str = str + " " + info[i].trim();
                        }

                        str = str.replace("(", "");
                        str = str.replace(")", "");
                    }
                }
                //System.out.println(str);
                //catch info comes without (c)/WK
                String str2 ="";
                String str3 ="";
                String str4 ="";
                if (str.length()>0){
                    str2 = str+" "+"(W)";
                    str3 = str+" "+"(C)";
                    str4 = str+" "+"(C)"+" "+"(W)";
                }


                for (String key : player.keySet()) {
                    if (str.trim().equalsIgnoreCase(key.trim()) || str2.trim().equalsIgnoreCase(key.trim())
                         || str3.trim().equalsIgnoreCase(key.trim()) || str4.trim().equalsIgnoreCase(key.trim())) {
                        String foundName = key.trim();
                        Player player1 = player.get(foundName);
                        //except wK:
                        if (!player1.getPlayerRole().trim().equals("WK")) {
                            player1.addPoint(2);
                        } else {
                            player1.addPoint(10);
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner2 != null) {
                scanner2.close();
            }
        }
    }

    public void manualInput(String playerName, int point) {
        for (String key : player.keySet()) {
            if (playerName.trim().equalsIgnoreCase(key.trim())) {
                Player player1 = player.get(playerName);
                player1.addPoint(point);
            }
        }

    }

    public void extraPoint() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader("ScoreCard.csv"));
            while (scanner.hasNextLine()) {
                String[] line1 = scanner.nextLine().split(";");//run info
                if (line1.length == 2 && line1[0].trim().equalsIgnoreCase("MOM")) {
                    String name = line1[1].trim();

                    for (String key : player.keySet()) {
                        if (name.trim().equalsIgnoreCase(key.trim())) {
                            Player player1 = player.get(name.trim());
                            player1.addPoint(50);
                        }
                    }
                }
                if (line1.length == 2 && line1[0].trim().equalsIgnoreCase("Hattric")) {
                    String name = line1[1].trim();

                    for (String key : player.keySet()) {
                        if (name.trim().equalsIgnoreCase(key.trim())) {

                            Player player1 = player.get(name.trim());
                            player1.addPoint(10);
                        }
                    }
                }
                if (line1.length == 2 && line1[0].trim().equalsIgnoreCase("MOT")) {
                    String name = line1[1].trim();

                    for (String key : player.keySet()) {
                        if (name.trim().equalsIgnoreCase(key.trim())) {

                            Player player1 = player.get(name.trim());
                            player1.addPoint(200);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public void pointWrite() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //System.out.println(formatter.format(calendar.getTime()));
        String date = formatter.format(calendar.getTime());
        //String date ="28-05-2019";

        File file = new File("Generated point sheets/Points");
        FileWriter fw = null;

        //File file2 = new File("point sheets/PointsAll");
        //FileWriter fw2 = null;


        try {
            fw = new FileWriter(file + "_" + date + ".csv");
            //fw2 = new FileWriter(file2+".csv");

            for (Player player : player.values()) {
                //teamPoint += player.getTotalPoint();
                System.out.println("player: " + player.getPlayerName().trim() + " gives " + player.getTotalPoint() + " points");
                //fw.append(("player: " + player.getPlayerName() + " gives " + player.getTotalPoint() + " points ") + ';'+"\n");
                fw.append(player.getPlayerName());
                fw.append(";");
                fw.append(String.valueOf(player.getTotalPoint()));
                fw.append("\n");

                System.out.println();
            }

            //fw.append("team points:" + "\n");
            //fw.append(teamPoint + "\n");
            //fw.append("----------------------" + "\n");
            fw.close();
            //fw2.append("team points:" + "\n");
            //fw2.append(teamPoint + "\n");
            //fw2.append("----------------------" + "\n");
            //fw2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public Map<String, Player> getPlayer() {
//        return player;
//    }
//
//    public static int getTeamPoint() {
//        return teamPoint;
//    }
//
//    public String getTeamName() {
//        return teamName;
//    }
}