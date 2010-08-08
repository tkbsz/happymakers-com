package demoapp.security;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@AuditLog
@Interceptor
public class AuditLogInterceptor {

	@Resource
	SessionContext sessionContext;

	@EJB
	AuditService auditService;

	@AroundInvoke
	public Object recordAuditLogEntry(InvocationContext ctx) throws Exception {
		Object result = ctx.proceed();

		StringBuilder sb = new StringBuilder();
		sb.append(ctx.getMethod().getName());
		sb.append("(");
		for (Object p : ctx.getParameters()) {
			sb.append(p);
			sb.append(",");
		}
		sb.append(")");

		String userName = sessionContext.getCallerPrincipal().getName();

//		System.out.println(userName + ": " + sb.toString());

		auditService.recordEntry(userName, sb.toString());

		return result;
	}
}
