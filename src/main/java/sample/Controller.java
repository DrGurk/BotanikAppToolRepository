package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;

//import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;


public class Controller {
    public Controller(){}
    @FXML
    Button newPlant;
    @FXML
    Button tagAdd;
    @FXML
    Button removePlant;
    @FXML
    Button tagRemove;
    @FXML
    Button newQuestion;
    @FXML
    Button removeQuestion;
    @FXML
    Button savePlant;
    @FXML
    Button newImage;
    @FXML
    Button removeImage;
    @FXML
    Button updateTag;

    @FXML
    TextArea descArea;
    @FXML
    TextArea tagArea;
    @FXML
    TextArea answers;
    @FXML
    TextArea tagQuestionArea;

    @FXML
    ListView<PlantInfo> plantSelect;

    @FXML
    ChoiceBox imageSelector;
    @FXML
    ChoiceBox<TriviaQuestionData> questionSelect;

    @FXML
    TextField question;
    @FXML
    TextField newPlantText;
    @FXML
    TextField newQuestionText;
    @FXML
    TextField tagAddText;

    @FXML
    ListView<Tag> tagList;
    @FXML
    CheckListView<Tag> tagCheckList;

    @FXML
    Button saveQuestion;

    @FXML
    ImageView image;
    @FXML
    MenuItem scanImages;
    @FXML
    MenuItem menuOpen;
    @FXML
    MenuItem menuSave;
    @FXML
    MenuItem menuValidate;
    @FXML
    MenuItem menuExit;
    @FXML
    MenuItem menuAndroid;

    @FXML
    public void initialize(){
        plantSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PlantInfo>() {

            @Override
            public void changed(ObservableValue<? extends PlantInfo> observable, PlantInfo oldValue, PlantInfo newValue) {
                if(newValue!=null) {
                    loadPlant(newValue);
                    updateQuestionList(newValue);
                    System.out.println("Selected item: " + newValue);
                }
            }
        });
        tagList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tag>() {

            @Override
            public void changed(ObservableValue<? extends Tag> observable, Tag oldValue, Tag newValue) {
                if(newValue!=null) {
                    loadTag(newValue);
                    System.out.println("Selected item: " + newValue);
                }
            }
        });
        imageSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue!=null) {
                    loadImage(newValue);
                    System.out.println("Selected image: " + newValue);
                }
            }
        });
        questionSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TriviaQuestionData>(){
            @Override
            public void changed(ObservableValue<? extends TriviaQuestionData> observable, TriviaQuestionData oldValue, TriviaQuestionData newValue) {
                if(newValue!=null) {
                    loadQuestion(newValue);
                    System.out.println("Selected image: " + newValue);
                }
            }
        });
        EventHandler<ActionEvent> eventNewPlant = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                String in = newPlantText.getText();
                if(in.equals("")){
                    System.out.println("Nichts eingegeben!");
                    return;
                }
                if(DatabaseLoader.duplicatePlantCheck(in)){
                    System.out.println("Name bereits vergeben!");
                    return;
                }
                DatabaseLoader.plants.add(new PlantInfo(in));
                updatePlantList();
                System.out.println(in + " erfolgreich hinzugefuegt!");
                System.out.println(DatabaseLoader.plants.size());
            }
        };
        newPlant.setOnAction(eventNewPlant);
        EventHandler<ActionEvent> eventRemovePlant = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                PlantInfo pi = plantSelect.getSelectionModel().getSelectedItem();
                if(pi == null){
                    return;
                }
                int index = DatabaseLoader.getPlantIndex(pi.name);
                if(index != -1){
                    DatabaseLoader.plants.remove(index);
                    System.out.println("Pflanze geloescht!");
                }
                updatePlantList();
            }
        };
        removePlant.setOnAction(eventRemovePlant);
        EventHandler<ActionEvent> eventSavePlant = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                PlantInfo pi = plantSelect.getSelectionModel().getSelectedItem();
                if(pi!= null) {
                    savePlant(pi);
                    updatePlantList();
                }
            }
        };
        savePlant.setOnAction(eventSavePlant);

        EventHandler<ActionEvent> eventRemoveTag = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Tag t = tagList.getSelectionModel().getSelectedItem();
                if(t == null){
                    return;
                }
                int index = DatabaseLoader.getTagIndex(t.name);
                if(index != -1){
                    DatabaseLoader.tags.remove(index);
                    System.out.println("Tag geloescht!");
                }
                updateTagList();
            }
        };
        tagRemove.setOnAction(eventRemoveTag);

        EventHandler<ActionEvent> eventNewTag = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                String in = tagAddText.getText();
                if(in.equals("")){
                    System.out.println("Nichts eingegeben!");
                    return;
                }
                if(DatabaseLoader.duplicateTagCheck(in)){
                    System.out.println("Name bereits vergeben!");
                    return;
                }
                DatabaseLoader.tags.add(new Tag(in));
                updateTagList();
                System.out.println(in + " erfolgreich hinzugefuegt!");
                System.out.println(DatabaseLoader.tags.size());
            }
        };
        tagAdd.setOnAction(eventNewTag);
        EventHandler<ActionEvent> eventUpdateTag = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if(tagList.getSelectionModel().getSelectedItem() == null){
                    return;
                }
                int id = DatabaseLoader.getTagIndex(tagList.getSelectionModel().getSelectedItem().name);
                if(id == -1){
                    return;
                }
                DatabaseLoader.tags.elementAt(id).question = tagQuestionArea.getText();
            }
        };
        updateTag.setOnAction(eventUpdateTag);
        var toList = FXCollections.observableList(DatabaseLoader.tags);
        tagCheckList.setItems(toList);
        FileChooser fileChooser = new FileChooser();
        newImage.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        PlantInfo pi = plantSelect.getSelectionModel().getSelectedItem();
                        if(pi != null) {
                            Stage stage = (Stage) tagAdd.getScene().getWindow();
                            File file = fileChooser.showOpenDialog(stage);
                            if (file != null) {
                                //plantSelect.getSelectionModel().getSelectedItem().images.add(file.toURI().toString());
                                String str = plantSelect.getSelectionModel().getSelectedItem().name;
                                File plantname = new File("bilder" + File.separator + str);
                                plantname.mkdir();
                                boolean flag = true;
                                int count = 0;
                                while(flag){
                                    File check = new File("bilder" + File.separator + str + File.separator + str + "_" + count);
                                    if (!check.exists()){
                                        flag = false;
                                    } else{
                                        count++;
                                    }
                                }
                                File dest = new File("bilder" + File.separator + str  + File.separator + str + "_" + count);
                                try {
                                    Files.copy(file.toPath(), dest.toPath());
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                plantSelect.getSelectionModel().getSelectedItem().images.add(dest.toPath().toString());
                                updateImageList(pi);
                            }
                        } else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Keine Pflanze ausgewählt!");
                            alert.setHeaderText("Operation nicht moeglich");
                            alert.setContentText("Bitte wähle zuerst eine Pflanze aus!");

                            alert.showAndWait();
                        }
                    }
                });
        newQuestion.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        PlantInfo pi = plantSelect.getSelectionModel().getSelectedItem();
                        if(pi != null) {
                            if(pi.duplicateQuestionCheck(newQuestionText.getText())){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Frage mit diesem Namen existiert bereits!");
                                alert.setHeaderText("Operation nicht moeglich");
                                alert.setContentText("Bitte wähle einen anderen Namen aus!");

                                alert.showAndWait();
                                return;
                            }
                            pi.triviaQuestions.add(new TriviaQuestionData(newQuestionText.getText()));
                            updateQuestionList(pi);
                        } else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Keine Pflanze ausgewählt!");
                            alert.setHeaderText("Operation nicht moeglich");
                            alert.setContentText("Bitte wähle zuerst eine Pflanze aus!");

                            alert.showAndWait();
                        }
                    }
                });
        saveQuestion.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        PlantInfo pi = plantSelect.getSelectionModel().getSelectedItem();
                        if(pi != null) {
                            saveQuestion(questionSelect.getSelectionModel().getSelectedItem());
                        } else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Keine Pflanze ausgewählt!");
                            alert.setHeaderText("Operation nicht moeglich");
                            alert.setContentText("Bitte wähle zuerst eine Pflanze aus!");

                            alert.showAndWait();
                        }
                    }
                });
        menuOpen.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        Stage stage = (Stage) tagAdd.getScene().getWindow();
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            DatabaseLoader.load(file);
                            updatePlantList();
                            updateTagList();
                        }
                    }
                });
        scanImages.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        Stage stage = (Stage) tagAdd.getScene().getWindow();
                        ScanImages();
                    }
                });
        menuSave.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        /*
                        Stage stage = (Stage) tagAdd.getScene().getWindow();
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            DatabaseLoader.load(file);
                        }*/
                        DatabaseLoader.save();
                    }
                });
        menuValidate.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        validate();
                    }
                });
        menuAndroid.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        android();
                    }
                });

    }


    public void updatePlantList(){
        var oList = FXCollections.observableList(DatabaseLoader.plants);
        plantSelect.setItems(oList);
    }
    public void updateTagList(){
        var oList = FXCollections.observableList(DatabaseLoader.tags);
        tagList.setItems(oList);
    }
    public void updateImageList(PlantInfo pi){
        var oList = FXCollections.observableList(pi.images);
        imageSelector.setItems(oList);
    }
    public void updateQuestionList(PlantInfo pi){
        var oList = FXCollections.observableList(pi.triviaQuestions);
        questionSelect.setItems(oList);
    }
    public void loadPlant(PlantInfo pi){
        String tags = "";
        for(String s : pi.tags){
            tags += s + "\n";
        }
        //tagArea.setText(tags);

        var toList = FXCollections.observableList(DatabaseLoader.tags);
        tagCheckList.setItems(toList);
        descArea.setText(pi.description);
        var oList = FXCollections.observableList(pi.images);
        imageSelector.setItems(oList);
        for(String s: pi.tags){
            int tmp = DatabaseLoader.getTagIndex(s);
            tagCheckList.getCheckModel().check(tmp);
        }
        /*
        tagCheckList.getCheckModel().getCheckedItems().addListener(new ListChangeListener() {
            public void onChanged(ListChangeListener.Change c) {
                System.out.println(tagCheckList.getCheckModel().getCheckedItems());
            }
        });
        imageSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //handle it
                System.out.println("Selected item: " + newValue);
            }
        });*/
        updateImageList(pi);
        image.setImage(null);
    }
    public void loadTag(Tag t){
        if(t != null) {
            tagQuestionArea.setText(t.question);
        }
    }
    public void loadQuestion(TriviaQuestionData q){
        question.setText(q.question);
        String tmp = "";
        tmp += q.correctAnswer + "\n";
        for(String s: q.wrongAnswers){
            tmp += s + "\n";
        }
        answers.setText(tmp);

    }
    public void saveQuestion(TriviaQuestionData q){
        if(q == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Keine Frage ausgewählt!");
            alert.setHeaderText("Operation nicht moeglich");
            alert.setContentText("Bitte wähle eine Frage aus!");

            alert.showAndWait();

        }
        q.question = question.getText();
        String[] sarr = answers.getText().split("\n");
        if(sarr.length > 3){
            q.correctAnswer = sarr[0];
            q.wrongAnswers.clear();
            for(int i = 1; i < sarr.length; i++){
                q.wrongAnswers.add(sarr[i]);
            }
        } else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Zu wenig Antworten!");
            alert.setHeaderText("Operation nicht moeglich");
            alert.setContentText("Zu wenig Antworten!");

            alert.showAndWait();
        }
    }
    public void savePlant(PlantInfo pi){
        Vector<Tag> vtags = new Vector<Tag>(tagCheckList.getCheckModel().getCheckedItems());
        pi.tags.clear();
        for(Tag t : vtags){
            pi.tags.add(t.name);
        }
        pi.description = descArea.getText();
    }
    public void loadImage(String f){
        PlantInfo pi = plantSelect.getSelectionModel().getSelectedItem();
        if(pi == null){
            return;
        }
        String str = pi.getFolderName() + f;
        Image img = new Image("file:" +str);
        image.setImage(img);
    }
    public void validate(){
        boolean alertflag = false;
        Vector<String> fails = new Vector<String>();
        Vector<Integer> ints = new Vector<Integer>();
        for(PlantInfo pi: DatabaseLoader.plants){
            for(Tag t: DatabaseLoader.tags){
                ints.add(0);
            }
            for(String s: pi.tags){
                int tmp = DatabaseLoader.getTagIndex(s);
                ints.set(tmp,ints.elementAt(tmp) + 1);
            }


        }
        for(int i = 0; i < ints.size() && i < DatabaseLoader.tags.size(); i++){
            if(ints.elementAt(i).intValue() < 4){
                fails.add(DatabaseLoader.tags.elementAt(i).name);
            }
        }
        if(fails.size() != 0){
            alertflag = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validierung");
            alert.setHeaderText("Es kann Probleme geben");
            String s = unravel(fails, " ");
            alert.setContentText("Die Folgenden Tags haben <4 Pflanzen: " + s);

            alert.showAndWait();
        }
        fails.clear();
        for(PlantInfo pi: DatabaseLoader.plants){
            if(pi.images.size() == 0){
                fails.add(pi.name);
            }
        }
        if(fails.size() != 0) {
            alertflag = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validierung");
            alert.setHeaderText("Es kann Probleme geben");
            String s = unravel(fails, " ");
            alert.setContentText("Die Folgenden Pflanzen haben keine Bilder: " + s);

            alert.showAndWait();
        }
        fails.clear();
        for(PlantInfo pi: DatabaseLoader.plants){
            if(pi.triviaQuestions.size() == 0){
                fails.add(pi.name);
            }
        }
        if(fails.size() != 0) {
            alertflag = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validierung");
            alert.setHeaderText("Es kann Probleme geben");
            String s = unravel(fails, " ");
            alert.setContentText("Die Folgenden Pflanzen haben keine Fragen: " + s);

            alert.showAndWait();
        }
        if(!alertflag){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validierung");
            alert.setHeaderText("Keine Probleme gefunden!");
            alert.setContentText("Die Validierung hat keine Probleme entdeckt!");

            alert.showAndWait();

        }
    }

    public String unravel(Vector<String> in, String delim){
        String out = "";
        for(String s: in){
            out += s + delim;
        }
        return out;
    }
    public void android(){
        commonSetup();
        try {
            String path = new File(".").getCanonicalPath();
            String apath = path + File.separator +"android";
            File fandroid = new File(apath);
            fandroid.mkdir();
            String drawable = apath + File.separator + "drawable";
            String raw = apath + File.separator + "raw";

            copyImages(drawable);
            setupRaw(raw);
        } catch(IOException e){
            System.err.println("IOEXception");
        }
    }
    public void copyImages(String path){
        try {
            File f = new File(path);
            f.mkdir();
            for (PlantInfo pi : DatabaseLoader.plants) {
                int count = 0;
                String s = pi.name + "_";
                for (String img : pi.images) {
                    Path p1 = Paths.get(img.substring(6));
                    Path p2 = Paths.get(path + File.separator + s + count+ ".jpg");
                    Files.copy(p1, p2);
                }
            }
        } catch(IOException e){
            System.err.println("IOEXception copy");
            e.printStackTrace();
        }
    }
    public void commonSetup(){
        for(Tag t: DatabaseLoader.tags){
            t.holders.clear();
            for(PlantInfo pi: DatabaseLoader.plants){
                if(pi.tags.contains(t.name)){
                    t.holders.add(pi.name);
                }
            }
        }
    }
    public void setupRaw(String path){
        try {
            File dir = new File(path);
            dir.mkdir();
            for (PlantInfo pi : DatabaseLoader.plants) {
                int count = 0;
                String s = "questions_" + pi.name;
                String d = "description_" + pi.name;
                FileWriter file = new FileWriter("android" + File.separator + "raw" + File.separator + s);
                for(int i = 0; i < pi.triviaQuestions.size(); ++i){
                    TriviaQuestionData q = pi.triviaQuestions.elementAt(i);
                    file.write(q.question + "\n");
                    file.write(q.correctAnswer + "\n");
                    for(String str: q.wrongAnswers){
                        file.write(str + "\n");
                    }
                    if(i != pi.triviaQuestions.size() - 1){
                        file.write("Q\n");
                    }
                }
                file.close();
                file = new FileWriter("android" +  File.separator + "raw" + File.separator + d);
                file.write(pi.description);
                file.close();
            }
            FileWriter file2 = new FileWriter("android" + File.separator + "raw" + File.separator + "tags");
            for(Tag t: DatabaseLoader.tags){
                file2.write(t.name + "\n");
                String s = "tag_" +t.name.toLowerCase();
                FileWriter file = new FileWriter("android" + File.separator + "raw" + File.separator + s);
                file.write(t.question + "\n");
                for(String hold: t.holders){
                    file.write(hold + "\n");
                }
                file.close();
            }
            file2.close();
        } catch(IOException e){
            System.err.println("IOEXception  raw");
            e.printStackTrace();
        }
    }
    void ScanImages(){

        File images = new File("bilder");
        images.mkdir();
        String[] directories = images.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for(String s : directories){
            File subdirs = new File("bilder" + File.separator + s);
            int id = DatabaseLoader.getPlantIndex(s);
            File[] imageList = subdirs.listFiles();
            if(id == -1){
                return;
            }
            DatabaseLoader.plants.elementAt(id).images.clear();
            int count = 0;
            String tmpstr = "bilder" + File.separator + s + File.separator + "tmp";
            File tmp = new File(tmpstr);
            tmp.mkdir();
            for(File imgs : imageList){
                try {
                    String extension = "";

                    int i = imgs.toPath().toString().lastIndexOf('.');
                    if (i > 0) {
                        extension = imgs.toPath().toString().substring(i);
                    }
                    Files.move(imgs.toPath(), new File(tmpstr + File.separator + s + "_" + count++ + extension).toPath());
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            imageList = tmp.listFiles();
            count = 0;
            for(File imgs : imageList){
                String extension = "";
                try {


                    int i = imgs.getName().lastIndexOf('.');
                    if (i > 0) {
                        extension = imgs.getName().substring(i);
                    }
                    Files.move(imgs.toPath(), new File("bilder" + File.separator + s + File.separator + s + "_" + count + extension).toPath());
                    DatabaseLoader.plants.elementAt(id).images.add(s + "_" + count++ + extension);
                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }
            tmp.delete();
        }

    }

}
