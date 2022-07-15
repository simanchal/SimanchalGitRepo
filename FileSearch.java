package com.linkedin.tools;

import java.util.ArrayList;
import java.util.List;


public class FileSearch {
  public List<String> search(String path, String searchExpr) {
    Search search = Search.parse(searchExpr);
    assert search != null;
    return search.filter(path);
  }

  abstract static class Search {
    FileSystem _fileSystem;
    SearchType type;

    public Search(FileSystem fileSystem, SearchType type) {
      _fileSystem = fileSystem;
      this.type = type;
    }

    public abstract List<String> filter(String path);


    public static Search parse(String expr) {
      //parse expression
      return null;
    }

    static class ContentSearchRequest extends Search {
      String content;

      public ContentSearchRequest(FileSystem fileSystem, SearchType type, String content) {
        super(fileSystem, type);
        this.content = content;
      }

      @Override
      public List<String> filter(String path) {
        List<String> result = new ArrayList<>();
        for (String file : _fileSystem.getFileList(path)) {
          if (file.contains(content)) {
            result.add(file);
          }
        }
        return result;
      }
    }

    static class SizeGTRequest extends Search {
      public SizeGTRequest(FileSystem fileSystem, SearchType type) {
        super(fileSystem, type);
      }

      @Override
      public List<String> filter(String path) {
        return null;
      }
    }

    static class FileSystem {
      public List<String> getFileList(String path) {
        return null;
      }
    }

    enum SearchType {
      CONTENT, FILE_EXT
    }
  }
}
