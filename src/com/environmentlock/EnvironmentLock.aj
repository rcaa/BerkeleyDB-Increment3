package com.environmentlock;

import driver.Driver;
import driver.Util;

public aspect EnvironmentLock {

	Object around() : adviceexecution() && within(com.environmentlock.EnvironmentLockAbstract) {
		Object ret = null;
		if (new Driver().isActivated("environmentlock")) {
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
