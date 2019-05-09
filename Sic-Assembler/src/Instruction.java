import java.util.ArrayList;

public class Instruction {//possible class Field with sub classes label mnemonic operand comment,, and Error field to know which field caused the error
    
    String label;
    String mnemonic;
    String operand;//operand field
    String comment;
    String obc;
    public String errorMessage;
    String address;
    boolean commentLine = false;
    boolean directive = false;
    boolean Label;
    boolean indexing;
    public boolean error = false;
    int errorCount;
    int format = 0;
    byte opcode;
    //label 1:8 , 9 unused
    //10 ;15 ,mnemonic,16:17 unused, 18:35 operand,36:66 comment
    public Instruction(String label, String mnemonic, String operand,String comment) {
        this.errorMessage="";
        this.label = label;
        this.mnemonic = mnemonic;
        this.operand = operand;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public byte getOpcode() {
		return opcode;
	}

	public String getObc() {
		return obc;
	}

	public void setObc(String obc) {
		this.obc = obc;
	}

	public void setOpcode(byte opcode) {
		this.opcode = opcode;
	}

	public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public void setAddress(int add) {
    	this.address=Integer.toHexString(add);
    }
    public boolean isCommentLine() {
        return commentLine;
    }

    public void setCommentLine(boolean commentLine) {
        this.commentLine = commentLine;
    }
    public String getError() {
    	if(error)
    		return "Error";
    	return "no error";
    }
    @Override
    public String toString() {
        if(!commentLine)
            return label+" "+mnemonic+"  "+operand+comment;
        return comment;
    }
    public void appendErrorMessage(String Message){
        if(Message!=null)
            this.errorMessage+="-"+Message;
    }
    public Instruction(String line){// When reading from file an empty character is placed at the beginning so I can start at index 1 
        int len = line.length();
        errorCount =0;
        errorMessage ="";
        if(line.charAt(1) =='.' ){
            commentLine =true;
            comment = line.substring(1) ;
        }else{
        	if(line.charAt(1)==' '&&line.charAt(2)!=' ') {
        		this.error = true;
                this.errorMessage ="\t\t****Error: Missplaced Label";
        	}
            if(line.charAt(10)==' ') {
                this.error = true;
                this.errorMessage ="\t\t****Error: Missing or missplaced menomonic";
            }
            if(line.charAt(18)==' ') {
                this.error = true;
                this.errorMessage="\t\t****Error: Missing or misspalced operand";
                
            }
            //Should Unused fields always beempty or it doesnt matter?            
            if(false)
                error  = true;
            else{
            this.label = line.substring(1,9);
            if(line.charAt(1)!=' ') {
            	this.Label=true;
            }
            else
            	this.Label=false;
            this.mnemonic = line.substring(10,17);
            this.findFormatandOpcode();
            if(this.error&&this.mnemonic.charAt(0)=='+') {
            	String s=this.mnemonic.trim();
            	s=s.substring(1,s.length());
            	OperationMap.FormatOpcodePair f;
                f = OperationMap.getPair(s);
                if(f!=null) {
                format = f.getFormat();
                if(format ==2) {
                    this.error=true;
                    this.errorMessage="\t\t****Error: This Instruction can't be format 4";
                    }
                }
            }
            if(format==0 || format >4)
                directive = true;
            if(len<37)
            	this.operand=line.substring(18,line.length());
            else {
	            this.operand = line.substring(18,36);
	            this.comment = line.substring(37,line.length()-1);
            }
            }
        }
    }
    private void findFormatandOpcode(){
        if(mnemonic!=null){
            OperationMap.FormatOpcodePair f;
            f = OperationMap.getPair(mnemonic.trim());
            if(f!=null) {
            format = f.getFormat();
            opcode = f.getOpcode();
            if(opcode == 0xff)
                directive = true;
            if(format ==-1) {
                this.error=true;
                this.errorMessage="Unrecognized operation code";
                }
            }
            else {
            	this.error=true;
            	this.errorMessage="\t\t****Error: Unrecognized operation code";
            }
        }
        
    }
    public void processPseudoOperation(){
        
        if(directive == false)
            return;
        
        // do what you have to do with directives
    }
    public String getLabel() {
        return label;
    }
    
    public int getFormat() {
    	return format;
    }
    
    public String getAddress() {
    	return address;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getOperand() {
        return operand;
    }
    
    public boolean hasLabel() {
    	return Label;
    }
    public void setOperand(String operand) {
        this.operand = operand;
    }

    public boolean isDirective() {
        return directive;
    }

    public void setDirective(boolean directive) {
        this.directive = directive;
    }

    public boolean isError() {
        return error;
    }
    public void setError(boolean error) {
    	this.error=error;
    }

    public void checkError(ArrayList<Instruction> Ins) {
        for(int i=0;i<Ins.size();i++) {
        	if(Ins.get(i).isCommentLine());
        	else {
        		if(Ins.get(i).hasLabel()) {
        			if(Ins.get(i).getMnemonic().trim().equalsIgnoreCase("org")||Ins.get(i).getMnemonic().trim().equalsIgnoreCase("end")) {
        				this.setError(true);
		        		this.setErrorMessage("\t\t****Error: This statment can't have a label");
        			}
		        	String s=Ins.get(i).getLabel();
		        	if(label.equalsIgnoreCase(s)) {
		        		this.setError(true);
		        		this.setErrorMessage("\t\t****Error: Symbol '"+this.getLabel().trim()+"' already defined");
		        	}
        		}
        	}
        }
        if(this.getMnemonic().trim().equalsIgnoreCase("rsub"))
        	if(this.getOperand().charAt(0)!=' ') {
        		this.setError(true);
    			this.setErrorMessage("\t\t****Error: This statement can’t have an operand");
        	}
    }
    
    public boolean isIndexing() {
		return indexing;
	}

	public void setIndexing(boolean indexing) {
		this.indexing = indexing;
	}

	public void che() {
    	String mn=this.getMnemonic().trim();
        if(mn.equalsIgnoreCase("j")||mn.equalsIgnoreCase("+j")||mn.equalsIgnoreCase("jeq")||mn.equalsIgnoreCase("+jeq")||mn.equalsIgnoreCase("jlt")||mn.equalsIgnoreCase("+jlt")||mn.equalsIgnoreCase("jqt")||mn.equalsIgnoreCase("+jgt")) {
        	String op=this.getOperand().trim();
        	if(op.equalsIgnoreCase("*"))
        		this.error=false;
        	else if(op.charAt(0)=='*') {
        		if(op.charAt(1)=='+'||op.charAt(1)=='-') {
        			try {
        				int x=Integer.parseInt(op.substring(2,op.length()));
        				this.error=false;
        			}
        			catch(Exception e) {	
        			}
        		}
        	}
        }
    }
}