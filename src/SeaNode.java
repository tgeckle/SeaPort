import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Filename: SeaNode.java
 * Author: Theresa Geckle
 * Date: Nov 29, 2016
 * Purpose: Implements a Node class for use with the JTree class that extends 
 * the DefaultMutableTreeNode class. The only notable change is to the toString()
 * method, which is overridden so that the name field for the given Thing is
 * returned, rather than its own toString() output.
 */
public class SeaNode extends DefaultMutableTreeNode{
    
    public SeaNode(Object userObject) {
        super(userObject);
    }
    
    @Override
    public String toString() {
        if (userObject == null) {
            return "";
        } 
        else if (getUserObject() instanceof Thing) {
            Thing item = (Thing) getUserObject();
            return item.getName();
        }
        else {
            return getUserObject().toString();
        }
    }
}
