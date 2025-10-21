package com.library;
import javafx.scene.control.Separator;
import javafx.scene.control.ComboBox;
import com.library.model.Book;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Library Management System");

        // Show main menu directly
        showMainMenu();
    }

    private void showMainMenu() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("Library Management System");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Separator separator = new Separator();
        separator.setMaxWidth(300);

        Button booksBtn = new Button("Manage Books");
        Button membersBtn = new Button("Manage Members");
        Button issueBtn = new Button("Issue Book");
        Button returnBtn = new Button("Return Book");
        Button viewTransactionsBtn = new Button("View Transactions");

        // Simple, clean button style
        String buttonStyle = "-fx-font-size: 14px; -fx-min-width: 200px; -fx-min-height: 40px; " +
                "-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1px;";

        booksBtn.setStyle(buttonStyle);
        membersBtn.setStyle(buttonStyle);
        issueBtn.setStyle(buttonStyle);
        returnBtn.setStyle(buttonStyle);
        viewTransactionsBtn.setStyle(buttonStyle);

        // Hover effect
        for (Button btn : new Button[]{booksBtn, membersBtn, issueBtn, returnBtn, viewTransactionsBtn}) {
            btn.setOnMouseEntered(e -> btn.setStyle(buttonStyle + "-fx-background-color: #f5f5f5;"));
            btn.setOnMouseExited(e -> btn.setStyle(buttonStyle));
        }

        // Button actions
        booksBtn.setOnAction(e -> showBookManagement());
        membersBtn.setOnAction(e -> showMemberManagement());
        issueBtn.setOnAction(e -> showIssueBook());
        returnBtn.setOnAction(e -> showReturnBook());
        viewTransactionsBtn.setOnAction(e -> showTransactions());

        root.getChildren().addAll(titleLabel, separator, booksBtn, membersBtn, issueBtn, returnBtn, viewTransactionsBtn);

        Scene scene = new Scene(root, 700, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showBookManagement() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Title and Back button
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> showMainMenu());
        Label title = new Label("Book Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // Center: Book Table
        TableView<Book> table = new TableView<>();

        TableColumn<Book, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getBookId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        titleCol.setPrefWidth(200);

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAuthor()));
        authorCol.setPrefWidth(150);

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIsbn()));
        isbnCol.setPrefWidth(120);

        TableColumn<Book, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));
        categoryCol.setPrefWidth(120);

        TableColumn<Book, Integer> availableCol = new TableColumn<>("Available");
        availableCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAvailableCopies()).asObject());
        availableCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, titleCol, authorCol, isbnCol, categoryCol, availableCol);

        // Load data from database
        try {
            javafx.collections.ObservableList<Book> books = javafx.collections.FXCollections.observableArrayList(
                    com.library.service.DatabaseService.getInstance().getAllBooks()
            );
            table.setItems(books);
        } catch (Exception e) {
            showAlert("Error", "Failed to load books: " + e.getMessage());
        }

        root.setCenter(table);

        // Bottom: Buttons
        HBox bottomBar = new HBox(10);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER);

        Button addBtn = new Button("Add Book");
        Button refreshBtn = new Button("Refresh");
        Button deleteBtn = new Button("Delete Selected");

        // Simple style without bright colors
        addBtn.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #999;");
        refreshBtn.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #999;");
        deleteBtn.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #999;");

        addBtn.setOnAction(e -> showAddBookDialog(table));
        refreshBtn.setOnAction(e -> {
            try {
                table.setItems(javafx.collections.FXCollections.observableArrayList(
                        com.library.service.DatabaseService.getInstance().getAllBooks()
                ));
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    com.library.service.DatabaseService.getInstance().deleteBook(selected.getBookId());
                    table.getItems().remove(selected);
                    showAlert("Success", "Book deleted successfully!");
                } catch (Exception ex) {
                    showAlert("Error", "Failed to delete: " + ex.getMessage());
                }
            } else {
                showAlert("Warning", "Please select a book to delete");
            }
        });

        bottomBar.getChildren().addAll(addBtn, refreshBtn, deleteBtn);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
    }

    private void showMemberManagement() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Title and Back button
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> showMainMenu());
        Label title = new Label("Member Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // Center: Members Table
        TableView<com.library.model.Member> table = new TableView<>();

        TableColumn<com.library.model.Member, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getMemberId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<com.library.model.Member, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        nameCol.setPrefWidth(150);

        TableColumn<com.library.model.Member, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        emailCol.setPrefWidth(200);

        TableColumn<com.library.model.Member, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone()));
        phoneCol.setPrefWidth(120);

        TableColumn<com.library.model.Member, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
        addressCol.setPrefWidth(180);

        TableColumn<com.library.model.Member, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        statusCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, nameCol, emailCol, phoneCol, addressCol, statusCol);

        // Load data
        try {
            javafx.collections.ObservableList<com.library.model.Member> members =
                    javafx.collections.FXCollections.observableArrayList(
                            com.library.service.DatabaseService.getInstance().getAllMembers()
                    );
            table.setItems(members);
        } catch (Exception e) {
            showAlert("Error", "Failed to load members: " + e.getMessage());
        }

        root.setCenter(table);

        // Bottom: Buttons
        HBox bottomBar = new HBox(10);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER);

        Button addBtn = new Button("Add Member");
        Button refreshBtn = new Button("Refresh");
        Button deleteBtn = new Button("Delete Selected");

        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        refreshBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        addBtn.setOnAction(e -> showAddMemberDialog(table));

        refreshBtn.setOnAction(e -> {
            try {
                table.setItems(javafx.collections.FXCollections.observableArrayList(
                        com.library.service.DatabaseService.getInstance().getAllMembers()
                ));
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    com.library.service.DatabaseService.getInstance().deleteMember(selected.getMemberId());
                    table.getItems().remove(selected);
                    showAlert("Success", "Member deleted successfully!");
                } catch (Exception ex) {
                    showAlert("Error", "Failed to delete: " + ex.getMessage());
                }
            } else {
                showAlert("Warning", "Please select a member to delete");
            }
        });

        bottomBar.getChildren().addAll(addBtn, refreshBtn, deleteBtn);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
    }

    private void showIssueBook() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Top: Title and Back button
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> showMainMenu());
        Label title = new Label("Issue Book");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // Center: Form
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        Label bookLabel = new Label("Select Book:");
        ComboBox<String> bookCombo = new ComboBox<>();

        Label memberLabel = new Label("Select Member:");
        ComboBox<String> memberCombo = new ComboBox<>();

        Button issueBtn = new Button("Issue Book");
        issueBtn.setStyle("-fx-background-color: #e8e8e8; -fx-border-color: #aaa; -fx-font-size: 14px; -fx-min-width: 150px;");

        // Load books and members
        try {
            var books = com.library.service.DatabaseService.getInstance().getAllBooks();
            for (var book : books) {
                if (book.getAvailableCopies() > 0) {
                    bookCombo.getItems().add(book.getBookId() + " - " + book.getTitle() + " by " + book.getAuthor());
                }
            }

            var members = com.library.service.DatabaseService.getInstance().getAllMembers();
            for (var member : members) {
                memberCombo.getItems().add(member.getMemberId() + " - " + member.getName() + " (" + member.getEmail() + ")");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load data: " + e.getMessage());
        }

        issueBtn.setOnAction(e -> {
            String selectedBook = bookCombo.getValue();
            String selectedMember = memberCombo.getValue();

            if (selectedBook == null || selectedMember == null) {
                showAlert("Warning", "Please select both book and member");
                return;
            }

            try {
                // Extract IDs
                Long bookId = Long.parseLong(selectedBook.split(" - ")[0]);
                Long memberId = Long.parseLong(selectedMember.split(" - ")[0]);

                var transaction = com.library.service.DatabaseService.getInstance().issueBook(bookId, memberId);

                showAlert("Success", "Book issued successfully!\nDue Date: " + transaction.getDueDate());

                // Refresh combos
                bookCombo.getItems().clear();
                var books = com.library.service.DatabaseService.getInstance().getAllBooks();
                for (var book : books) {
                    if (book.getAvailableCopies() > 0) {
                        bookCombo.getItems().add(book.getBookId() + " - " + book.getTitle() + " by " + book.getAuthor());
                    }
                }

            } catch (Exception ex) {
                showAlert("Error", "Failed to issue book: " + ex.getMessage());
            }
        });

        grid.add(bookLabel, 0, 0);
        grid.add(bookCombo, 1, 0);
        grid.add(memberLabel, 0, 1);
        grid.add(memberCombo, 1, 1);
        grid.add(issueBtn, 1, 2);

        bookCombo.setPrefWidth(400);
        memberCombo.setPrefWidth(400);

        root.setCenter(grid);

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
    }

    private void showReturnBook() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Title and Back button
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> showMainMenu());
        Label title = new Label("Return Book");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // Center: Active Transactions Table
        TableView<com.library.model.Transaction> table = new TableView<>();

        TableColumn<com.library.model.Transaction, Long> idCol = new TableColumn<>("Trans ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getTransactionId()).asObject());
        idCol.setPrefWidth(80);

        TableColumn<com.library.model.Transaction, String> bookCol = new TableColumn<>("Book");
        bookCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getBook() != null ? data.getValue().getBook().getTitle() : "N/A"
        ));
        bookCol.setPrefWidth(200);

        TableColumn<com.library.model.Transaction, String> memberCol = new TableColumn<>("Member");
        memberCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getMember() != null ? data.getValue().getMember().getName() : "N/A"
        ));
        memberCol.setPrefWidth(150);

        TableColumn<com.library.model.Transaction, String> issueDateCol = new TableColumn<>("Issue Date");
        issueDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getIssueDate().toString()
        ));
        issueDateCol.setPrefWidth(100);

        TableColumn<com.library.model.Transaction, String> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDueDate().toString()
        ));
        dueDateCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, bookCol, memberCol, issueDateCol, dueDateCol);

        // Load active transactions
        try {
            javafx.collections.ObservableList<com.library.model.Transaction> transactions =
                    javafx.collections.FXCollections.observableArrayList(
                            com.library.service.DatabaseService.getInstance().getActiveTransactions()
                    );
            table.setItems(transactions);
        } catch (Exception e) {
            showAlert("Error", "Failed to load transactions: " + e.getMessage());
        }

        root.setCenter(table);

        // Bottom: Buttons
        HBox bottomBar = new HBox(10);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER);

        Button returnBtn = new Button("✅ Return Selected Book");
        Button refreshBtn = new Button("🔄 Refresh");

        returnBtn.setStyle("-fx-background-color: #e8e8e8; -fx-border-color: #aaa; -fx-font-size: 14px;");
        refreshBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        returnBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    com.library.service.DatabaseService.getInstance().returnBook(selected.getTransactionId());

                    // Get updated transaction to show fine
                    var updatedTransactions = com.library.service.DatabaseService.getInstance().getAllTransactions();
                    var returnedTransaction = updatedTransactions.stream()
                            .filter(t -> t.getTransactionId().equals(selected.getTransactionId()))
                            .findFirst()
                            .orElse(null);

                    String message = "Book returned successfully!";
                    if (returnedTransaction != null && returnedTransaction.getFineAmount() > 0) {
                        message += "\n\n⚠️ Fine Amount: ₹" + returnedTransaction.getFineAmount();
                    } else {
                        message += "\n\n✅ No fine (returned on time)";
                    }

                    showAlert("Success", message);

                    // Refresh table
                    table.setItems(javafx.collections.FXCollections.observableArrayList(
                            com.library.service.DatabaseService.getInstance().getActiveTransactions()
                    ));

                } catch (Exception ex) {
                    showAlert("Error", "Failed to return book: " + ex.getMessage());
                }
            } else {
                showAlert("Warning", "Please select a transaction to return");
            }
        });

        refreshBtn.setOnAction(e -> {
            try {
                table.setItems(javafx.collections.FXCollections.observableArrayList(
                        com.library.service.DatabaseService.getInstance().getActiveTransactions()
                ));
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh: " + ex.getMessage());
            }
        });

        bottomBar.getChildren().addAll(returnBtn, refreshBtn);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setScene(scene);
    }

    private void showTransactions() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> showMainMenu());
        Label title = new Label("All Transactions");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // Table
        TableView<com.library.model.Transaction> table = new TableView<>();

        TableColumn<com.library.model.Transaction, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getTransactionId()).asObject());

        TableColumn<com.library.model.Transaction, String> bookCol = new TableColumn<>("Book");
        bookCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getBook() != null ? data.getValue().getBook().getTitle() : "N/A"
        ));
        bookCol.setPrefWidth(180);

        TableColumn<com.library.model.Transaction, String> memberCol = new TableColumn<>("Member");
        memberCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getMember() != null ? data.getValue().getMember().getName() : "N/A"
        ));
        memberCol.setPrefWidth(130);

        TableColumn<com.library.model.Transaction, String> issueCol = new TableColumn<>("Issue Date");
        issueCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getIssueDate().toString()
        ));

        TableColumn<com.library.model.Transaction, String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDueDate().toString()
        ));

        TableColumn<com.library.model.Transaction, String> returnCol = new TableColumn<>("Return Date");
        returnCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getReturnDate() != null ? data.getValue().getReturnDate().toString() : "-"
        ));

        TableColumn<com.library.model.Transaction, Double> fineCol = new TableColumn<>("Fine (₹)");
        fineCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getFineAmount()).asObject());

        TableColumn<com.library.model.Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        table.getColumns().addAll(idCol, bookCol, memberCol, issueCol, dueCol, returnCol, fineCol, statusCol);

        try {
            table.setItems(javafx.collections.FXCollections.observableArrayList(
                    com.library.service.DatabaseService.getInstance().getAllTransactions()
            ));
        } catch (Exception e) {
            showAlert("Error", "Failed to load transactions: " + e.getMessage());
        }

        root.setCenter(table);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showAddBookDialog(TableView<Book> table) {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Book");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField isbnField = new TextField();
        TextField categoryField = new TextField();
        TextField copiesField = new TextField("1");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("Copies:"), 0, 4);
        grid.add(copiesField, 1, 4);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);
        grid.add(buttons, 0, 5, 2, 1);

        saveBtn.setOnAction(e -> {
            try {
                Book book = new Book(
                        titleField.getText(),
                        authorField.getText(),
                        isbnField.getText(),
                        categoryField.getText(),
                        Integer.parseInt(copiesField.getText())
                );
                com.library.service.DatabaseService.getInstance().addBook(book);

                // Refresh table
                table.setItems(javafx.collections.FXCollections.observableArrayList(
                        com.library.service.DatabaseService.getInstance().getAllBooks()
                ));

                showAlert("Success", "Book added successfully!");
                dialog.close();
            } catch (Exception ex) {
                showAlert("Error", "Failed to add book: " + ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 400, 300);
        dialog.setScene(scene);
        dialog.show();
    }

    private void showAddMemberDialog(TableView<com.library.model.Member> table) {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Member");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);
        grid.add(buttons, 0, 4, 2, 1);

        saveBtn.setOnAction(e -> {
            try {
                com.library.model.Member member = new com.library.model.Member(
                        nameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        addressField.getText()
                );
                com.library.service.DatabaseService.getInstance().addMember(member);

                table.setItems(javafx.collections.FXCollections.observableArrayList(
                        com.library.service.DatabaseService.getInstance().getAllMembers()
                ));

                showAlert("Success", "Member added successfully!");
                dialog.close();
            } catch (Exception ex) {
                showAlert("Error", "Failed to add member: " + ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.show();
    }
}