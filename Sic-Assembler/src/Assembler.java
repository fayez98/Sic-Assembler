/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Assembler {
    static HashMap<Character,Integer> RegisterMap;
    static int sa=-1;
    static ArrayList<Instruction> Instructions = new ArrayList<Instruction>();
    static ArrayList<Symb> labels;
    static{
        labels=new ArrayList<>();
        RegisterMap  = new HashMap<Character,Integer>();
        RegisterMap.put('a', 0);
        RegisterMap.put('x', 1);
        RegisterMap.put('l', 2);
        RegisterMap.put('b', 3);
        RegisterMap.put('s', 4);
        RegisterMap.put('t', 5);
        //                    Register Number
//                    -------- ------
//                        A      0
//                        X      1
//                        L      2
//                        B      3
//                        S      4
//                        T      5
//                        F      6
        
    }
    
    
    
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
     //           line = line.toLowerCase();//lower case to process easie
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
    public static void processPseudoOperation(Instruction ins){
        
        if(ins.getMnemonic().trim().equalsIgnoreCase("resw")){
                                                if(OperandHandler.isNumericString(ins.getOperand())){
                                                    
                                                    sa+=Integer.parseInt(ins.getOperand())*3;
                                                }else{
                                                    ins.appendErrorMessage("		****Error: Operand must a number"); // by tarek
                                                }
                        }
            		else if(ins.getMnemonic().trim().equalsIgnoreCase("resb")){
            			sa+=Integer.parseInt(ins.getOperand());
                        }
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
                        else if(ins.getMnemonic().trim().equalsIgnoreCase("equ")){
                            OperandHandler.simpleExpression(ins);
                            
                            String ad1=SymbolTable.SYMTBL.get(ins.getExpression1());
                            String ad2 = null;
                            if(OperandHandler.isNumericString(ins.getExpression2())){
                                ad2 = ins.getExpression2();
                            }else
                                ad2=SymbolTable.SYMTBL.get(ins.getExpression2().toLowerCase());
                            if(ad1 ==null || ad2 ==null){
                                
                                ins.appendErrorMessage("\t\t****Error: Undefined symbol in operand");
                                ins.setError(true);
                                
                            }else{
                                //change it to hex
                                int a1 = Integer.valueOf(ad1,16);
                                int a2 = Integer.valueOf(ad2, 16);
                                switch(ins.getExpressionType()){
                                    case '+':{
                                        a1 = a1+a2;
                                        break;}
                                    case '-':{
                                        a1 = a1-a2; break;}
                                    case '*':{
                                        a1 = a1*a2; break;}
                                    case '/':{
                                        a1 = a1/a2; break;
                                    }
                                    
                                }
                                if(a1 <0){
                                    ins.appendPass2Error("\t\t****Error: Value in symbol table can't be negative");
                                    ins.setAddress(0);// make it 0 in case of negative address
                                    ins.setError(true);
                                }
                                else{
                                    ins.setAddress(a1);
                                }
                            }
                            
                        }
        
    }
    public static void processFixedFormat(ArrayList<String> lines){// uses substring for each line to get specific field,, and remove extra spaces.
        int beg = -1;
        for(int i = 0 ; i < lines.size();i++){
            String line = lines.get(i);
            
            if(line.trim().equals(""))
                continue;
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
                SymbolTable.setStartLabel(ins.getLabel().toLowerCase().trim());
            }else{
                Pass1.firstPass(ins);
        }
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
    public static void Literals(Instruction ins){
        String operand = ins.getOperand();
        operand = operand.trim();
        
        if(operand.charAt(0) == '='){
            if(operand.length()<1){
                char type = Character.toLowerCase(operand.charAt(1));
                int start = operand.indexOf('\'');
                int end = operand.lastIndexOf('\'');
                String val = operand.substring(start+1,end);
                switch(type) {
                    case 'w' : { // WORD
                        
                        if(val.contains(" "))
                        {
                            ins.appendErrorMessage("\t\t****Error: This literal cannot contain spaces");
                        }else{
                            if(val.equals("") ==false){
                                if(OperandHandler.isNumericString(val)){
                                    int value = Integer.parseInt(val);
                               
                                    if(value>9999 || value <-9999 ){
                                        ins.appendErrorMessage("\t\t****Error: Literal too large");
                                    }else{
                                           ins.OperandBytes = new Byte[3];                    //get bytes
                                           if(value <0)
                                           ins.OperandBytes[2] = (byte)0xff;
                                           else
                                               ins.OperandBytes[2]=0;
                                           int lowerByte = (value%256);
                                           int middleByte = value/256;
                                           ins.OperandBytes[0] = (byte)lowerByte;
                                           ins.OperandBytes[1] = (byte)middleByte;
                                    }
                                    
                                }else{
                                    ins.appendErrorMessage("\t\t****Error: Literal is a not a number");
                                }

                            }
                        }
                        break;
                    }case 'x':{
                            if( !OperandHandler.isHexaDecimal(val)){
                            ins.appendErrorMessage("\t\t****Error: Not a Hexadecimal literal");
                            
                            }else{
                                int len =  val.length();
                                if(len <7){
                                    int numOfBytes =val.length()/2;
                                    if(len%2 ==1){
                                        val = '0'+val;//put a zeero at beginning
                                        numOfBytes ++;
                                    }
                                    ins.OperandBytes = new Byte[3];
                                    
                                    //11 22 33
                                    for(int i = numOfBytes;i>0;i--){
                                        String bYte = val.charAt(2*i-2)+""+val.charAt(2*i-1);
                                        
                                        ins.OperandBytes[i-1] = Byte.parseByte(bYte, 16);
                                    }
                                }else{
                                    ins.appendErrorMessage("\t\t****Error: Hexadecimal Literal too long");
                                    
                                }
                            }
                        break; 
                    }case 'c':{ // character literals
                        if(val.length()>3){
                            ins.appendErrorMessage("\t\t****Error: Literla too long");
                        }else{
                            while(val.length()!=3){
                                val ='0'+ val; /// might have to reverse the bytes
                            }
                            ins.OperandBytes= new Byte[3];
                            ins.OperandBytes[0] = (byte)(val.charAt(2)-'0');
                            ins.OperandBytes[1] = (byte)(val.charAt(1)-'0');
                            ins.OperandBytes[2] = (byte)(val.charAt(0)-'0');
                         }
                    }
                }
            }else{
               ins.setErrorMessage("");
            }
                    
            
        }
    }
    public static void opch(ArrayList<Instruction> in) {
    	String[] reg=new String[] {"A","X","L","B","S","T","F","PC","SW"};
    	for(int i=0;i<in.size();i++) {
                Instruction ins= in.get(i);
    		if(!in.get(i).isCommentLine()) {
	    		String op=in.get(i).getOperand().trim();
                        if(!op.equals("")) // for instructions without operand
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
	    		else if(!ins.isCommentLine() &&!ins.isDirect()){
	    			OperandHandler.memoryErrors(ins);
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
        
	try{
            File file = new File("obj.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("H^" + name + "^" + address+"^"+stringLength+"\n");

            for (int i = 0; i < Instructions.size(); i++){
		int form = Instructions.get(i).getFormat();
		switch(form){
                    case 1: {

			break;
                    }
                    case 2:{
                                
                        break;
                    }
                    case 3:{

                	break;
                    }
                    case 4:{
                        
			break;
                    }
                    default:{
			
                        break;
                    }
		}
            }
			writer.close();
	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
    }
    public static boolean listFileGenerator(String path){
        boolean noError = true;
        BufferedWriter writer = null;
        try {
            //create a temporary file
            //String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File(path);

            // This will output the full path where the file will be written to...
            
            String text="";
            
            opch(Instructions);
            text+="Line no.\tAddress\tLabel\t\tMnemonic\tOperands"+'\n';
            for(int i=0;i<Instructions.size();i++) {
                    if(!Instructions.get(i).isCommentLine()) {
                            text=text+""+(i+1)+"\t\t"+Instructions.get(i).getAddress()+"\t"+Instructions.get(i).getLabel()+"\t"+Instructions.get(i).getMnemonic()+"\t\t"+Instructions.get(i).getOperand()+'\n';
                            if(Instructions.get(i).isError()){
                                text = text+Instructions.get(i).getErrorMessage()+'\n';
                                noError =false;
                                
                            }
                    }
                            else 
                            text = text+""+(i+1)+"\t\t"+Instructions.get(i).getAddress()+"\t"+Instructions.get(i).getComment()+'\n';	
            }
            if(!Instructions.get(Instructions.size()-1).getMnemonic().trim().equalsIgnoreCase("end")){
                text = text+"\t\t****Error: Missing End Statment" +'\n';
                noError = false;
            }
            text = text + "Symbol Table: \n";
            text = text +SymbolTable.TableText();
            
            
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(text);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
                return false;
            }
        }
        return noError;
        
    }
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        processFixedFormat(readFile("Code.txt"));
        symtb();
        
        SymbolTable.showTable();
        Objectcode();
        if(listFileGenerator("listfile.txt")) // returns true if there are no Errors
            System.out.println("successful");
        else System.out.println("Failed");
        
        String operand = "mm'1234'abcd";
        int start = operand.indexOf('\'');
        int end = operand.lastIndexOf('\'');
        
        System.out.println(operand.substring(start+1,end));
    }
    
}