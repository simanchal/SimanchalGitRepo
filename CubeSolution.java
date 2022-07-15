package com.linkedin.tools;
// Lets say you have a dataset

// Country    State City               Users
// USA          CA     San Jose      10
// USA          CA     Cupertino     20
// USA          TX     Austin           20
// India          TN    Chennai        100
// India          MH   Mumbai         200
// USA          CA     Cupertino     50

// Give me total aggregate - 400 (*,*,*)
// Aggregate for  CA - 80              (*,CA,*)
// Aggregate for Cupertino - 70   (*,*,Cupertino)
// Aggregate for India - 300         (India,*,*)

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CubeSolution {
  Map<String, Integer> cache = new HashMap<>();
  // Lets say you have a dataset

  // Country    State City               Users
  // USA          CA     San Jose      10
  // USA          CA     Cupertino     20
  // USA          TX     Austin           20
  // India          TN    Chennai        100
  // India          MH   Mumbai         200
  // USA          CA     Cupertino     50

  // Give me total aggregate - 400 (*,*,*)
  // Aggregate for  CA - 80              (*,CA,*)
  // Aggregate for Cupertino - 70   (*,*,Cupertino)
  // Aggregate for India - 300         (India,*,*)
  //country
  //state
  //city
  //country
  public long totalUsers(String country, String state, String city) {
    List<String> dimensions = new ArrayList<>();
    if (!country.equals("*")) {
      dimensions.add(country);
    }
    if (!state.equals("*")) {
      dimensions.add(state);
    }
    if (!city.equals("*")) {
      dimensions.add(city);
    }
    if (dimensions.isEmpty()) {
      return cache.get("");
    }
    String queryKey = String.join("-", dimensions);
    return cache.get(queryKey);
  }

  public void preComputeCache(List<UserInfo> inputs) {
    for (UserInfo ifo : inputs) {
      for (String key : getDimensionPermutation(ifo.getDimensionFieldList())) {
        cache.putIfAbsent(key, 0);
        cache.put(key, cache.get(key) + ifo.userCount);
      }
    }
  }

  private List<String> getDimensionPermutation(List<String> inputDimension) {
    List<String> permutationKeys = new ArrayList<>();
    for (int i = 0; i < inputDimension.size(); i++) {
      getPermutations(0, i, inputDimension, permutationKeys, new ArrayList<>(), 0);
    }
    return permutationKeys;
  }

  private void getPermutations(int st, int permutationSize, List<String> dimension, List<String> globalResult,
      List<String> localResult, int localSize) {
    if (permutationSize == 0) {
      globalResult.add("");
      return;
    }
    if (permutationSize == localResult.size()) {
      globalResult.add(String.join("-", localResult));
      return;
    }

    for (int i = st; i < dimension.size(); i++) {
      localResult.add(dimension.get(i));
      getPermutations(i + 1, permutationSize, dimension, globalResult, localResult, localSize + 1);
      localResult.remove(localResult.size() - 1);
    }
  }

  static class UserInfo {
    public String country;
    public String state;
    public String city;
    public int userCount;

    public UserInfo(String country, String state, String city, int userCount) {
      this.country = country;
      this.state = state;
      this.city = city;
      this.userCount = userCount;
    }

    public List<String> getDimensionFieldList() {
      List<String> fields = new ArrayList<>();
      fields.add(this.country);
      fields.add(this.state);
      fields.add(this.city);
      return fields;
    }
  }
  // Country    State City               Users
  // USA          CA     San Jose      10
  // USA          CA     Cupertino     20
  // USA          TX     Austin           20
  // India          TN    Chennai        100
  // India          MH   Mumbai         200
  // USA          CA     Cupertino     50

  // Give me total aggregate - 400 (*,*,*)
  // Aggregate for  CA - 80              (*,CA,*)
  // Aggregate for Cupertino - 70   (*,*,Cupertino)
  // Aggregate for India - 300         (India,*,*)
  // Aggregate for India - 300         (India,*,Mumbai)

  public static void main(String[] args) {
    UserInfo input1 = new UserInfo("USA", "CA", "San Jose", 10);
    UserInfo input2 = new UserInfo("USA", "CA", "Cupertino", 20);
    UserInfo input3 = new UserInfo("USA", "TX", "Austin", 20);
    UserInfo input4 = new UserInfo("India", "TN", "Chennai", 100);
    UserInfo input5 = new UserInfo("India", "MH", "Mumbai", 200);
    UserInfo input6 = new UserInfo("USA", "CA", "Cupertino", 50);
    CubeSolution cube = new CubeSolution();
    List<UserInfo> inputs = new ArrayList<>();
    inputs.add(input1);
    inputs.add(input2);
    inputs.add(input3);
    inputs.add(input4);
    inputs.add(input5);
    inputs.add(input6);
    cube.preComputeCache(inputs);

    System.out.println(cube.totalUsers("*", "*", "*"));
    System.out.println();
    System.out.println(cube.totalUsers("*", "CA", "*"));
    System.out.println(cube.totalUsers("*", "*", "Cupertino"));
    System.out.println(cube.totalUsers("India", "*", "*"));
  }
}
