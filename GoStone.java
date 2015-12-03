public class GoStone {
    private int xLoc;     // x location on board
    private int yLoc;     // y location on board
    private int myColor;  // 0 = black, 1 = white

    private boolean marked; // used by the grouping & capturing methods

    public GoStone(int x, int y, int color) {
       xLoc    = x;
       yLoc    = y;
       myColor = color;
       marked  = false;
    }

    public int x() {
        return xLoc;
    }

    public int y() {
        return yLoc;
    }

    public int color() {
        return myColor;
    }

    public void captured() {
        xLoc = -1;
        yLoc = -1;
    }

    public boolean alreadyCaptured() {
        return (xLoc == -1);
    }

    public char colorChar() {
        if (myColor == 0) { return 'B'; }
        else              { return 'W'; }
    }

    public void print() {
        System.out.print(colorChar() + "(" + xLoc + ',' + yLoc + ")");
    }
}
