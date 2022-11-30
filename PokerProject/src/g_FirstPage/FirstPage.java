package g_FirstPage;

import static normalclass.Main.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import g_TitlePage.TitlePage;
import normalclass.Account;
import normalclass.SetupInfo;
import normalclass.User;

public class FirstPage extends JFrame{
	private static final long serialVersionUID = 1L;
	private ArrayList<ImageIcon> BasicButtonImage = new ArrayList<ImageIcon>();
	private ArrayList<ImageIcon> EnteredButtonImage = new ArrayList<ImageIcon>();
	private ArrayList<JButton> buttonList = new ArrayList<JButton>();
	
	public FirstPage() {
		
		setUndecorated(true);
		setSize(700, 390);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		//1.나가기 2.로그인 3.회원가입 4.찾기
		for(int q = 0 ; q < 4 ; q++)
		{
			BasicButtonImage.add(new ImageIcon(FirstPage.class.getResource("../plus_Images/First_Button"+(q + 1)+"Basic.png")));
			EnteredButtonImage.add(new ImageIcon(FirstPage.class.getResource("../plus_Images/First_Button"+(q + 1)+"Entered.png")));
			
			buttonList.add(new JButton(BasicButtonImage.get(q)));
			buttonList.get(q).setBorderPainted(false);
			buttonList.get(q).setContentAreaFilled(false);
			buttonList.get(q).setFocusPainted(false);
			if(q > 0)
				buttonList.get(q).addMouseListener(new ButtonEntered(q));
			
			add(buttonList.get(q));
		}
		
		buttonList.get(0).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				buttonList.get(0).setIcon(EnteredButtonImage.get(0));
				buttonList.get(0).setCursor(new Cursor(Cursor.HAND_CURSOR));
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				buttonList.get(0).setIcon(BasicButtonImage.get(0));
				buttonList.get(0).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				new SetupInfo().save();
				System.exit(0);
			}
		});
		
		JLabel backGround = new JLabel(new ImageIcon(FirstPage.class.getResource("../plus_Images/First_Background.png")));
		JLabel idLabel = new JLabel(new ImageIcon(FirstPage.class.getResource("../plus_Images/First_ID.png")));
		JLabel pwLabel = new JLabel(new ImageIcon(FirstPage.class.getResource("../plus_Images/First_PW.png")));
		JTextField idText = new JTextField(12);
		JPasswordField pwText = new JPasswordField(12);
		pwText.setEchoChar('*');
		
		class LoginAction implements ActionListener{ //로그인 이벤트
			@Override
			public void actionPerformed(ActionEvent e) {
				int loginCheck = 0;
				
				for(int q = 0 ; q < join.getMemberList().size() ; q++)
					if(searchAccount(join.getMemberList().get(q)
							, idText.getText()
							, String.valueOf(pwText.getPassword())))
					{
						loginCheck = 1;
						accountIndex = q;
						join.getMemberList().get(q).accum(Join.today);
						join.getMemberList().get(q).chargeTicket(Join.today);
						join.getMemberList().get(q).setLastContact(Join.today);

						user = new User(join.getMemberList().get(q).getName()
								,join.getMemberList().get(q).getMoney()
								,true);
						player.add(user);
						title = new TitlePage();
						dispose();
						break;
					}
				
				if(loginCheck == 0)
					msgBox("ID 나 Password가 일치하지 않습니다.");
				
				idText.setText("");
				pwText.setText("");
			}
		}//Login Action
		
		buttonList.get(1).addActionListener(new LoginAction());
		pwText.addActionListener(new LoginAction());
		
		buttonList.get(2).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Join().setVisible(true);
			}
		});
//		buttonList.get(3).addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e){
//				new FindAccount().setVisible(true);
//			}
//		});
		
		buttonList.get(0).setBounds(640, 0, 60, 37);
		buttonList.get(1).setBounds(30, 260, 210, 34);
		buttonList.get(2).setBounds(30, 305, 86, 34);
		buttonList.get(3).setBounds(154, 305, 86, 34);
		idLabel.setBounds(30, 170, 210, 34);
		idText.setBounds(90, 176, 110, 24);
		pwLabel.setBounds(30, 215, 210, 34);
		pwText.setBounds(90, 221, 110, 24);
		idText.setOpaque(true);
		pwText.setOpaque(true);
		idText.setBackground(Color.BLACK);
		idText.setForeground(Color.CYAN);
		pwText.setBackground(Color.BLACK);
		pwText.setForeground(Color.CYAN);
		idText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		pwText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	
		add(idLabel);
		add(idText);
		add(pwLabel);
		add(pwText);
		add(backGround, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public boolean searchAccount(Account account, String id, String password){
		if(account.getId().equals(id) && account.getPassword().equals(password))
			return true;
		else
			return false;
	}
	
	class ButtonEntered extends MouseAdapter {
		int index;
		
		ButtonEntered(int index){
			this.index = index;
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			buttonList.get(index).setIcon(EnteredButtonImage.get(index));
			buttonList.get(index).setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		@Override
		public void mouseExited(MouseEvent e) {
			buttonList.get(index).setIcon(BasicButtonImage.get(index));
			buttonList.get(index).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	//초기화면 (기웅)
}