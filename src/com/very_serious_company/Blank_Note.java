package com.very_serious_company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*!
    template for all notes
*/
public abstract class Blank_Note {
    protected JPanel panel; /*!< note body */
    public int my_id; /*!< note id */

    /*!
        get note body
    */
    public JPanel getJPanel(){
        return panel;
    }

    /*!
        actions when note is selected
    */
    public abstract void selection_reaction();

    /*!
        actions when note is deselected
    */
    public abstract void deselection_reaction();

    /*!
        load data into note
    */
    public abstract void load(String head_text, String body_text);

    /*!
        remove this note visually
    */
    public void self_destruct(){
        Reminderro_GUI.me.remove(panel);
        SwingUtilities.updateComponentTreeUI(Reminderro_GUI.me);
    }

    /*!
        return this note defined in string
    */
    public abstract String get_save_data();

    Blank_Note( int id, int x, int y, int height, int width) {
        my_id = id;
        Blank_Note me = this;
        panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBackground(Color.cyan);
        panel.setFocusable(true);

        //DELETE key listener
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 127 && Reminderro_GUI.selected_Component == me){
                    self_destruct();
                    Reminderro_GUI.remove_note(me);
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {}
        });

        panel.addMouseListener(new MouseAdapter() {
            private Note_Controller C_note_controller = new Note_Controller();

            class Note_Controller implements Runnable {
                private volatile boolean exit;

                public void run() {
                    exit = false;
                    Point mouse_position, delta;
                    delta = MouseInfo.getPointerInfo().getLocation();
                    delta.translate(-panel.getX(), -panel.getY());
                    while (!exit) {
                        mouse_position = MouseInfo.getPointerInfo().getLocation();
                        mouse_position.translate(-delta.x, -delta.y);
                        panel.setLocation(mouse_position);
                    }
                }
                public void stop() {
                    exit = true;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) panel.requestFocus();
                else if (e.getClickCount() == 2) Reminderro_GUI.select(me);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Thread t_controller = new Thread(C_note_controller);
                t_controller.start();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                C_note_controller.stop();
            }
        });
    }
}
