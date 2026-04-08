package com.hotel;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application { // Creates observable lists to bind UI tables to data. HotelManager handles
                                           // all business logic. isDarkMode toggles theme. primaryWindow is the main
                                           // app window.

    private final HotelManager manager = new HotelManager();

    private final ObservableList<Room> roomData = FXCollections.observableArrayList();
    private final ObservableList<Booking> bookingData = FXCollections.observableArrayList();

    private boolean isDarkMode = true;
    private Scene mainScene;
    private Stage primaryWindow;

    @Override
    public void start(Stage stage) { // JavaFX calls start() when app launches. Sets the primary window and shows
                                     // login. launch() is the entry point that calls start()
        primaryWindow = stage;
        showLoginScreen();
    }

    private void showLoginScreen() { // Creates a centered VBox with title, username/password fields, and a login
                                     // button. On success, loads the main dashboard. Checks hardcoded credentials.
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #2E000B;");

        Label title = new Label("Grand Vista");
        title.setStyle(
                "-fx-font-size: 36px; -fx-text-fill: #FFFBF0; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

        Label sub = new Label("Management System");
        sub.setStyle("-fx-font-size: 16px; -fx-text-fill: #C5A059; -fx-font-family: 'Georgia';");

        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(300);
        form.setPadding(new Insets(30));
        form.setStyle("-fx-background-color: #FFFBF0; -fx-background-radius: 8;");

        Label loginLbl = new Label("LOGIN");
        loginLbl.setStyle(
                "-fx-font-size: 20px; -fx-text-fill: #800020; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

        TextField userFld = new TextField();
        userFld.setPromptText("Username (admin)");
        userFld.setStyle("-fx-background-color: white; -fx-border-color: #800020; -fx-text-fill: #1a1a1a;");

        PasswordField passFld = new PasswordField();
        passFld.setPromptText("Password (admin)");
        passFld.setStyle("-fx-background-color: white; -fx-border-color: #800020; -fx-text-fill: #1a1a1a;");

        Label errorLbl = new Label("");
        errorLbl.setTextFill(Color.RED);

        Button loginBtn = new Button("Sign In");
        loginBtn.setStyle(
                "-fx-background-color: #800020; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> {
            if ("admin".equals(userFld.getText()) && "admin".equals(passFld.getText())) {
                showMainDashboard();
            } else {
                errorLbl.setText("Invalid credentials! (Use admin/admin)");
            }
        });

        form.getChildren().addAll(loginLbl, userFld, passFld, errorLbl, loginBtn);
        root.getChildren().addAll(title, sub, new Label(""), form);

        Scene loginScene = new Scene(root, 650, 500);
        primaryWindow.setTitle("Grand Vista — Login");
        primaryWindow.setScene(loginScene);
        primaryWindow.setMinWidth(600);
        primaryWindow.setMinHeight(450);
        primaryWindow.show();
    }

    private BorderPane mainLayout;

    private void showMainDashboard() { // BorderPane has five regions (Top, Bottom, Left, Right, Center). Sidebar goes
                                       // left, content goes center. Scene is 1100x700 pixels.
        refreshRoomData();
        refreshBookingData();

        mainLayout = new BorderPane();

        // ═══ LEFT SIDEBAR ═══
        VBox sidebar = new VBox(5);
        sidebar.setPrefWidth(180);
        sidebar.setPadding(new Insets(20, 15, 20, 15));
        sidebar.setStyle("-fx-background-color: #1A0005;");
        sidebar.setAlignment(Pos.TOP_LEFT);

        // Brand
        Label brandName = new Label("Grand Vista");
        brandName.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #C5A059; -fx-font-family: 'Georgia';");
        Label brandSub = new Label("HOTEL MANAGEMENT");
        brandSub.setStyle("-fx-font-size: 9px; -fx-text-fill: rgba(255,251,240,0.5); -fx-font-family: 'Georgia';");
        VBox brandBox = new VBox(2, brandName, brandSub);
        brandBox.setPadding(new Insets(0, 0, 25, 0));

        // Nav buttons: Creates navigation buttons. setActive clears all buttons' active
        // state, then highlights the clicked one. When clicked, updates center panel
        // with new content.
        Button btnDash = sidebarButton("🏨  Dashboard");
        Button btnRooms = sidebarButton("🛏  Rooms");
        Button btnBooking = sidebarButton("📋  Bookings");
        Button btnCheckout = sidebarButton("💳  Checkout & Billing");
        Button btnBilling = sidebarButton("🧾  Billing");
        Button btnAnalytics = sidebarButton("📈  Analytics");
        Button btnHistory = sidebarButton("📊  Reports");

        // Active state tracking
        final Button[] activeBtn = { btnDash };
        Runnable clearActive = () -> {
            for (Button b : new Button[] { btnDash, btnRooms, btnBooking, btnCheckout, btnBilling, btnAnalytics,
                    btnHistory }) {
                b.setStyle(sidebarButtonStyle(false));
            }
        };

        java.util.function.Consumer<Button> setActive = (btn) -> {
            clearActive.run();
            btn.setStyle(sidebarButtonStyle(true));
            activeBtn[0] = btn;
        };

        btnDash.setStyle(sidebarButtonStyle(true)); // default active

        btnDash.setOnAction(e -> {
            setActive.accept(btnDash);
            mainLayout.setCenter(buildDashboard());
        });
        btnRooms.setOnAction(e -> {
            setActive.accept(btnRooms);
            mainLayout.setCenter(wrapInScroll(buildRoomTab()));
        });
        btnBooking.setOnAction(e -> {
            setActive.accept(btnBooking);
            mainLayout.setCenter(wrapInScroll(buildBookingTab()));
        });
        btnCheckout.setOnAction(e -> {
            setActive.accept(btnCheckout);
            mainLayout.setCenter(wrapInScroll(buildCheckoutTab()));
        });
        btnBilling.setOnAction(e -> {
            setActive.accept(btnBilling);
            mainLayout.setCenter(wrapInScroll(buildBillingTab()));
        });
        btnAnalytics.setOnAction(e -> {
            setActive.accept(btnAnalytics);
            mainLayout.setCenter(buildAnalyticsTab());
        });
        btnHistory.setOnAction(e -> {
            setActive.accept(btnHistory);
            mainLayout.setCenter(wrapInScroll(buildHistoryTab()));
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footerLbl = new Label("v1.0 · JavaFX · Maven\n© 2024 Grand Vista HMS");
        footerLbl.setStyle("-fx-text-fill: rgba(255,251,240,0.3); -fx-font-size: 9px;");

        sidebar.getChildren().addAll(brandBox,
                btnDash, btnRooms, btnBooking, btnCheckout, btnBilling, btnAnalytics, btnHistory,
                spacer, footerLbl);

        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(buildDashboard());

        mainScene = new Scene(mainLayout, 1100, 700);
        mainScene.getStylesheets().add(getClass().getResource("/com/hotel/style.css").toExternalForm());

        primaryWindow.setTitle("Grand Vista — Hotel Management System");
        primaryWindow.setScene(mainScene);
        primaryWindow.setMinWidth(950);
        primaryWindow.setMinHeight(620);
        applyTheme();
    }

    private String sidebarButtonStyle(boolean active) { // Active buttons are burgundy with gold text and left border.
                                                        // Inactive are transparent. Hover changes text color if not
                                                        // active.
        if (active) {
            return "-fx-background-color: #800020; -fx-text-fill: #C5A059; -fx-font-size: 12px;" +
                    "-fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 15; -fx-background-radius: 6;" +
                    "-fx-alignment: center-left; -fx-max-width: 999; -fx-border-color: #C5A059; -fx-border-width: 0 0 0 3; -fx-border-radius: 6;";
        }
        return "-fx-background-color: transparent; -fx-text-fill: rgba(255,251,240,0.6); -fx-font-size: 12px;" +
                "-fx-font-weight: normal; -fx-cursor: hand; -fx-padding: 10 15; -fx-background-radius: 6;" +
                "-fx-alignment: center-left; -fx-max-width: 999;";
    }

    private Button sidebarButton(String text) {
        Button b = new Button(text);
        b.setStyle(sidebarButtonStyle(false));
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setOnMouseEntered(e -> {
            if (!b.getStyle().contains("#800020"))
                b.setStyle(sidebarButtonStyle(false) + "-fx-text-fill: #FFFBF0;");
        });
        b.setOnMouseExited(e -> {
            if (!b.getStyle().contains("#800020"))
                b.setStyle(sidebarButtonStyle(false));
        });
        return b;
    }

    private ScrollPane wrapInScroll(VBox content) { // Wraps VBox in ScrollPane with transparent background.
                                                    // setFitToWidth() makes content expand to scroll pane width.
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return sp;
    }

    private void applyTheme() {
        if (isDarkMode) {
            mainScene.getRoot().getStyleClass().remove("light-mode");
        } else {
            if (!mainScene.getRoot().getStyleClass().contains("light-mode")) {
                mainScene.getRoot().getStyleClass().add("light-mode");
            }
        }
    }

    private VBox buildDashboard() {
        VBox outerRoot = new VBox();
        outerRoot.getStyleClass().add("dashboard");

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox root = new VBox(25);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_LEFT);

        // ── Top bar: title + theme toggle ──
        Button themeToggle = new Button("☀ Light Mode");
        themeToggle.getStyleClass().add("theme-toggle");
        themeToggle.setOnAction(e -> {
            isDarkMode = !isDarkMode;
            themeToggle.setText(isDarkMode ? "☀ Light Mode" : "🌙 Dark Mode");
            applyTheme();
        });

        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");
        title.getStyleClass().add("hotel-title");

        Label welcomeSub = new Label("Welcome back — Grand Vista Hotel Management");
        welcomeSub.getStyleClass().add("hotel-sub");

        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        VBox titleBlock = new VBox(2, title, welcomeSub);
        titleRow.getChildren().addAll(titleBlock, spacer1, themeToggle);

        // ── Stat Cards Row ──: Streams through manager data to calculate totals.
        // Creates stat cards using dashStatCard() factory. HBox arranges them
        // horizontally
        int totalRooms = manager.getAllRooms().size();
        int availRooms = manager.getAvailableRooms().size();
        int occupiedRooms = totalRooms - availRooms;
        long activeBookingsCount = manager.getAllBookings().stream()
                .filter(b -> b.getStatus().equals("Active")).count();
        double totalRevenue = manager.getAllBookings().stream()
                .filter(b -> b.getStatus().equals("Checked Out"))
                .mapToDouble(Booking::getTotalBill).sum();

        HBox statsRow = new HBox(15);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        VBox cardTotal = dashStatCard("🏨", String.valueOf(totalRooms), "Total Rooms");
        VBox cardAvail = dashStatCard("✔", String.valueOf(availRooms), "Available");
        VBox cardOccupied = dashStatCard("🔑", String.valueOf(occupiedRooms), "Occupied");
        VBox cardBookings = dashStatCard("📋", String.valueOf(activeBookingsCount), "Active Bookings");
        VBox cardRevenue = dashStatCard("💰", String.format("₹%.2f", totalRevenue), "Total Revenue");

        statsRow.getChildren().addAll(cardTotal, cardAvail, cardOccupied, cardBookings, cardRevenue);
        for (var node : statsRow.getChildren()) {
            HBox.setHgrow(node, Priority.ALWAYS);
            ((VBox) node).setMaxWidth(Double.MAX_VALUE);
        }

        // ── Room Status Overview ──
        Label roomsTitle = new Label("Room Status Overview");
        roomsTitle.getStyleClass().add("section-heading");

        FlowPane roomGrid = new FlowPane(12, 12); // FlowPane arranges cards in a grid. Each room gets a card with
                                                  // dynamic colors (green = available, red = occupied). Loop builds all
                                                  // room cards.
        roomGrid.setPadding(new Insets(5, 0, 0, 0));

        for (Room r : manager.getAllRooms()) {
            VBox roomCard = new VBox(4);
            roomCard.setAlignment(Pos.CENTER);
            roomCard.setPrefWidth(155);
            roomCard.setPadding(new Insets(14, 10, 14, 10));

            String statusText = r.isAvailable() ? "AVAILABLE" : "OCCUPIED";
            String borderColor = r.isAvailable() ? "#2A7A4B" : "#800020";
            String bgColor, textColor, statusColor;

            if (isDarkMode) {
                bgColor = r.isAvailable() ? "#1A3A2A" : "#3D0011";
                textColor = "#FFFBF0";
                statusColor = r.isAvailable() ? "#4CAF50" : "#E74C3C";
            } else {
                bgColor = r.isAvailable() ? "#E8F5E9" : "#FFEBEE";
                textColor = "#1a1a1a";
                statusColor = r.isAvailable() ? "#2E7D32" : "#C62828";
            }

            roomCard.setStyle("-fx-background-color: " + bgColor + "; -fx-border-color: " + borderColor +
                    "; -fx-border-width: 2; -fx-background-radius: 6; -fx-border-radius: 6;");

            Label roomNum = new Label("Room " + r.getRoomNumber());
            roomNum.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + textColor
                    + "; -fx-font-family: 'Georgia';");

            Label roomType = new Label(r.getRoomType().toUpperCase());
            roomType.setStyle("-fx-font-size: 10px; -fx-text-fill: " + textColor + ";");

            Label roomStatus = new Label(statusText);
            roomStatus.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + statusColor + ";");

            Label roomPrice = new Label("₹" + (int) r.getPricePerDay() + "/night");
            roomPrice.setStyle("-fx-font-size: 11px; -fx-text-fill: #C5A059;");

            roomCard.getChildren().addAll(roomNum, roomType, roomStatus, roomPrice);
            roomGrid.getChildren().add(roomCard);
        }

        // ── Active Bookings Table ──
        Label bookingsTitle = new Label("Active Bookings");
        bookingsTitle.getStyleClass().add("section-heading");

        TableView<Booking> activeTable = buildBookingTable(); // PropertyValueFactory binds table columns to object
                                                              // properties via reflection. setItems() connects
                                                              // observable list. Table grows to fill available space.
        ObservableList<Booking> activeGuests = FXCollections.observableArrayList(
                manager.getAllBookings().stream()
                        .filter(b -> b.getStatus().equals("Active"))
                        .collect(Collectors.toList()));
        activeTable.setItems(activeGuests);
        activeTable.setPrefHeight(180);
        activeTable.setMinHeight(120);

        // ── Refresh btn ──
        Button refreshBtn = styledButton("🔄  Refresh Dashboard", "#800020");
        refreshBtn.setOnAction(e -> showMainDashboard());

        // ── Footer ──
        Label footer = new Label("v1.0 · JavaFX · Grand Vista HMS");
        footer.setStyle("-fx-text-fill: #666; -fx-font-size: 10px;");

        root.getChildren().addAll(titleRow, statsRow, roomsTitle, roomGrid,
                bookingsTitle, activeTable, refreshBtn, footer);
        scroll.setContent(root);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        outerRoot.getChildren().add(scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return outerRoot;
    }

    private VBox dashStatCard(String icon, String value, String label) { // Returns a VBox card with icon, large value,
                                                                         // and label. derive(-fx-base, 20%) lightens
                                                                         // the background color dynamically.
        VBox card = new VBox(4);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setStyle("-fx-background-color: derive(-fx-base, 20%); -fx-border-color: #800020;" +
                "-fx-border-width: 1; -fx-background-radius: 6; -fx-border-radius: 6;");

        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 18px;");

        Label valueLbl = new Label(value);
        valueLbl.setStyle(
                "-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-family: 'Georgia'; -fx-text-fill: #C5A059;");

        Label labelLbl = new Label(label);
        labelLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: derive(-fx-text-background-color, -20%);");

        card.getChildren().addAll(iconLbl, valueLbl, labelLbl);
        return card;
    }

    private VBox buildRoomTab() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label heading = sectionHeading("Room Management");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);
        form.getStyleClass().add("form-pane");

        TextField tfNumber = new TextField(); // Form collects room data. On button click, validates inputs, creates
                                              // Room object, calls manager, refreshes table, shows success message.
                                              // Catches exceptions and displays errors.
        tfNumber.setPromptText("e.g. 401");

        ComboBox<String> cbType = new ComboBox<>();
        cbType.getItems().addAll("Single", "Double", "Deluxe");
        cbType.setPromptText("Select type");
        cbType.setPrefWidth(160);

        TextField tfPrice = new TextField();
        tfPrice.setPromptText("e.g. 2000");

        form.add(new Label("Room Number:"), 0, 0);
        form.add(tfNumber, 1, 0);
        form.add(new Label("Room Type:"), 0, 1);
        form.add(cbType, 1, 1);
        form.add(new Label("Price/Day ₹:"), 0, 2);
        form.add(tfPrice, 1, 2);

        Label msgAdd = feedbackLabel();

        Button btnAdd = styledButton("➕  Add Room", "#800020");
        Button btnClear = styledButton("✖  Clear", "#666666");
        HBox btnRow = new HBox(10, btnAdd, btnClear);

        btnAdd.setOnAction(e -> {
            try {
                int num = Integer.parseInt(tfNumber.getText().trim());
                String type = cbType.getValue();
                double price = Double.parseDouble(tfPrice.getText().trim());
                if (type == null)
                    throw new Exception("Please select a room type.");
                if (manager.roomNumberExists(num))
                    throw new Exception("Room " + num + " already exists.");
                manager.addRoom(new Room(num, type, price));
                refreshRoomData();
                setMsg(msgAdd, "✔  Room " + num + " added successfully.", true);
                tfNumber.clear();
                cbType.setValue(null);
                tfPrice.clear();
            } catch (NumberFormatException ex) {
                setMsg(msgAdd, "✘  Please enter valid numbers.", false);
            } catch (Exception ex) {
                setMsg(msgAdd, "✘  " + ex.getMessage(), false);
            }
        });

        btnClear.setOnAction(e -> {
            tfNumber.clear();
            cbType.setValue(null);
            tfPrice.clear();
            msgAdd.setText("");
        });

        TableView<Room> table = buildRoomTable();
        table.setItems(roomData);
        VBox.setVgrow(table, Priority.ALWAYS);

        Button btnAll = styledButton("All Rooms", "#800020");
        Button btnAvail = styledButton("Available Only", "#555555");
        btnAll.setOnAction(e -> table.setItems(roomData));
        btnAvail.setOnAction(e -> {
            ObservableList<Room> avail = FXCollections.observableArrayList(manager.getAvailableRooms());
            table.setItems(avail);
        });

        HBox filterRow = new HBox(10, new Label("Filter:"), btnAll, btnAvail);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().addAll(heading, form, btnRow, msgAdd, new Separator(), filterRow, table);
        return root;
    }

    private VBox buildBookingTab() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label heading = sectionHeading("New Booking");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);
        form.getStyleClass().add("form-pane");

        TextField tfName = new TextField();
        tfName.setPromptText("Customer Name"); // Streams available rooms into combo box. On booking, validates dates,
                                               // calls manager.bookRoom(), refreshes both booking table and available
                                               // rooms list.
        TextField tfContact = new TextField();
        tfContact.setPromptText("Contact Number");
        ComboBox<Integer> cbRoom = new ComboBox<>();
        cbRoom.setPromptText("Select Room");
        DatePicker dpCheckIn = new DatePicker();
        DatePicker dpCheckOut = new DatePicker();

        Label msgBook = feedbackLabel();

        // REFRESH button to update available rooms
        Button btnRefreshRooms = styledButton("🔄  Refresh Available Rooms", "#800020");
        btnRefreshRooms.setOnAction(e -> {
            ObservableList<Integer> availRoomNumbers = FXCollections
                    .observableArrayList(manager.getAvailableRooms().stream()
                            .map(Room::getRoomNumber).toList());
            cbRoom.setItems(availRoomNumbers);
            setMsg(msgBook, "✔  Room list refreshed. " + availRoomNumbers.size() + " rooms available.", true);
        });

        // Initial load
        ObservableList<Integer> availRoomNumbers = FXCollections
                .observableArrayList(manager.getAvailableRooms().stream()
                        .map(Room::getRoomNumber).toList());
        cbRoom.setItems(availRoomNumbers);

        form.add(new Label("Customer Name:"), 0, 0);
        form.add(tfName, 1, 0);
        form.add(new Label("Contact Number:"), 0, 1);
        form.add(tfContact, 1, 1);
        form.add(new Label("Room Number:"), 0, 2);
        form.add(cbRoom, 1, 2);
        form.add(new Label("Check-In Date:"), 0, 3);
        form.add(dpCheckIn, 1, 3);
        form.add(new Label("Check-Out Date:"), 0, 4);
        form.add(dpCheckOut, 1, 4);

        Button btnBook = styledButton("✓  Create Booking", "#27AE60");
        Button btnClear = styledButton("✖  Clear", "#666666");
        HBox btnRow = new HBox(10, btnBook, btnClear, btnRefreshRooms);

        btnBook.setOnAction(e -> {
            try {
                String name = tfName.getText().trim();
                String contact = tfContact.getText().trim();
                Integer roomNo = cbRoom.getValue();
                LocalDate checkIn = dpCheckIn.getValue();
                LocalDate checkOut = dpCheckOut.getValue();

                if (name.isEmpty() || contact.isEmpty() || roomNo == null || checkIn == null || checkOut == null)
                    throw new Exception("Please fill all fields.");
                if (!checkOut.isAfter(checkIn))
                    throw new Exception("Check-out must be after check-in.");

                Booking booking = manager.bookRoom(name, contact, roomNo, checkIn, checkOut);
                refreshBookingData();

                // Refresh available rooms
                ObservableList<Integer> updatedRooms = FXCollections
                        .observableArrayList(manager.getAvailableRooms().stream()
                                .map(Room::getRoomNumber).toList());
                cbRoom.setItems(updatedRooms);

                setMsg(msgBook, "✔  Booking ID " + booking.getBookingId() + " created successfully!", true);
                tfName.clear();
                tfContact.clear();
                cbRoom.setValue(null);
                dpCheckIn.setValue(null);
                dpCheckOut.setValue(null);
            } catch (Exception ex) {
                setMsg(msgBook, "✘  " + ex.getMessage(), false);
            }
        });

        btnClear.setOnAction(e -> {
            tfName.clear();
            tfContact.clear();
            cbRoom.setValue(null);
            dpCheckIn.setValue(null);
            dpCheckOut.setValue(null);
            msgBook.setText("");
        });

        TableView<Booking> table = buildBookingTable();
        table.setItems(bookingData);
        VBox.setVgrow(table, Priority.ALWAYS);

        root.getChildren().addAll(heading, form, btnRow, msgBook, new Separator(),
                new Label("Current Bookings:"), table);
        return root;
    }

    private VBox buildCheckoutTab() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label heading = sectionHeading("Guest Checkout");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.getStyleClass().add("form-pane");

        TextField tfBookingId = new TextField(); // Lookup button streams through bookings to find by ID. If found,
                                                 // enables checkout button and stores ID in array. Checkout button
                                                 // calls manager.checkout(), refreshes data, clears form.
        tfBookingId.setPromptText("Enter Booking ID");

        Label msgCO = feedbackLabel();

        Button btnLookup = styledButton("🔍  Lookup", "#800020");
        Button btnCheckout = styledButton("✓  Process Checkout", "#27AE60");
        btnCheckout.setDisable(true);

        final int[] selectedId = { -1 };

        btnLookup.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tfBookingId.getText().trim());
                Optional<Booking> bOpt = manager.getAllBookings().stream()
                        .filter(b -> b.getBookingId() == id && b.getStatus().equals("Active"))
                        .findFirst();
                if (bOpt.isEmpty())
                    throw new Exception("No active booking found with ID " + id);
                selectedId[0] = id;
                btnCheckout.setDisable(false);
                setMsg(msgCO, "✔  Booking found. Ready to checkout.", true);
            } catch (NumberFormatException ex) {
                setMsg(msgCO, "✘  Please enter a valid numeric Booking ID.", false);
                btnCheckout.setDisable(true);
            } catch (Exception ex) {
                setMsg(msgCO, "✘  " + ex.getMessage(), false);
                btnCheckout.setDisable(true);
            }
        });

        btnCheckout.setOnAction(e -> {
            try {
                Booking b = manager.checkout(selectedId[0]);
                refreshRoomData();
                refreshBookingData();
                setMsg(msgCO, "✔  Checkout successful! Room " + b.getRoomNumber() + " is now available.", true);
                btnCheckout.setDisable(true);
                selectedId[0] = -1;
                tfBookingId.clear();
            } catch (Exception ex) {
                setMsg(msgCO, "✘  " + ex.getMessage(), false);
            }
        });

        form.add(new Label("Booking ID:"), 0, 0);
        form.add(tfBookingId, 1, 0);

        Label subHead = sectionHeading("Active Bookings (Reference)");
        TableView<Booking> bTable = buildBookingTable();
        bTable.setItems(FXCollections.observableArrayList(manager.getActiveBookings()));
        bTable.setPrefHeight(200);
        bTable.setMinHeight(150);
        VBox.setVgrow(bTable, Priority.ALWAYS);

        Button btnRefresh = styledButton("🔄 Refresh List", "#800020");
        btnRefresh.setOnAction(ev -> bTable.setItems(FXCollections.observableArrayList(manager.getActiveBookings())));

        // Form section
        VBox formSection = new VBox(10, heading, form, new HBox(10, btnLookup, btnCheckout), msgCO);
        formSection
                .setStyle("-fx-border-color: #800020; -fx-border-width: 1; -fx-padding: 15; -fx-background-radius: 8;");

        // Bookings table section
        VBox tableSection = new VBox(10, new Separator(), subHead, btnRefresh, bTable);

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        VBox.setVgrow(tableSection, Priority.ALWAYS);
        mainLayout.getChildren().addAll(formSection, tableSection);
        return mainLayout;
    }

    private VBox buildBillingTab() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label heading = sectionHeading("🧾  Generate Billing Invoice");

        HBox mainContent = new HBox(15);
        mainContent.setFillHeight(true);
        mainContent.setPrefHeight(500);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // ============ LEFT PANEL (50% width) ============
        VBox leftPanel = new VBox(12);
        leftPanel.setPrefWidth(400);
        leftPanel
                .setStyle("-fx-border-color: #800020; -fx-border-width: 1; -fx-padding: 15; -fx-background-radius: 6;");

        Label leftTitle = new Label("Select Booking & Services");
        leftTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField tfBookingId = new TextField(); // Finds booking by ID, displays customer name. ComboBox selects
                                                 // services with predefined prices. Spinner sets quantity. Add button
                                                 // creates ServiceItem and adds to observable list.

        tfBookingId.setPromptText("Booking ID");
        Button btnFind = styledButton("Find", "#800020");

        final Booking[] selectedBooking = { null };
        Label lblBookingInfo = new Label("No booking selected");
        lblBookingInfo.setStyle("-fx-text-fill: #999; -fx-font-size: 11;");

        final Runnable[] updateBillRef = { null };

        btnFind.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tfBookingId.getText().trim());
                Optional<Booking> bOpt = manager.getAllBookings().stream()
                        .filter(b -> b.getBookingId() == id).findFirst();
                if (bOpt.isEmpty())
                    throw new Exception("Booking not found");
                selectedBooking[0] = bOpt.get();
                lblBookingInfo.setText("✔ Booking #" + id + " - " + selectedBooking[0].getCustomerName());
                lblBookingInfo.setStyle("-fx-text-fill: #27AE60; -fx-font-size: 11;");
                if (updateBillRef[0] != null)
                    updateBillRef[0].run();
            } catch (Exception ex) {
                selectedBooking[0] = null;
                lblBookingInfo.setText("✘ " + ex.getMessage());
                lblBookingInfo.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11;");
            }
        });

        form.add(new Label("Booking ID:"), 0, 0);
        form.add(tfBookingId, 1, 0);
        form.add(btnFind, 2, 0);
        form.add(lblBookingInfo, 0, 1, 3, 1);

        // Services section
        Label servicesLabel = new Label("Add Services");
        servicesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");

        ComboBox<String> cbService = new ComboBox<>();
        cbService.getItems().addAll("Not Applicable", "Spa", "Lunch/Dinner", "Gym", "Laundry", "Airport Transfer");
        cbService.setPromptText("Select Service");
        cbService.setPrefWidth(180);

        Spinner<Integer> spinQty = new Spinner<>(1, 10, 1);
        spinQty.setPrefWidth(80);

        Button btnAddService = styledButton("Add", "#27AE60");

        // Services table
        TableView<ServiceItem> servicesTable = new TableView<>();
        servicesTable.setMinHeight(150);
        servicesTable.setPrefHeight(200);
        VBox.setVgrow(servicesTable, Priority.ALWAYS);
        servicesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ServiceItem, String> colService = new TableColumn<>("Service");
        colService
                .setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getService()));

        TableColumn<ServiceItem, Integer> colQty = new TableColumn<>("Qty");
        colQty.setCellValueFactory(cd -> new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getQuantity()));
        colQty.setPrefWidth(50);

        TableColumn<ServiceItem, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(cd -> new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getPrice()));
        colPrice.setPrefWidth(70);

        TableColumn<ServiceItem, Double> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(cd -> new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getTotal()));
        colTotal.setPrefWidth(70);

        servicesTable.getColumns().addAll(colService, colQty, colPrice, colTotal);

        ObservableList<ServiceItem> servicesList = FXCollections.observableArrayList();
        servicesTable.setItems(servicesList);

        // Service prices
        java.util.Map<String, Double> servicePrices = new java.util.HashMap<>();
        servicePrices.put("Not Applicable", 0.0);
        servicePrices.put("Spa", 1200.0);
        servicePrices.put("Lunch/Dinner", 800.0);
        servicePrices.put("Gym", 500.0);
        servicePrices.put("Laundry", 200.0);
        servicePrices.put("Airport Transfer", 800.0);

        btnAddService.setOnAction(e -> {
            String service = cbService.getValue();
            int qty = spinQty.getValue();
            if (service == null) {
                return;
            }
            double price = servicePrices.get(service);
            servicesList.add(new ServiceItem(service, qty, price));
            cbService.setValue(null);
            spinQty.getValueFactory().setValue(1);
        });

        HBox serviceRow = new HBox(8, cbService, spinQty, btnAddService);
        serviceRow.setAlignment(Pos.CENTER_LEFT);

        leftPanel.getChildren().addAll(
                leftTitle, form, new Separator(),
                servicesLabel, serviceRow, servicesTable);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        // ============ RIGHT PANEL (50% width) - BILL DISPLAY ============
        VBox rightPanel = new VBox(15);
        rightPanel.setPrefWidth(400);
        rightPanel
                .setStyle("-fx-border-color: #800020; -fx-border-width: 2; -fx-padding: 20; -fx-background-radius: 8;");

        Label billTitle = new Label("BILL SUMMARY");
        billTitle.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #800020;");

        TextArea billDisplay = new TextArea(); // Runnable calculates nights and charges. Builds formatted string with
                                               // padding. Updates TextArea. ListChangeListener triggers update whenever
                                               // services are added/removed.
        billDisplay.setEditable(false);
        billDisplay.setStyle(
                "-fx-control-inner-background: #1e1e1e; -fx-text-fill: #00FF00; -fx-font-family: 'Courier New'; -fx-font-size: 10; -fx-text-alignment: center;");
        billDisplay.setPrefRowCount(25);
        billDisplay.setWrapText(false);
        billDisplay.setPromptText("Bill will appear here...");
        VBox.setVgrow(billDisplay, Priority.ALWAYS);

        Button btnGenerateBill = styledButton("✓  GENERATE BILL", "#27AE60");
        btnGenerateBill.setPrefWidth(Double.MAX_VALUE);
        btnGenerateBill.setStyle("-fx-padding: 12; -fx-font-size: 14; -fx-font-weight: bold;");

        rightPanel.getChildren().addAll(billTitle, billDisplay, btnGenerateBill);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        // Uniform left padding so the entire bill block appears centered in the
        // TextArea
        final String PAD = "        "; // 8 spaces — shifts block to visual center

        // Update bill display when services change
        Runnable updateBill = () -> {
            if (selectedBooking[0] == null) {
                billDisplay.setText("Select a booking first...");
                return;
            }

            Booking b = selectedBooking[0];
            long nights = b.getCheckOutDate().toEpochDay() - b.getCheckInDate().toEpochDay();
            double roomCharge = nights * b.getPricePerDay();
            double serviceCharge = servicesList.stream().mapToDouble(ServiceItem::getTotal).sum();
            double grandTotal = roomCharge + serviceCharge;

            String sep = PAD + "════════════════════════════════════";
            String sep2 = PAD + "────────────────────────────────────";

            StringBuilder bill = new StringBuilder();
            bill.append(sep).append("\n");
            bill.append(PAD).append("     GRAND VISTA HOTEL\n");
            bill.append(PAD).append("          INVOICE\n");
            bill.append(sep).append("\n\n");
            bill.append(PAD).append(String.format("Booking ID    : %d%n", b.getBookingId()));
            bill.append(PAD).append(String.format("Customer      : %s%n", b.getCustomerName()));
            bill.append(PAD).append(String.format("Contact       : %s%n", b.getContactNumber()));
            bill.append("\n").append(sep2).append("\n");
            bill.append(PAD).append(String.format("Room #%d (%s)%n", b.getRoomNumber(), b.getRoomType()));
            bill.append(PAD).append(String.format("Check-In      : %s%n", b.getCheckInDate()));
            bill.append(PAD).append(String.format("Check-Out     : %s%n", b.getCheckOutDate()));
            bill.append(PAD).append(String.format("Nights        : %d%n", nights));
            bill.append(PAD).append(String.format("Rate/Night    : ₹ %.2f%n", b.getPricePerDay()));
            bill.append("\n").append(sep2).append("\n");
            bill.append(PAD).append(String.format("ROOM CHARGES  : ₹ %.2f%n", roomCharge));

            if (!servicesList.isEmpty()) {
                bill.append("\n").append(PAD).append("SERVICES:\n");
                for (ServiceItem si : servicesList) {
                    bill.append(PAD).append(String.format("  %s (x%d)  : ₹ %.2f%n",
                            si.getService(), si.getQuantity(), si.getTotal()));
                }
                bill.append("\n").append(PAD).append(String.format("SERVICE CHARGE: ₹ %.2f%n", serviceCharge));
            }

            bill.append("\n").append(sep).append("\n");
            bill.append(PAD).append(String.format("GRAND TOTAL   : ₹ %.2f%n", grandTotal));
            bill.append(sep).append("\n");
            bill.append(PAD).append("   Thank you for your stay!\n");
            bill.append(sep);

            billDisplay.setText(bill.toString());
        };

        // Update when services change
        servicesList.addListener((javafx.collections.ListChangeListener<ServiceItem>) c -> updateBill.run());
        updateBillRef[0] = updateBill;

        btnGenerateBill.setOnAction(e -> {
            if (selectedBooking[0] == null) {
                billDisplay.setText("Please select a booking first!");
                return;
            }

            Booking b = selectedBooking[0];
            long nights = b.getCheckOutDate().toEpochDay() - b.getCheckInDate().toEpochDay();
            double roomCharge = nights * b.getPricePerDay();
            double serviceCharge = servicesList.stream().mapToDouble(ServiceItem::getTotal).sum();
            double grandTotal = roomCharge + serviceCharge;

            // Save grand total (room + all services) to the booking
            manager.getAllBookings().forEach(bk -> {
                if (bk.getBookingId() == b.getBookingId()) {
                    bk.setTotalBill(grandTotal);
                }
            });
            selectedBooking[0].setTotalBill(grandTotal);
            DataStore.saveBookings(manager.getAllBookings());

            // Refresh UI
            refreshBookingData();

            billDisplay.setText(billDisplay.getText() + "\n\n✔ BILL SAVED TO BOOKING!");
            btnGenerateBill.setDisable(true);
        });

        mainContent.getChildren().addAll(leftPanel, rightPanel);

        root.getChildren().addAll(heading, mainContent);
        return root;
    }

    private VBox buildHistoryTab() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label heading = sectionHeading("📊  Checkout History & Revenue");

        Label lblSummary = new Label();
        lblSummary.getStyleClass().add("price-preview");

        Runnable calcSummary = () -> {
            double total = manager.getAllBookings().stream()
                    .filter(b -> b.getStatus().equals("Checked Out"))
                    .mapToDouble(Booking::getTotalBill).sum();
            long checkedOut = manager.getAllBookings().stream()
                    .filter(b -> b.getStatus().equals("Checked Out")).count();
            lblSummary.setText(String.format(
                    "Total Revenue: ₹ %.2f  |  Completed Stays: %d", total, checkedOut));
        };
        calcSummary.run();

        Button btnRefresh = styledButton("🔄 Refresh", "#800020");
        btnRefresh.setOnAction(e -> {
            refreshBookingData();
            calcSummary.run();
        });

        TableView<Booking> table = buildFullBookingTable();
        table.setItems(bookingData);
        VBox.setVgrow(table, Priority.ALWAYS);

        TextField tfViewId = new TextField();
        tfViewId.setPromptText("Booking ID");
        Button btnView = styledButton("🧾  View Receipt", "#800020");

        TextArea taReceipt = new TextArea();
        taReceipt.setEditable(false);
        taReceipt.setPrefRowCount(15);
        taReceipt.setWrapText(true);
        taReceipt.getStyleClass().add("details-area");

        btnView.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tfViewId.getText().trim());
                Optional<Booking> bOpt = manager.getAllBookings().stream()
                        .filter(b -> b.getBookingId() == id).findFirst();
                if (bOpt.isEmpty())
                    throw new Exception("Booking ID " + id + " not found.");
                taReceipt.setText(buildBillReceipt(bOpt.get()));
            } catch (NumberFormatException ex) {
                taReceipt.setText("Please enter a valid Booking ID.");
            } catch (Exception ex) {
                taReceipt.setText("Error: " + ex.getMessage());
            }
        });

        HBox viewRow = new HBox(10, new Label("View Receipt for ID:"), tfViewId, btnView);
        viewRow.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().addAll(heading, lblSummary, btnRefresh, table,
                new Separator(), viewRow, taReceipt);
        return root;
    }

    private void refreshRoomData() {
        roomData.setAll(manager.getAllRooms());
    } // Simple one-liners. setAll() clears and repopulates observable lists. Bound
      // tables update automatically via JavaFX binding.

    private void refreshBookingData() {
        bookingData.setAll(manager.getAllBookings());
    }

    private TableView<Room> buildRoomTable() {
        TableView<Room> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.getColumns().addAll(
                col("Room #", "roomNumber"),
                col("Type", "roomType"),
                col("Price/Day ₹", "pricePerDay"),
                col("Status", "available"));
        return t;
    }

    private TableView<Booking> buildBookingTable() {
        TableView<Booking> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.getColumns().addAll(
                col("Booking ID", "bookingId"),
                col("Customer", "customerName"),
                col("Contact", "contactNumber"),
                col("Room No", "roomNumber"),
                col("Check-In", "checkInDate"),
                col("Check-Out", "checkOutDate"),
                col("Bill ₹", "totalBill"),
                col("Status", "status"));
        return t;
    }

    private TableView<Booking> buildFullBookingTable() {
        TableView<Booking> t = buildBookingTable();
        t.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Booking item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setStyle("");
                else
                    setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            }
        });
        return t;
    }

    @SuppressWarnings("unchecked")
    private <T> TableColumn<T, ?> col(String title, String property) {
        TableColumn<T, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }

    private Label sectionHeading(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("section-heading");
        return l;
    }

    private Label feedbackLabel() { // Creates empty label with wrapping. setMsg() sets text and colors it green
                                    // (success) or red (error).
        Label l = new Label("");
        l.setWrapText(true);
        l.getStyleClass().add("feedback-label");
        return l;
    }

    private void setMsg(Label label, String msg, boolean success) {
        label.setText(msg);
        label.setTextFill(success ? Color.web("#27AE60") : Color.web("#E74C3C"));
    }

    private Button styledButton(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + color + "; -fx-text-fill:white;" +
                "-fx-font-size:13; -fx-padding:8 16; -fx-background-radius:6; -fx-cursor:hand;"); // Reusable button
                                                                                                  // factory. Takes text
                                                                                                  // and hex color. Sets
                                                                                                  // styling inline. On
                                                                                                  // hover, reduces
                                                                                                  // opacity to 0.85.
                                                                                                  // Returns configured
                                                                                                  // button.
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited(e -> b.setOpacity(1.0));
        return b;
    }

    private VBox buildAnalyticsTab() { // Returns a VBox card with title and large value. Used for analytics dashboard
                                       // stat cards (same concept as dashboard stat cards).
        VBox root = new VBox(25);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label heading = new Label("HOTEL ANALYTICS");
        heading.setStyle(
                "-fx-font-family: 'Georgia'; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #800020;");

        // Stat cards row
        HBox statsBox = new HBox(15);
        statsBox.setAlignment(Pos.CENTER);

        long totalRooms = manager.getAllRooms().size();
        long available = manager.getAvailableRooms().size();
        long occupied = totalRooms - available;
        double totalRevenue = manager.getAllBookings().stream()
                .filter(b -> b.getStatus().equals("Checked Out"))
                .mapToDouble(Booking::getTotalBill).sum();
        long activeBookings = manager.getAllBookings().stream()
                .filter(b -> b.getStatus().equals("Active")).count();

        statsBox.getChildren().addAll(
                analyticsStatCard("TOTAL ROOMS", String.valueOf(totalRooms)),
                analyticsStatCard("OCCUPIED", String.valueOf(occupied)),
                analyticsStatCard("ACTIVE BOOKINGS", String.valueOf(activeBookings)),
                analyticsStatCard("REVENUE", String.format("₹%.0f", totalRevenue)));

        // Revenue by Room Type BarChart: Creates bar chart with CategoryAxis (x) and
        // NumberAxis (y). Streams bookings, groups by room type, sums revenue. Each
        // data point is styled burgundy when rendered
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Room Type");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Revenue (₹)");

        BarChart<String, Number> revenueChart = new BarChart<>(xAxis, yAxis);
        revenueChart.setLegendVisible(false);
        revenueChart.setPrefHeight(320);
        revenueChart.setTitle("Revenue by Room Type");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        java.util.Map<String, Double> revenueByType = manager.getAllBookings().stream()
                .filter(b -> b.getStatus().equals("Checked Out"))
                .collect(Collectors.groupingBy(
                        Booking::getRoomType,
                        Collectors.summingDouble(Booking::getTotalBill)));

        for (String type : java.util.Arrays.asList("Single", "Double", "Deluxe")) {
            double rev = revenueByType.getOrDefault(type, 0.0);
            XYChart.Data<String, Number> data = new XYChart.Data<>(type, rev);
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: #800020;");
                }
            });
            series.getData().add(data);
        }
        revenueChart.getData().add(series);

        // Bookings by Room Type PieChart: Groups bookings by room type and counts.
        // Creates pie slices with custom colors. Shows "No Bookings Yet" if empty.
        // Colors applied dynamically when nodes render.
        java.util.Map<String, Long> bookingsByType = manager.getAllBookings().stream()
                .collect(Collectors.groupingBy(Booking::getRoomType, Collectors.counting()));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (String type : java.util.Arrays.asList("Single", "Double", "Deluxe")) {
            long count = bookingsByType.getOrDefault(type, 0L);
            if (count > 0) {
                pieData.add(new PieChart.Data(type + " (" + count + ")", count));
            }
        }
        if (pieData.isEmpty()) {
            pieData.add(new PieChart.Data("No Bookings Yet", 1));
        }

        PieChart bookingsPie = new PieChart(pieData);
        bookingsPie.setTitle("Bookings by Room Type");
        bookingsPie.setPrefHeight(320);
        bookingsPie.setLabelsVisible(true);

        // Color the pie slices
        String[] pieColors = { "#800020", "#C5A059", "#2A7A4B" };
        for (int i = 0; i < bookingsPie.getData().size(); i++) {
            final String clr = pieColors[i % pieColors.length];
            PieChart.Data slice = bookingsPie.getData().get(i);
            slice.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-pie-color: " + clr + ";");
                }
            });
        }

        VBox revWrapper = new VBox(revenueChart);
        revWrapper.setStyle(
                "-fx-background-color: derive(-fx-base, 20%); -fx-border-color: #800020; -fx-border-width: 1; -fx-background-radius: 6; -fx-border-radius: 6; -fx-padding: 10;");
        revWrapper.setAlignment(Pos.CENTER);
        VBox.setVgrow(revenueChart, Priority.ALWAYS);
        HBox.setHgrow(revWrapper, Priority.ALWAYS);
        revWrapper.setMaxWidth(Double.MAX_VALUE);

        VBox pieWrapper = new VBox(bookingsPie);
        pieWrapper.setStyle(
                "-fx-background-color: derive(-fx-base, 20%); -fx-border-color: #800020; -fx-border-width: 1; -fx-background-radius: 6; -fx-border-radius: 6; -fx-padding: 10;");
        pieWrapper.setAlignment(Pos.CENTER);
        VBox.setVgrow(bookingsPie, Priority.ALWAYS);
        HBox.setHgrow(pieWrapper, Priority.ALWAYS);
        pieWrapper.setMaxWidth(Double.MAX_VALUE);

        HBox chartsRow = new HBox(20, revWrapper, pieWrapper);

        // ── Occupancy Summary ──
        Label occTitle = new Label("OCCUPANCY SUMMARY");
        occTitle.setStyle(
                "-fx-font-family: 'Georgia'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #800020;");

        double occPercent = totalRooms > 0 ? (double) occupied / totalRooms * 100 : 0;

        // Progress bar visual: Calculates occupancy percentage. ProgressBar value is
        // 0.0-1.0 (occupied/total). Fills to show occupancy visually. Label shows
        // percentage text.
        ProgressBar occBar = new ProgressBar(totalRooms > 0 ? (double) occupied / totalRooms : 0);
        occBar.setPrefWidth(Double.MAX_VALUE);
        occBar.setPrefHeight(22);
        occBar.setStyle("-fx-accent: #800020;");

        Label occLabel = new Label(
                String.format("%d of %d rooms occupied  (%.1f%%)", occupied, totalRooms, occPercent));
        occLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #C5A059;");

        // Per-type breakdown: Groups rooms by type. For each type, counts occupied
        // rooms. Creates card showing "X / Total" and status (All Free/Full/Partial).
        // Status color changes based on occupancy.
        HBox typeBreakdown = new HBox(20);
        typeBreakdown.setAlignment(Pos.CENTER);
        java.util.Map<String, java.util.List<Room>> grouped = manager.getAllRooms().stream()
                .collect(Collectors.groupingBy(Room::getRoomType));

        for (String type : java.util.Arrays.asList("Single", "Double", "Deluxe")) {
            java.util.List<Room> rooms = grouped.get(type);
            if (rooms == null)
                continue;
            long occ = rooms.stream().filter(r -> !r.isAvailable()).count();
            long tot = rooms.size();
            VBox typeCard = new VBox(4);
            typeCard.setAlignment(Pos.CENTER);
            typeCard.setPadding(new Insets(12, 20, 12, 20));
            typeCard.setStyle("-fx-background-color: derive(-fx-base, 20%); -fx-border-color: #800020;" +
                    "-fx-border-width: 1; -fx-background-radius: 6; -fx-border-radius: 6;");

            Label typeName = new Label(type.toUpperCase());
            typeName.setStyle(
                    "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: derive(-fx-text-background-color, -30%);");
            Label typeOcc = new Label(occ + " / " + tot);
            typeOcc.setStyle(
                    "-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: 'Georgia'; -fx-text-fill: #C5A059;");
            Label typeStatus = new Label(occ == 0 ? "All Free" : (occ == tot ? "Full" : "Partial"));
            String sColor = occ == 0 ? "#4CAF50" : (occ == tot ? "#E74C3C" : "#C5A059");
            typeStatus.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: " + sColor + ";");

            typeCard.getChildren().addAll(typeName, typeOcc, typeStatus);
            HBox.setHgrow(typeCard, Priority.ALWAYS);
            typeCard.setMaxWidth(Double.MAX_VALUE);
            typeBreakdown.getChildren().add(typeCard);
        }

        VBox occSection = new VBox(10, occTitle, occBar, occLabel, typeBreakdown);
        occSection.setPadding(new Insets(10, 0, 0, 0));

        Button btnRefreshAnalytics = styledButton("🔄 Refresh Analytics", "#800020");
        btnRefreshAnalytics.setOnAction(e -> {
            mainLayout.setCenter(buildAnalyticsTab());
        });

        root.getChildren().addAll(heading, statsBox, new Separator(), chartsRow, new Separator(), occSection,
                btnRefreshAnalytics);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox wrapper = new VBox(scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return wrapper;
    }

    private VBox analyticsStatCard(String title, String value) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: derive(-fx-base, 20%); -fx-border-color: #800020; -fx-border-width: 1;" +
                "-fx-padding: 20; -fx-min-width: 180; -fx-background-radius: 6; -fx-border-radius: 6;");
        card.setAlignment(Pos.CENTER);
        Label lblTitle = new Label(title);
        lblTitle.setStyle(
                "-fx-text-fill: derive(-fx-text-background-color, -30%); -fx-font-size: 11px; -fx-font-weight: bold;");
        Label lblValue = new Label(value);
        lblValue.setStyle(
                "-fx-text-fill: #C5A059; -fx-font-family: 'Georgia'; -fx-font-size: 28px; -fx-font-weight: bold;");
        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    private String buildBillReceipt(Booking b) { // Uses triple-quoted string (Java 13+) for multiline formatting.
                                                 // Calculates nights. Returns formatted receipt string with all booking
                                                 // details.
        long nights = b.getCheckOutDate().toEpochDay() - b.getCheckInDate().toEpochDay();
        return String.format("""
                ══════════════════════════════════════
                       GRAND VISTA HOTEL
                       BILLING RECEIPT
                ══════════════════════════════════════
                Booking ID    : %d
                Customer      : %s
                Contact       : %s
                ──────────────────────────────────────
                Room Number   : %d
                Room Type     : %s
                Check-In      : %s
                Check-Out     : %s
                Nights        : %d
                Rate / Night  : ₹ %.2f
                ──────────────────────────────────────
                TOTAL AMOUNT  : ₹ %.2f
                Status        : %s
                ══════════════════════════════════════
                  Thank you for staying with us!
                ══════════════════════════════════════
                """,
                b.getBookingId(), b.getCustomerName(), b.getContactNumber(),
                b.getRoomNumber(), b.getRoomType(),
                b.getCheckInDate(), b.getCheckOutDate(), nights,
                b.getPricePerDay(), b.getTotalBill(), b.getStatus());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// Inner class for services: Simple data class for services. Constructor
// calculates total (qty × price). All fields final (immutable). Getters expose
// data to TableView.
class ServiceItem {
    private final String service;
    private final int quantity;
    private final double price;
    private final double total;

    public ServiceItem(String service, int quantity, double price) {
        this.service = service;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;
    }

    public String getService() {
        return service;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }
}
