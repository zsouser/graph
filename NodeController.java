import java.util.*;
/**
 * Data model for a vertex.
 * Allows for object-oriented storage
 * of vertex boolean flags, timekeeping,
 * ancestor detection, and edge storage.
 * 
 * @author Zach Souser
 * @version 2/13/13
 */

public class NodeController
{
    /** 
     * The graph the NodeController is in
     */
    
    private GraphController g;
    /**
     * The integer identifier of the vertex
     */
    
    public String id;
    
    /**
     * The degree of the vertex
     */
    
    private int degree;
    
    /**
     * The clock for discovering vertices
     */
    
    private static int discoveredClock = 0;
    
    /** 
     * The time at which this instance was discovered
     */
    
    private int discoveredTime;
    
    /**
     * Processed flag
     */
    
    private boolean processed;
    
    /**
     * Discovered flag
     */
    
    private boolean discovered;
    
    /**
     * Pointer to the parent who discovered this vertex
     * in a search
     */
    
    private NodeController parent;
    
    /**
     * The adjacency list for this vertex
     */
    
    private ArrayList<NodeController> edges;
    
    /**
     * The oldest reachable ancestor for this vertex
     */
    
    private NodeController reachableAncestor;
    
    /**
     * The oldest vertex strongly connected to this vertex 
     */
    
    private NodeController low;
    
    /**
     * The strongly connected component number
     */
    public int scc;

    /**
     * Identifiers for edge classifications
     */
    
    private final int TREE = 1;
    private final int BACK = 2;
    private final int FORWARD = 3;
    private final int CROSS = 4;
    
    /**
     * Constructor for objects of class NodeController
     * 
     * @param id the identifier for this vertex
     * @param g the graph this vertex will be in
     */
    public NodeController(String id,GraphController g) {
        this.g = g;
        this.id = id;
        this.scc = -1;
        this.low = null;
        this.edges = new ArrayList<NodeController>();
        this.parent = null;
    }
    
    /**
     * Add an edge to the adjacency list
     * 
     * @param v the vertex to be added
     */

    public void addEdge(NodeController v) {
        edges.add(v);
        this.degree++;
    }
    
    /**
     * Remove an edge from the adjacency list
     * 
     * @param v the vertex to be removed
     */
    
    public void removeEdge(NodeController v) {
        edges.remove(v);
        this.degree--;
    }
    
    
    /**
     * Calculates the edge classification for vertex processing
     * 
     * @param target the vertex at the end of the edge
     * @return int the classification code for the edge
     */
    
    public int edgeClass(NodeController target) {
        if (this.edges.contains(target)) {
            if (this == target.getParent()) return TREE;
            if (target.isDiscovered() && !target.isProcessed()) return BACK;
            if (target.isProcessed() && (target.getDiscoveredTime() > this.getDiscoveredTime())) return FORWARD;
            if (target.isProcessed() && (target.getDiscoveredTime() < this.getDiscoveredTime())) return CROSS;
            
        }
return -1;
    }
    
    /**
     * Process the edge between this vertex and the target
     * 
     * @param target the target of the edge
     */
    
    public void process(NodeController target) {
        int edgeClass = this.edgeClass(target);
        
        if (edgeClass == TREE) {
            this.degree++;
        }

        if (edgeClass == BACK && this != target.getParent()) {
            g.DAG = false;
            g.finished = true;
            if (this.low == null || target.getDiscoveredTime() < this.reachableAncestor.getDiscoveredTime()) {
                this.reachableAncestor = target;
                this.low = target;
            }
        }
        else if (edgeClass == CROSS) {
            if (scc == -1 && (this.low == null || target.getDiscoveredTime() < this.low.getDiscoveredTime()))
                this.low = target;
        }
        
        
    }
    
    /**
     * Clears the flags for this vertex
     */
    
    public void clear() {
        discovered = false;
        processed = false;
        discoveredClock = 0;
        discoveredTime = 0;
    }
    
    /**
     * Marks this vertex as processed
     */
    
    public void markProcessed() {
        this.processed = true;
    }
    /**
     * Check whether this vertex is processed
     * 
     * @return true if it is processed, false if not
     */
    
    public boolean isProcessed() {
        return this.processed;
    }
    
    /**
     * Early processing function
     */
    
    public void processEarly() {
        this.reachableAncestor = this;
        g.active.push(this);
    }
    
    /**
     * Process the articulation vertex functionality
     */
    
    public void processArticulation() {
        boolean root = this.parent == null;
        if (root && this.degree > 1) {
            g.articulation.add(this); // root
        }
            
        else if (!root && this.reachableAncestor == this.parent) 
            g.articulation.add(this); // parent

        else if (this == this.reachableAncestor && this.degree > 0)
            g.articulation.add(this); // bridge
    }
    
    /**
     * Process the ancestor functionality
     */
    
    public void processAncestors() {
        if (this.parent != null && 
                this.reachableAncestor != null && 
                this.parent.reachableAncestor != null && 
                this.reachableAncestor.getDiscoveredTime() < this.parent.reachableAncestor.getDiscoveredTime())
            parent.reachableAncestor = this.reachableAncestor; 

        if (this.low == parent) popComponent();
    
        if (this.low != null && 
                this.parent != null && 
                this.parent.low != null && 
                this.low.getDiscoveredTime() < parent.low.getDiscoveredTime()) 
            this.low = parent.low;
    }
    
    
    /**
     * Late processing function, calls processArticulation and processAncestors,
     * and pushes this to the sort stack
     */
    public void processLate() {
        processArticulation();
        g.sortStack.push(this);    
        processAncestors();
    }
    
    /**
     * Pop the strongly connected component from the stack, and process the
     * Strongly Connected Component number
     */

    public void popComponent() {
        g.componentsFound++;
        while (!g.active.isEmpty()) {
            NodeController v = g.active.pop();
            v.scc = g.componentsFound;
        }
    }
    
    /**
     * Discover this vertex
     */

    public void discover() {
        this.discoveredTime = discoveredClock++;
        this.discovered = true;
    }
        
    /**
     * Determine whether this vertex is discovered
     * 
     * @return true if it is, false if not
     */
    public boolean isDiscovered() {
        return this.discovered;
    }
    
    /**
     * Set the parent to the vertex from within a search
     * @param p the parent vertex
     */
    
    public void setParent(NodeController p) {
        this.parent = p;
    }
    
    /**
     * Get the parent for this vertex
     * 
     * @return the parent vertex
     */

    public NodeController getParent() {
        return this.parent;
    }
    
    /**
     * Return the degree of the vertex
     * @return the degree
     */
    public int degree() {
        return this.degree;
    }
    
    /**
     * Get the adjacency list
     * 
     * @return the ArrayList of vertices
     */
    
    public ArrayList<NodeController> getEdgeList() {
        return this.edges;
    }
    
    /**
     * Access the time at which this vertex was discovered
     * 
     * @return the time
     */
    
    public int getDiscoveredTime() {
        return this.discoveredTime;
    }
    
    /**
     * toString
     * @return the string representation of the vertex
     */

    public String toString() {
        return "NodeController " + this.id;
    }
    
    /**
     * HashCode is the id of the vertex
     */
    public int hashCode() {
        return this.id.hashCode();
    }
    
    /**
     * equals method
     */
    
    public boolean equals(Object o) {
        if (o == null) return false;
        return ((NodeController)o).id.equals(this.id);
    }
    
}
