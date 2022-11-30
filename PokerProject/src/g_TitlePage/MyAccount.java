package g_TitlePage;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import g_FirstPage.FirstPage;
import normalclass.Main;

public class MyAccount extends JFrame {
	private static final long serialVersionUID = 0;
	ArrayList<JLabel> textLabel = new ArrayList<JLabel>();
	ArrayList<JLabel> InfoLabel = new ArrayList<JLabel>();
	String[] textArr = {"<Name>", "<Money>", "<JoinDate>", "<ChargeTicket>","<Score>"};
	
	public MyAccount(){
		
		setTitle("My Infomation");
		setSize(220,150);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new GridLayout(5, 2));
		setDefaultCloseOperation(FirstPage.DISPOSE_ON_CLOSE);
		
		InfoLabel.add(new JLabel(Main.join.getMemberList().get(Main.accountIndex).getName(), SwingConstants.LEFT));
		InfoLabel.add(new JLabel(String.format("%,d원", Main.user.getMoney(), SwingConstants.LEFT)));
		InfoLabel.add(new JLabel(Main.join.getMemberList().get(Main.accountIndex).getJoinDate(), SwingConstants.LEFT));
		InfoLabel.add(new JLabel(Main.join.getMemberList().get(Main.accountIndex).getChargeTicket()+"개", SwingConstants.LEFT));
		InfoLabel.add(new JLabel(Main.join.getMemberList().get(Main.accountIndex).getScore()[0]+" 승 "
				+Main.join.getMemberList().get(Main.accountIndex).getScore()[1]+" 패", SwingConstants.LEFT));
		
		for(int q = 0 ; q < textArr.length ; q++){
			textLabel.add(new JLabel(textArr[q], SwingConstants.CENTER));
			this.add(textLabel.get(q));
			this.add(InfoLabel.get(q));
		}
	} 
}
