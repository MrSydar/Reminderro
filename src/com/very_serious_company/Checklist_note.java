package com.very_serious_company;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Checklist_note extends Blank_Note {
    private GridBagConstraints header_pattern, tick_pattern;
    private JLabel offline_header;
    private JTextField online_header;
    private JPanel checklist_area;
    private List<JCheckBox> checkboxes = new ArrayList<>();
    private JButton bAdd;
    private int maximum_checkboxes; /*!< number of maximum checkboxes in body */
    private Font checked_font, unchecked_font; /*!< fonts for text */

    @Override
    public void selection_reaction() {
        panel.setBackground(Color.red);

        panel.remove(offline_header);
        panel.add(online_header, header_pattern);

        if(checkboxes.size() + 1 <= maximum_checkboxes) checklist_area.add(bAdd, tick_pattern);

        SwingUtilities.updateComponentTreeUI(panel);
    }

    @Override
    public void deselection_reaction() {
        panel.setBackground(Color.cyan);

        panel.remove(online_header);
        offline_header.setText(online_header.getText());
        panel.add(offline_header, header_pattern);

        checklist_area.remove(bAdd);
    }

    @Override
    public void load(String head_text, String body_text) {
        String[] checks = body_text.split("\n"), constructor;
        System.out.println(body_text);
        offline_header.setText(head_text);
        online_header.setText(head_text);
        for(String tmp_check : checks){
            constructor = tmp_check.split(" ", 2);
            addCheck(constructor[0].equals("T"), constructor[1]);
        }
    }

    protected String get_parameters(){
        return ("[CN]" +"\n"+ panel.getLocation().x +" "+ panel.getLocation().y +" "+ panel.getWidth() +" "+ maximum_checkboxes);
    }

    @Override
    public String get_save_data() {
        String data = get_parameters();
        data += "\n" + online_header.getText();
        data += "\n" + checkboxes.size();
        for (JCheckBox checkbox : checkboxes) data += "\n" + (checkbox.isSelected() ? "T " : "F ") + checkbox.getText();

        return (data + "\n");
    }

    /*!
        add new checkbox
    */
    private void addCheck(boolean checked, String check_text){
        JCheckBox new_check = new JCheckBox(check_text);
        new_check.setFont(checked ? checked_font : unchecked_font);
        new_check.setSelected(checked);

        new_check.addItemListener(e1 -> new_check.setFont(e1.getStateChange() == ItemEvent.SELECTED ? checked_font : unchecked_font));

        checkboxes.add(new_check);
        checklist_area.add(checkboxes.get(checkboxes.size()-1), tick_pattern);
        tick_pattern.gridy++;
    }

    Checklist_note(int id, int x, int y, int rows, int width) {
        super(id, x, y, 55 + 24 * rows, width);
        if(this instanceof Timer_Checklist_note) rows--;
        maximum_checkboxes = rows;

        Font font = new Font("arial", Font.BOLD, 12);
        Map<TextAttribute, Object>  attributes = new HashMap<>(font.getAttributes());
        unchecked_font = new Font(attributes);
        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        checked_font = new Font(attributes);

        GridBagLayout tmp = new GridBagLayout();
        panel.setLayout(tmp);

        bAdd = new JButton("+");
        bAdd.setMargin(new Insets(0, 0, 0, 0));
        bAdd.setPreferredSize(new Dimension(25,25));
        tick_pattern = new GridBagConstraints();
        tick_pattern.gridy = 0;
        tick_pattern.anchor = GridBagConstraints.WEST;

        bAdd.addActionListener(e -> {
            AtomicBoolean decision = new AtomicBoolean(false);

            JDialog d = new JDialog(Reminderro_GUI.me, "New checkbox", true);
            d.setLocationRelativeTo(null);
            d.setResizable(false);
            d.setLayout(new GridBagLayout());

            JButton create_button = new JButton("Create");
            JButton cancel_button = new JButton("Cancel");
            GridBagConstraints field_pattern = new GridBagConstraints();
            GridBagConstraints button_pattern = new GridBagConstraints();

            field_pattern.fill = GridBagConstraints.HORIZONTAL;
            field_pattern.insets = new Insets(4, 4,4,4);
            field_pattern.ipady = 8;
            field_pattern.gridwidth = 2;

            JTextField textField = new JTextField("text");
            textField.setBorder(BorderFactory.createLineBorder(Color.black));///////////DEBUG
            d.add(textField, field_pattern);

            button_pattern.gridy = 2;
            button_pattern.insets = new Insets( 4, 4, 4, 4);

            button_pattern.gridx = 0;
            d.add(create_button, button_pattern);
            button_pattern.gridx = 1;
            d.add(cancel_button, button_pattern);

            d.setMinimumSize(new Dimension(300, 150));

            create_button.addActionListener (res -> {
                if(textField.getText().length() * 6 > width-100)
                    JOptionPane.showMessageDialog(null, "Too long input!","Task failed successfully", JOptionPane.WARNING_MESSAGE);
                else {
                    decision.set(true);
                    d.setVisible(false);
                }
            });
            cancel_button.addActionListener (res -> d.setVisible(false));
            d.setVisible(true);

            if(!decision.get()) return;

            checklist_area.remove(bAdd);

            addCheck(false, textField.getText());

            if (checkboxes.size() + 1 <= maximum_checkboxes) checklist_area.add(bAdd, tick_pattern);
            SwingUtilities.updateComponentTreeUI(checklist_area);
        });

        GridBagConstraints checklistArea_pattern= new GridBagConstraints();
        checklist_area = new JPanel();

        checklist_area.setLayout(new GridBagLayout());

        online_header = new JTextField("Hello World!");
        online_header.setBorder(BorderFactory.createLineBorder(Color.black));///////////DEBUG

        offline_header = new JLabel(online_header.getText(), SwingConstants.CENTER);
        offline_header.setBorder(BorderFactory.createLineBorder(Color.black));///////////DEBUG

        header_pattern = new GridBagConstraints();
        header_pattern.fill = GridBagConstraints.HORIZONTAL;
        header_pattern.weightx = 1;
        header_pattern.gridx = 0;
        header_pattern.gridy = 0;
        header_pattern.insets = new Insets(4, 4, 2, 4);
        header_pattern.ipady = 10;

        panel.add(offline_header, header_pattern);

        checklistArea_pattern.fill = GridBagConstraints.BOTH;
        checklistArea_pattern.weighty = 1;
        checklistArea_pattern.insets = new Insets(2, 4, 4, 4);
        checklistArea_pattern.gridx = 0;
        checklistArea_pattern.gridy = 1;

        panel.add(checklist_area, checklistArea_pattern);
    }
}
