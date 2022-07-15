package com.my.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonParser {

  private final static String jsonString =
      "{'name':'user','id':1234,'marks':[{'english':85,'physics':80,'chemistry':75}]}";

  public static void main(String[] args) {
    JsonParser jsonParser = new JsonParser();
    StringBuilder test = new StringBuilder(jsonString);
    System.out.println(jsonParser.replaceComma(test));
  }

  static class JSONObject {
    Map<String, String> objects;

    public JSONObject(String jsonStr) {
      objects = new HashMap<>();
      parse(jsonStr);
    }

    public void parse(String jsonStr) {
      if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
        StringBuilder json = new StringBuilder(jsonStr);
        json.deleteCharAt(0);
        json.deleteCharAt(json.length() - 1);
        //replace the comma inside the json array objects
        replaceComma(json);
        for (String object : json.toString().split(",")) {
          String[] keyVal = object.split(":", 2);
          objects.put(keyVal[0].replace("'", ""), keyVal[1].replace("'", ""));
        }
      }
    }

    public String getObject(String element) {
      return objects.get(element);
    }

    public JSONArray getArray(String element) {
      return new JSONArray(objects.get(element).replace("|", ","));
    }
  }

  static class JSONArray {
    List<String> _jsonObjectList;

    public JSONArray(String jsonString) {
      _jsonObjectList = new ArrayList<>();
    }

    void parse(String jsonStr) {
      if (jsonStr.startsWith("[") && jsonStr.endsWith("]")) {
        StringBuilder json = new StringBuilder(jsonStr);
        json.deleteCharAt(0);
        json.deleteCharAt(json.length() - 1);
        //replace the comma inside the json array objects
        //replace the comma inside the json array objects
        replaceComma(json);
        for (String object : json.toString().split(",")) {
          _jsonObjectList.add(object);
        }
      }
    }

    public JSONObject getElement(int idx) {
      return new JSONObject(_jsonObjectList.get(idx).replace("|", ","));
    }
  }

  public static StringBuilder replaceComma(StringBuilder str) {
    boolean isArray = false;
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (isArray) {
        if (c == ',') {
          str.setCharAt(i, '|');
        }
      }
      if (c == '[') {
        isArray = true;
      }
      if (c == ']') {
        isArray = false;
      }
    }
    return str;
  }
}



