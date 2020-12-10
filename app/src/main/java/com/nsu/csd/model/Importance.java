package com.nsu.csd.model;

public class Importance {

    public enum evIm{LOW,MIDDLE, HEIGHT}

    public static String getImp(evIm e){
        switch (e){
            case LOW: return "LOW";
            case MIDDLE:return "MIDDLE";
            case HEIGHT:return "HEIGHT";
        }
        return "";
    }

}
