package model;

import java.util.HashMap;

public class Month {
    private int month;
    private int m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11;
    private HashMap<Integer, String> map2letters;

    public Month(int month){
        this.month = month;

        if(this.month == 1){
            this.m1 = 12;
        }else{
            this.m1 = month - 1;
        }

        if(this.m1 == 1){
            this.m2 = 12;
        }else{
            this.m2 = m1 - 1;
        }

        if(this.m2 == 1){
            this.m3 = 12;
        }else{
            this.m3 = m2 - 1;
        }

        if(this.m3 == 1){
            this.m4 = 0;
        }else{
            this.m4 = m3 - 1;
        }

        if(this.m4 == 1){
            this.m5 = 12;
        }else{
            this.m5 = m4 - 1;
        }

        if(this.m5 == 1){
            this.m6 = 12;
        }else{
            this.m6 = m5 - 1;
        }

        if(this.m6 == 1){
            this.m7 = 12;
        }else{
            this.m7 = m6 - 1;
        }

        if(this.m7 == 1){
            this.m8 = 12;
        }else{
            this.m8 = m7 - 1;
        }

        if(this.m8 == 1){
            this.m9 = 12;
        }else{
            this.m9 = m8 - 1;
        }

        if(this.m9 == 1){
            this.m10 = 12;
        }else{
            this.m10 = m9 - 1;
        }

        if(this.m10 == 1){
            this.m11 = 12;
        }else{
            this.m11 = m10 - 1;
        }

        this.map2letters = new HashMap<>();
        map2letters.put(1, "Jan");
        map2letters.put(2, "Feb");
        map2letters.put(3, "Mar");
        map2letters.put(4, "Apr");
        map2letters.put(5, "May");
        map2letters.put(6, "Jun");
        map2letters.put(7, "Jul");
        map2letters.put(8, "Aug");
        map2letters.put(9, "Sep");
        map2letters.put(10, "Oct");
        map2letters.put(11, "Nov");
        map2letters.put(12, "Dec");

    }

    public String current(){
        return String.valueOf(month);
    }

    public String current2letters(){
        return map2letters.get(month);
    }

    public String previous(int i) {
        switch(i){
            case 1:
                return String.valueOf(m1);
            case 2:
                return String.valueOf(m2);
            case 3:
                return String.valueOf(m3);
            case 4:
                return String.valueOf(m4);
            case 5:
                return String.valueOf(m5);
            case 6:
                return String.valueOf(m6);
            case 7:
                return String.valueOf(m7);
            case 8:
                return String.valueOf(m8);
            case 9:
                return String.valueOf(m9);
            case 10:
                return String.valueOf(m10);
            case 11:
                return String.valueOf(m11);
        }
        return null;
    }

    public String previous2letters(int i) {
        switch(i){
            case 1:
                return map2letters.get(m1);
            case 2:
                return map2letters.get(m2);
            case 3:
                return map2letters.get(m3);
            case 4:
                return map2letters.get(m4);
            case 5:
                return map2letters.get(m5);
            case 6:
                return map2letters.get(m6);
            case 7:
                return map2letters.get(m7);
            case 8:
                return map2letters.get(m8);
            case 9:
                return map2letters.get(m9);
            case 10:
                return map2letters.get(m10);
            case 11:
                return map2letters.get(m11);
        }
        return null;
    }
}
