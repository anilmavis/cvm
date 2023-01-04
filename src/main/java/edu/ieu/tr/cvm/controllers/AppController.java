package edu.ieu.tr.cvm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sqlite.SQLiteException;

import edu.ieu.tr.cvm.AcademicCv;
import edu.ieu.tr.cvm.Cv;
import edu.ieu.tr.cvm.Database;
import edu.ieu.tr.cvm.Exporter;
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

    //publications

   @FXML
    private TitledPane publicationsTitledPane;
    @FXML
    private VBox publicationsVBox;


    //tags
    @FXML
    private TitledPane tagsTitledPane;
    @FXML
    private VBox tagsVBox;


    //school

    @FXML
    private TitledPane schoolTitledPane;
    @FXML
    private VBox schoolVBox;

    @FXML
    private Button filterButton;

    @FXML
    private TextField nameFilter;

    @FXML
    private TextField lowestGpaTextField;

    @FXML
    private TextField highestGpaTextField;

    @FXML
    private TextField lowestBirthTextField;

    @FXML
    private TextField highestBirthTextField;

    @FXML
    private Button exportButton;

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
            if (cv instanceof AcademicCv academicCv) {
                academicRoot.getChildren().add(new TreeItem<Cv>(academicCv));
            } else {
                root.getChildren().add(new TreeItem<Cv>(cv));
            }
        });
        database.getLocations().forEach(s -> locationVBox.getChildren().add(new CheckBox(s)));
        database.getSkills().forEach(s -> skillsVBox.getChildren().add(new CheckBox(s)));
        database.getSchool().forEach(s -> schoolVBox.getChildren().add(new CheckBox(s)));
        database.getPublications().forEach(s -> publicationsVBox.getChildren().add(new CheckBox(s)));
        database.getTags().forEach(s -> tagsVBox.getChildren().add(new CheckBox(s)));



        treeView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {

                Dialog<Cv> d = new Dialog<>();
                DialogPane pane = new DialogPane();
                pane.setMaxHeight(600);
                pane.setMaxWidth(400);
                pane.getStylesheets().add("style.css");
                pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                d.setDialogPane(pane);
                VBox box = new VBox();
                box.setSpacing(3);
                if (treeView.getSelectionModel().getSelectedItem() == null) {
                    return;
                }
                Cv cv = treeView.getSelectionModel().getSelectedItem().getValue();
                if (root.getValue().equals(cv) || treeView.getSelectionModel().getSelectedItem().getValue() == null) {
                    return;
                }
                if (academicRoot.getValue().equals(cv)) {
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
                StringBuilder educationString = new StringBuilder();
                cv.getEducation().forEach((k, v) -> educationString.append(k + ", " + v + "\n"));
                TextArea education = new TextArea(educationString.toString());
                box.getChildren().add(education);
                education.setPromptText("education\rname 1, register year 1\rname 2, register year 2\r...");


                StringBuilder skillsString = new StringBuilder();
                cv.getSkills().forEach((k, v) -> skillsString.append(k + ", " + v + "\n"));
                TextArea skills = new TextArea(skillsString.toString());
                skills.setPromptText("skills\rname 1, level 1\rname 2, level 2\r...");
                box.getChildren().add(skills);


                StringBuilder publicationsString = new StringBuilder();
                final TextArea publications = new TextArea();
                if (cv instanceof AcademicCv academicCv) {

                    academicCv.getPublications().forEach((k, v) -> publicationsString.append(k + ", " + v + "\n"));
                    publications.setText(publicationsString.toString());
                    publications.setPromptText("publications\rname 1, year 1\rname 2, year 2\r...");
                    box.getChildren().add(publications);
                }
                StringBuilder tagsString = new StringBuilder();
                cv.getTags().forEach(tag -> tagsString.append(tag + "\n"));
                TextArea tags = new TextArea(tagsString.toString());
                tags.setPromptText("tags\rtag 1\rtag 2\r...");
                box.getChildren().add(tags);
                pane.setContent(box);

                d.setTitle("edit");

                String oldHomeAddress = homeAddress.getText();
                d.setResultConverter(type -> {
                    if (type == ButtonType.OK) {
                        try {


                            if(!homeAddress.getText().isEmpty()) {
                                locationVBox.getChildren().clear();
                                database.getLocations().forEach(v->{if (!v.isEmpty()){if(v.equals(oldHomeAddress)){;}locationVBox.getChildren().add(new CheckBox(v));}});
                                locationVBox.getChildren().add(new CheckBox(homeAddress.getText()));}
                            locationTitledPane.setContent(locationVBox);
                            skillsVBox.getChildren().clear();
                            database.getSkills().forEach(v -> {if (!v.isEmpty()){skillsVBox.getChildren().add(new CheckBox(v));}});
                            
                            skillsTitledPane.setContent(skillsVBox);
                            schoolVBox.getChildren().clear();
                            database.getSchool().forEach(v -> {if (!v.isEmpty()){schoolVBox.getChildren().add(new CheckBox(v));};});
                            schoolTitledPane.setContent(schoolVBox);
                            tagsVBox.getChildren().clear();
                            database.getTags().forEach(v -> {if (!v.isEmpty()){tagsVBox.getChildren().add(new CheckBox(v));};});
                            tagsTitledPane.setContent(tagsVBox);
                            publicationsVBox.getChildren().clear();
                            database.getPublications().forEach(v -> {if (!v.isEmpty()){publicationsVBox.getChildren().add(new CheckBox(v));};});
                            publicationsTitledPane.setContent(publicationsVBox);


                            Map<String, Integer> newPublications = new HashMap<>();
                            Arrays.asList(publications.getText().split("\n")).forEach(line -> {
                                String[] values = line.split(",");
                                for(int i = 0; i < values.length; i++){
                                    values[i] = values[i].trim();
                                }
                                if (values.length > 1) {
                                    newPublications.put(values[0], Integer.parseInt(values[1]));
                                }
                            });

                            Map<String, Integer> newEducation = new HashMap<>();
                            Arrays.asList(education.getText().split("\n")).forEach(line -> {
                                String[] values = line.split(",");
                                for(int i = 0; i < values.length; i++){
                                    values[i] = values[i].trim();
                                }
                                if (values.length > 1) {
                                    newEducation.put(values[0], Integer.parseInt(values[1]));
                                }
                            });

                            Map<String, Integer> newSkills = new HashMap<>();
                            Arrays.asList(skills.getText().split("\n")).forEach(line -> {
                                String[] values = line.split(",");
                                for(int i = 0; i < values.length; i++){
                                    values[i] = values[i].trim();
                                }
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
                                        Arrays.asList(tags.getText().split("\n")));
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

                                        Arrays.asList(tags.getText().split("\n")));
                                System.out.println(cvv);
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
            pane.getStylesheets().add("style.css");

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
            pane.setMaxHeight(400);
            pane.setMaxWidth(400);
            VBox box = new VBox();
            box.setSpacing(3);
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
                        
                        Cv cv;
                        Map<String, Integer> newEducation = new HashMap<>();
                        Arrays.asList(education.getText().split("\n")).forEach(line -> {
                            String[] values = line.split(",");
                            for(int i =0;i<values.length;i++){
                                values[i]=values[i].trim();
                            }
                            if (values.length > 1) {
                                newEducation.put(values[0], Integer.parseInt(values[1]));
                            }
                        });

                        Map<String, Integer> newSkills = new HashMap<>();
                        Arrays.asList(skills.getText().split("\n")).forEach(line -> {
                            String[] values = line.split(",");
                            for(int i =0;i<values.length;i++){
                                values[i]=values[i].trim();
                            }
                            if (values.length > 1) {
                                newSkills.put(values[0], Integer.parseInt(values[1]));
                            }
                        });
                        if (status.getSelectionModel().getSelectedItem() == "Academician") {

                            Map<String, Integer> newPublications = new HashMap<>();
                            Arrays.asList(publications.getText().split("\n")).forEach(line -> {
                                String[] values = line.split(",");
                                for(int i =0;i<values.length;i++){
                                    values[i]=values[i].trim();
                                }
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
                                    Arrays.asList(tags.getText().split("\n")));
                        } else {
                            String[] values = tags.getText().split("\n");
                            for(int i =0;i<values.length;i++){
                                values[i]=values[i].trim();
                            }
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
                                    Arrays.asList(values));
                        }
                        Cv cvs=database.insert(cv);

                        if(!homeAddress.getText().isEmpty()) {
                                locationVBox.getChildren().add(new CheckBox(homeAddress.getText()));}
                            locationTitledPane.setContent(locationVBox);
                            skillsVBox.getChildren().clear();
                            database.getSkills().forEach(v -> {if (!v.isEmpty()){skillsVBox.getChildren().add(new CheckBox(v));}});
                            
                            skillsTitledPane.setContent(skillsVBox);
                            schoolVBox.getChildren().clear();
                            database.getSchool().forEach(v -> {if (!v.isEmpty()){schoolVBox.getChildren().add(new CheckBox(v));};});
                            schoolTitledPane.setContent(schoolVBox);
                            tagsVBox.getChildren().clear();
                            database.getTags().forEach(v -> {if (!v.isEmpty()){tagsVBox.getChildren().add(new CheckBox(v));};});
                            tagsTitledPane.setContent(tagsVBox);
                            publicationsVBox.getChildren().clear();
                            database.getPublications().forEach(v -> {if (!v.isEmpty()){publicationsVBox.getChildren().add(new CheckBox(v));};});
                            publicationsTitledPane.setContent(publicationsVBox);
                        





                        return cvs;
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

        filterButton.setOnAction(value -> {
            ArrayList<String> queries = new ArrayList<>();
            try {
                if (!nameFilter.getText().isEmpty()) {

                    queries.add("cv.fullName like '%" + nameFilter.getText() + "%' and ");

                }
                if (!highestBirthTextField.getText().isEmpty() && !lowestBirthTextField.getText().isEmpty()) {
                    queries.add("cv.birthYear <= " + highestBirthTextField.getText() + " and cv.birthYear >= "
                            + lowestBirthTextField.getText() + " and ");
                }

                if (!highestGpaTextField.getText().isEmpty() && !lowestGpaTextField.getText().isEmpty()) {
                    queries.add("cv.gpa <= " + highestGpaTextField.getText() + " and cv.gpa >= "
                            + lowestGpaTextField.getText() + " and ");
                }
                
                // if nothing is selected on filter accordion, the tree view is cleared. solve this problem later
                root.getChildren().clear();
                academicRoot.getChildren().clear();




                // SKILLS BEGIN
                queries.add("1=1 ");

                skillsVBox.getChildren().forEach(skill -> {
                    CheckBox checkBox = (CheckBox) skill;

                    if (checkBox.isSelected()) {

                        queries.add(" and cv.id in (select skill.cv_id from skill where skill.name = '" + checkBox.getText() + "' ) ");


                    }

                });
                //queries.add(") and ");



                // SKILLS END



              //  queries.add("and cv.id in (select education.cv_id from education where 1=1  ");


                schoolVBox.getChildren().forEach(school -> {
                    CheckBox schoolCheckBox = (CheckBox) school;

                    if (schoolCheckBox.isSelected()) {

                        queries.add("and cv.id in (select education.cv_id from education where education.name = '" + schoolCheckBox.getText() + "') ");

                    }

                });
                queries.add(" and ");


                // SCHOOL END





                publicationsVBox.getChildren().forEach(publication -> {
                    CheckBox publicationCheckBox = (CheckBox) publication;

                    if (publicationCheckBox.isSelected()) {

                        queries.add("publications.name = '" + publicationCheckBox.getText() + "' and ");

                    }

                });



                //PUBLICATION END





                    tagsVBox.getChildren().forEach(tags -> {
                    CheckBox tagsCheckBox = (CheckBox) tags;

                    if (tagsCheckBox.isSelected()) {

                        queries.add("tag.name = '" + tagsCheckBox.getText() + "' and ");

                    }

                });




                //TAGS END




                if (queries.size() != 0) {
                    database.filterAll(queries).forEach(cv -> {
                        if (cv instanceof AcademicCv academicCv) {
                            academicRoot.getChildren().add(new TreeItem<>(academicCv));
                        } else {
                            root.getChildren().add(new TreeItem<>(cv));
                        }
                        textField.clear();

                    });
                }






            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        });

        /*
         * filterButton.setOnAction(value -> {
         * root.getChildren().clear();
         * try {
         * database.filter("gpa", Double.parseDouble(lowestGpaTextField.getText()),
         * Double.parseDouble(highestGpaTextField.getText()))
         * .forEach(cv -> root.getChildren().add(new TreeItem<>(cv)));
         * } catch (NumberFormatException | SQLException e) {
         * e.printStackTrace();
         * Alert alert = new Alert(AlertType.ERROR);
         * alert.setContentText(e.getMessage());
         * alert.show();
         * }
         * });
         */

        menuItemHelp.setOnAction((value) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Help");
            alert.setHeaderText("Help");
            alert.setContentText("Welcome to CVM");
            TextArea area = new TextArea(menuItemHelp.getText());
            area.setText("Welcome to CVM (CV Manager)! Where you can store both student and academic CVs and filter them according to your needs for an employee.\n" +
                    "\n" +
                    "Created CVs in the system have attributes depending on their types which are academic and student. The user can add and delete CVs, edit their attributes to update and filter them with the attributes such as major, GPA, school, skills etc. and their tags. The user can also search any word within the CVs and find related candidate’s CV and tag the CV.\n" +
                    "\n" +
                    "How To Create a CV?\n" +
                    "Click the (+) button on the left-side of the page. A new page will open for you. First, select the type of the CV, located on the top, you want to add/fill in. You can select either a student or academician. You will see an empty CV page waiting to be filled with the wanted information. After filling the required information click OK. Your CV has been saved.\n" +
                    "\n" +
                    "How To Delete a CV?\n" +
                    "On the left-side of the page you can see the saved CV’s under the types Student and Academician. To delete a CV, just select the CV you want to delete by clicking on it once and then press the “Delete” button which is located in the middle of the page. The CV you have selected will be deleted.\n" +
                    "\n" +
                    "How To Edit a CV?\n" +
                    "On the left-side of the page you can see the saved CV’s under the types Student and Academician. To edit a CV, just select the CV you want to edit by clicking on it and then press the “Edit” button which is located in the middle of the page. You will see a page like the adding page but with information filled. Just edit the information you want and then press the “Save” button. The CV will be edited.\n" +
                    "\n" +
                    "How To Use the Search Bar?\n" +
                    "You can use the search bar by writing anything you want from the name of the applicant to the name of the schools, skills, locations etc. The CVs would be sorted out and shown to you on the rectangular area that is below the search bar, according to your searching. There will be no need to press any button to search as the search bar gets activated even typing one letter.\n" +
                    "\n" +
                    "How To Filter CVs?\n" +
                    "On the right-side of the page there is the filter section. After you click on the “Filter” button you will see many options to filter. You can click each filtering option and set your desired qualifications for an applicant. On the birth year and GPA filters you can enter an interval of numbers from lowest to highest. For the other filters such as skills, school, major etc. you will see checkboxes named with the available features of the applicants within the database. You can simply check the checkboxes you want to filter. After these selections just press the “Filter” button down below. You will see the CVs that compile with your selections on the left-side of the page.\n" +
                    "\n" +
                    "How To View CV?\n" +
                    "To view a CV, just click on the person you want to view it’s CV twice on the left-side of the page and then you can see the CV of it on the new page that will be opened.\n" +
                    "\n" +
                    "How To Export a CV?\n" +
                    "You can export a CV to PDF by selecting the CV you want to export located on the left-side of the page and then clicking on the “Export” button on the middle of the page. You can see the PDF version of the CV after saving it to your computer.\n");
            area.setWrapText(true);
            area.setEditable(false);

            alert.getDialogPane().setExpandableContent(area);
            alert.getDialogPane().setStyle("-fx-background-color:    #e6eaf7;");
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
        exportButton.setOnAction(value -> {
            try {
                Exporter.export(treeView.getSelectionModel().getSelectedItem().getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
