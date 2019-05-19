/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sic;

import static sic.Assembler.labels;


public class OperandHandler {
    public static String A;
    
    
public static boolean isRegisterOperation(Instruction ins){
	String mnemonic = ins.getMnemonic().toLowerCase().trim();
	
	if(mnemonic.equals("rmo"))
		return true;
	if(mnemonic.equals("addr"))
		return true;
	if(mnemonic.equals("subr"))
		return true;
	if(mnemonic.equals("compr"))
		return true;
	if(mnemonic.equals("tixr"))
		return true;
	return false;
}
public static boolean isTwoRegisters(Instruction ins){
        
        
	if(isRegisterOperation(ins) && !isSingleRegister(ins)) {
		return true;
	}
	return false;
	
}
public static boolean isSingleRegister(Instruction ins){
    if(ins.getMnemonic().trim().equalsIgnoreCase("tixr"))
        return true;
    return false;
}
public static void twoRegisters(Instruction ins){
	
	String operand = ins.getOperand().trim().toLowerCase();
	
        if(operand.length()<3){ // operand in form of A,X
            ins.setError(true);
            ins.appendErrorMessage("\t\t****Error: Operand too Short");
        }else if(operand.length()>3){
            ins.setError(true);
            ins.appendErrorMessage("\t\t****Error: Operand too long");
        }else{
            String []Rs = operand.split(",");
            char r1 =Rs[0].charAt(0);
            char r2 =Rs[1].charAt(0);
            if(!Assembler.RegisterMap.containsKey(r1) ||!Assembler.RegisterMap.containsKey(r2)){
                ins.setError(true);
                ins.appendErrorMessage("\t\t****Error: Unknown Register");
            }
            ins.setR1(Rs[0].toLowerCase());

            ins.setR2(Rs[1].toLowerCase());
        }
	
	
    }

public static void singleRegister(Instruction ins){
	if(isRegisterOperation(ins) && !isTwoRegisters(ins) &&ins.getMnemonic().trim().toLowerCase().equals("tixr"))
	{
           String operand = ins.getOperand().trim().toLowerCase();
           if(operand.length()>1){
               ins.appendErrorMessage("\t\t****Error: Extra Characters at end of statement");
           }else if(!Assembler.RegisterMap.containsKey(operand.charAt(0))){
               ins.appendErrorMessage("\t\t****Error: Unknown Register");
           }
            ins.setR1(operand);
                
	}
	
}
public static boolean isHexaDecimal(String test){
    test = test.toLowerCase();
    for(int i = 0;i<test.length() ; i++){
        if(test.charAt(i)>='0' && test.charAt(i)<='9')
            continue;
        if(test.charAt(i)>='a' && test.charAt(i)<='f')
            continue;
        return false;
    }
    return true;
}
public static boolean isNumericString(String test){
    boolean negative = false;
    if(test.charAt(0)=='-')
        negative = true;
    for(int i = 0;i<test.length();i++){
        if(negative)
        {
            negative = false;
            continue;
        }
        if(test.charAt(i)<'0' || test.charAt(i)>'9' )
            return false;
            
    }
    return true;
    
}

public static boolean simpleExpression(Instruction ins){
    String operand = ins.getOperand().trim();
    if(operand.contains("+") || operand.contains("-") ||operand.contains("*")||operand.contains("/"))// when called get memoryInstructions and added to the labels location counter
   {
       int offset = 0;
       
       char sign ='+';
       int position =0;
       position = operand.lastIndexOf("+");
       if(position ==-1){
           position = operand.lastIndexOf("-");
           sign = '-'; 
       }
       if(position == -1){
           position = operand.lastIndexOf("*");
           sign ='*';
       }
       if(position ==-1){
           position = operand.lastIndexOf("/");
           sign = '/';
       }
       ins.setExpressionType(sign);
       String first = operand.substring(0,position).toLowerCase().trim();
       String second = operand.substring(position+1).trim().toLowerCase(); 
       
       ins.setExpression1(first);
       if(isNumericString(second)){
       offset = Integer.parseInt(second); // Parse works for +123  or -123
       ins.setOffset(offset);
       ins.setExpression2(second);
       
       }else{
           ins.setExpression2(second);
       }
   //}else if(){ 
   
       return true;
   }
    return false;
}


public static void memoryInstructions(Instruction ins){
   String operand =  ins.getOperand().trim();
   
   if(!simpleExpression(ins)){ // other memory operands
       
       if((operand.contains("x")||operand.contains("X")) &&operand.contains(",") ){
           ins.setIndexing(true);
           operand = operand.split(",")[0];          //set operand to be label in case of indexing
           ins.setOperand(operand);
       }else if(operand.contains("@")){
           ins.setIndirect(true);
           operand = operand.substring(1);
           
       }else if(operand.contains("#")){
           ins.setImmediate(true);
           
       }else if(operand.contains("=")){
           // do something with literals
       }
   }
       
    
}
public static void memoryErrors(Instruction ins){ //Pass1
                                int flag=0;
	    			int ff=0;
		    		String operand=ins.getOperand().trim();
                                if(operand.equals(""))
                                    return;
                               
		    		if(operand.charAt(0)=='@'||operand.charAt(0)=='#'){
                                        char prefix = operand.charAt(0);
                                        if(prefix =='@'){
                                            ins.setIndirect(true);
                                        }else ins.setImmediate(true);
		    			operand=operand.substring(1,operand.length());
                                        if(operand.length()==0){
                                            ins.appendErrorMessage("\t\t****Error: Operand Missing ");
                                        }
                                        else if(operand.contains("@")||operand.contains("#")){
                                            ins.appendErrorMessage("\t\t****Error: Only one addressing mode is allowed ");
                                        }
                                }
                                
                                if(operand.contains(",") &&(operand.contains("x")||operand.contains("X"))) {
                                    int idx = operand.indexOf(',');
                                    if(operand.charAt(operand.length()-1)!='x'&&operand.charAt(operand.length()-1)!='X')
                                    {
                                        ins.appendErrorMessage("\t\t****Error: wrong form for indexing , must be in form of :address,x ");
                                        String address = operand.substring(0, idx).toLowerCase();
                                        if(isNumericString(address)==false){
                                            if(SymbolTable.SYMTBL.containsKey(address)==false && Instruction.PASS ==2){
                                                ins.appendPass2Error("\t\t****Error: undefined symbol");
                                            }
                                        }
                                        
                                    }else{
                                        ins.setIndexing(true);
                                    }
                                }
		    		
		    		
		    		else if(flag==0&&ff==1) {
		    			try {
		    	            Double num = Double.parseDouble(operand);
		    	        } catch (NumberFormatException e) {
		    	        	ins.setError(true);
			    			ins.appendErrorMessage("\t\t****Error: Problem with operand");
                                                System.out.println("Number Exception");
		    	        }
		    		}
}
public static void findErrors(Instruction ins){
    if(isRegisterOperation(ins))
    {   
        
        ins.setRegisterOperation(true);
        if(isTwoRegisters(ins)){
            twoRegisters(ins);
        }
        else {
            singleRegister(ins);
        }
    }else{ // non register
        memoryErrors(ins);
        
        
        
    }
}


    
}
