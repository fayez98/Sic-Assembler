/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.ArrayList;


public class Assembler {
    
    static int sa=-1;
    static ArrayList<Instruction> Instructions = new ArrayList<Instruction>();
    static ArrayList<Symb> labels=new ArrayList<Symb>();
    /**
     * @param args the command line arguments
     */
    //1234567890123456789012345678901
    //label 1:8 , 9 unused
    //10 ;15 ,mnemonic,16:17 unused, 18:35 operand,36:66 comment
    public static ArrayList<String> readFile(String filename){
        BufferedReader reader = null;
        ArrayList<String> List = new ArrayList<String>();
        try {
            File file = new File(filename);
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();//lower case to process easier
                line = " "+line;
                List.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return List;
    }
    public static void processInput(ArrayList<String> lines,String output){
        for(int i = 0 ; i < lines.size();i++){
            
            String []tokens = lines.get(i).split(" ");
            for(int j = 0;j<tokens.length ;j++){
                // deal with each field H
            }
        }
    }
    public static void processFixedFormat(ArrayList<String> lines){// uses substring for each line to get specific field,, and remove extra spaces.
        int beg = -1;
        for(int i = 0 ; i < lines.size();i++){
            String line = lines.get(i);
            Instruction ins = new Instruction(line);
            
            if(!ins.isCommentLine() && beg==-1)
                beg = i;
            if(beg == i)
            {
                if(!ins.getMnemonic().trim().equalsIgnoreCase("start")){
                    ins.error = true;
                    ins.appendErrorMessage("START Directive not found!");
                }
                sa=Integer.parseInt(ins.getOperand(),16);
                ins.setAddress(sa);
            }
            if(beg!=i&&!ins.isCommentLine()) {
            	ins.setAddress(sa);
            	if(!ins.isDirective()) {
            		sa+=ins.getFormat();
            	}
            	else {
            		if(ins.getMnemonic().trim().equalsIgnoreCase("resw"))
						sa+=Integer.parseInt(ins.getOperand())*3;
            		else if(ins.getMnemonic().trim().equalsIgnoreCase("resb"))
            			sa+=Integer.parseInt(ins.getOperand());
            		else if(ins.getMnemonic().trim().equalsIgnoreCase("byte")) {
            			if(ins.getOperand().charAt(0)=='c'||ins.getOperand().charAt(0)=='C')
            				sa+=ins.getOperand().length()-3;
            			else if(ins.getOperand().charAt(0)=='x'||ins.getOperand().charAt(0)=='X') {
            				String s=ins.getOperand().trim();
            				s=s.substring(2,s.length()-1);
            				if(s.length()%2==0) {
            					sa+=s.length()/2;
            				}
            				else {
            					ins.setError(true);
            					ins.setErrorMessage("\t\t****Error: Not a hexadecimal String");
            				}
            				try {
            					int x=Integer.parseInt(s,16);
            				}
            				catch(Exception e){
            					ins.setError(true);
            					ins.setErrorMessage("\t\t****Error: Not a hexadecimal String");
            				}
            			}
            			else
            				sa++;
            		}
            		else if(ins.getMnemonic().trim().equalsIgnoreCase("word")) {
            			int c=1;
            			for(int j=0;j<ins.getOperand().length();j++) {
            				if(ins.getOperand().lastIndexOf(j)==',')
            					c++;
            			}
            			sa+=3*c;
            		}
            		else if(ins.getMnemonic().trim().equalsIgnoreCase("org")) 
            			sa+=3;
            	}
            }
            else if(ins.isCommentLine()) {
            	ins.setAddress(sa);
            }
            if(!ins.isCommentLine()) {
            	ins.checkError(Instructions);
            	if(ins.hasLabel()) {
            		if(ins.getMnemonic().trim().equalsIgnoreCase("org")||ins.getMnemonic().trim().equalsIgnoreCase("end")) {
        				ins.setError(true);
		        		ins.setErrorMessage("\t\t****Error: This statment can't have a label");
        			}
            		labels.add(new Symb(ins.getLabel().trim(),ins.getAddress()));
            		}
            	}
            Instructions.add(ins); // when you process an operation  
        }
    }
    public static void symtb() {
    	for(int j=0;j<labels.size();j++) {
    		System.out.println("\t"+labels.get(j).getLabel()+"\t"+labels.get(j).getAddress());
    	}
    	opch(Instructions);
    	System.out.println("Line no.\tAddress\tLabel\t\tMnemonic\tOperands");
    	for(int i=0;i<Instructions.size();i++) {
    		if(!Instructions.get(i).isCommentLine()) {
    			System.out.println((i+1)+"\t\t"+Instructions.get(i).getAddress()+"\t"+Instructions.get(i).getLabel()+"\t"+Instructions.get(i).getMnemonic()+"\t\t"+Instructions.get(i).getOperand());
    			if(Instructions.get(i).isError())
    			System.out.println(Instructions.get(i).getErrorMessage());
    		}
    			else 
    			System.out.println((i+1)+"\t\t"+Instructions.get(i).getAddress()+"\t"+Instructions.get(i).getComment());	
    	}
    	if(!Instructions.get(Instructions.size()-1).getMnemonic().trim().equalsIgnoreCase("end"))
    		System.out.println("\t\t****Error: Missing End Statment");
    }
    public static void opch(ArrayList<Instruction> in) {
    	String[] reg=new String[] {"A","X","L","B","S","T","F","PC","SW"};
    	for(int i=0;i<in.size();i++) {
    		if(!in.get(i).isCommentLine()) {
	    		String op=in.get(i).getOperand().trim();
	    		if(op.charAt(0)=='*') {
	    			if(op.equalsIgnoreCase("*"));
	            	else if(op.charAt(0)=='*') {
	            		if(op.charAt(1)=='+'||op.charAt(1)=='-') {
	            			try {
	            				int x=Integer.parseInt(op.substring(2,op.length()));
	            			}
	            			catch(Exception e) {
	            				in.get(i).setError(true);
	    		    			in.get(i).setErrorMessage("\t\t****Error: Problem with operand");
	            			}
	            		}
	            		else {
	            			in.get(i).setError(true);
			    			in.get(i).setErrorMessage("\t\t****Error: Problem with operand");
	            		}
	            	}
	    		}
	    		else if(!in.get(i).isCommentLine()&&!in.get(i).isDirective()){
	    			int flag=0;
	    			int ff=0;
		    		String s=in.get(i).getOperand().trim();
		    		if(s.charAt(0)=='@')
		    			s=s.substring(1,s.length());
		    		for(int j=0;j<s.length();j++) {
		    			if(s.charAt(j)==',')
		    				if(s.charAt(j+1)=='x'||s.charAt(j+1)=='X') {
		    					String v=s.substring(0,j-1);
		    					s=v.concat(s.substring(j+2,s.length()));
		    					in.get(i).setIndexing(true);
		    				}
		    		}
		    		for(int k=0;k<9;k++)
		    			if(s.equalsIgnoreCase(reg[k])){
		    				flag=1;
		    				break;
		    		}
		    		if(flag==0) {
		    			if(s.charAt(0)=='#') {
		    				ff=1;
		    				s=s.substring(1,s.length());
		    				}
			    		for(int j=0;j<labels.size();j++) {
			    			if(s.equalsIgnoreCase(labels.get(j).getLabel())) {
			    				flag=1;
			    				break;
			    			}
			    		}
		    		}
		    		if(flag==0 && ff==0) {
		    			in.get(i).setError(true);
		    			in.get(i).setErrorMessage("\t\t****Error: Problem with operand");
		    		}
		    		else if(flag==0&&ff==1) {
		    			try {
		    	            Double num = Double.parseDouble(s);
		    	        } catch (NumberFormatException e) {
		    	        	in.get(i).setError(true);
			    			in.get(i).setErrorMessage("\t\t****Error: Problem with operand");
		    	        }
		    		}
	    		}
    		}
    	}
    }

	public static void Objectcode() {
		String name = labels.get(0).getLabel().trim();
		String address = labels.get(0).getAddress().trim();
		int start = Integer.parseInt(labels.get(0).getAddress(),16);
		int end = Integer.parseInt(labels.get(labels.size()-1).getAddress(),16);
		int integerLength = end - start;
		String stringLength = Integer.toHexString(integerLength);



		try
		{
			File file = new File("obj.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			writer.write("H^" + name + "^" + address+"^"+stringLength+"\n");


			for (int i = 0; i < Instructions.size(); i++)
			{
				int form = Instructions.get(i).getFormat();
				switch(form)
				{
					case 1: {

						break;
					}
					case 2:{

						break;
					}
					case 3:{

						break;
					}
					case 4:
					{
						break;
					}
					default:
						break;
				}
			}
			writer.close();
	}
	catch (IOException e)

	{
		e.printStackTrace();
	}
	}

    public static void main(String[] args) {
        // TODO code application logic here
        processFixedFormat(readFile("Code.txt"));
        symtb();
        Objectcode();
    }
    
}