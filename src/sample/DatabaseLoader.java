package sample;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;

public class DatabaseLoader {
    public static String dataDir;
    public static String androidDir;
    public static String iosDir;

    public static boolean unsaved = false;

    public static Vector<PlantInfo> plants = new Vector<PlantInfo>();
    public static Vector<Tag> tags = new Vector<Tag>();

    public static void init(){

    }


    public static Vector<String> readFile(String dir){
        Vector<String> out = new Vector<String>();
        try {
            File myObj = new File(dir);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                out.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred reading file " + dir);
            e.printStackTrace();
        }
        return out;
    }

    public static void writeFile(String dir, String filename, Vector<String> data, boolean clear){
        try {
            FileWriter myWriter = new FileWriter(dir + filename);
            for(String s: data) {
                myWriter.write(s);
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
    public static void createDirectory(String parent, String name){

        try {

            Path path = Paths.get(parent + name + "/");

            //java.nio.file.Files;
            Files.createDirectories(path);

        } catch (IOException e) {

            System.err.println("Failed to create directory!" + e.getMessage());

        }
    }
    public static boolean duplicatePlantCheck(String in){
        for(PlantInfo pi : plants){
            if(in.equals(pi.name)){
                return true;
            }
        }
        return false;
    }
    public static boolean duplicateTagCheck(String in){
        for(Tag t : tags){
            if(in.equals(t.name)){
                return true;
            }
        }
        return false;
    }

    public static int getPlantIndex(String in){
        int count = 0;
        for(PlantInfo pi: plants){
            if(in.equals(pi.name)){
                return count;
            }
            count++;
        }
        return -1;
    }
    public static int getTagIndex(String in){
        int count = 0;
        for(Tag t: tags){
            if(in.equals(t.name)){
                return count;
            }
            count++;
        }
        return -1;
    }

    public static void save(){
        try{
            DataBaseWriter dbw = new DataBaseWriter();
            dbw.plants = plants;
            dbw.tags = tags;
            FileOutputStream fos = new FileOutputStream(new File("datenbank.dat"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dbw);
            oos.close();
        }catch(Exception e){}
    }
    public static void load(File f){
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            DataBaseWriter dbw =(DataBaseWriter) ois.readObject();
            ois.close();
            plants = dbw.plants;
            tags = dbw.tags;
        }catch(Exception e){}
    }

}
