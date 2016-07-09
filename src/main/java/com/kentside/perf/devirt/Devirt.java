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

package com.kentside.perf.devirt;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Param;
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
public class Devirt {
    interface Op {
        int op(int i);
    }

    static class Inc implements Op {
        public int op(int i) {
            return i + 1;
        }
    }

    static class Dec implements Op {
        public int op(int i) {
            return i - 1;
        }
    }

    static class Add implements Op {
        public int op(int i) {
            return i + 5;
        }
    }

    static class Ops {
        public Op[] ops;
    }

    @State(Scope.Benchmark)
    public static class IncIncIncOps extends Ops {
        public IncIncIncOps() {
            ops = new Op[] {new Inc(), new Inc(), new Inc() };
        }
    }

    @State(Scope.Benchmark)
    public static class IncDecIncOps extends Ops {
        public IncDecIncOps() {
            ops = new Op[] {new Inc(), new Dec(), new Inc() };
        }
    }

    @State(Scope.Benchmark)
    public static class IncDecAddOps extends Ops {
        public IncDecAddOps() {
            ops = new Op[] {new Inc(), new Dec(), new Add() };
        }
    }

    @State(Scope.Benchmark)
    public static class DecAddIncOps extends Ops {
        public DecAddIncOps() {
            ops = new Op[] {new Dec(), new Add(), new Inc() };
        }
    }

    @State(Scope.Benchmark)
    public static class AddIncDecOps extends Ops {
        public AddIncDecOps() {
            ops = new Op[] {new Add(), new Inc(), new Dec() };
        }
    }

    int val = 12345;

    Ops incIncIncOps = new IncIncIncOps();

    Ops incDecIncOps = new IncDecIncOps();

    Ops incDecAddOps = new IncDecAddOps();
    Ops decAddIncOps = new DecAddIncOps();
    Ops addIncDecOps = new AddIncDecOps();

    @Benchmark
    public void mono() {
        Ops ops = this.incIncIncOps;
        for (int i = ops.ops.length; i-- > 0; ) {
            val = ops.ops[i].op(val);
        }
    }

    @Benchmark
    public void bi() {
        Ops ops = this.incDecIncOps;
        for (int i = ops.ops.length; i-- > 0; ) {
            val = ops.ops[i].op(val);
        }
    }

    @Benchmark
    public void tri() {
        Ops ops = this.incDecAddOps;
        for (int i = ops.ops.length; i-- > 0; ) {
            val = ops.ops[i].op(val);
        }
    }

    @Benchmark
    public void incDecAddSwitch() {
        ops(this.incDecAddOps);
    }

    @Benchmark
    @OperationsPerInvocation(3)
    public void incDecNulSwitchTrio() {
        ops(this.incDecAddOps);
        ops(this.decAddIncOps);
        ops(this.addIncDecOps);
    }

//    @Benchmark
//    @Group("g")
//    public int incDecAddGroup() {
//        return ops(this.incDecAddOps);
//    }

//    @Benchmark
//    @Group("g")
//    public int decAddIncGroup() {
//        return ops(this.decAddIncOps);
//    }

//    @Benchmark
//    @Group("g")
//    public int addIncDecGroup() {
//        return ops(this.addIncDecOps);
//    }

    void ops(Ops ops) {
        switch (ops.ops.length) {
        case 3:
            val = ops.ops[2].op(val);
            val = ops.ops[1].op(val);
            val = ops.ops[0].op(val);
            break;

        default:
            for (int i = ops.ops.length; i-- > 0; ) {
                val = ops.ops[i].op(val);
            }
        }
    }
}

