package com.very_serious_company;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Timer_Checklist_note extends Checklist_note{
    public LocalDate timer;
    JLabel expire_time;
    private int maximum_checkboxes;

    public void refresh_date(){
        LocalDate current_date = LocalDate.now();
        Period period = Period.between(current_date, timer);
        expire_time.setText(period.getYears() + "y :" + period.getMonths() + "m :" + period.getDays() + "d");
    }

    @Override
    protected String get_parameters(){
        return ("[CNT]" +"\n"+ panel.getLocation().x +" "+ panel.getLocation().y +" "+ panel.getWidth() +" "+
                maximum_checkboxes +" "+ timer.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    Timer_Checklist_note(int id, int x, int y, int rows, int width, LocalDate _expires){
        super(id, x, y, rows+1, width);
        maximum_checkboxes = rows;

        expire_time = new JLabel(_expires.toString(), SwingConstants.CENTER);
        timer = _expires;

        GridBagConstraints footer_pattern = new GridBagConstraints();

        footer_pattern.fill = GridBagConstraints.HORIZONTAL;
        footer_pattern.gridx = 0;
        footer_pattern.gridy = 3;
        footer_pattern.insets = new Insets(0, 4,4,4);
        footer_pattern.ipady = 10;

        panel.add(expire_time, footer_pattern);

        refresh_date();
    }
}
