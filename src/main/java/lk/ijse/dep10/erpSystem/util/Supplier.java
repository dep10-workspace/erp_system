package lk.ijse.dep10.erpSystem.util;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import jdk.jfr.DataAmount;

import java.io.Serializable;
import java.util.ArrayList;

public class Supplier implements Serializable {
    private String id;
    private String name;
    private String address;
    private ArrayList<String> contact;

    public Supplier(String id, String name, String address, ArrayList<String> contact) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getContact() {
        return contact;
    }

    public void setContact(ArrayList<String> contact) {
        this.contact = contact;
    }
    public ComboBox getContactComb(){
        ComboBox cmb=new ComboBox();
        ObservableList<String>contactList= cmb.getItems();
        contactList.add("077-3017818");
        for (String s : contact) {
            contactList.add(s);

        }
        return cmb;
    }
}
