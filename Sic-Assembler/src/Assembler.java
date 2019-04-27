/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Assembler {
    
    static int sa=-1;
    static ArrayList<Instruction> Instructions = new ArrayList<Instruction>();
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
            			if(ins.getOperand().charAt(0)!='c'&&ins.getOperand().charAt(0)!='C')
            				sa++;
            			else
            				sa+=ins.getOperand().length()-3;
            		}
            		else if(ins.getMnemonic().trim().equalsIgnoreCase("word")) {
            			int c=1;
            			for(int j=0;j<ins.getOperand().length();j++) {
            				if(ins.getOperand().lastIndexOf(j)==',')
            					c++;
            			}
            			sa+=3*c;
            		}
            	}
            }
            else if(ins.isCommentLine()) {
            	ins.setAddress(sa);
            }
            Instructions.add(ins); // when you process an operation  
        }
    }
    public static void symtb() {
    	System.out.println("Line no.\tAddress\tLabel\t\tMnemonic\tOperands");
    	for(int i=0;i<Instructions.size();i++) {
    		if(!Instructions.get(i).isCommentLine()) 
    			System.out.println((i+1)+"\t\t"+Instructions.get(i).getAddress()+"\t"+Instructions.get(i).getLabel()+"\t"+Instructions.get(i).getMnemonic()+"\t\t"+Instructions.get(i).getOperand());
    		else
    			System.out.println((i+1)+"\t\t"+Instructions.get(i).getAddress()+"\t"+Instructions.get(i).getComment());
    	}
    }
    public static void main(String[] args) {
        // TODO code application logic here
        processFixedFormat(readFile("Code.txt"));
        symtb();
    }
    
}