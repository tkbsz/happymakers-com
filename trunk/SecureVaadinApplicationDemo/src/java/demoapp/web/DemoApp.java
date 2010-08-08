package demoapp.web;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.security.Principal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@SessionScoped
public class DemoApp extends Application {

	/*
	 * Remember not to invoke getApplication().getMainWindow() when the
	 * components are initialiezed, as this will happen before
	 * the init() method in this method has been invoked.
	 */
	@Inject ManageCompetencesView manageCompetencesView;
	@Inject EmployeesView employeesView;
	@Inject	AuditLogView auditLogView;

	private String userRole;

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@Override
	public void init() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		Label header = new Label("Secure Vaadin Application Demo, logged in as " + ((Principal) getUser()).getName() + " (" + userRole + ")" );
		header.setStyleName("h1");
		layout.addComponent(header);

		Button logoutButton = new Button("Logout", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				close(); // closes the application
			}
		});
		layout.addComponent(logoutButton);

		TabSheet tabs = new TabSheet();
		tabs.setSizeFull();
		layout.addComponent(tabs);
		layout.setExpandRatio(tabs, 1);

		tabs.addComponent(employeesView);
		tabs.addComponent(manageCompetencesView);
		tabs.addComponent(auditLogView);

		setMainWindow(new Window("Secure Vaadin Application Demo", layout));
	}

}
