public class Instruction {//possible class Field with sub classes label mnemonic operand comment,, and Error field to know which field caused the error
    
    String label;
    String mnemonic;
    String operand;//operand field
    String comment;
    String errorMessage;
    String address;
    boolean commentLine = false;
    boolean directive = false;
    boolean error = false;
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
            if(line.charAt(9)!=' ')
                error = true;
            if(line.charAt(16)!=' ')
                error = true;
            if(line.charAt(17)!=' ')
                error = true;
            if(error){
                errorMessage = "Incomplete Instruction (too short)";
                errorCount ++;
            }
            
            //Should Unused fields always beempty or it doesnt matter?            
            if(false)
                error  = true;
            else{
            this.label = line.substring(1,9);
            this.mnemonic = line.substring(10,16);
            this.findFormatandOpcode();
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
            format = f.getFormat();
            opcode = f.getOpcode();
            if(opcode == 0xff)
                directive = true;
            if(format ==-1)
                error=true;
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
        this.error = error;
    }
    
}