package com.nio;

import driver.Driver;
import driver.Util;

public aspect NIOAspect {
	
	Object around() : adviceexecution() && within(com.nio.NIOAbstract) {
		Object ret = null;
		if (new Driver().isActivated("nio") && !(new Driver().isActivated("io"))) {
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