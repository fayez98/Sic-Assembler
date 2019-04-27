import java.util.HashMap;


public class OperationMap {
    
    
    public static HashMap<String, FormatOpcodePair> OperationToFormat;//Mnemonic and format
    
    public static class FormatOpcodePair{
        private int format;
        private byte opcode;
        public  FormatOpcodePair(int format ,int opcode){
            this.format = format;
            this.opcode = (byte)opcode;
        }
        public byte getOpcode(){
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
        
        
        OperationToFormat.put("rmo", new FormatOpcodePair(2, 0xAC));
        OperationToFormat.put("lda",  new FormatOpcodePair(3, 0));
        OperationToFormat.put("+lda",  new FormatOpcodePair(4, 0));
        
        OperationToFormat.put("ldx",  new FormatOpcodePair(3, 0x04));
        OperationToFormat.put("+ldx",  new FormatOpcodePair(4, 0x04));
        
        OperationToFormat.put("lds",  new FormatOpcodePair(3, 0x6C));
        OperationToFormat.put("+lds",  new FormatOpcodePair(4, 0x6C));
        
        OperationToFormat.put("ldt",  new FormatOpcodePair(3, 0x74));
        OperationToFormat.put("+ldt",  new FormatOpcodePair(4, 0x74));
        
        OperationToFormat.put("ldl",  new FormatOpcodePair(3, 0x08));
        OperationToFormat.put("+ldl",  new FormatOpcodePair(4, 0x08));
        
        OperationToFormat.put("ldb",  new FormatOpcodePair(3, 0x68));
        OperationToFormat.put("+ldb",  new FormatOpcodePair(4, 0x68));//Register A,X,S,T,L,B
        
        OperationToFormat.put("sta",  new FormatOpcodePair(3, 0x0C));
        OperationToFormat.put("+sta",  new FormatOpcodePair(4, 0x0C));
        
        OperationToFormat.put("stx",  new FormatOpcodePair(3, 0x10));
        OperationToFormat.put("+stx",  new FormatOpcodePair(4, 0x10));
        
        OperationToFormat.put("sts",  new FormatOpcodePair(3, 0x7C));
        OperationToFormat.put("+sts",  new FormatOpcodePair(4, 0x7C));
        
        OperationToFormat.put("stt",  new FormatOpcodePair(3, 0x84));
        OperationToFormat.put("+stt",  new FormatOpcodePair(4, 0x84));
        
        OperationToFormat.put("stl",  new FormatOpcodePair(3, 0x14));
        OperationToFormat.put("+stl",  new FormatOpcodePair(4, 0x14));
        
        OperationToFormat.put("stb",  new FormatOpcodePair(3, 0x78));
        OperationToFormat.put("+stb",  new FormatOpcodePair(4, 0x78));
        
        OperationToFormat.put("ldch",  new FormatOpcodePair(3, 0x50));
        OperationToFormat.put("+ldch", new FormatOpcodePair(4, 0x50));
        OperationToFormat.put("stch", new FormatOpcodePair(3, 0x54));
        OperationToFormat.put("+stch", new FormatOpcodePair(4, 0x54));
        OperationToFormat.put("add", new FormatOpcodePair(3, 0x18));
        OperationToFormat.put("+add",new FormatOpcodePair(4, 0x18) );
        OperationToFormat.put("sub", new FormatOpcodePair(3, 0x1C));
        OperationToFormat.put("+sub",new FormatOpcodePair(4, 0x1C) );
        OperationToFormat.put("addr", new FormatOpcodePair(2, 0x90));//ADDR IS F 2 NOT 3
        //OperationToFormat.put("+addr",new FormatOpcodePair(4, opcode) );
        
        OperationToFormat.put("subr", new FormatOpcodePair(2, 0x94));
        
        OperationToFormat.put("comp", new FormatOpcodePair(3, 0x28));
        OperationToFormat.put("+comp",new FormatOpcodePair(4, 0x28) );
        
        OperationToFormat.put("compr", new FormatOpcodePair(2, 0xA0));
        
        OperationToFormat.put("j", new FormatOpcodePair(3, 0x3C));
        OperationToFormat.put("+j",new FormatOpcodePair(4, 0x3C) );
        OperationToFormat.put("jeq", new FormatOpcodePair(3, 0x30));
        OperationToFormat.put("+jeq",new FormatOpcodePair(4, 0x30) );
        OperationToFormat.put("jlt", new FormatOpcodePair(3, 0x38));
        OperationToFormat.put("+jlt",new FormatOpcodePair(4, 0x38) );
        OperationToFormat.put("jgt", new FormatOpcodePair(3, 0x34));
        OperationToFormat.put("+jgt",new FormatOpcodePair(4, 0x34) );
        OperationToFormat.put("tix", new FormatOpcodePair(3, 0x2C));
        OperationToFormat.put("+tix",new FormatOpcodePair(4, 0x2C) );
        OperationToFormat.put("tixr", new FormatOpcodePair(2, 0xB8));
        //DIRECTIVE opcode is FF
        OperationToFormat.put("start", new FormatOpcodePair(0, 0xFF));
        OperationToFormat.put("end",new FormatOpcodePair(5, 0xFF));
        OperationToFormat.put("byte",new FormatOpcodePair(6, 0xFF));
        OperationToFormat.put("word",new FormatOpcodePair(7, 0xFF));
        OperationToFormat.put("resb",new FormatOpcodePair(8, 0xFF));
        OperationToFormat.put("resw",new FormatOpcodePair(9, 0xFF));
        OperationToFormat.put("equ", new FormatOpcodePair(10, 0xFF));
        OperationToFormat.put("org",new FormatOpcodePair(11, 0xFF));
        OperationToFormat.put("base", new FormatOpcodePair(12, 0xFF));
                
                
        
        
        
        
        
        
        
        
        
    }
    
    
    public static FormatOpcodePair getPair(String mnemonic){
        if(mnemonic == null)
            return null;
        if(!OperationToFormat.containsKey(mnemonic))
            return null;
        
        return OperationToFormat.get(mnemonic);
    }
}