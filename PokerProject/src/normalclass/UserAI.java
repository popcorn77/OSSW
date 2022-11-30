package normalclass;

import static normalclass.Main.analyzerB;
import static normalclass.Main.analyzerPE;
import static normalclass.Main.game;
import static normalclass.Main.gameSystem;
import static normalclass.Main.player;
import static normalclass.Main.waitTime;

import java.text.ChoiceFormat;
import java.util.ArrayList;

public class UserAI extends User {
	private ArrayList<Card> cardMemory = new ArrayList<Card>();
	private ArrayList<Card> enemyDeck = new ArrayList<Card>();
	private ArrayList<Enemy> enemyInfo = new ArrayList<Enemy>();
	private int aiLevel;
	private float percentage;

	//<Constructor>-------------------------------------------
	public UserAI(int money, int aiLevel, int FIndex, int PIndex, boolean userOrAI, String aiName){
		super(aiName, money, userOrAI);
		this.aiLevel = aiLevel;
		setFIndex(FIndex);
		setPIndex(PIndex);
	}

	//<Function>----------------------------------------------
	@Override
	public void enemyArrSet(int unknown) { //적의 정보를 enemyArr에 저장
		enemyInfo.clear();
		cardMemory.clear();

		for(int q = 0 ; q < gameSystem.getDieUser().size() ; q++) {
			for(int w = 0 ; w < gameSystem.getDieUser().get(q).getMycard().size() ; w++) {
				if(gameSystem.getDieUser().get(q).getMycard().get(w).getFnB()) {
					cardMemory.add(gameSystem.getDieUser().get(q).getMycard().get(w));
				}
			}
		}
		for(int q = 0 ; q < player.size() ; q++)
			if(getFIndex() != q) {
				for(int w = 0 ; w < player.get(q).getMycard().size() ; w++)
					if(player.get(q).getMycard().get(w).getFnB() && w != 6) {
						enemyDeck.add(player.get(q).getMycard().get(w));
						cardMemory.add(player.get(q).getMycard().get(w));
					}
				
				analyzerB.analysis(enemyDeck);
				switch(aiLevel) {
				case 1:case 3:
					enemyInfo.add(new Enemy(analyzerB.getTmpGrade(0)
							, analyzerB.getTmpGrade(1)
							, analyzerB.getTmpGradeNum()
							, enemyDeck
							, player.get(q).getUserName()
							, analyzerB.getTmpGradeName()));
					break;
				case 2:
					enemyInfo.add(new Enemy(analyzerB.getTmpGrade(0)
							, analyzerB.getTmpGrade(1)
							, analyzerB.getTmpGradeNum()
							, analyzerB.getPreStraight()
							, analyzerB.getPreFlush()
							, enemyDeck
							, player.get(q).getUserName()
							, analyzerB.getTmpGradeName()));
					break;
				}
				analyzerB.initA();
				enemyDeck.clear();
			} else {
				cardMemory.addAll(getMycard());
				analyzerB.analysis(getMycard());
				setGrade(analyzerB.getTmpGrade(0), analyzerB.getTmpGrade(1));
				setGradeName(analyzerB.getTmpGradeName());
				setGradeNum(analyzerB.getTmpGradeNum());
				analyzerB.initA();
			}
		
		if(aiLevel == 3 && game.getCardSplitNum() >= 5) {
			for(int q = 0 ; q < enemyInfo.size() ; q++) {
				enemyInfo.get(q).setPredictEnemy(
						analyzerPE.analysis(enemyInfo.get(q).getVisibleDeck()
						, this
						, enemyInfo.get(q).getUserName()
						, unknown));
			}
				
		}
		percentage = setPercent(game.getBettingTurn());
	}

	private float setPercent(int bettingTurn) { //AI가 이길 수 있는 확률 설정
		//기본적으로 52 + 내 등급 * 4 가 승률로 지정된다.
		float per = 52 + (getGrade(0) * 4);
		
		//내가 마지막 턴이 오기전에 플러쉬나 스트레이트가 완성 직전인 경우(4플, 4스) 승률 +3%
		if((analyzerB.getPreFlush() == 1 || analyzerB.getPreStraight() == 1) 
				&& bettingTurn <= 1)
			per += 3 ;
		
		//내 패가 J이상일 경우 추가 퍼센트
		switch(getGradeNum(getGrade(0))) {
		case 14: per += 2; break;
		case 13: per += 1.5; break;
		case 12: per += 1; break;
		case 11: per += 0.5; break;
		}
		
		//내 등급이 풀 하우스 이상일 경우 추가 퍼센트  
		switch(getGrade(0)) {
		case 6: per += 2; break;
		case 7: per += 5; break;
		case 8: per += 7; break;
		case 9: per += 8; break;
		}

		for(int q = 0 ; q < enemyInfo.size() ; q++) {
			float tmp = 0;
			if(analyzerB.comparison(this, enemyInfo.get(q))) {
				//내가 상대보다 높을 때
				tmp = setPercent2(enemyInfo.get(q), 1.6f / player.size());
				per -= tmp >= per ? per : tmp;
			} else {
				//내가 상대보다 낮을 때
				tmp = setPercent2(enemyInfo.get(q), 0.3f / player.size());
				per -= tmp >= per ? per : tmp;
				
				switch(enemyInfo.get(q).getGrade(0) - getGrade(0)) {
				case 0: per -= 1; break;
				case 1: per -= 2; break;
				case 2: per -= 3; break;
				case 3: per -= 4; break;
				case 4: per -= 5; break;
				case 5: per -= 6; break;
				case 6: per -= 7; break;
				case 7: per -= 8; break;
				case 8: per -= 9; break;
				case 9: per -= 10;break;
				}
			}

			if(enemyInfo.get(q).getPreFlush() == 1 && getGrade(0) < 5)
				per -= 2;
			if(enemyInfo.get(q).getPreStraight() == 1 && getGrade(0) < 4)
				per -= 2;
		}
		analyzerB.initA();
		return per;
	}
	
	private float setPercent2(Enemy cUser, float x) {
		if(aiLevel == 3 && game.getCardSplitNum() >= 5) {
			float[] weight = {30, 15, 10, 6, 5, 3, 1, 0.1f, 0.05f};
			float per = 0;

			for(int q = getGrade(0) ; q < weight.length ; q++)
				per += cUser.getPredictEnemy()[q] / weight[q];
			
			String tmp = x == (float)(1.6 / player.size()) ? "1.6" : "0.3";
			System.out.println("내 등급 : "+this.getGradeName()+" 상대 보이는 패 : "+cUser.getGradeName());
			System.out.println(tmp+" "+cUser.getUserName()+"에 대한 가중치 : "+per * x);
			return per * x;
		}
		else
			return 0;
	}

	//<GETTER & SETTER>---------------------------------------
	@Override
	public int getAILevel() {
		return aiLevel;
	}
	@Override
	public float getPercent() {
		return percentage;
	}

	@Override
	public ArrayList<Card> getCardMemory() {
		return cardMemory;
	}
	
	@Override
	public void addMemory(Card card) {
		cardMemory.add(card);
	}

	/**
	 * @param a HALF
	 * @param b BBING
	 * @param c CHECK
	 * @param d CALL
	 * @param e DIE
	 */
	private void playerAction(int a, int b, int c, int d, int e) {
		int order = (int)(Math.random() * 10) + 1;
		
		int section1 = a;
		int section2 = a + b;
		int section3 = a + b + c;
		int section4 = a + b + c + d;
		int section5 = a + b + c + d + e;
		
		if(order <= section1) {
			half();
		}
		else if (order > section1 && order <= section2) {
			bbing();
		}
		else if (order > section2 && order <= section3) {
			check();
		}
		else if (order > section3 && order <= section4) {
			call();
		}
		else if (order > section4 && order <= section5) {
			die();
		}
	}
	
	/**행동코드 -> (예: 01A -> 첫번째 턴의 하프패 자기승률 80% 이상)
	1. 턴 : 0 1 2
	2. 배팅정보 0:선  1:하프  2:삥  3:체크
	3. 퍼센트 : A(>80) B(70~79) C(65~69) D(60~64) E(57~59) F(53~56) G(50~52) H(45~49) I(40~44) J(39>=)*/
	
	@Override
	public void aiBehavior(int bettingTurn, int bettingInfo) {
		String pattern = "0#J|40#I|45#H|50#G|53#F|57#E|60#D|65#C|70#B|80#A";
		ChoiceFormat cf = new ChoiceFormat(pattern);
		
		String behaviorCode = "" + bettingTurn + bettingInfo;
		String percentCode = cf.format(percentage);

		/*if(percentage >= 80)
			percentCode = "A";

		else if(percentage >= 70 && percentage < 80)
			percentCode = "B";

		else if(percentage >= 65 && percentage < 70)
			percentCode = "C";

		else if(percentage >= 60 && percentage < 65)
			percentCode = "D";

		else if(percentage >= 57 && percentage < 60)
			percentCode = "E";

		else if(percentage >= 53 && percentage < 57)
			percentCode = "F";

		else if(percentage >= 50 && percentage < 53)
			percentCode = "G";

		else if(percentage >= 45 && percentage < 50)
			percentCode = "H";

		else if(percentage >= 40 && percentage < 45)
			percentCode = "I";

		else
			percentCode = "J";*/

		behaviorCode += percentCode;
		waitTime(1000);
		System.out.println(this.getUserName()+"행동코드 : "+behaviorCode);
		
		switch(behaviorCode) {
		case "00A":
			playerAction(3, 2, 5, 0, 0);
			break;
			
		case "00B":case "00C":case "00D":
			playerAction(2, 4, 4, 0, 0);
			break;
			
		case "00E":
			playerAction(2, 3, 5, 0, 0);
			break;
			
		case "00F":
			playerAction(1, 3, 6, 0, 0);
			break;
			
		case "00G":
			playerAction(1, 4, 5, 0, 0);
			break;
			
		case "00H":
			playerAction(1, 3, 6, 0, 0);
			break;
			
		case "00I":
			playerAction(0, 4, 6, 0, 0);
			break;
			
		case "00J":
			playerAction(0, 3, 7, 0, 0);
			break;
			
		case "01A":
			playerAction(4, 0, 0, 6, 0);
			break;
			
		case "01B":
			playerAction(3, 0, 0, 7, 0);
			break;
			
		case "01C":case "01D":
			playerAction(2, 0, 0, 8, 0);
			break;
			
		case "01E":case "01F":
			playerAction(1, 0, 0, 9, 0);
			break;
			
		case "01G":case "01H":
			playerAction(0, 0, 0, 10, 0);
			break;
			
		case "01I":case "01J":
			playerAction(0, 0, 0, 9, 1);
			break;
			
		case "02A":
			playerAction(3, 0, 0, 7, 0);
			break;
			
		case "02B":
			playerAction(2, 0, 0, 8, 0);
			break;
			
		case "02C":case "02D":case "02E":case "02F":
			playerAction(1, 0, 0, 9, 0);
			break;
			
		case "02G":case "02H":case "02I":case "02J":
			playerAction(0, 0, 0, 10, 0);
			break;
			
		case "03A":
			playerAction(3, 2, 0, 5, 0);
			break;
			
		case "03B":case "03C":case "03D":
			playerAction(2, 4, 0, 4, 0);
			break;
			
		case "03E":
			playerAction(2, 3, 0, 5, 0);
			break;
			
		case "03F":case "03G":
			playerAction(1, 4, 0, 5, 0);
			break;
			
		case "03H":
			playerAction(1, 3, 0, 6, 0);
			break;
			
		case "03I":case "03J":
			playerAction(0, 4, 0, 6, 0);
			break;	
		case "10A":
			playerAction(4, 3, 3, 0, 0);
			break;
			
		case "10B":
			playerAction(3, 4, 3, 0, 0);
			break;
			
		case "10C":case "10D":
			playerAction(3, 3, 4, 0, 0);
			break;
			
		case "10E":
			playerAction(2, 5, 3, 0, 0);
			break;
			
		case "10F":
			playerAction(2, 3, 5, 0, 0);
			break;
			
		case "10G":
			playerAction(0, 5, 5, 0, 0);
			break;
			
		case "10H":
			playerAction(0, 4, 6, 0, 0);
			break;
			
		case "10I":case "10J":
			playerAction(0, 3, 7, 0, 0);
			break;
			
		case "11A":
			playerAction(5, 0 ,0 ,5, 0);
			break;
			
		case "11B":case "11C":
			playerAction(3, 0, 0, 7, 0);
			break;
			
		case "11D":
			playerAction(2, 0, 0, 8, 0);
			break;
			
		case "11E":case "11F":
			playerAction(1, 0, 0, 8, 1);
			break;
			
		case "11G":case "11H":
			playerAction(0, 0, 0, 8, 2);
			break;
			
		case "11I":case "11J":
			playerAction(0, 0, 0, 7, 3);
			break;
			
		case "12A":
			playerAction(5, 0, 0, 5, 0);
			break;
			
		case "12B":case "12C":
			playerAction(3, 0, 0, 7, 0);
			break;
			
		case "12D":case "12E":case "12F":
			playerAction(2, 0, 0, 8, 0);
			break;
			
		case "12G":
			playerAction(1, 0, 0, 8, 1);
			break;
			
		case "12H":
			playerAction(0, 0, 0, 8, 2);
			break;
			
		case "12I":case "12J":
			playerAction(0, 0, 0, 7, 3);
			break;
			
		case "13A":
			playerAction(6, 0, 0, 4, 0);
			break;
			
		case "13B":case "13C":
			playerAction(5, 0, 0, 5, 0);
			break;
			
		case "13D":
			playerAction(4, 0, 0, 6, 0);
			break;
			
		case "13E":
			playerAction(3, 0, 0, 7, 0);
			break;
			
		case "13F":
			playerAction(2, 0, 0, 8, 0);
			break;
			
		case "13G":
			playerAction(1, 0, 0, 9, 0);
			break;
			
		case "13H":case "13I":case "13J":
			playerAction(0, 0, 0, 10, 0);
			break;
		case "20A":
			playerAction(7, 0, 3, 0, 0);
			break;
			
		case "20B":
			playerAction(7, 1, 2, 0, 0);
			break;
			
		case "20C":
			playerAction(6, 2, 2, 0, 0);
			break;
			
		case "20D":
			playerAction(5, 3, 2, 0, 0);
			break;
			
		case "20E":
			playerAction(3, 3, 4, 0, 0);
			break;
			
		case "20F":
			playerAction(2, 4, 4, 0, 0);
			break;
			
		case "20G":
			playerAction(1, 4, 5, 0, 0);
			break;	
			
		case "20H":
			playerAction(1, 2, 7, 0, 0);
			break;
			
		case "20I":
			playerAction(1, 1, 8, 0, 0);
			break;
			
		case "20J":
			playerAction(1, 1, 7, 0, 1);
			break;
			
		case "21A":
			playerAction(6, 0, 0, 4, 0);
			break;
			
		case "21B":
			playerAction(4, 0, 0, 6, 0);
			break;
			
		case "21C":case "21D":
			playerAction(3, 0, 0, 7, 0);
			break;
			
		case "21E":
			playerAction(2, 0, 0, 8, 0);
			break;
			
		case "21F":
			playerAction(2, 0, 0, 7, 1);
			break;
			
		case "21G":
			playerAction(1, 0, 0, 6, 3);
			break;
			
		case "21H":
			playerAction(1, 0, 0, 1, 8);
			break;
			
		case "21I":case "21J":
			playerAction(1, 0, 0, 0, 9);
			break;
			
		case "22A":
			playerAction(7, 0, 0, 3, 0);
			break;
			
		case "22B":
			playerAction(5, 0, 0, 5, 0);
			break;
			
		case "22C":
			playerAction(4, 0, 0, 6, 0);
			break;
			
		case "22D":case "22E":
			playerAction(3, 0, 0, 7, 0);
			break;
			
		case "22F":case "22G":
			playerAction(2, 0, 0, 7, 1);
			break;
			
		case "22H":
			playerAction(1, 0, 0, 7, 2);
			break;
			
		case "22I":
			playerAction(1, 0, 0, 6, 3);
			break;
			
		case "22J":
			playerAction(1, 0, 0, 5, 4);
			break;
			
		case "23A":
			playerAction(9, 0, 0, 1, 0);
			break;
		
		case "23B":
			playerAction(8, 0, 0, 2, 0);
			break;
			
		case "23C":
			playerAction(7, 0, 0, 3, 0);
			break;
			
		case "23D":
			playerAction(6, 0, 0, 4, 0);
			break;
			
		case "23E":
			playerAction(4, 0, 0, 6, 0);
			break;
			
		case "23F":
			playerAction(3, 0, 0, 7, 0);
			break;
			
		case "23G":case "23H":
			playerAction(2, 0, 0, 8, 0);
			break;
			
		case "23I":case "23J":
			playerAction(1, 0, 0, 9, 0);
			break;
		}
	}
}