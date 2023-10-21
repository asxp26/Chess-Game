package chess;

class Queen extends Pieces {

    // Constructor for the Queen class
    public Queen (boolean isWhite) {

        // Call the constructor of the parent class (Pieces) with the appropriate piece type
        super (isWhite ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ);

    }

    // Override the "moveablePlay" method from the parent class
    @Override
    public boolean moveablePlay (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        // Check if the move is both feasible and follows the rules for the Queen's movement
        return feasMove (board, prevVal_X, prevVal_Y, updVal_X, updVal_Y) 
                && ((Math.abs(updVal_X - prevVal_X) == Math.abs(updVal_Y - prevVal_Y) // Check if the move is diagonal and there are no obstructions along the path
                && noPassDiagonally (board, prevVal_X, prevVal_Y, updVal_X, updVal_Y))
                || (((updVal_X != prevVal_X && updVal_Y == prevVal_Y) // Check if the move is horizontal or vertical and there are no obstructions along the path
                || (updVal_X == prevVal_X && updVal_Y != prevVal_Y)) 
                && noPassHorizontally (board, prevVal_X, prevVal_Y, updVal_X, updVal_Y)));

    }

}