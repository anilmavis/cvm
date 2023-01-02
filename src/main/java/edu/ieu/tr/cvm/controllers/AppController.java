package edu.ieu.tr.cvm.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.ieu.tr.cvm.AcademicCv;
import edu.ieu.tr.cvm.Cv;
import edu.ieu.tr.cvm.Database;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class AppController {
    private Database database;

    @FXML
    private TreeView<Cv> treeView;

    private TreeItem<Cv> root;

    private TreeItem<Cv> academicRoot;

    @FXML
    private Button addButton;

    @FXML
    private TextField textField;

    @FXML
    private MenuItem menuItemHelp;

    @FXML
    private MenuItem menuItemDelete;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private Button removeButton;

    @FXML
    private Accordion filterAccordion;

    // location
    @FXML
    private TitledPane locationTitledPane;
    @FXML
    private VBox locationVBox;

    // skills

    @FXML
    private TitledPane skillsTitledPane;
    @FXML
    private VBox skillsVBox;
    @FXML
    private Button filterGpaButton;

    @FXML
    private TextField lowestGpaTextField;

    @FXML
    private TextField highestGpaTextField;

    @FXML
    private void initialize() throws ClassNotFoundException, SQLException {
        database = Database.getInstance();
        database.open();
        root = new TreeItem<Cv>(new Cv("student", -1, -1, "", "", "", "", "", new HashMap<>(), new HashMap<>(),
                new ArrayList<>()));
        root.setExpanded(true);
        academicRoot = new TreeItem<Cv>(new AcademicCv("academic", -1, -1, "", "", "", "", "", "", new HashMap<>(),
                new HashMap<>(), new HashMap<>(), new ArrayList<>()));
        academicRoot.setExpanded(true);
        treeView.setRoot(new TreeItem<>());
        treeView.getRoot().setExpanded(true);
        treeView.getRoot().getChildren().add(root);
        treeView.getRoot().getChildren().add(academicRoot);
        treeView.setCellFactory(value -> new TreeCell<>() {
            @Override
            protected void updateItem(final Cv cv, final boolean empty) {
                super.updateItem(cv, empty);
                if (cv == null || empty) {
                    setText(null);
                } else {
                    setText(cv.getFullName());
                }
            }
        });
        database.getAll().forEach((cv) -> {
            root.getChildren().add(new TreeItem<Cv>(cv));
        });
        database.getLocations().forEach(s -> locationVBox.getChildren().add(new CheckBox(s)));
        database.getSkills().forEach(s -> skillsVBox.getChildren().add(new CheckBox(s)));
        treeView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                
                Dialog<Cv> d = new Dialog<>();
                DialogPane pane = new DialogPane();
                pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                d.setDialogPane(pane);
                VBox box = new VBox();
                if (treeView.getSelectionModel().getSelectedItem() == null) {return;}
                Cv cv = treeView.getSelectionModel().getSelectedItem().getValue();
                if (root.getValue().equals(cv) || treeView.getSelectionModel().getSelectedItem().getValue() == null) {
                    return;
                }
                if (academicRoot.getValue().equals( cv)) {
                    return;
                }
                TextField fullName = new TextField(cv.getFullName());
                fullName.setPromptText("full name");
                box.getChildren().add(fullName);
                TextField birthYear = new TextField(String.valueOf(cv.getBirthYear()));
                birthYear.setPromptText("birth year");
                box.getChildren().add(birthYear);
                TextField gpa = new TextField(String.valueOf(cv.getGpa()));
                gpa.setPromptText("gpa");
                box.getChildren().add(gpa);
                TextField email = new TextField(cv.getEmail());
                email.setPromptText("email");
                box.getChildren().add(email);
                TextField description = new TextField(cv.getDescription());
                description.setPromptText("description");
                box.getChildren().add(description);
                TextField homeAddress = new TextField(cv.getHomeAddress());
                homeAddress.setPromptText("home address");
                box.getChildren().add(homeAddress);
                final TextField jobAddress = new TextField();
                if (cv instanceof AcademicCv academicCv) {
                    jobAddress.setText(academicCv.getJobAddress());
                    jobAddress.setPromptText("job address");
                    box.getChildren().add(jobAddress);
                }

                TextField phone = new TextField(cv.getPhone());
                phone.setPromptText("phone");
                box.getChildren().add(phone);
                TextField website = new TextField(cv.getWebsite());
                website.setPromptText("website");
                box.getChildren().add(website);
                Label label = new Label("Education:");
                StringBuilder educationString = new StringBuilder();
                cv.getEducation().forEach((k, v) -> educationString.append(k + ", " + v + "\r"));
                TextArea education = new TextArea(educationString.toString());
                box.getChildren().add(education);
                education.setPromptText("education\rname 1, register year 1\rname 2, register year 2\r...");
                StringBuilder skillsString = new StringBuilder();
                cv.getSkills().forEach((k, v) -> skillsString.append(k + ", " + v + "\r"));
                TextArea skills = new TextArea(skillsString.toString());
                skills.setPromptText("skills\rname 1, level 1\rname 2, level 2\r...");
                box.getChildren().add(skills);
                StringBuilder publicationsString = new StringBuilder();
                final TextArea publications = new TextArea();
                if (cv instanceof AcademicCv academicCv) {

                    academicCv.getPublications().forEach((k, v) -> publicationsString.append(k + ", " + v + "\r"));
                    publications.setText(publicationsString.toString());
                    publications.setPromptText("publications\rname 1, year 1\rname 2, year 2\r...");
                    box.getChildren().add(publications);
                }
                StringBuilder tagsString = new StringBuilder();
                cv.getTags().forEach(tag -> tagsString.append(tag + "\r"));
                TextArea tags = new TextArea(tagsString.toString());
                tags.setPromptText("tags\rtag 1\rtag 2\r...");
                box.getChildren().add(tags);
                pane.setContent(box);

                d.setTitle("edit");

                d.setResultConverter(type -> {
                    if (type == ButtonType.OK) {
                        try {
                            locationVBox.getChildren().add(new CheckBox(homeAddress.getText()));
                            locationTitledPane.setContent(locationVBox);

                            Map<String, Integer> newPublications = new HashMap<>();
                            Arrays.asList(publications.getText().trim().split("\r")).forEach(line -> {
                                String[] values = line.trim().split(",");
                                if (values.length > 1) {
                                    newPublications.put(values[0], Integer.parseInt(values[1]));
                                }
                            });

                            Map<String, Integer> newEducation = new HashMap<>();
                            Arrays.asList(education.getText().trim().split("\r")).forEach(line -> {
                                String[] values = line.trim().split(",");
                                if (values.length > 1) {
                                    newEducation.put(values[0], Integer.parseInt(values[1]));
                                }
                            });

                            Map<String, Integer> newSkills = new HashMap<>();
                            Arrays.asList(skills.getText().trim().split("\r")).forEach(line -> {
                                String[] values = line.trim().split(",");
                                if (values.length > 1) {
                                    newSkills.put(values[0], Integer.parseInt(values[1]));
                                }
                            });

                            Cv cvv; // new edited cv
                            if (cv instanceof AcademicCv) {
                                cvv = new AcademicCv(cv.getId(), fullName.getText(),
                                        Integer.parseInt(birthYear.getText()),
                                        Float.parseFloat(gpa.getText()),
                                        email.getText(),
                                        description.getText(),
                                        homeAddress.getText(),
                                        jobAddress.getText(),
                                        phone.getText(),
                                        website.getText(),
                                        newEducation,
                                        newSkills,
                                        newPublications,
                                        Arrays.asList(tags.getText().trim().split("\r")));
                            } else {
                                cvv = new Cv(cv.getId(), fullName.getText(),
                                        Integer.parseInt(birthYear.getText()),
                                        Float.parseFloat(gpa.getText()),
                                        email.getText(),
                                        description.getText(),
                                        homeAddress.getText(),

                                        phone.getText(),
                                        website.getText(),
                                        newEducation,
                                        newSkills,

                                        Arrays.asList(tags.getText().trim().split("\r")));
                            }
                            database.update(cvv);
                            treeView.getSelectionModel().getSelectedItem().setValue(cvv);

                            return cvv; // return new cv
                        } catch (NumberFormatException | SQLException e) {
                            e.printStackTrace();
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setContentText(e.getMessage());
                            alert.show();
                        }
                    }
                    return null;
                });
                Optional<Cv> optional = d.showAndWait();

                if (optional.isPresent()) {

                    Cv cvv = optional.get();
                    
                    if (cvv instanceof AcademicCv academicCv) {
                        
                    } else {
                        
                    }
                }

            }

        });
        addButton.setOnAction((value) -> {

            DialogPane pane = new DialogPane();

            TextField fullName = new TextField();
            fullName.setPromptText("full name");
            TextField birthYear = new TextField();
            birthYear.setPromptText("birth year");
            TextField gpa = new TextField();
            gpa.setPromptText("gpa");
            TextField email = new TextField();
            email.setPromptText("email");
            TextField description = new TextField();
            description.setPromptText("description");
            TextField homeAddress = new TextField();
            homeAddress.setPromptText("home address");
            TextField jobAddress = new TextField();
            jobAddress.setPromptText("job address");
            TextField phone = new TextField();
            phone.setPromptText("phone");
            TextField website = new TextField();
            website.setPromptText("website");
            Label label = new Label("Education:");
            TextArea education = new TextArea();
            education.setPromptText("education\rname 1, register year 1\rname 2, register year 2\r...");
            TextArea skills = new TextArea();
            skills.setPromptText("skills\rname 1, level 1\rname 2, level 2\r...");
            TextArea publications = new TextArea();
            publications.setPromptText("publications\rname 1, year 1\rname 2, year 2\r...");
            TextArea tags = new TextArea();
            tags.setPromptText("tags\rtag1\rtag2\r...");

            HBox statusHbox = new HBox();
            ChoiceBox<String> status = new ChoiceBox<>();
            status.getItems().add("Academician");
            status.getItems().add("Student");
            jobAddress.setVisible(false);
            publications.setVisible(false);
            status.getSelectionModel().selectLast();
            status.getSelectionModel().selectedItemProperty().addListener((listener, valuee, newValue) -> {
                if (newValue == "Academician") {
                    publications.setVisible(true);
                    publications.clear();
                    jobAddress.setVisible(true);
                    jobAddress.clear();

                } else if (newValue == "Student") {
                    publications.setVisible(false);
                    publications.clear();
                    jobAddress.setVisible(false);
                    jobAddress.clear();
                }
            });
            statusHbox.getChildren().add(status);

            pane.setStyle("-fx-background-color: #cdd5ee;");
            pane.setMaxHeight(400);
            pane.setMaxWidth(400);
            VBox box = new VBox();
            box.getChildren().addAll(statusHbox,
                    fullName,
                    birthYear, // filter
                    gpa, // filter
                    email,
                    description,
                    homeAddress, // filter
                    jobAddress,
                    phone,
                    website,
                    label,
                    education,
                    skills, // filter
                    publications,
                    tags); // filter
            pane.setContent(box);
            pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CLOSE);
            Dialog<Cv> dialog = new Dialog<>();
            dialog.setTitle("add");
            dialog.setDialogPane(pane);

            dialog.setResultConverter(type -> {
                if (type == ButtonType.OK) {
                    try {
                        locationVBox.getChildren().add(new CheckBox(homeAddress.getText()));
                        locationTitledPane.setContent(locationVBox);
                        Cv cv;
                        Map<String, Integer> newEducation = new HashMap<>();
                        Arrays.asList(education.getText().trim().split("\r")).forEach(line -> {
                            String[] values = line.trim().split(",");
                            if (values.length > 1) {
                                newEducation.put(values[0], Integer.parseInt(values[1]));
                            }
                        });

                        Map<String, Integer> newSkills = new HashMap<>();
                        Arrays.asList(skills.getText().trim().split("\r")).forEach(line -> {
                            String[] values = line.trim().split(",");
                            if (values.length > 1) {
                                newSkills.put(values[0], Integer.parseInt(values[1]));
                            }
                        });
                        if (status.getSelectionModel().getSelectedItem() == "Academician") {

                            Map<String, Integer> newPublications = new HashMap<>();
                            Arrays.asList(publications.getText().trim().split("\r")).forEach(line -> {
                                String[] values = line.trim().split(",");
                                if (values.length > 1) {
                                    newPublications.put(values[0], Integer.parseInt(values[1]));
                                }
                            });

                            cv = new AcademicCv(fullName.getText(),
                                    Integer.parseInt(birthYear.getText()),
                                    Float.parseFloat(gpa.getText()),
                                    email.getText(),
                                    description.getText(),
                                    homeAddress.getText(),
                                    jobAddress.getText(),
                                    phone.getText(),
                                    website.getText(),
                                    newEducation,
                                    newSkills,
                                    newPublications,
                                    Arrays.asList(tags.getText().trim().split("\r")));
                        } else {
                            cv = new Cv(fullName.getText(),
                                    Integer.parseInt(birthYear.getText()),
                                    Float.parseFloat(gpa.getText()),
                                    email.getText(),
                                    description.getText(),
                                    homeAddress.getText(),

                                    phone.getText(),
                                    website.getText(),
                                    newEducation,
                                    newSkills,
                                    Arrays.asList(tags.getText().trim().split("\r")));
                        }
                        return database.insert(cv);
                    } catch (NumberFormatException | SQLException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setContentText(e.getMessage());
                        alert.show();
                    }
                }
                return null;
            });
            Optional<Cv> optional = dialog.showAndWait();
            if (optional.isPresent()) {
                Cv cv = optional.get();

                if (cv instanceof AcademicCv academicCv) {
                    academicRoot.getChildren().add(new TreeItem<>(academicCv));
                } else {
                    root.getChildren().add(new TreeItem<>(cv));
                }
            }
        });
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            root.getChildren().clear();
            try {
                database.filter("fullName", newValue).forEach(cv -> root.getChildren().add(new TreeItem<>(cv)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        removeButton.setOnAction((value) -> {
            final TreeItem<Cv> selectedItem = treeView.getSelectionModel().getSelectedItem();

            try {
                database.delete(selectedItem.getValue());
                root.getChildren().remove(selectedItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        filterGpaButton.setOnAction(value -> {
            root.getChildren().clear();
            try {
                database.filter("gpa", Double.parseDouble(lowestGpaTextField.getText()),
                        Double.parseDouble(highestGpaTextField.getText()))
                        .forEach(cv -> root.getChildren().add(new TreeItem<>(cv)));
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        });

        menuItemHelp.setOnAction((value) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Help");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Aenean rutrum ullamcorper rutrum. Mauris placerat neque id odio porta sodales. Morbi mollis, turpis vitae tempus elementum, turpis est iaculis erat, nec mattis enim ligula vitae sem. Nam feugiat hendrerit lectus eget congue. Vestibulum enim libero, elementum at tortor et, consequat imperdiet purus. Integer eget nunc suscipit, molestie metus et, iaculis massa. Vestibulum imperdiet neque ut pharetra iaculis. Etiam id imperdiet nisi. Vivamus nec dapibus augue. Nam euismod, nibh eu dictum imperdiet, neque purus tincidunt sapien, at eleifend nibh dolor et felis.");
            alert.showAndWait();

        });

        menuItemDelete.setOnAction((value) -> {
            final TreeItem<Cv> selectedItem = treeView.getSelectionModel().getSelectedItem();

            try {
                database.delete(selectedItem.getValue());
                root.getChildren().remove(selectedItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        menuItemClose.setOnAction((value) -> {
            try {
                database.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Platform.exit();
        });
    }
}
