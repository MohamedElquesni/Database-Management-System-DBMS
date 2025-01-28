# 📌 Database Management System (DBMS)

## 📖 Introduction
This project is an advanced **Relational Database Management System (RDBMS)** implemented in **Java**. It efficiently handles **data storage, indexing, and query execution** using structured approaches such as **B+ Trees, serialized pages, and metadata tracking**.

### 🚀 **Key Features:**
- Supports **table creation, insertion, updating, and deletion**
- Uses **B+ Trees for indexing** for fast lookups 🔍
- Efficiently manages **metadata and serialization**
- Designed to work with **Java 11** and **Maven**, ensuring compatibility and maintainability

---

## ⚙️ System Architecture & Workflow
This system is designed using an **object-oriented approach** with a well-structured data model. Below is an overview of the core components and how they interact.

### **🔹 Code Examples from Key Components**

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

#### **BTree.java** (Indexing System for Fast Lookups)
```java
BTree<Integer, String> index = new BTree<>();
index.insert(1, "P:1,R:0"); // Page 1, Row 0
index.serialize("ID.ser");
```

### 📌 **Data Representation: Tables & Metadata**
- **Tables** are stored as collections of **pages** for efficient memory management.
- **Metadata** is stored in `metadata.csv` to define **schema details** (column types, primary keys, and indices).
- Example of metadata representation:
  ```csv
  Student,id,java.lang.Integer,true,null,null
  Student,name,java.lang.String,false,null,null
  ```
- Metadata updates dynamically using `TextFileWriter.metaWrite()`.

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
- The system optimizes searches using **B+ Trees**, which store indexes in serialized `.ser` files.
- Each indexed column generates a separate **B+ Tree structure**, enabling fast lookups.
- Example of inserting into an indexed column:
  ```java
  BTree<Integer, String> index = new BTree<>();
  index.insert(123, "P:1,R:3"); // Page 1, Row 3
  index.serialize("id.ser");
  ```
- Indexing **reduces query execution time** significantly compared to linear searches. 🌳

### 📌 **Query Execution Engine** 🔎
- The system supports **SQL-like queries**, allowing operations using **AND/OR conditions**.
- Queries are processed by filtering through indexed and non-indexed tables efficiently.
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
✔ Java **11** (Ensure Java 22 is NOT used)  
✔ IntelliJ IDEA / Eclipse

### **1️⃣ Clone the Repository**
```sh
  git clone https://github.com/MohamedElquesni/Database-Management-System-DBMS.git
  cd database-project
```

### **2️⃣ Compile and Run the Project**
```sh
  javac -d bin src/*.java
  java -cp bin Table
```

### **3️⃣ Running the Project**
- **Run from `Table.java` instead of `DBApp.java`** due to execution issues.
- Ensure **DBApp.config** is set properly:
  ```properties
  MaximumRowsCountinPage=5
  ```

---

## 🛑 Known Issues
### ❌ **DBApp.java Execution Issue**
- Running from `DBApp.java` may fail due to metadata handling issues.
- Prefer running from **`Table.java`** for stable execution.

---

## 📸 Screenshots

### 🖼️ **Serialized Tables & Pages (`.ser` files)**
![Serialized Tables](https://github.com/MohamedElquesni/Database-Management-System-DBMS/blob/master/2.png)

### 📊 **Metadata Updates in `metadata.csv`**
![Metadata CSV](https://github.com/MohamedElquesni/Database-Management-System-DBMS/blob/master/3.png)

### 🌳 **B+ Tree Index Structure**
![B+ Tree Index](https://github.com/MohamedElquesni/Database-Management-System-DBMS/blob/master/1.png)

---

## 📝 License, Contributions & Future Enhancements
- **License:** This project is licensed under **MIT License**.
- **Contributions:** We welcome community contributions! 🚀 Fork the repo and submit a pull request.
- **Planned Enhancements:**
  - ✅ Implement **SQL Query Optimizations**.
  - ✅ Extend indexing mechanisms with **Hash Indexing**.
  - ✅ Introduce **Concurrency Control** for multi-threaded query execution.
  - ✅ Support **ACID Transactions** for complete database reliability.

💡 **Built with Java 11, Maven, and a passion for databases!** 🚀🔥
