package g_GamePage;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import static normalclass.Main.*;
import normalclass.User;

public class CardPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private ArrayList<JPanel> cardPanel = new ArrayList<JPanel>();
	private JLabel moneyLabel;
	
	
	public CardPanel(User user, int index) {
		setLayout(null);
		setOpaque(true);
		
		JPanel cardGroup = new JPanel();
		cardGroup.setBackground(Color.WHITE);
		JPanel haveMoney = new JPanel();
		haveMoney.setBackground(Color.DARK_GRAY);
		cardGroup.setOpaque(true);
		haveMoney.setOpaque(true);
		
		cardGroup.setBounds(0, 0, 420, 90);
		cardGroup.setLayout(new GridLayout(1, 7));
		
		for(int q = 0 ; q < 7 ; q++){
			cardPanel.add(new JPanel());
			cardPanel.get(q).setOpaque(true);
			cardPanel.get(q).setBackground(Color.BLACK);
			cardGroup.add(cardPanel.get(q));
		}
		
		cardPanel.get(6).addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(user.getMycard().size() == 7
						&& !player.get(0).getMycard().get(6).getFnB()
						&& index == 0)
				{
					user.getMycard().get(6).setFnB(true);
					
					for(int q = 0 ; q < game.getCardPanel().size() ; q++)
						for(int w = 0 ; w < game.getCardPanel().get(q).getCardPanel().size() ; w++)
							game.getCardPanel().get(q).getCardPanel().get(w).removeAll();
						
					game.SetCardField();
					
					Timer timer = new Timer();
					TimerTask timertask = new TimerTask(){
						public void run(){
							game.getPedigree().resetPedigree();
						}
					};
					timer.schedule(timertask, 1000);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e){
				if(user.getMycard().size() == 7
						&& user.getMycard().get(6).getFnB() == false
						&& index == 0){
					cardPanel.get(6).setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			@Override
			public void mouseExited(MouseEvent e){
				cardPanel.get(6).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		haveMoney.setBounds(0, 90, 420, 30);
		haveMoney.setLayout(new GridLayout(1, 3));
		
		JLabel nameLabel = new JLabel(" NAME : "+user.getUserName());
		
		String money = String.format("보유금액 %,d원", user.getMoney());
		moneyLabel = new JLabel(money);
		
		nameLabel.setForeground(Color.WHITE);
		moneyLabel.setForeground(Color.WHITE);
		
		haveMoney.add(nameLabel);
		haveMoney.add(moneyLabel);
		
		if(index >= 1) {
			JLabel LevelLabel = new JLabel("AI수준 : LEVEL "+player.get(index).getAILevel());
			haveMoney.add(LevelLabel);
			LevelLabel.setForeground(Color.WHITE);
		}
		
		add(cardGroup);
		add(haveMoney);
	}
	
	public void resetMoney(String money){
		moneyLabel.setText(money);
	}
	
	public ArrayList<JPanel> getCardPanel(){
		return cardPanel;
	}
}
