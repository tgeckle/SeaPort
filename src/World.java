
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

/**
 * Filename: Thing.java
 * Author: Theresa Geckle
 * Date: Nov 1, 2016
 * Purpose: Implements a class that acts as a container for all of the Ports.
 * Implements methods for constructing a World from an input file. 
 */
class World extends Thing {

    ArrayList<SeaPort> ports = new ArrayList<>();
    Time time = new Time(0);

    /*
    Method for creating random ports to use in random input files.
    */
    public void createRandomPorts(int n, int numDocks, int numPass, 
            int numCargo, int numJobs, int numPersons) {
        for (int i = 0; i < n; i++) {
            ports.add(new SeaPort(numDocks, numPass, numCargo, numJobs, numPersons));
        }
    } // end method createRandomPorts
    
    
    
    /*
   creates constituent ArrayLists when generating random input files 
   */
   public void toArray(List<Thing> ma) {
        for (SeaPort mp : ports) {
            mp.toArray(ma);
        }
    } // end toArray
   
    // Reads a file to create a World of SeaPorts, Ships, Persons, etc.
    public void readFile(Scanner input) {
        while (input.hasNextLine()) {
            processInput(new Scanner(input.nextLine()));
        } // end while
    } // end readFile()

    // Processes a single line of Scanner input, ignoring blank & comment lines 
    public void processInput(Scanner input) {
        if (!input.hasNext()) {
            return;
        }
        switch (input.next()) {
            case "port":
                addPort(input);
                break;
            case "dock":
                addDock(input);
                break;
            case "ship":
                addShip(input);
                break;
            case "cship":
                addCargoShip(input);
                break;
            case "pship":
                addPassengerShip(input);
                break;
            case "person":
                addPerson(input);
                break;
            case "job":
                addJob(input);
                break;
        } // end switch statement
    } // end processInput()
    
    /*
    Adds a Port to the World
    */
    public void addPort(Scanner input) {
        SeaPort newPort = new SeaPort(input);
        ports.add(newPort);
    } // end addPort
    
    /*
    Adds a Dock to a particular Port in the World
    */
    public void addDock(Scanner input) {
        Dock newDock = new Dock(input);
        getPortByIndex(newDock.getParent()).addDock(newDock);
    } // end addDock
    
    /*
    Adds a Ship to a particular Port and [Dock] in the World
    */
    public void addShip(Scanner input) {
        Ship newShip = new Ship(input);
        if (getDockByIndex(newShip.getParent()) instanceof SeaPort) {
            getPortByIndex(newShip.getParent()).addShip(newShip);
        } else {
            getPortByIndex((getDockByIndex(newShip.getParent()).getParent()))
                    .addShip(newShip);
        }
    } // end addShip
    
    /*
    Adds a Cargo Ship to a particular Port and [Dock] in the World
    */
    public void addCargoShip(Scanner input) {
        Ship newShip = new CargoShip(input);
        if (getDockByIndex(newShip.getParent()) instanceof SeaPort) {
            getPortByIndex(newShip.getParent()).addShip(newShip);
        } else {
            getPortByIndex((getDockByIndex(newShip.getParent()).getParent()))
                    .addShip(newShip);
        }
    } // end addCargoShip
    
    /*
    Adds a Passenger Ship to a particular Port and [Dock] in the World
    */
    public void addPassengerShip(Scanner input) {
        Ship newShip = new PassengerShip(input);
        if (getDockByIndex(newShip.getParent()) instanceof SeaPort) {
            getPortByIndex(newShip.getParent()).addShip(newShip);
        } else {
            getPortByIndex((getDockByIndex(newShip.getParent()).getParent()))
                    .addShip(newShip);
        }
    } // end addPassengerShip
    
    /*
    Adds a Person Ship to a particular Port in the World
    */
    public void addPerson(Scanner input) {
        Person dude = new Person(input);
        getPortByIndex(dude.getParent()).addPerson(dude);
    } // end addPerson
    
    /*
    Adds a Job to the World (unimplemented)
    */
    public void addJob(Scanner input) {

    } // end addJob
    
    /*
    Method that returns the Port object associated with a given index
    @param index the index to be searched for
    */
    public SeaPort getPortByIndex(int index) {
        for (SeaPort port : ports) {
            if (port.getIndex() == index) {
                return port;
            }
        }
        return null;
    } // end getPortByIndex method
    
    /*
    Method that returns the Dock OR Port object associated with a given index.
    Structured this way because the parent of a Ship may be a Dock or a Port
    if it is in the queue. 
    @param index the index to be searched for
    */
    public Thing getDockByIndex(int index) {
        for (SeaPort port : ports) {
            if (port.getIndex() == index) {
                return port;
            }
            for (Dock dock : port.docks) {
                if (dock.getIndex() == index) {
                    return dock;
                }
            }
        }
        return null;
    } // end getDockByIndex method
    
    /*
    method to search for all constituent Things of which the name maches the 
    search target.
    @param target the search target
    */
    public String searchNames(String target) {
        String result = "Results with name \"" + target + "\": " + 
                System.getProperty("line.separator");

        for (SeaPort port : ports) {
            if (port.getName().equals(target)) {
                result += port.getIndex() + " (port)" + System.getProperty("line.separator");
            }

            for (Dock dock : port.getDocks()) {
                if (dock.getName().equals(target)) {
                    result += dock.getIndex() + " (dock)"
                            + System.getProperty("line.separator");
                }
            }

            for (Ship ship : port.getShips()) {
                if (ship.getName().equals(target)) {
                    result += ship.getIndex() + " (ship)"
                            + System.getProperty("line.separator");
                }
            }

            for (Person person : port.getPeople()) {
                if (person.getName().equals(target)) {
                    result += person.getIndex() + " (person)"
                            + System.getProperty("line.separator");
                }
            }
        }

        return result;
    } // end searchNames()
    
    /*
    method to search for all constituent Things of which the index maches the 
    search target.
    @param target the search target
    */
    public String searchIndices(int target) {
        String result = "";

        for (SeaPort port : ports) {
            if (port.getIndex() == target) {
                result += target + ": " + port.getName() + " (port)"
                        + System.getProperty("line.separator");
            }

            for (Dock dock : port.getDocks()) {
                if (dock.getIndex() == target) {
                    result += target + ": " + dock.getName() + " (dock)"
                            + System.getProperty("line.separator");
                }
            }

            for (Ship ship : port.getShips()) {
                if (ship.getIndex() == target) {
                    result += target + ": " + ship.getName() + " (ship)"
                            + System.getProperty("line.separator");
                }
            }

            for (Person person : port.getPeople()) {
                if (person.getIndex() == target) {
                    result += target + ": " + getName() + " (person)"
                            + System.getProperty("line.separator");
                }
            }
        }

        return result;
    } // end searchIndices()
    
    /*
    method to search for all constituent Things of which the type maches the 
    search target. Case insensitive.
    @param target the search target
    */
    public String searchType(String target) {
        String result = "All results of type " + target + ": "
                + System.getProperty("line.separator");

        switch (target.toUpperCase()) {
            case ("PORT"): {
                for (SeaPort port : ports) {
                    result += port.getIndex() + ": " + port.getName() + 
                            System.getProperty("line.separator");
                }
                break;
            }
            case ("SEAPORT"): {
                for (SeaPort port : ports) {
                    result += port.getIndex() + ": " + port.getName() + 
                            System.getProperty("line.separator");
                }
                break;
            }
            case ("DOCK"): {
                for (SeaPort port : ports) {
                    for (Dock dock : port.getDocks()) {
                        result += dock.getIndex() + ": " + dock.getName()
                                + System.getProperty("line.separator");
                    }
                }
                break;
            }
            case ("SHIP"): {
                for (SeaPort port : ports) {
                    for (Ship ship : port.getShips()) {
                        result += ship.getIndex() + ": " + ship.getName()
                                + System.getProperty("line.separator");
                    }
                }
                break;
            }
            case ("PASSENGER SHIP"): {
                for (SeaPort port : ports) {
                    for (Ship ship : port.getShips()) {
                        if (ship.getClass().getName().equals("PassengerShip")) {
                            result += ship.getIndex() + ": " + ship.getName()
                                    + System.getProperty("line.separator");
                        }
                    }
                }
                break;
            }
            case ("CARGO SHIP"): {
                for (SeaPort port : ports) {
                    for (Ship ship : port.getShips()) {
                        if (ship.getClass().getName().equals("CargoShip")) {
                            result += ship.getIndex() + ": " + ship.getName()
                                    + System.getProperty("line.separator");
                        }
                    }
                }
                break;
            }

        }

        return result;
    } // end searchType()
    
    /*
    method to search for all constituent Persons of which the name maches the 
    search target.
    @param target the search target
    */
    public String searchSkill(String target) {
        String result = "All people with skill \"" + target + "\":"
                 + System.getProperty("line.separator");

        for (SeaPort port : ports) {
            for (Person person : port.getPeople()) {
                if (person.getSkill().equals(target)) {
                    result += person.getIndex() + ": " + person.getName() + " " 
                            + target + System.getProperty("line.separator");
                }
            }
        }

        return result;
    } // end searchSkills()
    
    /*
    method to search for all constituent Things of which the parent index maches 
    the search target.
    @param target the search target
    */
    public String searchParentID(int target) {
        String result = "Children of " + target + ": "
                + System.getProperty("line.separator");

        for (SeaPort port : ports) {
            if (port.getParent() == target) {
                result += port.getIndex() + ": " + port.getName() + " (port)"
                        + System.getProperty("line.separator");
            }

            for (Dock dock : port.getDocks()) {
                if (dock.getParent() == target) {
                    result += dock.getIndex() + ": " + dock.getName() + " (dock)"
                            + System.getProperty("line.separator");
                }
            }

            for (Ship ship : port.getShips()) {
                if (ship.getParent() == target) {
                    result += ship.getIndex() + ": " + ship.getName() + " (ship)"
                            + System.getProperty("line.separator");
                }
            }

            for (Person person : port.getPeople()) {
                if (person.getParent() == target) {
                    result += person.getIndex() + ": " + person.getName() + " (person)"
                            + System.getProperty("line.separator");
                }
            }
        }

        return result;
    } // end searchByParentID()
    
    /*
    World toString
    */
    public String toString() {
        String result = "THE WORLD: " + System.getProperty("line.separator");
        for (SeaPort port : ports) {
            result += "  Seaport: " + port.toString()
                    + System.getProperty("line.separator")
                    + System.getProperty("line.separator");
        }
        return result;
    }

} // end class World
