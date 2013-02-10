

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class VertexTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class VertexTest
{
    /**
     * Default constructor for test class VertexTest
     */
    Graph g;
    Vertex v1, v2, v3, v4;
    public VertexTest()
    {
         Graph g = new Graph(6,true);
         Vertex v1 = new Vertex(1,g);
         Vertex v2 = new Vertex(2,g);
         Vertex v3 = new Vertex(3,g);
         Vertex v4 = new Vertex(4,g);
         Vertex v5 = new Vertex(5,g);
    }
    @Test
    public void testAddEdge() {
        
    }
}
