package com.example.csm117.chefinder;

/**
 * Created by Ashwin on 11/9/2017.
 */

public class Group {
    private String groupName;

    public Group(String name) {
        groupName = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String name) {
        groupName = name;
    }

    public String toString() {
        return groupName;
    }

}
