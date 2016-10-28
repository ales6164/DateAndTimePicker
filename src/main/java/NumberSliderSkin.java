package main.java;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NumberSliderSkin extends SkinBase<NumberSlider> {

    private final int maxNumber, extraFields;
    private int selectedValue;
    private final String fieldNumberFormat;

    private static class Cell extends StackPane {

        private final int value;
        private final String text;

        public Cell(int value, String text) {
            this.getStyleClass().add("cell");
            this.value = value;
            this.text = text;
            final Label label = new Label(text);
            getChildren().add(label);
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    public NumberSliderSkin(NumberSlider timeChooser) {
        super(timeChooser);
        maxNumber = timeChooser.getMaxNumber();
        selectedValue = timeChooser.getSelectedValue();
        extraFields = timeChooser.getExtraFields();
        fieldNumberFormat = timeChooser.getFieldNumberFormat();

        final NumberPickerPane numberPickerPane = new NumberPickerPane();
        numberPickerPane.setNumberEventListener(cell -> timeChooser.setSelectedValue(cell.getValue()));

        final VBox vbox = new VBox();

        final Button hourUp = new Button("▲");
        hourUp.getStyleClass().add("time-arrow");
        hourUp.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            numberPickerPane.moveSlider(-1);
        });
        final Button hourDown = new Button("▼");
        hourDown.getStyleClass().add("time-arrow");
        hourDown.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            numberPickerPane.moveSlider(1);
        });

        vbox.getChildren().addAll(hourUp, numberPickerPane, hourDown);
        vbox.setAlignment(Pos.CENTER);

        final HBox content = new HBox();
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(vbox);
        getChildren().add(content);
    }

    class NumberPickerPane extends VBox {

        private NumberEventListener listener;

        public NumberPickerPane() {
            refresh();
        }

        public void moveSlider(int i) {
            selectedValue = Math.floorMod((selectedValue + i), (maxNumber + 1));
            refresh();
        }

        private void refresh() {
            super.getChildren().clear();

            for (int i = 0; i < extraFields * 2 + 1; i++) {
                int number = selectedValue - extraFields + i;

                if (number > maxNumber) {
                    number = number - maxNumber - 1;
                } else if (number < 0) {
                    number = maxNumber + number + 1;
                }

                Cell cell = new Cell(number, String.format(fieldNumberFormat, number));

                if (number == selectedValue) {
                    cell.getStyleClass().add("cell-selected");
                }

                cell.setOnMouseClicked(arg0 -> {
                    selectedValue = cell.getValue();
                    if (listener != null) listener.onNumberChange(cell);
                    refresh();
                });

                super.getChildren().add(cell);
            }
        }

        public void setNumberEventListener(NumberEventListener listener) {
            this.listener = listener;
        }
    }

    private interface NumberEventListener {
        void onNumberChange(Cell cell);
    }
}
