/**
 * Created by Barrionuevo on 11/4/14.
 */

import org.apache.commons.collections15.Factory;

public class GraphFactory implements Factory<Graph<String, Integer>> {

    public class VertexFactory implements Factory<String> {
        int c = 0;

        public String create() {
            c = c + incV;
            return "v" + c;
        }
    }

    public class EdgeFactory implements Factory<Integer> {
        int c = 0;

        public Integer create() {
            c = c + incE;
            return Integer.valueOf(c);
        }
    }

    // Instance Variables
    public int incV = +1;
    public int incE = -1;

    // Constructor
    public GraphFactory() {
        super();
    }

    // Methods
    public VertexFactory vertexFactory = new GraphFactory.VertexFactory();

    public EdgeFactory edgeFactory = new GraphFactory.EdgeFactory();

    public Graph<String, Integer> create() {
        return new Graph<String, Integer>();
    }


}
