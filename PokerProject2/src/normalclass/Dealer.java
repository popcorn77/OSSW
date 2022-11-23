package normalclass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/** Dealer - 배팅금액관리, 게임덱, 카드덱을 가지고 있음 */
public class Dealer {
	
	@SuppressWarnings("unchecked")
	private ArrayList<Card>[] deck = new ArrayList[4];
	private List<Card> gameDeck = new LinkedList<Card>();
	
	private boolean allinGame;
	private int currentBetMoney;
	private int visibleBetMoney;
	private int startBet; // 학교 금액
	/**
	 * 플레이어가 참가했을 때 보유 하고있던 금액
	 */
	private int[] joinMoney = new int[5];
	/**
	 * 각 플레이어가 배팅한 금액
	 */
	private int[] betMoney = new int[5];
	private int finalBetMoney; // 총합
	
	//<Constructor>-------------------------------------------
	public Dealer() {
		for(int q = 0 ; q < deck.length ; q++){
			deck[q] = new ArrayList<Card>();
		}
		setCardDeck();
	}
	
	//<GETTER & SETTER>---------------------------------------
	public int getCurrentBetMoney() {
		return currentBetMoney;
	}
	public void setCurrentBetMoney(int currentBetMoney) {
		this.currentBetMoney = currentBetMoney;
	}
	
	
	public int getVisibleBetMoney() {
		return visibleBetMoney;
	}
	public void setVisibleBetMoney(int visibleBetMoney) {
		this.visibleBetMoney = visibleBetMoney;
	}
	
	
	public boolean isAllinGame() {
		return allinGame;
	}
	public void setAllinGame(boolean allin) {
		allinGame = allin;
	}
	
	
	public int getStartBetting() {
		return startBet;
	}
	public void setStartBetting(int money) {
		startBet = money;
	}
	
	
	public int getFinalBetMoney() {
		return finalBetMoney;
	}
	public void setFinalBetMoney() {
		int sum = 0;
		for(int q = 0 ; q < betMoney.length ; q++) {
			sum += betMoney[q];
		}
		finalBetMoney = sum;
	}
	
	
	public int[] getJoinMoney() {
		return joinMoney;
	}
	public void setJoinMoney(int index, int joinMoney) {
		this.joinMoney[index] = joinMoney;
	}
	
	
	public int[] getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(int index, int allinBetmoney) {
		betMoney[index] = allinBetmoney;
	}
	public void addBetMoney(int index, int allinBetmoney) {
		betMoney[index] += allinBetmoney;
	}
	
	
	public void setGameDeck(int playerNum) {
		shuffle();
		int spade = 0, dia = 0, heart = 0, clover = 0;
		
		while(gameDeck.size() < playerNum * 7) {
			int random = (int)(Math.random() * 4);
			
			switch(random) {
			case 0:
				if(clover < 13) {
					gameDeck.add(deck[random].get(clover));
					clover ++;
				}
				break;
			case 1:
				if(heart < 13) {
					gameDeck.add(deck[random].get(heart));
					heart ++;
				}
				break;
			case 2:
				if(dia < 13) {
					gameDeck.add(deck[random].get(dia));
					dia ++;
				}
				break;
			case 3:
				if(spade < 13) {
					gameDeck.add(deck[random].get(spade));
					spade ++;
				}
				break;
			}
		}
		for(int q = 0 ; q < deck.length ; q++)
			deck[q].clear();
		
		setCardDeck(); // 섞은후 게임덱에 담은다음에 다시 초기화
	}
	public List<Card> getGameDeck() {
		return gameDeck;
	}
	
	//<Function>----------------------------------------------
	public void setCardDeck() {//카드덱 생성 총 52장
		for(int q = deck.length ; q > 0 ; q--)
			for(int w = 0 ; w < 13 ; w++)
				deck[q - 1].add(new Card(w + 2, q));
	}
	
	public void shuffle() {
		for(int q = 0 ; q < 10 ; q++)
			for(int w = 0 ; w < deck.length ; w++)
				Collections.shuffle(deck[w]);
	}
	
	public void initD() {
		allinGame = false;
		gameDeck.clear();
		Arrays.fill(betMoney, 0);
		Arrays.fill(joinMoney, 0);
		startBet = 0;
		finalBetMoney = 0;
		currentBetMoney = 0;
		visibleBetMoney = 0;
	}
}//class