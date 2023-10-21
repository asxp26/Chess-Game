package chess;

class King extends Pieces {

    // Constructor for the King class
    public King (boolean isWhite) {

        // Call the constructor of the parent class (Pieces) with the appropriate piece type
        super (isWhite ? ReturnPiece.PieceType.WK : ReturnPiece.PieceType.BK);

    }

    // Override the "moveablePlay" method from the parent class
    @Override
    public boolean moveablePlay (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        // Calculate the absolute difference in X and Y coordinates between the previous and updated positions
        int sepX = Math.abs (updVal_X - prevVal_X);
        int sepY = Math.abs (updVal_Y - prevVal_Y);

        // Check if the King's move is within the rules of movement (one square in any direction)
        boolean usualPlay = ((sepX == 1 && (sepY == 1 || sepY == 0) || sepY == 1 && sepX == 0));

        // Check if the move is both feasible and follows the rules for the King's movement
        return feasMove (board, prevVal_X, prevVal_Y, updVal_X, updVal_Y) && usualPlay; 

    }

}