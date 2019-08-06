/*
 * Copyright 2016-2019 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package benchmarks.immutableList.builder

import benchmarks.*
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.openjdk.jmh.annotations.*

@State(Scope.Thread)
open class AddAll {
    @Param(BM_1, BM_10, BM_100, BM_1000, BM_10000, BM_100000, BM_1000000, BM_10000000)
    var size: Int = 0

    @Param(IP_100, IP_99_09, IP_95, IP_70, IP_50, IP_30, IP_0)
    var immutablePercentage: Double = 0.0

    private var listToAdd = emptyList<String>()

    @Setup
    fun prepare() {
        listToAdd = List(size) { "another element" }
    }

    // Results of the following benchmarks do not indicate memory or time spent per operation,
    // however regressions there do indicate changes.
    //
    // The benchmarks measure mean time and memory spent per added element.
    //
    // Expected time: nearly constant.
    // Expected memory: nearly constant.

    /**
     * Adds [size] elements to an empty persistent list builder using `addAll` operation.
     */
    @Benchmark
    fun addAllLast(): PersistentList.Builder<String> {
        val builder = persistentListOf<String>().builder()
        builder.addAll(listToAdd)
        return builder
    }

    /**
     * Adds `size / 2` elements to an empty persistent list builder
     * and then adds `size - size / 2` elements using `addAll` operation.
     */
    @Benchmark
    fun addAllLast_Half(): PersistentList.Builder<String> {
        val initialSize = size / 2
        val subListToAdd = listToAdd.subList(0, size - initialSize) // assuming subList creation is neglectable

        val builder = persistentListBuilderAdd(initialSize, immutablePercentage)
        builder.addAll(subListToAdd)
        return builder
    }

    /**
     * Adds `size - size / 3` elements to an empty persistent list builder
     * and then adds `size / 3` elements using `addAll` operation.
     */
    @Benchmark
    fun addAllLast_OneThird(): PersistentList.Builder<String> {
        val initialSize = size - size / 3
        val subListToAdd = listToAdd.subList(0, size - initialSize)

        val builder = persistentListBuilderAdd(initialSize, immutablePercentage)
        builder.addAll(subListToAdd)
        return builder
    }

    /**
     * Adds `size / 2` elements to an empty persistent list builder
     * and then inserts `size - size / 2` elements at the beginning using `addAll` operation.
     */
    @Benchmark
    fun addAllFirst_Half(): PersistentList.Builder<String> {
        val initialSize = size / 2
        val subListToAdd = listToAdd.subList(0, size - initialSize)

        val builder = persistentListBuilderAdd(initialSize, immutablePercentage)
        builder.addAll(0, subListToAdd)
        return builder
    }

    /**
     * Adds `size - size / 3` elements to an empty persistent list builder
     * and then inserts `size / 3` elements at the beginning using `addAll` operation.
     */
    @Benchmark
    fun addAllFirst_OneThird(): PersistentList.Builder<String> {
        val initialSize = size - size / 3
        val subListToAdd = listToAdd.subList(0, size - initialSize)

        val builder = persistentListBuilderAdd(initialSize, immutablePercentage)
        builder.addAll(0, subListToAdd)
        return builder
    }

    /**
     * Adds `size / 2` elements to an empty persistent list builder
     * and then inserts `size - size / 2` elements at the middle using `addAll` operation.
     */
    @Benchmark
    fun addAllMiddle_Half(): PersistentList.Builder<String> {
        val initialSize = size / 2
        val index = initialSize / 2
        val subListToAdd = listToAdd.subList(0, size - initialSize)

        val builder = persistentListBuilderAdd(initialSize, immutablePercentage)
        builder.addAll(index, subListToAdd)
        return builder
    }

    /**
     * Adds `size - size / 3` elements to an empty persistent list builder
     * and then inserts `size / 3` elements at the middle using `addAll` operation.
     */
    @Benchmark
    fun addAllMiddle_OneThird(): PersistentList.Builder<String> {
        val initialSize = size - size / 3
        val index = initialSize / 2
        val subListToAdd = listToAdd.subList(0, size - initialSize)

        val builder = persistentListBuilderAdd(initialSize, immutablePercentage)
        builder.addAll(index, subListToAdd)
        return builder
    }
}