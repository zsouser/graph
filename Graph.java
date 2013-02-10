import java.util.*;
// do better
public class Graph {
    public int componentsFound;
    public boolean DIRECTED;
    public boolean DAG; 
    public boolean finished;
    public boolean checkFinished;
    
    public HashSet<Vertex> vertices, rootArticulation, parentArticulation, bridgeArticulation;
    public Stack<Vertex> sortStack, active;
    public static void main(String[] args) {
        Graph g = new Graph(false);
        Vertex v1 = g.addVertex(new Vertex(1,g));
        Vertex v2 = g.addVertex(new Vertex(2,g));
        Vertex v3 = g.addVertex(new Vertex(3,g));
        Vertex v4 = g.addVertex(new Vertex(4,g));
        Vertex v5 = g.addVertex(new Vertex(5,g));
        Vertex v6 = g.addVertex(new Vertex(6,g));
        Vertex v7 = g.addVertex(new Vertex(7,g));
        g.addEdge(v1,v2);
        g.addEdge(v2,v3);
        g.addEdge(v3,v1);
        g.addEdge(v4,v5);
        g.addEdge(v5,v6);
        g.addEdge(v6,v7);
        g.addEdge(v1,v7);
        //g.findArticulationVertices();
       g.findBridges();
    }

    public Graph(boolean directed) {
        this.componentsFound = 0;
        this.DAG = true;
        this.DIRECTED = directed;
        this.rootArticulation = new HashSet<Vertex>();
        this.bridgeArticulation = new HashSet<Vertex>();
        this.parentArticulation = new HashSet<Vertex>();
        this.sortStack = new Stack<Vertex>();
        this.active = new Stack<Vertex>();
        this.vertices = new HashSet<Vertex>();
    }

    public Vertex addVertex(Vertex v) {
        this.vertices.add(v);
        return v;
    }
    
    public void addEdge(Vertex start, Vertex finish) {
        start.addEdge(finish);
        if (!DIRECTED) finish.addEdge(start);
    } 
    
    public void bfs(Vertex start) {
        LinkedList<Vertex> q = new LinkedList<Vertex>();
        q.add(start);
        start.discover();
        while (!q.isEmpty()) {
            Vertex v = q.removeLast(); 
            v.processEarly();
            v.markProcessed();
            HashSet<Vertex> edges = v.getEdgeList();
            for (Vertex v2 : edges) {
                if (!v2.isProcessed()) {
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

    public void dfs(Vertex start) {
        if (checkFinished && finished) return;
        start.processEarly();
        start.discover();
        
        HashSet<Vertex> edges = start.getEdgeList();
        
        for (Vertex v : edges) {
            if (!v.isDiscovered()) {
                v.setParent(start);
                start.process(v);
                dfs(v);
            }
            else if (!v.isProcessed()) {
                start.process(v);
                if (checkFinished && finished) return;
            }
        }
        
        start.markProcessed();
        start.processLate();

    }
    
    public void findPath(Vertex start, Vertex end) {
        bfs(start);
        if ((start == end) || (end == null) || (start == null)) System.out.println(start);
        else {
            findPath(start, end.getParent());
            System.out.println(end);
        }
    }

    public void topSort() {
        this.sortStack = new Stack<Vertex>();
        for (Vertex v : vertices) {
            if (!v.isDiscovered()) dfs(v);
        }
        if (!DAG) {
            System.out.println("Not a DAG");
            return;
        }
        System.out.println();
        System.out.println("Top Sort:");
        System.out.println();
        while (!this.sortStack.isEmpty()) System.out.print(this.sortStack.pop() + " -> ");
        System.out.println();
    }

    public void stronglyConnected() {
        for (Vertex v : vertices) {
            if (!v.isDiscovered()) dfs(v);
            System.out.println(v + " is connected to " + v.scc + " others");
        }
    }

    public boolean isTree() {
        this.sortStack = new Stack<Vertex>();
        for (Vertex v : vertices) {
            if (!v.isDiscovered()) dfs(v);
        }
        if (DAG) return true;
        return false;
    }
    
    public boolean isConnected() {
        for (Vertex v : vertices) {
            bfs(v);
            for (Vertex v2 : vertices) {
                if (!v2.isDiscovered()) return false;
            }
        }
        return true;
    }

    public void findArticulationVertices() {
        //checkFinished = true;
        for (Vertex v : vertices) {
            if (!v.isDiscovered()) dfs(v);
        }
        System.out.print("\nRoot articulation: ");
        for (Vertex v : this.rootArticulation) {
            System.out.print(v + " ");
        }
        System.out.println("\nParent articulation:");
        for (Vertex v : this.parentArticulation) { 
            System.out.print(v + " ");
        }
        System.out.println("\nBridge articulation:");
        for (Vertex v : this.bridgeArticulation) {
            System.out.print(v + " " );
        }
        System.out.println();
    }
    
    public void findBridges() {
        if (!isConnected()) {
            System.out.println("The graph was already not connected");
            return;
        }
        
        for (Vertex v : vertices) {
            
            Vertex[] edges = v.getEdgeList().toArray(new Vertex[v.getEdgeList().size()]);
            for (Vertex edge : edges) {
                v.removeEdge(edge);
                if (!DIRECTED) edge.removeEdge(v);
                if (!isConnected()) 
                    System.out.println("The edge between " + v + " and " + edge + " is a bridge");
                else 
                    System.out.println("The edge between " + v + " and " + edge + " is not a bridge");
                    
                v.addEdge(edge);
                if (!DIRECTED) edge.addEdge(v);
            }
        }
    }
}

// topological sort
// bfs
// dfs
// isTree
// 