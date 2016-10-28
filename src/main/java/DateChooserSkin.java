package main.java;


import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.*;

public class DateChooserSkin extends SkinBase<DateChooser> {

    private final Date selectedValue;
    private final Label month;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMM yyyy");

    private static class CalendarCell extends StackPane {

        private final Date date;

        public CalendarCell(Date day, String text) {
            this.getStyleClass().add("any-cell");
            this.date = day;
            Label label = new Label(text);
            getChildren().add(label);
        }

        public Date getDate() {
            return date;
        }
    }

    public DateChooserSkin(DateChooser dateChooser) {
        super(dateChooser);
        selectedValue = dateChooser.getSelectedValue();
        final DatePickerPane calendarPane = new DatePickerPane(selectedValue);
        calendarPane.setDateEventListener(cell -> dateChooser.setSelectedValue(cell.getDate()));

        month = new Label(simpleDateFormat.format(calendarPane.getShownMonth()));
        HBox hbox = new HBox();

        Button monthBack = new Button("◀");
        monthBack.getStyleClass().add("month-arrow");
        monthBack.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            calendarPane.forward(-1);
        });
        Button monthForward = new Button("▶");
        monthForward.getStyleClass().add("month-arrow");
        monthForward.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            calendarPane.forward(1);
        });


        HBox.setHgrow(month, Priority.ALWAYS);
        month.getStyleClass().add("month-label");
        hbox.getChildren().addAll(monthBack, month, monthForward);
        hbox.setAlignment(Pos.CENTER);

        BorderPane content = new BorderPane();
        getChildren().add(content);
        content.setTop(hbox);
        content.setCenter(calendarPane);
    }

    class DatePickerPane extends Region {

        private final Date selectedDate;
        private final Calendar cal;
        private CalendarCell selectedDayCell;
        private final SimpleDateFormat sdf = new SimpleDateFormat("d");
        private int rows, columns;
        private DateEventListener listener;

        public DatePickerPane(Date date) {
            setPrefSize(250, 200);
            this.columns = 6;
            this.rows = 5;

            cal = Calendar.getInstance();
            Date helperDate = new Date(date.getTime());
            cal.setTime(helperDate);
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            selectedDate = date;
            refresh();
        }

        public void forward(int i) {
            cal.add(Calendar.MONTH, i);
            month.setText(simpleDateFormat.format(cal.getTime()));
            refresh();
        }

        private void refresh() {
            super.getChildren().clear();
            this.rows = 5;
            Date copy = new Date(cal.getTime().getTime());

            DateFormatSymbols symbols = new DateFormatSymbols();
            symbols.setShortWeekdays(new String[]{"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"});
            String[] dayNames = symbols.getShortWeekdays();

            for (int i = 0; i < 7; i++) {
                CalendarCell calendarCell = new CalendarCell(cal.getTime(), dayNames[i]);
                if(i > 4) {
                    calendarCell.getStyleClass().addAll("weekday-cell", "weekday-cell-weekend");
                } else {
                    calendarCell.getStyleClass().add("weekday-cell");
                }
                super.getChildren().add(calendarCell);
            }

            final int month = cal.get(Calendar.MONTH);
            int weekday = cal.get(Calendar.DAY_OF_WEEK);

            cal.set(Calendar.DAY_OF_MONTH, 1);
            System.out.println(cal.get(Calendar.DAY_OF_MONTH));

            if (weekday != Calendar.MONDAY) {
                Calendar check = Calendar.getInstance();
                check.setTime(copy);
                check.setFirstDayOfWeek(Calendar.MONDAY);

                int lastDate = check.getActualMaximum(Calendar.DATE);
                System.out.println(lastDate);
                check.set(Calendar.DATE, 1);
                int weekday2 = check.get(Calendar.DAY_OF_WEEK);
                if (weekday2 == Calendar.SUNDAY) {
                    weekday2 = 8;
                }
                if ((lastDate + weekday2 - 1) > 36) {
                    rows = 6;
                }
            }
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            Calendar testSelected = Calendar.getInstance();
            testSelected.setTime(selectedDate);

            for (int i = 0; i < (rows); i++) {
                for (int j = 0; j <= columns; j++) {
                    String formatted = sdf.format(cal.getTime());
                    System.out.println(formatted);
                    final CalendarCell dayCell = new CalendarCell(cal.getTime(), formatted);
                    dayCell.getStyleClass().add("calendar-cell");
                    if (cal.get(Calendar.MONTH) != month) {
                        dayCell.getStyleClass().add("calendar-cell-inactive");
                    } else {
                        if (isSameDay(testSelected, cal)) {
                            dayCell.getStyleClass().add("calendar-cell-selected");
                            selectedDayCell = dayCell;
                        }
                        if (isToday(cal)) {
                            dayCell.getStyleClass().add("calendar-cell-today");
                        }
                    }
                    dayCell.setOnMouseClicked(arg0 -> {
                        if (selectedDayCell != null) {
                            selectedDayCell.getStyleClass().add("calendar-cell");
                            selectedDayCell.getStyleClass().remove("calendar-cell-selected");
                        }
                        selectedDate.setTime(dayCell.getDate().getTime());
                        dayCell.getStyleClass().remove("calendar-cell");
                        dayCell.getStyleClass().add("calendar-cell-selected");
                        selectedDayCell = dayCell;
                        Calendar checkMonth = Calendar.getInstance();
                        checkMonth.setTime(dayCell.getDate());

                        if (checkMonth.get(Calendar.MONTH) != month) {
                            forward(checkMonth.get(Calendar.MONTH) - month);
                        }

                        SimpleDateFormat dt1 = new SimpleDateFormat("dd. MM. yyyy");
                        if (listener != null) listener.onDateChange(dayCell);
                    });

                    super.getChildren().add(dayCell);
                    cal.add(Calendar.DATE, 1);
                }
            }
            cal.setTime(copy);
        }

        @Override
        protected ObservableList<Node> getChildren() {
            return FXCollections.unmodifiableObservableList(super.getChildren());
        }

        public Date getShownMonth() {
            return cal.getTime();
        }

        @Override
        protected void layoutChildren() {
            ObservableList<Node> children = getChildren();
            double width = getWidth();
            double height = getHeight();

            double cellWidth = (width / (columns + 1));
            double cellHeight = height / (6 + 1);

            for (int i = 0; i < (rows + 1); i++) {
                for (int j = 0; j < (columns + 1); j++) {
                    if (children.size() <= ((i * (columns + 1)) + j)) {
                        break;
                    }
                    Node get = children.get((i * (columns + 1)) + j);
                    layoutInArea(get, j * cellWidth, i * cellHeight, cellWidth, cellHeight, 0.0d, HPos.LEFT, VPos.TOP);
                }

            }
        }

        public void setDateEventListener(DateEventListener listener) {
            this.listener = listener;
        }
    }

    private interface DateEventListener {
        void onDateChange(CalendarCell cell);
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    private static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }
}
