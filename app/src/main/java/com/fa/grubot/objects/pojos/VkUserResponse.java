package com.fa.grubot.objects.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VkUserResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;

    /**
     * No args constructor for use in serialization
     *
     */
    public VkUserResponse() {
    }

    /**
     *
     * @param id
     * @param lastName
     * @param firstName
     */
    public VkUserResponse(Integer id, String firstName, String lastName) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}