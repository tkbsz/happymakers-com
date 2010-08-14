package demoapp.web;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import demoapp.security.AuditEntry;
import demoapp.security.AuditService;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;

public class AuditLogView extends CustomComponent {

	@EJB
	AuditService auditService;

	@PostConstruct
	public void init() {
		setCaption("Audit Log");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		final BeanItemContainer<AuditEntry> container = new BeanItemContainer<AuditEntry>(AuditEntry.class);
		Table table = new Table();
		table.setContainerDataSource(container);
		table.setVisibleColumns(new String[] {"entryTimeStamp", "userName", "message"});
		table.setColumnHeaders(new String[] {"TimeStamp", "User", "Message"});
		table.setSizeFull();
		layout.addComponent(table);
		layout.setExpandRatio(table, 1);
		layout.addComponent(new Button("Refresh", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {
					List<AuditEntry> entries = auditService.getAuditEntries();
					container.removeAllItems();
					for (AuditEntry e : entries) {
						container.addBean(e);
					}
				} catch (EJBAccessException e) {
					getApplication().getMainWindow().showNotification(
							"Access Denied",
							Notification.TYPE_WARNING_MESSAGE);
				} catch (Exception e) {
					getApplication().getMainWindow().showNotification(
							"Unexpected error",
							Notification.TYPE_ERROR_MESSAGE);
				}
			}
		
		}));

		setCompositionRoot(layout);
		setSizeFull();
	}

}
