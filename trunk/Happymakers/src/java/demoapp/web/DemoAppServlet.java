package demoapp.web;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import demoapp.security.UserRoles;
import java.security.Principal;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns={"/ui/*", "/VAADIN/*"})
public class DemoAppServlet extends AbstractApplicationServlet {

	@Inject	Instance<DemoApp> application;

	@Override
	protected Class<? extends Application> getApplicationClass() throws
			ClassNotFoundException {
		return DemoApp.class;
	}

	@Override
	protected Application getNewApplication(HttpServletRequest request) throws
			ServletException {
		DemoApp app = application.get();
		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			throw new ServletException("Access denied");
		}

		// In this example, a user can be in one role only
		if (request.isUserInRole(UserRoles.ROLE_DIRECTOR)) {
			app.setUserRole(UserRoles.ROLE_DIRECTOR);
		} else if (request.isUserInRole(UserRoles.ROLE_PAYROLL_ASSISTANT)) {
			app.setUserRole(UserRoles.ROLE_PAYROLL_ASSISTANT);
		} else if (request.isUserInRole(UserRoles.ROLE_PROJECT_MANAGER)) {
			app.setUserRole(UserRoles.ROLE_PROJECT_MANAGER);
		} else {
			throw new ServletException("Access denied");
		}

		app.setUser(principal);
		app.setLogoutURL(request.getContextPath() + "/logout.jsp");
		return app;
	}
}
