# 🏨 Grand Vista Hotel Management System

A JavaFX 21 desktop application for end-to-end hotel management. Manage rooms, handle customer bookings, process checkouts, and track billing history — all from a polished dark-themed UI with persistent CSV-based data storage across sessions. Now with **login authentication**, **advanced analytics**, and **comprehensive reports**.

---

## 🎯 Features

### Authentication & Security 
- **Admin Login Screen** — Secure login interface with username/password authentication
- **Session Management** — Login required before accessing the main dashboard
- **Default Credentials** — Username: `admin`, Password: `admin`
- **Branded Login UI** — Custom-styled login screen with Grand Vista branding

### Room Management
- **Add Rooms** — Register rooms with a room number, type (Single / Double / Deluxe), and price per day
- **View All Rooms** — Full room table with Room Number, Type, Price/Day, and Status
- **Filter Rooms** — Toggle between All Rooms and Available Only with a single click
- **Availability Tracking** — Room status updates automatically on booking and checkout
- **Room Type Support** — Single, Double, and Deluxe room categories

### Booking System
- **Customer Booking Form** — Enter customer name, contact number, select room, and pick dates
- **Live Bill Estimation** — Estimated bill updates in real-time as you select room and dates
- **Room Selector** — Dropdown auto-populates with only available rooms at time of booking
- **Active Bookings Table** — Live table of all current active bookings visible on booking and checkout screens
- **Input Validation** — Contact number format, date logic, and empty-field checks with inline feedback
- **Auto-Generated Booking IDs** — Sequential booking IDs assigned automatically

### Checkout & Billing
- **Booking Lookup** — Enter a Booking ID to verify the booking exists before proceeding
- **Checkout & Generate Bill** — Processes the checkout, releases the room, and generates a full itemised receipt
- **Receipt Display** — Formatted billing receipt shown in-app with all stay details
- **Active Bookings Reference** — Reference table of active bookings visible on checkout screen
- **Itemised Receipts** — Receipts show Booking ID, customer info, room details, nights stayed, rate, and total

### Billing & History
- **Complete Booking History** — Full table of every booking ever made, including checked-out stays
- **Revenue Summary** — Total Revenue and Completed Stays count displayed at the top
- **Individual Bill Viewer** — Enter any Booking ID to pull up and display its full receipt
- **Itemised Receipts** — Receipts show Booking ID, customer info, room details, nights stayed, rate, and total
- **History Dashboard** — Dedicated Reports tab showing complete checkout history with revenue summary

### Analytics Dashboard 
- **Key Metrics Cards** — Display total rooms, occupied rooms, active bookings, and total revenue
- **Revenue by Room Type** — Bar chart showing revenue breakdown across Single, Double, and Deluxe rooms
- **Bookings by Room Type** — Pie chart visualizing booking distribution by room category
- **Occupancy Summary** — Progress bar and percentage showing overall room occupancy
- **Per-Type Occupancy Cards** — Individual cards for each room type showing:
  - Occupied vs. total rooms
  - Occupancy percentage
  - Status badge (All Free / Full / Partial)
- **Color-Coded Analytics** — Green for available, red for fully booked, gold for partial occupancy
- **Real-Time Data** — All charts and metrics update with current data
- **Refresh Analytics** — Button to manually refresh analytics data

### Dashboard 
- **Quick Stats** — At-a-glance summary of key metrics
- **Recent Activity** — Overview of latest bookings and checkouts
- **System Status** — Current hotel occupancy and active bookings
- **Sidebar Navigation** — Intuitive left-sidebar menu for all features with active button highlighting

### Data Persistence
- **Automatic Save** — All rooms and bookings are written to `rooms.csv` and `bookings.csv` on every change
- **Auto-Load on Startup** — Data is reloaded from CSV files every time the app launches
- **No Database Required** — Pure CSV storage, zero setup needed
- **Data Integrity** — Transactional saves ensure data consistency

### UI & Theming
- **Dark Premium Theme** — Custom dark burgundy/gold CSS stylesheet applied across all tabs
- **Color Scheme** — Professional burgundy (#800020) with gold accents (#C5A059)
- **Light Mode Toggle** — Switch between dark and light themes from the Dashboard
- **Tab-Based Navigation** — Dashboard, Rooms, Bookings, Checkout & Billing, Billing, Analytics, Reports — all in one window
- **Sidebar Navigation** — Fixed left sidebar with navigation buttons for all sections
- **Scrollable Layouts** — Forms scroll independently so tables are always visible on any window size
- **Color-Coded Feedback** — Green for success, red for errors, inline on every action
- **Responsive Design** — Layouts adapt to window size changes

---

## 🛠️ Tech Stack

| Component | Technology | Details |
|-----------|-----------|---------|
| **Language** | Java 21 | Core application logic |
| **UI Framework** | JavaFX 21 | Desktop GUI — controls, layouts, tables, charts |
| **Styling** | CSS3 (JavaFX CSS) | External stylesheet, dark/light theming |
| **Charts** | JavaFX Charts | BarChart, PieChart for analytics |
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
cd HotelMS
```

### Step 2: Run the app

```bash
mvn javafx:run
```

The login screen appears immediately. Use **Username: `admin`** and **Password: `admin`** to sign in.

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
HotelMS/
├── pom.xml                                      # Maven build config (Java 21, JavaFX 21)
├── README.md                                    # This file
├── .gitignore                                   # Ignores target/, *.csv, .vscode/
│
├── src/
│   └── main/
│       ├── java/com/hotel/
│       │   ├── MainApp.java                     # JavaFX entry point + all UI tabs & layouts
│       │   │                                    # Includes login screen, dashboard, all views
│       │   ├── HotelManager.java                # Business logic (book, checkout, queries)
│       │   ├── DataStore.java                   # CSV read/write persistence layer
│       │   ├── Room.java                        # Room model (number, type, price, status)
│       │   └── Booking.java                     # Booking model (ID, customer, dates, bill)
│       │
│       └── resources/com/hotel/
│           └── style.css                        # External CSS — dark theme + light mode
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

### Logging In
1. Launch the application with `mvn javafx:run`
2. The **Login Screen** appears with Grand Vista branding
3. Enter **Username:** `admin` and **Password:** `admin`
4. Click **Sign In** to access the main dashboard

### Using the Dashboard
1. After login, you're on the **Dashboard** tab — your home screen
2. View quick statistics and recent activity
3. Use the left **Sidebar** to navigate to any feature

### Adding a Room
1. Go to the **Rooms** tab via the sidebar
2. Enter a Room Number (e.g. `101`), select a Room Type (Single/Double/Deluxe), and enter Price/Day
3. Click **Add Room** — it appears in the table instantly and is saved to `rooms.csv`
4. Table shows all rooms with their current status (Available/Occupied)

### Making a Booking
1. Go to the **Bookings** tab via the sidebar
2. Fill in Customer Name and Contact Number (10 digits)
3. Select an available room from the dropdown
4. Pick Check-In and Check-Out dates — the estimated bill updates live
5. Click **Confirm Booking** — booking is saved and the active bookings table refreshes
6. Each booking gets a unique ID automatically

### Checking Out a Guest
1. Go to the **Checkout & Billing** tab via the sidebar
2. Enter the Booking ID and click **Lookup** — confirms the booking exists
3. Click **Checkout & Generate Bill** — room is released, receipt is generated and displayed
4. The receipt shows all booking and billing details

### Viewing Analytics
1. Go to the **Analytics** tab via the sidebar
2. See key metrics cards (Total Rooms, Occupied, Active Bookings, Revenue)
3. View **Revenue by Room Type** bar chart
4. See **Bookings by Room Type** pie chart
5. Check **Occupancy Summary** with overall percentage and per-type breakdown
6. Click **Refresh Analytics** to update all data

### Viewing Billing History & Reports
1. Go to the **Reports** tab via the sidebar
2. See **Total Revenue** and **Completed Stays** summary at the top
3. View the full **Checkout History** table with all past bookings
4. Enter any Booking ID in the **View Receipt** field and click **View Bill** to see the itemised receipt
5. Click **Refresh** to reload the latest data

---

## 🗃️ Data Files

The app saves data files in whichever directory you run `mvn javafx:run` from:

| File | Contents | Created |
|------|----------|---------|
| `rooms.csv` | All rooms — number, type, price, status | On first Add Room |
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

### Issue: Login screen appears but credentials don't work
**Solution:** Use **Username: `admin`** and **Password: `admin`** exactly as shown. Both are case-sensitive.

### Issue: `mvn` is not recognized
**Solution:** Install Maven and add it to your `PATH`. Verify with `mvn -version`.

### Issue: `java` is not recognized or wrong version
**Solution:** Install JDK 21+ and set `JAVA_HOME` to the JDK folder.

### Issue: App launches but shows no rooms or bookings
**Solution:** Check that `rooms.csv` and `bookings.csv` exist in the folder you ran `mvn javafx:run` from. If missing, start fresh by adding rooms manually.

### Issue: Analytics charts don't show data
**Solution:** Ensure there are bookings with "Checked Out" status. Charts only display data for completed stays. Make a test booking and checkout to generate data.

### Issue: UI changes not reflected after editing CSS
**Solution:** Run `mvn package` to copy the updated `style.css` into `target/classes/`, then relaunch.

### Issue: Changes to Java code not reflected
**Solution:** Always run `mvn package` before `mvn javafx:run` after any `.java` file change.

### Issue: `[ERROR] Source option 5 is no longer supported`
**Solution:** Make sure your `JAVA_HOME` points to JDK 21, not an older version.

### Issue: Sidebar buttons not clickable or text overlapping
**Solution:** Ensure the window is at least 950px wide. Try resizing the window or maximizing it.

---

## ✨ What's New in This Version

**Version 1.0 (with enhancements)**

✅ **Login Authentication** — Secure admin login before accessing the system  
✅ **Analytics Dashboard** — Advanced metrics, charts, and occupancy tracking  
✅ **Reports Tab** — Complete checkout history with revenue summary  
✅ **Sidebar Navigation** — Modern left sidebar with active button highlighting  
✅ **Enhanced Dashboard** — Quick stats and system overview  
✅ **Color-Coded Occupancy** — Visual indicators for room status  
✅ **Real-Time Charts** — Bar and pie charts for revenue and booking analysis  
✅ **Refresh Buttons** — Manual data refresh on all major screens  

---

## 📈 Future Enhancements

- [ ] Search and filter bookings by customer name or date range
- [ ] Edit / cancel an existing booking
- [ ] SQLite database backend for larger datasets
- [ ] Export billing history to PDF or Excel
- [ ] Room photos and descriptions
- [ ] Multi-user support with different staff roles
- [ ] Email receipt generation
- [ ] Advanced room rate management (seasonal pricing)
- [ ] Guest preferences and special requests
- [ ] Housekeeping and maintenance tracking

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
