import org.apache.commons.collections15.Transformer;

import java.awt.Color;
import java.awt.Paint;
import java.util.Iterator;
import java.util.Set;
import javax.swing.*;


/**
 * This class is used to visualize graphs.
 * Created by Barrionuevo on 11/4/14.
 */
public class Visualization extends JPanel {

    protected static final class ColoredSetsTransformerSet<V> implements Transformer<V, Paint> {

        Set<Set<V>> verticesGroups_ = null;
        Color[] colors = null;

        public ColoredSetsTransformerSet(Set<Set<V>> verticesGroups) {
            super();
            this.verticesGroups_ = verticesGroups;
            colors = new Color[verticesGroups.size()];
            for (int i = 0; i < verticesGroups.size(); i++) {
                int red = Math.round((float) Math.sin(1.666 * i) * 127 + 128);
                int grn = Math.round((float) Math.sin(2.666 * i) * 127 + 128);
                int blu = Math.round((float) Math.sin(4.666 * i) * 127 + 128);
                float[] hsbvals = Color.RGBtoHSB(red, grn, blu, null);
                colors[i] = Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]);
            }
        }

        public Paint transform(V n) {
            Iterator<Set<V>> it_set = verticesGroups_.iterator();
            int i = 0;
            while (it_set.hasNext()) {
                Set<V> group = it_set.next();
                if (group.contains(n))
                    return colors[i];
                i++;
            }
            return Color.black;
        }

    }

}
