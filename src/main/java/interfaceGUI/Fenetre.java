package interfaceGUI;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import client.Client;
import server.database.DatabaseReader;
import server.middleware.Serveur;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.Message;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

/**
 * Class Fenetre
 * 
 * **/

public class Fenetre extends JFrame implements ActionListener {
	
	private JButton boutonCreate = new JButton("Créer une salle de discussion");
	private JButton boutonJoin = new JButton("Rejoindre une salle de discussion");
	private JButton boutonValidateCreate = new JButton("Valider");
	private JButton boutonValidateJoin = new JButton("Valider");
	private JButton boutonEnvoyerMessage = new JButton("Envoyer");
	private JButton boutonQuitter = new JButton("Quitter la salle");
	private JButton boutonPseudo = new JButton("Valider");
	
	private static JPanel panel = new JPanel();
	private static JPanel panel2 = new JPanel();
	private static JPanel panel3 = new JPanel();
	private static JPanel text = new JPanel();
    private static JScrollPane panelscroll= new JScrollPane(text);
	private static JPanel panel4 = new JPanel();

	private static JTextField textIP = new JTextField("127.0.0.1");
	private static JTextField textPort = new JTextField("6668");

	private static JTextField textMessage = new JTextField();
	private static JTextField textPseudo = new JTextField();

	
	private JLabel labelIP = new JLabel("Adresse IP : ");
	private JLabel labelPort = new JLabel("Port : ");
	private JLabel labelMessage = new JLabel("Message : ");
	private JLabel labelPseudo = new JLabel("Pseudo : ");

	private static JLabel label;
	
	private int PortValue;
	private String Ipvalue;;			
	private String PortValueString; 
	private String Message;
	private String clientPseudo;
	
	private Serveur s = new Serveur();
	private static Client c1;
	
	/**
	 * Create the GUI
	 * **/
	
	public void buildPanel() {
        setTitle("Messagerie");
        setMinimumSize(new Dimension(700,500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel2.add(Box.createRigidArea(new Dimension(0,100)));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS));
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.LINE_AXIS));
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.PAGE_AXIS));

        panel4.add(panel);
        panel4.add(panel2);
        panel4.add(panelscroll);
        panel4.add(panel3);
        panel.add(boutonCreate);
        panel.add(boutonJoin);

        panel2.add(labelPseudo);
        panel2.add(textPseudo);
        panel2.add(boutonPseudo);

        boutonCreate.setEnabled(false);
        boutonJoin.setEnabled(false);

        panelscroll.setVisible(false);
        text.setBackground(Color.WHITE);

        setContentSize();
        setActionListener();

        getContentPane().add(panel4);
        setVisible(true);
    }
	/**
	 * Set the size of the differents items
	 * **/
		
	private void setContentSize() {
		textIP.setMaximumSize(new Dimension(200,24));
		textPort.setMaximumSize(new Dimension(200,24));
		textIP.setMaximumSize(new Dimension(200,24));
		textPort.setMaximumSize(new Dimension(200,24));
		textMessage.setMaximumSize(new Dimension(200,24));
		textPseudo.setMaximumSize(new Dimension(200,24));
	}
	
	/**
	 *  Set an ActionListener for an item
	 * **/
	
	private void setActionListener() {
		boutonCreate.addActionListener(this);
		boutonJoin.addActionListener(this);
		boutonValidateCreate.addActionListener(this);
		boutonValidateJoin.addActionListener(this);
		boutonQuitter.addActionListener(this);
		boutonEnvoyerMessage.addActionListener(this);
		boutonJoin.addActionListener(this);
		boutonPseudo.addActionListener(this);
	}
	
	/**
	 * Refresh the Chat screen from the Database
	 * **/	
	
	public static void readDatabase() {	//Actualise la fenetre contenant les messages du sallon
		text.removeAll();	//efface les anciens textes
		ResultSet resultSet = DatabaseReader.getMessages(c1.getPseudo());	//récupere le contenu de la DB
        try {
            while (resultSet.next()) {
                label = new JLabel(resultSet.getString("pseudo")+" : "+resultSet.getString("msg"));
                text.add(label);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }	
        text.revalidate();
        text.repaint();
	}
	
	/**
	 * Get the action performed by the user
	 * **/
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == boutonPseudo ) {
            boutonCreate.setEnabled(true);
            boutonJoin.setEnabled(true);
            panel2.remove(labelPseudo);
            panel2.remove(textPseudo);
            panel2.remove(boutonPseudo);
         }
		if(source == boutonCreate){
			boutonCreate.setEnabled(false);
			boutonJoin.setEnabled(false);
			
			panel2.add(labelIP);
			panel2.add(textIP);
			panel2.add(labelPort);
			panel2.add(textPort);
			panel3.add(boutonValidateCreate);
		}
		if(source == boutonJoin){
			boutonCreate.setEnabled(false);
			boutonJoin.setEnabled(false);
			
			panel2.add(labelIP);
			panel2.add(textIP);
			panel2.add(labelPort);
			panel2.add(textPort);
			panel3.add(boutonValidateJoin);
		}
		
		if(source == boutonValidateCreate) {
			boutonCreate.setEnabled(true);
			boutonJoin.setEnabled(true);
			
			Ipvalue = textIP.getText();
			PortValueString = textPort.getText();
			PortValue = Integer.parseInt(PortValueString);
			
			s.connect(Ipvalue, PortValue);
			s.open(textPseudo.getText());
			
			panel2.remove(textIP);
			panel2.remove(textPort);
			panel2.remove(labelIP);
			panel2.remove(labelPort);			
			panel3.remove(boutonValidateCreate);	
		}
	
		if(source == boutonValidateJoin){

			panelscroll.setVisible(true);
			boutonCreate.setEnabled(true);
			boutonJoin.setEnabled(true);
			
			boutonCreate.setEnabled(false);
			boutonJoin.setEnabled(false);
			
			Ipvalue = textIP.getText();			
			PortValueString = textPort.getText();
			PortValue = Integer.parseInt(PortValueString);
			clientPseudo = textPseudo.getText();			

			c1 = new Client(clientPseudo);
			c1.connect(Ipvalue,PortValue);
			
			panel2.remove(textIP);
			panel2.remove(textPort);
			panel2.remove(labelIP);
			panel2.remove(labelPort);
			panel2.remove(labelPseudo);
			panel2.remove(textPseudo);
			panel3.remove(boutonValidateJoin);
			
			panel2.add(boutonQuitter);
			panel3.add(labelMessage);
			panel3.add(textMessage);
			panel3.add(boutonEnvoyerMessage);
			
			text.removeAll();
			readDatabase();
		}
		
		if(source == boutonEnvoyerMessage){
			text.removeAll();
			Message = textMessage.getText();
			Message msg1 = new Message(c1.getPseudo(), Message);
			c1.msg(msg1);	
			textMessage.setText("");
		}
		
		if(source == boutonQuitter){
			boutonCreate.setEnabled(true);
			boutonJoin.setEnabled(true);
			panelscroll.setVisible(false);
			
			panel2.remove(boutonQuitter);
			panel3.remove(textMessage);
			panel3.remove(boutonEnvoyerMessage);
			panel3.remove(labelMessage);
		}
		validate();
	}
}
