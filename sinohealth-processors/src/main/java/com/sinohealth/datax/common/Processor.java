package com.sinohealth.datax.common;

import java.util.List;

public interface Processor<I, O> {

	public O dataProcess(I i, O o, CommonData commonData);

}
