package com.very_serious_company;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateTimerNoteDialog extends CreateNoteDialog {
    JTextField timer_field;

    void checkout(){
        int height;
        int width;
        LocalDate timer;
        try{
            height = Integer.parseInt(height_field.getText());
            width = Integer.parseInt(width_field.getText());

            if((isCheckbox ? (height < 1 || height > 25) : (height < 150 || height > 1000))) {
                height_field.setText(isCheckbox ? "5" : "150");
                throw new Exception(isCheckbox ? "Bad rows number" : "Bad height range!");
            }
            if(width < 150 || width > 1000){
                width_field.setText("150");
                throw new Exception("Bad width range!");
            }

            timer = LocalDate.parse(timer_field.getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
        catch (Exception exc){
            JOptionPane.showMessageDialog(null,exc.getMessage(),"Task failed successfully",JOptionPane.WARNING_MESSAGE);
            return;
        }

        Reminderro_GUI.add_note(-1, -1, width, height, owner, timer, isCheckbox);
        d.setVisible(false);
    }

    CreateTimerNoteDialog(JFrame owner, String title, boolean is_checkbox){
        super(owner, title, is_checkbox);

        timer_field = new JTextField("09.02.2001");
        timer_field.setBorder(BorderFactory.createLineBorder(Color.black));///////////DEBUG
        field_pattern.gridx = 0;
        field_pattern.gridy = 1;
        field_pattern.gridwidth = 2;
        d.add(timer_field, field_pattern);
    }
}
