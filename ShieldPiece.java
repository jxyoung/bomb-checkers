/*ShieldPiece.java*/

/**
 * Represents a ShieldPiece in Checkers61bl
 * 
 * @author
 */

public class ShieldPiece extends Piece {

	/**
	 * Define any variables associated with a ShieldPiece object here. These
	 * variables MUST be private or package private.
	 */
	private String type = "shield";
	/**
	 * Constructs a new ShieldPiece
	 * 
	 * @param side
	 *            what side this ShieldPiece is on
	 * @param b
	 *            Board that this ShieldPiece belongs to
	 */
	public ShieldPiece(int side, Board b) {
		super(side, b);
	}
	
	/**
	 * Gets type "shield" in the ShieldPiece class
	 */
	@Override
	public String getType() {
		return type;
	}

	@Override
	public String picture() {
		return "img/" + type + isFire() + ".png";
	}
}