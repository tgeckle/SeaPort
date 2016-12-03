import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;

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
    private final String[] TYPES = {"LIST ALL...", "Things", "People", "Ships", 
        "Ships in Queue", "Docks"}; // list of types to sort
    private String[] portCombo = {"SELECT PORT..."};
    private String[] sortOptions = {"SORT BY...", "Name", "Index"};
    DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(sortOptions);
    private String[] sortOptionsPerson = {"SORT BY...", "Name", "Index", "Skill"};
    DefaultComboBoxModel personModel = new DefaultComboBoxModel(sortOptionsPerson);
    private String[] sortOptionsShip = {"SORT BY...", "Name", "Index", "Weight",
        "Length", "Width", "Draft"};
    DefaultComboBoxModel shipModel = new DefaultComboBoxModel(sortOptionsShip);
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
    World theWorld; // All ports etc. are held within the World
    
    WorldRunner runner;

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
        JPanel sortPane = new JPanel(new BorderLayout(20, 0));
        sortPane.setBorder(new EmptyBorder(0, 20, 10, 20));
        
        JPanel topPane = new JPanel(new GridLayout(3,1,0,0));
        JPanel searchOptionsPane = new JPanel(new BorderLayout(10, 0));
        JPanel sortByPane = new JPanel(new BorderLayout(10, 0));
        
        JPanel bottomPane = new JPanel(new BorderLayout(10, 10));
        bottomPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JPanel textPane = new JPanel(new BorderLayout(10, 10));
        
        JPanel jobPane = new JPanel(new BorderLayout());
        JPanel jobToolbarPane = new JPanel(new GridLayout(1, 4, 30, 0));
        jobToolbarPane.setBorder(new EmptyBorder(4, 20, 0, 20));

        // Initialize Components
        JFileChooser chooser = new JFileChooser();
        JTextField fileField = new JTextField(40);
        fileField.setEditable(false);
        JTextField searchField = new JTextField(22);
        JComboBox options = new JComboBox(SEARCH_OPTIONS);
        
        JComboBox<String> portSelect = new JComboBox<>(portCombo);
        JComboBox<String> typeSelect = new JComboBox<>(TYPES);
        JComboBox<String> sortBySelect = new JComboBox<>(sortOptions);
        portSelect.setEnabled(false);
        typeSelect.setEnabled(false);
        sortBySelect.setEnabled(false);

        JButton fileButton = new JButton("Open File");
        JButton searchButton = new JButton("Search");
        JButton sortButton = new JButton("Sort");

        JTextArea outputTextArea = new JTextArea(15,60);
        JScrollPane displayPane = new JScrollPane(outputTextArea);
        outputTextArea.setEditable(false);

        JTextArea jobTextArea = new JTextArea(12,60);
        JScrollPane jobDisplayPane = new JScrollPane(jobTextArea);
        outputTextArea.setEditable(false);
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        JButton jobSuspendButton = new JButton("Suspend Job");
        JButton jobResumeButton = new JButton("Resume Job");
        JButton jobCancelButton = new JButton("Cancel Job");
        
        JTree tree = new JTree(buildInitialTree());
        JScrollPane treePane = new JScrollPane(tree);

        // Listeneres
        // Listener for file input button
        fileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                theWorld = new World();
                chooser.setDialogTitle("Choose Input File");
                chooser.setFileFilter(filter);
                
                Stack<String> portNames = new Stack<>();
                portNames.add("SELECT PORT...");

                int returnVal = chooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        fileField.setText(chooser.getSelectedFile().getAbsolutePath());
                        Scanner input = new Scanner(chooser.getSelectedFile());
                        theWorld.readFile(input);
                        
                        for (SeaPort port : theWorld.ports) {
                            portNames.add(port.getName());
                        }
                        portSelect.setModel(new DefaultComboBoxModel(portNames));
                        
                        portSelect.setEnabled(true);
                        typeSelect.setEnabled(true);

                        outputTextArea.setText(theWorld.toString());
                        tree.setModel(buildTree());
                        
                        runner = new WorldRunner(theWorld, 
                                jobTextArea, progressBar);
                        runner.execute();
                        
                        
                    } catch (FileNotFoundException exc) {
                        JOptionPane.showMessageDialog(null, "File not found."
                                + "Please try a different file. ", "File Not Found",
                                JOptionPane.ERROR_MESSAGE);
//                    } catch (InterruptedException exc) {
//                        System.out.println("Interrupted");
                    }

                }

            }
        });
        
        jobCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runner.cancel();
            }
        });
        
        jobSuspendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runner.pause();
            }
        });
        
        jobResumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runner.unPause();
            }
        });
        
        typeSelect.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                sortBySelect.setModel(defaultModel); //Resets sortBy combo box
                switch (typeSelect.getSelectedIndex()) {
                    case 0: {
                        sortBySelect.setEnabled(false);
                        break;
                    }
                    case 2: {
                        sortBySelect.setModel(personModel);
                        sortBySelect.setEnabled(true);
                        break;
                    }
                    case 3: {
                        sortBySelect.setModel(shipModel);
                        sortBySelect.setEnabled(true);
                        break;
                    }
                    case 4: {
                        sortBySelect.setModel(shipModel);
                        sortBySelect.setEnabled(true);
                        break;
                    }
                    default: {
                        sortBySelect.setEnabled(true);
                        break;
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
        
        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sortResult = "";
                ArrayList<? extends Thing> sortList = new ArrayList<>();
                ArrayList<Thing> thingList = new ArrayList<>();
                ArrayList<Ship> shipList = new ArrayList<>();
                ArrayList<Person> personList = new ArrayList<>();
                
                // Changes sorting criterion based on selection from JComboBox
                switch (sortBySelect.getSelectedItem().toString()) { 
                    case "SORT BY..." : {
                        JOptionPane.showMessageDialog(null, "Please select "
                                + "sorting option.", "Selection Not Made",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    case "Name" : {
                        Thing.sortCriterion = SortBy.NAME;
                        break;
                    }
                    case "Index" : {
                        Thing.sortCriterion = SortBy.INDEX;
                        break;
                    }
                    case "Skill" : {
                        Thing.sortCriterion = SortBy.SKILL;
                        break;
                    }
                    case "Weight" : {
                        Thing.sortCriterion = SortBy.WEIGHT;
                        break;
                    }
                    case "Length" : {
                        Thing.sortCriterion = SortBy.LENGTH;
                        break;
                    }
                    case "Width" : {
                        Thing.sortCriterion = SortBy.WIDTH;
                        break;
                    }
                    case "Draft" : {
                        Thing.sortCriterion = SortBy.DRAFT;
                        break;
                    }
                    
                }
                
                
                if (portSelect.getSelectedIndex() > 0) {
                    SeaPort curPort = theWorld.ports.get(portSelect.getSelectedIndex() - 1);
                    
                    switch (typeSelect.getSelectedIndex()) {
                        case 0 : {
                            JOptionPane.showMessageDialog(null, "Please select "
                                    + "type of Thing to be sorted.",
                                    "Selection Not Made",
                                    JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        case 1 : { //Things
                            thingList.addAll(curPort.docks);
                            thingList.addAll(curPort.ships);
                            thingList.addAll(curPort.persons);
                            Collections.sort(thingList);
                            sortList = thingList;
                            break;
                        }
                        case 2 : { //People
                            personList.addAll(curPort.persons);
                            Collections.sort(personList);
                            sortList = personList;
                            break;
                        }
                        case 3 : { //Ships
                            shipList.addAll(curPort.ships);
                            Collections.sort(shipList);
                            sortList = shipList;
                            break;
                        }
                        case 4 : { //Ships in Queue
                            shipList.addAll(curPort.queue);
                            Collections.sort(shipList);
                            sortList = shipList;
                            break;
                        }
                        case 5 : { //Docks
                            thingList.addAll(curPort.docks);
                            Collections.sort(thingList);
                            sortList = thingList;
                            break;
                        }
                    } // end switch
                    
                    for (Thing item : sortList) {
                        sortResult += item.toString() + 
                                System.getProperty("line.separator");
                    } // end for
                    
                    outputTextArea.setText(sortResult);
                }
                else {
                    JOptionPane.showMessageDialog(null, "<html><body><p style="
                            + "'width: 200px;'> No port selected. "
                            + "Please select a port from the drop-down and try "
                            + "again. If there are no ports to select it is "
                            + "because no input file has been selected."
                            + "</p></body></html>", 
                            "Selection Not Made", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Add components to panels, panels to frame
        filePane.add(fileField, BorderLayout.CENTER);
        filePane.add(fileButton, BorderLayout.EAST);
        
        searchOptionsPane.add(options, BorderLayout.WEST);
        searchOptionsPane.add(searchButton, BorderLayout.EAST);
                
        sortByPane.add(sortBySelect, BorderLayout.WEST);
        sortByPane.add(sortButton, BorderLayout.EAST);
        
        sortPane.add(portSelect, BorderLayout.WEST);
        sortPane.add(typeSelect, BorderLayout.CENTER);
        sortPane.add(sortByPane, BorderLayout.EAST);

        searchPane.add(searchField, BorderLayout.CENTER);
        searchPane.add(searchOptionsPane, BorderLayout.EAST);
        
        jobToolbarPane.add(progressBar);
        jobToolbarPane.add(jobSuspendButton);
        jobToolbarPane.add(jobResumeButton);
        jobToolbarPane.add(jobCancelButton);
        
        jobPane.add(jobDisplayPane, BorderLayout.CENTER);
        jobPane.add(jobToolbarPane, BorderLayout.SOUTH);
        
        textPane.add(displayPane, BorderLayout.CENTER);
        textPane.add(jobPane, BorderLayout.SOUTH);
        
        topPane.add(filePane);
        topPane.add(searchPane);
        topPane.add(sortPane);
        
        bottomPane.add(treePane, BorderLayout.WEST);
        bottomPane.add(textPane, BorderLayout.CENTER);

        frame.add(topPane, BorderLayout.NORTH);
        frame.add(bottomPane, BorderLayout.CENTER);

        add(frame);
        
        pack();
        setVisible(true);
    }
    
    private DefaultTreeModel buildTree() {
        SeaNode rootNode = new SeaNode("The World                          ");
        
        for (SeaPort port : theWorld.ports) {
            SeaNode portNode = new SeaNode(port);
            
            SeaNode docksNode = new SeaNode("Docks");
            SeaNode queueNode = new SeaNode("Queue");
            SeaNode shipsNode = new SeaNode("Ships");
            SeaNode peopleNode = new SeaNode("People");
            
            for (Dock dock : port.docks) {
                SeaNode dockNode = new SeaNode(dock);
                dockNode.add(new SeaNode(dock.ship));
                docksNode.add(dockNode);
            }
            
            for (Ship ship : port.queue) {
                SeaNode shipNode = new SeaNode(ship);
                SeaNode jobsNode = new SeaNode("Jobs");
                
                for (Job job : ship.jobs) {
                    SeaNode jobNode = new SeaNode(job);
                    jobsNode.add(jobNode);
                }
                
                shipNode.add(jobsNode);
                queueNode.add(shipNode);
            }
            
            for (Ship ship : port.ships) {
                SeaNode shipNode = new SeaNode(ship);
                SeaNode jobsNode = new SeaNode("Jobs");
                
                for (Job job : ship.jobs) {
                    SeaNode jobNode = new SeaNode(job);
                    jobsNode.add(jobNode);
                }
                
                shipNode.add(jobsNode);
                shipsNode.add(shipNode);
            }
            
            for (Person dude : port.persons) {
                SeaNode personNode = new SeaNode(dude);
                peopleNode.add(personNode);
            }
            
            portNode.add(docksNode);
            portNode.add(queueNode);
            portNode.add(shipsNode);
            portNode.add(peopleNode);
            
            rootNode.add(portNode);
        }
        
        return new DefaultTreeModel(rootNode);
    }

    private DefaultTreeModel buildInitialTree() {
        SeaNode rootNode = new SeaNode("The World                          ");
        rootNode.add(new SeaNode("Ports"));
        
        return new DefaultTreeModel(rootNode);
    }
    
    public static void main(String[] args) {
        SeaPortGUI gui = new SeaPortGUI();
        
    }

}
