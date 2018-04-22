package com.fa.grubot.objects.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VkMessagePOJO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date")
    @Expose
    private Integer date;
    @SerializedName("out")
    @Expose
    private Integer out;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("read_state")
    @Expose
    private Integer readState;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;

    /**
     * No args constructor for use in serialization
     */
    public VkMessagePOJO() {
    }

    /**
     * @param id
     * @param body
     * @param readState
     * @param title
     * @param userId
     * @param date
     * @param out
     */
    public VkMessagePOJO(Integer id, Integer date, Integer out, Integer userId, Integer readState, String title, String body) {
        super();
        this.id = id;
        this.date = date;
        this.out = out;
        this.userId = userId;
        this.readState = readState;
        this.title = title;
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getOut() {
        return out;
    }

    public void setOut(Integer out) {
        this.out = out;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReadState() {
        return readState;
    }

    public void setReadState(Integer readState) {
        this.readState = readState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}