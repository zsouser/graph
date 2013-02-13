import java.util.ArrayList;
import java.util.List;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.OutputStream;
import java.io.Serializable;
/**
 * Represents a graph comprised of collection of nodes,
 * a collection of edges, and an indicator as to
 * whether or not the graph is directed.
 * @author Dr. Jody Paul
 * @version 20130213a
 */
public class Graph implements Serializable {
    /**
     * Serialization version indicator used to determine if
     * a file is compatible with this class.
     */
    private static final long serialVersionUID = 1038001L;
    /** Default state save/restore file name. */
    public static String SERIAL_FILENAME = "graph.ser";
    /** Ensure serialization of necessary fields. */
    private static final ObjectStreamField[] serialPersistentFields
                 = { new ObjectStreamField("isDirected", Boolean.class),
                     new ObjectStreamField("nodes", List.class),
                     new ObjectStreamField("edges", List.class) };

    /**
     * The list of nodes in this graph.
     * Each node is represented by a Node object.
     * @serial
     */
    private List<Node> nodes = new ArrayList<Node>();

    /**
     * The list of edges in this graph.
     * Each edge is represented by an Edge object.
     * @serial
     */
    private List<Edge> edges = new ArrayList<Edge>();

    /**
     * Indicator of directedness of this graph;
     * true = directed, false = undirected
     * @serial
     */
    private Boolean isDirected = new Boolean(false);

    /**
     * Construct an undirected graph with no nodes or edges.
     */
    public Graph() { }

    /**
     * Construct an undirected graph with nodes only, no edges.
     * Performs no error checking.
     * @param nodeLabels a list of String objects to be used as node labels
     */
    public Graph(List<String> nodeLabels) {
        for (String label : nodeLabels) {
            this.nodes.add(new Node(label, null));
        }
    }

    /**
     * Construct a simple undirected, unweighted graph.
     * Performs no error checking.
     * @param nodeLabels a list of String objects to be used as node labels
     * @param edgeList a list of edges where each edge is specified as a
     *        list of two strings representing the start-node-label and
     *        end-node-label in that order.
     */
    public Graph(List<String> nodeLabels, List<List<String>> edgeList, Boolean simple) {
        this(nodeLabels);
        for (List<String> edge : edgeList) {
            this.edges.add(new Edge(edge.get(0), edge.get(1), false, null, null));
        }
    }

    /**
     * Construct a general graph.
     * Performs no error checking.
     * @param nodeLabels a list of String objects to be used as node labels
     * @param nodeCosts a list of Integer objects to be used as cost of each node in nodeLabels
     * @param directed true if this is a directed graph; false otherwise
     * @param edgeList a list of edges where each edge is specified as a
     *        list of Strings representing
     *        <start-node-label,end-node-label,directedness,edge-label,edge-weight>;
     *        edge-label and edge-weight may be null.
     */
    public Graph(List<String> nodeLabels,
                 List<Integer> nodeCosts,
                 Boolean directed,
                 List<List<String>> edgeList) {
        for (int i = 0; i < nodeLabels.size(); i++) {
            this.nodes.add(new Node(nodeLabels.get(i), nodeCosts.get(i)));
        }
        this.isDirected = directed;
        for (List<String> edge : edgeList) {
            this.edges.add(new Edge(edge.get(0),
                                    edge.get(1),
                                    Boolean.valueOf(edge.get(2)),
                                    edge.get(3),
                                    Integer.valueOf(edge.get(4))));
        }
    }

    /**
     * Retrieve nodes as list of labels.
     * @return list of labels of nodes in this graph
     */
    public List<String> getNodeNamesAsStrings() {
        List<String> labels = new ArrayList<String>();
        for (Node n : this.nodes) {
            labels.add(n.getLabel());
        }
        return labels;
    }


    /**
     * Retrieve nodes as list of pairs of Strings.
     * The first element is the node label, the second is the associated cost.
     * @return list of labels of nodes in this graph
     */
    public List<List<String>> getNodesAsStrings() {
        List<List<String>> nodeStrings = new ArrayList<List<String>>();
        for (Node n : this.nodes) {
            List<String> nodePair = new ArrayList<String>();
            nodePair.add(n.getLabel());
            nodePair.add(n.getCost().toString());
            nodeStrings.add(nodePair);
        }
        return nodeStrings;
    }

    /**
     * Retrieve edges as list of lists of five Strings:
     * two node labels, an indicator of directedness,
     * a label, and a weight.
     * If a directed edge, the first of the pair of nodes represents
     * the start node while the second represents the end node.
     * If a directed edge, the indicator is "true"; 
     * otherwise the indicator is "false".
     * If the edge has no label, it will be shown as "null".
     * If the edge has a weight, it will be given as an integer string;
     * otherwise it will be shown as "null".
     * @return list of edges in this graph
     */
    public List<List<String>> getEdgesAsStrings () {
        List<List<String>> edgeList = new ArrayList<List<String>>();
        for (Edge e : this.edges) {
            List<String> oneEdge = new ArrayList<String>();
            oneEdge.add(e.getStartNode());
            oneEdge.add(e.getEndNode());
            oneEdge.add(e.isDirected().toString());
            oneEdge.add("" + e.getLabel());
            oneEdge.add("" + e.getWeight());
            edgeList.add(oneEdge);
        }
        return edgeList;           
    }

    /**
     * Disclose if graph is directed or undirected.
     * @return true if graph is directed; false otherwise
     */
    public Boolean isDirected() { return this.isDirected; }

    /**
     * Simple string representation of graph.
     * @return a string that shows the nodes and edges of this graph
     */
    @Override
    public String toString() {
        return "GRAPH: " + 
        (isDirected ? "Directed" : "Undirected") +
        "\n " +
        "Nodes(" + nodes + ")\n " +
        "Edges(" + edges + ")";
    }

    @Override public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( !(o instanceof Graph) ) return false;
        Graph that = (Graph)o;
        return
            (this.nodes.equals(that.nodes)) &&
            (this.edges.equals(that.edges)) &&
            (this.isDirected.equals(that.isDirected));
    }

    @Override public int hashCode() {
        return
        17 + 
        ((this.nodes == null) ? 0 : this.nodes.hashCode()) +
        ((this.edges  == null) ? 0 : this.edges.hashCode()) +
        ((this.isDirected == null) ? 0 : this.isDirected.hashCode());
    }

    /**
     * Save the state of this graph.
     * @param graphFileName the name of the file into which to save,
     *        or null to use default file name. 
     */
    public void saveGraph(String graphFileName) {
        if (graphFileName == null) graphFileName = Graph.SERIAL_FILENAME;
        // Serialize the graph.
        try {
            OutputStream file = new FileOutputStream(graphFileName);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try { output.writeObject(this); }
            finally { output.close(); }
        }
        catch (IOException ex) {
            System.err.println("Unsuccessful save. " + ex);
        }

        // Attempt to deserialize the graph as verification.
        try {
            InputStream file = new FileInputStream(graphFileName);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            try {

                @SuppressWarnings("unchecked") // Accommodate type erasure.
                Graph restored = null;
                restored = (Graph)input.readObject();
                // Simple check that deserialized data matches original.
                if (!this.toString().equals(restored.toString()))
                    System.err.println("[1] State restore did not match save!");
                if (!this.equals(restored))
                    System.err.println("[2] State restore did not match save!");
            }
            finally { input.close(); }
        }
        catch (ClassNotFoundException ex) {
            System.err.println(
                "Unsuccessful deserialization: Class not found. " + ex);
        }
        catch (IOException ex) {
            System.err.println("Unsuccessful deserialization: " + ex);
        }
    }

    /**
     * Reload this graph.
     * @param graphFileName the name of the file with serialized graph,
     *        or null to use default file name. 
     */
    public void restoreGraph(String graphFileName) {
        if (graphFileName == null) graphFileName = Graph.SERIAL_FILENAME;
        Graph restored = null;
        try {
            InputStream file = new FileInputStream(graphFileName);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            try {
                @SuppressWarnings("unchecked") // Accommodate type erasure.
                Graph retrieved = (Graph)input.readObject();
                restored = retrieved;
            }
            finally {
                input.close();
            }
        }
        catch (ClassNotFoundException ex) {
            System.err.println(
                "Unsuccessful deserialization: Class not found. " + ex);
        }
        catch (IOException ex) {
            System.err.println("Unsuccessful deserialization: " + ex);
        }
        if (restored == null) {
            System.err.println("Unsuccessful deserialization: restored == null");
        } else {
            this.nodes = restored.nodes;
            this.edges = restored.edges;
            this.isDirected = restored.isDirected;
        }
    }

    /**
     * Node representation is comprised of a
     * String label and an Integer cost.
     * If cost is null, this node is not part of
     * a graph whose nodes have a cost attribute.
     */
    private class Node implements Serializable  {
        private String label;
        private Integer cost;
        /**
         * Construct a node with attributes as
         * specified by the parameters.
         * @param nodeLabel the label associated with this node
         * @param nodeCost the cost or weight associated with this node
         */
        Node(String nodeLabel, Integer nodeCost) {
            this.label = nodeLabel;
            this.cost = nodeCost;
        }

        String getLabel() { return this.label; }

        Integer getCost() { return this.cost; }

        @Override public String toString() { return this.label; }

        /**
         * Equals predicate helper method.
         * @param x first object for comparison
         * @param y second object for comparison
         * @return true if both objects are null or
         *         objects' native equals method returns true;
         *         false otherwise
         */
        private boolean closeEnough(Object x, Object y) {
            if (x == y) return true;
            if (x == null || y == null) return false;
            return x.equals(y);
        }

        @Override public boolean equals(Object o) {
            if ( this == o ) return true;
            if ( !(o instanceof Node) ) return false;
            Node that = (Node)o;
            return
            closeEnough(this.label, that.label) &&
            closeEnough(this.cost, that.cost);
        }

        @Override public int hashCode() {
            return
            29 + 
            ((this.label == null) ? 0 : this.label.hashCode()) +
            ((this.cost  == null) ? 0 : this.cost.hashCode());
        }
    }

    /**
     * Edge representation is comprised of two String
     * node labels, a Boolean indicator of directedness,
     * an optional Integer weight, and an optional
     * String edge label.
     * Nodes are referred to as start and end even
     * if that distinction is not meaningful (as for
     * undirected edges).
     * If weight is null, this edge is not part of
     * a graph whose edges have a weight attribute.
     */
    private class Edge implements Serializable  {
        private String start;
        private String end;
        private Boolean directed;
        private String label;
        private Integer weight;

        /**
         * Construct an edge with attributes as
         * specified by the parameters.
         * @param startNode the label of the start node
         * @param endNode the label of the end node
         * @param isDirected true if a directed edge; false otherwise
         * @param edgeLabel the label of this edge (null if none)
         * @param edgeWeight the integer weight of this edge (null if none)
         */
        Edge(String startNode, String endNode, Boolean isDirected,
             String edgeLabel, Integer edgeWeight) {
            this.start = startNode;
            this.end = endNode;
            this.directed = isDirected;
            this.label = edgeLabel;
            this.weight = edgeWeight;
        }

        String getStartNode() { return this.start; }

        String getEndNode() { return this.end; }

        Boolean isDirected() { return this.directed; }

        String getLabel() { return this.label; }

        Integer getWeight() { return this.weight; }

        @Override public String toString() {
            return "<" + this.start + "," + this.end + "," + 
                   this.directed + "," + this.label + "," + this.weight + ">";
        }

        /**
         * Equals predicate helper method.
         * @param x first object for comparison
         * @param y second object for comparison
         * @return true if both objects are null or
         *         objects' native equals method returns true;
         *         false otherwise
         */
        boolean closeEnough(Object x, Object y) {
            if (x == y) return true;
            if (x == null || y == null) return false;
            return x.equals(y);
        }

        @Override public boolean equals(Object o) {
            if ( this == o ) return true;
            if ( !(o instanceof Edge) ) return false;
            Edge that = (Edge)o;
            return
            closeEnough(this.start, that.start) &&
            closeEnough(this.end, that.end) &&
            closeEnough(this.directed, that.directed) &&
            closeEnough(this.label, that.label) &&
            closeEnough(this.weight, that.weight);
        }

        @Override public int hashCode() {
            return
            37 + 
            ((this.start    == null) ? 0 : this.start.hashCode()) +
            ((this.end      == null) ? 0 : this.end.hashCode()) +
            ((this.directed == null) ? 0 : this.directed.hashCode()) +
            ((this.label    == null) ? 0 : this.label.hashCode()) +
            ((this.weight   == null) ? 0 : this.weight.hashCode());
        }
    }
}