import java.util.*;

/**
 * Class Graph
 * 
 * Data structure for a graph
 * 
 * @author Zach Souser
 * @version 2/13/13
 */
public class GraphController {
    /** 
     * The number of strongly connected components in the graph
     */
    
    public int componentsFound;
    
    /**
     * Is the graph directed?
     */
    
    public boolean DIRECTED;
    
    /**
     * Is the graph a DAG?
     */
    
    public boolean DAG; 
    
    /**
     * Is the DFS finished?
     */
    
    public boolean finished;
    
    /**
     * List of vertices
     */
    
    public ArrayList<NodeController> vertices;
    
    /**
     * List of articulation vertices
     */
    
    public ArrayList<NodeController> articulation;
    
    /**
     * List of edges
     */
    
    public ArrayList<EdgeController> edges;
    
    /** 
     * List of bridges
     */
    public ArrayList<EdgeController> bridges;
    
    /**
     * The DFS sort stack for topological sort
     */
    public Stack<NodeController> sortStack;
    
    /** 
     * The strongly connected component stack
     */
    
    public Stack<NodeController> active;
    
    /**
     * Constructor for class Graph
     * 
     * @param directed flag for whether the graph is directed;
     */
    
    public GraphController(Graph g) {
        this(g.isDirected());
        List<String> names = g.getNodeNamesAsStrings();
        List<List<String>> edgeData = g.getEdgesAsStrings();
        for (String s : names) {
            NodeController v = addVertex(new NodeController(s,this));
        }
        
        for (List<String> edge : edgeData) {
            NodeController v1 = getVertex(edge.get(0));
            NodeController v2 = getVertex(edge.get(1));
            addEdge(new EdgeController(v1,v2));
        }
        
    }
    
    public GraphController(boolean directed) {
        this.componentsFound = 0;
        this.DAG = true;
        this.DIRECTED = directed;
        this.articulation = new ArrayList<NodeController>();
        this.sortStack = new Stack<NodeController>();
        this.active = new Stack<NodeController>();
        this.vertices = new ArrayList<NodeController>();
        this.edges = new ArrayList<EdgeController>();
    }
    
    /**
     * Add a vertex to the graph
     * 
     * @param the NodeController to be added
     * @return the NodeController that was added
     */

    public NodeController addVertex(NodeController v) {
        this.vertices.add(v);
        return v;
    }
    
    
    /** 
     * Retrieve a vertex from the graph based on its name
     */
    
    public NodeController getVertex(String label) {
        for (NodeController v : vertices) {
            if (v.equals(new NodeController(label,this))) return v;
        }
        return null;
    }
    
    /**
     * Delete a vertex from the graph
     * 
     * @param the target vertex to delete
     */
    
    public void deleteVertex(NodeController target) {
       for (int i = 0; i < vertices.size(); i++) {
           if (vertices.get(i).equals(target)) vertices.remove(i);
           else if (vertices.get(i).getEdgeList().contains(target)) vertices.get(i).removeEdge(target);
        }
    }
    
    /**
     * Add an edge to the graph
     * 
     * @param the edge to be added
     * @return the edge that was added
     */
    public EdgeController addEdge(EdgeController e) {
        this.edges.add(e);
        e.start.addEdge(e.end);
        if (!DIRECTED) e.end.addEdge(e.start);
        return e;
    }
    
    /**
     * Remove an edge from the graph
     * 
     * @param the edge to be removed
     */
    
    public void removeEdge(EdgeController e) {
        this.edges.remove(e);
        e.start.removeEdge(e.end);
        if (!DIRECTED) e.end.removeEdge(e.start);
    }
    
    /**
     * Clear the search
     */
    
    public void clear() {
        for (NodeController v : vertices) {
            v.clear();
        }
    }
    
    /**
     * Breadth-first search traverses the vertices, 
     * modifying them accordingly to activate most
     * functionality
     * 
     * @param start the vertex to initiate the search from
     */
    
    public void bfs(NodeController start) {
        LinkedList<NodeController> q = new LinkedList<NodeController>();
        q.add(start);
        start.discover();
        while (!q.isEmpty()) {
            NodeController v = q.removeLast(); 
            v.processEarly();
            v.markProcessed();
            ArrayList<NodeController> edges = v.getEdgeList();
            for (NodeController v2 : edges) {
                if (!v2.isProcessed()) {
                    //if (!DIRECTED);
                    //v2.process(v);
                    v.process(v2);
                }
                if (!v2.isDiscovered()) {
                    q.add(v2);
                    v2.discover();
                }
                v2.setParent(v);
            }
            
            // still not sure, but // 
            v.processLate();
        }
    }
    /**
     * Depth-first search traverses the vertices,
     * modifying them and activating most functionality
     * 
     * @param start the NodeController to initiate the search from
     */
    
    public void dfs(NodeController start) {
        if (finished) return;
        start.processEarly();
        start.discover();
        
        ArrayList<NodeController> edges = start.getEdgeList();
        
        for (NodeController v : edges) {
            if (!v.isDiscovered()) {
                v.setParent(start);
                start.process(v);
                dfs(v);
            }
            else if (!v.isProcessed()) {
                start.process(v);
                //if (!DIRECTED)
                //v.process(start);
                if (finished) return;
            }
        }
        
        start.markProcessed();
        start.processLate();

    }
    
    /**
     * Find a path between two vertices
     * 
     * @param start the start vertex
     * @param end the end vertex
     * 
     * @return a list of vertices that form a path from beginning to end
     */
    public List<NodeController> findPath(NodeController start, NodeController end) {
        List<NodeController> list = new ArrayList<NodeController>();
        bfs(start);
        if (!DAG) return null;
        findPath(start, end, list);
        return list;
    }
    
    
    /**
     * Recursive helper function for finding paths
     * 
     * @param start the start vertex
     * @param end the end vertex
     * @param list the working list for the path
     */
    public boolean findPath(NodeController start, NodeController end, final List<NodeController> list) {
        if (end == null) {
            return false;
        }
        else if (start == end) {
            list.add(start);
            return true;
        }
        else {
            if (findPath(start, end.getParent(), list)) {
                list.add(end);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Topological sort
     * 
     * @return the stack formed by the depth first search
     */
    public Stack<NodeController> topSort() {
        this.sortStack = new Stack<NodeController>();
        for (NodeController v : vertices) {
            if (!v.isDiscovered()) dfs(v);
        }
        if (!DAG) {
            return null;
        }
        return this.sortStack;
    }
    
    /**
     * Get a list of strongly connected component numbers for each vertex
     * 
     * @return the list of SCC integers
     */

    public List<Integer> stronglyConnected() {
        List<Integer> list = new ArrayList<Integer>();
        for (NodeController v : vertices) {
            if (!v.isDiscovered()) dfs(v);
        }
        for (NodeController v : vertices) {
            list.add(v.scc);
        }
        return list;
    }
    
    /**
     * Assess whether the graph is a tree
     * 
     * @return true if it is, false if not
     */

    public boolean isTree() {
        this.sortStack = new Stack<NodeController>();
        for (NodeController v : vertices) {
            if (!v.isDiscovered()) dfs(v);
        }
        if (DAG) return true;
        return false;
    }
    
    /**
     * Assess whether the graph is connected
     * 
     * @return true if it is, false if not
     */
    
    public boolean isConnected() {
        for (NodeController v : vertices) {
            bfs(v);
            for (NodeController v2 : vertices) {
                if (!v2.isDiscovered()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get the articulation vertices stored in the graph
     * after a depth first search
     * 
     * @return the List of articulation vertices
     */
    
    public List<NodeController> getArticulationVertices() {
        //checkFinished = true;
        for (NodeController v : vertices) {
            if (!v.isDiscovered()) dfs(v);
        }
        return this.articulation;
    }
    
    /**
     * Find the bridges in the graph. Removes an edge at a time
     * and checks for connectivity
     * 
     * @return the list of edges that are bridges
     */
    
    public List<EdgeController> findBridges() {
        if (!isConnected()) {
            System.out.println("Not connected");
            return null;
        }
        clear();
        ArrayList<EdgeController> temp = new ArrayList<EdgeController>();  
        for (EdgeController e : this.edges) {
            e.start.removeEdge(e.end);
            //System.out.println(e + ":" +e.start.getEdgeControllerList());
            if (!isConnected()) temp.add(e);
            e.start.addEdge(e.end);
            clear();
        }
        return temp;
    }
    
}
