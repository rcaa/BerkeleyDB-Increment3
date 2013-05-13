package driver;

import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.internal.AroundClosure;

public class Util {

	public static Object proceedAroundCall(JoinPoint thisJoinPoint)
			throws Throwable {
		Object ret = null;
		Object[] args = thisJoinPoint.getArgs();
		if (args.length > 0) {
			int i = (args.length - 1);
			if (args[i] instanceof AroundClosure) {
				ret = ((AroundClosure) args[i]).run(args);
			}
		}
		for (Object object : args) {
			System.out.println(object);
		}
		return ret;
	}
}