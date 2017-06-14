/*Board.java*/

import java.awt.Font;

/**
 * Represents a Board configuration of a game of Checkers61bl
 * 
 * @author
 */

public class Board {

	/**
	 * Define any variables associated with a Board object here. These variables
	 * MUST be private.
	 */
	private Piece pieces[][];
	private Piece curr;
	private int currX;
	private int currY;
	private int firePieces;
	private int waterPieces;
	private int side = 0;
	private boolean fireTurn;
	private boolean notMoved;
	private boolean yesFire;
	private boolean exploded = false;
	private boolean justBlewUp = false;

	/**
	 * Constructs a new Board
	 * 
	 * @param shouldBeEmpty
	 *            if true, add no pieces
	 */
	public Board(boolean empty) {
		StdDrawPlus.setScale(0, 8);
		pieces = new Piece[8][8];
		curr = null;
		currX = -1;
		currY = -1;
		firePieces = 12;
		waterPieces = 12;
		fireTurn = true;
		notMoved = true;
		drawBoard();
		if (!empty) {
			drawPieces(true);
		}
	}

	// ------------CUSTOM METHODS START HERE------------ //

	/**
	 * Draws a new Board
	 */
	private void drawBoard() {
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[0].length; j++) {
				if (currX == i && currY == j) {
					StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
				} else if ((i + j) % 2 == 0) {
					StdDrawPlus.setPenColor(StdDrawPlus.LIGHT_GRAY);
				} else {
					StdDrawPlus.setPenColor(StdDrawPlus.BLACK);
				}
				StdDrawPlus.filledSquare(i + .5, j + .5, .5);
			}
		}
	}

	private void drawCurrentPlayer() {
		StdDrawPlus.setPenColor(StdDrawPlus.BLACK);
		StdDrawPlus.setFont(new Font("Serif", Font.PLAIN, 16));
		StdDrawPlus.text(pieces.length / 2, -0.225, "Current Player: " + turn());
	}

	/**
	 * Draw new pieces
	 * 
	 * @param start
	 *            if true, initialize new pieces at the beginning
	 */
	private void drawPieces(boolean start) {
		int side, row;
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[0].length; j++) {
				Piece p = pieces[i][j];
				if (j < 4) {
					side = 0;
					row = j;
				} else {
					side = 1;
					row = 7 - j;
				}
				if (start) {
					if ((i + j) % 2 == 0) {
						if (row == 0) {
							pieces[i][j] = new Piece(side, this);
						}
						if (row == 1) {
							pieces[i][j] = new ShieldPiece(side, this);
						}
						if (row == 2) {
							pieces[i][j] = new BombPiece(side, this);
						}
					}
				}
				if (p != null) {
					place(p, i, j);
				}
			}
		}
	}

	private String turn() {
		if (side == 0) {
			return "Fire";
		}
		return "Water";
	}

	/**
	 * Checks to make sure that a piece can move to a valid square rather than
	 * just anywhere
	 */
	private boolean validMove(int x1, int y1, int x2, int y2) {
		Piece p = pieceAt(x1, y1);
		int validX, validY;
		validX = x2 - x1;
		validY = y2 - y1;
		if (validY * kingModifier(p) >= 0 && Math.abs(validX) == Math.abs(validY) && Math.abs(validY) <= 2) {
			if (pieceAt(((validX / 2) + x1), ((validY / 2) + y1)) != null)
				return true;
		}
		return false;
	}

	/**
	 * Determines whether or not a king piece should be able to move forwards
	 * AND backwards
	 */
	private int kingModifier(Piece p) {
		if (p.isKing() == false) {
			return 1 - 2 * p.side();
		}
		return 0;

	}

	// ------------CUSTOM METHODS END HERE------------ //

	/**
	 * gets the Piece at coordinates (x, y)
	 * 
	 * @param x
	 *            X-coordinate of Piece to get
	 * @param y
	 *            Y-coordinate of Piece to get
	 * @return the Piece at (x, y)
	 */
	public Piece pieceAt(int x, int y) {
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			return null;
		}
		return pieces[x][y];
	}

	/**
	 * Places a Piece at coordinate (x, y)
	 * 
	 * @param p
	 *            Piece to place
	 * @param x
	 *            X coordinate of Piece to place
	 * @param y
	 *            Y coordinate of Piece to place
	 */
	public void place(Piece p, int x, int y) {
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			System.out.println("Checker piece is not within range of the board.");
		}
		if (pieceAt(x, y) == null) {
			if (curr != null) {
				move(currX, currY, x, y);
				currX = x;
				currY = y;
				if (y == 7 && yesFire == true || y == 0 && yesFire == false) {
					if (p.getType() == "bomb") {
						exploded = false;
					}
					p.isKing();
				}
			}
			if (exploded == false) {
				pieces[x][y] = p;
			} else {
				pieces[x][y] = p;
				remove(x, y);
				justBlewUp = true;
			}
			if (p.side() == 0) {
				firePieces++;
				yesFire = true;
			} else {
				waterPieces++;
				yesFire = false;
			}
		}
		if (p != null) {
			StdDrawPlus.picture(x + .5, y + .5, p.picture(), 1, 1);
		}
	}

	/**
	 * Removes a Piece at coordinate (x, y)
	 * 
	 * @param x
	 *            X coordinate of Piece to remove
	 * @param y
	 *            Y coordinate of Piece to remove
	 * @return Piece that was removed
	 */
	public Piece remove(int x, int y) {
		Piece p = pieceAt(x, y);
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			System.out.println("Checker piece is not within range of the board.");
			return null;
		}
		if (p == null) {
			System.out.println("There is no piece at these coordinates.");
			return null;
		} else {
			if (p.side() == 0) {
				firePieces--;
				yesFire = true;
			} else {
				waterPieces--;
				yesFire = false;
			}
			pieces[x][y] = null;
			if ((x + y) % 2 == 0) {
				StdDrawPlus.setPenColor(StdDrawPlus.LIGHT_GRAY);
			} else {
				StdDrawPlus.setPenColor(StdDrawPlus.BLACK);
			}
			StdDrawPlus.filledSquare(x + .5, y + .5, .5);
		}
		return p;
	}

	/**
	 * Determines if a Piece can be selected
	 * 
	 * @param x
	 *            X coordinate of Piece
	 * @param y
	 *            Y coordinate of Piece to select
	 * @return true if the Piece can be selected
	 */
	public boolean canSelect(int x, int y) {
		boolean condition1;
		boolean condition2;
		Piece p = pieceAt(x, y);
		if (p != null && p.equals(curr) == false && p.side() == side) {
			return curr == null || notMoved;
		} else {
			if (curr != null) {
				condition1 = notMoved && validMove(currX, currY, x, y);
				condition2 = curr.hasCaptured() && validMove(currX, currY, x, y);
				return condition1 || condition2;
			}
		}
		return false;
	}

	/**
	 * Selects a square. If no Piece is active, selects the Piece and makes it
	 * active. If a Piece is active, performs a move if an empty place is
	 * selected. Else, allows you to reselect Pieces
	 * 
	 * @param x
	 *            X coordinate of place to select
	 * @param y
	 *            Y coordinate of place to select
	 */
	public void select(int x, int y) {
		Piece p = pieceAt(x, y);
		if (p == null) {
			place(curr, x, y);
			notMoved = false;
		}
		curr = pieces[x][y];
		currX = x;
		currY = y;
	}

	/**
	 * Moves the piece located at Piece at (x1, y1) to (x2, y2)
	 * 
	 * @param x1
	 *            Original X coordinate of p
	 * @param y1
	 *            Origin Y coordinate of p
	 * @param x2
	 *            X coordinate to move to
	 * @param y2
	 *            Y coordinate to move to
	 */
	public void move(int x1, int y1, int x2, int y2) {
		Piece p1, temp;
		Piece p2 = pieceAt(x1, y1);
		int capturedX = (x1 + x2) / 2;
		int capturedY = (y1 + y2) / 2;
		if (Math.abs(x1 - x2) == 1) {
			capturedX = x1;
			capturedY = y1;
		}
		p1 = pieceAt(capturedX, capturedY);
		if (p1 == null) {
			return;
		}
		if (p1.side() != side) {
			p1.startCapturing();
			remove(capturedX, capturedY);
			p1.hasCaptured();
			if (p2.getType() == "bomb") {
				for (int i = x2 - 1; i < x2 + 2; i++) {
					for (int j = y2 - 1; j < y2 + 2; j++) {
						temp = pieceAt(i, j);
						if (temp != null && temp.getType() != "shield") {
							remove(i, j);
						}
						exploded = true;
					}
				}
			}
		}
		remove(x1, y1);
		x1 = x2;
		y1 = y2;
		if (yesFire == true && y1 == 7 || yesFire == false && y1 == 0) {
			p1.becomeKing();
		}
	}

	/**
	 * Determines if the turn can end
	 * 
	 * @return true if the turn can end
	 */
	public boolean canEndTurn() {
		if (notMoved == false) {
			if (curr != null || justBlewUp == true) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ends the current turn. Changes the player.
	 */
	public void endTurn() {
		if (firePieces == 0) {
			firePieces--;
		}
		if (waterPieces == 0) {
			waterPieces--;
		}
		StdDrawPlus.clear();
		curr = null;
		currX = -1;
		currY = -1;
		side = 1 - side;
		fireTurn = !fireTurn;
		notMoved = true;
		exploded = false;
		justBlewUp = false;
	}

	/**
	 * Returns the winner of the game
	 * 
	 * @return The winner of this game
	 */
	public String winner() {
		String winner;
		if (firePieces > -1 && waterPieces > -1) {
			winner = "Ongoing";
		} else if (firePieces == -1 && waterPieces == -1) {
			winner = "Tie";
		} else if (firePieces == -1) {
			winner = "Water";
		} else if (waterPieces == -1) {
			winner = "Fire";
		} else {
			winner = "Tie";
		}
		return winner;
	}

	/**
	 * Starts a game
	 */
	public static void main(String[] args) {
		int x, y;
		Board b = new Board(false);
		while (b.winner() == "Ongoing") {
			b.drawBoard();
			b.drawPieces(false);
			b.drawCurrentPlayer();
			StdDrawPlus.show(20);
			if (StdDrawPlus.mousePressed()) {
				x = (int) StdDrawPlus.mouseX();
				y = (int) StdDrawPlus.mouseY();
				if (b.canSelect(x, y)) {
					b.select(x, y);
				}
			}
			if (b.canEndTurn()) {
				if (StdDrawPlus.isSpacePressed()) {
					b.endTurn();
				}
			}
		}
		System.out.println(b.winner());
	}
}
