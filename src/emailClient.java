import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;

public class emailClient extends JFrame {
    JTextField usrName = new JTextField();
    JTextField recipient = new JTextField();
    JTextField subBox = new JTextField();
    JPasswordField passField = new JPasswordField();
    JTextArea messBox = new JTextArea(20,20);
    JComboBox<String> inbox = new JComboBox();

    public emailClient() {
        renderGUI();
    }

    private void renderGUI() {
        setTitle("JavaMail Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(500, 500));

        getContentPane().setLayout(new BorderLayout());
        
        //labels
        JLabel userLabel = new JLabel("Username: ");
		JLabel passLabel  = new JLabel("password");
		JLabel subLabel = new JLabel("Subject: ");
		JLabel toLabel = new JLabel("To: ");
        JLabel messLabel = new JLabel("Message");
        JLabel inboxLabel	= new JLabel("Inbox");
		
        
		//
        //layout
        //top
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(4, 4));
        top.add(userLabel);
        top.add(usrName);
        top.add(passLabel);
        top.add(passField);
        top.add(toLabel);
        top.add(recipient);
        top.add(subLabel);
        top.add(subBox);

              
      

        //message box
        JPanel messPanel = new JPanel();
		messPanel.setLayout(new BoxLayout(messPanel, BoxLayout.PAGE_AXIS));
		messBox.setLineWrap(true);
		messBox.setWrapStyleWord(true);
		JScrollPane scrollpane = new JScrollPane(messBox);
		messPanel.add(messLabel);
		messPanel.add(scrollpane);
        
		//send button
		JPanel button = new JPanel();
        button.setLayout(new BorderLayout());
        JButton sendButton = new JButton("Send");
        button.add(sendButton, BorderLayout.EAST);
        //connect button
        JButton connectButton =new JButton("Connect");
        button.add(connectButton, BorderLayout.WEST);
        
        //inbox
        JPanel inPanel = new JPanel();
        inPanel.setLayout(new BorderLayout());
        inPanel.add(inboxLabel, BorderLayout.WEST);
        inPanel.add(inbox);
        
        //middle pannl
        JPanel mid = new JPanel();
        mid.setLayout(new BorderLayout());
        mid.add(inPanel,BorderLayout.NORTH);
        mid.add(messPanel, BorderLayout.SOUTH);
        
        //page layout
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(mid, BorderLayout.CENTER);
      
        getContentPane().add(button, BorderLayout.SOUTH);
        

       
        //add button action
        sendButton.addActionListener(new EmailListener());
        connectButton.addActionListener(new inboxListener() );

       

    
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                emailClient client = new emailClient();              
                client.setVisible(true);
            }
        });
    }

    class EmailListener implements ActionListener {
        EmailListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        	
        	// reciever
    		String to = recipient.getText();
    		
    		// sender
    		final String from = usrName.getText();
    		final String password = new String(passField.getPassword());
        	
        	//host
        	String host = "outlook.office365.com";
        	
        	
        	//email properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
    		props.put("mail.smtp.starttls.enable", "true");
    		props.put("mail.smtp.host", host);
    		props.put("mail.smtp.port", "25");

    		//create session
            Session session = Session.getDefaultInstance(props);

            try {
              
            	//
            	// create message
            	//
            	
            	//mime message
                Message message = new MimeMessage(session);
                //set from
                message.setFrom(new InternetAddress(from));
                //set to
                message.setRecipient(Message.RecipientType.TO,(new InternetAddress(to)));
                //set subject
                message.setSubject(subBox.getText());
                //the mess
                message.setText(messBox.getText());

                //send email
                Transport.send(message, from, 
                        password);
                
               
                
          
               System.out.println("the email has been sent...");
                
                
            } catch (MessagingException except) {
            	except.printStackTrace();
            }
            
        }
    }

    class inboxListener implements ActionListener {
        inboxListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        	
             		
    		// sender
    		final String from = usrName.getText();
    		final String password = new String(passField.getPassword());
        	
        	//host
        	String host = "outlook.office365.com";
        	
        	//email properties
            Properties props2 = System.getProperties();
            props2.setProperty("mail.store.protocol", "imaps");
          
    		//create session
            Session session2 = Session.getDefaultInstance(props2);

        	
        	 try{
        		 Store store = session2.getStore("imaps");
        	 

              store.connect(host, from, password);

              //create the folder object and open it
              Folder emailFolder = store.getFolder("INBOX");
              emailFolder.open(Folder.READ_ONLY);
              Message[] messages = emailFolder.getMessages();
              for (int i=0; i<25; i++){
            	  //System.out.println("SentOn: " + messages[i].getSentDate() + " Subject: " + messages[i].getSubject());
            	  inbox.addItem(new String(messages[i].getSubject()));;
        	 }
              //close the conection
              emailFolder.close(true);
              store.close();
        	 }
                 catch (MessagingException except) {
                 	except.printStackTrace();
                 } 

      

}}}