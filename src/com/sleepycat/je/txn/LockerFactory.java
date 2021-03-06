/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002-2006
 *      Sleepycat Software.  All rights reserved.
 *
 * $Id: LockerFactory.java,v 1.1.6.3.2.1 2006/10/23 21:07:30 ckaestne Exp $
 */

package com.sleepycat.je.txn;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DbInternal;
import com.sleepycat.je.Environment;
import com.sleepycat.je.OperationContext;
import com.sleepycat.je.TransactionConfig;
import com.sleepycat.je.dbi.DatabaseImpl;
import com.sleepycat.je.dbi.EnvironmentImpl;

/**
 * Factory of static methods for creating Locker objects.
 */
public class LockerFactory {


    /**
     * Get a locker for a writable operation, also specifying whether to retain
     * non-transactional locks when a new locker must be created.
     *
     * @param retainNonTxnLocks is true for DbTree operations, so that the
     * handle lock may be transferred out of the locker when the operation is
     * complete.
     */
    public static Locker getWritableLocker(Environment env,
                                           OperationContext cxt,
                                           boolean retainNonTxnLocks)
        throws DatabaseException {

        EnvironmentImpl envImpl = DbInternal.envGetEnvironmentImpl(env);
//        boolean envIsTransactional = envImpl.isTransactional();

//	if (userTxn == null) {
//	    Transaction xaLocker = env.getThreadTransaction();
//	    if (xaLocker != null) {
//		return DbInternal.getLocker(xaLocker);
//	    }
//	}

//        if (dbIsTransactional && userTxn == null) {
//
//            if (autoCommitConfig == null) {
//                autoCommitConfig = DbInternal.getDefaultTxnConfig(env);
//            }
//            return new AutoTxn(envImpl, autoCommitConfig);
//
//        } else if (userTxn == null) {

            if (retainNonTxnLocks) {
                return new BasicLocker(envImpl);
            } else {
                return new ThreadLocker(envImpl);
            }

//        } else {
//
//            /* 
//             * The user provided a transaction, the environment and the
//             * database had better be opened transactionally.
//             */
//            if (!envIsTransactional) {
//                throw new DatabaseException
//		    ("A Transaction cannot be used because the"+
//		     " environment was opened" +
//		     " non-transactionally");
//            }
//            if (!dbIsTransactional) {
//                throw new DatabaseException
//		    ("A Transaction cannot be used because the" +
//		     " database was opened" +
//		     " non-transactionally");
//            }
//
//            /*
//             * Use the locker for the given transaction.  For read-comitted,
//             * wrap the given transactional locker in a special locker for that
//             * isolation level.  But if retainNonTxnLocks we cannot use
//             * read-committed, since retainNonTxnLocks is used for handle locks
//             * that must be retained across operations.
//             */
//            Locker locker = DbInternal.getLocker(userTxn);
//            if (locker.isReadCommittedIsolation() && !retainNonTxnLocks) {
//                return new ReadCommittedLocker(envImpl, locker);
//            } else {
//                return locker;
//            }
//        }
    }
   
    /**
     * Get a locker for a read or cursor operation.
     * See getWritableLocker for an explanation of retainNonTxnLocks.
     */
    public static Locker getReadableLocker(Environment env,
                                           OperationContext cxt,
                                           boolean retainNonTxnLocks,
                                           boolean readCommittedIsolation) 
        throws DatabaseException {

 

        return getReadableLocker
	    (env, (Locker)null, retainNonTxnLocks, readCommittedIsolation);
    }

    /**
     * Get a locker for this database handle for a read or cursor operation.
     * See getWritableLocker for an explanation of retainNonTxnLocks.
     */
    public static Locker getReadableLocker(Environment env,
                                           Database dbHandle,
                                           Locker locker,
                                           boolean retainNonTxnLocks,
                                           boolean readCommittedIsolation) 
        throws DatabaseException {
        
        /* 
         * Don't reuse a non-transactional locker unless retaining
         * non-transactional locks was requested.
         */
        if (locker != null &&
            !retainNonTxnLocks) {
            locker = null;
        }

        /*
         * Request read-comitted if that isolation level is configured for the
         * locker being reused, or if true is passed for the parameter (this is
         * the case when read-committed is configured for the cursor).
         */
        if (locker != null && locker.isReadCommittedIsolation()) {
            readCommittedIsolation = true;
        }

        return getReadableLocker
            (env, locker, retainNonTxnLocks, readCommittedIsolation);
    }

    /**
     * Get a locker for a read or cursor operation.
     * See getWritableLocker for an explanation of retainNonTxnLocks.
     */
    private static Locker getReadableLocker(Environment env,
                                            Locker locker,
                                            boolean retainNonTxnLocks,
                                            boolean readCommittedIsolation) 
        throws DatabaseException {




        if (locker == null) {

            /*
             * A non-transactional locker is requested.  If we're retaining
             * non-transactional locks across operations, use a BasicLocker
             * since the locker may be used across threads; this is used when
             * acquiring handle locks internally (open, close, remove, etc).
             * Otherwise, use a ThreadLocker to avoid self-deadlocks within the
             * same thread; this used for ordinary user operations.
             */
            EnvironmentImpl envImpl = DbInternal.envGetEnvironmentImpl(env);
            if (retainNonTxnLocks) {
                locker = new BasicLocker(envImpl);
            } else {
                locker = new ThreadLocker(envImpl);
            }
        }
        return locker;
    }
}
