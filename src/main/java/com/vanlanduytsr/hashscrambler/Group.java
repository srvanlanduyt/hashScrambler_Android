package com.vanlanduytsr.hashscrambler;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


@Entity(tableName = "group_table")
public class Group implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "list_string")
    public String listString;

    @ColumnInfo(name = "num_times_used")
    public int numTimesUsed;

    @ColumnInfo(name = "is_favorite")
    public boolean isFavorite;

    @ColumnInfo(name = "motifiedOn")
    public Date modifiedOn;

    @ColumnInfo(name = "lastUsed")
    public Date lastUsed;


    public Group(String name, String listString) {
        this.name = name;
        this.listString = cleanString(listString);
        this.numTimesUsed = 0;
        this.isFavorite = false;
        this.modifiedOn = Calendar.getInstance().getTime();
        this.lastUsed = Calendar.getInstance().getTime();
    }

    @Ignore
    public Group () {}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListString(String listString) {
        this.listString = listString;
    }

    public void setIsFavorite(boolean set) {
        this.isFavorite = set;
    }

    public void setNumTimesUsed(int numTimesUsed) {
        this.numTimesUsed = numTimesUsed;
    }

    public void setLastUsed(Date date) {
        this.lastUsed = date;
    }

    public void setModifiedOn(Date date) {
        this.modifiedOn = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getListString() {
        return listString;
    }

    public int getNumTimesUsed() {
        return numTimesUsed;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public String getLastUsed() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        return dateFormat.format(lastUsed);
    }

    public String getModifiedOn() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        return dateFormat.format(modifiedOn);
    }

    public String[] getListArray() {
        return cleanArray(makeDetailArray(listString));
    }







    private String[] makeDetailArray(String list) {
        return list.split(" ");
    }

    private String[] cleanArray(String[] array) {
        ArrayList<String> tempArray = new ArrayList<>();
        String pattern = "[^A-Za-z0-9]+";

        for (int i = 0; i < array.length; i++) {
            tempArray.add(i, array[i]);
        }

        for (int i = tempArray.size() - 1; i >= 0; i--) {
            if (tempArray.get(i).equalsIgnoreCase("") || tempArray.get(i).equalsIgnoreCase(" ")) {
                tempArray.remove(tempArray.get(i));
            }
        }

        for (int i = 0; i < tempArray.size(); i++) {
            String tempString = tempArray.get(i);
            tempArray.remove(i);
            tempArray.add(i, tempString.replaceAll(pattern, ""));
        }

        for (int i = 0; i < tempArray.size(); i++) {
            if (tempArray.get(i).equalsIgnoreCase("") || tempArray.get(i).equalsIgnoreCase(" ")) {
                tempArray.remove(i);
            }
        }

        String[] outputArray = new String[tempArray.size()];

        for (int i = 0; i < tempArray.size(); i++) {
            outputArray[i] = tempArray.get(i);
        }
        return outputArray;
    }

    public String cleanString(String toClean) {
        String[] tempArray = cleanArray(makeDetailArray(toClean));
        String returnString = "";
        for (int i = 0; i < tempArray.length; i++) {
            returnString += tempArray[i] + " ";
        }

        return returnString;

    }


}
