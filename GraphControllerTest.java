import java.util.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * GraphTest
 * 
 * JUnit test case for graph API
 * 
 * @author Zach Souser
 * @version 2/13/13
 */

public class GraphControllerTest {
    GraphController g1, g2, g3, g4;
    NodeController[] v;
    EdgeController[] e;
    public GraphControllerTest() {
        g1 = new GraphController(true);
        g2 = new GraphController(false);
        g3 = new GraphController(true);
        v = new NodeController[20];
        e = new EdgeController[20];
        for (int i = 0; i < 6; i++) {
            v[i] = g1.addVertex(new NodeController(""+i,g1));
        }
         
        for (int i = 6; i < 9; i++) {
            v[i] = g2.addVertex(new NodeController(""+i,g2));
        }
        
        for (int i = 9; i < 18; i++) {
            v[i] = g3.addVertex(new NodeController(""+i,g3));
        }
       
       // E0-E4 and V0-V5
        e[0] = g1.addEdge(new EdgeController(v[0],v[1]));
        e[1] = g1.addEdge(new EdgeController(v[1],v[2]));
        e[2] = g1.addEdge(new EdgeController(v[0],v[3]));
        e[3] = g1.addEdge(new EdgeController(v[2],v[4]));
        e[4] = g1.addEdge(new EdgeController(v[3],v[5]));
        // E5-E7 and V6-V8
        e[5] = g2.addEdge(new EdgeController(v[6],v[7]));
        e[6] = g2.addEdge(new EdgeController(v[7],v[8]));
        e[7] = g2.addEdge(new EdgeController(v[8],v[6]));
        // E8-> and V9-V19
        e[8] = g3.addEdge(new EdgeController(v[9],v[10])); //
        e[9] = g3.addEdge(new EdgeController(v[9],v[11]));//
        e[10] = g3.addEdge(new EdgeController(v[9],v[12]));//
        e[11] = g3.addEdge(new EdgeController(v[11],v[17]));//
        e[12] = g3.addEdge(new EdgeController(v[11],v[16]));
        e[13] = g3.addEdge(new EdgeController(v[17],v[16]));
        e[14] = g3.addEdge(new EdgeController(v[12],v[13]));//
        e[15] = g3.addEdge(new EdgeController(v[13],v[14]));//
        e[16] = g3.addEdge(new EdgeController(v[13],v[15]));//
        
    }
    
    @Test
    public void testAccessors() {
        NodeController dummy = g1.addVertex(new NodeController("",g1));
        assert(g1.vertices.contains(dummy));
        g1.deleteVertex(dummy);
        assertFalse(g1.vertices.contains(dummy));
        
    }
    
    @Test
    public void testFindPath() {
        
        // G1
        
        // Path from 0 to 5
        
        List<NodeController> path = g1.findPath(v[0],v[5]);
        List<NodeController> expected = new ArrayList<NodeController>();
        expected.add(v[0]);
        expected.add(v[3]);
        expected.add(v[5]);
        assertEquals(path,expected);
        
        // Path from 1 to 4
        
        path = g1.findPath(v[1], v[4]);
        expected = new ArrayList<NodeController>();
        expected.add(v[1]);
        expected.add(v[2]);
        expected.add(v[4]);
        assertEquals(path,expected);
        
        // Path from 5 to 4 does not exist
        
        path = g1.findPath(v[5],v[4]);
        assert(path.isEmpty());
        
        // G2
        // not a DAG, won't have a path
        assertNull(g2.findPath(v[6],v[8]));
        
        // G3
        
        path = g3.findPath(v[9],v[15]);
        expected = new ArrayList<NodeController>();
        expected.add(v[9]);
        expected.add(v[12]);
        expected.add(v[13]);
        expected.add(v[15]);
        assertEquals(path,expected);
        
        path = g3.findPath(v[9],v[16]);
        expected = new ArrayList<NodeController>();
        expected.add(v[9]);
        expected.add(v[11]);
        expected.add(v[17]);
        expected.add(v[16]); 
        assertEquals(path,expected);
        
    }
    
    @Test
    public void testTopSort() {
        Stack<NodeController> sort = g1.topSort();
        Stack<NodeController> expected = new Stack<NodeController>();
       
        // Note that the expected top sort is in reverse, it is a stack
        
        expected.push(v[4]);
        expected.push(v[2]);
        expected.push(v[1]);
        expected.push(v[5]);
        expected.push(v[3]);
        expected.push(v[0]);
        
        assertEquals(sort,expected);
        
        
        // Not a DAG, will be null
        assertNull(g2.topSort());
        
        sort = g3.topSort();
        expected = new Stack<NodeController>();
        expected.push(v[10]);
        expected.push(v[16]);
        expected.push(v[17]);
        expected.push(v[11]);
        expected.push(v[14]);
        expected.push(v[15]);
        expected.push(v[13]);
        expected.push(v[12]);
        expected.push(v[9]);
        
        assertEquals(sort,expected);
    }
    
    @Test
    public void testStronglyConnected() {
        List<Integer> sccList = g1.stronglyConnected();
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        assertEquals(sccList,expected);
        
        
        sccList = g2.stronglyConnected();
        expected = new ArrayList<Integer>();
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(-1));
        assertEquals(sccList,expected);
        
        sccList = g3.stronglyConnected();
        expected = new ArrayList<Integer>();
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        expected.add(new Integer(1));
        assertEquals(sccList,expected);
    }
    
    @Test
    public void testIsTree() {
        assert(g1.isTree());
        assertFalse(g2.isTree());
        assert(g1.isTree());
    }
    
    @Test
    public void testGetArticulationVertices() {
        
        // G1
        
        List<NodeController> results = g1.getArticulationVertices();
        List<NodeController> expected = new ArrayList<NodeController>();
        expected.add(v[2]);
        expected.add(v[1]);
        expected.add(v[3]);
        expected.add(v[0]);
        
        assertEquals(results,expected);
        
        
        // G2
        
        results = g2.getArticulationVertices();
        expected = new ArrayList<NodeController>();
        expected.add(v[6]);
        
        assertEquals(results,expected);
        
        // G3
        
        results = g3.getArticulationVertices();
        expected = new ArrayList<NodeController>();
        expected.add(v[17]);
        expected.add(v[11]);
        expected.add(v[13]);
        expected.add(v[12]);
        expected.add(v[9]);
        assertEquals(results,expected);
    }
    
    
    @Test
    public void testFindBridges() {
        List<EdgeController> results = g1.findBridges();
        List<EdgeController> expected = new ArrayList<EdgeController>();
       // for g1, all edges are bridges
        expected.add(e[0]);
        expected.add(e[1]);
        expected.add(e[2]);
        expected.add(e[3]);
        expected.add(e[4]);
       
        assertEquals(results,expected);
       
       
        // G2 has no bridges
        assert(g2.findBridges().isEmpty());
        
        // G3 
        // All edges but the ones that form a cross edge are bridges
        results = g3.findBridges();
        expected = new ArrayList<EdgeController>();
        expected.add(e[8]);
        expected.add(e[9]);
        expected.add(e[10]);
        expected.add(e[11]);
        expected.add(e[14]);
        expected.add(e[15]);
        expected.add(e[16]);
        assertEquals(results,expected);
    }
}