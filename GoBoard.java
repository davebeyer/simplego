// Using ArrayList rather than Array whenever we have a list that may 
// need to grow using the ArrayList .add()  method
//
// Note that ArrayList has a "myArray.size()" method rather than "myArray.length" to return
// the current size/length of the list.   Also, to get the ith element, you need
// to do myArray.get(i) rather than myArray[i]

import java.util.ArrayList;

public class GoBoard {
    private GoStone[][]  boardPoints;   // size x size array
    private int          boardSize;

    private ArrayList<GoStone> capturedBlack; 
    private ArrayList<GoStone> capturedWhite; 

    GoBoard(int size) {
        boardSize   = size;
        boardPoints = new GoStone[size][size];

        capturedBlack = new ArrayList<GoStone>();
        capturedWhite = new ArrayList<GoStone>();
    }

    public void print() {
        GoStone stone;
        System.out.println("Current board:");

        // print columns heading
        System.out.print("     ");
        for (int y = 0; y < boardSize; y++) {
            System.out.print( String.format("%3d", y) );
        }
        System.out.println("");  // 
        System.out.println("");  // extra line break

        for (int x = 0; x < boardSize; x++) {
            System.out.print( String.format("%3d   ", x) );
            for (int y = 0; y < boardSize; y++) {
                stone = getStone(x, y);
                if (stone != null) { 
                    System.out.print(" " + stone.colorChar() + " ");
                } else {
                    System.out.print(" . ");
                }    
            }    
            System.out.println("");  // go to next line
        }    

        System.out.println(""); 
        System.out.println("  Captured white: " + capturedWhite.size() + 
                                     " black: " + capturedBlack.size());

        System.out.println("");  // insert blank line
    }

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isValidLocation(int x, int y) {
        if (x < 0 || x > boardSize - 1) { return false; }
        if (y < 0 || y > boardSize - 1) { return false; }
        return true;
    }

    public GoStone getStone(int x, int y) {
        if ( !isValidLocation(x, y) ) { return null; }
        return (boardPoints[x][y]);  // might be null if there's no stone there
    }

    public void placeStone(int x, int y, int color) {
       if (getStone(x, y) != null) {
          // report an error to the user (spot already taken!)

          // and then return
          return;
       }

       // It's convenient to track the stone's location 
       // in the stone itself, and on the board

       GoStone newStone = new GoStone(x, y, color);
       boardPoints[x][y] = newStone;

       //
       // Now check to see if placing this stone causes any of 
       // the neighbor stone groups of the opposite color to be captured,
       // and lastly, check whether this stone itself (and stone group)
       // is captured.  (Important to check neighbor stones first, since that
       // takes priority in the rules of Go.)
       //

       ArrayList<GoStone> nbrs = getNeighbors(newStone, opponentColor(newStone.color()));

       for (int i = 0; i < nbrs.size(); i++) {
           checkIfCaptured(nbrs.get(i));  // this also removes the stones if needed
       }

       // Finally, check whether this stone just placed was put into a captured spot
       checkIfCaptured(newStone);
    }

    ////////////////////////////////////////////////////////////////////////////////
    //
    // The remaining methods below handle determining groups and captured stones 
    //

    public void checkIfCaptured(GoStone stone) {
        if (stone.alreadyCaptured()) {
            // Already captured, probably part of a group that was just captured
            return;
        }

        // First get the entire group of stones connected to this stone
        ArrayList<GoStone> stoneGroup = getGroup(stone);

        // Then check whether group is dead (has no liberties), and if so, remove
        // the entire group 

        if ( isGroupDead(stoneGroup) ) {
            // inform user, list dead stones, wait for "ok" to proceed
            informUserOfDeadGroup(stoneGroup);  

            // Move captured stones to opponent's captured stones
            captureDeadStones(stoneGroup);    
        } 
    }

    //
    // The following methods are private (access is restricted to
    // the other methods in this class)
    //

    private void informUserOfDeadGroup(ArrayList<GoStone> stoneGroup) {
        print();  // print board

        System.out.println("The following stones have been captured:");
        System.out.print("  "); 

        for (int i = 0; i < stoneGroup.size(); i++) {
            stoneGroup.get(i).print();
            System.out.print("; ");
        }
        System.out.println("");  // new line

        System.out.println("Enter 'Return' to continue:");
        TextIO.getAnyChar();
    }

    private int opponentColor(int color) {
        if (color == 1) { return 0; }
        else            { return 1; }
    }

    private void captureDeadStones(ArrayList<GoStone> stones) {
        GoStone stone;

        for (int i = 0; i < stones.size(); i++) {
            stone = stones.get(i);

            boardPoints[stone.x()][stone.y()] = null;
            stone.captured();  // just erases board location

            if (stone.color() == 0) {
                capturedBlack.add(stone);
            } else {
                capturedWhite.add(stone);
            }
        }
    }

    private boolean isGroupDead(ArrayList<GoStone> stoneGroup) {
        for (int i = 0; i < stoneGroup.size(); i++) {
            if ( hasLiberty(stoneGroup.get(i)) ) {
                return false;
            }
        }        
        return true;
    }

    private boolean hasLiberty(GoStone stone) {
        if ( isFreePoint(stone.x() - 1,  stone.y()) ) { return true; }
        if ( isFreePoint(stone.x() + 1,  stone.y()) ) { return true; }
        if ( isFreePoint(stone.x(),  stone.y() - 1) ) { return true; }
        if ( isFreePoint(stone.x(),  stone.y() + 1) ) { return true; }

        return false; // no liberties
    }

    private boolean isFreePoint(int x, int y) {
        if ( !isValidLocation(x, y) ) { return false; }

        GoStone stone = getStone(x, y);

        if (stone == null) { return true;  }
        else               { return false; }        
    }

    private GoStone getStoneOfColor(int x, int y, int color) {
       GoStone stone = getStone(x, y);
       if (stone == null)          { return null; }

       // if not the right color, then skip it
       if (stone.color() != color) { return null; }

       return stone;
   }

    private ArrayList<GoStone> getNeighbors(GoStone stone, int color) {
        ArrayList<GoStone> neighbors = new ArrayList<GoStone>();
        GoStone   nbr;

        nbr = getStoneOfColor(stone.x() - 1, stone.y(), color);
        if (nbr != null) { neighbors.add(nbr); }

        nbr = getStoneOfColor(stone.x() + 1, stone.y(), color);
        if (nbr != null) { neighbors.add(nbr); }

        nbr = getStoneOfColor(stone.x(), stone.y() - 1, color);
        if (nbr != null) { neighbors.add(nbr); }

        nbr = getStoneOfColor(stone.x(), stone.y() + 1, color);
        if (nbr != null) { neighbors.add(nbr); }

        return neighbors;
    }

    //
    // getGroup() method, to compute a connected group of stones
    //

    private ArrayList<GoStone> getGroup(GoStone startingStone) {
        // Initialize the stoneGroup
        ArrayList<GoStone> stoneGroup = new ArrayList<GoStone>();

        doGetGroup(startingStone, stoneGroup);

        return stoneGroup;
    }

    // doGetGroup(), the main workhorse for getGroup() is a recursive
    // method to compute the complete stone group

    private void doGetGroup(GoStone stone, ArrayList<GoStone> stoneGroup) {
        // Add this stone  to the stone group
        stoneGroup.add(stone);

        ArrayList<GoStone> neighbors = getNeighbors(stone, stone.color());

        // For each neighbor (that hasn't already been handled), call
        // doGetGroup() recursively to get the stone group starting
        // from that neighbor

        for (int i = 0; i < neighbors.size(); i++) {
            if ( !stoneGroup.contains(neighbors.get(i)) ) {
                // This neighbor stone has not already been handled
                doGetGroup(neighbors.get(i), stoneGroup);
            }
        }
    }
   
}
