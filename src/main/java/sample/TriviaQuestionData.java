package sample;

import java.io.Serializable;
import java.util.Vector;

public class TriviaQuestionData implements Serializable {
    public String name;
    public String question;
    public String correctAnswer;
    public Vector<String> wrongAnswers = new Vector<String>();

    public TriviaQuestionData(String in){
        name = in;
    }
    public TriviaQuestionData(){
    }
    public String toString(){
        return name;
    }
}
