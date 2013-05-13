package com.lookaheadcache;

import driver.Driver;
import driver.Util;

public aspect LookAheadCacheDynamic {

	Object around() : adviceexecution() && within(com.lookaheadcache.LookAheadCacheAbstract) {
		Object ret = null;
		if (new Driver().isActivated("lookAheadCache")) {
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