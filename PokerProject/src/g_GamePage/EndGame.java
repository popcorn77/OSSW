package g_GamePage;

import static normalclass.Main.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import g_FirstPage.FirstPage;
import g_TitlePage.TitlePage;


public class EndGame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JLabel winner = new JLabel("",SwingConstants.CENTER);
	private JLabel totalMoney = new JLabel("",SwingConstants.CENTER);
	private JLabel winnerLabel = new JLabel("",SwingConstants.CENTER);
	private JLabel totalLabel = new JLabel("",SwingConstants.CENTER);
	private JPanel buttonPanel = new JPanel();
	private JButton backButton = new JButton(new ImageIcon(EndGame.class.getResource("../plus_Images/Game_Back.png")));
	private JButton continueButton = new JButton(new ImageIcon(EndGame.class.getResource("../plus_Images/Game_Continue.png")));
	
	public EndGame(String winnerName, int grade, int betMoney) {
		JLabel resultBackground = new JLabel(new ImageIcon(FirstPage.class.getResource("../plus_Images/Game_result"+grade+".png")));
		
		setSize(300, 300);
		setUndecorated(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		buttonPanel.setLayout(new GridLayout(1,2));
		setDefaultCloseOperation(EndGame.DISPOSE_ON_CLOSE);
		
		winner.setForeground(Color.WHITE);
		totalMoney.setForeground(Color.WHITE);
		winnerLabel.setForeground(Color.YELLOW);
		totalLabel.setForeground(Color.YELLOW);
		winner.setText("W I N N E R");
		totalMoney.setText("TOTAL MONEY");
		winnerLabel.setText(winnerName);
		totalLabel.setText(String.format("%,d원", betMoney));
		
		buttonPanel.setBounds(4,276,292,20);
		winner.setBounds(0, 150, 150, 100);
		totalMoney.setBounds(150, 150, 150, 100);
		winnerLabel.setBounds(0, 180, 150, 100);
		totalLabel.setBounds(150, 180, 150, 100);
		resultBackground.setBounds(0,0,300,300);
		
		for(int q = 0 ; q < player.size() ; q++){
			player.get(q).setallin(false);
		}
		
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				title = new TitlePage();
				game.initializedEnd();
				game.dispose();
				dispose();
			}
		});
		
		continueButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {

				game.initializedReplay();
				gameSystem.continueUser();

				for(int q = 0 ; q < player.size() ; q++) {
					
					if(player.get(q).getUserOrAI() && user.getMoney() <= 10000) {
						game.initializedEnd();
						game.dispose();
						dispose();
						title = new TitlePage();
						msgBox("잔액이 10000원 이하이므로 게임에 재참여불가");
						return;
					}
					else if(!player.get(q).getUserOrAI() && player.get(q).getMoney() <= 100000) {
						player.remove(q);
						q--;
					}
					player.get(q).setPIndex(q);
				}

				if(player.size() == 1) {
					game.initializedEnd();
					game.dispose();
					dispose();
					title = new TitlePage();
					msgBox("상대가 모두 기권하였습니다.");
					return;
				}

				game.dispose();
				gameSystem.resetPlayerIndex();
				game = new PlayGame();
				dispose();
			}
		});
		
		buttonPanel.add(backButton);
		buttonPanel.add(continueButton);
		add(winner);
		add(totalMoney);
		add(winnerLabel);
		add(totalLabel);
		add(buttonPanel, BorderLayout.SOUTH);
		add(resultBackground, BorderLayout.CENTER);
		setVisible(true);
	}
}