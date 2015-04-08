import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by Barrionuevo on 11/23/14.
 */
public class DisplayColoringGraph extends JPanel implements ActionListener {

    static private final String newline = "\n";
    final DefaultModalGraphMouse graphMouse;
    GraphFactory gf;
    JButton genGraphButton;
    JButton colorGraphButton;
    JTextArea log;
    JTextField numOfVerticesField;
    JTextField connectProbField;
    VisualizationViewer vv;
    JScrollPane graphScrollPane;
    Boolean flag;
    Graph graph;

    public DisplayColoringGraph() {
        super(new BorderLayout());

        flag = false;

        log = new JTextArea(10, 70);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        numOfVerticesField = new JTextField(4);

        connectProbField = new JTextField(3);
        JScrollPane logScrollPane = new JScrollPane(log);

        graphScrollPane = new JScrollPane();
        graphScrollPane.setBackground(Color.white);

        genGraphButton = new JButton("Generates a Graph");
        genGraphButton.addActionListener(this);

        colorGraphButton = new JButton("Colors Graph");
        colorGraphButton.addActionListener(this);

        JLabel numOfVerticesFieldLabel = new JLabel("Number of Vertices: ");
        JLabel connectProbLabel = new JLabel("Probability of Connectedness (between 0 and 1): ");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(numOfVerticesFieldLabel);
        buttonPanel.add(numOfVerticesField);
        buttonPanel.add(connectProbLabel);
        buttonPanel.add(connectProbField);
        buttonPanel.add(genGraphButton);
        buttonPanel.add(colorGraphButton);

        setPreferredSize(new Dimension(900, 600));

        graph = new Graph();

        vv = new VisualizationViewer(new FRLayout(new GraphJung(graph)));
        graphMouse = new DefaultModalGraphMouse();

        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());
        vv.setBackground(Color.white);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        graphScrollPane.add(vv);

        add(buttonPanel, BorderLayout.PAGE_START);
        add(graphScrollPane, BorderLayout.CENTER);
        add(logScrollPane, BorderLayout.PAGE_END);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Graph Coloring");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new DisplayColoringGraph());

        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == genGraphButton) {
            try {
                int n = Integer.parseInt(numOfVerticesField.getText());
                float p = Float.parseFloat(connectProbField.getText());
                if (n >= 1 && p <= 1 && p > 0) {
                    int start = 0;
                    gf = new GraphFactory();
                    graph = RandomGraph.generateUndirectedGraph(
                            gf, gf.vertexFactory, gf.edgeFactory, n, p);
                    log.replaceRange(graph.toString().substring(0) + newline, start, log.getSelectionEnd());
                    remove(graphScrollPane);

                    FRLayout fl = new FRLayout(new GraphJung(graph));
                    fl.setSize(new Dimension(graphScrollPane.getWidth() - 25, graphScrollPane.getHeight() - 25));
                    vv = new VisualizationViewer(fl);
                    vv.setGraphMouse(graphMouse);
                    vv.addKeyListener(graphMouse.getModeKeyListener());
                    vv.setBackground(Color.white);
                    vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
                    graphScrollPane.removeAll();
                    graphScrollPane = new JScrollPane(vv);
                    add(graphScrollPane, BorderLayout.CENTER);
                    revalidate();
                    flag = true;
                }
            } catch (Exception e) {

            }
        } else if (event.getSource() == colorGraphButton && flag) {

            Coloring<String, Integer> coloring = new Coloring<String, Integer>(gf);
            Set<Set<String>> sol = coloring.graphColoring(graph);
            Transformer<String, Paint> vertexPaint = new Visualization.ColoredSetsTransformerSet<String>(sol);
            vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
            Iterator intr = sol.iterator();
            int color = 1;
            while (intr.hasNext()) {
                log.append("Set Color " + color++ + ": " + intr.next().toString() + newline);
            }

            log.append(newline + "Number of colors = " + sol.size() + newline);

            graphScrollPane.repaint();
            flag = false;

        }
    }
}
