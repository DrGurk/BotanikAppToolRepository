package sample;

import java.io.File;
import java.io.Serializable;
import java.util.Vector;

public class PlantInfo implements Serializable {
    public String name;
    public String description = "";
    public Vector<String> images = new Vector<String>();
    public Vector<String> tags = new Vector<String>();
    public Vector<TriviaQuestionData> triviaQuestions = new Vector<TriviaQuestionData>();
    public int numImages;

    public PlantInfo(){};
    public PlantInfo(String in){
        name = in;
    }
    public static Vector<PlantInfo> getPlantInfos(Vector<String> in){
        Vector<PlantInfo> out = new Vector<PlantInfo>();
        for(String s: in){
            PlantInfo pi = new PlantInfo();
            String[] split = s.split(",");
            if(split.length == 2){
                pi.name = split[0];
                //pi.primTag.elementAt(0) = split[1];
                out.add(pi);
            }
        }

        return out;
    }

    public void addQuestions(Vector<String> in){
        for(int i = 0; i < in.size() - 4; i++){
            TriviaQuestionData t = new TriviaQuestionData();
            t.question = in.elementAt(i++);
            t.correctAnswer = in.elementAt(i++);
            while((i < in.size()) && !in.elementAt(i).equals("Q")){
                t.wrongAnswers.add(in.elementAt(i++));
            }
            triviaQuestions.add(t);
        }
    }
    public String toString(){
        return name;
    }

    public void setTags(String in){
        System.out.println(in);
        int count = 0;
        String s = "";
        tags.clear();
        String[] arrSplit = in.split("\n");
        for (int i=0; i < arrSplit.length; i++)
        {
            tags.add(arrSplit[i]);
        }
    }

    public boolean duplicateQuestionCheck(String in){
        for(TriviaQuestionData t : triviaQuestions){
            if(in.equals(t.name)){
                return true;
            }
        }
        return false;
    }

    public String getFolderName(){
        return "bilder" + File.separator + name + File.separator;
    }

    /*
    PlantInfo(String in){
        Vector<String> data = FileFinder.readFile("data/primary_tags");
        for(String s: data){
            String[] split = s.split(",");
            if(split.length == 2 && split[0].toLowerCase().equals(in.toLowerCase())){
                name = split[0];
                primTag = split[1];
            }
        }

    }*/
}
