# 📌 Database Management System (DBMS)

## 📖 Introduction
This project is an advanced **Relational Database Management System (RDBMS)** implemented in **Java**. It efficiently handles **data storage, indexing, and query execution** using structured approaches such as **B+ Trees, serialized pages, and metadata tracking**.

🚀 **Key Features:**
- Supports **table creation, insertion, updating, and deletion** with structured metadata.
- Utilizes **B+ Trees for indexing**, enabling **fast search and retrieval** 🔍.
- Implements **serialization for data persistence** to enhance storage efficiency.
- Designed to be modular, **scalable**, and **efficient** for handling large datasets.
- Built to work with **Java 11** and **Maven**, ensuring compatibility and maintainability.

This project implements a **mini relational database management system** using Java. It supports **table creation, indexing (B+ Tree), and query operations** while efficiently handling **data storage through serialization**.

🚀 **Key Features:**
- Supports **table creation, insertion, updating, and deletion**
- Uses **B+ Trees for indexing** for fast lookups 🔍
- Efficiently manages **metadata and serialization**
- Designed to work with **Java 11** and **Maven**

---

## ⚙️ System Architecture & Workflow
This system is designed using an **object-oriented approach** with a well-structured data model. Below is an overview of the core components and how they interact.

### **🔹 Code Examples from Each File**

#### **DBApp.java** (Main Application & Table Management)
```java
DBApp dbApp = new DBApp();
Hashtable<String, String> htbl = new Hashtable<>();
htbl.put("ID", "java.lang.Integer");
htbl.put("Name", "java.lang.String");
htbl.put("Age", "java.lang.Integer");
dbApp.createTable("Student", "ID", htbl);
```

#### **Table.java** (Handles Table Creation & Insertions)
```java
Table table = new Table("Student", "ID", htbl);
Tuple t1 = new Tuple();
t1.tuple.add(1);
t1.tuple.add("John Doe");
t1.tuple.add(20);
table.insertNew(t1, new ArrayList<>(htbl.keySet()));
```

#### **Page.java** (Handles Data Storage in Pages)
```java
Page page = new Page(1, "Student");
page.insertPage(t1);
page.serialize(); // Saves the page to a .ser file
```

#### **Tuple.java** (Defines Data Rows)
```java
Tuple t = new Tuple();
t.tuple.add(5);
t.tuple.add("Alice");
t.tuple.add(22);
System.out.println(t.toString()); // Prints: 5,Alice,22
```

#### **BTree.java** (Indexing System for Fast Lookups)
```java
BTree<Integer, String> index = new BTree<>();
index.insert(1, "P:1,R:0"); // Page 1, Row 0
index.serialize("ID.ser");
```

#### **ConfigReader.java** (Reads Configurations)
```java
int maxRows = ConfigReader.configReaderInt("MaximumRowsCountinPage");
System.out.println("Max Rows: " + maxRows);
```

#### **TextFileWriter.java** (Handles Metadata Storage)
```java
TextFileWriter.metaWrite("Student,ID,java.lang.Integer,true,null,null");
```


### 📌 **Data Representation: Tables & Metadata**
- The **database consists of tables**, each stored as a collection of **pages** for efficient memory management.
- **Metadata** is stored in `metadata.csv` to define **schema details** such as column types, primary keys, and indices.
- Example of metadata representation:
  ```csv
  Student,id,java.lang.Integer,true,null,null
  Student,name,java.lang.String,false,null,null
  ```
- Metadata updates occur dynamically using `TextFileWriter.metaWrite()` to ensure **schema consistency**. 📂
- Tables are managed via the `Table.java` class.
- Metadata (column types, primary keys, etc.) is stored in `metadata.csv`.
- Example of metadata storage:
  ```csv
  Student,id,java.lang.Integer,true,null,null
  Student,name,java.lang.String,false,null,null
  ```
- `TextFileWriter.metaWrite()` appends table schema entries to `metadata.csv`.

### **2️⃣ Data Storage: Pages and Tuples** 📄
- Each table consists of **pages** (handled in `Page.java`).
- A page stores multiple **tuples (rows)** and is serialized to a file (`.ser`).
- Example of tuple representation:
  ```java
  Tuple t = new Tuple();
  t.tuple.add(123);  // ID
  t.tuple.add("John Doe");  // Name
  t.tuple.add(3.5);  // GPA
  ```
- Pages ensure **sorted order** using **binary search** for efficient lookups.

### 📌 **Efficient Indexing with B+ Trees** 🌳
- The system **optimizes searches** using **B+ Trees**, which store indexes in serialized `.ser` files.
- B+ Trees are used for **faster data retrieval** in large datasets.
- Each column that requires indexing generates a separate **B+ Tree structure**, enabling fast lookups.
- Example of inserting into an indexed column:
  ```java
  BTree<Integer, String> index = new BTree<>();
  index.insert(123, "P:1,R:3"); // Page 1, Row 3
  index.serialize("id.ser");
  ```
- Indexing **reduces query execution time** significantly compared to linear searches. 🌳
- Indexing is managed using a **B+ Tree** implementation (`BTree.java`).
- Each column index is stored as a **serialized B+ Tree** (`columnName.ser`).
- Inserting a record automatically updates the corresponding B+ Tree index.
- Example of inserting into an indexed column:
  ```java
  BTree<Integer, String> index = new BTree<>();
  index.insert(123, "P:1,R:3"); // Page 1, Row 3
  index.serialize("id.ser");
  ```

### 📌 **Query Execution Engine** 🔎
- The system supports **SQL-like queries**, allowing operations using **AND/OR conditions**.
- Queries are processed by filtering through indexed and non-indexed tables efficiently.
- Example of querying student records:
  ```sql
  SELECT * FROM Student WHERE name = 'John Doe' OR gpa = 3.5;
  ```
  ```java
  SQLTerm[] conditions = new SQLTerm[2];
  conditions[0] = new SQLTerm("Student", "name", "=", "John Doe");
  conditions[1] = new SQLTerm("Student", "gpa", "=", 3.5);
  String[] operators = {"OR"};
  Iterator result = dbApp.selectFromTable(conditions, operators);
  ```
- The system efficiently retrieves matching records by leveraging **B+ Trees and binary search operations**. 🔎
- The system supports **SQL-like selection using AND/OR operators**.
- `selectFromTable()` filters results based on given conditions.
- Example SQL query and its equivalent Java implementation:
  ```sql
  SELECT * FROM Student WHERE name = 'John Doe' OR gpa = 3.5;
  ```
  ```java
  SQLTerm[] conditions = new SQLTerm[2];
  conditions[0] = new SQLTerm("Student", "name", "=", "John Doe");
  conditions[1] = new SQLTerm("Student", "gpa", "=", 3.5);
  String[] operators = {"OR"};
  Iterator result = dbApp.selectFromTable(conditions, operators);
  ```

---

## 🛠️ Setup & Installation
### **Prerequisites**
✔ Java **11** (Make sure Java 22 is NOT used)  
✔ Apache Maven  
✔ IntelliJ IDEA / Eclipse

### **1️⃣ Clone the Repository**
```sh
  git clone <repo-url>
  cd database-project
```
### **2️⃣ Compile and Run with Maven**
```sh
  mvn clean install
  mvn exec:java -Dexec.mainClass="DBApp"
```
### **3️⃣ Running the Project** 🚀
- **Recommended:** Run from `Table.java` instead of `DBApp.java` due to execution issues.
- Ensure **DBApp.config** is set properly:
  ```properties
  MaximumRowsCountinPage=5
  ```

---

## 🛑 Known Issues
### ❌ **DBApp.java Execution Issue**
- The system **sometimes fails when run from `DBApp.java`** due to metadata handling issues.
- Prefer running from **`Table.java`** for stable execution.



---

## 📸 Screenshots
-🖼️ **Output from Running Main Method:**

- 📂 **Serialized Tables & Pages** (`.ser` files)
  ![Output Screenshot 1](<PLACEHOLDER_1>)
- 📊 **Metadata Updates in `metadata.csv`**
   ![Output Screenshot 2](<PLACEHOLDER_2>)
- 🌳 **B+ Tree Index Structure**
   ![Output Screenshot 3](<PLACEHOLDER_3>)

  
  


---

## 📝 License, Contributions & Future Enhancements
- **License:** This project is licensed under **MIT License**.
- **Contributions:** We welcome community contributions! 🚀 If you'd like to improve this project, fork the repo and submit a pull request.
- **Planned Enhancements:**
  - ✅ Implement **SQL Query Optimizations**.
  - ✅ Extend indexing mechanisms with **Hash Indexing**.
  - ✅ Introduce **Concurrency Control** for multi-threaded query execution.
  - ✅ Support **ACID Transactions** for complete database reliability.

💡 **Built with Java 11, Maven, and a passion for databases!** 🚀🔥
- Licensed under **MIT License**
- Contributions are welcome! Fork the repo and submit a pull request.

💡 **Built with Java 11, Maven, and passion for databases!** 🚀🔥
