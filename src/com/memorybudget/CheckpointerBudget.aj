package com.memorybudget;

import driver.Driver;
import driver.Util;

public privileged aspect CheckpointerBudget {

	pointcut driver() : if(new Driver().isActivated("memoryBudget"));

	Object around() : adviceexecution() && (within(com.memorybudget.CheckpointerBudgetAbstract) 
			|| within(com.memorybudget.CleanerBudgetAbstract) 
			|| within(com.memorybudget.CursorBudgetAbstract) 
			|| within(com.memorybudget.DINBINBudgetAbstract) 
			|| within(com.memorybudget.EvictorBudgetAbstract) 
			|| within(com.memorybudget.INBudgetAbstract) 
			|| within(com.memorybudget.INListBudgetAbstract) 
			|| within(com.memorybudget.LockBudgetAbstract) 
			|| within(com.memorybudget.LockManagerBudgetAbstract) 
			|| within(com.memorybudget.PreloaderBudgetAbstract) 
			|| within(com.memorybudget.TreeWalkerBudgetAbstract) 
			|| within(com.memorybudget.TxnBudgetAbstract) 
			|| within(com.memorybudget.WeaveMemoryBudgetAbstract)) {
		Object ret = null;
		if (new Driver().isActivated("memoryBudget")) {
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