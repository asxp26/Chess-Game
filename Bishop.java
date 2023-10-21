package chess;

class Bishop extends Pieces {

    // Constructor for the Bishop class
    public Bishop (boolean isWhite) {

        // Call the constructor of the parent class (Pieces) with the appropriate piece type
        super (isWhite ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB);

    }

    // Override the "moveablePlay" method from the parent class
    @Override
    public boolean moveablePlay (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        // Check if the move is both feasible and follows the diagonal movement pattern of a Bishop
        return feasMove (board, prevVal_X, prevVal_Y, updVal_X, updVal_Y) 
                && (Math.abs(updVal_X - prevVal_X) == Math.abs(updVal_Y - prevVal_Y) 
                && noPassDiagonally (board, prevVal_X, prevVal_Y, updVal_X, updVal_Y));

    }

}