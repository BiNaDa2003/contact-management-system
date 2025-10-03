
package com.java.contactmanagementsystemgui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

    public void addContact(Contact contact) {
        String sql = "INSERT INTO contacts(name, phone_number, email, address) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getPhoneNumber());
            pstmt.setString(3, contact.getEmail());
            pstmt.setString(4, contact.getAddress());
            pstmt.executeUpdate();
            System.out.println("Contact added to database: " + contact.getName());
        } catch (SQLException e) {
            System.err.println("Error adding contact: " + e.getMessage()); // System.err.println to print to standard error stream
        }
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        
        String sql = "SELECT id, name, phone_number, email, address FROM contacts ORDER BY name";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Contact contact = new Contact(
                    rs.getInt("id"), 
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    rs.getString("address")
                );
                contacts.add(contact);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all contacts: " + e.getMessage());
        }
        return contacts;
    }

    public Contact getContactByName(String name) {
        String sql = "SELECT id, name, phone_number, email, address FROM contacts WHERE name = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Contact(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    rs.getString("address")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error searching contact: " + e.getMessage());
        }
        return null; 
    }

    
    public boolean updateContact(int id, Contact newContactData) {
        String sql = "UPDATE contacts SET name = ?, phone_number = ?, email = ?, address = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newContactData.getName());
            pstmt.setString(2, newContactData.getPhoneNumber());
            pstmt.setString(3, newContactData.getEmail());
            pstmt.setString(4, newContactData.getAddress());
            pstmt.setInt(5, id); 

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Contact updated in database: ID " + id);
                return true;
            } else {
                System.out.println("No contact found with ID: " + id + " for update.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error updating contact: " + e.getMessage());
            return false;
        }
    }

    
    public boolean deleteContact(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Contact deleted from database: ID " + id);
                return true;
            } else {
                System.out.println("No contact found with ID: " + id + " for deletion.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting contact: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean doesContactNameExist(String name) {
        String sql = "SELECT COUNT(*) FROM contacts WHERE name = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking contact name existence: " + e.getMessage());
        }
        return false;
    }
}


