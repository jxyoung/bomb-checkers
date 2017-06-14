/*Piece.java*/

/**
 * Represents a Normal Piece in Checkers61bl
 * 
 * @author
 */

public class Piece {

	private String type = "pawn";
	private boolean capturing;
	private boolean isKing = false;
	protected int side;
	/**
	 * Initializes a Piece
	 * 
	 * @param side
	 *            The side of the Piece
	 * @param b
	 *            The Board the Piece is on
	 */
	Piece(int side, Board b) {
		this.side = side;
		capturing = false;
	}
	/**
	 * Returns the side that the piece is on
	 * 
	 * @return 0 if the piece is fire and 1 if the piece is water
	 */
	public int side() {
		if (side == 0) {
			return 0;
		}
		return 1;
	}

	public boolean isKing() {
		return isKing;
	}
	
	public void becomeKing() {
		isKing = true;
	}

	/**
	 * Gets type "pawn" in the Piece class
	 */
	public String getType() {
		return type;
	}
	
	public String isFire() {
		if (side == 0) {
			if (isKing()) {
				return "-fire-crowned";
			}
			return "-fire";
		} else {
			if (isKing()) {
				return "-water-crowned";
			}
		return "-water";
		}
	}
	
	public String picture() {
		return "img/" + type + isFire() + ".png";
	}
	
	/**
	 * Destroys the piece at x, y. ShieldPieces do not blow up
	 * 
	 * @param x
	 *            The x position of Piece to destroy
	 * @param y
	 *            The y position of Piece to destroy
	 */
	void getBlownUp(int x, int y) {
		// YOUR CODE HERE
	}

	/**
	 * Does nothing. For bombs, destroys pieces adjacent to it
	 * 
	 * @param x
	 *            The x position of the Piece that will explode
	 * @param y
	 *            The y position of the Piece that will explode
	 */
	void explode(int x, int y) {
		// YOUR CODE HERE
	}

	/**
	 * Signals that this Piece has begun to capture (as in it captured a Piece)
	 */
	void startCapturing() {
		capturing = true;
	}

	/**
	 * Returns whether or not this piece has captured this turn
	 * 
	 * @return true if the Piece has captured
	 */
	public boolean hasCaptured() {
		if (capturing) {
			return true;
		}
		return false;
	}

	/**
	 * Resets the Piece for future turns
	 */
	public void finishCapturing() {
		capturing = false;
	}

}