# 🏨 Grand Vista Hotel Management System

A JavaFX 21 desktop application for end-to-end hotel management. Manage rooms, handle customer bookings, process checkouts, and track billing history — all from a polished dark-blue UI with persistent CSV-based data storage across sessions.

---

## 🎯 Features

### Room Management
- **Add Rooms** — Register rooms with a room number, type (Single / Double / Suite), and price per day
- **View All Rooms** — Full room table with Room Number, Type, Price/Day, and Status
- **Filter Rooms** — Toggle between All Rooms and Available Only with a single click
- **Availability Tracking** — Room status updates automatically on booking and checkout

### Booking System
- **Customer Booking Form** — Enter customer name, contact number, select room, and pick dates
- **Live Bill Estimation** — Estimated bill updates in real-time as you select room and dates
- **Room Selector** — Dropdown auto-populates with only available rooms at time of booking
- **Active Bookings Table** — Live table of all current active bookings visible below the form
- **Input Validation** — Contact number format, date logic, and empty-field checks with inline feedback

### Checkout
- **Booking Lookup** — Enter a Booking ID to verify the booking exists before proceeding
- **Checkout & Generate Bill** — Processes the checkout, releases the room, and generates a full itemised receipt
- **Receipt Display** — Formatted billing receipt shown in-app with all stay details
- **Active Bookings Reference** — Reference table of active bookings visible while on the checkout screen

### Billing & History **[CORE FEATURE]**
- **Complete Booking History** — Full table of every booking ever made, including checked-out stays
- **Revenue Summary** — Total Revenue and Completed Stays count displayed at the top
- **Individual Bill Viewer** — Enter any Booking ID to pull up and display its full receipt
- **Itemised Receipts** — Receipts show Booking ID, customer info, room details, nights stayed, rate, and total

### Data Persistence
- **Automatic Save** — All rooms and bookings are written to `rooms.csv` and `bookings.csv` on every change
- **Auto-Load on Startup** — Data is reloaded from CSV files every time the app launches
- **Default Seed Data** — 6 default rooms are created on first launch if no data files exist
- **No Database Required** — Pure CSV storage, zero setup needed

### UI & Theming
- **Dark Blue Theme** — Custom dark-blue CSS stylesheet applied across all tabs
- **Light Mode Toggle** — Switch between dark and light themes from the Dashboard
- **Tab-Based Navigation** — Dashboard, Rooms, Booking, Checkout, Billing — all in one window
- **Scrollable Layouts** — Forms scroll independently so tables are always visible on any window size
- **Color-Coded Feedback** — Green for success, red for errors, inline on every action

---

## 🛠️ Tech Stack

| Component | Technology | Details |
|-----------|-----------|---------|
| **Language** | Java 21 | Core application logic |
| **UI Framework** | JavaFX 21 | Desktop GUI — controls, layouts, tables |
| **Styling** | CSS3 (JavaFX CSS) | External stylesheet, dark/light theming |
| **Build Tool** | Maven 3.6+ | Dependency management, compile, run |
| **Persistence** | CSV (flat files) | rooms.csv + bookings.csv, no SQL needed |
| **IDE Support** | VS Code / IntelliJ | `.vscode/` config included |

---

## 📋 Prerequisites

- **JDK 21+** — [Download here](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** — [Download here](https://maven.apache.org/download.cgi)
- **JAVA_HOME** set and both `java` and `mvn` available on your `PATH`

> Verify your setup:
> ```bash
> java -version    # should show 21+
> mvn -version     # should show 3.6+
> ```

---

## 🚀 Quick Start (2 Steps)

### Step 1: Navigate to the project folder

```bash
cd HotelMS_FIXED
```

### Step 2: Run the app

```bash
mvn javafx:run
```

The application window opens immediately.

---

## 📖 Detailed Setup

### Windows

```bash
# 1. Navigate to project folder
cd D:\College\Year2\Sem4\OSDL\Project\HotelMS

# 2. Compile the project
mvn package

# 3. Run the app
mvn javafx:run
```

### macOS / Linux

```bash
# 1. Navigate to project folder
cd /path/to/HotelMS

# 2. Compile the project
mvn package

# 3. Run the app
mvn javafx:run
```

> **Note:** After any change to `.java` source files, always run `mvn package` before `mvn javafx:run` to recompile. Changes to `style.css` alone do not require a recompile.

---

## 📁 Project Structure

```
HotelMS_FIXED/
├── pom.xml                                      # Maven build config (Java 21, JavaFX 21)
├── README.md                                    # This file
├── .gitignore                                   # Ignores target/, *.csv, .vscode/
│
├── src/
│   └── main/
│       ├── java/com/hotel/
│       │   ├── MainApp.java                     # JavaFX entry point + all UI tabs & layouts
│       │   ├── HotelManager.java                # Business logic (book, checkout, queries)
│       │   ├── DataStore.java                   # CSV read/write persistence layer
│       │   ├── Room.java                        # Room model (number, type, price, status)
│       │   └── Booking.java                     # Booking model (ID, customer, dates, bill)
│       │
│       └── resources/com/hotel/
│           └── style.css                        # External CSS — dark blue theme + light mode
│
├── target/                                      # Compiled output (auto-generated by Maven)
│   └── classes/com/hotel/
│       ├── *.class                              # Compiled Java bytecode
│       └── style.css                            # CSS copied here at build time
│
└── .vscode/                                     # VS Code launch & task config
    ├── launch.json
    ├── settings.json
    └── tasks.json
```

---

## 📖 How to Use

### Adding a Room
1. Go to the **Rooms** tab
2. Enter a Room Number (e.g. `101`), select a Room Type, and enter Price/Day
3. Click **Add Room** — it appears in the table instantly and is saved to `rooms.csv`

### Making a Booking
1. Go to the **Booking** tab
2. Fill in Customer Name and Contact Number (10 digits)
3. Select an available room from the dropdown
4. Pick Check-In and Check-Out dates — the estimated bill updates live
5. Click **Confirm Booking** — booking is saved and the active bookings table refreshes

### Checking Out a Guest
1. Go to the **Checkout** tab
2. Enter the Booking ID and click **Lookup** — confirms the booking exists
3. Click **Checkout & Generate Bill** — room is released, receipt is generated and displayed

### Viewing Billing History
1. Go to the **Billing** tab
2. See the full history table and total revenue summary
3. Enter any Booking ID in the **View Bill** field and click **View Bill** to see the itemised receipt

---

## 🗃️ Data Files

The app saves data files in whichever directory you run `mvn javafx:run` from:

| File | Contents | Created |
|------|----------|---------|
| `rooms.csv` | All rooms — number, type, price, status | On first Add Room (or first launch with seed data) |
| `bookings.csv` | All bookings — ID, customer, dates, bill, status | On first Confirm Booking |

> **Do not delete these files** unless you want to reset all data. The app loads them automatically on every startup.

---

## 🧾 Sample Bill Receipt

```
══════════════════════════════════════
       GRAND VISTA HOTEL
       BILLING RECEIPT
══════════════════════════════════════
Booking ID    : 1
Customer      : Halcyon Vector
Contact       : 7291456824
──────────────────────────────────────
Room Number   : 101
Room Type     : Single
Check-In      : 2026-04-07
Check-Out     : 2026-04-23
Nights        : 16
Rate / Night  : ₹ 2000.00
──────────────────────────────────────
TOTAL AMOUNT  : ₹ 32000.00
Status        : Checked Out
══════════════════════════════════════
  Thank you for staying with us!
══════════════════════════════════════
```

---

## 🚨 Troubleshooting

### Issue: `mvn` is not recognized
**Solution:** Install Maven and add it to your `PATH`. Verify with `mvn -version`.

### Issue: `java` is not recognized or wrong version
**Solution:** Install JDK 21+ and set `JAVA_HOME` to the JDK folder.

### Issue: App launches but shows no data
**Solution:** Check that `rooms.csv` and `bookings.csv` exist in the folder you ran `mvn javafx:run` from. If missing, the app seeds 6 default rooms on first launch automatically.

### Issue: UI changes not reflected after editing CSS
**Solution:** Run `mvn package` to copy the updated `style.css` into `target/classes/`, then relaunch.

### Issue: Changes to Java code not reflected
**Solution:** Always run `mvn package` before `mvn javafx:run` after any `.java` file change.

### Issue: `[ERROR] Source option 5 is no longer supported`
**Solution:** Make sure your `JAVA_HOME` points to JDK 21, not an older version.

---

## 📈 Future Enhancements

- [ ] Search and filter bookings by customer name or date range
- [ ] Edit / cancel an existing booking
- [ ] SQLite database backend for larger datasets
- [ ] Export billing history to PDF or Excel
- [ ] Room photos and descriptions
- [ ] Multi-user support with staff login
- [ ] Dashboard graphs — occupancy rate over time, revenue trends
- [ ] Email receipt generation

---

## 📄 License

No specific license. Contact the author for usage rights.

---

## 👨‍💻 Author

**Vector**  
GitHub: [@HalcyonVector](https://github.com/HalcyonVector)

---

## 🤝 Contributing

Want to improve Grand Vista HMS?

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m "Add amazing feature"`
4. Push the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 🙋 Support

Found a bug or have questions?  
[Open an issue](https://github.com/HalcyonVector/Hotel-management/issues) on GitHub.

---

**Made with 🏨 for hotel management, built with Java**
