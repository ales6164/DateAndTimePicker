package app;


import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class DateChooserSkin extends SkinBase<DateChooser> {

    private final TextField textField;
    private final Date date;
    private final Label month;
    private final BorderPane content;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMM yyyy");

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
        // this date is the selected date
        date = dateChooser.getDate();
        textField = dateChooser.getTextField();
        final DatePickerPane calendarPane = new DatePickerPane(date);

        month = new Label(simpleDateFormat.format(calendarPane.getShownMonth()));
        HBox hbox = new HBox();

        // create the navigation Buttons
        Button monthBack = new Button("<");
        monthBack.getStyleClass().add("month-arrow");
        monthBack.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            calendarPane.forward(-1);
        });
        Button monthForward = new Button(">");
        monthForward.getStyleClass().add("month-arrow");
        monthForward.addEventHandler(ActionEvent.ACTION, event -> {
            event.consume();
            calendarPane.forward(1);
        });

        // center the label and make it grab all free space
        HBox.setHgrow(month, Priority.ALWAYS);
        //month.setMaxWidth(Double.MAX_VALUE);
        month.getStyleClass().add("month-label");
        hbox.getChildren().addAll(monthBack, month, monthForward);
        hbox.setAlignment(Pos.CENTER);

        // use a BorderPane to Layout the view
        content = new BorderPane();
        getChildren().add(content);
        content.setTop(hbox);
        content.setCenter(calendarPane);
    }

    /**
     * @author eppleton
     */
    class DatePickerPane extends Region {

        private final Date selectedDate;
        private final Calendar cal;
        private CalendarCell selectedDayCell;
        // this is used to format the day cells
        private final SimpleDateFormat sdf = new SimpleDateFormat("d");
        // empty cell header of weak-of-year row
        //private final CalendarCell woyCell = new CalendarCell(new Date(), "");
        private int rows, columns;//default

        public DatePickerPane(Date date) {
            setPrefSize(250, 200);
            //woyCell.getStyleClass().add("week-of-year-cell");
            //setPadding(new Insets(5, 0, 5, 0));
            this.columns = 6;
            this.rows = 5;

            // use a copy of Date, because it's mutable
            // we'll helperDate it through the month
            cal = Calendar.getInstance();
            Date helperDate = new Date(date.getTime());
            cal.setTime(helperDate);
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            // the selectedDate is the date we will change, when a date is picked
            selectedDate = date;
            refresh();
        }

        /**
         * Move forward the specified number of Months, move backward by using
         * negative numbers
         *
         * @param i
         */
        public void forward(int i) {
            cal.add(Calendar.MONTH, i);
            month.setText(simpleDateFormat.format(cal.getTime()));
            refresh();
        }

        private void refresh() {
            super.getChildren().clear();
            this.rows = 5; // most of the time 5 rows are ok
            Date copy = new Date(cal.getTime().getTime());

            // Display a styleable row of localized weekday symbols 
            DateFormatSymbols symbols = new DateFormatSymbols();
            symbols.setShortWeekdays(new String[]{"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"});
            String[] dayNames = symbols.getShortWeekdays();

            for (int i = 0; i < 7; i++) { // array starts with an empty field
                CalendarCell calendarCell = new CalendarCell(cal.getTime(), dayNames[i]);
                if(i > 4) {
                    calendarCell.getStyleClass().addAll("weekday-cell", "weekday-cell-weekend");
                } else {
                    calendarCell.getStyleClass().add("weekday-cell");
                }
                super.getChildren().add(calendarCell);
            }

            // find out which month we're displaying
            final int month = cal.get(Calendar.MONTH);
            int weekday = cal.get(Calendar.DAY_OF_WEEK);

            cal.set(Calendar.DAY_OF_MONTH, 1);
            System.out.println(cal.get(Calendar.DAY_OF_MONTH));

            // if the first day is a sunday we need to rewind 7 days otherwise the 
            // code below would only start with the second week. There might be 
            // better ways of doing this...
            if (weekday != Calendar.MONDAY) {
                // it might be possible, that we need to add a row at the end as well...

                Calendar check = Calendar.getInstance();
                check.setTime(copy);
                check.setFirstDayOfWeek(Calendar.MONDAY);

                int lastDate = check.getActualMaximum(Calendar.DATE);
                System.out.println(lastDate);
                System.out.println(Calendar.SUNDAY);
                System.out.println(Calendar.MONDAY);
                check.set(Calendar.DATE, 1);
                int weekday2 = check.get(Calendar.DAY_OF_WEEK);
                if (weekday2 == Calendar.SUNDAY) {
                    weekday2 = 8;
                }
                System.out.println(lastDate + weekday2 - 1);
                if ((lastDate + weekday2 - 1) > 36) {
                    rows = 6;
                }
            }
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);


            // used to identify and style the cell with the selected date;
            Calendar testSelected = Calendar.getInstance();
            testSelected.setTime(selectedDate);

            for (int i = 0; i < (rows); i++) {
                for (int j = 0; j <= columns; j++) {
                    System.out.println(j);
                    String formatted = sdf.format(cal.getTime());
                    System.out.println("formatted: " + formatted);
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
                        textField.setText(dt1.format(date));
                    });

                    // grow the hovered cell in size  
                    dayCell.setOnMouseEntered(e -> {
                        dayCell.setScaleX(1.1);
                        dayCell.setScaleY(1.1);
                    });

                    dayCell.setOnMouseExited(e -> {
                        dayCell.setScaleX(1);
                        dayCell.setScaleY(1);
                    });

                    super.getChildren().add(dayCell);
                    cal.add(Calendar.DATE, 1);
                }
            }
            cal.setTime(copy);
        }

        /**
         * Overriden, don't add Children directly
         *
         * @return unmodifieable List
         */
        @Override
        protected ObservableList<Node> getChildren() {
            return FXCollections.unmodifiableObservableList(super.getChildren());
        }

        /**
         * get the current month our calendar displays. Should always give you the
         * correct one, even if some days of other mnths are also displayed
         *
         * @return
         */
        public Date getShownMonth() {
            return cal.getTime();
        }

        @Override
        protected void layoutChildren() {
            ObservableList<Node> children = getChildren();
            double width = getWidth();
            double height = getHeight();

            double cellWidth = (width / (columns + 1));
            double cellHeight = height / (rows + 1);

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
    }
    // utility methods

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
