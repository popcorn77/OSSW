package g_GamePage;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import g_FirstPage.FirstPage;
import normalclass.Main;
import normalclass.User;

public class CardOpen extends JFrame{
	private static final long serialVersionUID = 1L;
	private ArrayList<JLabel> cardLabel = new ArrayList<JLabel>();
	private InvisibleMycard invisibleMycard = new InvisibleMycard

			();

	public CardOpen(User user) {

		setUndecorated(true);
		setSize(180, 110);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(FirstPage.DISPOSE_ON_CLOSE);

		JPanel cardOpenPanel = new JPanel();
		JPanel LabelPanel = new JPanel();
		cardOpenPanel.setLayout(new GridLayout(1, 3));
		cardOpenPanel.setBounds(0, 0, 180, 82);
		LabelPanel.setBounds(0, 83, 180, 30);
		JLabel label = new JLabel("오픈할 카드를선택하세요.");

		label.setForeground(Color.WHITE);
		cardOpenPanel.setOpaque(true);
		cardOpenPanel.setBackground(Color.BLACK);

		LabelPanel.add(label);
		LabelPanel.setOpaque(true);
		LabelPanel.setBackground(Color.BLACK);

		for(int q = 0 ; q < 3 ; q++) {
			cardLabel.add(new JLabel(user.getMycard().get

					(q).getCardFrontImg()));
			cardLabel.get(q).addMouseListener(new 

					OpenCard(user, q));
			cardOpenPanel.add(cardLabel.get(q));
		}

		for(int q = 1 ; q < Main.player.size() ; q++){
			Main.player.get(q).getMycard().get((int)

					(Math.random() * 3)).setFnB(true);
		}

		add(cardOpenPanel);
		add(LabelPanel);
		setVisible(true);
	}

	class OpenCard extends MouseAdapter {
		User user;
		int index;

		OpenCard(User user, int index) {
			this.user = user;
			this.index = index;
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			//exitButton3.setIcon(p4ButtonEnter);
			cardLabel.get(index).setCursor(new Cursor

					(Cursor.HAND_CURSOR));
		}
		@Override
		public void mouseExited(MouseEvent e) {
			//exitButton3.setIcon(p4ButtonBasic);
			cardLabel.get(index).setCursor(new Cursor

					(Cursor.DEFAULT_CURSOR));
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			user.getMycard().get(index).setFnB(true);
			Main.game.cardSort();

			for(int q = 0 ; q < Main.game.getCardPanel

					().size() ; q++) {
				for(int w = 0 ; w < 

						Main.game.getCardPanel().get(q).getCardPanel().size() ; w++) {
					Main.game.getCardPanel().get

					(q).getCardPanel().get(w).removeAll();
				}
			}

			Main.game.SetCardField();
			invisibleMycard.setBounds(270, 525, 120, 82);
			invisibleMycard.setVisible(true);
			invisibleMycard.add(new JLabel

					(Main.user.getMycard().get(0).getCardFrontImg()));
			invisibleMycard.add(new JLabel

					(Main.user.getMycard().get(1).getCardFrontImg()));
			Main.game.add(invisibleMycard);

			Timer timer = new Timer();
			TimerTask timertask = new TimerTask() {
				public void run() {
					Main.game.startGame();
				}
			};
			timer.schedule(timertask, 1000);
			dispose();
		}
	}

	class InvisibleMycard extends JPanel {
		private static final long serialVersionUID = 0;

		public InvisibleMycard() {
			setVisible(false);
			setSize(120,82);
			setLayout(new GridLayout(1, 2));
			setOpaque(true);
			setBackground(Color.BLACK);
		}
	}

	public InvisibleMycard getInvisibleMycard() {
		return invisibleMycard;
	}
}
