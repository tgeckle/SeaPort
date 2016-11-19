import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Filename: SeaPortGUI.java
 * Author: Theresa Geckle
 * Date: 11/2/2016
 * Purpose: Implements a GUI for displaying and testing the methods and fields
 * * of the classes for the SeaPort Project. 
 * * * Takes a .txt input file and builds a World from the data.
 * * * Offers options to search the World for Ships, Docks, Ports, and Persons
 * * that meet the search criteria.
 */
public class SeaPortGUI extends JFrame {

    private final String[] SEARCH_OPTIONS = {"Name", "Index", "Type", "Skill",
        "Parent ID"}; // list of options for search criteria
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
    World theWorld; // All ports etc. are held within the World

    public SeaPortGUI() {
        super();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize JPanels
        JPanel frame = new JPanel(new BorderLayout());
        frame.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel filePane = new JPanel(new BorderLayout(50, 0));
        filePane.setBorder(new EmptyBorder(0, 40, 10, 60));
        JPanel searchPane = new JPanel(new BorderLayout(20, 0));
        searchPane.setBorder(new EmptyBorder(0, 20, 10, 20));
        
        JPanel topPane = new JPanel(new BorderLayout());
        JPanel searchOptionsPane = new JPanel(new BorderLayout(10, 0));

        // Initialize Components
        JFileChooser chooser = new JFileChooser();
        JTextField fileField = new JTextField(40);
        fileField.setEditable(false);
        JTextField searchField = new JTextField(22);
        JComboBox options = new JComboBox(SEARCH_OPTIONS);

        JButton fileButton = new JButton("Open File");
        JButton searchButton = new JButton("Search");

        JTextArea outputTextArea = new JTextArea(25,60);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        outputTextArea.setEditable(false);

        // Listeneres
        // Listener for file input button
        fileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                theWorld = new World();
                chooser.setDialogTitle("Choose Input File");
                chooser.setFileFilter(filter);

                int returnVal = chooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        fileField.setText(chooser.getSelectedFile().getAbsolutePath());
                        Scanner input = new Scanner(chooser.getSelectedFile());
                        theWorld.readFile(input);

                        outputTextArea.setText(theWorld.toString());
                    } catch (FileNotFoundException exc) {
                        JOptionPane.showMessageDialog(null, "File not found."
                                + "Please try a different file. ", "File Not Found",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }

            }
        });
        
        // Listener for the search button
        searchButton.addActionListener(new ActionListener() {
            String result = "";

            public void actionPerformed(ActionEvent e) {
                if (searchField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please input text to "
                            + "search.", "Empty Input",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    switch (options.getSelectedIndex()) {
                        case 0: // Search by name
                            result = theWorld.searchNames(searchField.
                                    getText());
                            break;
                        case 1: // search by index
                            try {
                                result = theWorld.searchIndices(Integer.parseInt(searchField.
                                        getText()));
                            }
                            catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Please input "
                                        + "a number.", "Invalid Input", 
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            break;
                        case 2: // Search by type
                            result = theWorld.searchType(searchField.
                                    getText());
                            break;
                        case 3: // Search people by skill
                            result = theWorld.searchSkill(searchField.
                                    getText());
                            break;
                        case 4: // Search by parent ID
                            try {
                                result = theWorld.searchParentID(Integer.parseInt(searchField.
                                        getText()));
                            }
                            catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Please input "
                                        + "a number.", "Invalid Input", 
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            break;
                    }
                }
                
                outputTextArea.setText(result);
            }
        });

        //Add components to panels, panels to frame
        filePane.add(fileField, BorderLayout.CENTER);
        filePane.add(fileButton, BorderLayout.EAST);
        
        searchOptionsPane.add(options, BorderLayout.WEST);
        searchOptionsPane.add(searchButton, BorderLayout.EAST);

        searchPane.add(searchField, BorderLayout.CENTER);
        searchPane.add(searchOptionsPane, BorderLayout.EAST);
        
        topPane.add(filePane, BorderLayout.NORTH);
        topPane.add(searchPane, BorderLayout.SOUTH);

        frame.add(topPane, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        add(frame);
        
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SeaPortGUI gui = new SeaPortGUI();
    }

}
