package normalclass;

import java.util.ArrayList;
import java.util.Arrays;

import analyzer.Analyzer;

/** AnalyzerBasic - 유저의 패분석 , 요청받은 두개의 덱중 누가더 높은 등급인지 비교 */
public class AnalyzerBasic extends Analyzer {

	private String tmpGradeName;
	private int[] tmpGrade = new int[2];
	private int[] tmpGradeNum = new int[10];
	private int preSFChk;

	//<Function>----------------------------------------------
	
	/** 요청받은 패 분석 */
	public void analysis (ArrayList<Card> passDeck) {
		requestDeck.addAll(passDeck);
		setNSDeck();
		checkPoker();

		if(tmpGrade[0] != 5)
			tmpGrade[1] = setShape(tmpGradeNum[tmpGrade[0]]); 
	}
	
	/** 상대와 나의 패 비교분석 */
	public boolean comparison (User user, User cUser) {
		int grade1 = user.getGrade(0);
		int grade2 = cUser.getGrade(0);
		int gradeNum1 = user.getGradeNum(user.getGrade(0));
		int gradeNum2 = cUser.getGradeNum(cUser.getGrade(0));
		
		if(grade1 == grade2) {
			if(gradeNum1 == gradeNum2) {
				if(grade1 == 2) {
					if(user.getGradeNum(1) > cUser.getGradeNum(1)) return true;
					else return false;
				}
				else if(user.getGradeName().equals("Mountain Straight") 
						&& cUser.getGradeName().equals("Back Straight")) {
					return true;
				}
				else if(user.getGradeName().equals("Back Straight")
						&& cUser.getGradeName().equals("Mountain Straight")) {
					return false;
				}
				else {
					if(user.getGrade(1) > cUser.getGrade(1)) return true;
					else return false;
				}
			}
			else if(gradeNum1 > gradeNum2) return true;
			else return false;
		}
		else if(grade1 > grade2) return true;
		else return false;
	}

	private int setShape (int index) {
		int cnt = 0;
		int[] shapeArr = new int[4];
		
		for(int q = 0 ; q < requestDeck.size() ; q++)
			if(index == requestDeck.get(q).getCNum()) {
				shapeArr[cnt] = requestDeck.get(q).getCShape(); 
				cnt ++;
			}
		
		Arrays.sort(shapeArr);
		return shapeArr[3];
	}

	private void checkPoker () {
		Main.gameSystem.deckSort(requestDeck);
		
		straight : for(int q = 0 ; q < 10 ; q++)
		{
			if(straightCheck(numberDeck, q) >= 5 && tmpGrade[0] <= 4)
			{
				tmpGrade[0] = 4;
				switch(q){
				case 0:
					tmpGradeName = "Back Straight";
					tmpGradeNum[4] = 14;
					break straight;
				case 9:
					tmpGradeName = "Mountain Straight";
					tmpGradeNum[4] = q + 5;
					break straight;
				default:
					tmpGradeName = "Straight";
					tmpGradeNum[4] = q + 5;
					break;
				}
			}
			else if(straightCheck(numberDeck, q) == 4)
				preSFChk = 1;
		}

		for(int q = 0 ; q < shapeDeck.length ; q++)
			if(shapeDeck[q] >= 5 && tmpGrade[0] <= 5)
			{
				for(int w = 0 ; w < requestDeck.size() ; w++)
					if(requestDeck.get(w).getCShape() == q + 1)
					{
						tmpGradeNum[5] = requestDeck.get(w).getCNum();
						break;
					}
				tmpGrade[0] = 5;
				tmpGrade[1] = q + 1;
				tmpGradeName = "Flush";
				straightFlush(tmpGrade[1]);
				break;
			}

		for(int q = 1 ; q < numberDeck.length ; q++)
		{
			if(numberDeck[q] == 4)
			{
				tmpGradeNum[7] = q + 1;
				if(tmpGrade[0] <= 7)
				{
					tmpGrade[0] = 7;
					tmpGradeName = "Four Card";
				}
			}
			else if(numberDeck[q] == 3)
			{
				tmpGradeNum[3] = q + 1;
				if(tmpGrade[0] <= 3)
				{
					tmpGrade[0] = 3;
					tmpGradeName = "Triple";
				}
			}
			else if(numberDeck[q] == 2 && tmpGradeNum[1] >= 1)
			{
				if(tmpGradeNum[2] == 0)
					tmpGradeNum[2] = q + 1;

				else if(tmpGradeNum[2] >= 1)
				{
					tmpGradeNum[1] = tmpGradeNum[2];
					tmpGradeNum[2] = q + 1;
				}
				if(tmpGrade[0] <= 2)
				{
					tmpGrade[0] = 2;
					tmpGradeName = "TwoPair";
				}
			}
			else if(numberDeck[q] == 2 && tmpGradeNum[2] == 0)
			{
				tmpGradeNum[1] = q + 1;
				if(tmpGrade[0] <= 1)
				{
					tmpGrade[0] = 1;
					tmpGradeName = "OnePair";
				}
			}
			else if(numberDeck[q] == 1)
			{
				tmpGradeNum[0] = q + 1;
				if(tmpGrade[0] <= 0)
				{
					tmpGrade[0] = 0;
					tmpGradeName = "Top";
				}
			}
			if(tmpGradeNum[1] >= 1 && tmpGradeNum[3] >= 1)
			{
				tmpGradeNum[6] = tmpGradeNum[3];
				if(tmpGrade[0] <= 6)
				{
					tmpGrade[0] = 6;
					tmpGradeName = "Full House";
				}
			}
		}
	}

	private int straightCheck(int[] numberDeck, int q){
		int straightChk = 0;
		for(int w = q ; w < q + 5 ; w++)
			if(numberDeck[w] >= 1)
				straightChk ++;

		return straightChk;
	}

	private void straightFlush(int shapeNum){
		ArrayList<Card> tmpDeck = new ArrayList<Card>();
		int[] tmpNumberDeck = new int[14];

		for(int q = 0 ; q < requestDeck.size() ; q++)
			if(requestDeck.get(q).getCShape() == shapeNum)
			{
				if(requestDeck.get(q).getCNum() == 14)
					tmpNumberDeck[0] ++;

				tmpDeck.add(requestDeck.get(q));
				tmpNumberDeck[requestDeck.get(q).getCNum() - 1] ++;
			}

		stFlush : for(int q = 0 ; q < 10 ; q++)
			if(straightCheck(tmpNumberDeck, q) == 5)
			{
				switch(q){
				case 0:
					tmpGradeName = "Back Straight Flush";
					tmpGrade[0] = 8;
					tmpGradeNum[8] = 14;
					break stFlush;
				case 9:
					tmpGradeName = "Royal Straight Flush";
					tmpGrade[0] = 9;
					tmpGradeNum[9] = q + 5;
					break stFlush;
				default:
					tmpGradeName = "Straight Flush";
					tmpGrade[0] = 8;
					tmpGradeNum[8] = q + 5;
					break;
				}
			}
	}

	@Override
	public void initA(){
		preSFChk = 0;
		tmpGradeName = "";
		requestDeck.clear();
		Arrays.fill(tmpGrade, 0);
		Arrays.fill(shapeDeck, 0);
		Arrays.fill(numberDeck, 0);
		Arrays.fill(tmpGradeNum, 0);
	}

	//<ONLY GETTER>-------------------------------------------
	public int getPreStraight(){
		return preSFChk;
	}

	public int getPreFlush(){
		for(int q = 0 ; q < shapeDeck.length ; q++)
			if(shapeDeck[q] == 4)
				return 1;

		return 0;
	}

	public String getTmpGradeName(){
		return tmpGradeName;
	}

	public int getTmpGrade(int index){
		return tmpGrade[index];
	}

	public int getTmpGradeNum(int index){
		return tmpGradeNum[index];
	}

	public int[] getTmpGradeNum(){
		return tmpGradeNum;
	}
}