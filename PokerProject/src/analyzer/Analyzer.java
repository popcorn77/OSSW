package analyzer;

import java.util.ArrayList;
import java.util.List;

import normalclass.Card;
/**
 * 분석기를 구현하기 위한 추상클래스
 * 분석기에 필요한 공통 변수및 메서드를 protected로 정의해 놓았다.
 * 분석기에 공통으로 필요한 초기화 메서드를 추상메서드로 정의해 놓았다.
 * @author ASUS
 */
abstract public class Analyzer {

	protected List<Card> requestDeck = new ArrayList<Card>();
	protected int[] numberDeck = new int[14];
	protected int[] shapeDeck = new int[4];
	
	protected void setNSDeck() {
		for(int q = 0 ; q < requestDeck.size() ; q++) {
			if(requestDeck.get(q).getCNum() == 14)
				numberDeck[0] ++;

			numberDeck[requestDeck.get(q).getCNum() - 1] ++;
			shapeDeck[requestDeck.get(q).getCShape() - 1] ++;
		}
	}
	
	abstract public void initA();
}
