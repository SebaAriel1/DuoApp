package com.example.progeria.duoapp.FirebaseObjets;

/**
 * Created by Seba on 11-06-2017.
 */

public class User {

    private String division;
    private String game;
    private String key;
    private String league;
    private String mail;
    private String name;
    private String region;
    private String mainrol;
    private String otherrol;
    private String username;

    public User() {
    }

    public User(String division, String game, String key, String league, String mail, String name, String region, String mainrol, String otherrol, String username) {
        this.division = division;
        this.game = game;
        this.key = key;
        this.league = league;
        this.mail = mail;
        this.name = name;
        this.region = region;
        this.mainrol = mainrol;
        this.otherrol = otherrol;
        this.username = username;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMainRol() {
        return mainrol;
    }

    public void setMainRol(String mainRol) {
        this.mainrol = mainRol;
    }

    public String getOtherRol() {
        return otherrol;
    }

    public void setOtherRol(String otherRol) {
        this.otherrol = otherRol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}