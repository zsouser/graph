
/**
 * Simple data model for an edge.
 * Allows for bridge detection
 * 
 * @author Zach Souser 
 * @version 2/13/13
 */
public class EdgeController
{
    /**
     * The start vertex
     */
    
    public NodeController start;
    
    /** 
     * The destination vertex
     */
    
    public NodeController end;
    
    /**
     * Constructor for objects of class Edge
     * 
     * @param a the originating vertex
     * @param b the destination vertex
     */
    
    public EdgeController(NodeController a, NodeController b)
    {
        start = a;
        end = b;
    }
    
    /**
     * toString representation of an edge
     */
   
    public String toString() {
        return start + " -> " + end;
    }
}
