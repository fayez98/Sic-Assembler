/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sic;

import static sic.Assembler.Instructions;
import static sic.Assembler.labels;
import static sic.Assembler.processPseudoOperation;
import static sic.Assembler.sa;


public class Pass1 {
    public static void firstPass(Instruction ins){
        if(!ins.isCommentLine()) {
            	ins.setAddress(sa);
            	if(!ins.isDirective()) {
            		ins.setSize(ins.getFormat());
            		sa+=ins.getFormat();
            	}
            	else { //process pseudo operation.
            		      processPseudoOperation(ins);
            	}
            }
            else if(ins.isCommentLine()) {
            	ins.setAddress(sa);
            }
            if(!ins.isCommentLine()) {
            	ins.checkError(Instructions);
                checkFirstPassErrors(ins);
            	if(ins.hasLabel()) {
            		if(ins.getMnemonic().trim().equalsIgnoreCase("org")||ins.getMnemonic().trim().equalsIgnoreCase("end")) {
        				ins.setError(true);
		        		ins.setErrorMessage("\t\t****Error: This statment can't have a label");
        			}
                        
            		labels.add(new Symb(ins.getLabel().trim(),ins.getAddress()));
                        if(SymbolTable.SYMTBL.put(ins.getLabel().trim().toLowerCase(),ins.getAddress())!=null){
                            ins.appendErrorMessage("\t\t****Error: Duplicate Label Definition");
                        }
                }
            }
            
            Instructions.add(ins); // when you process an operation  
    }
    
    public static void checkFirstPassErrors(Instruction ins){
        OperandHandler.findErrors(ins);
    }
}
