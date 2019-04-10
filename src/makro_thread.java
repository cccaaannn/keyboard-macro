import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class makro_thread extends Thread {



		
	ArrayList<Integer> key_list = new ArrayList<Integer>();
	ArrayList<Integer> delay_list = new ArrayList<Integer>();
	Robot r;
	int delay_index = 0;


	public makro_thread(ArrayList<Integer> key_list,ArrayList<Integer> delay_list){
	
		this.key_list = key_list;
		this.delay_list = delay_list;
		
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void run(){

		System.out.println("-------------------makcro started------------------------");

		makro_frame.is_running = true;		

			for (int i = 0; i < key_list.size(); i++) {

				
				if(key_list.get(i) == 0) {
					System.out.println(delay_list.get(delay_index));
					System.out.println(delay_index);
					
					try {
						Thread.sleep(delay_list.get(delay_index));
					} catch (InterruptedException e) {
						continue;
					}
					finally {
						delay_index++;
					}				
				}
				else {

				int count = 0;
				for (int j = 0; j <= i; j++) {
					if(key_list.get(i) == key_list.get(j)) {
						count++;
					}
				}
				
				if(count % 2 == 0) {
					r.keyRelease(key_list.get(i));
				}
				else{
					r.keyPress(key_list.get(i));
				}
				}
				


			}		
		
			makro_frame.info_label.setForeground(makro_frame.green);
			makro_frame.info_label.setText("waiting");
			makro_frame.is_running = false;		

	}
}







