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

    public void calculateAll(){
        playerListRead();
        pointCalculate();
        manualPointInput();
        pointWrite();
    }

    public void playerListRead() {
        //player.clear();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader(getTeamName()+".csv"));
            scanner.useDelimiter(";");
            while (scanner.hasNextLine()) {
                String name = scanner.next().trim();
                scanner.skip(scanner.delimiter());
                String role = scanner.nextLine().trim();
                Player createPlayer = new Player(name, role);
                player.put(name, createPlayer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }


    public void pointCalculate() {

//search run
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader("ScoreCard.csv"));
            while (scanner.hasNextLine()) {
                String[] line1 = scanner.nextLine().split(";");
                String[] line2 = scanner.nextLine().split(";");//wicket and catch info
                String[] line3 = scanner.nextLine().split(";");
                String name = line1[1].trim();
                System.out.println(name);
                for (String key : player.keySet()) {
                    if (name.trim().equalsIgnoreCase(key.trim())) {
                        int run = Integer.parseInt(line3[1].trim());
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
                for (int i = 0; i < 5; i++) {
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
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

//search wicket:
        try {
            scanner = new Scanner(new FileReader("wicket.csv"));
            scanner.useDelimiter(",");
            while (scanner.hasNextLine()) {
                String[] line1 = scanner.nextLine().split(";");
                String name = line1[0].trim();
                for (String key : player.keySet()) {
                    if (name.trim().equalsIgnoreCase(key.trim())) {
                        int wicket = Integer.parseInt(line1[1].trim());
                        Player player1 = player.get(name);
                        int wicketPoint = 0;
                        if (wicket < 5) {
                            wicketPoint = wicket * 5;
                        }
                        if (wicket >= 5) {
                            wicketPoint += (25 + (5 * 5) + (wicket - 5) * 10);
                        }
                        //System.out.println(wicketPoint);
                        player1.addPoint(wicketPoint);
                        if (Double.parseDouble(line1[2]) <= 3.5) {
                            player1.addPoint(10);
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

//search catch:
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(new FileReader("ScoreCard.csv"));
            while (scanner2.hasNextLine()) {

                String[] line1 = scanner2.nextLine().split(";");
                String[] line2 = scanner2.nextLine().split(";");//catch and bowl info
                String[] info = line2[1].split(" ");
                int length = info.length;
                String str = "";
                if (info[0].trim().equalsIgnoreCase("c") || info[0].trim().equalsIgnoreCase("st")) {
                    for (int i = 1; i < length; i++) {
                        if (!(info[i].trim().equalsIgnoreCase("b"))) {
                            str = str + " " + info[i].trim();
                            str=str.replace("�","");
                        } else if ((info[i].trim().equalsIgnoreCase("b"))) {
                            break;
                        }
                    }
                }
                System.out.println(str);

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
                for (int i = 0; i < 6; i++) {
                    if (scanner2.hasNextLine()) {
                        scanner2.nextLine();
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


    public void pointWrite() {
        File file = new File(teamName.trim());
        FileWriter fw = null;
        int matchCount =0;
        try {
            fw = new FileWriter(file + "_points" + ".csv", true);
            for (Player player : player.values()) {
                teamPoint += player.getTotalPoint();
                System.out.println("player: " + player.getPlayerName().trim() + " gives " + player.getTotalPoint() + " points");
                fw.append(("player: " + player.getPlayerName() + " gives " + player.getTotalPoint() + " points ") + "\n");
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

    public void manualPointInput() {
        Scanner scanner3 = null;

        try {
            scanner3 = new Scanner(new FileReader("ExtraPoint.csv"));
            while (scanner3.hasNextLine()) {
                String[] name = scanner3.nextLine().split(";");
                String catagory = name[0].trim();
                String playerName = name[1].trim();
                for (String key : player.keySet()) {
                    if (playerName.trim().equalsIgnoreCase(key.trim())) {
                        Player player1 = player.get(playerName.trim());
                        if (catagory.equalsIgnoreCase("MOM")) {
                            player1.addPoint(50);
                        } else if (catagory.equalsIgnoreCase("RUN/OUT")) {
                            int numberOfRunOut = Integer.parseInt(name[2]);
                            player1.addPoint(2 * numberOfRunOut);
                        } else if (catagory.equalsIgnoreCase("HATTRIC")) {
                            player1.addPoint(10);
                        } else if (catagory.equalsIgnoreCase("MOT")) {
                            player1.addPoint(100);
                        }
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
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