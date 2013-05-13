package com.evictor;

import driver.Driver;
import driver.Util;

public aspect WeaveEvictor {

	Object around() : adviceexecution() && within(com.evictor.WeaveEvictorAbstract) {
		Object ret = null;
		if (new Driver().isActivated("evictor")) {
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
