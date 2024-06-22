import java.sql.Struct;
import java.util.ArrayList;
import java.util.Scanner;

class Car{
    private String carId;
    private String brand;
    private String model;
    private double basePrice;
    private boolean isAvailable;

    public Car(String carId,String brand,String model,double basePrice){
        this.carId=carId;
        this.model=model;
        this.brand=brand;
        this.basePrice=basePrice;
        this.isAvailable=true;
    }

    public String getCarId(){
        return carId;
    }
    public String getBrand(){
        return brand;
    }
    public String getModel(){
        return model;
    }

    public double getBasePrice(){
        return basePrice;
    }
    public boolean isAvailable(){
        return isAvailable;
    }

    public double calculatePrice(int rentalDays){
        return basePrice*rentalDays;
    }
    public void returnCar(){
        isAvailable=true;
    }
    public void rentCar(){
        isAvailable=false;
    }

}

class Customer{
    private String customerId;
    private String name;

    public Customer(String customerId,String name){
        this.customerId=customerId;
        this.name=name;
    }

    public String getCustomerId(){
        return customerId;
    }

    public String getName(){
        return name;
    }
}

class Rental{
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car,Customer customer,int days){
        this.car=car;
        this.customer=customer;
        this.days=days;
    }


    public Car getCar(){
        return car;
    }
    public Customer getCustomer(){
        return customer;
    }

    public int getDays(){
        return days;
    }
}

class CarRentalSystem{

    private ArrayList<Car>cars;
    private ArrayList<Customer>customers;
    private ArrayList<Rental>rentals;

    public CarRentalSystem(){
        cars=new ArrayList<>();
        customers=new ArrayList<>();
        rentals=new ArrayList<>();
    }

    public void addCar(Car car){
        cars.add(car);
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
    }


    public void rentCar(Car car, Customer customer,int days){
        if(car.isAvailable()){
            car.rentCar();
            rentals.add(new Rental(car,customer,days));
        }
        else{
            System.out.println("Car is not available for the rent😥");
        }
    }


    public void returnCar(Car car){
        car.returnCar();

        Rental rentalToRemove=null;

        for(Rental rental:rentals){
            if(rental.getCar()==car){
                rentalToRemove=rental;
                break;
            }
        }
        if(rentalToRemove!=null){
            rentals.remove(rentalToRemove);
        }
        else{
            System.out.println("Car was not rented yet!");
        }

    }

    public void listCustomers() {
        if(customers.isEmpty()){
            System.out.println("No Customer Found!");
        }
        else{
            System.out.println("===== List of Customers =====");
            for (Customer customer : customers) {
                System.out.println("Customer ID: " + customer.getCustomerId() + ", Name: " + customer.getName());
            }

        }

    }

    public void listRentedCars() {
        if(rentals.isEmpty()){
            System.out.println("No cars rented yet!");
        }
        else {


            System.out.println("===== List of Rented Cars =====");
            for (Rental rental : rentals) {
                Car car = rental.getCar();
                Customer customer = rental.getCustomer();
                int rentalDays = rental.getDays();
                System.out.println("Car: " + car.getBrand() + " " + car.getModel() +
                        ", Rented by: " + customer.getName() +
                        ", Rental Days: " + rentalDays);
            }
        }
    }
    public void calculateTotalRevenue() {
        double totalRevenue = 0.0;
        for (Rental rental : rentals) {
            Car car = rental.getCar();
            int rentalDays = rental.getDays();
            totalRevenue += car.calculatePrice(rentalDays);
        }
        System.out.printf("Total Revenue: $%.2f%n", totalRevenue);
    }


    public void menu(){



        Scanner sc=new Scanner(System.in);
        while(true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. List All Customers");
            System.out.println("4. List Rented Cars");
            System.out.println("5. Calculate Total Revenue");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if(choice==1){
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = sc.nextLine();

                System.out.println("\nAvailable Cars:");

                for(Car car:cars){
                    if(car.isAvailable()){
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }
                System.out.print("\nEnter the car ID you want to rent: ");
                String carId=sc.nextLine();
                System.out.print("Enter the number of days for rental: ");
                int rentalDays=sc.nextInt();


                sc.nextLine();

                Customer newCustomer=new Customer("CUS"+(customers.size()+1),customerName);
                addCustomer(newCustomer);


                Car selectedCar=null;
                for(Car car:cars){
                    if(car.getCarId().equals(carId)&&car.isAvailable()){
                        selectedCar=car;
                        break;
                    }
                }
                if(selectedCar!=null){
                    double totalPrice=selectedCar.calculatePrice(rentalDays);

                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = sc.nextLine();

                    if(confirm.equalsIgnoreCase("Y")){
                        rentCar(selectedCar,newCustomer,rentalDays);
                        System.out.println("\nCar rented successfully.");
                    }
                    else{
                        System.out.println("\nRental Cancelled❗");
                    }
                }
                else{
                    System.out.println("\nInvalid car selection or car not available for rent.");
                }


            }
            else if(choice==2){

                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = sc.nextLine();

                Car carToReturn=null;
                for(Car car:cars){
                    if(car.getCarId().equals(carId)&&!car.isAvailable()){
                        carToReturn=car;
                        break;
                    }
                }
                if(carToReturn!=null){
                    Customer customer=null;
                    for(Rental rental:rentals){
                        if(rental.getCar()==carToReturn){
                            customer=rental.getCustomer();
                            break;
                        }
                    }
                    if(customer!=null){
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getName());
                    }
                    else{
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                }
                else {
                    System.out.println("Invalid car ID or car is not rented.");
                }


            }
            else if(choice==3){
                listCustomers();


            }
            else if(choice==4){
                listRentedCars();


            }
            else if(choice==5){
                calculateTotalRevenue();

            }
            else if (choice==6){
                break;
            }else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }

        }
        System.out.println("\nThank you for using the Car Rental System!");

    }




}





public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();
        Car car1=new Car("C001","Tata","Nexon",70.0);
        Car car2=new Car("C002","Maruti","Eco",50);
        Car car3=new Car("C003","Mahindra","Thar",80);


        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);
        rentalSystem.menu();


    }
}