package sample;

import java.io.Serializable;
import java.util.Vector;

public class DataBaseWriter implements Serializable {
    public Vector<PlantInfo> plants = new Vector<PlantInfo>();
    public Vector<Tag> tags = new Vector<Tag>();
}
