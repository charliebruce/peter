package ember.server.game;

/**
 * A point on the world map.
 * @author charlie
 *
 */
public class Location implements Cloneable {

    private int x, y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public static Location location(int x, int y) {
        return new Location(x, y);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Location)) {
            return false;
        }
        Location loc = (Location) other;
        return loc.x == x && loc.y == y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public boolean withinDistance(Location other, int dist) {

        int deltaX = other.x - x, deltaY = other.y - y;
        return (deltaX <= (dist) && deltaX >= (0 - dist - 1) && deltaY <= (dist) && deltaY >= (0 - dist - 1));
    }

    public boolean withinDistance(Location other) {
       
        int deltaX = other.x - x, deltaY = other.y - y;
        return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
    }

    public boolean withinInteractionDistance(Location l) {
        return withinDistance(l, 3);
    }


    public double getDistance(Location other) {
        int xdiff = this.getX() - other.getX();
        int ydiff = this.getY() - other.getY();
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
    }

}
