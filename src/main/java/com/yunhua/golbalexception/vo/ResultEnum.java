package com.yunhua.golbalexception.vo;

public enum ResultEnum {
	UNKONW_ERROR(-1,"未知错误"),
	SUCCESS(0,"成功"),
	ERROR(1,"失败"),
	LOFINERROR(101,"登录失败"),
	ADDCARSUCCESS(200,"添加车辆成功"),
	ADDCARFAIL(102,"添加车辆失败"),
	GETCARSUCCESS(200,"获取车辆信息成功"),
	PARAMCHECHFAIL(103,"参数校验失败")
	;
	
	private Integer code;
	private String msg;
	
	ResultEnum(Integer code,String msg) {
		this.code = code;
		this.msg = msg;
	}
 
	public Integer getCode() {
		return code;
	}
 
	public String getMsg() {
		return msg;
	}
}