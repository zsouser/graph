import java.util.*;
/**
 * Write a description of class Vertex here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Vertex
{
    private Graph g;
    public int id;
    private int degree;
    private static int processedClock = 0, discoveredClock = 0;
    private int processedTime, discoveredTime;
    private boolean processed = false, discovered = false;
    private Vertex parent;
    private HashSet<Vertex> edges;
    private Vertex reachableAncestor;
    private Vertex low;
    public int scc;

    private final int TREE = 1;
    private final int BACK = 2;
    private final int FORWARD = 3;
    private final int CROSS = 4;
    
    /**
     * Constructor for objects of class Vertex
     */
    public Vertex(int id,Graph g) {
        this.g = g;
        this.id = id;
        this.scc = -1;
        this.low = null;
        this.edges = new HashSet<Vertex>();
        this.parent = null;
    }

    public void addEdge(Vertex v) {
        edges.add(v);
        this.degree++;
    }
    
    public void removeEdge(Vertex v) {
        edges.remove(v);
        this.degree--;
    }

    public int edgeClass(Vertex target) {
        if (this.edges.contains(target)) {
            if (this == target.getParent()) return TREE;
            if (target.isDiscovered() && !target.isProcessed()) return BACK;
            if (target.isProcessed() && (target.getDiscoveredTime() > this.getDiscoveredTime())) return FORWARD;
            if (target.isProcessed() && (target.getDiscoveredTime() < this.getDiscoveredTime())) return CROSS;
            
        }
return -1;
    }
    
    public void process(Vertex target) {
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

    public void markProcessed() {
        this.processed = true;
        this.processedTime = processedClock++;
    }

    public boolean isProcessed() {
        return this.processed;
    }
    
    public void processEarly() {
        this.reachableAncestor = this;
        g.active.push(this);
    }

    public void processLate() {
        boolean root = this.parent == null;
        if (root && this.degree > 1) {
            g.rootArticulation.add(this);
        }
            
        if (!root && this.reachableAncestor == this.parent) 
            g.parentArticulation.add(this);
        

        if (this == this.reachableAncestor && this.degree > 0) 
            g.bridgeArticulation.add(this);
            
        
        if (this.parent != null && 
                this.reachableAncestor != null && 
                this.parent.reachableAncestor != null && 
                this.reachableAncestor.getDiscoveredTime() < this.parent.reachableAncestor.getDiscoveredTime())
            parent.reachableAncestor = this.reachableAncestor; 

        g.sortStack.push(this);
        if (this.low == parent) popComponent();
        if (this.low != null && 
                this.parent != null && 
                this.parent.low != null && 
                this.low.getDiscoveredTime() < parent.low.getDiscoveredTime()) 
            this.low = parent.low;
    }

    public void popComponent() {
        g.componentsFound++;
        while (!g.active.isEmpty()) {
            Vertex v = g.active.pop();
            v.scc = g.componentsFound;
        }
    }

    public void discover() {
        this.discoveredTime = discoveredClock++;
        this.discovered = true;
    }
        
    
    public boolean isDiscovered() {
        return this.discovered;
    }
    
    public void setParent(Vertex p) {
        this.parent = p;
    }

    public Vertex getParent() {
        return this.parent;
    }
    
    public int degree() {
        return this.edges.size();
    }
    
    public HashSet<Vertex> getEdgeList() {
        return this.edges;
    }
    
    public int getProcessedTime() {
        return this.processedTime;
    }

    public int getDiscoveredTime() {
        return this.discoveredTime;
    }

    public String toString() {
        return "Vertex " + this.id;
    }
    
    public int hashCode() {
        return this.id;
    }
    
    public boolean equals(Object o) {
        return ((Vertex)o).id == this.id;
    }
}
