package normalclass;

import java.io.Serializable;
import java.util.StringTokenizer;
/** Account - 유저의 계정이 되는 클래스 */
public class Account implements Serializable {
	private static final long serialVersionUID = 0;
	
	private int[] scoreArr = new int[2];
	private int accumDate;
	private int chargeTicket;
	private int money;
	private String id;
	private String password;
	private String name;
	private String phNum;
	private String joinDate;
	private String lastContact;
	
	//<Constructor>-------------------------------------------
	public Account(){}
	
	public Account(String id, String password, String name, String phNum, String joinDate) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.phNum = phNum;
		this.joinDate = joinDate;
		this.scoreArr[0] = 0;
		this.scoreArr[1] = 0;
		this.money = 200000;
	}
	//<Function>----------------------------------------------
	public int charging() {
		final int base = 50000;
		int chargeMoney = base + (accumDate * 5000);
		
		chargeTicket --;
		while(chargeMoney > Main.MAX_CHARGE)
			chargeMoney --;
		
		return chargeMoney;
	}
	
	public void accum(String today) {
		int[][] tmpArr = new int[2][3];
		StringTokenizer date = new StringTokenizer(joinDate,".");
		StringTokenizer date2 = new StringTokenizer(today,".");
		
		for(int q = 0 ; date.hasMoreTokens() ; q++) {
			tmpArr[0][q] = Integer.parseInt(date.nextToken());
			tmpArr[1][q] = Integer.parseInt(date2.nextToken());
		}
		
		accumDate = (tmpArr[1][0] - tmpArr[0][0]) 
				* 365 + (tmpArr[1][1] - tmpArr[0][1])
				* 30 + (tmpArr[1][2] - tmpArr[0][2]);
	}
	
	public void chargeTicket (String today) {
		
		while(chargeTicket > 1)
			chargeTicket --;
			
		if(lastContact != null) {
			if(!lastContact.equals(today) && chargeTicket == 0)
				chargeTicket ++;
		}
		else
			chargeTicket ++;
	}
	
	//<GETTER & SETTER>---------------------------------------
	public void setLastContact(String date){
		lastContact = date;
	}
	public String getlastContact(){
		return lastContact;
	}
	
	
	public void setMoney(int money){
		this.money = money;
	}
	public int getMoney(){
		return money;
	}
	
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}

	
	public int[] getScore() {
		return scoreArr;
	}
	public void setScore(int index, int score) {
		this.scoreArr[index] = score;
	}
	public void addScore(int index, int score) {
		this.scoreArr[index] += score;
	}
	//--------------------------------ONLY GETTER
	public String getJoinDate() {
		return joinDate;
	}

	public int getAccumDate() {
		return accumDate;
	}

	public int getChargeTicket() {
		return chargeTicket;
	}
	
	public String getId(){
		return id;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getPhNum(){
		return phNum;
	}
}