package g_TitlePage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import g_FirstPage.FirstPage;
import normalclass.Account;
import static normalclass.Main.*;

public class DeleteAccount extends JFrame{
	private static final long serialVersionUID = 1L;
	private static int chk;
	
	public DeleteAccount() {

		setTitle("계정탈퇴");
		setSize(300,100);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(FirstPage.DISPOSE_ON_CLOSE);

		JLabel idLabel = new JLabel("ID", SwingConstants.CENTER);
		JLabel pwLabel = new JLabel("Password", SwingConstants.CENTER);

		JTextField idText = new JTextField();
		JPasswordField pwText = new JPasswordField();

		JButton deleteBt = new JButton("delete");
		JButton cancelBt = new JButton("Cancel");

		JPanel accountInput = new JPanel();
		JPanel accountInput2 = new JPanel();

		accountInput.setSize(350, 50);
		accountInput.setLayout(new GridLayout(2, 2));
		accountInput2.setSize(350, 50);
		accountInput2.setLayout(new GridLayout(1, 2));

		cancelBt.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				dispose();
			}
		});

		deleteBt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				boolean deleteAccount = true;
				chk = 0;
				if(idText.getText().equals("")
						|| String.valueOf(pwText.getPassword()).equals("")) {
					gameMsg += "<공백 존재>  ";
					deleteAccount = false;
				}

				if(deleteAccount){
					for(int q = 0; q < join.getMemberList().size() ; q++) {
						if(searchAccount(join.getMemberList().get(q)
								,idText.getText()
								,String.valueOf(pwText.getPassword()))) {
							
							player.remove(0);
							join.getMemberList().remove(q);
							join.getMemberList().trimToSize();
							dispose();
							title.dispose();
							first = new FirstPage();
							msgBox("그동안 이용해주셔서 감사합니다.");
							chk = 1;
							break;
						}
					}
				}
				if(chk == 0){
					gameMsg += "<입력한 계정이 없습니다.>";
					idText.setText("");
					pwText.setText("");
					deleteAccount = false;
				}
				if(!deleteAccount){
					msgBox(gameMsg);
					gameMsg = "";
				}
			}
		});

		accountInput.add(idLabel);
		accountInput.add(idText);
		accountInput.add(pwLabel);
		accountInput.add(pwText);
		accountInput2.add(deleteBt);
		accountInput2.add(cancelBt);
		getContentPane().add(accountInput, BorderLayout.CENTER);
		getContentPane().add(accountInput2, BorderLayout.SOUTH);
	}
	
	public boolean searchAccount(Account account, String id, String password){

		if(account.getId().equals(id) && account.getPassword().equals(password)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static int getChk(){
		return chk;
	}
}