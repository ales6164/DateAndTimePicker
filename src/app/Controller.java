package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd. MM. yyyy");

    @FXML
    private DateTimePicker dpDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        StringConverter converter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) return dateFormatter.format(date);
                else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    LocalDate date = LocalDate.parse(string, dateFormatter);

                    if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusYears(1))) {
                        return dpDate.getValue();
                    } else return date;
                }

                return null;
            }
        };

        //dpDate.setDayCellFactory(dayCellFactory);
        dpDate.setConverter(converter);
        dpDate.setPromptText("dd. MM. yyyy");
        dpDate.setValue(LocalDate.now());
    }
}
