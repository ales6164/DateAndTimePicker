package app;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeChooserSkin extends SkinBase<TimeChooser> {

    private final TextField textField;
    private final int hour;
    private final int minute;
    private static int currentHour;
    private static int currentMinute;
    private final Label time;
    private final HBox content;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMM yyyy");

    private static class TimeCell extends StackPane {

        private final int value;
        private final String text;
        private final int type;

        public TimeCell(int value, String text, int type) {
            this.getStyleClass().add("any-time-cell");
            this.value = value;
            this.text = text;
            this.type = type;
            Label label = new Label(text);
            getChildren().add(label);
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    public TimeChooserSkin(TimeChooser timeChooser) {
        super(timeChooser);
        // this date is the selected date
        hour = timeChooser.getHour();
        minute = timeChooser.getMinute();
        textField = timeChooser.getTextField();

        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(i, String.format("%02d", i));
        }
        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            minutes.add(i, String.format("%02d", i));
        }

        TimePickerPane hourPane = new TimePickerPane(hours, hour, 0);
        TimePickerPane minutePane = new TimePickerPane(minutes, minute, 1);

        time = new Label();
        VBox vbox = new VBox();
        VBox vbox2 = new VBox();

        // create the navigation Buttons
        Button hourUp = new Button("▲");
        hourUp.getStyleClass().add("time-arrow");
        hourUp.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            hourPane.moveSlider(-1);
        });
        Button hourDown = new Button("▼");
        hourDown.getStyleClass().add("time-arrow");
        hourDown.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            hourPane.moveSlider(1);
        });

        Button minuteUp = new Button("▲");
        minuteUp.getStyleClass().add("time-arrow");
        minuteUp.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            minutePane.moveSlider(-1);
        });
        Button minuteDown = new Button("▼");
        minuteDown.getStyleClass().add("time-arrow");
        minuteDown.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            minutePane.moveSlider(1);
        });


        vbox.getChildren().addAll(hourUp, hourPane, hourDown);
        vbox.setAlignment(Pos.CENTER);

        vbox2.getChildren().addAll(minuteUp, minutePane, minuteDown);
        vbox2.setAlignment(Pos.CENTER);

        // use a BorderPane to Layout the view
        content = new HBox();
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(vbox, vbox2);
        getChildren().add(content);
        //content.setCenter(timePickerPane);
    }

    /**
     * @author eppleton
     */
    class TimePickerPane extends VBox {

        private int pickerType;
        private ObservableList<String> list;
        private int selectedNumber;
        private int rangeCenter;
        private TimeCell selectedCell;


        public TimePickerPane(ObservableList<String> items, int value, int type) {
            setPrefSize(30, 150);

            list = items;
            rangeCenter = list.size() / 2;
            selectedNumber = rangeCenter;
            pickerType = type;
            refresh();
        }

        /**
         * Move forward the specified number of Months, move backward by using
         * negative numbers
         *
         * @param i
         */
        public void moveSlider(int i) {
            if (rangeCenter + i < list.size() - 3 && rangeCenter + i > 3) {
                rangeCenter += i;
            }
            refresh();
        }

        private void refresh() {
            super.getChildren().clear();

            int start = rangeCenter - 4;
            int end = rangeCenter + 4;

            if (start < 0) {
                start = 0;
            } else if (start > list.size() - 8) {
                start = list.size() - 8;
            }
            if (end > list.size()) {
                end = list.size();
            } else if (end < 8) {
                end = 8;
            }


            for (int i = start; i < end; i++) {
                TimeCell timeCell = new TimeCell(i, list.get(i), pickerType);
                timeCell.getStyleClass().add("time-cell");

                if (i == selectedNumber) {
                    timeCell.getStyleClass().add("time-cell-selected");
                    selectedCell = timeCell;

                    if (pickerType == 0) {
                        currentHour = timeCell.getValue();
                    } else {
                        currentMinute = timeCell.getValue();
                    }

                    updateTimeString(textField);
                }

                timeCell.setOnMouseClicked(arg0 -> {
                    if (selectedCell != null) {
                        selectedCell.getStyleClass().remove("time-cell-selected");
                    }
                    selectedNumber = timeCell.getValue();
                    timeCell.getStyleClass().add("time-cell-selected");
                    selectedCell = timeCell;

                    if (pickerType == 0) {
                        currentHour = timeCell.getValue();
                    } else {
                        currentMinute = timeCell.getValue();
                    }

                    updateTimeString(textField);
                });

                super.getChildren().add(timeCell);
            }
        }


    }

    // utility methods
    private static void updateTimeString(TextField textField) {
        textField.setText(Integer.toString(currentHour) + ":" + Integer.toString(currentMinute) + " Uhr");
    }
}
