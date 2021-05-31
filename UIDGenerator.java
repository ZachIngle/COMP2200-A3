// UIDGenerator.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021

import java.util.Random;
import java.util.ArrayList;

public class UIDGenerator {
    private static UIDGenerator instance = new UIDGenerator();

    private static ArrayList<String> IDList = new ArrayList<String>();
    private static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static int IDLength = 16;
    private static Random gen = new Random();

    private UIDGenerator() {}

    public static UIDGenerator getInstance() {
        return instance;
    }

    public String getID() {
        char[] newID = new char[IDLength];

        String ID = null;
        while (ID == null || IDList.contains(ID)) {
            for (int i = 0; i < IDLength; i++) {
                newID[i] = characters.charAt(gen.nextInt(characters.length()));
            }
            
            ID = new String(newID);
        }

        IDList.add(ID);
        return ID;
    }
}
