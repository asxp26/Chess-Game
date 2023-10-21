// Anuj Patel 

package chess;
import java.util.ArrayList;

class ReturnPiece {

    // Define the possible types of chess pieces.
    static enum PieceType {WP, WR, WN, WB, WQ, WK,
                    BP, BR, BN, BB, BK, BQ};
    static enum PieceFile {a, b, c, d, e, f, g, h};

    // Type of the chess piece
    PieceType pieceType;
    // File (column) of the chess piece
    PieceFile pieceFile;
    // Rank (row) of the chess piece
    int pieceRank;  // 1..8

     // A method to return a string representation of the piece.
    public String toString() {

        return ""+pieceFile+pieceRank+":"+pieceType;

    }

    // Check if two ReturnPiece objects are equal.
    public boolean equals(Object other) {

        if (other == null || !(other instanceof ReturnPiece)) {

            return false;

        }

        ReturnPiece otherPiece = (ReturnPiece)other;

        return pieceType == otherPiece.pieceType &&
                pieceFile == otherPiece.pieceFile &&
                pieceRank == otherPiece.pieceRank;

    }

}

class ReturnPlay {

    enum Message {ILLEGAL_MOVE, DRAW,
        RESIGN_BLACK_WINS, RESIGN_WHITE_WINS,
        CHECK, CHECKMATE_BLACK_WINS,    CHECKMATE_WHITE_WINS,
        STALEMATE};

    // List of pieces on the board
    ArrayList<ReturnPiece> piecesOnBoard;
    // Message indicating the game status
    Message message;

}

public class Chess {

    static Board board = new Board();

    enum Player { white, black }

    /**
     * Plays the next move for whichever player has the turn.
     *
     * @param move String for next move, e.g. "a2 a3"
     *
     * @return A ReturnPlay instance that contains the result of the move.
     *         See the section "The Chess class" in the assignment description for details of
     *         the contents of the returned ReturnPlay instance.
     */
     public static ReturnPlay play(String move) {

        /* FILL IN THIS METHOD */
        
        /* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
        /* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
        
        return board.tryMove(move);

    }

    /**
     * This method should reset the game, and start from scratch.
     */
    public static void start() {

        /* FILL IN THIS METHOD */
        board = new Board();

        PlayChess.printBoard(board.getPiecesOnBoard());

    }
    
}

// ---- to run ----
// javac *.java
// cd ..
// java chess.PlayChess