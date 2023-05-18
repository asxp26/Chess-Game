// cd "Chess Game"
// javac ChessGame.java
// java ChessGame

import java.util.Scanner;

public class ChessGame {
    private static char[][] board;
    private static int whitePoints;
    private static int blackPoints;

    public static void main(String[] args) {
        initializeBoard();
        printBoard();

        Scanner scanner = new Scanner(System.in);
        boolean whiteTurn = true;

        while (true) {
            String player = whiteTurn ? "White" : "Black";
            System.out.println(player + " player's turn.");

            System.out.print("Enter source position (e.g., a2): ");
            String source = scanner.nextLine();

            System.out.print("Enter destination position (e.g., a4): ");
            String destination = scanner.nextLine();

            if (movePiece(source, destination, whiteTurn)) {
                updatePoints(board[destination.charAt(0) - 'a'][destination.charAt(1) - '1']);
                printBoard();

                if (whitePoints >= 10 || blackPoints >= 10) {
                    System.out.println("Game over!");
                    System.out.println("White: " + whitePoints + " points");
                    System.out.println("Black: " + blackPoints + " points");
                    break;
                }

                whiteTurn = !whiteTurn;
            } else {
                System.out.println("Invalid move! Try again.");
            }
        }
    }

    private static void initializeBoard() {
        board = new char[8][8];
        whitePoints = 0;
        blackPoints = 0;

        // Initialize pieces for white player
        board[0][0] = 'R';
        board[0][1] = 'N';
        board[0][2] = 'B';
        board[0][3] = 'Q';
        board[0][4] = 'K';
        board[0][5] = 'B';
        board[0][6] = 'N';
        board[0][7] = 'R';
        for (int i = 0; i < 8; i++) {
            board[1][i] = 'P';
        }

        // Initialize pieces for black player
        board[7][0] = 'r';
        board[7][1] = 'n';
        board[7][2] = 'b';
        board[7][3] = 'q';
        board[7][4] = 'k';
        board[7][5] = 'b';
        board[7][6] = 'n';
        board[7][7] = 'r';
        for (int i = 0; i < 8; i++) {
            board[6][i] = 'p';
        }
    }

    private static void printBoard() {
        System.out.println("   a b c d e f g h");
        System.out.println("  -----------------");
        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " |");
            for (int j = 0; j < 8; j++) {
                char piece = board[i][j] == '\0' ? '-' : board[i][j];
                System.out.print(piece + "|");
            }
            System.out.println(" " + (i + 1));
        }
        System.out.println("  -----------------");
        System.out.println("   a b c d e f g h");
        System.out.println();
    }

    private static boolean movePiece(String source, String destination, boolean whiteTurn) {
        int sourceFile = source.charAt(0) - 'a';
        int sourceRank = source.charAt(1) - '1';
        int destFile = destination.charAt(0) - 'a';
        int destRank = destination.charAt(1) - '1';
    
        if (isValidMove(sourceFile, sourceRank, destFile, destRank, whiteTurn)) {
            char piece = board[sourceRank][sourceFile];
            board[destRank][destFile] = piece;
            board[sourceRank][sourceFile] = '\0';
            return true;
        }

        return false;
    }

    private static boolean isValidMove(int sourceFile, int sourceRank, int destFile, int destRank, boolean whiteTurn) {
        char piece = board[sourceRank][sourceFile];

        // Check if the source position contains a piece
        if (piece == '\0') {
            return false;
        }

        // Check if the destination position is within the board
        if (destFile < 0 || destFile > 7 || destRank < 0 || destRank > 7) {
            return false;
        }

        // Check if the destination position is occupied by the player's own piece
        if (Character.isUpperCase(piece) && Character.isUpperCase(board[destRank][destFile])) {
            return false;
        }
        if (Character.isLowerCase(piece) && Character.isLowerCase(board[destRank][destFile])) {
            return false;
        }

        // Check specific rules for each piece (assuming basic rules for now)
        char pieceType = Character.toUpperCase(piece);
        switch (pieceType) {
            case 'P':
                if (whiteTurn) {
                    // White pawn
                    if (destRank - sourceRank == 1 && Math.abs(destFile - sourceFile) == 1 &&
                            Character.isLowerCase(board[destRank][destFile])) {
                        return true; // Capture diagonally
                    }
                    if (sourceRank == 1 && destRank - sourceRank == 2 && sourceFile == destFile &&
                            board[sourceRank + 1][sourceFile] == '\0') {
                        return true; // Double-step on first move
                    }
                    return destRank - sourceRank == 1 && sourceFile == destFile; // Move one step forward
                } else {
                    // Black pawn
                    if (sourceRank - destRank == 1 && Math.abs(destFile - sourceFile) == 1 &&
                            Character.isUpperCase(board[destRank][destFile])) {
                        return true; // Capture diagonally
                    }
                    if (sourceRank == 6 && sourceRank - destRank == 2 && sourceFile == destFile &&
                            board[sourceRank - 1][sourceFile] == '\0') {
                        return true; // Double-step on first move
                    }
                    return sourceRank - destRank == 1 && sourceFile == destFile; // Move one step forward
                }
            case 'R':
                return sourceFile == destFile || sourceRank == destRank; // Move horizontally or vertically
            case 'N':
                return (Math.abs(destFile - sourceFile) == 2 && Math.abs(destRank - sourceRank) == 1) ||
                        (Math.abs(destFile - sourceFile) == 1 && Math.abs(destRank - sourceRank) == 2); // L-shaped move
            case 'B':
                return Math.abs(destFile - sourceFile) == Math.abs(destRank - sourceRank); // Move diagonally
            case 'Q':
                return sourceFile == destFile || sourceRank == destRank ||
                        Math.abs(destFile - sourceFile) == Math.abs(destRank - sourceRank); // Move horizontally, vertically, or diagonally
            case 'K':
                return Math.abs(destFile - sourceFile) <= 1 && Math.abs(destRank - sourceRank) <= 1; // Move one step in any direction
            default:
                return false;
        }
    }

    private static void updatePoints(char piece) {
        if (piece == 'Q') {
            whitePoints += 5;
        } else if (piece == 'q') {
            blackPoints += 5;
   
        } else if (piece == 'R') {
            whitePoints += 3;
        } else if (piece == 'r') {
            blackPoints += 3;
        } else if (piece == 'B' || piece == 'N') {
            whitePoints += 2;
        } else if (piece == 'b' || piece == 'n') {
            blackPoints += 2;
        } else if (piece == 'P') {
            whitePoints += 1;
        } else if (piece == 'p') {
            blackPoints += 1;
        }
    }
}