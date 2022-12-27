import java.io.Serializable;

class ChatRoom implements Serializable {
	private static final long serialVersionUID = 1L;
	String room_id;
	String userlist;
	String owner;

	public ChatRoom(String room_id, String userlist) {
		this.room_id = room_id;
		this.userlist = userlist;
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
}