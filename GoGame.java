public class GoGame {
    GoPlayer player1;
    GoPlayer player2;
    GoBoard  goBoard;

    GoGame(GoPlayer p1, GoPlayer p2, GoBoard board) {
       player1 = p1;
       player2 = p2;
       goBoard = board;
    }

    public void play() {
        while(true) {
	    goBoard.print();
            player1.nextMove();

	    goBoard.print();
            player2.nextMove();
        }
    }

    public static void main(String[] args) {
        GoBoard  board   = new GoBoard(7);                 // n x n board size
        GoPlayer player1 = new HumanGoPlayer(board, 0);    // 0 = black
        GoPlayer player2 = new ComputerGoPlayer(board, 1); // 1 = white

        GoGame game = new GoGame(player1, player2, board);
        game.play();
    }
}
