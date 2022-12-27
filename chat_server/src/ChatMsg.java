// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.io.Serializable;
import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String date;
	private String room_id;
	private String userlist;
	private String code; 
	private String data;
	String owner;
	ImageIcon profile;
	public ImageIcon img;

	
	public ChatMsg(String id, String code, String msg) {
	this.id = id;
	this.code = code;
	this.data = msg;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getdate() {
		return date;
	}
	
	public void setdate(String date) {
		this.date=date;
	}
	
	public String getData() {
		return data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		this.userlist=userlist;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
	public ImageIcon getImg() {
		return img;
	}
	
	public void setProfile(ImageIcon profile) {
		this.profile = profile;
	}
	public ImageIcon getProfile() {
		return profile;
	}
}