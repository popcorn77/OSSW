package g_TitlePage;

import static normalclass.Main.*;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import g_FirstPage.FirstPage;
import normalclass.SetupInfo;
import normalclass.UserAI;

public class TitlePage extends JFrame {
	private static final long serialVersionUID = 0;
	private ArrayList<Integer> aiLevel = new ArrayList<Integer>();
	private ArrayList<JButton> playButton = new ArrayList<JButton>();
	private ArrayList<JButton> subButton = new ArrayList<JButton>();
	private ArrayList<ImageIcon> sub_button_BasicImage = new ArrayList<ImageIcon>();
	private ArrayList<ImageIcon> sub_button_EnteredImage = new ArrayList<ImageIcon>();
	private ArrayList<ImageIcon> buttonBasicImage = new ArrayList<ImageIcon>();
	private ArrayList<ImageIcon> buttonEnteredImage = new ArrayList<ImageIcon>();
	private JLabel BackGround = new JLabel(new ImageIcon(TitlePage.class.getResource("../plus_Images/Title_Background.png")));
	private String[] aiName = {"기웅이", "지나가는 광수", "트럼프", "상민God", "나그네", "할머니", "엄미현"};
	
	public TitlePage() {
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent e) {
				new SetupInfo().save();
			}
		});//그냥 종료해도 계정정보가 저장됨
		
		BackGround.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		setDefaultCloseOperation(FirstPage.DISPOSE_ON_CLOSE);
		setTitle(" SE7EN Poker ");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		
		for(int q = 0 ; q < 4 ; q++){
			buttonBasicImage.add(new ImageIcon(TitlePage.class.getResource("../plus_Images/Title_"+(q+2)+"PlayerButtonBasic.png")));
			buttonEnteredImage.add(new ImageIcon(TitlePage.class.getResource("../plus_Images/Title_"+(q+2)+"PlayerButtonEntered.png")));
			
			playButton.add(new JButton(buttonBasicImage.get(q)));
			playButton.get(q).setBorderPainted(false);
			playButton.get(q).setContentAreaFilled(false);
			playButton.get(q).setFocusPainted(false);
			playButton.get(q).addMouseListener(new PlayButton(q));
			
			add(playButton.get(q));
			
			sub_button_BasicImage.add(new ImageIcon(TitlePage.class.getResource("../plus_Images/Title_SubButton"+(q+1)+"Basic.png")));
			sub_button_EnteredImage.add(new ImageIcon(TitlePage.class.getResource("../plus_Images/Title_SubButton"+(q+1)+"Entered.png")));
			
			subButton.add(new JButton(sub_button_BasicImage.get(q)));
			subButton.get(q).addMouseListener(new SubButton(q));
			subButton.get(q).setBorderPainted(false);
			subButton.get(q).setContentAreaFilled(false);
			subButton.get(q).setFocusPainted(false);
			subButton.get(q).setBounds(50, (q + 1) * 50, 90, 28);
			playButton.get(q).setBounds(900, (q + 1) * 120, 284, 80);
			
			add(subButton.get(q));
		}
		
		add(BackGround);
		setVisible(true);
	}
	
	class PlayButton extends MouseAdapter {
		int index;
		
		PlayButton(int index) {
			this.index = index;
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			playButton.get(index).setIcon(buttonEnteredImage.get(index));
			playButton.get(index).setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		@Override
		public void mouseExited(MouseEvent e) {
			playButton.get(index).setIcon(buttonBasicImage.get(index));
			playButton.get(index).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		public void mouseClicked(MouseEvent e) {
			if(user.getMoney() > 10000) {
				title.setVisible(false);
				new LevelSelect(index + 1);
			} else {
				msgBox("잔액이 10000원 이하이므로 게임에 참여불가");
			}
		}
	}
	
	class SubButton extends MouseAdapter {
		int index;
		
		SubButton(int index) {
			this.index = index;
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			subButton.get(index).setIcon(sub_button_EnteredImage.get(index));
			subButton.get(index).setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		@Override
		public void mouseExited(MouseEvent e) {
			subButton.get(index).setIcon(sub_button_BasicImage.get(index));
			subButton.get(index).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			
			switch(index) {
			case 0:
				new MyAccount().setVisible(true);
				break;
			case 1:
				if(join.getMemberList().get(accountIndex).getChargeTicket() == 1 && user.getMoney() < 10000) {
					int chargeMoney = join.getMemberList().get(accountIndex).charging();
					user.addMoney(chargeMoney);
					msgBox(String.format("%,d 원 충전완료!", chargeMoney));
				}
				else if(user.getMoney() >= 10000) {
					msgBox("잔액이 10,000원 이하일때만 충전 가능합니다!");
				}
				else if(join.getMemberList().get(accountIndex).getChargeTicket() == 0) {
					msgBox("충전 티켓이 없습니다!");
				}
				break;
			case 2:
				new DeleteAccount().setVisible(true);
				break;
			case 3:
				dispose();
				player.remove(0);
				first.setVisible(true);
				new SetupInfo().save();
				new SetupInfo().setup();
				break;
			}
		}
	}
	
	public void setAILevel(int index) {
		aiLevel.add(index);
	}
	
	public ArrayList<Integer> getAILevel(){
		return aiLevel;
	}
	
	public void setAIPlayer(int aiNum){
		for(int q = 0 ; q < 10 ; q++)
		{
			String temp = "";
			int random = (int)(Math.random() * 7);
			temp = aiName[random];
			aiName[random] = aiName[0];
			aiName[0] = temp;
		}
		
		for(int q = 0 ; q < aiNum ; q++)
			player.add(new UserAI(Math.round((user.getMoney() * 8 + ((int)(Math.random() * (user.getMoney() * 10)) - user.getMoney() * 5)) / 1000) * 1000
					, aiLevel.get(q)
					, q + 1
					, q + 1
					, false
					, aiName[q]));
		
	}
}
