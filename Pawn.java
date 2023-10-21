package chess;

class Pawn extends Pieces {

    // Constructor for the Pawn class
    public Pawn (boolean isWhite) {

        // Call the constructor of the parent class (Pieces) with the appropriate piece type
        super (isWhite ? ReturnPiece.PieceType.WP : ReturnPiece.PieceType.BP);

    }

    @Override
    public boolean moveablePlay (Board board, int oldX, int oldY, int newX, int newY) {

        // Determine the direction of movement based on whether the Pawn is white or black
        int moveP = isWhite() ? 1 : -1;

        // Check if the move is both feasible and follows the rules for Pawn movement
        return feasMove (board, oldX, oldY, newX, newY) 
                && (((newY - oldY == moveP && oldX == newX) 
                && board.spotsOnBoard[newX][newY].piece == null) // Check for a standard one-square move forward with no obstruction
                || (this.regPos && newY - oldY == 2 * moveP // Check for a two-square move forward from the starting position
                && newX == oldX && board.spotsOnBoard[oldX][oldY + moveP].piece == null 
                && board.spotsOnBoard[newX][newY].piece == null) 
                || (Math.abs(newX - oldX) == 1 && newY - oldY == moveP 
                && (canTake(board, newX, newY)))); // Check for a diagonal capture move

    }
}