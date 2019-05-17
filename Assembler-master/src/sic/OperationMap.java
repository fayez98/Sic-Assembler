package sic;
import java.util.HashMap;


public class OperationMap {
    
    
    public static HashMap<String, FormatOpcodePair> OperationToFormat;//Mnemonic and format
    
    public static class FormatOpcodePair{
        private int format;
        private String opcode;
        public  FormatOpcodePair(int format ,String opcode){
            this.format = format;
            this.opcode = opcode;
        }
        public String getOpcode(){
            return this.opcode;
        }
        public int getFormat(){
            return this.format;
        }
        
    }
    
    static{
        OperationToFormat = new HashMap<String,FormatOpcodePair>(); //idea instead of putting jsut the format,, also put machine code for each instruction
        int opcode =0;
        //OperationToFormat.put("hello", new )
        
//                    Register Number
//                    -------- ------
//                        A      0
//                        X      1
//                        L      2
//                        B      3
//                        S      4
//                        T      5
//                        F      6
        
        
        OperationToFormat.put("rmo", new FormatOpcodePair(2,"AC"));
        OperationToFormat.put("lda",  new FormatOpcodePair(3,"00"));
        OperationToFormat.put("+lda",  new FormatOpcodePair(4,"00"));
        
        OperationToFormat.put("ldx",  new FormatOpcodePair(3,"04"));
        OperationToFormat.put("+ldx",  new FormatOpcodePair(4,"04"));
        
        OperationToFormat.put("lds",  new FormatOpcodePair(3,"6C"));
        OperationToFormat.put("+lds",  new FormatOpcodePair(4,"6C"));
        
        OperationToFormat.put("ldt",  new FormatOpcodePair(3,"74"));
        OperationToFormat.put("+ldt",  new FormatOpcodePair(4,"74"));
        
        OperationToFormat.put("ldl",  new FormatOpcodePair(3,"08"));
        OperationToFormat.put("+ldl",  new FormatOpcodePair(4,"08"));
        
        OperationToFormat.put("ldb",  new FormatOpcodePair(3,"68"));
        OperationToFormat.put("+ldb",  new FormatOpcodePair(4,"68"));//Register A,X,S,T,L,B
        
        OperationToFormat.put("sta",  new FormatOpcodePair(3,"0C"));
        OperationToFormat.put("+sta",  new FormatOpcodePair(4,"0C"));
        
        OperationToFormat.put("stx",  new FormatOpcodePair(3,"10"));
        OperationToFormat.put("+stx",  new FormatOpcodePair(4,"10"));
        
        OperationToFormat.put("sts",  new FormatOpcodePair(3,"7C"));
        OperationToFormat.put("+sts",  new FormatOpcodePair(4,"7C"));
        
        OperationToFormat.put("stt",  new FormatOpcodePair(3,"84"));
        OperationToFormat.put("+stt",  new FormatOpcodePair(4,"84"));
        
        OperationToFormat.put("stl",  new FormatOpcodePair(3,"14"));
        OperationToFormat.put("+stl",  new FormatOpcodePair(4,"14"));
        
        OperationToFormat.put("stb",  new FormatOpcodePair(3, "78"));
        OperationToFormat.put("+stb",  new FormatOpcodePair(4,"78"));
        
        OperationToFormat.put("ldch",  new FormatOpcodePair(3,"50"));
        OperationToFormat.put("+ldch", new FormatOpcodePair(4,"50"));
        OperationToFormat.put("stch", new FormatOpcodePair(3, "54"));
        OperationToFormat.put("+stch", new FormatOpcodePair(4,"54"));
        OperationToFormat.put("add", new FormatOpcodePair(3,"18"));
        OperationToFormat.put("+add",new FormatOpcodePair(4,"18") );
        OperationToFormat.put("sub", new FormatOpcodePair(3,"1C"));
        OperationToFormat.put("+sub",new FormatOpcodePair(4,"1C") );
        OperationToFormat.put("addr", new FormatOpcodePair(2,"90"));//ADDR IS F 2 NOT 3
        //OperationToFormat.put("+addr",new FormatOpcodePair(4, opcode) );
        
        OperationToFormat.put("subr", new FormatOpcodePair(2,"94"));
        
        OperationToFormat.put("comp", new FormatOpcodePair(3,"28"));
        OperationToFormat.put("+comp",new FormatOpcodePair(4,"28") );
        
        OperationToFormat.put("compr", new FormatOpcodePair(2,"A0"));
        
        OperationToFormat.put("j", new FormatOpcodePair(3,"3C"));
        OperationToFormat.put("+j",new FormatOpcodePair(4,"3C") );
        OperationToFormat.put("jeq", new FormatOpcodePair(3,"30"));
        OperationToFormat.put("+jeq",new FormatOpcodePair(4,"30") );
        OperationToFormat.put("jlt", new FormatOpcodePair(3,"38"));
        OperationToFormat.put("+jlt",new FormatOpcodePair(4, "38") );
        OperationToFormat.put("jgt", new FormatOpcodePair(3, "34"));
        OperationToFormat.put("+jgt",new FormatOpcodePair(4, "34") );
        OperationToFormat.put("tix", new FormatOpcodePair(3, "2C"));
        OperationToFormat.put("+tix",new FormatOpcodePair(4, "2C") );
        OperationToFormat.put("tixr", new FormatOpcodePair(2, "B8"));
        //DIRECTIVE opcode is FF
        OperationToFormat.put("start", new FormatOpcodePair(0, "FF"));
        OperationToFormat.put("end",new FormatOpcodePair(5, "FF"));
        OperationToFormat.put("byte",new FormatOpcodePair(6, "FF"));
        OperationToFormat.put("word",new FormatOpcodePair(7, "FF"));
        OperationToFormat.put("resb",new FormatOpcodePair(8, "FF"));
        OperationToFormat.put("resw",new FormatOpcodePair(9, "FF"));
        OperationToFormat.put("equ", new FormatOpcodePair(10, "FF"));
        OperationToFormat.put("org",new FormatOpcodePair(11, "FF"));
        OperationToFormat.put("base", new FormatOpcodePair(12, "FF"));
                
                
        
        
        
        
        
        
        
        
        
    }
    
    
    public static FormatOpcodePair getPair(String mnemonic){
        if(mnemonic == null)
            return null;
        mnemonic = mnemonic.toLowerCase();
        if(!OperationToFormat.containsKey(mnemonic))
            return null;
        
        return OperationToFormat.get(mnemonic);
    }
}