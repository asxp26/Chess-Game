# Project Description

Implementation of a chess game in Java. It allows two players to take turns making moves on the chessboard. 

----------------------------------------------------------------------------------------------------------------------------------------

Here's a breakdown of the code:

- The ChessGame class contains the main method, where the game is initialized and played.
- The chessboard is represented by a 2D array of characters called board. The board is an 8x8 grid, and each cell represents a position on the board. Empty cells are represented by the null character ('\0').
- The whitePoints and blackPoints variables keep track of the points accumulated by each player.
- The main method initializes the board, prints the initial state of the board, and starts a loop that continues until the game is over.
- Inside the loop, it prompts the current player for their move by asking for the source position (where the piece is) and the destination position (where the piece will be moved).
- The movePiece method is called to validate and execute the move. If the move is valid, the piece is moved, and the points are updated based on the captured piece. The board is then printed again.
- After each move, it checks if either player has accumulated 10 or more points. If so, it prints the final scores and ends the game.
- The initializeBoard method sets up the initial configuration of the chessboard by placing the pieces in their starting positions.
- The printBoard method displays the current state of the board on the console.
- The isValidMove method checks if a move is valid based on the rules of chess. It considers factors like the type of piece, the source, and destination positions, and whether it's the player's turn.
- The updatePoints method updates the points based on the captured piece. Each piece type has a different point value.

----------------------------------------------------------------------------------------------------------------------------------------
