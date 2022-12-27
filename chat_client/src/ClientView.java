import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ClientView extends JFrame {
	private static final long serialVersionUID = 1L;
	/*private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private JLabel lblUserName;
	private JTextPane textArea;*/
	
	private static final int BUF_LEN = 128;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private Vector <ChatRoom> ChatRoomVector =new Vector<ChatRoom>();
	
	private JButton ChatRoom;//채팅방 목록 버튼  
	private JButton BtnProfile;  //프로필 목록 버튼  
	private JButton userb;
	private JPanel myprofile;
	private JPanel friendprofile;
	private JLabel friendb;
	
	private Color lightgray=new Color(230,230,230,255);
	private Color palepink=new Color(245, 220, 224);
	private Color palepink2=new Color(230,225,225);
	private Color darkpink=new Color(231,84,128);
	private Color lightpink=new Color(255, 233, 236);
	
	//private List<String> userList=new ArrayList<>(Arrays.asList("user1","user2","user3","user4","user5")); //전체사용자 목록  
	private Vector<String> userList=new Vector<>(Arrays.asList("user1","user2","user3","user4","user5"));
	
	
	private Image userprofile=new ImageIcon("src/userProfile.png").getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
	private ImageIcon userProfile=new ImageIcon(userprofile);//기본 프로필사진(버튼) 
	
	private Image roomicon=new ImageIcon("src/roomIcon.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);//친구리스트 버튼
	private ImageIcon roomIcon=new ImageIcon(roomicon);//채팅방리스트 버튼 
	
	HashMap<String,ImageIcon> profilemap= new HashMap<String,ImageIcon>(){{//초기값 지정
	    put("user1", userProfile);
	    put("user2", userProfile);
	    put("user3", userProfile);
	    put("user4", userProfile);
	    put("user5", userProfile);
	}};
	
	private Image profileicon=new ImageIcon("src/profileIcon.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);//친구리스트 버튼
	private ImageIcon profileIcon=new ImageIcon(profileicon);
	
	//private ImageIcon ori_icon;
	private Frame frame;
	private FileDialog fd;
	
	private Image makeroom=new ImageIcon("src/makeroom.png").getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
	private ImageIcon makeRoom=new ImageIcon(makeroom);
	
	private JCheckBox[] checkFriend;
	
	public String room_id;
	public String userlist="";
	
	public JPanel ChatRoomList=new JPanel();
	
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
	
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private JLabel lblUserName;
	private JTextPane textArea;
	
	private JButton SendEmoji;
	private JButton SendImage;
	
	
	//LocalDate date=LocalDate.now();
	//LocalTime time=LocalTime.now();
	
	public ClientView mainview;
	
	public ClientView(String username, String ip_addr, String port_no) {
		mainview=this;
		UserName = username;
		
		setTitle(username);
		setBounds(100,100,350,445);//+20+20-5
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c=getContentPane();
		c.setLayout(null);
		
		c.setBackground(palepink);
		
		
		JButton BtnProfile=new JButton(profileIcon);
		BtnProfile.setLocation(12,15);
		BtnProfile.setSize(40,40);
		
		JButton ChatRoom=new JButton(roomIcon);
		ChatRoom.setLocation(12,75);
		ChatRoom.setSize(40,40);
		
		myprofile=new JPanel();
		myprofile.setLayout(new GridBagLayout());
		myprofile.setBackground(palepink2);
		myprofile.setLocation(65,32);//+20+10+2
		myprofile.setSize(285,70);	
		
		JLabel text1=new JLabel("My Profile❤");
		text1.setLocation(65,0);
		text1.setSize(100,40);	
		text1.setForeground(darkpink);
		Font font=new Font("Times",Font.BOLD,15);
		text1.setFont(font);
		c.add(text1);
		
		JLabel text2=new JLabel("Friends❤");
		text2.setLocation(65,107);//+20+20-5
		text2.setSize(100,40);	
		text2.setForeground(darkpink);
		text2.setFont(font);
		c.add(text2);
		
		friendprofile=new JPanel();
		friendprofile.setLayout(new GridLayout(4,1));
		friendprofile.setBackground(palepink2);
		friendprofile.setLocation(65,140);//+20+20-5
		friendprofile.setSize(285,280);
		//friendprofile.setSize(285,300);
		
		JButton MakeRoom=new JButton(makeRoom);
		MakeRoom.setLocation(12, 330);
		MakeRoom.setSize(40,40);
		
		//Container ChatRoomList=getContentPane();
		ChatRoomList.setLayout(new GridLayout(6,1));
		ChatRoomList.setBackground(palepink2);
		ChatRoomList.setLocation(67,0);
		ChatRoomList.setSize(285,420);
		
		c.add(BtnProfile);
		c.add(ChatRoom);
		c.add(myprofile);
		c.add(friendprofile);
		
		
		ChatRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					friendprofile.setVisible(false);
					myprofile.setVisible(false);
					text1.setVisible(false);
					text2.setVisible(false);
					c.add(ChatRoomList);
					c.add(MakeRoom);
					ChatRoomList.setVisible(true);
					MakeRoom.setVisible(true);
			}
		});
		
		BtnProfile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					MakeRoom.setVisible(false);
					ChatRoomList.setVisible(false);
					friendprofile.setVisible(true);
					myprofile.setVisible(true);
					text1.setVisible(true);
					text2.setVisible(true);
			}
		});
		
		MakeRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new showFriendList(username);
			}
		});

		for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
			if(username.equals(entry.getKey())){
				UserName=entry.getKey();
				userb=new JButton(entry.getValue());
				JLabel txt = new JLabel(UserName);
				GridBagConstraints gbc=new GridBagConstraints();
				gbc.fill=GridBagConstraints.NONE;
				
				gbc.weightx=0.1;
				myprofile.add(userb,gbc);
				
				gbc.weightx=0.5;
				myprofile.add(txt,gbc);
			}
			else {
					friendb=new JLabel(entry.getValue());
					friendb.setText("    "+entry.getKey());
					friendb.setHorizontalAlignment(SwingConstants.LEFT);
					friendprofile.add(friendb);
			}
		}
		
		userb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame = new Frame("프로필 사진 선택");
				fd = new FileDialog(frame, "frame_image", FileDialog.LOAD);
				fd.setVisible(true);
				ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
				Image IMG = new ImageIcon(fd.getDirectory() + fd.getFile()).getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
				ImageIcon img=new ImageIcon(IMG);
				obcm.setImg(img);
				System.out.println(img);
				SendObject(obcm);
			}
		});
		
		setVisible(true);
		
			

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			ChatMsg obcm = new ChatMsg(UserName, "100", "hello");
			SendObject(obcm);
			
			ListenNetwork net = new ListenNetwork();
			net.start();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//AppendText("connect error");
		}
	}
	
	class showFriendList extends JFrame{
		showFriendList(String username){
			userlist+=username+" ";
			JCheckBox[] checkFriend;
			setBounds(500,100,200,300);
			Container c=getContentPane();
			c.setLayout(new BorderLayout());
		
			c.setBackground(Color.WHITE);
			
			JPanel friendList=new JPanel();
			friendList.setLayout(new GridLayout(4,1));
			friendList.setBackground(palepink2);
			
			checkFriend=new JCheckBox[5];
			
			class MyItemListener implements ItemListener{
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED) {
						if(e.getItem()==checkFriend[0])
							userlist+="user1 ";
						else if(e.getItem()==checkFriend[1])
							userlist+="user2 ";
						else if(e.getItem()==checkFriend[2])
							userlist+="user3 ";
						else if(e.getItem()==checkFriend[3])
							userlist+="user4 ";
						else if(e.getItem()==checkFriend[4])
							userlist+="user5 ";
					}
				}
			}
			
			MyItemListener listener = new MyItemListener();
				
				
			for(int i=0;i<5;i++) {
				if(userList.get(i)!=UserName) {
					checkFriend[i]=new JCheckBox(userList.get(i).toString());
					checkFriend[i].setBorderPainted(true);
					friendList.add(checkFriend[i]);
					checkFriend[i].addItemListener(listener);
					checkFriend[i].setBackground(palepink2);
				}
			}
		
			JLabel label = new JLabel("새로운 채팅");
			label.setHorizontalAlignment(JLabel.CENTER);
			
			JButton buttonOK = new JButton("OK");
			buttonOK.setBackground(palepink);
			
			c.add(label,BorderLayout.NORTH);
			c.add(friendList, BorderLayout.CENTER);
			c.add(buttonOK, BorderLayout.SOUTH);
			
			
			buttonOK.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SendRoomFriendList(username, userlist);
					userlist="";
					dispose();
				}
			});
			
			setVisible(true);
		}		
	}
	
	public void SendRoomFriendList(String username, String msg) {
			ChatMsg obcm = new ChatMsg(username, "500", msg);
			LocalTime time=LocalTime.now();
			obcm.setroom_id(time.toString());
			obcm.setOwner(username);
			mainview.SendObject(obcm);
	}
	
	public void AddChatRoom(ChatMsg cm) { //채팅방 목록에 버튼 추
		String roomid=cm.getroom_id();
		String users=cm.getData();
		//String username=cm.getId();
		//String msg=cm.getuserlist();
		//String owner=cm.getOwner();
		
		ChatRoom cr=new ChatRoom(mainview,roomid,users);
		ChatRoomVector.add(cr);
		
		System.out.println(roomid);
		System.out.println(users);
		
		JPanel userlistb=new JPanel();
		userlistb.setLayout(new BorderLayout());
		LocalDate date=LocalDate.now();
		JLabel roomdate=new JLabel(date.toString());
		roomdate.setFont(new Font("Times",Font.PLAIN,10));
		JLabel userlist=new JLabel(users);
		for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
			if(cm.getOwner().equals(entry.getKey())){
				userlist.setIcon(entry.getValue());
			}
		}
		//userlist.setIcon(profilemap.get);
		userlist.setFont(new Font("Times",Font.PLAIN,15));
		userlistb.setBackground(lightpink);
		userlistb.add(roomdate,BorderLayout.NORTH);
		userlistb.add(userlist,BorderLayout.CENTER);
		userlistb.setBorder(new LineBorder(palepink2));
	
		ChatRoomList.add(userlistb);
		ChatRoomList.revalidate();
		
		userlistb.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ShowChatView(cm);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void ChangeProfile(ChatMsg cm) {
		for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
			if(cm.getId().equals(UserName)&&cm.getId().equals(entry.getKey())){
				userb=new JButton(entry.getValue());
				JLabel txt = new JLabel(UserName);
				GridBagConstraints gbc=new GridBagConstraints();
				gbc.fill=GridBagConstraints.NONE;
				
				gbc.weightx=0.1;
				myprofile.add(userb,gbc);
				
				gbc.weightx=0.5;
				myprofile.add(txt,gbc);
			
			}
			else if(entry.getKey().equals(UserName)&&!cm.getId().equals(entry.getKey())) {
				userb=new JButton(entry.getValue());
				JLabel txt = new JLabel(UserName);
				GridBagConstraints gbc=new GridBagConstraints();
				gbc.fill=GridBagConstraints.NONE;
				
				gbc.weightx=0.1;
				myprofile.add(userb,gbc);
				
				gbc.weightx=0.5;
				myprofile.add(txt,gbc);
			}
			else if(!entry.getKey().equals(UserName)&&cm.getId().equals(entry.getKey())) {
				friendb=new JLabel(entry.getValue());
				friendb.setText("    "+entry.getKey());
				friendb.setHorizontalAlignment(SwingConstants.LEFT);
				friendprofile.add(friendb);
			}
			else if(!entry.getKey().equals(UserName)&&!cm.getId().equals(entry.getKey())){
				friendb=new JLabel(entry.getValue());
				friendb.setText("    "+entry.getKey());
				friendb.setHorizontalAlignment(SwingConstants.LEFT);
				friendprofile.add(friendb);
			}
			setVisible(true);
		}
	}

	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
					} else
						continue;
					switch (cm.getCode()) {
						//UserStatus 따라 현재 활동 중인지 표
						//chatting list 에 있는지 학인해서 추
					case "200": // chat message
						for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
							if(cm.getId().equals(entry.getKey())){
								cm.setProfile(entry.getValue());
							}
						}
						if (cm.getId().equals(UserName))
							AppendMyText(cm); // 내 메세지는 우측에
						else
							AppendText(cm);
						break;
					case "300": 
						myprofile.removeAll();
						friendprofile.removeAll();
						repaint();
						for(int i=0;i<5;i++) {
							if(cm.getId().equals(userList.get(i))) {
								profilemap.replace(cm.getId(),cm.getImg());
							}
						}
						ChangeProfile(cm);
						break;
					case "410":
						for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
							if(cm.getId().equals(entry.getKey())){
								cm.setProfile(entry.getValue());
							}
						}
						if (cm.getId().equals(UserName)) {
							System.out.println("client got emoji");
							AppendMyEmoji(cm); 
						}// 내 메세지는 우측에
						else
							AppendEmoji(cm);
						break;
					case "420":
						for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
							if(cm.getId().equals(entry.getKey())){
								cm.setProfile(entry.getValue());
							}
						}
						if (cm.getId().equals(UserName))
							AppendMyEmoji(cm); // 내 메세지는 우측에
						else
							AppendEmoji(cm);
						break;
					case "430":
						for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
							if(cm.getId().equals(entry.getKey())){
								cm.setProfile(entry.getValue());
							}
						}
						if (cm.getId().equals(UserName))
							AppendMyEmoji(cm); // 내 메세지는 우측에
						else
							AppendEmoji(cm);
						break;
					case "440":
						for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
							if(cm.getId().equals(entry.getKey())){
								cm.setProfile(entry.getValue());
							}
						}
						if (cm.getId().equals(UserName))
							AppendMyEmoji(cm); // 내 메세지는 우측에
						else
							AppendEmoji(cm);
						break;
					case "450":
						for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
							if(cm.getId().equals(entry.getKey())){
								cm.setProfile(entry.getValue());
							}
						}
						if (cm.getId().equals(UserName))
							AppendMyEmoji(cm); // 내 메세지는 우측에
						else
							AppendEmoji(cm);
						break;
					case "460":
						for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
							if(cm.getId().equals(entry.getKey())){
								cm.setProfile(entry.getValue());
							}
						}
						if (cm.getId().equals(UserName))
							AppendMyEmoji(cm); // 내 메세지는 우측에
						else
							AppendEmoji(cm);
						break;
					case "470":
						for(Entry<String, ImageIcon> entry : profilemap.entrySet()) {
							if(cm.getId().equals(entry.getKey())){
								cm.setProfile(entry.getValue());
							}
						}
						if (cm.getId().equals(UserName))
							AppendMyEmoji(cm); // 내 메세지는 우측에
						else
							AppendEmoji(cm);
						break;
					case "500":
						//
					case "510":
						AddChatRoom(cm);
						break;
					}
				} catch (IOException e) {
					//AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
			}
		}
	}

	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}
	
	public void ShowChatView(ChatMsg cm) {
		for(ChatRoom r:ChatRoomVector) {
			if(r.room_id.equals(cm.getroom_id())) {
				System.out.println(cm.getroom_id()+"ShowChatView");
				r.ShowChatView(cm);
			}
		}
	}
	
	public void AppendText(ChatMsg cm) {
		for(ChatRoom r:ChatRoomVector) {
			if(r.room_id.equals(cm.getroom_id())) {
				r.AppendName(cm);
				r.AppendText(cm);
			}
		}
	}
	public void AppendMyText(ChatMsg cm) {
		for(ChatRoom r:ChatRoomVector) {
			if(r.room_id.equals(cm.getroom_id())) {
				r.AppendMyText(cm);
			}
		}
	}
	
	public void AppendEmoji(ChatMsg cm) {
		for(ChatRoom r:ChatRoomVector) {
			if(r.room_id.equals(cm.getroom_id())) {
				r.AppendName(cm);
				r.AppendEmoji(cm);
			}
		}
	}
	public void AppendMyEmoji(ChatMsg cm) {
		for(ChatRoom r:ChatRoomVector) {
			if(r.room_id.equals(cm.getroom_id())) {
				r.AppendMyEmoji(cm);
			}
		}
		System.out.println("Appending Emoji");
	}
	
	public void SendMessage(String msg) {
		try {
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			try {
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) {
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
		}
	}

}
