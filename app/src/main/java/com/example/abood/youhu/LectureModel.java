package com.example.abood.youhu;

/**
 * Created by Abood on 4/15/2017.
 */



/**
 * Created by hisham on 9/6/2015.
 */
public class LectureModel {
    private String menu;
    private int id;
    private String video;
    private String name;


    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getName() {
        name.replaceAll("\\.mp4"," ");
        return name;
    }

    public void setName(String name)
    {this.name = name;
    }

}