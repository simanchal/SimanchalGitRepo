package com.practice;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class StreamProcessor {
  // Messages are <key, value> pairs
  class Msg {
    long m_key;  // unique
    long m_val;
  }

  // This is the sliding window that needs to be implemented.
  class Window {
    // Define a window of a certain size in microsecond granularity
    long windowSize;
    Map<Long, NBALeagueUtil.Msg> messages;
    Queue<NBALeagueUtil.Msg> queue;
    int runningSum;
    Lock _lock = new ReentrantLock();
    ReentrantReadWriteLock _readWriteLock = new ReentrantReadWriteLock(true);
    Lock _readLock = _readWriteLock.readLock();
    Lock _writeLock = _readWriteLock.writeLock();

    void Window(int nMicrosecs) {
      this.messages = new ConcurrentHashMap<>();
      this.queue = new LinkedList<>();
      this.runningSum = 0;
      tt.schedule(_runnable, 10000l);
    }

    // add a new incoming message
    void addMsg(NBALeagueUtil.Msg m) {
      _writeLock.lock();
      try {
        messages.put(m.m_key, m);
        queue.add(m);
        runningSum += m.m_val;
      } finally {
        _writeLock.unlock();
      }
    }

    // get a message given a key. If the message does not exist or message is older than the window size, return NULL
    // it's *important* to be correct
    NBALeagueUtil.Msg getMsg(long key) {
      if (messages.containsKey(key)) {
        NBALeagueUtil.Msg ms = messages.get(key);
        if (ms.m_key < curWindowTime()) {
          clear(key);
          return null;
        } else {
          return ms;
        }
      } else {
        return null;
      }
    }

    private void clear(long key) {
      //clear map and queue
      _writeLock.lock();
      while (!queue.isEmpty() && queue.peek().m_key < curWindowTime()) {
        NBALeagueUtil.Msg top = queue.poll();
        messages.remove(top.m_key);
        runningSum -= top.m_val;
      }
      _writeLock.unlock();
    }

    // get the average of message values in the window. Like getMsg, it's important to be correct.
    Double getAvg() {
      _readLock.lock();
      try {
        clear(20);
        if (!queue.isEmpty()) {
          int avg = runningSum / queue.size();
        }
      } finally {
        _readLock.unlock();
      }
      return 0.0;
    }

    long curWindowTime() {
      //tt.schedule(_runnable,10000l);
      return System.currentTimeMillis() - 5 * 60 * 1000;
    }

    Timer tt = new Timer();
    TimerTask _runnable = new TimerTask() {
      @Override
      public void run() {
        _readLock.lock();
        try {
          while (true) {
            if (queue.isEmpty() && queue.peek().m_key < curWindowTime()) {
              clear(20);
            }
            try {
              Thread.sleep(1000l);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        } finally {
          _readLock.unlock();
        }
      }
    };
  }
}
