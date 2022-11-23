package normalclass;

import javax.swing.ImageIcon;

public class Card {
	private int cNum;
	private int cShape;
	private boolean fnB;
	private String primaryKey;
	private ImageIcon cardFrontImg;
	private ImageIcon cardBackImg;
	
	//<Constructor>-------------------------------------------
	public Card(int cardNum, int cardShape) {
		this.cNum = cardNum;
		this.cShape = cardShape;
		this.primaryKey = "Card" + cardNum + cardShape;
		this.cardFrontImg = new ImageIcon(Main.class.getResource("../plus_Card/"+primaryKey+".png"));
		this.cardBackImg = new ImageIcon(Main.class.getResource("../plus_Card/CardBackImg.png"));
	}
	
	//GETTER & SETTER-----------------------------------------
	public boolean getFnB() {
		return fnB;
	}
	public void setFnB(boolean fnB) {
		this.fnB = fnB;
	}

	public 	int getCNum() {
		return cNum;
	}
	
	public int getCShape() {
		return cShape;
	}
	
	public ImageIcon getCardFrontImg() {
		return cardFrontImg;
	}
	
	public ImageIcon getCardBackImg() {
		return cardBackImg;
	}
	
	public String getPrimaryKey() {
		return primaryKey;
	}
}