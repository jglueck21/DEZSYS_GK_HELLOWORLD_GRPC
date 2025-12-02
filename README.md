# DEZSYS_GK72_DATAWAREHOUSE_GRPC Documentation
**GitHub Repository:** [https://github.com/jglueck21/DEZSYS_GK_HELLOWORLD_GRPC]

**Author:** _Julian Glueck_  
**Date:** _2025-12-02_

---
## 1. Introduction
This project demonstrates the implementation of **gRPC (Remote Procedure Call)** technology to build a simple middleware system. The purpose is to show:

- A basic **HelloWorld** RPC service.
- How to extend the service to transfer a **DataWarehouse record** between client and server.
- Multi-language client-server communication (Java server, Python client).

We use **Protocol Buffers** (`.proto`) to define services and data structures.

---

## 2. Implementation Steps

### 2.1 Initial Setup

1. Cloned the original HelloWorld gRPC project from [GitHub](https://github.com/ThomasMicheler/DEZSYS_GK_HELLOWORLD_GRPC.git).    
2. Verified the original HelloWorld service was running correctly with Java server and Python client.
3. Examined the original files:
    

|File|Description|
|---|---|
|`hello.proto`|Defines `HelloWorldService` with `hello()` RPC.|
|`HelloWorldServer.java`|Java gRPC server implementation.|
|`HelloWorldClient.java`|Java gRPC client implementation.|
|`HelloWorldServiceImpl.java`|Service logic for `hello()`.|
|`helloWorldClient.py`|Python gRPC client for `hello()`.|

---

### 2.2 Proto File Extension

**Changes Made:**
- Added package and Java options:
`package hello; option java_package = "hello"; option java_outer_classname = "Hello";`

- Added a new RPC `sendWarehouse()` to transfer `WarehouseRecord`:
`rpc sendWarehouse(WarehouseRecord) returns (HelloResponse) {}`

- Added messages `ProductData` and `WarehouseRecord` to represent warehouse data:    
``` Java
message ProductData {
	int32 productId = 1;
	string productName = 2;
	string productCategory = 3;
	int32 quantity = 4;
	double price = 5;
}
message WarehouseRecord {
	string warehouseID = 1;
	string warehouseName = 2;
	string timestamp = 3;
	repeated ProductData productDataList = 4;
}
```

**Reasoning:**  
This allows the service to transfer structured warehouse information, including a list of products with ID, name, category, quantity, and price.

---

### 2.3 Server Implementation

**Changes Made in `HelloWorldServiceImpl.java`:**
- Added `sendWarehouse()` method:
``` Java
@Override public void sendWarehouse(Hello.WarehouseRecord request, StreamObserver<Hello.HelloResponse> responseObserver) {
	System.out.println("Received Warehouse Record: " + request.getWarehouseID());
	Hello.HelloResponse response = Hello.HelloResponse.newBuilder()             .setText("Warehouse record received: " + request.getWarehouseID())             .build();
	responseObserver.onNext(response);
	responseObserver.onCompleted(); }
```

**Notes:**
- Iterates over `ProductDataList` to print each product.
- Returns a confirmation message as `HelloResponse`.
**Server Launch:** No major changes; prints server start info.

---

### 2.4 Java Client Changes

**Changes Made in `HelloWorldClient.java`:**
- Updated default name values to `"Julian Glueck"`.
- Added code to call the new `sendWarehouse()` RPC:
``` Java
Hello.ProductData product1 = Hello.ProductData.newBuilder()... Hello.WarehouseRecord warehouseRecord = Hello.WarehouseRecord.newBuilder()... Hello.HelloResponse warehouseResponse = stub.sendWarehouse(warehouseRecord);
```

- Prints detailed warehouse record and response.
    
**Reasoning:**  
Allows testing the extended RPC by sending multiple product entries to the server.

---

### 2.5 Python Client Changes

**Changes Made in `helloWorldClient.py`:**

- Updated default name values to `"Julian Glueck"`.

- Added code to call `sendWarehouse()` on the Java server:
``` Java
product1 = hello_pb2.ProductData(...) warehouse_record = hello_pb2.WarehouseRecord(...) warehouse_response = stub.sendWarehouse(warehouse_record)
```

- Prints received confirmation and details of the warehouse record.    

**Reasoning:**  
Demonstrates cross-language compatibility of gRPC services.

---

### 2.6 Testing & Output

**Steps:**
1. Start the Java server:
`java HelloWorldServer`

2. Run the Java client:
`java HelloWorldClient`

3. Run the Python client:
`python helloWorldClient.py`

**Expected Output:**

- **Hello RPC:**
    `Hello Response: Hello World, Julian Glueck`
    
- **Warehouse RPC:**
    ``` Java
    Warehouse Response:
	    Warehouse record received: WH-001
		    Sent Warehouse Record:
			    ID: WH-001
			    Name: Main Warehouse
			    Timestamp: 2025-12-02T10:15:30Z
			    Products:
				    - 101: MacBook Pro (Electronics), Quantity: 50, Price: 19.99
				    - 102: Digital Clock (Gadgets), Quantity: 30, Price: 49.99
    ```
    
- Python client receives similar responses.

**Issues Encountered:**

- Needed to add `package` and `java_package` options in `.proto` to prevent Java compilation errors.
- Wrong Java Version, I used Java24, which didn't work, so I had to force it to use Java17 to work 
- Ensured numbering in messages (`ProductData`) was consecutive.

---

## 3. gRPC & Protocol Buffers Questions

**Q1: What is gRPC and why does it work across languages and platforms?**

- gRPC is a **high-performance RPC framework** that uses Protocol Buffers to serialize structured data.    
- Works across languages because `.proto` files define **language-agnostic service interfaces**, which can generate client/server stubs in multiple languages.

**Q2: Describe the RPC life cycle starting with the client.**

1. Client creates a stub from generated code.
2. Client calls RPC method on stub.
3. gRPC serializes the request using Protocol Buffers.
4. Server receives, deserializes, and executes the method.
5. Server serializes the response and sends it back.
6. Client receives and deserializes the response.

**Q3: Describe the workflow of Protocol Buffers.**

- Define messages in `.proto` files.
- Generate source code using `protoc` compiler.
- Serialize messages to binary format for transmission.
- Deserialize on the receiver side.

**Q4: Benefits of Protocol Buffers**

- Compact binary format → faster transmission.
- Strongly typed and versioned messages.
- Language-agnostic and backward-compatible.

**Q5: When is Protocol Buffers not recommended?**

- When human-readable text format is required (JSON/XML may be better).
- For very simple data structures where overhead of `.proto` compilation is unnecessary.

**Q6: Three data types in Protocol Buffers**

- `int32` / `int64`
- `string`
- `double`

---

### 4. Summary

- Implemented a **HelloWorld gRPC service**.    
- Extended it to **transfer DataWarehouse records**.
- Developed clients in **Java and Python**.
- Documented all implementation steps, including **problems and solutions**.
- Demonstrated cross-language RPC using **gRPC + Protocol Buffers**.
