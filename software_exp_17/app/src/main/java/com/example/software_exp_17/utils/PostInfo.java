package com.example.software_exp_17.utils;

import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostInfo {
    private String totaltime;
    private String plan;
    private String contents;
    private String publisher;
    private Date createdAt;
    private ArrayList<String> heart;
    private long heartcount;
    private String id;

    public PostInfo(String totaltime, String plan, String contents, String publisher, Date createdAt, ArrayList<String> heart, long heartcount){
        this.totaltime = totaltime;
        this.plan = plan;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.heart = heart;
        this.heartcount = heartcount;
    }

    public PostInfo(String totaltime, String plan, String contents, String publisher, Date createdAt, ArrayList<String> heart, long heartcount, String id){
        this.totaltime = totaltime;
        this.plan = plan;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.heart = heart;
        this.heartcount = heartcount;
        this.id = id;
    }

    public String getTotaltime(){
        return this.totaltime;
    }
    public void setTotaltime(String totaltime){
        this.totaltime = totaltime;
    }

    public String getPlan(){
        return this.plan;
    }
    public void setPlan(String plan){
        this.plan = plan;
    }

    public String getContents(){
        return this.contents;
    }

    public void setContents(String contents){
        this.contents = contents;
    }

    public String getPublisher(){
        return this.publisher;
    }

    public void setPublisher(String publisher){
        this.publisher = publisher;
    }

    public Date getCreatedAt(){
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }

    public ArrayList<String>  getHeart(){
        return this.heart;
    }

    public void setHeart(ArrayList<String> heart){
        this.heart = heart;
    }

    public long getHeartcount(){
        return this.heartcount;
    }

    public void setHeartcount(long heartcount){
        this.heartcount = heartcount;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

}