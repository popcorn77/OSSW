package g_GamePage;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;


public class BettingMoneyInfo extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JLabel turn = new JLabel("", SwingConstants.CENTER);
	private JLabel bettingMoney = new JLabel("", SwingConstants.CENTER);
	private JLabel total = new JLabel("TOTAL :", SwingConstants.CENTER);
	private JLabel totalMoney = new JLabel("", SwingConstants.CENTER);
	
	public BettingMoneyInfo() {
		setSize(170, 40);
		setLayout(new GridLayout(2, 1));
		
		JPanel showBettingMoney = new JPanel();
		JPanel showTotalMoney = new JPanel();
		
		turn.setForeground(Color.WHITE);
		bettingMoney.setForeground(Color.WHITE);
		total.setForeground(Color.WHITE);
		totalMoney.setForeground(Color.WHITE);
		showBettingMoney.setBackground(Color.BLACK);
		showTotalMoney.setBackground(Color.BLACK);
		
		showBettingMoney.setSize(170, 20);
		showBettingMoney.setLayout(new GridLayout(1, 2));
		
		showTotalMoney.setSize(170, 20);
		showTotalMoney.setLayout(new GridLayout(1, 2));
		
		showBettingMoney.add(turn);
		showBettingMoney.add(bettingMoney);
		
		showTotalMoney.add(total);
		showTotalMoney.add(totalMoney);
		
		add(showBettingMoney);
		add(showTotalMoney);
	}
	
	public void updateMoneyPanel(String currentTurn, String bettingMoney, String totalMoney) {
		this.turn.setText(currentTurn);
		this.bettingMoney.setText(bettingMoney+"원");
		this.totalMoney.setText(totalMoney);
	}
}
