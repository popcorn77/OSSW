package normalclass;

import static normalclass.Main.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import g_GamePage.BettingMoneyInfo;
import g_GamePage.CardPanel;
/** 
 * GameSystem - 플레이어와 배팅정보 업데이트, 오픈된 패에 따른 선 결정, 죽은 유저, 재참여 유저 관리 
 * */
public class GameSystem {
	private ArrayList<User> dieUser = new ArrayList<User>();
	
	public void deckSort(List<Card> cardDeck) {
		
		for(int q = 0 ; q < cardDeck.size() ; q++)
			for(int w = q + 1 ; w < cardDeck.size() ; w++) {
				
				if(cardDeck.get(q).getCNum() < cardDeck.get(w).getCNum()) {
					Card tmpCard = cardDeck.get(w);
					cardDeck.remove(w);
					cardDeck.add(q, tmpCard);
				}
			}
	}
	
	public void bettingBadge(int index, ArrayList<User> turnList, ArrayList<JLabel> bettingBadge, int bettingCode){//배팅 뱃지 설정
		
		for(int q = 0 ; q < bettingBadge.size() ; q++)
			bettingBadge.get(q).setVisible(false);
		
		if(bettingCode != DIE) {
			switch(bettingCode) {
			case HALF:
				bettingBadge.get(0).setLocation(game.getBadgeLocation()[turnList.get(index).getPIndex()][0]
						, game.getBadgeLocation()[turnList.get(index).getPIndex()][1]);
				break;
			case BBING:
				bettingBadge.get(1).setLocation(game.getBadgeLocation()[turnList.get(index).getPIndex()][0]
						, game.getBadgeLocation()[turnList.get(index).getPIndex()][1]);
				break;
			case CHECK:
				bettingBadge.get(2).setLocation(game.getBadgeLocation()[turnList.get(index).getPIndex()][0]
						, game.getBadgeLocation()[turnList.get(index).getPIndex()][1]);
				break;
			case CALL:
				bettingBadge.get(3).setLocation(game.getBadgeLocation()[turnList.get(index).getPIndex()][0]
						, game.getBadgeLocation()[turnList.get(index).getPIndex()][1]);
				break;
			}
			bettingBadge.get(bettingCode - 1).setVisible(true);
		}
	}
	
	public void updatePlayerInfo(int unknown) {
		
		for(int q = 0 ; q < player.size() ; q++) {
			if(player.get(q).getUserOrAI()) {
				analyzerB.analysis(player.get(q).getMycard());
				player.get(q).setGrade(analyzerB.getTmpGrade(0), analyzerB.getTmpGrade(1));
				player.get(q).setGradeName(analyzerB.getTmpGradeName());
				player.get(q).setGradeNum(analyzerB.getTmpGradeNum());
				analyzerB.initA();
			} else {
				player.get(q).enemyArrSet(unknown);
			}
		}
	}
	
	public void updateBettingInfo(
			  int index
			, int viewBetMoney
			, ArrayList<CardPanel> CardPanel
			, BettingMoneyInfo moneyPanel
			, ArrayList<User> turnList) {
		String formatString;
		String betMoneyFormat;
		
		for(int w = 0 ; w < CardPanel.size() ; w++) {
			formatString = String.format("보유금액 %,d원", player.get(w).getMoney());
			CardPanel.get(w).resetMoney(formatString);
		}
		
		formatString = String.format("%,d원", dealer.getFinalBetMoney());
		betMoneyFormat = String.format("%,d", viewBetMoney);
		moneyPanel.updateMoneyPanel(turnList.get(index).getUserName(), betMoneyFormat, formatString);
	}
	
	public void setPlayerTurn(ArrayList<User> turnList) { //(선을 기준으로 반시계 방향)
		int index = 0;
		setFirstPlayer(player.size(), turnList);
		
		for(int q = 0 ; q < player.size() ; q++)
			if(turnList.get(0).getUserName().equals(player.get(q).getUserName()))
				index = q;
		
		for(int q = 0 ; q < player.size() - 1 ; q++) {
			index = index == player.size() - 1 ? 0 : ++index;
			turnList.add(player.get(index));
		}
	}
	
	public void setFirstPlayer(int playerNum, ArrayList<User> turnList) { //카드 나눠줄 선을 정함
		ArrayList<Card> tmpDeck = new ArrayList<Card>();
		turnList.clear();
		
		for(int q = 0 ; q < playerNum ; q++) {
			player.get(q).initU();
			
			for(int w = 0 ; w < player.get(q).getMycard().size() ; w++)
				if(player.get(q).getMycard().get(w).getFnB() && w != 6)
					tmpDeck.add(player.get(q).getMycard().get(w));
			
			analyzerB.analysis(tmpDeck);
			player.get(q).setGrade(analyzerB.getTmpGrade(0), analyzerB.getTmpGrade(1));
			player.get(q).setGradeName(analyzerB.getTmpGradeName());
			player.get(q).setGradeNum(analyzerB.getTmpGradeNum());
			analyzerB.initA();
			tmpDeck.clear();
		}
		
		for(int q = 0 ; q < playerNum ; q++) {
			int tmp = playerNum - 1;
			
			for(int w = 0 ; w < playerNum ; w++)
				if(q != w )
					if(analyzerB.comparison(player.get(q), player.get(w)))
						tmp --;
					
			if(tmp == 0) {
				turnList.add(player.get(q));
				break;
			}
			tmp = playerNum - 1;
		}
	}
	
	public void resetPlayerIndex() {//리스트가 변할 때 각 유저들의 인덱스를 그에맞게 맞춰줌
		for(int q = 0 ; q < player.size() ; q++)
			player.get(q).setFIndex(q);
	}
	
	public boolean setDieUser(ArrayList<User> turnList, ArrayList<JLabel> dieLabel, ArrayList<CardPanel> CardPanel) {

		for(int q = 0 ; q < player.size() ; q++)
			if(!player.get(q).isSurvive()) {
				
				for(int w = 0 ; w < turnList.size() ; w++) {
					if(player.get(q).getUserName().equals(turnList.get(w).getUserName())) {
						
						updateBettingInfo(w, 0, CardPanel, game.getMoneyPanel(), turnList);
						CardPanel.remove(turnList.get(w).getFIndex());
						dieLabel.get(turnList.get(w).getPIndex()).setVisible(true);
						turnList.remove(w);
						break;
					}
				}
				dieUser.add(player.get(q));
				player.remove(q);
				resetPlayerIndex();
				return true;
			}
		return false;
	}
	public ArrayList<User> getDieUser() {
		return dieUser;
	}
	
	public void continueUser() {
		for(int q = 0 ; q < dieUser.size() ; q++) // 컴퓨터는 30만원이상 있어야 재참여 가능
			if(dieUser.get(q).getMoney() > 300000 && !dieUser.get(q).getUserOrAI()) {
				player.add(dieUser.get(q));
				dieUser.get(q).getMycard().clear();
				dieUser.remove(q);
				q--;
			}
			else if(dieUser.get(q).getUserOrAI()) {
				player.add(0, dieUser.get(q));
				dieUser.get(q).getMycard().clear();
				dieUser.remove(q);
				q--;
			}
	}
}