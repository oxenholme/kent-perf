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

package com.kentside.perf.processor;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * Essentially trying to test if division really is slow.
 */
@State(Scope.Benchmark)
public class Operators {
    int a = 0x535353;
    int b = 0x1313;

    double c = 1234567.89;
    double d = 98765.4321;

    @Benchmark
    public int intNone() {
        return a;
    }

    @Benchmark
    public double doubleNone() {
        return c;
    }

    @Benchmark
    public int intAdd() {
        return a + b;
    }

    @Benchmark
    public int intMul() {
        return a * b;
    }

    @Benchmark
    public int intDiv() {
        return a / b;
    }

    @Benchmark
    public double doubleAdd() {
        return c + d;
    }

    @Benchmark
    public double doubleMul() {
        return c * d;
    }

    @Benchmark
    public double doubleDiv() {
        return c / d;
    }
}
