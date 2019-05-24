package com.zubaer;

public class Main {

    public static void main(String[] args) {
        MyTeam myTeam = new MyTeam("Team_xubaer");
        myTeam.playerListRead();
        System.out.println("attention !!");
        System.out.println("you can add manual point by calling manualPointInput method: "+"\n");
        myTeam.pointCalculate();
        myTeam.pointWrite();
        //myTeam.manualPointInput();

    }
}
