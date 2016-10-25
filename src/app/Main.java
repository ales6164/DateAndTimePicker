package app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Locale;

public class Main extends Application {

    ContextMenu popup;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.GERMAN);

        primaryStage.setTitle("Date and Time Picker");
        VBox root = new VBox();

        HBox hbox = new HBox();
        HBox hbox2 = new HBox();

        final Label label1 = new Label("am");

        final TextField textField = new TextField();

        // create a popup trigger menu button and it's associated popup content.
        final MenuItem wizPopup = new MenuItem();
        wizPopup.setGraphic(createPopupContent(textField));
        MenuButton popupButton = new MenuButton();
        popupButton.setId("dateChooserButton");
        popupButton.getItems().setAll(
                wizPopup
        );

        final Label label2 = new Label("folgenden");
        final Label label4 = new Label("folgenden");

        final Label label3 = new Label("um");

        final TextField textField2 = new TextField();

        // create a popup trigger menu button and it's associated popup content.
        final MenuItem wizPopup2 = new MenuItem();
        wizPopup2.setGraphic(createTimePopupContent(textField2));
        MenuButton popupButton2 = new MenuButton();
        popupButton2.setId("timeChooserButton");
        popupButton2.getItems().setAll(
                wizPopup2
        );

        hbox.getChildren().addAll(label1, textField, popupButton, label2);
        hbox2.getChildren().addAll(label3, textField2, popupButton2, label4);
        root.getChildren().addAll(hbox, hbox2);
        root.getStyleClass().add("main");

        String css = this.getClass().getResource("Main.css").toExternalForm();
        Scene scene = new Scene(root, 400, 250);
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private VBox createPopupContent(TextField tf) {
        final DateChooser dateChooser = new DateChooser(tf);

        final VBox wizBox = new VBox(0);
        wizBox.setId("calendarPicker");
        wizBox.setAlignment(Pos.CENTER);
        wizBox.getChildren().setAll(
                dateChooser
        );

        return wizBox;
    }

    private HBox createTimePopupContent(TextField tf) {
        final TimeChooser timeChooser = new TimeChooser(tf);

        final HBox wizBox = new HBox(0);
        wizBox.setId("calendarPicker");
        wizBox.setAlignment(Pos.CENTER);
        wizBox.getChildren().setAll(
                timeChooser
        );

        return wizBox;
    }
}
