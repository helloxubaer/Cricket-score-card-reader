package com.zubaer;

import java.io.*;
import java.util.*;

public class MyTeam {
    private Map<String, Player> player = new HashMap<>();
    private static int teamPoint;
    private String teamName;

    public MyTeam(String teamName) {
        this.teamName = teamName;
    }

    public void calculateAll() {
        playerListRead();
        runCalculate();
        wicket();
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
                            int run = Integer.parseInt(line1[1].trim());
                            Player player1 = player.get(name.trim());
                            try {
                                player1.addPoint(run / 5);
                                int multiplier = 10;
                                int count = 0;
                                int bonus = 0;
                                for (int i = 50; i <= run; i = i + 50) {
                                    count++;
                                }
                                for (int i = 1; i < count; i++) {
                                    multiplier += 5;
                                }
                                bonus = count * multiplier;
                                player1.addPoint(bonus);

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

    public void wicket() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader("ScoreCard.csv"));

            while (scanner.hasNextLine()) {
                String[] line1 = scanner.nextLine().split(";");

                if (line1.length == 8) {
                    String name = line1[0].trim();
                    for (String key : player.keySet()) {
                        if (name.trim().equalsIgnoreCase(key.trim())) {
                            int wicket = Integer.parseInt(line1[4].trim());
                            Player player1 = player.get(name);
                            int wicketPoint = 0;
                            if (wicket < 5) {
                                wicketPoint = wicket * 5;
                            }
                            if (wicket >= 5) {
                                wicketPoint += (25 + (5 * 5) + (wicket - 5) * 10);
                            }
                            player1.addPoint(wicketPoint);
                            if (Double.parseDouble(line1[7]) <= 3.5) {
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


                for (String key : player.keySet()) {
                    if (str.trim().equalsIgnoreCase(key.trim())) {
                        Player player1 = player.get(str.trim());
                        //except wK:
                        if (!player1.getPlayerRole().trim().equals("WK")) {
                            player1.addPoint(2);
                        } else {
                            player1.addPoint(5);
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

    public void manualInput(String playerName, int point){
        for (String key : player.keySet()) {
            if (playerName.trim().equalsIgnoreCase(key.trim())) {
                Player player1 = player.get(playerName);
                player1.addPoint(point);
            }
        }
        
    }


    public void pointWrite() {
        File file = new File(teamName.trim());
        FileWriter fw = null;



        try {
            fw = new FileWriter(file + "_points" + ".csv");

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
            fw.append("team points:" + "\n");
            fw.append(teamPoint + "\n");
            fw.append("----------------------" + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
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

    public Map<String, Player> getPlayer() {
        return player;
    }

    public static int getTeamPoint() {
        return teamPoint;
    }

    public String getTeamName() {
        return teamName;
    }
}