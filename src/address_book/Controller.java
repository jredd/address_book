package address_book;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
//import java.xm
//import javax.xml.soap.Node;
import javafx.scene.Node;


import java.awt.*;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ResourceBundle;
import java.io.InputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;



public class Controller implements Initializable {

    @FXML
    private Pane pane_input_mode;
    @FXML
    private Pane pane_view_mode;
    @FXML
    private Button btn_control_view_mode;
    @FXML
    private Button btn_cancel_mode;
    @FXML
    private Button btn_delete_contact;
    @FXML
    private Label label_full_name;
    @FXML
    private Label label_address_line1;
    @FXML
    private Label label_address_line2;
    @FXML
    private Label label_phone;
    @FXML
    private Label label_gender;
    @FXML
    private Label label_validation_warning;
    @FXML
    private TextField field_first_name;
    @FXML
    private TextField field_last_name;
    @FXML
    private TextField field_middle_name;
    @FXML
    private TextField field_address1;
    @FXML
    private TextField field_address2;
    @FXML
    private TextField field_city;
    @FXML
    private TextField field_zip;
    @FXML
    private TextField field_phone;
    @FXML
    private ComboBox combo_state;
    @FXML
    private ComboBox combo_gender;
    @FXML
    private ListView<String> contact_list_view;

    private List current_selection;
    private String current_mode = "view";

    private final HashMap<String, List<Comparable<?>>> contacts = new HashMap();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing Application and setting things");

        load_data();
        load_contacts_to_list_view();

        // Set the first contact in the list to the contact view
        int index = contact_list_view.getSelectionModel().getSelectedIndex()+1;
        set_list_view_selection(index);
    }

    public void load_data() {
        try {
            // Import and begin adding each contact to the mapper
            InputStream is = Controller.class.getResourceAsStream("contacts.txt");
            Scanner scanner = new Scanner(is);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                List<String> contact = Arrays.asList(line.split("     "));
                String name = build_name(contact);
                contacts.put(name, new ArrayList<Comparable<?>>(contact));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        contact_list_view.getSelectionModel().select(6);
    }

    public void load_contacts_to_list_view(){
        ObservableList<String> names = FXCollections.observableArrayList();
        Set<Map.Entry<String, List<Comparable<?>>>> set = contacts.entrySet();
        Iterator<Map.Entry<String, List<Comparable<?>>>> i = set.iterator();
        while (i.hasNext()) {
            Map.Entry<String, List<Comparable<?>>> name = i.next();
            names.add((String) name.getKey());
        }

        Collections.sort(names);
        contact_list_view.setItems(names);
    }

    public String build_name(List contact) {

        String middle_init = ""+contact.get(2);
        String first_name = ""+contact.get(0);
        String last_name = ""+contact.get(1);
        String full_name;

        // Concatenate the strings based on the found data
        if (middle_init.equals("None")) {
            full_name = first_name + " " + last_name;
        }else {
            full_name = first_name + " " + middle_init + " " + last_name;
        }

        return full_name;
    }

    public void set_contact_view_information(List<Comparable<?>> contact) {

        String address1 = ""+contact.get(3);
        String address2 = ""+contact.get(4);
        String address_line1;
        String address_line2 = ""+contact.get(5)+", "+contact.get(6)+" "+contact.get(7);

        // Concatenate the strings based on the found data
        if (address2.equals("None")) {
            address_line1 = address1;
        }else {
            address_line1 = address1 + " " + address2;
        }

        label_full_name.setText(build_name(contact));
        label_address_line1.setText(address_line1);
        label_address_line2.setText(address_line2);
        label_phone.setText(""+contact.get(8));
        label_gender.setText(""+contact.get(9));
    }

    public void clear_text_fields() {
        field_first_name.clear();
        field_last_name.clear();
        field_middle_name.clear();
        field_address1.clear();
        field_address2.clear();
        field_address2.clear();
        field_city.clear();
        field_zip.clear();
        field_phone.clear();
        combo_state.setPromptText("---");
        combo_gender.setPromptText("---");
    }

    public void list_clicked(MouseEvent event) {
        String item = contact_list_view.getSelectionModel().getSelectedItem();
        List<Comparable<?>> contact = contacts.get(item);

        // Make sure that execution only happens on newly selected contacts
        if (contact.equals(current_selection)){
            return;
        }
        current_selection = contact;
        if (current_mode.equals("view")) {
            System.out.println("turtles");
            set_contact_view_information(contact);
        }else {
            System.out.println("asdklfhj");
            set_edit_view();
            btn_control_view_mode.setText("Edit");
            current_mode = "view";
            toggle_view_pane(true);
            toggle_input_pane(false);
            toggle_cancel_btn(false);
        }

    }

    public void add_contact(MouseEvent Event) {
        System.out.println("Add contact clicked");
        btn_control_view_mode.setText("Done");
        current_mode = "add";
        toggle_cancel_btn(true);
        toggle_view_pane(false);
        toggle_input_pane(true);
        clear_text_fields();
    }

    public void set_list_view_selection(int index){
        contact_list_view.getSelectionModel().select(index);
        contact_list_view.getFocusModel().focus(index);
        contact_list_view.scrollTo(index);
        List<Comparable<?>> new_current_contact = contacts.get(contact_list_view.getSelectionModel().getSelectedItem());
        System.out.println(new_current_contact);
        set_contact_view_information(contacts.get(build_name(new_current_contact)));
    }

    public void delete_contact(MouseEvent Event) {
        System.out.println("delete contact btn clicked");
        List<Comparable<?>> current_contact = contacts.get(contact_list_view.getSelectionModel().getSelectedItem());
        String name_key = build_name(current_contact);
        contacts.remove(name_key);
        load_contacts_to_list_view();
        save_contacts_to_file();

        // Set the view to the currently selected item in the list view
        int index = contact_list_view.getSelectionModel().getSelectedIndex()+1;
        set_list_view_selection(index);
    }

    public void toggle_cancel_btn(boolean visible){
        btn_cancel_mode.setVisible(visible);
        if (visible) {
            btn_cancel_mode.setDisable(false);
        }else {
            btn_cancel_mode.setDisable(true);
        }
    }

    public void toggle_view_pane(boolean visible){
        pane_view_mode.setVisible(visible);
        if (visible) {
            pane_view_mode.setDisable(false);
        }else {
            pane_view_mode.setDisable(true);
        }
    }

    public void toggle_input_pane(boolean visible){
        toggle_cancel_btn(visible);
        pane_input_mode.setVisible(visible);
        if (visible) {
            pane_input_mode.setDisable(false);
        }else {
            pane_input_mode.setDisable(true);
        }
    }

    public void cancel_mode(MouseEvent Event) {
        System.out.println("cancel mode btn clicked");
        toggle_cancel_btn(false);
        toggle_view_pane(true);
        toggle_input_pane(false);
        btn_control_view_mode.setText("Edit");

        current_mode = "view";
    }

    public void set_edit_view() {
        List<Comparable<?>> contact = contacts.get(contact_list_view.getSelectionModel().getSelectedItem());
        System.out.println(contact);

        String name_middle = ""+contact.get(2);
        String address2 = ""+contact.get(4);

        field_first_name.setText(""+contact.get(0));
        field_last_name.setText(""+contact.get(1));
        if (!name_middle.equals("None")) {
            field_middle_name.setText(name_middle);
        }
        field_address1.setText(""+contact.get(3));
        if (!address2.equals("None")) {
            field_address2.setText(address2);
        }
        field_city.setText(""+contact.get(5));
        field_zip.setText(""+contact.get(7));
        field_phone.setText(""+contact.get(8));
        combo_state.setValue("" + contact.get(6));
        combo_gender.setValue(""+contact.get(9));
    }

    public boolean validate_fields() {

        for (Node node : pane_input_mode.getChildren()) {
            // Validate Text Fields
            if (node instanceof TextField) {
                String id = ((TextField) node).getId();
                String field_value = ((TextField) node).getText();
                if (!id.equals("field_address2")) {
                    if ((!id.equals("field_middle_name"))) {
                        if (field_value.length() == 0) {
                            ((TextField) node).setPromptText("Required");
                            ((TextField) node).setStyle("-fx-background-color: #f93838;");
                            return false;
                        } else {
                            ((TextField) node).setStyle("-fx-background-color: #fff;");
                        }
                    }
                }
            }
            // Validate comboboxes
            if (node instanceof ComboBox) {
                if (((ComboBox) node).getValue() == null) {
                    ((ComboBox) node).setStyle("-fx-background-color: #f93838;");
                    return false;
                }else {
                    ((ComboBox) node).setStyle("-fx-background-color: #fff;");
                }
            }
        }
        return true;
    }

    public List<Comparable<?>> get_text_field_content(List<Comparable<?>> contact) {
        String name_middle = ""+field_middle_name.getText();
        String address2 = ""+field_address2.getText();
        System.out.println(contact);

        contact.set(0, field_first_name.getText());
        contact.set(1, field_last_name.getText());
        if (name_middle.length() == 0) {
            System.out.println("here is the middle name -"+name_middle);
            contact.set(2, "None");
        } else {
            contact.set(2, name_middle);
        }
        contact.set(3, field_address1.getText());
        if (address2.length() == 0) {
            contact.set(4, "None");
        } else {
            contact.set(4, address2);
        }
        contact.set(5, field_city.getText());
        contact.set(7, field_zip.getText());
        contact.set(8, field_phone.getText());
        contact.set(6, combo_state.getSelectionModel().getSelectedItem().toString());
        contact.set(9, combo_gender.getSelectionModel().getSelectedItem().toString());

        return contact;
    }

    public void update_contact(){
        List<Comparable<?>> current_contact = contacts.get(contact_list_view.getSelectionModel().getSelectedItem());
        get_text_field_content(current_contact);

        // Set the local data with the new information
        System.out.println(contacts);
        set_contact_view_information(current_contact);
    }

    public boolean check_if_contact_does_not_exists(){
        label_validation_warning.setText(null);
        String first_name = field_first_name.getText();
        String last_name = field_last_name.getText();
        String middle_name = field_middle_name.getText();
        String full_name;

        if (middle_name.equals("None")) {
            full_name = first_name+" "+last_name;
        } else {
            full_name = first_name+" "+middle_name+" "+last_name;
        }
        if (contacts.get(full_name) != null) {
            label_validation_warning.setText("Contact Already Exists.");
            return false;
        }
        return true;
    }

    public void save_contacts_to_file() {
        try {
            // Open up the file to begin reading the lines in
            String str_file = getClass().getResource("contacts.txt").getFile();
            File file = new File(str_file);
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);

            for (String key : contacts.keySet()) {
                List contact = contacts.get(key);
                String contact_string = "";
                for (int i=0; i < contact.size(); i++) {
                    contact_string += contact.get(i)+ "     ";
                }
                bw.write(contact_string);
                bw.write("\n");
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void add_contact(List contact) {
        try {
            // Open up the file to begin reading the lines in
            String str_file = getClass().getResource("contacts.txt").getFile();
            File file = new File(str_file);
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            String contact_string = "";
            for (int i=0; i < contact.size(); i++) {
                contact_string += contact.get(i)+ "     ";
            }
            bw.write(contact_string);
            bw.write("\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void control_view_contact(MouseEvent Event) {
        System.out.println("The current mode is "+current_mode);
        // Set the UI properties based on the current view.
        if (current_mode.equals("view")) {
            btn_control_view_mode.setText("Done");
            current_mode = "edit";
            toggle_view_pane(false);
            toggle_input_pane(true);
            set_edit_view();

        }else if (current_mode.equals("edit")) {

            if (validate_fields()) {
                save_contacts_to_file();
                update_contact();
                btn_control_view_mode.setText("Edit");
                current_mode = "view";
                toggle_view_pane(true);
                toggle_input_pane(false);
            }
        }else if (current_mode.equals("add")) {

            if (validate_fields()) {
                if (check_if_contact_does_not_exists()) {
                    btn_control_view_mode.setText("Edit");
                    current_mode = "view";
                    toggle_view_pane(true);
                    toggle_input_pane(false);
                    // Setup new contact list string to then be added to the
                    // Hash and update the txt file.
                    List<String> new_contact_info = Arrays.asList(new String[10]);
                    List<Comparable<?>> new_contact = new ArrayList<Comparable<?>>(new_contact_info);
                    new_contact = get_text_field_content(new_contact);
                    add_contact(new_contact);

                    // Add new contact to contacts model abd rebuild the contacts list view.

                    String name = build_name(new_contact);
                    contacts.put(name, new_contact);
                    load_contacts_to_list_view();
                    set_contact_view_information(new_contact);
//                    contact_list_view.getItems().s
//                    for (Object entry : contact_list_view.getItems() ) {
//                        String entryText = name;
//                    }
//                    contacts.;
//                    contacts.get(name);
//                    contact_list_view.getSelectionModel().getSelectedItem(name);
//                    contact_list_view.getFocusModel().focus(index);

                }
            }
        }
    }
}
