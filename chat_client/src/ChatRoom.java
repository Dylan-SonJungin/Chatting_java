import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

class ChatRoom implements Serializable{
	
	public ClientView mainview;
	
	
	private LocalTime time;
	private SimpleDateFormat format;
	
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private JLabel lblUserName;
	private JTextPane textArea;
	
	private JButton SendEmoji;
	private JButton SendImage;
	
	private Image Smile=new ImageIcon("src/smile.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);//친구리스트 버튼
	private ImageIcon smileicon=new ImageIcon(Smile);
	
	private Image Sleepy=new ImageIcon("src/sleepy.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);//친구리스트 버튼
	private ImageIcon sleepyicon=new ImageIcon(Sleepy);
	
	private Image Santa=new ImageIcon("src/santa.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);//친구리스트 버튼
	private ImageIcon santaicon=new ImageIcon(Santa);
	
	private Image Tree=new ImageIcon("src/tree.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);//친구리스트 버튼
	private ImageIcon treeicon=new ImageIcon(Tree);
	
	private Image Heart=new ImageIcon("src/heart.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);//친구리스트 버튼
	private ImageIcon hearticon=new ImageIcon(Heart);
	
	private Image Snow=new ImageIcon("src/snow.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);//친구리스트 버튼
	private ImageIcon snowicon=new ImageIcon(Snow);
	
	ImageIcon new_icon;
	
	private Frame frame;
	private FileDialog fd;
	
	private Color lightgray=new Color(230,230,230,255);
	private Color palepink=new Color(245, 220, 224);
	private Color palepink2=new Color(230,225,225);
	private Color darkpink=new Color(231,84,128);
	private Color lightpink=new Color(255, 233, 236);
	private Color green=new Color(182, 230, 255);
	
	private static final long serialVersionUID = 1L;
	String room_id;
	String userlist;
	String owner;
	
	public ChatRoom(ClientView mainview, String room_id, String userlist) {
		this.mainview=mainview;
		this.room_id = room_id;
		this.userlist = userlist;
	}

	public ClientView getmainview() {
		return mainview;
	}
	public void setmainview(ClientView mainview) {
		this.mainview=mainview;
	}
	
	public String getroom_id() {
		return room_id;
	}
	public void setroom_id(String room_id) {
		this.room_id = room_id;
	}
	public String getuserlist() {
		return userlist;
	}
	public void setuserlist(String userlist) {
		this.userlist = userlist;
	}
	
	public String getowner() {
		return owner;
	}
	public void setowner(String owner) {
		this.owner=owner;
	}
	
	
class ChatView extends JFrame {

		ChatView(ChatMsg cm) {
			setTitle(cm.getId());
			setBounds(100, 100, 350, 515);
			contentPane = new JPanel();
			setContentPane(contentPane);
			contentPane.setBackground(palepink2);
			contentPane.setLayout(null);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(0, 0, 350, 380);
			contentPane.add(scrollPane);
			
			textArea = new JTextPane();
			textArea.setEditable(true);
			textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
			textArea.setBackground(lightpink);
			scrollPane.setViewportView(textArea);

			txtInput = new JTextField();
			txtInput.setBounds(10, 430, 255, 45);
			contentPane.add(txtInput);
			txtInput.setColumns(10);
			
			btnSend= new JButton("Send");
			btnSend.setBounds(270, 430, 70, 45);
			btnSend.setForeground(darkpink);
			btnSend.setFont(new Font("Times",Font.BOLD,13));
			contentPane.add(btnSend);
			
			SendEmoji=new JButton("Emoji");
			SendEmoji.setBounds(10,390,80,40);
			SendEmoji.setForeground(darkpink);
			SendEmoji.setFont(new Font("Times",Font.BOLD,13));
			contentPane.add(SendEmoji);
			
			SendImage=new JButton("Image");
			SendImage.setBounds(100,390,80,40);
			SendImage.setForeground(darkpink);
			SendImage .setFont(new Font("Times",Font.BOLD,13));
			contentPane.add(SendImage);
			
			setVisible(true);

			btnSend.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String msg = null;
					msg = txtInput.getText();
					txtInput.setText("");
					txtInput.requestFocus();
					ChatMsg obcm = new ChatMsg(cm.getId(), "200", msg);
					obcm.setroom_id(cm.getroom_id());
					mainview.SendObject(obcm);
					System.out.println(mainview.toString());
				}
			});
			
			
			SendEmoji.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//System.out.println(mainview.toString());
					new showEmoji(cm);
					//showEmoji show=new showEmoji(cm);
				}
			});
			
			SendImage.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame = new Frame("보낼 사진 선택");
					fd = new FileDialog(frame, "frame_image", FileDialog.LOAD);
					fd.setVisible(true);
					ImageIcon IMG = new ImageIcon(fd.getDirectory() + fd.getFile());
					Image img=IMG.getImage();
					int width, height;
					double ratio;
					width = IMG.getIconWidth();
					height = IMG.getIconHeight();
					if (width > 200 || height > 200) {
						if (width > height) {
							ratio = (double) height / width;
							width = 200;
							height = (int) (width * ratio);
						} else {
							ratio = (double) width / height;
							height = 200;
							width = (int) (height * ratio);
						}
						Image new_img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
						ImageIcon new_icon = new ImageIcon(new_img);
						ChatMsg obcm = new ChatMsg(cm.getId(), "400", "IMG");
						obcm.setroom_id(cm.getroom_id());
						obcm.setImg(new_icon);
						mainview.SendObject(obcm);
					}
				}
			});
			
		}
	}

	public void ShowChatView(ChatMsg cm) {
		new ChatView(cm);
	}
	
	class showEmoji extends JFrame{
		
		showEmoji(ChatMsg cm){
			setBounds(500,100,300,228);
			Container c=getContentPane();
			c.setLayout(null);
		
			c.setBackground(palepink2);
			
			
			JButton smile = new JButton(smileicon);
			smile.setLocation(0,0);
			smile.setSize(100,100);
			c.add(smile);
			
			JButton heart = new JButton(hearticon);
			heart.setLocation(100,0);
			heart.setSize(100,100);
			c.add(heart);
			
			JButton sleepy = new JButton(sleepyicon);
			sleepy.setLocation(200,0);
			sleepy.setSize(100,100);
			c.add(sleepy);
			
			JButton tree = new JButton(treeicon);
			tree.setLocation(0,100);
			tree.setSize(100,100);
			c.add(tree);
			
			JButton snow = new JButton(snowicon);
			snow.setLocation(100,100);
			snow.setSize(100,100);
			c.add(snow);
			
			JButton santa = new JButton(santaicon);
			santa.setLocation(200,100);
			santa.setSize(100,100);
			c.add(santa);
			
			smile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(cm.getId(), "400", "smile");
					obcm.setroom_id(cm.getroom_id());
					obcm.setImg(smileicon);
					mainview.SendObject(obcm);
					dispose();
				}
			});
			sleepy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(cm.getId(), "400", "sleepy");
					obcm.setroom_id(cm.getroom_id());
					obcm.setImg(sleepyicon);
					mainview.SendObject(obcm);
					dispose();
				}
			});
			heart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(cm.getId(), "400", "heart");
					obcm.setroom_id(cm.getroom_id());
					obcm.setImg(hearticon);
					mainview.SendObject(obcm);
					dispose();
				}
			});
			santa.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(cm.getId(), "400", "santa");
					obcm.setroom_id(cm.getroom_id());
					obcm.setImg(santaicon);
					mainview.SendObject(obcm);
					dispose();
				}
			});
			tree.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(cm.getId(), "400", "tree");
					obcm.setroom_id(cm.getroom_id());
					obcm.setImg(treeicon);
					mainview.SendObject(obcm);
					dispose();
				}
			});
			snow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(cm.getId(), "400", "snow");
					obcm.setroom_id(cm.getroom_id());
					obcm.setImg(snowicon);
					mainview.SendObject(obcm);
					dispose();
				}
			});
			
			setVisible(true);
		}		
	}

	public void AppendText(ChatMsg cm) {
		String msg = cm.getData().trim(); // 앞뒤 blank와 \n을 제거한다.
		
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setBackground(left, Color.white);
		StyleConstants.setFontSize(left, 18);
	    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(),msg+"\n\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
	}
	
	public void AppendMyText(ChatMsg cm) {
		String msg = cm.getData().trim(); // 앞뒤 blank와 \n을 제거한다.	
		StyledDocument doc=textArea.getStyledDocument();
		SimpleAttributeSet right=new SimpleAttributeSet();
		StyleConstants.setAlignment(right,StyleConstants.ALIGN_RIGHT);
		StyleConstants.setBackground(right, green);
		StyleConstants.setFontSize(right, 18);
		doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(), msg+"\n\n", right);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
	}
	
	public void AppendName(ChatMsg cm) {
		Date today = new Date();
		
		format = new SimpleDateFormat("hh:mm:ss a");
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setFontSize(left, 11);
	    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), "["+cm.getId()+"]\n"+format.format(today)+"\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image Profile=cm.getProfile().getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon profile=new ImageIcon(Profile);
		
		textArea.insertIcon(profile);
		
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
	}
	
	public void AppendEmoji(ChatMsg cm) {
		String usr = cm.getId().trim(); // 앞뒤 blank와 \n을 제거한다.
		//AppendName();
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setBackground(left, Color.white);
	    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), "\n\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textArea.insertIcon(cm.getImg());
		int len2 = textArea.getDocument().getLength();
		textArea.setCaretPosition(len2);
	}

	public void AppendMyEmoji(ChatMsg cm) {
		String usr = cm.getData().trim(); // 앞뒤 blank와 \n을 제거한다.	
		StyledDocument doc=textArea.getStyledDocument();
		SimpleAttributeSet right=new SimpleAttributeSet();
		StyleConstants.setAlignment(right,StyleConstants.ALIGN_RIGHT);
		StyleConstants.setBackground(right, Color.yellow);
		doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(),"\n\n", right);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textArea.insertIcon(cm.getImg());
		int len2 = textArea.getDocument().getLength();
		textArea.setCaretPosition(len2);
	}
}