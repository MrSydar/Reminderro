package com.very_serious_company;

import javax.swing.*;
import java.awt.*;

public class CreateNoteDialog{
    JDialog d;
    JButton create_button;
    GridBagConstraints field_pattern;
    JTextField height_field;
    JTextField width_field;
    JFrame owner;
    boolean isCheckbox;

    void checkout(){
        int height;
        int width;

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
        }
        catch (Exception exc){
            JOptionPane.showMessageDialog( null,exc.getMessage(),"Task failed successfully",JOptionPane.WARNING_MESSAGE);
            return;
        }

        Reminderro_GUI.add_note( -1, -1, width,height, owner, null, isCheckbox);
        d.setVisible(false);
    }

    public void show(){
        d.setVisible(true);
    }

    CreateNoteDialog(JFrame _owner, String title, boolean is_checkbox){
        owner = _owner;
        isCheckbox = is_checkbox;
        d = new JDialog(owner, title, true);
        d.setLocationRelativeTo(null);
        d.setResizable(false);
        d.setLayout( new GridBagLayout() );

        create_button = new JButton("Create");
        JButton cancel_button = new JButton("Cancel");
        field_pattern = new GridBagConstraints();
        GridBagConstraints button_pattern = new GridBagConstraints();

        field_pattern.fill = GridBagConstraints.HORIZONTAL;
        field_pattern.insets = new Insets(4, 10,4,10);
        field_pattern.ipadx = 60;
        field_pattern.ipady = 8;

        width_field = new JTextField("width");
        width_field.setBorder(BorderFactory.createLineBorder(Color.black));///////////DEBUG
        field_pattern.gridx = 0;
        field_pattern.gridy = 0;
        d.add(width_field, field_pattern);

        height_field = new JTextField(is_checkbox ? "rows" : "height");
        height_field.setBorder(BorderFactory.createLineBorder(Color.black));///////////DEBUG
        field_pattern.gridx = 1;
        field_pattern.gridy = 0;
        d.add(height_field, field_pattern);

        button_pattern.gridy = 2;
        button_pattern.insets = new Insets( 4, 4, 4, 4);

        button_pattern.gridx = 0;
        d.add(create_button, button_pattern);

        button_pattern.gridx = 1;
        d.add(cancel_button, button_pattern);

        d.setMinimumSize(new Dimension(300, 150));

        create_button.addActionListener (e -> checkout());

        cancel_button.addActionListener (e -> d.setVisible(false));
    }
}
