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

package com.kentside.perf.buffer;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import sun.misc.Unsafe;

@State(Scope.Benchmark)
public class Buffer {
    static final Unsafe UNSAFE;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot access Unsafe", e);
        }
    }

    static final int SIZE = 8;

    static final int ARG_OFF_0 = 1;
    static final int ARG_OFF_1 = 3;
    static final int RES_OFF_0 = 6;

    static final int BYTE_SIZE = SIZE << 2;

    static final int ARG_BYTE_0 = ARG_OFF_0 << 2;
    static final int ARG_BYTE_1 = ARG_OFF_1 << 2;
    static final int RES_BYTE_0 = RES_OFF_0 << 2;

    class Custom {
        public int int0;
        public int int1;
        public int int2;
        public int int3;
        public int int4;
        public int int5;
        public int int6;
        public int int7;
    }

    Custom custom;
    int[] array;
    ByteBuffer unsafeBuffer;
    long unsafeAddr;
    ByteBuffer bigBuffer;
    ByteBuffer littleBuffer;
    IntBuffer bigIntBuffer;
    IntBuffer littleIntBuffer;
    ByteBuffer directBigBuffer;
    ByteBuffer directLittleBuffer;
    IntBuffer directBigIntBuffer;
    IntBuffer directLittleIntBuffer;

    @Setup
    public void setup()
    {
        custom = new Custom();
        array = new int[SIZE];
        unsafeBuffer = ByteBuffer.allocateDirect(BYTE_SIZE);
        bigBuffer = ByteBuffer.allocate(BYTE_SIZE);
        littleBuffer = ByteBuffer.allocate(BYTE_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        bigIntBuffer = ByteBuffer.allocate(BYTE_SIZE).asIntBuffer();
        littleIntBuffer = ByteBuffer.allocate(BYTE_SIZE).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        directBigBuffer = ByteBuffer.allocateDirect(BYTE_SIZE);
        directLittleBuffer = ByteBuffer.allocateDirect(BYTE_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        directBigIntBuffer = ByteBuffer.allocateDirect(BYTE_SIZE).asIntBuffer();
        directLittleIntBuffer = ByteBuffer.allocateDirect(BYTE_SIZE).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();

        try {
            Field f = java.nio.Buffer.class.getDeclaredField("address");
            f.setAccessible(true);
            unsafeAddr = ((Long) f.get(unsafeBuffer)).longValue();
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot access Unsafe", e);
        }
    }

    @Benchmark
    public void doCustom()
    {
        custom.int6 = custom.int1 + custom.int3;
    }

    @Benchmark
    public void doArray()
    {
        array[RES_OFF_0] = array[ARG_OFF_0] + array[ARG_OFF_1];
    }

    @Benchmark
    public void doUnsafe()
    {
        UNSAFE.putInt(null, unsafeAddr + RES_BYTE_0, UNSAFE.getInt(null, unsafeAddr + ARG_BYTE_0) + UNSAFE.getInt(null, unsafeAddr + ARG_BYTE_1));
    }

    @Benchmark
    public void doBigBuffer()
    {
        bigBuffer.putInt(RES_BYTE_0, bigBuffer.getInt(ARG_BYTE_0) + bigBuffer.getInt(ARG_BYTE_1));
    }

    @Benchmark
    public void doLittleBuffer()
    {
        littleBuffer.putInt(RES_BYTE_0, littleBuffer.getInt(ARG_BYTE_0) + littleBuffer.getInt(ARG_BYTE_1));
    }

    @Benchmark
    public void doBigIntBuffer()
    {
        bigIntBuffer.put(RES_OFF_0, bigIntBuffer.get(ARG_OFF_0) + bigIntBuffer.get(ARG_OFF_1));
    }

    @Benchmark
    public void doLittleIntBuffer()
    {
        littleIntBuffer.put(RES_OFF_0, littleIntBuffer.get(ARG_OFF_0) + littleIntBuffer.get(ARG_OFF_1));
    }

    @Benchmark
    public void doDirectBigBuffer()
    {
        directBigBuffer.putInt(RES_BYTE_0, directBigBuffer.getInt(ARG_BYTE_0) + directBigBuffer.getInt(ARG_BYTE_1));
    }

    @Benchmark
    public void doDirectLittleBuffer()
    {
        directLittleBuffer.putInt(RES_BYTE_0, directLittleBuffer.getInt(ARG_BYTE_0) + directLittleBuffer.getInt(ARG_BYTE_1));
    }

    @Benchmark
    public void doDirectBigIntBuffer()
    {
        directBigIntBuffer.put(RES_OFF_0, directBigIntBuffer.get(ARG_OFF_0) + directBigIntBuffer.get(ARG_OFF_1));
    }

    @Benchmark
    public void doDirectLittleIntBuffer()
    {
        directLittleIntBuffer.put(RES_OFF_0, directLittleIntBuffer.get(ARG_OFF_0) + directLittleIntBuffer.get(ARG_OFF_1));
    }
}

