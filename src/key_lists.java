import java.io.Serializable;
import java.util.ArrayList;

public class key_lists implements Serializable{

	ArrayList<Integer> key_list = new ArrayList<Integer>();
	ArrayList<Integer> delay_list = new ArrayList<Integer>();
	ArrayList<String> key_list_str = new ArrayList<String>();

	
	public String arraylist_tostring() {
		String  s = "";
		for (int i = 0; i < key_list.size(); i++) {
			s += key_list_str.get(i) + "\n";
		}
		return s;
	}
	
	
	key_lists copy(key_lists new_key_lists) {
		
		this.key_list.equals(new_key_lists.key_list);
		this.delay_list.equals(new_key_lists.delay_list);
		this.key_list_str.equals(new_key_lists.key_list_str);
		
		
		return new_key_lists;
	}
	
	
	
}
