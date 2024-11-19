package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameCardPanel extends JPanel{
	
	private JTextField peopleHand;
	private JTextField peopleSeen;
	private JLabel peopleInHand;
	private JLabel peopleInSeen;
	private ArrayList<Card> handPeople;
	private ArrayList<Card> seenPeople;
	
	private JLabel roomInHand;
	private JLabel roomInSeen;
	private JTextField roomHand;
	private JTextField roomSeen;
	private ArrayList<Card> handRoom;
	private ArrayList<Card> seenRoom;
	
	private JLabel weaponInHand;
	private JLabel weaponInSeen;
	private JTextField weaponHand;
	private JTextField weaponSeen;
	private ArrayList<Card> handWeapon;
	private ArrayList<Card> seenWeapon;
	
	JPanel overAll;
	
	public GameCardPanel() {
		handPeople = new ArrayList<Card>();
		seenPeople = new ArrayList<Card>();
		handRoom = new ArrayList<Card>();
		seenRoom = new ArrayList<Card>();
		handWeapon = new ArrayList<Card>();
		seenWeapon = new ArrayList<Card>();
		
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
			peopleHand.setEditable(false);
			panel.add(peopleHand);
		} else {
			loopThrough(handPeople, panel);
		}
		
		panel.add(peopleInSeen);
		if (seenPeople.size() == 0) {
			peopleSeen = new JTextField("None");
			peopleSeen.setEditable(false);
			panel.add(peopleSeen);
		} else {
			loopThrough(seenPeople, panel);
		}
		
		peopleHand.setBorder(new EtchedBorder());
		peopleSeen.setBorder(new EtchedBorder());
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
		panel.add(roomInHand);
		
		if (handRoom.size() == 0) {
			roomHand = new JTextField("None");
			roomHand.setEditable(false);
			panel.add(roomHand);
		} else {
			loopThrough(handRoom, panel);
		}
		
		roomInSeen = new JLabel("Seen");
		panel.add(roomInSeen);
		
		if (seenRoom.size() == 0) {
			roomSeen = new JTextField("None");
			roomSeen.setEditable(false);
			panel.add(roomSeen);
		} else {
			loopThrough(seenRoom, panel);
		}
		
		roomHand.setBorder(new EtchedBorder());
		roomSeen.setBorder(new EtchedBorder());
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
		panel.add(weaponInHand);
		if (handWeapon.size() == 0) {
			weaponHand = new JTextField("None");
			weaponHand.setEditable(false);
			panel.add(weaponHand);
		} else {
			loopThrough(handWeapon, panel);
		}
		
		weaponInSeen = new JLabel("Seen");
		panel.add(weaponInSeen);
		if (seenWeapon.size() == 0) {
			weaponSeen = new JTextField("None");
			weaponSeen.setEditable(false);
			panel.add(weaponSeen);
		} else {
			loopThrough(seenWeapon, panel);
		}
		
		weaponHand.setBorder(new EtchedBorder());
		weaponSeen.setBorder(new EtchedBorder());
		overall.setBorder(border);
		overall.add(panel);
		return overall;
	}
	
	public void addPeopleHand(Card card) {
		handPeople.add(card);
		updatePanels();
	}
	
	public void addPeopleSeen(Card card) {
		seenPeople.add(card);
		updatePanels();
	}
	
	public void addRoomHand(Card card) {
		handRoom.add(card);
		updatePanels();
	}
	
	public void addRoomSeen(Card card) {
		seenRoom.add(card);
		updatePanels();
	}
	
	public void addWeaponHand(Card card) {
		handWeapon.add(card);
		updatePanels();
	}
	
	public void addWeaponSeen(Card card) {
		seenWeapon.add(card);
		updatePanels();
	}
	
	public void loopThrough(ArrayList<Card> cardArray, JPanel panel) {
		for (Card c: cardArray) {
			JTextField newAddition = new JTextField(c.getName());
			newAddition.setBackground(getColor(c.getColor()));
			newAddition.setEditable(false);
			panel.add(newAddition);
		}
	}
	
	public Color getColor(String color) {
		Color returnColor = Color.white;
		switch(color) {
		case "orange":
			returnColor = Color.orange;
			break;
		case "green": 
			returnColor = Color.green;
			break;
		case "blue":
			returnColor = Color.blue;
			break;
		case "pink":
			returnColor = Color.pink;
			break;
		}
		return returnColor;
	}
	
	public void updatePanels() {		
		SwingUtilities.invokeLater(() -> {
            overAll.removeAll();

            overAll.add(createPeoplePanel());
            overAll.add(createRoomPanel());
            overAll.add(createWeaponPanel());

            overAll.revalidate();
            overAll.repaint();
        });
	}
	
	public static void main(String[] args) {
		GameCardPanel panel = new GameCardPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(180, 750);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		// test filling in the data
		panel.addPeopleHand(new Card( "Col. Mustard", "white", CardType.PERSON));
		
		panel.addPeopleSeen(new Card( "Mrs White", "orange", CardType.PERSON));
		panel.addPeopleSeen(new Card( "Ms Scarlett", "white", CardType.PERSON));
		panel.addPeopleSeen(new Card( "Mrs Peacock", "green", CardType.PERSON));
		panel.addPeopleSeen(new Card( "Reverend Green", "blue", CardType.PERSON));
		
		panel.addRoomSeen(new Card( "Hall", "orange", CardType.ROOM));
		panel.addRoomSeen(new Card( "Ballroom", "white", CardType.ROOM));
		panel.addRoomSeen(new Card( "Kitchen", "white", CardType.ROOM));
		panel.addRoomSeen(new Card( "Billiard Room", "green", CardType.ROOM));
		panel.addRoomSeen(new Card( "Conservatory", "green", CardType.ROOM));
		panel.addRoomSeen(new Card( "Lounge", "blue", CardType.ROOM));
		panel.addRoomSeen(new Card( "Library", "blue", CardType.ROOM));
		panel.addRoomSeen(new Card( "Dining Room", "pink", CardType.ROOM));
		
		panel.addWeaponHand(new Card( "Wrench", "white", CardType.WEAPON));
		panel.addWeaponHand(new Card( "Rope", "white", CardType.WEAPON));
		
		panel.addWeaponSeen(new Card( "Lead Pipe", "orange", CardType.WEAPON));
		panel.addWeaponSeen(new Card( "Dagger", "pink", CardType.WEAPON));
		panel.addWeaponSeen(new Card( "Revolver", "pink", CardType.WEAPON));
		
	}
}
