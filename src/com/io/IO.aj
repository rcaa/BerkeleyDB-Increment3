package com.io;

import driver.Driver;
import driver.Util;

public aspect IO {

	Object around() : adviceexecution() && within(com.io.IOAbstract) {
		Object ret = null;
		if (new Driver().isActivated("io")) {
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