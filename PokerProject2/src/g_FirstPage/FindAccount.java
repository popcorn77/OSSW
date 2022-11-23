package g_FirstPage;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;


public class FindAccount extends JFrame{
	private static final long serialVersionUID = 1L;

	public FindAccount() {
		
		setSize(150,100);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3,1));
		JButton Idbt = new JButton("ID");
		JButton Pwbt = new JButton("Password");
		JButton Cancel = new JButton("Cancel");
		setDefaultCloseOperation(FirstPage.DISPOSE_ON_CLOSE);
		
		add(Idbt);
		add(Pwbt);
		add(Cancel);
		
		Cancel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				dispose();
			}
		});
		Idbt.addMouseListener(new MouseAdapter() {
	         @Override
	         public void mousePressed(MouseEvent e){
	            new FindID().setVisible(true);
	         }
	    });
		
		Pwbt.addMouseListener(new MouseAdapter() {
	         @Override
	         public void mousePressed(MouseEvent e){
	            new FindPassword().setVisible(true);
	         }
	    });
		setVisible(true);	
	}
}
