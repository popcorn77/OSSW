package g_GamePage;

import static normalclass.Main.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import g_TitlePage.TitlePage;
import normalclass.Card;
import normalclass.User;
/** PlayGame - 실제 게임이 진행되는 클래스 */
public class PlayGame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Image screenImage;
	private Graphics screenGraphics;
	private Image background = new ImageIcon(PlayGame.class.getResource("../plus_Images/Game_BackGround.png")).getImage();
	private Image cardDeckImage = new ImageIcon(PlayGame.class.getResource("../plus_Images/Game_CardDeck.png")).getImage();
	private JLabel turnBar = new JLabel(new ImageIcon(PlayGame.class.getResource("../plus_Images/Game_TurnBar.png")));
	private JButton exitButton = new JButton("Exit");
	private JPanel userChoice = new JPanel();
	
	private ArrayList<User> turnList = new ArrayList<User>();
	private ArrayList<JLabel> allinLabel = new ArrayList<JLabel>();
	private ArrayList<JLabel> dieLabel = new ArrayList<JLabel>();
	private ArrayList<JLabel> bettingBadge = new ArrayList<JLabel>();
	private ArrayList<JButton> myButton = new ArrayList<JButton>();
	private ArrayList<CardPanel> CardPanel = new ArrayList<CardPanel>();
	private BettingMoneyInfo bettingMoneyInfo = new BettingMoneyInfo();
	private PedigreeTree pedigree = new PedigreeTree();
	private CardOpen cardOpen;
	private EndGame endGame;
	
	private boolean buttonEnable;
	private int turnEndNum;
	private int turnRotation;
	private int cardSplitNum;//총 몇장의 카드를 뿌렸는지
	private int bettingInfo; // FIRST HALF BBING CHECK
	private int bettingCode; // DIE HALF BBING CHECK CALL
	private int bettingTurn; // 배팅 턴 0,1,2
	private static final String[] MY_BUTTON_NAME = {"다이", "콜", "삥", "체크", "하프"};
	private static final int[][] CARD_PANEL_LOCATION = { {427, 520}, {855, 340}, {855, 130}, {5, 130}, {5, 340} };
	private static final int[][] BADGE_LOCATION = { {426, 488}, {855, 308}, {855, 98}, {376, 98}, {376, 308} };
	
	//Constructor---------------------------------------------------------
	public PlayGame() {
		new HashMap<String, Integer>();
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		turnBar.setBounds(0, 0, 420, 10);
		pedigree.setLocation(860, 520);
		exitButton.setBounds(1160, 660, 80, 30);
		bettingMoneyInfo.setBounds(555, 440, 170, 40);
		userChoice.setBounds(427, 660, 420, 30);
		userChoice.setLayout(new GridLayout(1,5));
		exitButton.setEnabled(false);
		turnBar.setVisible(false);
		
		for(int q = 0 ; q < MY_BUTTON_NAME.length ; q++) {
			if(q <= 3) {
				bettingBadge.add(new JLabel(new ImageIcon(PlayGame.class.getResource("../plus_Images/Game_Badge"+(q + 1)+".png"))));
				bettingBadge.get(q).setVisible(false);
				bettingBadge.get(q).setSize(50, 20);
				add(bettingBadge.get(q));
			}
			allinLabel.add(new JLabel(new ImageIcon(PlayGame.class.getResource("../plus_Images/Game_AllinUser.png"))));
			allinLabel.get(q).setBounds(BADGE_LOCATION[q][0], BADGE_LOCATION[q][1], 50, 20);
			allinLabel.get(q).setVisible(false);
			add(allinLabel.get(q));
			
			dieLabel.add(new JLabel(new ImageIcon(PlayGame.class.getResource("../plus_Images/Game_DieUser.png"))));
			dieLabel.get(q).setBounds(CARD_PANEL_LOCATION[q][0], CARD_PANEL_LOCATION[q][1], 420, 90);
			dieLabel.get(q).setVisible(false);
			add(dieLabel.get(q));
			
			myButton.add(new JButton(MY_BUTTON_NAME[q]));
			myButton.get(q).addMouseListener(new OrderButton(q));
			myButton.get(q).setEnabled(buttonEnable);
			userChoice.add(myButton.get(q));
		}

		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!user.isSurvive()) {
					join.getMemberList().get(accountIndex).addScore(1, 1);
					initializedEnd();
					title = new TitlePage();
					dispose();
					if(endGame != null)
						endGame.dispose();
				}
			}
		});
		
		add(turnBar);
		add(userChoice);
		add(exitButton);
		add(bettingMoneyInfo);
		add(pedigree);
		
		setLayout(null);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		
		setCardPanel();
		gameBasicSetting();
	}
	
	public void userButtonEnable() {
		for(int q = 0 ; q < MY_BUTTON_NAME.length ; q++){
			myButton.get(q).setEnabled(buttonEnable);
		}		
	}//유저 버튼 활성화/비활성화
	
	class OrderButton extends MouseAdapter {
		private int index;
		
		OrderButton(int index) {
			this.index = index;
		}
		@Override
		public void mouseClicked(MouseEvent e){
			
			if(buttonEnable){
				switch(index){
				case 0:
					user.die();
					exitButton.setEnabled(!user.isSurvive());
					cardOpen.getInvisibleMycard().removeAll();
					cardOpen.getInvisibleMycard().setVisible(false);
					break;
				case 1:
					user.call();
					break;
				case 2:
					user.bbing();
					break;
				case 3:
					user.check();
					break;
				case 4:
					user.half();
					break;
				}
			}
		}
	}//유저 행동 부분
	
	//<Function>----------------------------------------------
	@Override
	public void paint(Graphics g) {
		 screenImage = createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
		 screenGraphics = screenImage.getGraphics();
		 screenDraw(screenGraphics);
		 g.drawImage(screenImage, 0, 0, null);
	}
	
	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		g.drawImage(cardDeckImage, 610, 0, null);
		paintComponents(g);
		this.repaint();
	}
	/**
	 * 게임 시작시에 기본 배팅금액 및 플레이어들의 기본셋팅을 하는 메서드
	 * 유저가 가진돈에 따라서 기본 배팅금액을 100 ~ 10000원으로 설정
	 * dealer에게 각 유저의 참가금액(joinMoney)를 기억시킴
	 * 각 플레이어들이 첫번째 배팅을 하도록 하고
	 * 각 플레이어를 살아있는 유저로 설정
	 */
	public void gameBasicSetting() {
		
		if(player.get(0).getMoney() > 10000000) {
			dealer.setStartBetting(10000);
		}
		else if(player.get(0).getMoney() > 1000000) {
			dealer.setStartBetting(1000);
		}
		else {
			dealer.setStartBetting(100);
		}
		
		for(int q = 0 ; q < player.size() ; q++) {
			dealer.setJoinMoney(player.get(q).getPIndex(), player.get(q).getMoney());
			turnList.add(player.get(q));
			player.get(q).setSurvive(true);
			player.get(q).startBetting();
		}

		dealer.setFinalBetMoney();
		dealer.setGameDeck(player.size());
		cardSplit(3);
	}
	
	public void game() {
		
		game : while(bettingTurn != 3) {
			System.out.printf("*********************%n"
							+ "Start the %d betting!!%n"
							+ "*********************%n"
							, bettingTurn + 1);
			bettingInfo = FIRST;
			waitTime(player.size() * 300);
			gameSystem.setPlayerTurn(turnList);
			turnBar.setVisible(true);
			
			switch(bettingTurn) {
			case 0:
			case 1: gameSystem.updatePlayerInfo(2); break;
			case 2: gameSystem.updatePlayerInfo(3); break;
			}
			
			for(int q = 0 ; turnEndNum < turnList.size() ; q++) {
				turnEndNum ++;
				for(int w = 0 ; w < player.size() ; w++)
					System.out.println(player.get(w).getUserName()+"승률"+player.get(w).getPercent());
				
				if(cardSplitNum < 7) pedigree.resetPedigree();
				if(q == turnList.size()) {
					turnRotation ++;
					q = 0;
				}
				
				if(!turnList.get(q).isAllin()) {
					turnList.get(q).setTurn(true);
					turnBar.setLocation(turnList.get(q).getTurnBar()[0], turnList.get(q).getTurnBar()[1]);
				}
				
				while(turnList.get(q).isMyTurn()) {
					if(turnList.size() == 2 && dealer.isAllinGame()) break;
					//남은사람이 2명이고 올인게임인 경우 게임을 즉시 종료
					
					waitTime(100);
					if(turnList.get(q).getUserOrAI()) {
						buttonEnable = true;
						userButtonEnable();
					} else {
						turnList.get(q).aiBehavior(bettingTurn, bettingInfo);
					}
				}
				
				dealer.setFinalBetMoney();
				gameSystem.updateBettingInfo(q, dealer.getVisibleBetMoney(),  CardPanel, bettingMoneyInfo, turnList);
				
				if(!turnList.get(q).isAllin()) {
					if(gameSystem.setDieUser(turnList, dieLabel, CardPanel)) { 
						q --; turnEndNum --;
					}
					gameSystem.bettingBadge(q, turnList, bettingBadge, bettingCode);
				}
				
				if(player.size() == 1) {
					gameResult();
					break game;
				}
				buttonEnable = false;
				userButtonEnable();
				waitTime(500);
			}
			if(bettingTurn < 2) {
				cardSplit(1);
				bettingTurn ++;
				turnRotation = 0;
				turnEndNum = 0;
			} else {
				gameResult();
				break;
			}
		}
	}
	
	private int[] set1st() {
		int[] result = new int[player.size()];
		
		for(int q = 0 ; q < player.size() ; q++) {
			gameSystem.deckSort(player.get(q).getMycard());
			
			for(int w = 0 ; w < player.size() ; w++) {
				if(player.size() == 1) {
					result[q] ++;
				} else {
					if(analyzerB.comparison(player.get(q), player.get(w)))
						result[q] ++;
				}
			}
		}
		return result;
	}
	
	private void showResult(int index) {
		String myGrade = "";
		
		switch(player.get(index).getGrade(0)) {
		case 0:case 1:case 3:case 4:case 5:case 7:case 8:case 9:
			myGrade = changer(player.get(index).getGradeNum(player.get(index).getGrade(0)));
			break;
			
		case 2:case 6:
			myGrade = changer(player.get(index).getGradeNum(player.get(index).getGrade(0)));
			myGrade += changer(player.get(index).getGradeNum(1));
			break;
		}
		
		myGrade += " "+player.get(index).getGradeName();
		JLabel tmpLabel = new JLabel(myGrade);
		
		tmpLabel.setForeground(Color.WHITE);
		tmpLabel.setBounds(CARD_PANEL_LOCATION[player.get(index).getPIndex()][0]
				, CARD_PANEL_LOCATION[player.get(index).getPIndex()][1] - 20, 100, 20);
		game.add(tmpLabel);
	}
	
	private void overTurnCard() {//모든카드를 앞면으로 뒤집음
		for(int q = 0 ; q < player.size() ; q++) {
			
			for(int w = 0 ; w < player.get(q).getMycard().size() ; w++)
				player.get(q).getMycard().get(w).setFnB(true);
			
			for(int w = 0 ; w < CardPanel.get(q).getCardPanel().size() ; w++)
				CardPanel.get(q).getCardPanel().get(w).removeAll();
		}
	}
	
	private void concealItems() {//턴바, 뱃지 숨김
		for(int q = 0 ; q < bettingBadge.size() ; q++) {
			bettingBadge.get(q).setVisible(false);
		}
		turnBar.setVisible(false);
	}
	
	public void gameResult() {
		int[] result = set1st();
		concealItems();
		overTurnCard();
		
		for(int q = 0 ; q < player.size() ; q++) {
			int tmp = player.get(q).getGradeNum(player.get(q).getGrade(0));
			int grade = player.get(q).getGrade(0);
			int shape = player.get(q).getGrade(1);
			int[] backstArr = {14, 2, 3, 4, 5};
			int[] stArr = new int[5];
			ArrayList<Card> tmpDeck = new ArrayList<Card>();
			
			for(int w = 0 ; w < stArr.length ; w++) {
				stArr[w] = tmp;
				tmp --;
			}
			
			showResult(q);
			
			for(int w = 0 ; w < player.get(q).getMycard().size() ; w++) {
				int cardNum = player.get(q).getMycard().get(w).getCNum();
				int shapeNum = player.get(q).getMycard().get(w).getCShape();
				int gradeNum = player.get(q).getGradeNum(player.get(q).getGrade(0));
				Card card = player.get(q).getMycard().get(w);
				
				if(tmpDeck.size() < 5) {
					switch(grade){
					case 0:case 1:case 2:case 3:case 6:case 7:
						
						if(gradeNum == cardNum) {
							tmpDeck.add(card);
							player.get(q).getMycard().remove(w);
							w --;
						}
						break;
					case 4:case 8:case 9:
						
						switch(player.get(q).getGradeName()) {
						case "Back Straight":case "Back Straight Flush":
							
							for(int e = 0 ; e < backstArr.length ; e++)
								if(cardNum == backstArr[e]) {
									backstArr[e] = 0;
									tmpDeck.add(card);
									player.get(q).getMycard().remove(w);
									w --;
									break;
								}
							break;
						default:
							
							for(int e = 0 ; e < stArr.length ; e++)
								if(cardNum == stArr[e]) {
									stArr[e] = 0;
									tmpDeck.add(card);
									player.get(q).getMycard().remove(w);
									w --;
									break;
								}
							break;
						}
						break;
					case 5:
						if(shape == shapeNum) {
							tmpDeck.add(card);
							player.get(q).getMycard().remove(w);
							w --;
						}
						break;
					}
				}
			}
			
			switch(grade){
			case 2:case 6:
				
				for(int w = 0 ; w < player.get(q).getMycard().size() ; w++) {
					int onePairNum = player.get(q).getGradeNum(1);
					int cardNum = player.get(q).getMycard().get(w).getCNum();
					Card card = player.get(q).getMycard().get(w);
					
					if(onePairNum == cardNum && tmpDeck.size() < 5) {
						tmpDeck.add(card);
						player.get(q).getMycard().remove(w);
						w --;
					}
				}
				break;
			}
			
			for(int w = 14 ; tmpDeck.size() < 5 ; w--)//빈덱 메꾸기
				for(int e = 0 ; e < player.get(q).getMycard().size() ; e++) {
					int cardNum = player.get(q).getMycard().get(e).getCNum();
					Card card = player.get(q).getMycard().get(e);
					
					if(cardNum == w)
						tmpDeck.add(card);
				}
			
			cardOpen.getInvisibleMycard().setVisible(false);
			player.get(q).getMycard().clear();
			player.get(q).getMycard().addAll(tmpDeck);
			tmpDeck.clear();
		}
		
		SetCardField();
		for(int q = 0 ; q < result.length ; q++) {
			if(result[q] == player.size() - 1 && result.length > 1) {
				endGame(q);
				break;
			}
			else if(result.length == 1)
				endGame(q);
		}
	}//gameResult
	
	public String changer(int gradeNum) {
		String tmpGrade = " ";
		switch(gradeNum) {
		case 11: tmpGrade += "J"; break;
		case 12: tmpGrade += "Q"; break;
		case 13: tmpGrade += "K"; break;
		case 14: tmpGrade += "A"; break;
		default: tmpGrade += ""+gradeNum;
		}
		return tmpGrade;
	}
	
	public void endGame(int index) {
		int totalMoney = 0;
		User winner = player.get(index);
		ArrayList<User> allPlayer = new ArrayList<User>();
		allPlayer.addAll(gameSystem.getDieUser());
		allPlayer.addAll(player);
		
		//승리자가 올인유저이고 게임이 올인 게임인 경우
		if(winner.isAllin() && dealer.isAllinGame()) {
			for(int q = 0 ; q < allPlayer.size() ; q++) {
				
				//승리자가 처음에 가지고 있었던 돈보다 패배한 플레이어가 배팅한 금액이 많을경우
				if(dealer.getBetMoney()[allPlayer.get(q).getPIndex()] > dealer.getJoinMoney()[winner.getPIndex()] ) {
					
					//패배한 플레이어는 승리자가 가져갈 수 있는 금액을 제외한 나머지 금액을 돌려받는다.
					dealer.addBetMoney(allPlayer.get(q).getPIndex(), - dealer.getJoinMoney()[winner.getPIndex()]);
					allPlayer.get(q).addMoney(dealer.getBetMoney()[allPlayer.get(q).getPIndex()]);
					totalMoney += dealer.getJoinMoney()[winner.getPIndex()];
				} else {
					
					//패배한 플레이어가 걸었던 모든 금액을 승리자가 가져간다.
					totalMoney += dealer.getBetMoney()[allPlayer.get(q).getPIndex()];
					dealer.setBetMoney(allPlayer.get(q).getPIndex(), 0);
				}
			}
		} else {
			//일반적인 게임의 경우
			totalMoney = dealer.getFinalBetMoney();
		}
		
		//승패 기록
		if(winner.getUserOrAI())
			join.getMemberList().get(accountIndex).addScore(0, 1);
		else
			join.getMemberList().get(accountIndex).addScore(1, 1);
		
		winner.addMoney(totalMoney);
		endGame = new EndGame(winner.getUserName(), winner.getGrade(0), totalMoney);
	}
	
	public void startGame() {
		gameSystem.setPlayerTurn(turnList);
		gameSystem.updatePlayerInfo(2);
		cardSplit(2);
	}
	
	public void setCardPanel() { //카드패널을 플레이어수 만큼 생성
		for(int q = 0 ; q < player.size() ; q++) {
			player.get(q).setTurnBar(CARD_PANEL_LOCATION[q][0], CARD_PANEL_LOCATION[q][1] - 10);
			
			CardPanel.add(new CardPanel(player.get(q), q));
			CardPanel.get(q).setBounds(CARD_PANEL_LOCATION[q][0]
					, CARD_PANEL_LOCATION[q][1]
					, 420
					, 120);
			add(CardPanel.get(q));
		}
	}
	
	public void cardSplit(int again) { // 카드를 나눠주는 메서드
		int splitAgainNum = again;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int q = 0 ; q < splitAgainNum; q++) {
					cardSplitNum++;
					for(int w = 0 ; w < turnList.size() ; w++) {
						game.cardSplitMotion(turnList.get(w).getPIndex());
						turnList.get(w).setMyCard(dealer.getGameDeck().get(0));
						
						if(cardSplitNum >= 4 && cardSplitNum < 7)
							turnList.get(w).getMycard().get(cardSplitNum - 1).setFnB(true);
						
						SetCardField();
						dealer.getGameDeck().remove(0);
						waitTime(100);
					}
				}
				switch(cardSplitNum) {
				case 3:
					Timer timer = new Timer();
					TimerTask timertask = new TimerTask() {
						@Override
						public void run() {
							cardOpen = new CardOpen(user);
						}
					};
					timer.schedule(timertask, 1000);
					break;
				case 5:
					userButtonEnable();
					game();
					break;
				}
			}
		}).start();
	}
	
	public void cardSplitMotion(int w) { //카드 나눠줄때 애니메이션
		int[] currentXY = new int[2];
		int[][] destination = { {608, 400},{870, 350},{770, 135},{450, 135},{350, 350} };
		
		JLabel moveCard = new JLabel(new ImageIcon(PlayGame.class.getResource("../plus_Card/CardBackImg.png")));
		moveCard.setBounds(615, 60, 60, 82);
		currentXY[0] = moveCard.getX();
		currentXY[1] = moveCard.getY();
		
		game.add(moveCard);
		int delay = 0;
		while(destination[w][0] != moveCard.getX() || destination[w][1] != moveCard.getY()) {
			
			if(destination[w][0] > moveCard.getX()) {
				currentXY[0] ++;
			}
			else if(destination[w][0] < moveCard.getX()) {
				currentXY[0] --;
			}
			
			if(destination[w][1] > moveCard.getY()) {
				currentXY[1] ++;
			}
			else if(destination[w][1] < moveCard.getY()) {
				currentXY[1] --;
			}
			
			moveCard.setLocation(currentXY[0], currentXY[1]);
			delay ++;
			if(delay == splitSpeed) {
				waitTime(1);
				delay = 0;
			}
		}
		game.remove(moveCard);
	}
	
	public void SetCardField() { //카드를 화면에 올려주는 메서드
		for(int q = 0 ; q < turnList.size() ; q++)
			for(int w = 0 ; w < turnList.get(q).getMycard().size() ; w++) {
				//앞면인 카드는 앞면으로 뒷면인 카드는 뒷면으로 화면에 올려줌
				if(turnList.get(q).getMycard().get(w).getFnB())
					CardPanel.get(turnList.get(q).getFIndex()).getCardPanel().get(w).add(new JLabel(turnList.get(q).getMycard().get(w).getCardFrontImg()));
				else
					CardPanel.get(turnList.get(q).getFIndex()).getCardPanel().get(w).add(new JLabel(turnList.get(q).getMycard().get(w).getCardBackImg()));
			}
	}
	
	public void cardSort() { //카드 3장을 받았을 때 오픈한 카드를 3번째로 나머지 두장을 1,2번째로 정렬해주는 메서드
		
		for(int q = 0 ; q < player.size() ; q++) {
			int cardSize = player.get(q).getMycard().size();
			
			for(int w = 0 ; w < cardSize ; w++)
				if(player.get(q).getMycard().get(w).getFnB()) {
					player.get(q).getMycard().add(player.get(q).getMycard().get(w));
					player.get(q).getMycard().remove(w);
					break;
				}
		}
	}
	
	/** 게임 끝낼시에 초기화*/
	public void initializedEnd() {
		player.clear();
		
		user.initU();
		user.getMycard().clear();
		player.add(user);
		gameSystem.getDieUser().clear();
		
		bettingTurn = 0;
		turnList.clear();
		CardPanel.clear();
		title.getAILevel().clear();
		dealer.initD();
	}
	
	/** 게임 재시작시 초기화*/
	public void initializedReplay() {
		
		for(int q = 0 ; q < player.size() ; q++) {
			player.get(q).initU();
			player.get(q).getMycard().clear();
		}
		
		bettingTurn = 0;
		turnList.clear();
		CardPanel.clear();
		title.getAILevel().clear();
		dealer.initD();
	}
	
	//-----------------------------------------GETTER & SETTER
	public int getBettingInfo() {
		return bettingInfo;
	}
	public void setBettingInfo(int info) {
		bettingInfo = info;
	}
	
	
	public int getTurnEnd() {
		return turnEndNum;
	}
	public void setTurnEnd(int turnEndNum) {
		this.turnEndNum = turnEndNum;
	}
	
	
	public int getBettingCode() {
		return bettingCode;
	}
	public void setBettingCode(int code) {
		bettingCode = code;
	}
	
	
	//<ONLY GETTER>-------------------------------------------
	public int getCardSplitNum() {
		return cardSplitNum;
	}
	
	public ArrayList<JLabel> getAllinLabel() {
		return allinLabel;
	}
	
	public int[][] getBadgeLocation() {
		return BADGE_LOCATION;
	}
	
	public int getBettingTurn() {
		return bettingTurn;
	}
	
	public int getRotation() {
		return turnRotation;
	}
		
	public PedigreeTree getPedigree() {
		return pedigree;
	}
	
	public BettingMoneyInfo getMoneyPanel() {
		return bettingMoneyInfo;
	}
	
	public ArrayList<User> getTurnList() {
		return turnList;
	}
	
	public ArrayList<CardPanel> getCardPanel() {
		return CardPanel;
	}
}