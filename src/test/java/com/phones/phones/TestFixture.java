package com.phones.phones;

import com.phones.phones.dto.*;
import com.phones.phones.model.*;

import java.util.*;

import static com.phones.phones.model.LineStatus.*;

public class TestFixture {

    /*********** User role ***********/

    public static Set<UserRole> testEmployeeRole() {
        Set<UserRole> role = new HashSet<UserRole>();
        UserRole employee = UserRole
                .builder()
                .id(1L)
                .role("Employee")
                .build();
        role.add(employee);
        return role;
    }

    public static Set<UserRole> testClientRole() {
        Set<UserRole> role = new HashSet<UserRole>();
        UserRole client = UserRole
                .builder()
                .id(1L)
                .role("Client")
                .build();
        role.add(client);
        return role;
    }

    public static Set<UserRole> testBothUserRoles() {
        Set<UserRole> bothRoles = new HashSet<UserRole>();

        UserRole client = UserRole
                .builder()
                .id(1L)
                .role("Client")
                .build();

        UserRole employee = UserRole
                .builder()
                .id(1L)
                .role("Employee")
                .build();

        bothRoles.add(client);
        bothRoles.add(employee);

        return bothRoles;
    }



    /*********** User objects ***********/

    public static User testDisabledUser() {
        User newUser = User.builder()
                .id(1L)
                .name("Rodrigo")
                .surname("Leon")
                .dni("404040")
                .username("rl")
                .password("123")
                .isActive(false)
                .city(new City())
                .userRoles(testBothUserRoles())
                .lines(new ArrayList<>())
                .build();

        return newUser;
    }

    public static User testUser() {
        User newUser = User.builder()
                .id(1L)
                .name("Rodrigo")
                .surname("Leon")
                .dni("404040")
                .username("rl")
                .password("123")
                .isActive(true)
                .city(new City())
                .userRoles(testBothUserRoles())
                .lines(new ArrayList<>())
                .build();
        return newUser;
    }

    public static User testClientUser() {
        User newUser = User.builder()
                .id(1L)
                .name("Rodrigo")
                .surname("Leon")
                .dni("404040")
                .username("rl")
                .password("123")
                .isActive(true)
                .city(new City())
                .userRoles(testClientRole())
                .lines(new ArrayList<>())
                .build();
        return newUser;
    }

    public static List<User> testListOfUsers() {
        List<User> users = new ArrayList<>();
        User newUser = User.builder()
                .id(1L)
                .name("Rodrigo")
                .surname("Leon")
                .dni("404040")
                .username("rl")
                .password("123")
                .isActive(true)
                .city(new City())
                .userRoles(testBothUserRoles())
                .lines(new ArrayList<>())
                .build();

        User newUser2 = User.builder()
                .id(2L)
                .name("Jose")
                .surname("Calandra")
                .dni("373737")
                .username("jc")
                .password("123")
                .isActive(true)
                .city(new City())
                .userRoles(testBothUserRoles())
                .lines(new ArrayList<>())
                .build();
        users.add(newUser);
        users.add(newUser2);

        return users;
    }


    public static UserDto testUserDto() {
        UserDto newUser = UserDto.builder()
                .name("Jose")
                .surname("Calandra")
                .dni("37557189")
                .username("jc")
                .password("123456")
                .city(new City())
                .userRoles(testBothUserRoles())
                .build();
        return newUser;
    }


    public static UserLoginDto testUserLoginDto() {
        UserLoginDto newUser = UserLoginDto.builder()
                .username("jc")
                .password("123456")
                .build();
        return newUser;
    }



    /*********** Rates objects ***********/

    public static List<RateDto> testListOfRatesDto() {
        List<RateDto> rates = new ArrayList<>();
        RateDto newRate = RateDto.builder()
                .id(1L)
                .originCity("Mar del plata")
                .destinationCity("Buenos Aires")
                .priceMinute((float) 5)
                .build();

        RateDto newRate2 = RateDto.builder()
                .id(2L)
                .originCity("Mar del plata")
                .destinationCity("Miramar")
                .priceMinute((float) 2.5)
                .build();
        rates.add(newRate);
        rates.add(newRate2);

        return rates;
    }


    public static List<Rate> testListOfRates() {

        List<Rate> rates = new ArrayList<>();
        Rate newRate = Rate.builder()
                .id(1L)
                .priceMinute((float) 5)
                .cost((float)2.5)
                .originCity(new City())
                .destinationCity(new City())
                .calls(new ArrayList<>())
                .build();

        Rate newRate2 = Rate.builder()
                .id(2L)
                .priceMinute((float) 10)
                .cost((float)4)
                .originCity(new City())
                .destinationCity(new City())
                .calls(new ArrayList<>())
                .build();

        rates.add(newRate);
        rates.add(newRate2);
        return rates;
    }




    /*********** Province objects ***********/

    public static Province testProvince() {
        Province newProvince = Province.builder()
                .id(1L)
                .name("Buenos Aires")
                .cities(new ArrayList<City>())
                .build();
        return newProvince;


    }

    public static List<Province> testListOfProvinces() {
        List<Province> provinces = new ArrayList<>();
        Province newProvince = Province
                .builder()
                .id(1L)
                .name("Buenos Aires")
                .cities(new ArrayList<City>())
                .build();

        Province newProvince2 = Province
                .builder()
                .id(1L)
                .name("Buenos Aires")
                .cities(new ArrayList<City>())
                .build();

        provinces.add(newProvince);
        provinces.add(newProvince2);

        return provinces;
    }



    /*********** City objects ***********/

    public static City testCity() {
        City newCity = City.builder()
                .id(1L)
                .name("Mar del Plata")
                .prefix("0223")
                .province(testProvince())
                .users(testListOfUsers())
                .originRates(testListOfRates())
                .destinationRates(testListOfRates())
                .build();
        return newCity;
    }

    public static List<City> testListOfCities() {

        List<City> cities = new ArrayList<>();
        City newCity = City.builder()
                .id(1L)
                .name("Mar del Plata")
                .prefix("0223")
                .province(testProvince())
                .users(testListOfUsers())
                .originRates(testListOfRates())
                .destinationRates(testListOfRates())
                .build();

        City newCity2 = City.builder()
                .id(2L)
                .name("Balcarce")
                .prefix("2266")
                .province(testProvince())
                .users(testListOfUsers())
                .originRates(testListOfRates())
                .destinationRates(testListOfRates())
                .build();

        cities.add(newCity);
        cities.add(newCity2);
        return cities;
    }


    public static List<CityTopDto> testListOfCityTop() {
        List<CityTopDto> topCities = new ArrayList<>();
         CityTopDto topCity = CityTopDto.builder()
                .name("Capital Federal")
                .quantity((long) 10)
                .build();

        CityTopDto topCity2 = CityTopDto.builder()
                .name("Mar del Plata")
                .quantity((long) 15)
                .build();

        topCities.add(topCity);
        topCities.add(topCity2);

        return topCities;
    }



    /*********** Lines objects ***********/


    public static LineDto testLineDto() {
        LineDto newLineDto = LineDto.builder()
                .number("2235472861")
                .status(ENABLED)
                .lineType(new LineType(1L, "Mobile", new ArrayList<>()))
                .build();
        return newLineDto;
    }


    public static Line testLine(String number) {
        Line newLine = Line.builder()
                .id(1L)
                .number(number)
                .creationDate(new Date(2020, 01, 10))
                .status(ENABLED)
                .lineType(new LineType(1L, "Mobile", new ArrayList<>()))
                .user(testUser())
                .invoices(new ArrayList<>())
                .originCalls(new ArrayList<>())
                .destinationCalls(new ArrayList<>())
                .build();
        return newLine;
    }

    public static Line testDisabledLine(String number) {
        Line newLine = Line.builder()
                .id(1L)
                .number(number)
                .creationDate(new Date(2020, 01, 10))
                .status(DISABLED)
                .lineType(new LineType(1L, "Mobile", new ArrayList<>()))
                .user(testUser())
                .invoices(new ArrayList<>())
                .originCalls(new ArrayList<>())
                .destinationCalls(new ArrayList<>())
                .build();
        return newLine;
    }

    public static Line testSuspendedLine(String number) {
        Line newLine = Line.builder()
                .id(1L)
                .number(number)
                .creationDate(new Date(2020, 01, 10))
                .status(SUSPENDED)
                .lineType(new LineType(1L, "Mobile", new ArrayList<>()))
                .user(testUser())
                .invoices(new ArrayList<>())
                .originCalls(new ArrayList<>())
                .destinationCalls(new ArrayList<>())
                .build();
        return newLine;
    }

    public static List<Line> testListOfLines() {
        List<Line> lines = new ArrayList<>();
        Line newLine = Line.builder()
                .id(1L)
                .number("2235472861")
                .creationDate(new Date(2020, 01, 10))
                .status(ENABLED)
                .lineType(new LineType(1L, "Mobile", new ArrayList<>()))
                .user(testUser())
                .invoices(new ArrayList<>())
                .originCalls(new ArrayList<>())
                .destinationCalls(new ArrayList<>())
                .build();

        Line newLine2 = Line.builder()
                .id(1L)
                .number("223222222")
                .creationDate(new Date(2020, 01, 10))
                .status(ENABLED)
                .lineType(new LineType(1L, "Mobile", new ArrayList<>()))
                .user(testUser())
                .invoices(new ArrayList<>())
                .originCalls(new ArrayList<>())
                .destinationCalls(new ArrayList<>())
                .build();

        lines.add(newLine);
        lines.add(newLine2);

        return  lines;
    }


    /*********** Calls Objects ***********/

    public static Call testCall() {
        Call newCall = Call.builder()
                .id(1L)
                .duration(5)
                .totalPrice((float) 15)
                .totalCost((float)10)
                .creationDate(new Date(2020, 06, 10))
                .originNumber("2235472861")
                .destinationNumber("22322222")
                .originLine(new Line())
                .destinationLine((new Line()))
                .rate(new Rate())
                .invoice(new Invoice())
                .build();
        return newCall;
    }

    public static Call testConstCall() {
        Call newCall = new Call(1L, 5, (float)15,(float)10,new Date(2020, 06, 10),"2235472861","22322222",new Line(),new Line(), new Rate(), new Invoice());

        return newCall;
    }

    public static List<Call> testListOfCalls() {
        List<Call> calls = new ArrayList<>();
        Call newCall = Call.builder()
                .id(1L)
                .duration(5)
                .totalPrice((float) 15)
                .creationDate(new Date(2020, 06, 10))
                .originNumber("2235472861")
                .destinationNumber("22322222")
                .originLine(new Line())
                .destinationLine((new Line()))
                .rate(new Rate())
                .invoice(new Invoice())
                .build();

        Call newCall2 = Call.builder()
                .id(2L)
                .duration(10)
                .totalPrice((float) 30)
                .creationDate(new Date(2020, 06, 14))
                .originNumber("22322222")
                .destinationNumber("2235472861")
                .originLine(new Line())
                .destinationLine((new Line()))
                .rate(new Rate())
                .invoice(new Invoice())
                .build();

        calls.add(newCall);
        calls.add(newCall2);

        return calls;
    }

    public static InfrastructureCallDto testInfrastructureCallDto() {
        InfrastructureCallDto newCall = InfrastructureCallDto.builder()
                .originNumber("2235472861")
                .destinationNumber("22322222")
                .duration(5)
                .creationDate(new Date(2020, 06, 10))
                .build();
        return newCall;
    }


    /*********** Invoice objects ***********/

    public static Invoice testInvoice() {
        Invoice newInvoice = Invoice.builder()
                .id(1L)
                .numberCalls(15)
                .costPrice((float) 200)
                .totalPrice((float) 300)
                .creationDate(new Date(2020, 06, 10))
                .dueDate(new Date(2020, 06, 30))
                .line(testLine("2235472861"))
                .calls(testListOfCalls())
                .build();
        return newInvoice;
    }

    public static List<Invoice> testListOfInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        Invoice newInvoice = Invoice.builder()
                .id(1L)
                .numberCalls(15)
                .costPrice((float) 200)
                .totalPrice((float) 300)
                .creationDate(new Date(2020, 06, 30))
                .dueDate(new Date(2020, 07, 15))
                .line(testLine("2235472861"))
                .calls(testListOfCalls())
                .build();

        Invoice newInvoice2 = Invoice.builder()
                .id(2L)
                .numberCalls(30)
                .costPrice((float) 400)
                .totalPrice((float) 600)
                .creationDate(new Date(2020, 07, 30))
                .dueDate(new Date(2020, 8, 15))
                .line(testLine("2235472861"))
                .calls(testListOfCalls())
                .build();

        invoices.add(newInvoice);
        invoices.add(newInvoice2);

        return invoices;
    }


}
