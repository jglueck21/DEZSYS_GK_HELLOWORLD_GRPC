import io.grpc.stub.StreamObserver;
import hello.Hello;                     // generated messages
import hello.HelloWorldServiceGrpc;     // service stubs


public class HelloWorldServiceImpl extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {

    @Override
    public void hello(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> responseObserver) {
        System.out.println("Received hello request: " + request.getFirstname() + " " + request.getLastname());

        String message = "Hello World, " + request.getFirstname() + " " + request.getLastname();
        Hello.HelloResponse response = Hello.HelloResponse.newBuilder()
                .setText(message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendWarehouse(Hello.WarehouseRecord request, StreamObserver<Hello.HelloResponse> responseObserver) {
        System.out.println("Received Warehouse Record: " + request.getWarehouseID());
        System.out.println("Warehouse Name: " + request.getWarehouseName());
        System.out.println("Timestamp: " + request.getTimestamp());
        System.out.println("Products:");

        for (Hello.ProductData p : request.getProductDataListList()) {
            System.out.printf("- %d: %s (%s), Quantity: %d, Price: %.2f%n",
                    p.getProductId(),
                    p.getProductName(),
                    p.getProductCategory(),
                    p.getQuantity(),
                    p.getPrice());
        }

        Hello.HelloResponse response = Hello.HelloResponse.newBuilder()
                .setText("Warehouse record received: " + request.getWarehouseID())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
