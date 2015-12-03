
public class HumanGoPlayer extends GoPlayer {

   HumanGoPlayer(GoBoard board, int color) {
        super(board, color);
   }

   public void nextMove() {
       int x;
       int y;

       while (true) {
           System.out.println("Enter row for next move for " + myColorStr());
           x = TextIO.getlnInt();

           System.out.println("Enter column for next move for " + myColorStr());
           y = TextIO.getlnInt();

           if ( !goBoard.isValidLocation(x, y) ) {
              System.out.println("That is an invalid location!");
              continue;
           }

           if ( goBoard.getStone(x, y) != null ) {
              System.out.println("There is already a stone there!");
              continue;
           }

           goBoard.placeStone(x, y, myColor);
           return;   // done with move
       }           
   }
}
