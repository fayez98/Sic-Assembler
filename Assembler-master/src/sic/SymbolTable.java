/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sic;

import java.util.HashMap;



abstract public class SymbolTable {
    public static HashMap<String,String> SYMTBL;
    private static String startLabel ="";
    static {
        
        SYMTBL = new HashMap<String,String>();
    }

   
    public static void setStartLabel(String startLabel) {
        SymbolTable.startLabel = startLabel;
    }
    
    public static void showTable(){
        System.out.println("Name\t\t\tVALUE");
        System.out.println("-----------------------------");
        SYMTBL.remove(startLabel); // label besides start shouldnt be shown
        
        SYMTBL.forEach((k, v) -> System.out.println(k + "\t\t\t" + (v )));
    }
    public static String TableText(){
        String text ="Name\t\t\tVALUE"+'\n';
        text = text + "-----------------------------\n";
        java.util.Iterator it = ((java.util.Map)SYMTBL).entrySet().iterator();
        while(it.hasNext()){
            java.util.Map.Entry pair = (java.util.Map.Entry)it.next();
            
            text = text +""+pair.getKey() + "\t\t\t" + pair.getValue()+"\n";
        }
        
        return text;
    }
}
