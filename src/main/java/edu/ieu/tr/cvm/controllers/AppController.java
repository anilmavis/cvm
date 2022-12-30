package edu.ieu.tr.cvm.controllers;

import java.sql.SQLException;

import edu.ieu.tr.cvm.Cv;
import edu.ieu.tr.cvm.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class AppController {
    private Database database; // placeholder to avoid calling Database.getInstance() all the time

    @FXML
    private TreeView<Cv> treeView;

    private TreeItem<Cv> root;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Accordion filterAccordion;

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
			try {
				onAdd();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		});
        removeButton.setOnAction((value) -> onRemove());
    }

    private void onAdd() throws SQLException {
        final Cv cv1 = Cv.Builder.getInstance()
                .fullName("a")
                .birthYear(2001)
                .email("a@izmirekonomi.edu.tr")
                .description("I am a student.")
                .homeAddress("izmir")
                .jobAdress("izmir")
                .telephoneNumber("unknown")
                .build();
        database.insert(cv1);
    }

    private void onRemove() {
        // treeView.getRoot().getChildren().add();
    }

    private void clear() {
        treeView.getRoot().getChildren().clear();
    }
}
