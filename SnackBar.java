import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class SnackBar {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snack Bar");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 900);

            JTabbedPane tabbedPane = new JTabbedPane();

            SnackBarPanel snackBarPanel = new SnackBarPanel();
            tabbedPane.addTab("Snack Bar", snackBarPanel);

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }

    public static class SnackBarPanel extends JPanel {
        private DefaultTableModel cartTableModel;
        private DefaultTableModel stockTableModel;
        private JLabel totalLabel;
        private double total;
        private Map<String, Item> inventory;

        public SnackBarPanel() {
            setLayout(new BorderLayout());
            loadInventory();
            initializeUI();
        }

        private void loadInventory() {
            inventory = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("StockInventory.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.isBlank()) {
                        String[] parts = line.split(" \\| "); // Adjusted to match new format
                        if (parts.length == 4) {
                            String itemName = parts[0].trim();
                            double price = Double.parseDouble(parts[1].trim());
                            int quantity = Integer.parseInt(parts[2].trim());
                            String category = parts[3].trim();
                            inventory.put(itemName, new Item(itemName, price, quantity, category));
                        }
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading inventory: " + e.getMessage());
            }
        }

        private void updateInventoryFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("StockInventory.txt"))) {
                for (Item item : inventory.values()) {
                    writer.write(item.name + " | " + item.price + " | " + item.quantity + " | " + item.category + "\n");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error updating inventory file: " + e.getMessage());
            }
        }

        private void initializeUI() {
            JLabel title = new JLabel("Snack Bar System", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 24));
            add(title, BorderLayout.NORTH);

            JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 10));

            // Stock Table Panel
            JPanel stockPanel = new JPanel(new BorderLayout());
            stockPanel.setBorder(BorderFactory.createTitledBorder("Stock"));

            stockTableModel = new DefaultTableModel(new String[]{"Item", "Stock"}, 0);
            JTable stockTable = new JTable(stockTableModel);
            inventory.forEach((item, details) -> stockTableModel.addRow(new Object[]{item, details.quantity}));

            stockTable.setFont(new Font("Arial", Font.BOLD, 18));
            stockTable.setRowHeight(30);
            JTableHeader stockHeader = stockTable.getTableHeader();
            stockHeader.setFont(new Font("Arial", Font.BOLD, 18));

            stockPanel.add(new JScrollPane(stockTable), BorderLayout.CENTER);

            // Cart Panel
            JPanel cartPanel = new JPanel(new BorderLayout());
            cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));

            cartTableModel = new DefaultTableModel(new String[]{"Item", "Quantity", "Price (Php)"}, 0);
            JTable cartTable = new JTable(cartTableModel);

            JPanel cartFooter = new JPanel(new BorderLayout());
            totalLabel = new JLabel("Total: Php 0.00", JLabel.RIGHT);
            JButton clearCartButton = new JButton("Clear Cart");
            JButton checkoutButton = new JButton("Checkout");

            clearCartButton.addActionListener(e -> {
                cartTableModel.setRowCount(0);
                total = 0.0;
                totalLabel.setText("Total: Php 0.00");
            });

            checkoutButton.addActionListener(e -> {
                if (total > 0) {
                    StringBuilder receipt = new StringBuilder();
                    for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                        receipt.append(cartTableModel.getValueAt(i, 0))
                                .append(" x").append(cartTableModel.getValueAt(i, 1))
                                .append(" - ").append(cartTableModel.getValueAt(i, 2)).append("\n");
                    }

                    // Log the transaction to the file
                    logTransaction(receipt.toString());

                    // Show receipt
                    JOptionPane.showMessageDialog(this, "Thank you for your purchase!\nTotal: Php " + String.format("%.2f", total));
                    cartTableModel.setRowCount(0);
                    total = 0.0;
                    totalLabel.setText("Total: Php 0.00");
                    updateInventoryFile();
                } else {
                    JOptionPane.showMessageDialog(this, "Your cart is empty!");
                }
            });

            cartFooter.add(totalLabel, BorderLayout.CENTER);
            cartFooter.add(clearCartButton, BorderLayout.WEST);
            cartFooter.add(checkoutButton, BorderLayout.EAST);

            cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
            cartPanel.add(cartFooter, BorderLayout.SOUTH);

            // Panel for Adding Items
            JPanel selectionPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            selectionPanel.setBorder(BorderFactory.createTitledBorder("Add Items"));

            JComboBox<String> snackComboBox = new JComboBox<>(getSnacks());
            JSpinner snackQuantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
            JButton addSnackButton = new JButton("Add Snack");

            JComboBox<String> drinkComboBox = new JComboBox<>(getDrinks());
            JSpinner drinkQuantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
            JButton addDrinkButton = new JButton("Add Drink");

            selectionPanel.add(new JLabel("Select Snack:"));
            selectionPanel.add(snackComboBox);
            selectionPanel.add(new JLabel("Quantity:"));
            selectionPanel.add(snackQuantitySpinner);
            selectionPanel.add(new JLabel("Select Drink:"));
            selectionPanel.add(drinkComboBox);
            selectionPanel.add(new JLabel("Quantity:"));
            selectionPanel.add(drinkQuantitySpinner);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.add(addSnackButton);
            buttonPanel.add(addDrinkButton);

            JPanel southPanel = new JPanel(new BorderLayout());
            southPanel.add(selectionPanel, BorderLayout.CENTER);
            southPanel.add(buttonPanel, BorderLayout.SOUTH);

            add(southPanel, BorderLayout.SOUTH);

            // Button Actions
            addSnackButton.addActionListener(e -> {
                String itemName = snackComboBox.getSelectedItem().toString();
                int quantity = (int) snackQuantitySpinner.getValue();
                if (inventory.get(itemName).quantity < quantity) {
                    JOptionPane.showMessageDialog(this, "Insufficient stock for " + itemName);
                    return;
                }
                inventory.get(itemName).quantity -= quantity;
                updateStockTable();
                double price = inventory.get(itemName).price * quantity;
                cartTableModel.addRow(new Object[]{itemName, quantity, String.format("Php %.2f", price)});
                total += price;
                totalLabel.setText("Total: Php " + String.format("%.2f", total));
            });

            addDrinkButton.addActionListener(e -> {
                String itemName = drinkComboBox.getSelectedItem().toString();
                int quantity = (int) drinkQuantitySpinner.getValue();
                if (inventory.get(itemName).quantity < quantity) {
                    JOptionPane.showMessageDialog(this, "Insufficient stock for " + itemName);
                    return;
                }
                inventory.get(itemName).quantity -= quantity;
                updateStockTable();
                double price = inventory.get(itemName).price * quantity;
                cartTableModel.addRow(new Object[]{itemName, quantity, String.format("Php %.2f", price)});
                total += price;
                totalLabel.setText("Total: Php " + String.format("%.2f", total));
            });

            centerPanel.add(stockPanel);
            centerPanel.add(cartPanel);

            add(centerPanel, BorderLayout.CENTER);
        }

        private void updateStockTable() {
            stockTableModel.setRowCount(0);
            inventory.forEach((item, details) -> stockTableModel.addRow(new Object[]{item, details.quantity}));
        }

        private String[] getSnacks() {
            return inventory.values().stream()
                    .filter(item -> item.category.equalsIgnoreCase("Snack"))
                    .map(item -> item.name)
                    .toArray(String[]::new);
        }

        private String[] getDrinks() {
            return inventory.values().stream()
                    .filter(item -> item.category.equalsIgnoreCase("Drink"))
                    .map(item -> item.name)
                    .toArray(String[]::new);
        }

        // Log transaction in SnackBarTransaction.txt
        private void logTransaction(String receipt) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("SnackBarTransaction.txt", true))) {
                writer.write("Transaction on " + LocalDateTime.now() + "\n");
                writer.write(receipt);
                writer.write("Total: Php " + String.format("%.2f", total) + "\n");
                writer.write("---------------------------------------------------\n");
            } catch (IOException e) {
                System.out.println("Error saving transaction log: " + e.getMessage());
            }
        }
    }

    static class Item {
        String name;
        double price;
        int quantity;
        String category;

        Item(String name, double price, int quantity, String category) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.category = category;
        }
    }
}
