package chess;

class Rook extends Pieces {

    // Constructor for the Rook class
    public Rook (boolean isWhite) {

        // Call the constructor of the parent class (Pieces) with the appropriate piece type
        super (isWhite ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR);

    }

    // Override the "moveablePlay" method from the parent class
    @Override
    public boolean moveablePlay (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        // Check if the move is both feasible and follows the horizontal or vertical movement pattern of a Rook
        return feasMove (board, prevVal_X, prevVal_Y, updVal_X, updVal_Y) 
                && (((updVal_X != prevVal_X && updVal_Y == prevVal_Y) // Check if the move is horizontal (along the same Y coordinate)
                || (updVal_X == prevVal_X && updVal_Y != prevVal_Y)) // Check if the move is vertical (along the same X coordinate)
                && noPassHorizontally (board, prevVal_X, prevVal_Y, updVal_X, updVal_Y)); // Ensure there are no obstructions along the horizontal or vertical path

    }

}