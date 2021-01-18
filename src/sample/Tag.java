package sample;

import java.io.Serializable;
import java.util.Vector;

public class Tag implements Serializable {
    public String name;
    public String question;
    public boolean isPrimary;
    public Vector<String> holders = new Vector<String>();

    public Tag(){}
    public Tag(String in){
        name = in;
    }

    @Override
    public String toString() {
        return name;
    }
    public boolean equals(String s){
        return s.equals(name);
    }
}
