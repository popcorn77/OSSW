package g_FirstPage;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import normalclass.Main;

public class FindID extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public FindID() {
		
		setSize(250,100);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("ID");
		setLayout(new GridLayout(3,2));
		
		
		JLabel nLabel = new JLabel("이름", SwingConstants.CENTER);
		JTextField nField = new JTextField();
		JLabel pLabel = new JLabel("핸드폰 번호", SwingConstants.CENTER);
		JTextField pField = new JTextField();
		pField.setText("( - ) 제외");
		
		JButton find = new JButton("찾기");
		JButton cancel = new JButton("취소");
		
		find.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				boolean chk = true;
				
				for(int q = 0 ; q < pField.getText().length() ; q++){
					try{
						Integer.parseInt(pField.getText().substring(q, q + 1));
					}catch(Exception ex){
						Main.gameMsg += "<전화번호에 문자나 (-)이 포함> ";
						chk = false;
						break;
					}
				}
				if(nField.getText().equals("") || pField.getText().equals("")) {
					Main.gameMsg += "<공백 존재> ";
					chk = false;
				}
				
				if(chk){
					Main.msgBox("찾으시는 정보 ->  "+findId(nField.getText(), pField.getText()));
					dispose();
				}
				else{
					Main.msgBox(Main.gameMsg);
					Main.gameMsg = "";
				}
				
			}
			
		});
		
		cancel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				dispose();
			}
		});
		
		add(nLabel);
		add(nField);
		add(pLabel);
		add(pField);
		add(find);
		add(cancel);
		
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public String findId(String name, String phNum) {
		for(int q = 0; q<Main.join.getMemberList().size(); q++) {
			
			if(Main.join.getMemberList().get(q).getName().equals(name)
				&& Main.join.getMemberList().get(q).getPhNum().equals(phNum)) {
				
				return Main.join.getMemberList().get(q).getId();
			} 
		}
		return " 정보 없음";
	}
}