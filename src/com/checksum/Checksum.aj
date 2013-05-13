package com.checksum;

import driver.Driver;
import driver.Util;

public aspect Checksum {

	Object around() : adviceexecution() && within(com.checksum.ChecksumAbstract){
		Object ret = null;
		if (new Driver().isActivated("checksum")) {
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