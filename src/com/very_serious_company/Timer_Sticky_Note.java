package com.very_serious_company;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Timer_Sticky_Note extends Sticky_Note{
    public LocalDate timer; /*!< note timer */
    JLabel expire_time; /*!< time remaining */

    @Override
    protected String get_parameters(){
        return ("[SNT]" +"\n"+ panel.getLocation().x +" "+ panel.getLocation().y +" "+ panel.getWidth() +" "+
                panel.getHeight() +" "+ timer.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    /*!
        refresh timer
    */
    public void refresh_date(){
        LocalDate current_date = LocalDate.now();
        Period period = Period.between(current_date, timer);
        expire_time.setText(period.getYears() + "y :" + period.getMonths() + "m :" + period.getDays() + "d");
    }

    Timer_Sticky_Note(int id, int x, int y, int height, int width, LocalDate _expires){
        super(id, x, y, height, width);
        expire_time = new JLabel(_expires.toString(), SwingConstants.CENTER);
        timer = _expires;

        GridBagConstraints footer_pattern = new GridBagConstraints();

        footer_pattern.fill = GridBagConstraints.HORIZONTAL;
        footer_pattern.gridx = 0;
        footer_pattern.gridy = 2;
        footer_pattern.insets = new Insets(0, 4,4,4);
        footer_pattern.ipady = 10;

        panel.add(expire_time, footer_pattern);

        refresh_date();
    }
}
