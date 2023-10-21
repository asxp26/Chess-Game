package chess;
import java.util.ArrayList;

class Board {

    boardSpot[][] spotsOnBoard = new boardSpot[8][8];
    boolean whiteTurnCheck = true;
    boolean gameOverCheck = false;

    public Board () {

        // Initialize the chess board and pieces
        for (int x = 0; x < 8; x++) {

            for (int y = 0; y < 8; y++) {

                spotsOnBoard[x][y] = new boardSpot(x, y);

            }

        }

         // Set up the initial chess pieces on the board
        for (int x = 0; x < 2; x++) {

            spotsOnBoard[0][x * 7].piece = new Rook(x == 0);
            spotsOnBoard[1][x * 7].piece = new Knight(x == 0);
            spotsOnBoard[2][x * 7].piece = new Bishop(x == 0);
            spotsOnBoard[3][x * 7].piece = new Queen(x == 0);
            spotsOnBoard[4][x * 7].piece = new King(x == 0);
            spotsOnBoard[5][x * 7].piece = new Bishop(x == 0);
            spotsOnBoard[6][x * 7].piece = new Knight(x == 0);
            spotsOnBoard[7][x * 7].piece = new Rook(x == 0);

        }

        for (int x = 0; x <= 7; x++) {

            spotsOnBoard[x][1].piece = new Pawn(true);

        }

        for (int y = 0; y <= 7; y++) {

            spotsOnBoard[y][6].piece = new Pawn(false);

        }

    }

    public ReturnPlay tryMove (String move) {

        ReturnPlay play = new ReturnPlay();
        play.piecesOnBoard = getPiecesOnBoard();

        if (gameOverCheck) {

             // If the game is already over, return the current state.
            return play;

        }

        if (move.equals("resign")) {

            // Handle resignation of a player.
            if (whiteTurnCheck) {

                play.message = ReturnPlay.Message.RESIGN_BLACK_WINS;

            } 
            
            else {

                play.message = ReturnPlay.Message.RESIGN_WHITE_WINS;

            }

            return play;

        }

        String[] canMove = move.split(" ");
        boolean drawCheck = canMove.length == 3 && canMove[2].equals("draw?");
        boolean promCheck = canMove.length == 3
                && (canMove[2].equals("R")
                || canMove[2].equals("B")
                || canMove[2].equals("N")
                || canMove[2].equals("Q"));

        if ((canMove[0].length() != 2 || canMove[1].length() != 2) || (canMove.length != 2 && !drawCheck && !promCheck)) {

            // Check for illegal move format.
            play.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return play;

        }

        int prevVal_X = filePieceX(String.valueOf(canMove[0].charAt(0)));
        int prevVal_Y = Character.getNumericValue(canMove[0].charAt(1)) - 1;

        int updVal_X = filePieceX(String.valueOf(canMove[1].charAt(0)));
        int updVal_Y = Character.getNumericValue(canMove[1].charAt(1)) - 1;

        if (!posValid(prevVal_X, prevVal_Y) || !posValid(updVal_X, updVal_Y)) {

             // Check if the move is within the board bounds.
            play.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return play;

        }

        Pieces piece = spotsOnBoard[prevVal_X][prevVal_Y].piece;

        if (piece == null) {

            // There is no piece to move.
            play.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return play;

        }

        if (piece.checkAndDoMove(this, prevVal_X, prevVal_Y, updVal_X, updVal_Y, canMove.length == 3 ? canMove[2] : "")) {

             // If the move is valid, update the board and check for game status.
            play.piecesOnBoard = getPiecesOnBoard();
            whiteTurnCheck = !whiteTurnCheck;

            play.message = drawCheck ? ReturnPlay.Message.DRAW : (kingThreat(whiteTurnCheck) ? (checkMateCheck(whiteTurnCheck, updVal_X, updVal_Y) ? (whiteTurnCheck ? ReturnPlay.Message.CHECKMATE_BLACK_WINS : ReturnPlay.Message.CHECKMATE_WHITE_WINS) : ReturnPlay.Message.CHECK) : null);
            gameOverCheck = play.message != ReturnPlay.Message.CHECK && play.message != null;

            return play;

        }

        play.message = ReturnPlay.Message.ILLEGAL_MOVE;
        return play;
        
    }

    public boolean kingThreat (boolean isWhite) {

        boardSpot king = null;

        // Find the king of the specified color.
        for (int x = 0; x < 8; x++) {

            for (int y = 0; y < 8; y++) {

                if (spotsOnBoard[x][y].piece != null && spotsOnBoard[x][y].piece instanceof King && spotsOnBoard[x][y].piece.isWhite() == isWhite) {

                    king = spotsOnBoard[x][y];

                }

            }

        }

        if (king == null) {

            // King not found, no threat.
            return false;

        }

        // Check if any opponent's piece can attack the king.
        for (int x = 0; x < 8; x++) {

            for (int y = 0; y < 8; y++) {

                if (spotsOnBoard[x][y].piece != null && spotsOnBoard[x][y].piece.moveablePlay(this, x, y, king.x, king.y)) {

                    // King is under threat.
                    return true;

                }

            }

        }

        // King is not under threat.
        return false;

    }

    public boolean checkMateCheck (boolean isWhite, int xTrouble, int yTrouble) {

        boardSpot king = null;

         // Find the king of the specified color.
        for (int x = 0; x < 8; x++) {

            for(int y = 0; y < 8; y++) {

                if (spotsOnBoard[x][y].piece != null && spotsOnBoard[x][y].piece instanceof King && spotsOnBoard[x][y].piece.isWhite() == isWhite) {

                    king = spotsOnBoard[x][y];

                }

            }

        }

        if (king == null) {

            // King not found, not in checkmate.
            return false;

        }

          // Check if the king can move to an adjacent square.
        for (int x = -1; x < 2; x++) {

            for (int y = -1; y < 2; y++) {

                if (king.piece.futFeasMove(this, king.x, king.y, king.x + x, king.y + y)) {

                    // The king can escape to an adjacent square.
                    return false;

                }

            }

        }

         // Check if any piece of the same color can capture the threatening piece.
        for (int x = 0; x < 8; x++) {

            for (int y = 0; y < 8; y++) {

                if (spotsOnBoard[x][y].piece != null && spotsOnBoard[x][y].piece.isWhite() == isWhite) {

                    if (spotsOnBoard[x][y].piece.moveablePlay(this, x, y, xTrouble, yTrouble)) {
                        
                        System.out.println(x + " " + y);

                        if (!king.piece.futFeasMove(this, king.x, king.y, king.x + x, king.y + y)) {

                            continue;

                        }

                        // A friendly piece can capture the threatening piece.
                        return false;

                    }

                }

            }

        }

        // Checkmate, no escape for the king.
        return true;

    }

     // Iterate through the board and collect information about the pieces.
    public ArrayList<ReturnPiece> getPiecesOnBoard() {

        ArrayList<ReturnPiece> piecesOnBoard = new ArrayList<ReturnPiece>();

        for (int x = 0; x < 8; x++) {

            for (int y = 0; y < 8; y++) {

                if (spotsOnBoard[x][y].piece != null) {

                    ReturnPiece returnPiece = new ReturnPiece();
                    returnPiece.pieceRank = y + 1;
                    returnPiece.pieceFile = pieceFileX(x);
                    returnPiece.pieceType = spotsOnBoard[x][y].piece.pieceType;
                    piecesOnBoard.add(returnPiece);

                }

            }

        }

        return piecesOnBoard;

    }

    public ReturnPiece.PieceFile pieceFileX(int x) {

        return switch (x) {

            case 0 -> ReturnPiece.PieceFile.a;
            case 1 -> ReturnPiece.PieceFile.b;
            case 2 -> ReturnPiece.PieceFile.c;
            case 3 -> ReturnPiece.PieceFile.d;
            case 4 -> ReturnPiece.PieceFile.e;
            case 5 -> ReturnPiece.PieceFile.f;
            case 6 -> ReturnPiece.PieceFile.g;
            case 7 -> ReturnPiece.PieceFile.h;
            default -> null;

        };

    }

    public int filePieceX(String pieceFile) {

        return switch(pieceFile) {

            case "a" -> 0;
            case "b" -> 1;
            case "c" -> 2;
            case "d" -> 3;
            case "e" -> 4;
            case "f" -> 5;
            case "g" -> 6;
            case "h" -> 7;
            default -> -1;

        };

    }

    public boolean posValid(int x, int y) {

        return (y >= 0 && y <= 7) && (x >= 0 && x <= 7);

    }

}

class boardSpot {

    int x;
    int y;

    Pieces piece;

    public boardSpot(int x, int y) {

        this.x = x;
        this.y = y;

    }

}