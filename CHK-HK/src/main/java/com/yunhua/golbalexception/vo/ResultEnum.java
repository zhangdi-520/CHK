package com.yunhua.golbalexception.vo;

public enum ResultEnum {
	UNKONW_ERROR(-1,"未知错误"),
	SUCCESS(0,"成功"),
	ERROR(1,"失败"),
	LOGINERROR(101,"登录失败"),
	ADDCARSUCCESS(200,"添加车辆成功"),
	ADDCARFAIL(102,"添加车辆失败"),
	GETCARSUCCESS(200,"获取车辆信息成功"),
	PARAMCHECHFAIL(103,"参数校验失败"),
	ADDMERCHANTFAIL(104,"添加商户信息失败"),
	ADDMERCHANTSUCCESS(200,"添加商户信息成功"),
	DELETEMERCHANTFAIL(105,"删除商户信息失败"),
	DELETEMERCHANTSUCCESS(200,"删除商户信息成功"),
	UPDATEMERCHANTFAIL(106,"修改商户信息失败"),
	UPDATEMERCHANTSUCCESS(200,"修改商户信息成功"),
	GETMERCHANTFAIL(107,"获取商户信息失败"),
	GETMERCHANTSUCCESS(200,"获取商户信息成功"),
	LISTALLEMPLOYEEFAIL(108,"获取所有的员工信息失败"),
	LISTALLEMPLOYEESUCCESS(200,"获取所有的员工信息成功"),
	GETEMPLOYEEFAIL(108,"获取一个员工信息失败"),
	GETEMPLOYEESUCCESS(200,"获取一个员工信息成功"),
	NOTFINDINDATABASE(109,"数据库中不存在该数据"),
	ADDDATABASEFAIL(110,"添加数据库失败")
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