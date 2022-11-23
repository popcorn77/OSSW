package normalclass;

import java.util.ArrayList;
import java.util.Arrays;

/** Enemy - 인공지능이 적의정보를 저장하기 위한 클래스 */
public class Enemy extends User {
	private ArrayList<Card> visibleDeck = new ArrayList<Card>();
	private float[] predictArr = new float[9];
	private String userName;
	private int preStraight;
	private int preFlush;
	
	//<Constructor>-------------------------------------------
	public Enemy(int grade, int shape, int[] gradeNum, ArrayList<Card> deck, String name, String gName) {
		setGrade(grade, shape);
		setGradeNum(gradeNum);
		this.visibleDeck.addAll(deck);
		this.userName = name;
		this.setGradeName(gName);
	}//레벨1,3
	
	public Enemy(int grade, int shape, int[] gradeNum, int preStraight, int preFlush, ArrayList<Card> deck, String name, String gName) {
		setGrade(grade, shape);
		setGradeNum(gradeNum);
		this.preStraight = preStraight;
		this.preFlush = preFlush;
		this.visibleDeck.addAll(deck);
		this.userName = name;
		this.setGradeName(gName);
	}//레벨2
	
	//<ONLY GETTER>-------------------------------------------
	public int getPreStraight(){
		return preStraight;
	}
	
	public int getPreFlush(){
		return preFlush;
	}
	
	public ArrayList<Card> getVisibleDeck(){
		return visibleDeck;
	}
	
	@Override
	public String getUserName(){
		return userName;
	}
	
	public float[] getPredictEnemy(){
		return predictArr;
	}
	public void setPredictEnemy(float[] predictArr){
		this.predictArr = Arrays.copyOf(predictArr, predictArr.length);
	}
}
