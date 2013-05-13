package com.deletedb;

import driver.Driver;
import driver.Util;

public aspect DatabaseDeleteOperation {

	Object around() : adviceexecution() && (within(com.deletedb.DatabaseDeleteOperationAbstract) 
			 || within(com.deletedb.TxnDeleteSupportAbstract)) {
		Object ret = null;
		if (new Driver().isActivated("deleteDB")) {
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