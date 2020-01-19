package com.vanlanduytsr.hashscrambler;

import android.content.Context;

public class HashTagger {

    private String outputString;
    private String[] hashedStringArray;
    private Context context;
    private SettingsManager sm;

    public HashTagger(Context context) {
        this.context = context;
    }

    public String addHashesFromGroup(Group detailGroup) {
        sm = new SettingsManager(context);
        hashedStringArray = new String[detailGroup.getListArray().length];
        outputString = "";
        hashedStringArray = detailGroup.listString.trim().split(" ");
        int start;

        if (hashedStringArray.length < 1) {
            outputString = "";
        } else {
            if (sm.getParagraphSpacing()) {
                start = 1;
            } else {
                start = 0;
            }

            for (int i = start; i < hashedStringArray.length; i++) {
                if (!hashedStringArray[i].startsWith("#") && hashedStringArray[i] != "") {
                    outputString += "#";
                    outputString += hashedStringArray[i] + " ";
                } else {
                    outputString += hashedStringArray[i] + " ";
                }
            }
        }
        return  outputString;
    }

    public String addHashesFromString(String inputString) {
        sm = new SettingsManager(context);
        outputString = "";
        hashedStringArray = inputString.trim().split(" ");
        int start;

        if (hashedStringArray.length < 1) {
            outputString = "";
        } else {
            if (sm.getParagraphSpacing()) {
                outputString += hashedStringArray[0];
                start = 1;
            } else {
                start = 0;
            }

            for (int i = start; i < hashedStringArray.length; i++) {
                if (!hashedStringArray[i].startsWith("#") && hashedStringArray[i] != "") {
                    outputString += "#";
                    outputString += hashedStringArray[i] + " ";
                } else {
                    outputString += hashedStringArray[i] + " ";
                }
            }
        }
        return outputString;
    }
}
