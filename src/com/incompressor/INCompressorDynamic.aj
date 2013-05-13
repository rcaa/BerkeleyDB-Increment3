package com.incompressor;

import driver.Driver;
import driver.Util;

public aspect INCompressorDynamic {

	Object around() : adviceexecution() && (within(com.incompressor.PurgeRootAbstract) 
			 || within(com.incompressor.INCompressorAbstract)) {
		Object ret = null;
		if (new Driver().isActivated("iNCompressor")) {
			ret = proceed();
		} else {
			try {
				ret = Util.proceedAroundCall(thisJoinPoint);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
}
