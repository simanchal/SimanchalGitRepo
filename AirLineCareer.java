package com.my.tools;

public abstract class AirLineCareer {
  TravelClass _class;
  double fare;
  PassengerClass _passengerClass;

  abstract double calculateFinalFare();

  public double calculate() {
    //add base fee
    fare = fare + 1.0;
    return calculateFinalFare();
  }

  enum TravelClass {
    BUSINESS, ECONOMIC;

    public void calculate() {

    }
  }

  interface PassengerClass {
    double calculate(double baseFare);
  }

  static class BusinessPassenger implements PassengerClass {

    @Override
    public double calculate(double baseFare) {
      return 0;
    }
  }
}

class Delta extends AirLineCareer {
  @Override
  double calculateFinalFare() {
    //base fare calculate
    fare = fare + 5.9;
    //return super.calculate();
    switch (_class) {
      case BUSINESS:
        fare = fare + 10.0;
        break;
      case ECONOMIC:
        fare = fare + 20.0;
        break;
    }
    return _passengerClass.calculate(fare);
    //return fare;
  }
}

class FairCalculator {
  public double finalFair() {
    AirLineCareer aa = new Delta();
    return aa.calculate();
  }
}
