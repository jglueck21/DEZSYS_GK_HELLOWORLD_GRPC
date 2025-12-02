import sys
import grpc
from resources import hello_pb2
from resources import hello_pb2_grpc

def main():
    firstname = sys.argv[1] if len(sys.argv) > 1 else "Julian"
    lastname = sys.argv[2] if len(sys.argv) > 2 else "Glueck"

    # Connect to Java gRPC server
    with grpc.insecure_channel("localhost:50051") as channel:
        stub = hello_pb2_grpc.HelloWorldServiceStub(channel)

        # --- Call hello() RPC ---
        hello_request = hello_pb2.HelloRequest(firstname=firstname, lastname=lastname)
        hello_response = stub.hello(hello_request)
        print("Hello RPC response:", hello_response.text)

        # --- Call sendWarehouse() RPC ---
        product1 = hello_pb2.ProductData(
            productId=101,
            productName="MacBook Air",
            productCategory="Electronics",
            quantity=50,
            price=999
        )
        product2 = hello_pb2.ProductData(
            productId=102,
            productName="Analog Clock",
            productCategory="Gadgets",
            quantity=30,
            price=49.99
        )

        warehouse_record = hello_pb2.WarehouseRecord(
            warehouseID="WH-001",
            warehouseName="Main Warehouse",
            timestamp="2025-12-02T10:15:30Z",
            productDataList=[product1, product2]
        )

        warehouse_response = stub.sendWarehouse(warehouse_record)
        print("\nWarehouse RPC response:", warehouse_response.text)

        print("Sent Warehouse Record:")
        print(f"ID: {warehouse_record.warehouseID}")
        print(f"Name: {warehouse_record.warehouseName}")
        print(f"Timestamp: {warehouse_record.timestamp}")
        print("Products:")
        for p in warehouse_record.productDataList:
            print(f"- {p.productId}: {p.productName} ({p.productCategory}), Quantity: {p.quantity}, Price: {p.price:.2f}")


if __name__ == "__main__":
    main()
