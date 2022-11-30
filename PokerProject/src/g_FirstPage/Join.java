package g_FirstPage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import normalclass.Account;
import normalclass.Main;

public class Join extends JFrame{
	private static final long serialVersionUID = 1L;
	private static ArrayList<Account> accountList = new ArrayList<Account>(); //계정이 담겨있는 리스트
	
	public static Calendar cal = Calendar.getInstance();
	public static String today = cal.get(Calendar.YEAR)
			+"."+(cal.get(Calendar.MONTH) + 1)
			+"."+cal.get(Calendar.DATE);
	
	public Join(){
		
		setTitle("회원가입");
		setSize(350,200);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(FirstPage.DISPOSE_ON_CLOSE);
		
		ArrayList<JLabel> labelList = new ArrayList<JLabel>();
		
		labelList.add(new JLabel("ID", SwingConstants.CENTER));	
		labelList.add(new JLabel("Password", SwingConstants.CENTER));
		labelList.add(new JLabel("Again Password", SwingConstants.CENTER));
		labelList.add(new JLabel("Name", SwingConstants.CENTER));
		labelList.add(new JLabel("PhoneNumber", SwingConstants.CENTER));
		
		for(int q = 0 ; q < labelList.size() ; q++){
			labelList.get(q).setForeground(Color.WHITE);
			labelList.get(q).setBackground(Color.BLACK);
			labelList.get(q).setOpaque(true);
		}
		
		JTextField idText = new JTextField();
		JTextField nameText = new JTextField();
		JTextField phNumText = new JTextField();
		JPasswordField pwText = new JPasswordField();
		JPasswordField pwText2 = new JPasswordField();
		pwText.setEchoChar('*');
		pwText2.setEchoChar('*');
		idText.setText("6 ~ 12자리로 입력");
		
		JButton createBT = new JButton("Create");
		JButton cancelBT = new JButton("Cancel");
		JButton confirm = new JButton("인증");
		
		JPanel accountInput = new JPanel();
		JPanel accountInput2 = new JPanel();
		JPanel phNumInput = new JPanel();
		
		accountInput.setSize(350, 100);
		accountInput.setLayout(new GridLayout(5, 2));
		accountInput2.setSize(350, 50);
		accountInput2.setLayout(new GridLayout(1, 2));
		phNumInput.setSize(350, 50);
		phNumInput.setLayout(new GridLayout(1, 2));
		
		cancelBT.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				dispose();
			}
		});

		confirm.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e){
				Main.msgBox("인증번호가 발송 되었습니다.");
				Main.waitTime(300);
				Main.msgBox("사실 그런거 없어요.");
			}
		});

		createBT.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				boolean createAccount = true;
				
				idCheck(idText.getText());
				for(int q = 0 ; q < phNumText.getText().length() ; q++)
				{
					try
					{
						Integer.parseInt(phNumText.getText().substring(q, q + 1));
					}catch(Exception ex)
					{
						Main.gameMsg += "전화번호는 숫자로 입력하세요.";
						createAccount = false;
						break;
					}
				}
				if(phNumText.getText().length() != 11)
				{
					createAccount = false;
					Main.gameMsg += "<전화번호 형식 불일치>  ";
				}
				if(idText.getText().equals("")
						|| String.valueOf(pwText.getPassword()).equals("")
						|| String.valueOf(pwText2.getPassword()).equals("")
						|| nameText.getText().equals("")){
					
					Main.gameMsg += "<공백 존재>  ";
					createAccount = false;
				}
				if(overlap(idText.getText()))
				{
					Main.gameMsg += "<ID 중복>  ";
					createAccount = false;
				}
				
				if(!(String.valueOf(pwText.getPassword()).equals(String.valueOf(pwText2.getPassword()))))
				{
					Main.gameMsg += "<Password 불일치>  ";
					createAccount = false;
				}
				
				if(createAccount)
				{
					dispose();
					Main.msgBox("환영합니다~ "+nameText.getText()+"님!!");
					
					addAccount(idText.getText(), String.valueOf(pwText.getPassword()), nameText.getText(), phNumText.getText());
				}
				else if(!createAccount)
				{
					Main.msgBox(Main.gameMsg);
					Main.gameMsg = "";
				}
			}
		});//MouseClicked Event
		
		accountInput.add(labelList.get(0));
		accountInput.add(idText);
		accountInput.add(labelList.get(1));
		accountInput.add(pwText);
		accountInput.add(labelList.get(2));
		accountInput.add(pwText2);
		accountInput.add(labelList.get(3));
		accountInput.add(nameText);
		accountInput.add(labelList.get(4));
		accountInput2.add(createBT);
		accountInput2.add(cancelBT);
		phNumInput.add(phNumText);
		phNumInput.add(confirm);
		accountInput.add(phNumInput);
		getContentPane().add(accountInput, BorderLayout.CENTER);
		getContentPane().add(accountInput2, BorderLayout.SOUTH);
	}
	
	public void addAccount(String id, String password, String name, String phNum){
		accountList.add(new Account(id, password, name, phNum, today));
	}
	
	public ArrayList<Account> getMemberList(){
		return accountList;
	}
	
	public boolean overlap(String id){
		for(int q = 0 ; q < accountList.size() ; q++)
			if(accountList.get(q).getId().equals(id))
				return true;
		
		return false;
	}
	
	public boolean idCheck(String id){
		if(!(id.length() >= 6 && id.length() <= 12))
		{
			Main.gameMsg += "<ID 글자수 불일치> ";
			return false;
		}
		return true;
	}
	//계정생성 (상민 + 기웅)
}