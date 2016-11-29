
import java.util.*;

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
        HashMap<Integer, Thing> map = new HashMap<>();
        while (input.hasNextLine()) {
            processInput(new Scanner(input.nextLine()), map);
        } // end while
    } // end readFile()

    // Processes a single line of Scanner input, ignoring blank & comment lines 
    public void processInput(Scanner input, HashMap<Integer, Thing> map) {
        if (!input.hasNext()) {
            return;
        }
        switch (input.next()) {
            case "port":
                addPort(input, map);
                break;
            case "dock":
                addDock(input, map);
                break;
            case "ship":
                addShip(input, map);
                break;
            case "cship":
                addCargoShip(input, map);
                break;
            case "pship":
                addPassengerShip(input, map);
                break;
            case "person":
                addPerson(input, map);
                break;
            case "job":
                addJob(input, map);
                break;
        } // end switch statement
    } // end processInput()
    
    /*
    Adds a Port to the World
    */
    public void addPort(Scanner input, HashMap<Integer, Thing> map) {
        SeaPort newPort = new SeaPort(input);
        map.put(newPort.getIndex(), newPort);
        ports.add(newPort);
    } // end addPort
    
    /*
    Adds a Dock to a particular Port in the World
    */
    public void addDock(Scanner input, HashMap<Integer, Thing> map) {
        Dock newDock = new Dock(input);
        map.put(newDock.getIndex(), newDock);
        ((SeaPort) map.get(newDock.getParent())).addDock(newDock);
    } // end addDock
    
    /*
    Adds a Ship to a particular Port and [Dock] in the World
    */
    public void addShip(Scanner input, HashMap<Integer, Thing> map) {
        Ship newShip = new Ship(input);
        map.put(newShip.getIndex(), newShip);
        if (map.get(newShip.getParent()) instanceof SeaPort) {
            ((SeaPort) map.get(newShip.getParent())).addShip(newShip);
        } 
        else {
            ((Dock) map.get(newShip.getParent())).addShip(newShip);
        }
    } // end addShip
    
    /*
    Adds a Cargo Ship to a particular Port and [Dock] in the World
    */
    public void addCargoShip(Scanner input, HashMap<Integer, Thing> map) {
        Ship newShip = new CargoShip(input);
        map.put(newShip.getIndex(), newShip);
        if (map.get(newShip.getParent()) instanceof SeaPort) {
            ((SeaPort) map.get(newShip.getParent())).addShip(newShip);
        } 
        else {
            ((Dock) map.get(newShip.getParent())).addShip(newShip);
        }
    } // end addCargoShip
    
    /*
    Adds a Passenger Ship to a particular Port and [Dock] in the World
    */
    public void addPassengerShip(Scanner input, HashMap<Integer, Thing> map) {
        Ship newShip = new PassengerShip(input);
        map.put(newShip.getIndex(), newShip);
        if (map.get(newShip.getParent()) instanceof SeaPort) {
            ((SeaPort) map.get(newShip.getParent())).addShip(newShip);
        } 
        else {
            ((Dock) map.get(newShip.getParent())).addShip(newShip);
        }
    } // end addPassengerShip
    
    /*
    Adds a Person Ship to a particular Port in the World
    */
    public void addPerson(Scanner input, HashMap<Integer, Thing> map) {
        Person dude = new Person(input);
        map.put(dude.getIndex(), dude);
        ((SeaPort) map.get(dude.getParent())).addPerson(dude);
    } // end addPerson
    
    /*
    Adds a Job to the World (unimplemented)
    */
    public void addJob(Scanner input, HashMap<Integer, Thing> map) {
        Job job = new Job(input);
        Ship ship = (Ship) map.get(job.getParent());
        ship.addJob(job);
    } // end addJob
    
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
