import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.time.LocalDate;
import java.time.LocalTime;

public class JavaObjServer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket;
	private Socket client_socket; 
	private Vector UserVec = new Vector();
	private static final int BUF_LEN = 128;
	
	private String room_id; //방 번호 생성  
	
	LocalDate date=LocalDate.now();
	LocalTime time=LocalTime.now();
	
	private Vector userList=new Vector();
	private Vector <ChatRoom> ChatRoomVector =new Vector<ChatRoom>();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaObjServer frame = new JavaObjServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JavaObjServer() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false);
				txtPortNumber.setEnabled(false);
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) {
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept();
					AppendText("새로운 참가자 from " + client_socket);
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user);
					//System.out.println("uvec add new user");
					new_user.start();
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					//System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getId() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		public String UserName = "";
		public String UserStatus;

		public UserService(Socket client_socket) {
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public void Login() {
			AppendText("새로운 참가자 " + UserName + " 입장.");
			//ChatMsg obcm=new ChatMsg(UserName,"100","LOGIN");
			//WriteOneObject(obcm);
		}

		public void Logout() {
			String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteAll(msg); // 나를 제외한 다른 User들에게 전송
			AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}

		public void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				user.WriteOne(str); //참여중인 모든 사용자에게 msg 전
			}
		}

		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				//System.out.println(user);
				//if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		public void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O")
					user.WriteOne(str);
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
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		public void WriteOne(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); 
			}
		}

		public void WriteOneObject(Object ob) {
			try {
			    oos.writeObject(ob);
			} 
			catch (IOException e) {
				AppendText("oos.writeObject(ob) error");		
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		
		public void MakeChatRoom(ChatRoom cr,ChatMsg cm) {
			ChatMsg obcm=new ChatMsg(cr.getowner(),"510",cr.getuserlist());
			obcm.setuserlist(cr.getuserlist());
			obcm.setroom_id(cr.getroom_id());
			obcm.setOwner(cm.getOwner());
			///System.out.println(cr.getowner()+" 서버에서 보내기 MakeChatRoom");
			WriteOneObject(obcm);
		}
		
		public void SendSmile(ChatMsg cm) {
			ChatMsg obcm=new ChatMsg(cm.getId(),"410","smile");
			obcm.setImg(cm.getImg());
			obcm.setroom_id(cm.getroom_id());
			System.out.println("sendsmile "+cm.getImg().toString());
			WriteAllObject(obcm);
		}
		public void SendSleepy(ChatMsg cm) {
			ChatMsg obcm=new ChatMsg(cm.getId(),"420","sleepy");
			obcm.setImg(cm.getImg());
			obcm.setroom_id(cm.getroom_id());
			WriteAllObject(obcm);
		}
		public void SendHeart(ChatMsg cm) {
			ChatMsg obcm=new ChatMsg(cm.getId(),"430","heart");
			obcm.setImg(cm.getImg());
			obcm.setroom_id(cm.getroom_id());
			WriteAllObject(obcm);
		}
		public void SendSanta(ChatMsg cm) {
			ChatMsg obcm=new ChatMsg(cm.getId(),"440","santa");
			obcm.setImg(cm.getImg());
			obcm.setroom_id(cm.getroom_id());
			WriteAllObject(obcm);
		}
		public void SendSnow(ChatMsg cm) {
			ChatMsg obcm=new ChatMsg(cm.getId(),"450","snow");
			obcm.setImg(cm.getImg());
			obcm.setroom_id(cm.getroom_id());
			WriteAllObject(obcm);
		}
		public void SendTree(ChatMsg cm) {
			ChatMsg obcm=new ChatMsg(cm.getId(),"460","tree");
			obcm.setImg(cm.getImg());
			obcm.setroom_id(cm.getroom_id());
			WriteAllObject(obcm);
		}
		
		public void SendImage(ChatMsg cm) {
			ChatMsg obcm=new ChatMsg(cm.getId(),"470","IMG");
			obcm.setImg(cm.getImg());
			obcm.setroom_id(cm.getroom_id());
			WriteAllObject(obcm);
		}
		
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					if (cm.getCode().matches("100")) {
						UserName = cm.getId();
						UserStatus = "O";
						Login();
					} else if (cm.getCode().matches("200")) {
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
						AppendText(msg);
						WriteAllObject(cm);
					} else if(cm.getCode().matches("300")){
						WriteAllObject(cm);
						System.out.println(cm.getImg());
					}else if (cm.getCode().matches("400")) {
						if(cm.getData().matches("smile")){
							SendSmile(cm);
						}
						if(cm.getData().matches("sleepy")){
							SendSleepy(cm);
						}
						if(cm.getData().matches("heart")){
							SendHeart(cm);
						}
						if(cm.getData().matches("tree")){
							SendTree(cm);
						}
						if(cm.getData().matches("santa")){
							SendSanta(cm);
						}
						if(cm.getData().matches("snow")){
							SendSnow(cm);
						}
						if(cm.getData().matches("IMG")){
							SendImage(cm);
						}
					} else if (cm.getCode().matches("500")) {
						String users=cm.getData();
						String roomid=cm.getroom_id();
						//String owner=cm.getOwner();
						
						AppendText("[SERVER]Made new ChatRoom:\n"+roomid+" "+users);
						
						ChatRoom cr=new ChatRoom(roomid, users);//새로운 ChatRoom 생성 
						ChatRoomVector.add(cr); //ChatRoomVector 로 관리 
						
						String roomusers[]=users.split(" ");
						
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							for(int j=0;j<roomusers.length;j++) {
								if (user.UserName.matches(roomusers[j])) {
									cr.setowner(user.UserName);
									//System.out.println(user.UserName+" 서버에서 방 만들기 ");
									user.MakeChatRoom(cr,cm);
								}
							}
						}
					}else if(cm.getCode().matches("600")){
						Logout();
						break;
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); 
						break;
					} catch (Exception ee) {
						break;
					}
				} 
			} // while
		} // run
	}

}
