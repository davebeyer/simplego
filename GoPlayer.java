public class GoPlayer {
    // Make these protected rather than private, so that the Human 
    // and ComputerGoPlayer classes can access them
    protected GoBoard goBoard;
    protected int     myColor;

    GoPlayer(GoBoard board, int color) {
        goBoard = board;
        myColor = color;
    }

    public void nextMove() {
        System.out.println("Child class must implement this!");
    }

    public String myColorStr() {
        if (myColor == 0) { return "Black"; }
        else              { return "White"; }
    }
}
