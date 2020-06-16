package com.very_serious_company;

import javax.swing.*;
import java.awt.*;

/*!
    note with text filed and header
*/
public class Sticky_Note extends Blank_Note{
    private GridBagConstraints header_pattern;      /*!< pattern for header */
    private JLabel offline_header;                  /*!< header component in deselected state */
    private JTextField online_header;               /*!< header component in selected state */
    private JTextArea note_text;                    /*!< text field component */

    /*!
        function called when user select this note (change style)
    */
    @Override
    public void selection_reaction(){
        panel.setBackground(Color.red);

        panel.remove(offline_header);
        panel.add(online_header, header_pattern);

        note_text.setEditable(true);
        note_text.getCaret().setVisible(true);
    }

    /*!
        function called when user deselect this note (change style)
    */
    @Override
    public void deselection_reaction(){
        panel.setBackground(Color.cyan);

        panel.remove(online_header);
        offline_header.setText(online_header.getText());
        panel.add(offline_header, header_pattern);

        note_text.setEditable(false);
        note_text.getCaret().setVisible(false);
    }

    /*!
        function called when Reminderro_GUI creates current note and then load data into it
    */
    @Override
    public void load(String head_text, String body_text) {
        offline_header.setText(head_text);
        online_header.setText(head_text);

        note_text.setText(body_text);
    }

    /*!
        function returns string with current note parameters to save into file:
        type, location on frame, width and height
    */
    protected String get_parameters(){
        return ("[SN]" +"\n"+ panel.getLocation().x +" "+ panel.getLocation().y +" "+ panel.getWidth() +" "+ panel.getHeight());
    }

    /*!
        function called by Reminderro_GUI to get string which fully defines this note
    */
    @Override
    public String get_save_data() {
        String data = get_parameters();
        data += "\n" + online_header.getText();

        if(note_text.getText().split("\n").length > 0){
            data += "\n" + note_text.getText().split("\n").length;
            data += "\n" + note_text.getText();
        }

        return (data);
    }

    Sticky_Note(int id, int x, int y, int height, int width) {
        super(id,x,y,height,width);

        panel.setLayout(new GridBagLayout());
        header_pattern = new GridBagConstraints();
        GridBagConstraints text_pattern = new GridBagConstraints();

        header_pattern.ipadx = panel.getWidth() - 80;

        header_pattern.fill = GridBagConstraints.HORIZONTAL;
        header_pattern.gridx = 0;
        header_pattern.gridy = 0;
        header_pattern.insets = new Insets(4, 4,2,4);
        header_pattern.ipady = 10;

        text_pattern.fill = GridBagConstraints.BOTH;
        text_pattern.gridx = 0;
        text_pattern.gridy = 1;
        text_pattern.weighty = 1;
        text_pattern.insets = new Insets(2, 4,4,4);

        online_header = new JTextField("Hello World!");

        offline_header = new JLabel(online_header.getText(), SwingConstants.CENTER);
        offline_header.setBorder(BorderFactory.createLineBorder(Color.black));///////////DEBUG
        offline_header.setMaximumSize(online_header.getPreferredSize());
        offline_header.setMinimumSize(online_header.getPreferredSize());
        offline_header.setPreferredSize(online_header.getPreferredSize());
        panel.add(offline_header, header_pattern);

        note_text = new JTextArea("NewNote");

        note_text.setLineWrap(true);
        note_text.getCaret().setVisible(false);

        note_text.setBorder(BorderFactory.createLineBorder(Color.red));///////////DEBUG
        note_text.setEditable(false);
        note_text.setMaximumSize(note_text.getPreferredSize());
        note_text.setMinimumSize(note_text.getPreferredSize());
        note_text.setPreferredSize(note_text.getPreferredSize());
        panel.add(note_text, text_pattern);

        online_header.setMaximumSize(offline_header.getPreferredSize());
        online_header.setMinimumSize(offline_header.getPreferredSize());
        online_header.setPreferredSize(offline_header.getPreferredSize());
    }
}
