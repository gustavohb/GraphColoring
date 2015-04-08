/**
 * Created by Barrionuevo on 11/4/14.
 */
public class Pair<A, B> {

    public final A first;
    public final B second;

    public Pair(A fst, B snd) {
        this.first = fst;
        this.second = snd;
    }

    public String toString() {
        return "Pair[" + first + "," + second + "]";
    }

    private static boolean equals(Object x, Object y) {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    public boolean equals(Object other) {
        return
                other instanceof Pair<?, ?> &&
                        equals(first, ((Pair<?, ?>) other).first) &&
                        equals(second, ((Pair<?, ?>) other).second);
    }

    public int hashCode() {
        if (first == null) return (second == null) ? 0 : second.hashCode() + 1;
        else if (second == null) return first.hashCode() + 2;
        else return first.hashCode() * 17 + second.hashCode();
    }

}
