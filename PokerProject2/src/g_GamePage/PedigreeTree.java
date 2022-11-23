package g_GamePage;

import static normalclass.Main.user;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PedigreeTree extends JPanel{
	private static final long serialVersionUID = 0;
	private ArrayList<JLabel> gradeLabel = new ArrayList<JLabel>();
	private String[] gradeName = {"Top"
			,"OnePair"
			,"TwoPair"
			,"Triple"
			,"Straight"
			,"Flush"
			,"Full House"
			,"Four Card"
			,"Straight Flush"
			,"Royal Straight Flush"};

	public PedigreeTree() {

		setSize(150,170);
		setLayout(new GridLayout(10,1));

		for(int q = 0 ;  q < gradeName.length ; q++){
			gradeLabel.add(new JLabel(gradeName[q],SwingConstants.CENTER));
			gradeLabel.get(q).setOpaque(true);
			gradeLabel.get(q).setBackground(Color.DARK_GRAY);
			gradeLabel.get(q).setForeground(Color.WHITE);
		}
		
		for(int q = gradeName.length ; q > 0 ; q--) {
			add(gradeLabel.get(q - 1));
		}
		
	}
	
	public void resetPedigree(){

		new Thread(new Runnable() {
			public void run() {

				for(int q = 0 ;  q < gradeLabel.size() ; q++){
					gradeLabel.get(q).setForeground(Color.WHITE);
				}
				gradeLabel.get(user.getGrade(0)).setForeground(Color.YELLOW);

			}
		}).start();
	}
	
	public ArrayList<JLabel> getGradeLabel(){
		return gradeLabel;
	}
	
	public String[] getGradeName() {
		return gradeName;
	}
}
