package com.very_serious_company;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*!
    Main program body frame
 */
public class Reminderro_GUI extends JFrame{
    private String save_location = "";                              /*!< current save file location */

    private static List<Blank_Note> notes = new ArrayList<>();      /*!< keeps notes on screen */
    private static int last_id;                                     /*!< last id used to generate new note */
    private static int[] mousePos = new int[2];                     /*!< mouse position variables */
    public static JFrame me;                                        /*!< this component */
    public static Blank_Note selected_Component;                    /*!< selected note using double click */

    /*!
        function to clear all notes in session
    */
    private void new_file(){
        for(Blank_Note note : notes) note.self_destruct();
        notes.clear();
        last_id=0;
        save_location="";
    }

    /*!
        function to read .vsc file and generate notes
    */
    private void open() throws Exception {
        Scanner fScn;
        String data, type;
        int x, y, width, height;

        JFileChooser fCh = new JFileChooser();
        fCh.setDialogTitle("New file location");
        fCh.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
        fCh.setFileFilter(new FileNameExtensionFilter(
                "Very Serious Company file", "vsc"));
        if(fCh.showOpenDialog(me) != 0 || fCh.getSelectedFile().getName().equals("")) return;
        new_file();
        fScn = new Scanner(new File(fCh.getSelectedFile().getPath()));
        save_location = fCh.getSelectedFile().getPath();

        while (fScn.hasNextLine()) {
            type = fScn.nextLine();
            data = fScn.nextLine();

            String[] token = data.split(" ");
            x = Integer.parseInt(token[0]);
            y = Integer.parseInt(token[1]);
            width = Integer.parseInt(token[2]);
            height = Integer.parseInt(token[3]);
            token[0] = fScn.nextLine();         //header
            token[1] = "";                      //body

            for(int i=Integer.parseInt(fScn.nextLine()); i > 0; i--) token[1] += fScn.nextLine() + "\n";

            switch (type) {
                case "[CN]":    //checklist note
                    add_note(x, y, width, height, me, null, true);
                    break;
                case "[CNT]":   //checklist note timer
                    add_note(x, y, width, height, me, LocalDate.parse(token[4], DateTimeFormatter.ofPattern("dd.MM.yyyy")), true);
                    break;
                case "[SN]":    //sticky note
                    add_note(x, y, width, height, me, null, false);
                    break;
                case "[SNT]":   //sticky note timer
                    add_note(x, y, width, height, me, LocalDate.parse(token[4], DateTimeFormatter.ofPattern("dd.MM.yyyy")), false);
                    break;
                default:
                    throw new Exception("Bad file");
            }
            notes.get(notes.size()-1).load(token[0], token[1]);
        }
        fScn.close();
    }

    /*!
        function to rewrite or create current save file
    */
    private void save() throws Exception {
        BufferedWriter writer;

        if(save_location.equals("")) save_as();
        new PrintWriter(save_location).close();
        writer = new BufferedWriter(new FileWriter(save_location));

        for(Blank_Note note : notes) writer.write(note.get_save_data());
        writer.close();
    }

    /*!
        function to create new save file
    */
    private void save_as() throws Exception {
        JFileChooser fCh = new JFileChooser();
        fCh.setDialogTitle("New file location");
        fCh.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
        fCh.setSelectedFile(new File("Table_1.vsc"));

        if(fCh.showOpenDialog(me) != 0 || fCh.getSelectedFile().getName().equals("")) throw new Exception("File location error");
        save_location = fCh.getSelectedFile().getPath();

        save();
    }

    /*!
        generates new note
    */
    public static void add_note(int x, int y, int width, int height, JFrame me, LocalDate time, boolean is_checkbox){
        Blank_Note new_note;

        if(x < 0 || y < 0){
            x = mousePos[0];
            y = mousePos[1];
        }

        //note type selection
        if(is_checkbox){
            if (time == null) new_note = new Checklist_note(last_id++, x, y, height, width);
            else new_note = new Timer_Checklist_note(last_id++, x, y, height, width, time);
        }
        else{
            if (time == null) new_note = new Sticky_Note(last_id++, x, y, height, width);
            else new_note = new Timer_Sticky_Note(last_id++, x, y, height, width, time);
        }

        notes.add(new_note);
        me.add(new_note.getJPanel());
        SwingUtilities.updateComponentTreeUI(me);
    }

    /*!
        delete note
    */
    public static void remove_note(Blank_Note note){
        notes.remove(note);
    }

    /*!
        select note with double right click
    */
    public static void select(Blank_Note note){
        select_clear();
        selected_Component = note;
        selected_Component.selection_reaction();
    }

    /*!
        deselect note
    */
    private static void select_clear(){
        if(selected_Component != null) selected_Component.deselection_reaction();
        selected_Component = null;
    }

    public Reminderro_GUI(){
        last_id = 0;
        me = this;

        /*!
            create popup menu where you can create new notes
        */
        final JPopupMenu popupMenu = new JPopupMenu("New");
        JMenuItem createNew_txtNote = new JMenuItem("Text note");
        JMenuItem createNew_TimerNote = new JMenuItem("Text note with timer");
        JMenuItem createNew_checkboxNote = new JMenuItem("Checkbox note");
        JMenuItem createNew_checkboxTimerNote = new JMenuItem("Checkbox with timer");
        popupMenu.add(createNew_txtNote);
        popupMenu.add(createNew_TimerNote);
        popupMenu.add(createNew_checkboxNote);
        popupMenu.add(createNew_checkboxTimerNote);

        /*!
            add actions to popup menu
        */
        createNew_txtNote.addActionListener(e -> {
            CreateNoteDialog d = new CreateNoteDialog(me, "Create", false);
            d.show();
            SwingUtilities.updateComponentTreeUI(me);
        });

        createNew_TimerNote.addActionListener(e -> {
            CreateNoteDialog d = new CreateTimerNoteDialog(me, "Create", false);
            d.show();
            SwingUtilities.updateComponentTreeUI(me);
        });

        createNew_checkboxNote.addActionListener(e -> {
            CreateNoteDialog d = new CreateNoteDialog(me, "Create", true);
            d.show();
            SwingUtilities.updateComponentTreeUI(me);
        });

        createNew_checkboxTimerNote.addActionListener(e -> {
            CreateNoteDialog d = new CreateTimerNoteDialog(me, "Create", true);
            d.show();
            SwingUtilities.updateComponentTreeUI(me);
        });
        this.add(popupMenu);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getButton() == 1) select_clear();
                else {
                    mousePos[0] = e.getX();
                    mousePos[1] = e.getY();
                    popupMenu.show(me, e.getX(), e.getY());
                }
            }
        });

        /*!
            menu configuration
        */
        JMenu submenu_File, submenu_Edit, submenu_Credits;
        JMenuBar mb=new JMenuBar();
        JMenuItem save, save_as, open, new_file, show_credits;
        save=new JMenuItem("Save");
        new_file=new JMenuItem("New");
        save_as=new JMenuItem("Save as..");
        open=new JMenuItem("Open");
        show_credits = new JMenuItem("Credits");

        submenu_File = new JMenu("File");
        submenu_File.add(new_file);
        submenu_File.add(open);
        submenu_File.add(save);
        submenu_File.add(save_as);

        new_file.addActionListener(actionEvent -> new_file());
        open.addActionListener(actionEvent -> {
            try {
                open();
            } catch (Exception e) {
                new_file();
                JOptionPane.showMessageDialog(null, e.getMessage(), "Task failed successfully",JOptionPane.WARNING_MESSAGE);
            }
        });

        save.addActionListener(actionEvent -> {
            try {
                save();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Task failed successfully",JOptionPane.WARNING_MESSAGE);
            }
        });

        save_as.addActionListener(actionEvent -> {
            try {
                save_as();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Task failed successfully",JOptionPane.WARNING_MESSAGE);
            }
        });

        submenu_Credits = new JMenu("Credits");
        submenu_Credits.add(show_credits);
        show_credits.addActionListener(actionEvent -> System.out.println("Credits"));

        submenu_Edit = new JMenu("Edit");
        mb.add(submenu_File); mb.add(submenu_Edit); mb.add(submenu_Credits);

        this.setJMenuBar(mb);

        this.setFocusable(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
