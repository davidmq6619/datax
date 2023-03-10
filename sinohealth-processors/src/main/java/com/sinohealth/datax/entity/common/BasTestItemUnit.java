package com.sinohealth.datax.entity.common;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * 检验结果Result
 * 
 * @author ld
 * @version 2017-07-05
 */
public class BasTestItemUnit {

	public Integer id;
	public String unit;
	@db_alias("unit_comm")
	public String unitComm;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitComm() {
		return unitComm;
	}

	public void setUnitComm(String unitComm) {
		this.unitComm = unitComm;
	}
}