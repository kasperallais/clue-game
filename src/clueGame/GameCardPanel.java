package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameCardPanel extends JPanel{
	
	private JTextField peopleHand;
	private JTextField peopleSeen;
	private JLabel peopleInHand;
	private JLabel peopleInSeen;
	private ArrayList<String> handPeople;
	private ArrayList<String> seenPeople;
	
	private JLabel roomInHand;
	private JLabel roomInSeen;
	private JTextField roomHand;
	private JTextField roomSeen;
	
	private JLabel weaponInHand;
	private JLabel weaponInSeen;
	private JTextField weaponHand;
	private JTextField weaponSeen;
	
	JPanel overAll;
	
	public GameCardPanel() {
		handPeople = new ArrayList<String>();
		overAll = new JPanel();
		setLayout(new GridLayout(1,1));
		TitledBorder border = BorderFactory.createTitledBorder("Known Cards");
		overAll.setLayout(new GridLayout(3,1));
		JPanel peoplePanel = createPeoplePanel();
		overAll.add(peoplePanel);
		JPanel roomPanel = createRoomPanel();
		overAll.add(roomPanel);
		JPanel weaponPanel = createWeaponPanel();
		overAll.add(weaponPanel);
		overAll.setBorder(border);
		add(overAll);
		
	}
	
	public JPanel createPeoplePanel() {
		JPanel overall = new JPanel();
		overall.setLayout(new GridLayout(0,1));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		TitledBorder border = BorderFactory.createTitledBorder("People");
		peopleInHand = new JLabel("In Hand:");
		peopleInSeen = new JLabel("Seen");
		panel.add(peopleInHand);

		if (handPeople.size() == 0) {
			peopleHand = new JTextField("None");
			panel.add(peopleHand);
		} else {
			for (String s: handPeople) {
				JTextField newPeopleHand = new JTextField(s);
				panel.add(newPeopleHand);
			}
		}
		
		peopleSeen = new JTextField("None");
		peopleHand.setBorder(new EtchedBorder());
		peopleSeen.setBorder(new EtchedBorder());
		
		panel.add(peopleInSeen);
		panel.add(peopleSeen);
		overall.add(panel);
		overall.setBorder(border);
		
		return overall;
	}
	
	public JPanel createRoomPanel() {
		JPanel overall = new JPanel();
		overall.setLayout(new GridLayout(0,1));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		TitledBorder border = BorderFactory.createTitledBorder("Rooms");
		roomInHand = new JLabel("In Hand:");
		roomInSeen = new JLabel("Seen");
		roomHand = new JTextField("None");
		roomSeen = new JTextField("None");
		roomHand.setBorder(new EtchedBorder());
		roomSeen.setBorder(new EtchedBorder());
		
		panel.add(roomInHand);
		panel.add(roomHand);
		panel.add(roomInSeen);
		panel.add(roomSeen);
		overall.setBorder(border);
		overall.add(panel);
		return overall;
	}
	
	public JPanel createWeaponPanel() {
		JPanel overall = new JPanel();
		overall.setLayout(new GridLayout(0,1));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		TitledBorder border = BorderFactory.createTitledBorder("Weapons");
		weaponInHand = new JLabel("In Hand:");
		weaponInSeen = new JLabel("Seen");
		weaponHand = new JTextField("None");
		weaponSeen = new JTextField("None");
		weaponHand.setBorder(new EtchedBorder());
		weaponSeen.setBorder(new EtchedBorder());
		
		panel.add(weaponInHand);
		panel.add(weaponHand);
		panel.add(weaponInSeen);
		panel.add(weaponSeen);
		overall.setBorder(border);
		overall.add(panel);
		return overall;
	}
	
	public void addPeopleCard(Player player) {
		handPeople.add(player.getName());
		updatePanels();
	}
	
	public void updatePanels() {		
		overAll.removeAll();

		overAll.add(createPeoplePanel());
		overAll.add(createRoomPanel());
		overAll.add(createWeaponPanel());

		overAll.updateUI();
		//overAll.revalidate();
		overAll.repaint();
	}
	
	
	
	
	public static void main(String[] args) {
		GameCardPanel panel = new GameCardPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(180, 750);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		// test filling in the data
		panel.addPeopleCard(new ComputerPlayer( "Col. Mustard", "orange", 0, 0));
		//panel.addSeenPerson();
//		panel.addRoomCard();
//		panel.addSeenRoom();
//		panel.addWeaponCard();
//		panel.addSeenWeapon();
		
	}
}
