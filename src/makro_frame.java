import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class makro_frame extends JFrame implements ActionListener,KeyListener, NativeKeyListener {


	//color
	static Color white = new Color(255,255,255);
	static Color light_black = new Color(61,61,61);
	static Color black = new Color(0,0,0);
	static Color green = new Color(108,254,0);
	static Color blue = new Color(0,150,255);
	static Color red = new Color(255,0,0);
	
	Color background_color = light_black;
	Color foreground_color = white;




	//thread for button presses
	makro_thread mt;



	//OLD VERSION UNUSED
	String []keys_str = new String[] {"press    'q'","release 'q'","press    'w'","release 'w'","press    'e'","release 'e'",
			"press    'r'","release 'r'","press    't'","release 't'","press    'y'","release 'y'","press    'u'","release 'u'","press    'i'","release 'i'",
			"press    'o'","release 'o'","press    'p'","release 'p'","press    'a'","release 'a'","press    's'","release 's'","press    'd'","release 'd'",
			"press    'f'","release 'f'","press    'g'","release 'g'","press    'h'","release 'h'","press    'j'","release 'j'","press    'k'","release 'k'",
			"press    'l'","release 'l'","press    'z'","release 'z'","press    'x'","release 'x'","press    'c'","release 'c'","press    'v'","release 'v'",
			"press    'b'","release 'b'","press    'n'","release 'n'","press    'm'","release 'm'","press    '0'","release '0'","press    '1'","release '1'",
			"press    '2'","release '2'","press    '3'","release '3'","press    '4'","release '4'","press    '5'","release '5'","press    '6'","release '6'",
			"press    '7'","release '7'","press    '8'","release '8'","press    '9'","release '9'",
			"press    'ctrl'","release 'ctrl'","press    'shift'","release 'shift'","press    'alt'","release 'alt'","press    'space'","release 'space'",
			"press    'backspace'","release 'backspace'","press    'delete'","release 'delete'","press    'enter'","release 'enter'"};





	String []delays = new String[] {"300000","240000","180000","120000","60000","30000","15000","10000","7500",
			"5000","2500","1000","500","300","150","100","50","30","10","1"};

	String []trigger_keys = new String[] {"Back Quote","Shift","Ctrl","Space","1","2","3","Z","X","C"};

	JComboBox<String> trigger_key_box = new JComboBox<String>(trigger_keys);


	//move frame
	int pX,pY;


	//textarea and scrollpane
	JTextArea keys_area = new JTextArea();

	JScrollPane sp1 = new JScrollPane(keys_area);

	//textfieldes
	JTextField delay_field = new JTextField(); 

	//labels
	static JLabel info_label = new JLabel();




	//buttons
	JButton exit =  new JButton(new ImageIcon(this.getClass().getResource(("delete-icon.png"))));
	JButton info =  new JButton(new ImageIcon(getClass().getResource(("Button-Info-icon.png"))));
	JButton clear =  new JButton(new ImageIcon(getClass().getResource(("1icons8-delete-48.png"))));
	JButton record =  new JButton(new ImageIcon(getClass().getResource(("add-icon.png"))));
	JButton add_delay =  new JButton(new ImageIcon(getClass().getResource(("add-icon.png"))));
	JButton del =  new JButton(new ImageIcon(getClass().getResource(("icons8-clear-symbol-48.png"))));
	JButton minimize =  new JButton(new ImageIcon(getClass().getResource(("math-minus-icon.png"))));
	JButton save =  new JButton("save");
	JButton load =  new JButton("load");


	//checkboxes
	JCheckBox loop_clicks = new JCheckBox("Loop clicks");


	//is running
	public static boolean is_running = false;

	public static boolean is_recording = false;


	//key lists object
	key_lists Key_Lists = new key_lists();







	makro_frame(){


		create_frame();

		labels();
		comboboxes();
		scrollpane();
		textfields();
		buttons();
		move_frame();

		native_key_listener();

	}







	void create_frame() {
		setSize(400,460);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);
		setOpacity(0.7f);
		setVisible(true);
		setAlwaysOnTop(true);
		this.setLayout(null);
		this.getContentPane().setBackground(background_color);
		setFocusable(true);
		addKeyListener(this);
		setLocationRelativeTo(null);
	}








	void comboboxes() {
		add(trigger_key_box);
		trigger_key_box.setBounds(10,410,200,35);
		trigger_key_box.setFont(new Font("arial",Font.BOLD,20));
		trigger_key_box.setBackground(background_color);
		trigger_key_box.setForeground(white);
		trigger_key_box.setFocusable(false);
		trigger_key_box.setSelectedIndex(0);
	}






	void scrollpane() {

		add(sp1);
		sp1.setBounds(10,20,380,300);
		sp1.setBackground(background_color);

		keys_area.setBackground(background_color);
		keys_area.setForeground(foreground_color);
		keys_area.setFont(new Font("arial",Font.BOLD,25));
		keys_area.setCaretColor(foreground_color);
		keys_area.setEditable(false);
		keys_area.setFocusable(false);

	}



	void textfields() {
		add(delay_field);
		delay_field.setBounds(10,370,200,35);
		delay_field.setFont(new Font("arial",Font.BOLD,20));
		delay_field.setFocusable(true);
		delay_field.setBackground(background_color);
		delay_field.setForeground(white);
		delay_field.setCaretColor(white);
		delay_field.setHorizontalAlignment(SwingConstants.CENTER);
		delay_field.setText("1");


	}

	void labels() {
		add(info_label);
		info_label.setBounds(10,325,200,35);
		info_label.setFont(new Font("arial",Font.BOLD,25));
		info_label.setForeground(green);
		info_label.setText("waiting");
		info_label.setHorizontalAlignment(SwingConstants.CENTER);
	}

	void buttons() {

		add(exit);
		exit.setBounds(375,2,17,17);
		exit.addActionListener(this);		
		exit.setFocusable(false);
		exit.setContentAreaFilled(false);
		exit.setBorderPainted(false);

		add(minimize);
		minimize.setBounds(350,2,17,17);
		minimize.addActionListener(this);		
		minimize.setFocusable(false);
		minimize.setContentAreaFilled(false);
		minimize.setBorderPainted(false);

		add(info);
		info.setBounds(325,2,17,17);
		info.addActionListener(this);		
		info.setFocusable(false);
		info.setContentAreaFilled(false);
		info.setBorderPainted(false);

		add(record);
		record.setBounds(220,325,35,35);
		record.addActionListener(this);		
		record.setFocusable(false);
		record.setContentAreaFilled(false);
		record.setForeground(foreground_color);

		add(add_delay);
		add_delay.setBounds(220,370,35,35);
		add_delay.addActionListener(this);		
		add_delay.setFocusable(false);
		add_delay.setContentAreaFilled(false);
		add_delay.setForeground(foreground_color);

		add(clear);
		clear.setBounds(330,325,60,80);
		clear.addActionListener(this);		
		clear.setFocusable(false);
		clear.setContentAreaFilled(false);
		clear.setForeground(foreground_color);

		add(del);
		del.setBounds(260,325,60,80);
		del.addActionListener(this);		
		del.setFocusable(false);
		del.setContentAreaFilled(false);
		del.setForeground(foreground_color);

		add(save);
		save.setBounds(260,415,60,30);
		save.setFont(new Font("arial",Font.BOLD,10));
		save.addActionListener(this);		
		save.setFocusable(false);
		save.setContentAreaFilled(false);
		save.setForeground(foreground_color);

		add(load);
		load.setBounds(330,415,60,30);
		load.setFont(new Font("arial",Font.BOLD,13));
		load.addActionListener(this);		
		load.setFocusable(false);
		load.setContentAreaFilled(false);
		load.setForeground(foreground_color);

	}





	void add_delay() {	

		int delay = 0;
		Double delay2 = 0.0;
		try {
			delay2 = Double.valueOf(delay_field.getText().toString());
			delay2 *= 1000.0;
			delay = (int) Math.round(delay2);
			if(delay > 0 && delay < 200000000) {				
				Key_Lists.key_list_str.add("delay " + delay + " ms");

				Key_Lists.delay_list.add(delay);	

				Key_Lists.key_list.add(0);

				keys_area.setText(Key_Lists.arraylist_tostring());
			}
		}
		catch (Exception e) {
			;
		}
	}

	void start_recording() {

		if(is_recording) {
			delay_field.setFocusable(true);
			toFront();
			requestFocus();
			is_recording = false;

			info_label.setForeground(green);
			info_label.setText("waiting");
		}
		else {
			delay_field.setFocusable(false);
			toFront();
			requestFocus();
			is_recording = true;

			info_label.setForeground(red);
			info_label.setText("recording");
		}	
	}

	void add_keys_by_keylistener(int a,String b) {

		Key_Lists.key_list.add(a);

		int count = 0;
		for (int i = 0; i < Key_Lists.key_list.size(); i++) {
			if(Key_Lists.key_list.get(i) == a) {
				count++;
			}
		}

		if(count % 2 == 0) {
			Key_Lists.key_list_str.add("release " + b);	
		}
		else{
			Key_Lists.key_list_str.add("press " + b);	
		}

		keys_area.setText(Key_Lists.arraylist_tostring());

	}

	void delete_last_key() {

		if(Key_Lists.key_list.size() > 0) {

			if(Key_Lists.key_list.get(Key_Lists.key_list.size()-1) == 0) {
				Key_Lists.delay_list.remove(Key_Lists.delay_list.size()-1);				
			}

			Key_Lists.key_list.remove(Key_Lists.key_list.size()-1);

			Key_Lists.key_list_str.remove(Key_Lists.key_list_str.size()-1);

			keys_area.setText(Key_Lists.arraylist_tostring());

		}
	}

	void clear_key_list() {

		Key_Lists.key_list.clear();

		Key_Lists.delay_list.clear();

		Key_Lists.key_list_str.clear();

		keys_area.setText(Key_Lists.arraylist_tostring());
	}

	void start() {
		if(!is_running && !is_recording) {
			if(Key_Lists.key_list.size() > 0) {

				info_label.setForeground(blue);
				info_label.setText("macro running");

				mt = new makro_thread(Key_Lists.key_list, Key_Lists.delay_list); 
				mt.start();
			}
		}
	}

	void stop() {
		mt.stop();
		is_running = false;
	}





	void save_to_file() {

		//file selector should be on top 
		this.setAlwaysOnTop(false);
		
		/**
		 * chose file with file selector 
		 */
		
		FileDialog fd = new FileDialog(this, "Choose a save directory", FileDialog.SAVE);
		
		//default path is current directory
		fd.setDirectory(System.getProperty("user.dir"));
		fd.setFile("*.cmakro");
		fd.setVisible(true);
		
		
		String filename = fd.getFile();
		String path = fd.getDirectory();
		String file_withpath = path + filename;
		
		if (filename != null) {
			  System.out.println("save path: " + file_withpath);
			  
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file_withpath));

			out.writeObject(Key_Lists);
			
			out.close();
			
			info_label.setForeground(green);
			info_label.setText("file saved :D");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
		this.setAlwaysOnTop(true);
		
	}

	void load_from_file(){

		//file selector should be on top 
		this.setAlwaysOnTop(false);
		
		/**
		 * chose file with file selector 
		 */
		
		FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
		
		//default path is current directory
		fd.setDirectory(System.getProperty("user.dir"));
		fd.setFile("*.cmakro");
		fd.setVisible(true);
		
		
		
		String filename = fd.getFile();
		String path = fd.getDirectory();
		String file_withpath = path + filename;
		
		
		if (filename != null) {
			  System.out.println("load path: " + file_withpath);	
		
			  
			  /**
			   * read object from file 
			   */
				try {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(file_withpath));

					Key_Lists = Key_Lists.copy((key_lists) in.readObject());

					keys_area.setText(Key_Lists.arraylist_tostring());		
					
					in.close();
					
					info_label.setForeground(green);
					info_label.setText("file loaded :D");
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					info_label.setForeground(white);
					info_label.setText("wrong file format");
					System.out.println("io exception");
					//e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
		}
		
		
		this.setAlwaysOnTop(true);
	
	}







	void show_info() {
		//info button, html for bold and dots
		setAlwaysOnTop(false);

		String msg = "<html><ul><li>Shortcuts:<br/>"
				+ "(\") start macro<br/>"
				+ "(esc) exit<br/><br/>"
				+ "<li>Author: <b>Can Kurt</b></ul></html>";

		JLabel label = new JLabel(msg);
		label.setFont(new Font("arial", Font.PLAIN, 15));

		JOptionPane.showMessageDialog(null, label ,"Info", JOptionPane.INFORMATION_MESSAGE);

		setAlwaysOnTop(true);
	}

	void minimize_frame() {
		setExtendedState(JFrame.ICONIFIED);
	}





	void native_key_listener() {
		// Initialze native hook.
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			ex.printStackTrace();

			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(this);

		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}

	void move_frame() {
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent me){
				pX=me.getX();
				pY=me.getY();
			}
		});

		addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent me){
				setLocation(getLocation().x+me.getX()-pX,getLocation().y+me.getY()-pY);
			}
		});
	}















	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exit) {
			System.exit(0);
		}
		if(e.getSource() == record) {
			start_recording();
		}
		if(e.getSource() == add_delay) {
			add_delay();
		}
		if(e.getSource() == clear) {
			clear_key_list();
		}
		if(e.getSource() == del) {
			delete_last_key();
		}
		if(e.getSource() == save) {
			save_to_file();
		}
		if(e.getSource() == load) {
			load_from_file();
		}
		if(e.getSource() == info) {
			show_info();
		}
		if(e.getSource() == minimize) {
			minimize_frame();
		}
	}





	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}

		if(is_recording) {
			add_keys_by_keylistener(e.getKeyCode(),e.getKeyText(e.getKeyCode()));
		}


	}


	@Override
	public void keyReleased(KeyEvent e) {

		if(is_recording) {
			add_keys_by_keylistener(e.getKeyCode(),e.getKeyText(e.getKeyCode()));
		}


	}








	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}






	///////////////////////////////////////////////////native key press functions//////////////////////////////////////////////////////////////////







	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

		if (e.getKeyText(e.getKeyCode()) == trigger_key_box.getSelectedItem().toString()) {
			start();
		}
	}




	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub

	}








}










