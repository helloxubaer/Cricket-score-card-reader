package com.zubaer;

import java.util.HashMap;
import java.util.Map;

public class Player {
    //private static Map<String, String> players = new HashMap<>();
    private String playerName;
    private String PlayerRole;
    private  double totalPoint=0;

    public Player(String playerName, String playerRole) {
        this.playerName = playerName;
        PlayerRole = playerRole;

    }

    public double addPoint(double point){
        totalPoint+=point;
        return totalPoint;
    }
    public double showPoint(){
        System.out.println(totalPoint);
        return totalPoint;
    }

    //public static Map<String, String> getPlayer() {
    //    return players;
   // }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerRole() {
        return PlayerRole;
    }

    public  double getTotalPoint() {
        return totalPoint;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + playerName + '\'' +
                ", PlayerRole='" + PlayerRole + '\'' +
                '}';
    }
}

