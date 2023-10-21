package chess;

class Knight extends Pieces {

    // Constructor for the Knight class
    public Knight (boolean isWhite) {

        // Call the constructor of the parent class (Pieces) with the appropriate piece type
        super (isWhite ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN);

    }

    // Override the "moveablePlay" method from the parent class
    @Override
    public boolean moveablePlay (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        // Calculate the absolute difference in X and Y coordinates between the previous and updated positions
        int sepX = Math.abs (updVal_X - prevVal_X);
        int sepY = Math.abs (updVal_Y - prevVal_Y);

        // Check if the move is both feasible and follows the L-shaped movement pattern of a Knight
        return feasMove (board, prevVal_X, prevVal_Y, updVal_X, updVal_Y) 
                && ((sepX == 1 && sepY == 2) 
                || (sepX == 2 && sepY == 1));

    }
    
}