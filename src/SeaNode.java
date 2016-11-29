import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Filename: SeaNode.java
 * Author: Theresa Geckle
 * Date: Nov 29, 2016
 * Purpose: 
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
