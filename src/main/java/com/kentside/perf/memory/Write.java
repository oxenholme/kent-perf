/**
 * This file is part of the Kentside Performance Test Suite.
 * Copyright (C) Helm Solutions Ltd (kentside@yandex.com)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.kentside.perf.memory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

/**
 * Essentially trying to demonstrate effect of cache lime size.
 * 
 * The expectation is that execution time will vary little with a
 * step size below the cache line size, but will vary linearly with
 * a step size above the cache line size.
 */
@State(Scope.Benchmark)
public class Write {
    static final int CACHELINE_SIZE = 64;

    byte[] bytes = new byte[1024 * 1024];

    static int base = 0;

    static synchronized int getOffset(int divisor) {
        base += CACHELINE_SIZE / divisor;
        return base;
    }

    @State(Scope.Thread)
    public static class OnePer /*CacheLine */ {
        public int offset = getOffset(1);
    }

    @State(Scope.Thread)
    public static class TwoPer /*CacheLine */ {
        public int offset = getOffset(2);
    }

    @State(Scope.Thread)
    public static class FourPer /*CacheLine */ {
        public int offset = getOffset(4);
    }

    @Benchmark
    @Threads(2)
    public void onePer(OnePer op) {
//        for (int i = op.offset, n = bytes.length; i < n; i += 1024) {
//            bytes[i] = 17;
//        }
        bytes[op.offset] = 17;
    }

    @Benchmark
    @Threads(2)
    public void twoPer(TwoPer op) {
//        for (int i = op.offset, n = bytes.length; i < n; i += 1024) {
//            bytes[i] = 17;
//        }
        bytes[op.offset] = 71;
    }
}
