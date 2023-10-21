package chess;

abstract class Pieces {

    // Type of the chess piece
    ReturnPiece.PieceType pieceType;
    // A flag to track if the piece has moved
    boolean regPos = true;

    public Pieces (ReturnPiece.PieceType pieceType) {

        this.pieceType = pieceType;

    }

    // Abstract method to check if a move is valid for a piece
    public abstract boolean moveablePlay (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y);

     // Check if a move is valid and update the board
    public boolean checkAndDoMove (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y, String prom) {

        // Check if it's the player's turn to move
        if (board.whiteTurnCheck != board.spotsOnBoard[prevVal_X][prevVal_Y].piece.isWhite()) {

            return false;

        }

         // Check if the move is a non-move (same location)
        if (prevVal_X == updVal_X && prevVal_Y == updVal_Y) {

            return false;

        }

         // Handle en passant move if valid
        if (goEmpFeasCheck(board, prevVal_X, prevVal_Y, updVal_X, updVal_Y)) {

            return true;

        }

         // Handle castling move if valid
        if (goCastValidCheck(board, prevVal_X, prevVal_Y, updVal_X, updVal_Y)) {

            return true;

        }

         // Check if the piece-specific move is valid
        if (moveablePlay(board, prevVal_X, prevVal_Y, updVal_X, updVal_Y)) {

            Pieces oldPiece = board.spotsOnBoard[prevVal_X][prevVal_Y].piece;
            Pieces newPiece = board.spotsOnBoard[updVal_X][updVal_Y].piece;

             // Temporarily update the board to check if the move exposes the king
            pieceSetup (board, prevVal_X, prevVal_Y, null);
            pieceSetup (board, updVal_X, updVal_Y, oldPiece);

             // Check if the king is threatened after the move
            if (board.kingThreat(board.whiteTurnCheck)) {

                // Revert the board to the original state
                pieceSetup(board, prevVal_X, prevVal_Y, oldPiece);
                pieceSetup(board, updVal_X, updVal_Y, newPiece);

                return false;

            }

            // Handle promotion if valid and set the regular position flag to false
            promIfFeas (board, updVal_X, updVal_Y, prom);
            regPos = false;

            return true;

        }

        return false;

    }

    // Check if a move is possible without updating the board
    public boolean futFeasMove (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        if (board.whiteTurnCheck != board.spotsOnBoard[prevVal_X][prevVal_Y].piece.isWhite()) {

            return false;

        }

        if (prevVal_X == updVal_X && prevVal_Y == updVal_Y) {

            return false;

        }

        if (moveablePlay(board, prevVal_X, prevVal_Y, updVal_X, updVal_Y)) {

            Pieces oldPiece = board.spotsOnBoard[prevVal_X][prevVal_Y].piece;
            Pieces newPiece = board.spotsOnBoard[updVal_X][updVal_Y].piece;

            pieceSetup(board, prevVal_X, prevVal_Y, null);
            pieceSetup(board, updVal_X, updVal_Y, oldPiece);

            // Check if the king is threatened after the move
            boolean checks = board.kingThreat(board.whiteTurnCheck);

            // Revert the board to the original state
            pieceSetup(board, prevVal_X, prevVal_Y, oldPiece);
            pieceSetup(board, updVal_X, updVal_Y, newPiece);

            return !checks;

        }

        return false;
        
    }

    // Check if a move is feasible based on the destination and presence of other pieces
    public boolean feasMove (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        if (updVal_X < 0 || updVal_X > 7 || updVal_Y < 0 || updVal_Y > 7) {

            return false;

        }

        Pieces piece = board.spotsOnBoard[updVal_X][updVal_Y].piece;
        boolean isLocTheSame = prevVal_X == updVal_X && prevVal_Y == updVal_Y;


        return ((piece == null && !isLocTheSame) ||
                (piece != null && piece.isWhite() != this.isWhite() && !isLocTheSame));

    }

    // Check if a piece can capture another piece
    public boolean canTake (Board board, int updVal_X, int updVal_Y) {

        return board.spotsOnBoard[updVal_X][updVal_Y].piece != null 
                && board.spotsOnBoard[updVal_X][updVal_Y].piece.isWhite() != isWhite();

    }

    // Set a piece on the board
    public void pieceSetup (Board board, int x, int y, Pieces piece) {

        board.spotsOnBoard[x][y].piece = piece;

    }

    // Check if the piece is white
    public boolean isWhite() {

        return pieceType == ReturnPiece.PieceType.WK || pieceType == ReturnPiece.PieceType.WR 
                || pieceType == ReturnPiece.PieceType.WQ || pieceType == ReturnPiece.PieceType.WP 
                || pieceType == ReturnPiece.PieceType.WN || pieceType == ReturnPiece.PieceType.WB;

    }

     // Check if there are no obstacles in a horizontal move
    public boolean noPassHorizontally (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        // Determine the direction of the move
        int negativeComponent = updVal_X == prevVal_X && updVal_Y > prevVal_Y ? -1 :
                (updVal_Y == prevVal_Y && updVal_X > prevVal_X ? -1 : 1);

        for (int i = 1; i < Math.abs(prevVal_X - updVal_X + prevVal_Y - updVal_Y); i++) {

            int x = updVal_X == prevVal_X ? updVal_X : (updVal_X + (negativeComponent * i));
            int y = updVal_Y == prevVal_Y ? updVal_Y : (updVal_Y + (negativeComponent * i));

            if (x < 0 || x > 7 || y < 0 || y > 7) {

                continue;

            }

            if (board.spotsOnBoard[x][y].piece != null) {

                return false;

            }

        }

        return true;

    }

    // Check if there are no obstacles in a diagonal move
    public boolean noPassDiagonally (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        // Determine the direction of the move
        int xNegativeComponent = updVal_X > prevVal_X ? 1 : -1;
        int yNegativeComponent = updVal_Y > prevVal_Y ? 1 : -1;

        for (int i = 1; i < Math.abs(prevVal_X - updVal_X); i++) {

            if (board.spotsOnBoard[prevVal_X + xNegativeComponent * i][prevVal_Y + yNegativeComponent * i].piece != null) {

                return false;

            }

        }

        return true;

    }

    // Check if a piece's move doesn't expose the king to check
    public boolean newMoveNoCheck (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        Pieces oldPiece = board.spotsOnBoard[prevVal_X][prevVal_Y].piece;
        Pieces newPiece = board.spotsOnBoard[updVal_X][updVal_Y].piece;

        pieceSetup(board, prevVal_X, prevVal_Y, null);
        pieceSetup(board, updVal_X, updVal_Y, oldPiece);

         // Check if the king is threatened after the move
        boolean checks = board.kingThreat(board.whiteTurnCheck);

        // Revert the board to the original state
        pieceSetup(board, prevVal_X, prevVal_Y, oldPiece);
        pieceSetup(board, updVal_X, updVal_Y, newPiece);

        return !checks;

    }

    // Check if a castling move is valid for the king
    public boolean castValidCheck (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        Pieces lRook = board.whiteTurnCheck ? board.spotsOnBoard[0][0].piece : board.spotsOnBoard[0][7].piece;
        Pieces rRook = board.whiteTurnCheck ? board.spotsOnBoard[7][0].piece : board.spotsOnBoard[7][7].piece;

        return (this instanceof King) && regPos && ((updVal_X == 6 &&
                lRook instanceof Rook &&
                lRook.regPos &&
                !board.kingThreat(board.whiteTurnCheck) &&
                futFeasMove(board, prevVal_X, prevVal_Y, 5, updVal_Y) &&
                newMoveNoCheck(board, prevVal_X, prevVal_Y, 6, updVal_Y) &&
                board.spotsOnBoard[6][prevVal_Y].piece == null)
                || (updVal_X == 2 &&
                rRook instanceof Rook &&
                rRook.regPos &&
                !board.kingThreat(board.whiteTurnCheck) &&
                futFeasMove(board, prevVal_X, prevVal_Y, 3, updVal_Y) &&
                newMoveNoCheck(board, prevVal_X, prevVal_Y, 2, updVal_Y) &&
                board.spotsOnBoard[2][prevVal_Y].piece == null));

    }

    // Perform a castling move if it's valid
    public boolean goCastValidCheck(Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        if (castValidCheck(board, prevVal_X, prevVal_Y, updVal_X, updVal_Y)) {

            if (updVal_X == 2) {

                board.spotsOnBoard[3][prevVal_Y].piece = board.spotsOnBoard[0][prevVal_Y].piece;
                board.spotsOnBoard[0][prevVal_Y].piece = null;

                board.spotsOnBoard[2][prevVal_Y].piece = board.spotsOnBoard[4][prevVal_Y].piece;
                board.spotsOnBoard[4][prevVal_Y].piece = null;

                board.spotsOnBoard[3][prevVal_Y].piece.regPos = false;
                board.spotsOnBoard[2][prevVal_Y].piece.regPos = false;
            } 
            
            else {

                board.spotsOnBoard[5][prevVal_Y].piece = board.spotsOnBoard[7][prevVal_Y].piece;
                board.spotsOnBoard[7][prevVal_Y].piece = null;

                board.spotsOnBoard[6][prevVal_Y].piece = board.spotsOnBoard[4][prevVal_Y].piece;
                board.spotsOnBoard[4][prevVal_Y].piece = null;

                board.spotsOnBoard[5][prevVal_Y].piece.regPos = false;
                board.spotsOnBoard[6][prevVal_Y].piece.regPos = false;

            }

            return true;

        }

        return false;

    }

    // Perform an en passant capture if it's valid
    public boolean goEmpFeasCheck (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        if (empFeasCheck(board, prevVal_X, prevVal_Y, updVal_X, updVal_Y)) {

            Pieces pawn = board.spotsOnBoard[prevVal_X][prevVal_Y].piece;
            Pieces destroyed = board.spotsOnBoard[updVal_X][updVal_Y - (isWhite() ? 1 : -1)].piece;

            board.spotsOnBoard[prevVal_X][prevVal_Y].piece = null;
            board.spotsOnBoard[updVal_X][updVal_Y - (isWhite() ? 1 : -1)].piece = null;
            board.spotsOnBoard[updVal_X][updVal_Y].piece = pawn;

            // Check if the king is threatened after the move
            if (board.kingThreat(isWhite())) {

                board.spotsOnBoard[prevVal_X][prevVal_Y].piece = pawn;
                board.spotsOnBoard[updVal_X][updVal_Y - (isWhite() ? 1 : -1)].piece = destroyed;
                board.spotsOnBoard[updVal_X][updVal_Y].piece = null;

                return false;

            }

            return true;

        }

        return false;

    }

    // Check if an en passant move is valid
    public boolean empFeasCheck (Board board, int prevVal_X, int prevVal_Y, int updVal_X, int updVal_Y) {

        int negativeComponent = isWhite() ? 1 : -1;
        Pieces maybePawn = board.spotsOnBoard[prevVal_X][prevVal_Y].piece;
        Pieces maybeCapturedPawn = board.spotsOnBoard[updVal_X][updVal_Y - negativeComponent].piece;

        return updVal_Y - prevVal_Y == negativeComponent
                && Math.abs(updVal_X - prevVal_X) == 1
                && board.spotsOnBoard[updVal_X][updVal_Y].piece == null
                && maybePawn instanceof Pawn
                && maybeCapturedPawn instanceof Pawn
                && board.spotsOnBoard[updVal_X][updVal_Y - negativeComponent].piece != null
                && board.spotsOnBoard[updVal_X][updVal_Y - negativeComponent].piece.isWhite() != isWhite()
                && updVal_Y == (isWhite() ? 5 : 2);

    }

    // Promote a pawn if it reaches the opposite end of the board
    public void promIfFeas (Board board, int updVal_X, int updVal_Y, String prom) {

        if ((updVal_Y == 7 && isWhite() || updVal_Y == 0 && !isWhite()) && board.spotsOnBoard[updVal_X][updVal_Y].piece instanceof Pawn) {

            switch (prom) {

                case "", "Q" -> board.spotsOnBoard[updVal_X][updVal_Y].piece = new Queen(isWhite());
                case "R" -> board.spotsOnBoard[updVal_X][updVal_Y].piece = new Rook(isWhite());
                case "N" -> board.spotsOnBoard[updVal_X][updVal_Y].piece = new Knight(isWhite());
                case "B" -> board.spotsOnBoard[updVal_X][updVal_Y].piece = new Bishop(isWhite());
                default -> System.out.println("ERROR");

            }

        }

    }

}