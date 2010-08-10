package demoapp.web;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import demoapp.domain.Employee;
import demoapp.domain.SalaryInfo;
import demoapp.enterprise.EmployeeBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;

public class EmployeesView extends CustomComponent {

	@EJB
	EmployeeBean employeeBean;
	private Form form;
	private Form salaryForm;
	private Button salaryInfoButton;
	private Button competencesButton;
	private Button deleteEmployeeButton;
	private Button saveEmployeeButton;
	private Button saveSalaryInfoButton;

	@PostConstruct
	public void init() {
		setCaption("Employees");
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);

		HorizontalLayout topBar = new HorizontalLayout();
		topBar.setSpacing(true);
		topBar.addComponent(new Label("Enter person number:"));
		final TextField personNumberField = new TextField();
		topBar.addComponent(personNumberField);
		final Button findEmployeeButton = new Button("Find Employee", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {
					Employee e = employeeBean.getEmployeeByPersonNumber(personNumberField.
							getValue().toString());
					if (e == null) {
						getApplication().getMainWindow().showNotification(
								"No such employee");
					} else {
						editEmployee(e, false);
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
		});
		topBar.addComponent(findEmployeeButton);
		final Button newEmployeeButton = new Button("New Employee", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				editEmployee(new Employee(), true);
			}
		});
		topBar.addComponent(newEmployeeButton);

		layout.addComponent(topBar);

		form = new Form();
		form.setCaption("Employee Details");
		form.setWriteThrough(false);
		form.setImmediate(true);
		form.setValidationVisibleOnCommit(true);
		form.setVisible(false);
		form.setFormFieldFactory(new DefaultFieldFactory() {

			@Override
			public Field createField(Item item, Object propertyId,
					Component uiContext) {
				Field f = super.createField(item, propertyId, uiContext);
				if ("firstName".equals(propertyId)) {
					f.setRequired(true);
					f.setRequiredError("Please enter a first name");
				} else if ("lastName".equals(propertyId)) {
					f.setRequired(true);
					f.setRequiredError("Please enter a last name");
				} else if ("dateOfBirth".equals(propertyId)) {
					f.setRequired(true);
					f.setRequiredError("Please enter a date of birth");
				} else if ("personNumber".equals(propertyId)) {
					f.setRequired(true);
					f.setRequiredError("Please enter a person number");
				}
				return f;
			}
		});

		salaryInfoButton = new Button("Open Salary Info", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				assert currentEmployee != null : "currentEmployee should not be null here";
				assert !addingNew : "addingNew should be false here";
				try {
					SalaryInfo si = employeeBean.getSalaryInfo(currentEmployee);
					if (si == null) {
						si = new SalaryInfo();
						si.setEmployee(currentEmployee);
					}
					editSalary(si);
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
		});
		competencesButton = new Button("Open Competences", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				getApplication().getMainWindow().showNotification("Try to implement this feature yourself! :-)");
			}
		});
		deleteEmployeeButton = new Button("Delete Employee", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				assert currentEmployee != null : "currentEmployee should not be null here";
				assert !addingNew : "addingNew should be false here";
				try {
					employeeBean.deleteEmployee(currentEmployee);
					editEmployee(null, false);
					getApplication().getMainWindow().showNotification(
							"Employee Deleted");
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
		});
		saveEmployeeButton = new Button("Save", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				assert currentEmployee != null : "currentEmployee should not be null here";
				try {
					form.commit();
				} catch (Exception e) {
					return;
				}
				try {
					if (addingNew) {
						employeeBean.insertEmployee(currentEmployee);
						editEmployee(currentEmployee, false);
					} else {
						editEmployee(
								employeeBean.updateEmployee(currentEmployee),
								false);
					}
					getApplication().getMainWindow().showNotification(
							"Employee Saved");
				} catch (EJBAccessException e) {
					getApplication().getMainWindow().showNotification(
							"Access Denied",
							Notification.TYPE_WARNING_MESSAGE);
				} catch (Exception e) {
					getApplication().getMainWindow().showNotification(
							"Unexpected error: " + e.toString(),
							Notification.TYPE_ERROR_MESSAGE);
				}
			}
		});

		form.getFooter().addComponent(saveEmployeeButton);
		form.getFooter().addComponent(deleteEmployeeButton);
		form.getFooter().addComponent(salaryInfoButton);
		form.getFooter().addComponent(competencesButton);

		layout.addComponent(form);

		salaryForm = new Form();
		salaryForm.setCaption("Employee Details");
		salaryForm.setWriteThrough(false);
		salaryForm.setImmediate(true);
		salaryForm.setValidationVisibleOnCommit(true);
		salaryForm.setVisible(false);
		salaryForm.setFormFieldFactory(new DefaultFieldFactory() {

			@Override
			public Field createField(Item item, Object propertyId,
					Component uiContext) {
				Field f = super.createField(item, propertyId, uiContext);
				if ("salary".equals(propertyId)) {
					f.setRequired(true);
					f.setRequiredError("Please enter a salary");
					f.addValidator(new DoubleValidator("Please enter a valid floating point value"));
				}
				return f;
			}
		});

		saveSalaryInfoButton = new Button("Save Salary", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				assert currentSalaryInfo != null : "currentSalaryInfo should not be null here";
				try {
					salaryForm.commit();
				} catch (Exception e) {
					return;
				}
				try {
					editSalary(employeeBean.saveSalaryInfo(currentSalaryInfo));
					getApplication().getMainWindow().showNotification(
							"Salary Info Saved");
				} catch (EJBAccessException e) {
					getApplication().getMainWindow().showNotification(
							"Access Denied",
							Notification.TYPE_WARNING_MESSAGE);
				} catch (Exception e) {
					getApplication().getMainWindow().showNotification(
							"Unexpected error: " + e.toString(),
							Notification.TYPE_ERROR_MESSAGE);
				}
			}
		});
		salaryForm.getFooter().addComponent(saveSalaryInfoButton);

		layout.addComponent(salaryForm);

		setCompositionRoot(layout);
		setSizeFull();
	}
	private Employee currentEmployee = null;
	private boolean addingNew = false;

	public void editEmployee(Employee e, boolean addingNew) {
		editSalary(null);
		currentEmployee = e;
		this.addingNew = addingNew;
		if (currentEmployee == null) {
			form.setItemDataSource(null);
			form.setVisible(false);
		} else {
			form.setItemDataSource(new BeanItem(currentEmployee));
			form.setVisibleItemProperties(
					new String[]{"firstName", "lastName", "personNumber", "dateOfBirth", "position", "test"});
			form.setVisible(true);
			deleteEmployeeButton.setVisible(!addingNew); // Persistent
			salaryInfoButton.setVisible(!addingNew);
			competencesButton.setVisible(!addingNew);
		}
	}

	private SalaryInfo currentSalaryInfo = null;

	public void editSalary(SalaryInfo si) {
		currentSalaryInfo = si;
		if (si == null) {
			salaryForm.setItemDataSource(null);
			salaryForm.setVisible(false);
		} else {
			salaryForm.setItemDataSource(new BeanItem(currentSalaryInfo));
			salaryForm.setVisibleItemProperties(new String[] { "salary" });
			salaryForm.setVisible(true);
		}
	}
}
