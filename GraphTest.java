import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
/**
 * Tests for class Graph.
 * @author Dr. Jody Paul
 * @version 20130213a
 */
public class GraphTest {
    @Test
    public void TestNullGraph() {
        Graph graph0 = new Graph();
        assertEquals("GRAPH: Undirected\n Nodes([])\n Edges([])", graph0.toString());
        assertEquals(0, graph0.getNodeNamesAsStrings().size());
        assertEquals(graph0, graph0);
        graph0.saveGraph("graph0.ser");
        Graph graph0a = new Graph();
        assertEquals(graph0, graph0a);
        graph0a.restoreGraph("graph0.ser");
        assertEquals(graph0, graph0a);
        assertTrue(graph0.equals(graph0a));
    }

    @Test
    public void TestConstructUndirectedGraphs() {
        List<String> nodes = new ArrayList<String>();
        nodes.add("A");
        nodes.add("B");
        nodes.add("C");
        Graph graph1 = new Graph(nodes);
        assertEquals("GRAPH: Undirected\n Nodes([A, B, C])\n Edges([])", graph1.toString());
        assertEquals(nodes, graph1.getNodeNamesAsStrings());
        assertFalse(graph1.isDirected());
        assertEquals(graph1, graph1);
        graph1.saveGraph("graph1.ser");
        Graph graph1a = new Graph();
        graph1a.restoreGraph("graph1.ser");
        assertTrue(graph1.equals(graph1));
        assertEquals(graph1, graph1a);
        List<List<String>> edges = new ArrayList<List<String>>();
        List<String> edge = new ArrayList<String>();
        edge.add("A"); edge.add("B"); edge.add("false");
        edges.add(edge);
        edge = new ArrayList<String>();
        edge.add("A"); edge.add("C"); edge.add("false");
        edges.add(edge);
        Graph graph2 = new Graph(nodes, edges, true);
        assertEquals("GRAPH: Undirected\n Nodes([A, B, C])\n Edges([<A,B,false,null,null>, <A,C,false,null,null>])", graph2.toString());
        assertEquals(nodes, graph2.getNodeNamesAsStrings());
        assertEquals("A", graph2.getEdgesAsStrings().get(0).get(0).substring(0,1));
        assertEquals("B", graph2.getEdgesAsStrings().get(0).get(1).substring(0,1));
        assertEquals("A", graph2.getEdgesAsStrings().get(0).get(0).substring(0,1));
        assertEquals("C", graph2.getEdgesAsStrings().get(1).get(1).substring(0,1));
        assertFalse(graph2.isDirected());
        assertEquals(graph2, graph2);
        graph2.saveGraph("graph2.ser");
        Graph graph2a = new Graph();
        graph2a.restoreGraph("graph2.ser");
        assertEquals(nodes, graph2a.getNodeNamesAsStrings());
        assertTrue(graph2.equals(graph2a));
    }


    @Test
    public void TestConstructGeneralGraphs() {
        List<String> nodeNames = new ArrayList<String>();
        nodeNames.add("A");
        nodeNames.add("B");
        nodeNames.add("C");
        List<Integer> nodeCosts = new ArrayList<Integer>();
        nodeCosts.add(2);
        nodeCosts.add(9);
        nodeCosts.add(10);
        List<List<String>> edges = new ArrayList<List<String>>();
        List<String> edgeX = new ArrayList<String>();
        edgeX.add("A");
        edgeX.add("B");
        edgeX.add("true");
        edgeX.add("X");
        edgeX.add("100");
        edges.add(edgeX);
        List<String> edgeY = new ArrayList<String>();
        edgeY.add("A");
        edgeY.add("C");
        edgeY.add("true");
        edgeY.add("Y");
        edgeY.add("110");
        edges.add(edgeY);
        List<String> edgeZ = new ArrayList<String>();
        edgeZ.add("B");
        edgeZ.add("C");
        edgeZ.add("true");
        edgeZ.add("Z");
        edgeZ.add("120");
        edges.add(edgeZ);
        Graph graphG = new Graph(nodeNames, nodeCosts, new Boolean(true), edges);
        assertEquals("GRAPH: Directed\n Nodes([A, B, C])\n Edges([<A,B,true,X,100>, <A,C,true,Y,110>, <B,C,true,Z,120>])", graphG.toString());
        assertEquals(nodeNames, graphG.getNodeNamesAsStrings());
        assertEquals("A", graphG.getEdgesAsStrings().get(0).get(0).substring(0,1));
        assertEquals("B", graphG.getEdgesAsStrings().get(0).get(1).substring(0,1));
        assertEquals("A", graphG.getEdgesAsStrings().get(1).get(0).substring(0,1));
        assertEquals("C", graphG.getEdgesAsStrings().get(1).get(1).substring(0,1));
        assertEquals("B", graphG.getEdgesAsStrings().get(2).get(0).substring(0,1));
        assertEquals("C", graphG.getEdgesAsStrings().get(2).get(1).substring(0,1));
        assertTrue(graphG.isDirected());
        assertEquals(graphG, graphG);
        graphG.saveGraph("graphG.ser");
        Graph graphGa = new Graph();
        graphGa.restoreGraph("graphG.ser");
        assertEquals(nodeNames, graphGa.getNodeNamesAsStrings());
        assertEquals("A", graphGa.getEdgesAsStrings().get(0).get(0).substring(0,1));
        assertEquals("B", graphGa.getEdgesAsStrings().get(0).get(1).substring(0,1));
        assertEquals("A", graphGa.getEdgesAsStrings().get(1).get(0).substring(0,1));
        assertEquals("C", graphGa.getEdgesAsStrings().get(1).get(1).substring(0,1));
        assertEquals("B", graphGa.getEdgesAsStrings().get(2).get(0).substring(0,1));
        assertEquals("C", graphGa.getEdgesAsStrings().get(2).get(1).substring(0,1));
        assertEquals(graphG.toString(), graphGa.toString());
        assertEquals(graphG, graphGa);
    }
}