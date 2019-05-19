package sic;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class GUItext extends JPanel{

		private JTextPane txt2;
	    private JTextArea txt1;
	    private JPanel panel;
	    private JFrame frame;
	    public String s,fl;

	    public GUItext(String s,String fl) throws IOException{
	    	this.s=s;
	        this.fl=fl;
	    	createGUI();
	        books();
	        System.out.println(fl);
	        frame.add(panel); 
	        frame.setVisible(true);
	    }

	    public void createGUI(){ 
	        frame = new JFrame();
	        frame.setTitle(s);
	        frame.setSize(730, 500);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        panel = new JPanel();
	        panel.setLayout(new GridBagLayout());
	        panel.setBounds(10, 10, 10, 10);
	        panel.setBorder(BorderFactory.createLineBorder (Color.decode("#1854A2"), 2));
	        frame.add(panel);
	    }


	    public void books() throws IOException{
	        String line;
//	        LineNumberReader lnr = new LineNumberReader(new FileReader(new File("books2.txt")));
//	        while((line = lnr.readLine()) != null){
//	            result += line;
//	        }
//	       
	        FileReader fr = new FileReader(fl);
	        BufferedReader reader = new BufferedReader(fr);
	        txt1 = new JTextArea();
	        while ((line = reader.readLine()) != null)
	        txt1.append(line + "\n");
	        panel.add(txt1);
	    }
	    
}
