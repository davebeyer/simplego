
public class ComputerGoPlayer extends GoPlayer {

   ComputerGoPlayer(GoBoard board, int color) {
        super(board, color);
   }

   public void nextMove() {
       int x;
       int y;

       while (true) {
           x = (int) ( Math.random() * goBoard.getBoardSize() );
           y = (int) ( Math.random() * goBoard.getBoardSize() );

           if ( goBoard.getStone(x, y) != null ) {
               // there's a stone in this spot, so try again
               continue;
           }

           // found an empty spot
           System.out.println("Computer (" + myColorStr() + ") moves to spot " + x + "," + y + " - Enter 'Return' to continue.");
           TextIO.getAnyChar();

           goBoard.placeStone(x, y, myColor);
           return;   // done with move
       }
   }
}
