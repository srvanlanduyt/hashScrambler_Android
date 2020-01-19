package com.vanlanduytsr.hashscrambler;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

public class Scrambler {

    private Group detailGroup;
    private ArrayList<Integer> intList;
    private ArrayList<Integer> outOrder;
    private String outputString;
    private int requestedItems;
    private String[] groupArray;
    private HashTagger ht;
    private Random random;

    public Scrambler(Group detailGroup, int requestedItems) {
        this.detailGroup = detailGroup;
        this.requestedItems = requestedItems;
    }

    public String scramble(Context context) {
        ht = new HashTagger(context);
        outputString = "";
        groupArray = detailGroup.getListArray();
        intList = new ArrayList<>();
        outOrder = new ArrayList<>();
        random = new Random();

        for (int i = 0; i < groupArray.length; i++) {
            intList.add(i, i);
        }

        while(intList.size() != 0 && outOrder.size() != requestedItems) {
            for (int i = 0; i < requestedItems; i++) {
                int randomInt = random.nextInt(intList.size());
                outOrder.add(intList.get(randomInt));
                intList.remove(randomInt);
            }
        }

        for (int i = 0; i < outOrder.size(); i++) {
            outputString += groupArray[outOrder.get(i)] + " ";
        }

        return outputString;
    }




}
