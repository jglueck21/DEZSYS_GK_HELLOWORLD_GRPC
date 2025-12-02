import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import hello.Hello;
import hello.HelloWorldServiceGrpc;


import java.util.Arrays;

public class HelloWorldClient {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        HelloWorldServiceGrpc.HelloWorldServiceBlockingStub stub =
                HelloWorldServiceGrpc.newBlockingStub(channel);

        // --- Call hello() RPC ---
        String firstname = args.length > 0 ? args[0] : "Julian";
        String lastname = args.length > 1 ? args[1] : "Glueck";

        Hello.HelloRequest helloRequest = Hello.HelloRequest.newBuilder()
                .setFirstname(firstname)
                .setLastname(lastname)
                .build();

        Hello.HelloResponse helloResponse = stub.hello(helloRequest);
        System.out.println("Hello Response: " + helloResponse.getText());

        // --- Call sendWarehouse() RPC ---
        Hello.ProductData product1 = Hello.ProductData.newBuilder()
                .setProductId(101)
                .setProductName("MacBook Pro")
                .setProductCategory("Electronics")
                .setQuantity(50)
                .setPrice(19.99)
                .build();

        Hello.ProductData product2 = Hello.ProductData.newBuilder()
                .setProductId(102)
                .setProductName("Digital Clock")
                .setProductCategory("Gadgets")
                .setQuantity(30)
                .setPrice(49.99)
                .build();

        Hello.WarehouseRecord warehouseRecord = Hello.WarehouseRecord.newBuilder()
                .setWarehouseID("WH-001")
                .setWarehouseName("Main Warehouse")
                .setTimestamp("2025-12-02T10:15:30Z")
                .addAllProductDataList(Arrays.asList(product1, product2))
                .build();

        Hello.HelloResponse warehouseResponse = stub.sendWarehouse(warehouseRecord);

        System.out.println("\nWarehouse Response: " + warehouseResponse.getText());
        System.out.println("Sent Warehouse Record:");
        System.out.println("ID: " + warehouseRecord.getWarehouseID());
        System.out.println("Name: " + warehouseRecord.getWarehouseName());
        System.out.println("Timestamp: " + warehouseRecord.getTimestamp());
        System.out.println("Products:");
        for (Hello.ProductData p : warehouseRecord.getProductDataListList()) {
            System.out.printf("- %d: %s (%s), Quantity: %d, Price: %.2f%n",
                    p.getProductId(),
                    p.getProductName(),
                    p.getProductCategory(),
                    p.getQuantity(),
                    p.getPrice());
        }

        channel.shutdown();
    }
}
