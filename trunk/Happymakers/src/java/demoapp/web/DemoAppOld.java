package demoapp.web;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.GridLayout.OutOfBoundsException;
import com.vaadin.ui.GridLayout.OverlapsException;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.security.Principal;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;


public class DemoAppOld extends Application {

    /*
     * Remember not to invoke getApplication().getMainWindow() when the
     * components are initialiezed, as this will happen before
     * the init() method in this method has been invoked.
     */
    @Inject
    ManageCompetencesView manageCompetencesView;
    @Inject
    EmployeesView employeesView;
    @Inject
    AuditLogView auditLogView;
    private String userRole;

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public void init() {

        GridLayout grid = new GridLayout(2, 3);
        grid.setHeight(768, Sizeable.UNITS_PIXELS);
        grid.setWidth(1024, Sizeable.UNITS_PIXELS);
        initHeader(grid);
        initMenu(grid);
        initContent(grid);
        initFooter(grid);


        // Happymakers.com
        setMainWindow(new Window("Happymakers.com - Main Window", grid));

    }

    private void initContent(GridLayout grid) throws OutOfBoundsException, OverlapsException {
        // products and details
        Panel contentPanel = new Panel("Happymakers.com - Products and Detais");
        contentPanel.setHeight(650, Sizeable.UNITS_PIXELS);
        contentPanel.setWidth(900, Sizeable.UNITS_PIXELS);
        FormLayout contentForm = new FormLayout();
        contentForm.addComponent(new Label("content"));
        contentPanel.setLayout(contentForm);
        grid.addComponent(contentPanel, 1, 1);
    }

    private void initMenu(GridLayout grid) throws OutOfBoundsException, OverlapsException {
        // content - menu
        Panel menuPanel = new Panel("Happymakers.com - Menu");
        menuPanel.setHeight(650, Sizeable.UNITS_PIXELS);
        menuPanel.setWidth(124, Sizeable.UNITS_PIXELS);
        FormLayout menuForm = new FormLayout();
        menuForm.addComponent(new Label("menu"));
        menuPanel.setLayout(menuForm);
        grid.addComponent(menuPanel, 0, 1);
    }

    private void initHeader(GridLayout grid) throws OutOfBoundsException, OverlapsException {
        // header
        Panel headerPanel = new Panel();
        headerPanel.setSizeUndefined();
        headerPanel.addComponent(new MenuBarUser());
        grid.addComponent(headerPanel, 0, 0, 1, 0);
    }

    private void initFooter(GridLayout grid) throws OutOfBoundsException, OverlapsException {
        // footer
        Panel footerPanel = new Panel("Happymakers.com - Footer");
        FormLayout footerForm = new FormLayout();
        footerForm.addComponent(new Label("footer"));
        footerPanel.setLayout(footerForm);
        grid.addComponent(footerPanel, 0, 2, 1, 2);
    }

    public void init3() {
        // Create a 4 by 4 grid layout.
        GridLayout grid = new GridLayout(5, 5);
        grid.setHeight(1200, Sizeable.UNITS_PIXELS);
        grid.setWidth(1920, Sizeable.UNITS_PIXELS);

        grid.addStyleName("example-gridlayout");
        // Fill out the first row using the cursor.
        grid.addComponent(new Button("R/C 1"));
        for (int i = 0; i < 3; i++) {
            grid.addComponent(new Button("Col " + (grid.getCursorX() + 1)));
        }
        // Fill out the first column using coordinates.
        for (int i = 1; i < 4; i++) {
            grid.addComponent(new Button("Row " + i), 0, i);
        }
        // Add some components of various shapes.
        grid.addComponent(new Button("3x1 button"), 1, 1, 3, 1);
        grid.addComponent(new Label("1x2 cell"), 1, 2, 1, 3);
        InlineDateField date = new InlineDateField("A 2x2 date field");
        date.setResolution(DateField.RESOLUTION_DAY);
        date.setLocale(Locale.GERMAN);
        grid.addComponent(date, 2, 2, 3, 3);

        // add a panel
        Panel panel = new Panel("I am a panel");
        FormLayout form = new FormLayout();
        form.addComponent(new Label("abc"));
        form.addComponent(new Button("button"));

        panel.setLayout(form);
        grid.addComponent(panel, 0, 4, 3, 4);

        // Happymakers.com
        setMainWindow(new Window("Happymakers.com", grid));





    }

    // old code
    public void init2() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        Label header = new Label("Secure Vaadin Application Demo, logged in as " + ((Principal) getUser()).getName() + " (" + userRole + ")");
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
