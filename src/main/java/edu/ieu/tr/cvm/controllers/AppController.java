package edu.ieu.tr.cvm.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import edu.ieu.tr.cvm.Cv;
import edu.ieu.tr.cvm.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public final class AppController {
    private Database database;

    @FXML
    private TreeView<Cv> treeView;

    private TreeItem<Cv> root;

    @FXML
    private Button addButton;

    @FXML
    private TextField textField;

    @FXML
    private Button removeButton;

    @FXML
    private Accordion filterAccordion;

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
        root = new TreeItem<Cv>();
        root.setExpanded(true);
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
        treeView.setRoot(root);
        database.getAll().forEach((cv) -> {
            root.getChildren().add(new TreeItem<Cv>(cv));
        });
        addButton.setOnAction((value) -> {
            DialogPane pane = new DialogPane();
            pane.setStyle("-fx-background-color: #cdd5ee;");
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
            TextArea education = new TextArea();
            education.setPromptText("education\reducation 1, year 1\reducation 2, year 2\r...");
            TextArea skills = new TextArea();
            skills.setPromptText("additional skills\rskill 1, level 1\rskill 2, level 2\r...");
            TextArea additionalSkills = new TextArea();
            additionalSkills.setPromptText("skills\rskill1, level 2\rskill 2, level 2\r...");
            TextArea tags = new TextArea();
            tags.setPromptText("tags\rtag1\rtag2\r...");
            VBox box = new VBox();
            box.getChildren().addAll(fullName,
                    birthYear,
                    gpa,
                    email,
                    description,
                    homeAddress,
                    jobAddress,
                    phone,
                    website,
                    education,
                    skills,
                    additionalSkills,
                    tags);
            pane.setContent(box);
            pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CLOSE);
            Dialog<Cv> dialog = new Dialog<>();
            dialog.setTitle("add");
            dialog.setDialogPane(pane);
            dialog.setResultConverter(type -> {
                if (type == ButtonType.OK) {
                    try {
                        return database.insert(new Cv(fullName.getText(),
                                Integer.parseInt(birthYear.getText()),
                                Float.parseFloat(gpa.getText()),
                                email.getText(),
                                description.getText(),
                                homeAddress.getText(),
                                jobAddress.getText(),
                                phone.getText(),
                                website.getText(),
                                new HashMap<>(),
                                new HashMap<>(),
                                new HashMap<>(),
                                new ArrayList<>()));
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
                root.getChildren().add(new TreeItem<>(optional.get()));
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
                        Double.parseDouble(highestGpaTextField.getText())).forEach(cv -> root.getChildren().add(new TreeItem<>(cv)));
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        });
    }
}
