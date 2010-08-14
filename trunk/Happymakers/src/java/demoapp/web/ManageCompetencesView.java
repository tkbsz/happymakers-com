package demoapp.web;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import demoapp.domain.Competence;
import demoapp.enterprise.CompetenceBean;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;

public class ManageCompetencesView extends CustomComponent {

	@EJB
	CompetenceBean competenceBean;
	private Form form;
	private BeanItemContainer<Competence> container;
	private Button applyButton;
	private Button newButton;
	private Button deleteButton;
	private Button refreshButton;

	@PostConstruct
	public void init() {
		setCaption("Manage Competences");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		container = new BeanItemContainer(Competence.class);
		final Table table = new Table();
		table.setContainerDataSource(container);
		table.setVisibleColumns(new Object[]{"competenceName", "description"});
		table.setColumnHeaders(new String[]{"Competence Name", "Description"});
		table.setSizeFull();
		table.setSelectable(true);
		table.setImmediate(true);
		layout.addComponent(table);
		layout.setExpandRatio(table, 2);
		table.addListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {
				editCompetence((Competence) table.getValue());
			}
		});

		HorizontalLayout buttons = new HorizontalLayout();

		refreshButton = new Button("Refresh", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				refresh();
			}
		});

		newButton = new Button("New", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				editCompetence(new Competence());
			}
		});

		buttons.setSpacing(true);
		buttons.addComponent(refreshButton);
		buttons.addComponent(newButton);
		layout.addComponent(buttons);

		form = new Form();
		form.setFormFieldFactory(new DefaultFieldFactory() {

			@Override
			public Field createField(Item item, Object propertyId,
					Component uiContext) {
				Field f = super.createField(item, propertyId, uiContext);
				if ("competenceName".equals(propertyId)) {
					f.setRequired(true);
					f.setRequiredError("Please enter a competence name");
				}
				return f;
			}
		});
		form.setCaption("Competence Details");
		form.setWriteThrough(false);
		form.setValidationVisibleOnCommit(true);
		form.setImmediate(true);

		applyButton = new Button("Apply", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				if (currentCompetence != null) {
					try {
						form.commit();
					} catch (Exception e) {
						// Let the form handle it.
						return;
					}
					if (form.isValid()) {
						try {
							if (currentCompetence.getVersion() == null) {
								competenceBean.insertCompetence(
										currentCompetence);
							} else {
								currentCompetence = competenceBean.
										updateCompetence(
										currentCompetence);
							}
							getApplication().getMainWindow().showNotification(
									"Competence Saved");
							refresh();
							table.setValue(currentCompetence);
						} catch (EJBAccessException e) {
							getApplication().getMainWindow().showNotification(
									"Access Denied",
									Notification.TYPE_WARNING_MESSAGE);
						} catch (Exception e) {
							getApplication().getMainWindow().showNotification(e.
									toString(),
									Notification.TYPE_ERROR_MESSAGE);
						}
					}
				}
			}
		});

		deleteButton = new Button("Delete", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				if (currentCompetence != null) {
					try {
						competenceBean.deleteCompetence(currentCompetence);
						editCompetence(null);
						getApplication().getMainWindow().showNotification(
								"Competence Deleted");
						refresh();
					} catch (EJBAccessException e) {
						getApplication().getMainWindow().showNotification(
								"Access Denied",
								Notification.TYPE_WARNING_MESSAGE);
					} catch (Exception e) {
						getApplication().getMainWindow().showNotification(e.
								toString(),
								Notification.TYPE_ERROR_MESSAGE);
					}
				}
			}
		});

		form.getFooter().addComponent(applyButton);
		form.getFooter().addComponent(deleteButton);

		layout.addComponent(form);
		layout.setExpandRatio(form, 1);
		setCompositionRoot(layout);
		setSizeFull();

		editCompetence(null);
	}
	private Competence currentCompetence = null;

	public void editCompetence(Competence competence) {
		this.currentCompetence = competence;
		if (competence == null) {
			form.setItemDataSource(null);
			applyButton.setEnabled(false);
			deleteButton.setEnabled(false);
		} else {
			form.setItemDataSource(new BeanItem(competence));
			applyButton.setEnabled(true);
			deleteButton.setEnabled(competence.getVersion() != null); // Persistent
		}
		form.setVisibleItemProperties(
				new Object[]{"competenceName", "description"});
	}

	public void refresh() {
		container.removeAllItems();
		try {
			Collection<Competence> competences = competenceBean.getCompetences();
			for (Competence c : competences) {
				container.addBean(c);
			}
		} catch (EJBAccessException e) {
			getApplication().getMainWindow().showNotification(
					"Access Denied",
					Notification.TYPE_WARNING_MESSAGE);
		} catch (Exception e) {
			getApplication().getMainWindow().showNotification(e.toString(),
					Notification.TYPE_ERROR_MESSAGE);
		}
	}
}
