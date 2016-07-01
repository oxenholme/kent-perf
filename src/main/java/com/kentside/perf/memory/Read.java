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

/**
 * Essentially trying to demonstrate effect of cache lime size.
 * 
 * The expectation is that execution time will vary little with a
 * step size below the cache line size, but will vary linearly with
 * a step size above the cache line size.
 */
@State(Scope.Benchmark)
public class Read {
    byte[] bytes = new byte[256 * 1024 * 1024];

    @Benchmark
    public int step8() {
        int ret = 0;
        for (int i = 0, n = bytes.length; i < n; i += 1) {
            ret += bytes[i];
        }
        return ret;
    }

    @Benchmark
    public int step16() {
        int ret = 0;
        for (int i = 0, n = bytes.length; i < n; i += 32) {
            ret += bytes[i];
        }
        return ret;
    }

    @Benchmark
    public int step32() {
        int ret = 0;
        for (int i = 0, n = bytes.length; i < n; i += 32) {
            ret += bytes[i];
        }
        return ret;
    }

    @Benchmark
    public int step64() {
        int ret = 0;
        for (int i = 0, n = bytes.length; i < n; i += 64) {
            ret += bytes[i];
        }
        return ret;
    }

    @Benchmark
    public int step128() {
        int ret = 0;
        for (int i = 0, n = bytes.length; i < n; i += 128) {
            ret += bytes[i];
        }
        return ret;
    }

    @Benchmark
    public int step256() {
        int ret = 0;
        for (int i = 0, n = bytes.length; i < n; i += 256) {
            ret += bytes[i];
        }
        return ret;
    }
}
