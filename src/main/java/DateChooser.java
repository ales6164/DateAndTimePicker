package main.java;

import java.util.Date;
import javafx.scene.control.Control;

public class DateChooser extends Control{

    private static final String DEFAULT_STYLE_CLASS = "date-chooser";
    private ChangeEventListener listener;
    private Date selectedValue;

    public DateChooser(Date preset) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.selectedValue = preset;
    }

    public DateChooser() {
        this(new Date(System.currentTimeMillis()));
    }

    public void setChangeEventListener(ChangeEventListener listener) {
        this.listener = listener;
    }

    public void setSelectedValue(Date selectedValue) {
        this.selectedValue = selectedValue;
        if (listener != null) listener.onChangeEvent(selectedValue);
    }

    @Override
    public String getUserAgentStylesheet() {
        return "main/resources/DateChooser.css";
    }

    public Date getSelectedValue() {
        return selectedValue;
    }

    public interface ChangeEventListener {
        void onChangeEvent(Date selectedValue);
    }
}
