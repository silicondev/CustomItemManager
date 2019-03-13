package io.github.silicondev.customitemmanager;

public enum Operators {
	
	HELP_NEXTPAGE("%nextPage%", ""),
	HELP_PREVPAGE("%prevPage%", ""),
	HELP_CURRENTPAGE("%currentPage%", ""),
	HELP_TOTALPAGE("%totalPage%", "");
	
	String op;
	String ret;
	
	Operators(String op, String ret) {
		this.op = op;
		this.ret = ret;
	}
	
	public void addReturn(String ret) {
		this.ret = ret;
	}
	
	public String getReturn() {
		return this.ret;
	}
	
	public String getOperator() {
		return this.op;
	}
}