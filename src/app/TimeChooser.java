package app;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;

import java.sql.Time;
import java.util.Date;

public class TimeChooser extends Control{

    private static final String DEFAULT_STYLE_CLASS = "time-chooser";
    private int hour;
    private int minute;
    private TextField textField;

    public TimeChooser(int hour, int minute) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.hour = hour;
        this.minute = minute;
    }

    public TimeChooser(TextField textField) {
        this(new Date(System.currentTimeMillis()).getHours(), new Date(System.currentTimeMillis()).getMinutes());
        this.textField = textField;
    }

    @Override
    public String getUserAgentStylesheet() {
        return "app/time.css";
    }

    public TextField getTextField() {
        return textField;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
