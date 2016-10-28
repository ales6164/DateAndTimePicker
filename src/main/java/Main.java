package main.java;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

public class Main extends Application {

    private static final String DEFAULT_FORMAT = "%02d";
    private static Date date;
    private static int hour, minute;
    private static TextField textField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.GERMAN);
        primaryStage.setTitle("Date Picker");

        date = new Date(System.currentTimeMillis());
        hour = LocalTime.now().getHour();
        minute = LocalTime.now().getMinute();

        StackPane root = new StackPane();
        HBox hbox = new HBox();
        textField = new TextField();
        final Label label1 = new Label("am");
        label1.getStyleClass().add("with-padding");
        final Label label2 = new Label("folgenden");
        label2.getStyleClass().add("with-padding");
        final MenuItem menuItem = new MenuItem();
        menuItem.setGraphic(createPopupContent(3));
        MenuButton popupButton = new MenuButton();
        popupButton.setId("openPopupBtn");
        popupButton.getItems().setAll(
                menuItem
        );

        hbox.getChildren().addAll(label1, textField, popupButton, label2);
        hbox.setAlignment(Pos.CENTER);
        root.getChildren().add(hbox);
        root.getStyleClass().add("main");

        String css = this.getClass().getResource("/main/resources/Main.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.show();

        updateTextField();
    }

    private HBox createPopupContent(int extraFields) {
        final DateChooser dateChooser = new DateChooser();
        dateChooser.setChangeEventListener(value -> {
            date = value;
            updateTextField();
            System.out.println("DATE");
            System.out.println(value);
        });

        final NumberSlider hourChooser = new NumberSlider(23, hour, extraFields, DEFAULT_FORMAT);
        hourChooser.setChangeEventListener((value) -> {
            hour = value;
            updateTextField();
            System.out.println("HOUR");
            System.out.println(value);
        });

        final NumberSlider minuteChooser = new NumberSlider(59, minute, extraFields, DEFAULT_FORMAT);
        minuteChooser.setChangeEventListener((value) -> {
            minute = value;
            updateTextField();
            System.out.println("MINUTES");
            System.out.println(value);
        });

        final VBox divs = new VBox();
        for (int i = 0; i < extraFields * 2 + 1; i++) {
            Label div = new Label(":");
            div.getStyleClass().add("time-divider");
            divs.getChildren().add(div);
        }
        divs.setAlignment(Pos.CENTER);


        final HBox popup = new HBox(0);
        popup.setAlignment(Pos.CENTER);
        popup.getChildren().setAll(dateChooser, hourChooser, divs, minuteChooser);

        return popup;
    }

    private void updateTextField() {
        SimpleDateFormat dt1 = new SimpleDateFormat("dd. MM. yyyy");
        textField.setText(dt1.format(date) + " um " + String.format(DEFAULT_FORMAT, hour) + ":" + String.format(DEFAULT_FORMAT, minute) + " Uhr");
    }
}
