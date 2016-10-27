package main.java;

import javafx.scene.control.Control;

public class NumberSlider extends Control {

    private static final String DEFAULT_STYLE_CLASS = "number-slider", DEFAULT_FORMAT = "%02d";
    private static final int DEFAULT_NUM_FIELDS = 3;
    private ChangeEventListener listener;
    private int maxNumber, selectedValue, extraFields;
    private String fieldNumberFormat;

    public NumberSlider(int maxNumber, int selectedValue, int extraFields, String fieldNumberFormat) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.maxNumber = maxNumber;
        this.selectedValue = selectedValue;
        this.extraFields = extraFields;
        this.fieldNumberFormat = fieldNumberFormat;
    }

    public NumberSlider(int maxNumber, int selected, int extraFields) {
        this(maxNumber, selected, extraFields, DEFAULT_FORMAT);
    }

    public NumberSlider(int maxNumber, int selected) {
        this(maxNumber, selected, DEFAULT_NUM_FIELDS, DEFAULT_FORMAT);
    }

    public void setChangeEventListener(ChangeEventListener listener) {
        this.listener = listener;
    }

    @Override
    public String getUserAgentStylesheet() {
        return "/main/resources/NumberSlider.css";
    }

    public int getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(int selectedValue) {
        this.selectedValue = selectedValue;
        if (listener != null) listener.onChangeEvent(selectedValue);
    }

    public int getExtraFields() {
        return extraFields;
    }

    public String getFieldNumberFormat() {
        return fieldNumberFormat;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public interface ChangeEventListener {
        void onChangeEvent(int selectedValue);
    }
}
