package app;

import java.util.Date;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class DateChooser extends Control{

    private static final String DEFAULT_STYLE_CLASS = "date-chooser";
    private Date date;
    private TextField textField;

    public DateChooser(Date preset) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.date = preset;
    }

    public DateChooser(TextField textField) {
        this(new Date(System.currentTimeMillis()));
        this.textField = textField;
    }

    @Override
    public String getUserAgentStylesheet() {
        return "app/calendar.css";
    }

    public Date getDate() {
        return date;
    }

    public TextField getTextField() {
        return textField;
    }
}
