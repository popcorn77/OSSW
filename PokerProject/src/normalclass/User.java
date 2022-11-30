package normalclass;

import static normalclass.Main.*;

import java.util.ArrayList;
import java.util.Arrays;

public class User implements UserIF {
	/**
	 * 조회한 플레이어가 유저인지 AI인지 판단하기 위한 변수
	 */
	private boolean userOrAI;
	/**
	 * 게임내에서 현재 살았는지 죽었는지 알기위한 변수
	 */
	private boolean survive;
	/**
	 * 현재 자신의 턴인지 알기위한 변수
	 */
	private boolean myTurn;
	/**
	 * 올인 상태를 나타내는 변수
	 */
	private boolean allin;
	/**
	 * 나의 이전 배팅이 콜인지 여부
	 */
	private boolean call;
	/**
	 * 플레이어 이름
	 */
	private String userName;
	/**
	 * 플레이어의 카드패 등급 이름
	 */
	private String gradeName;
	/**
	 * 유저의 보유금액
	 */
	private int userMoney;
	/**
	 * 게임을 플레이하면서 플레이어들이 죽을 때 그에따라 유동적으로 변하는 인덱스
	 */
	private int flexibleIndex;
	/**
	 * 플레이어 자리등을 판별할 때 사용하는 변하지않는 플레이어의 고유 인덱스
	 */
	private int primaryIndex;
	/**
	 * 플레이어의 카드패 정보를 저장하는 배열들
	 */
	private int[] grade = new int[2];
	private int[] gradeNum = new int[11];
	/**
	 * 턴을 알려주는 바의 포지션을 저장하기위한 배열
	 */
	private int[] turnBar = new int[2];
	/**
	 * 게임을 할때 받는 7장의 카드 리스트
	 */
	private ArrayList<Card> myCard = new ArrayList<Card>(7);
	
	//<Constructor>-------------------------------------------
	public User(){}
	
	public User(String userName, int money, boolean userOrAI) {
		this.userName = userName;
		this.userMoney = money;
		this.userOrAI = userOrAI;
	}
	
	//<Function>----------------------------------------------
	
	public void enemyArrSet(int unknown) {}
	public void aiBehavior(int gameTurn, int bettingInfo) {}
	public void addMemory(Card card) {}
	public int getAILevel() {return 0;}
	public float getPercent() {return 0;}
	public ArrayList<Card> getCardMemory() {return null;}
	
	public void startBetting() { //학교 금액
		userMoney -= dealer.getStartBetting();
		dealer.addBetMoney(primaryIndex, dealer.getStartBetting());
	}
	
	@Override
	public void half() {
		if(!call) {
			int bettingMoney = dealer.getFinalBetMoney();

			switch(game.getBettingInfo()) {
			case FIRST:
			case CHECK:
				bettingMoney = bettingMoney / 2;
				bettingMoney = Math.round(bettingMoney / 10) * 10;

				if(userMoney > bettingMoney) {
					dealer.addBetMoney(primaryIndex, bettingMoney);
					userMoney -= bettingMoney;
					dealer.setCurrentBetMoney(bettingMoney);
					dealer.setVisibleBetMoney(bettingMoney);
				} else {
					dealer.addBetMoney(primaryIndex, userMoney);
					dealer.setCurrentBetMoney(userMoney);
					dealer.setVisibleBetMoney(userMoney);
					userMoney = 0;
					allin = true;
					dealer.setAllinGame(true);
					game.getAllinLabel().get(primaryIndex).setVisible(true);
				}
				break;
			case HALF:
			case BBING:
				bettingMoney = dealer.getCurrentBetMoney() + (dealer.getCurrentBetMoney() + bettingMoney) / 2 ;
				bettingMoney = Math.round(bettingMoney / 10) * 10;

				if(userMoney > bettingMoney) {
					dealer.addBetMoney(primaryIndex, bettingMoney);
					dealer.setCurrentBetMoney(bettingMoney);
					dealer.setVisibleBetMoney(bettingMoney);
					userMoney -= bettingMoney;
				} else {
					dealer.addBetMoney(primaryIndex, userMoney);
					dealer.setCurrentBetMoney(userMoney);
					dealer.setVisibleBetMoney(userMoney);
					userMoney = 0;
					allin = true;
					dealer.setAllinGame(true);
					game.getAllinLabel().get(primaryIndex).setVisible(true);
				}
				break;
			}

			if(game.getTurnEnd() >= 1)
				game.setTurnEnd(1);

			game.setBettingCode(HALF);
			game.setBettingInfo(HALF);
			call = false;
			myTurn = false;
		}
	}
	
	@Override
	public void bbing() {
		if(flexibleIndex == game.getTurnList().get(0).getFIndex() && game.getRotation() == 0) {
			if(userMoney > dealer.getStartBetting()) {
				dealer.addBetMoney(primaryIndex, dealer.getStartBetting());
				userMoney -= dealer.getStartBetting();
				dealer.setCurrentBetMoney(dealer.getStartBetting());
				dealer.setVisibleBetMoney(dealer.getStartBetting());
			} else {
				dealer.addBetMoney(primaryIndex, userMoney);
				dealer.setCurrentBetMoney(userMoney);
				dealer.setVisibleBetMoney(userMoney);
				userMoney = 0;
				allin = true;
				dealer.setAllinGame(true);
				game.getAllinLabel().get(primaryIndex).setVisible(true);
			}

			game.setBettingCode(BBING);
			game.setBettingInfo(BBING);
			call = false;
			myTurn = false;
		}
	}
	
	@Override
	public void check() {
		if(flexibleIndex == game.getTurnList().get(0).getFIndex()) {
			dealer.setCurrentBetMoney(0);
			dealer.setVisibleBetMoney(0);
			game.setBettingCode(CHECK);
			game.setBettingInfo(CHECK);
			call = false;
			myTurn = false;
		}
	}

	@Override
	public void call() {
		if(flexibleIndex != game.getTurnList().get(0).getFIndex() || game.getRotation() != 0) {
			if(userMoney > dealer.getCurrentBetMoney()) {
				dealer.addBetMoney(primaryIndex, dealer.getCurrentBetMoney());
				dealer.setVisibleBetMoney(dealer.getCurrentBetMoney());
				userMoney -= dealer.getCurrentBetMoney();
			} else {
				dealer.addBetMoney(primaryIndex, userMoney);
				dealer.setVisibleBetMoney(userMoney);
				userMoney = 0;
				allin = true;
				dealer.setAllinGame(true);
				game.getAllinLabel().get(primaryIndex).setVisible(true);
			}
			game.setBettingCode(CALL);
			call = true;
			myTurn = false;
		}
	}

	@Override
	public void die() {
		game.setBettingCode(DIE);
		game.getCardPanel().get(flexibleIndex).setVisible(false);
		myTurn = false;
		survive = false;
	}
	@Override
	public void initU() {
		call = false;
		gradeName = "";
		Arrays.fill(grade, 0);
		Arrays.fill(gradeNum, 0);
	}
	
	//<GETTER & SETTER>---------------------------------------
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getGradeName() {
		return gradeName;
	}

	
	public void setGrade(int grade, int shape){
		this.grade[0] = grade;
		this.grade[1] = shape;
	}
	public int getGrade(int index){
		return grade[index];
	}
	
	
	public void setGradeNum(int[] tmpGradeNum){
		this.gradeNum = Arrays.copyOf(tmpGradeNum, tmpGradeNum.length);
	}
	public int getGradeNum(int index){
		return gradeNum[index];
	}
	public int[] getGradeNum(){
		return gradeNum;
	}
	
	
	public void setMyCard(Card card){
		myCard.add(card);
	}
	public ArrayList<Card> getMycard(){
		return myCard;
	}
	
	
	public void setSurvive(boolean survive) {
		this.survive = survive;
	}
	public boolean isSurvive() {
		return survive;
	}
	
	
	public void setallin(boolean allin) {
		this.allin = allin;
	}
	public boolean isAllin() {
		return allin;
	}
	
	
	public void setTurn(boolean turn) {
		this.myTurn = turn;
	}
	public boolean isMyTurn() {
		return myTurn;
	}
	
	
	public void addMoney(int money) {
		userMoney += money;
	}
	public void setMoney(int money) {
		this.userMoney = money;
	}
	public int getMoney() {
		return userMoney;
	}
	
	
	public void setFIndex(int index) {
		this.flexibleIndex = index;
	}
	public int getFIndex() {
		return flexibleIndex;
	}
	
	
	public void setPIndex(int index) {
		primaryIndex = index;
	}
	public int getPIndex() {
		return primaryIndex;
	}
	
	
	public void setTurnBar(int x, int y) {
		turnBar[0] = x;
		turnBar[1] = y;
	}
	public int[] getTurnBar() {
		return turnBar;
	}
	
	
	public boolean getUserOrAI() {
		return userOrAI;
	}
	
	public String getUserName() {
		return userName;
	}
	
}
