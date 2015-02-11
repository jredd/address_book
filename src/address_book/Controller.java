package address_book;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;
import java.io.InputStream;

import java.io.InputStream;
import java.net.URL;
import java.util.*;



public class Controller implements Initializable {

    @FXML
    private Pane pane_edit_mode;
    @FXML
    private Pane pane_view_mode;
    @FXML
    private Button btn_edit_contact;
    @FXML
    private Button btn_add_contact;
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
    private ListView<String> contact_list_view;

    private List current_selection;
    private String current_mode = "view";

    private final HashMap<String, List<Comparable<?>>> contacts = new HashMap();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing Application and setting things");

        load_data();
        load_contacts_to_list_view();

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

        // Set the first contact in the list to the contact view
        set_contact_view_information(contacts.get(names.get(0)));
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

    public void add_contact(ActionEvent Event) {

        System.out.println("Add contact clicked");
    }

    @FXML
    public void list_clicked(MouseEvent event) {
        String item = contact_list_view.getSelectionModel().getSelectedItem();
        List<Comparable<?>> contact = contacts.get(item);

        // Make sure that execution only happens on newly selected contacts
        if (contact.equals(current_selection)){
            return;
        }
        current_selection = contact;
        set_contact_view_information(contact);
    }

    @FXML
    public void edit_contact(ActionEvent Event) {
        System.out.println("Edit btn Clicked");
//        btn_edit_contact.setDisable(false);
        btn_edit_contact.setVisible(false);

    }


}
