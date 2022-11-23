package g_FirstPage;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import normalclass.Main;

public class FindPassword extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public FindPassword() {
		
		setSize(250,100);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Password");
		setLayout(new GridLayout(3,2));
		
		
		JLabel idLabel = new JLabel("ID", SwingConstants.CENTER);
		JTextField idTextField = new JTextField();
		JLabel nameLabel = new JLabel("이름", SwingConstants.CENTER);
		JTextField nameTextField = new JTextField();
		JButton find = new JButton("찾기");
		JButton cancel = new JButton("취소");
		
		find.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(idTextField.getText().equals("") || nameTextField.getText().equals("")) {
					Main.msgBox("<공백 존재>");
					
				}else{
					Main.msgBox("찾으시는 정보 ->  "+findPassword(idTextField.getText(), nameTextField.getText()));
					dispose();
				}
			}
		});
		
		cancel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				dispose();
			}
		});

		add(idLabel);
		add(idTextField);
		add(nameLabel);
		add(nameTextField);
		add(find);
		add(cancel);

		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);    
	}
	
	public String findPassword(String id, String name){
		for(int q = 0 ; q < Main.join.getMemberList().size() ; q++){
			
			if(Main.join.getMemberList().get(q).getId().equals(id)
					&& Main.join.getMemberList().get(q).getName().equals(name)){
				return Main.join.getMemberList().get(q).getPassword();
			}
		}
		return " 정보 없음";
	}
}
