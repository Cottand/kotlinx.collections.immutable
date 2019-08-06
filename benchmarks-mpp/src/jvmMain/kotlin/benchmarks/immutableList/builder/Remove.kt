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
import org.openjdk.jmh.annotations.*

@State(Scope.Thread)
open class Remove {
    @Param(BM_1, BM_10, BM_100, BM_1000, BM_10000, BM_100000, BM_1000000, BM_10000000)
    var size: Int = 0

    @Param(IP_100, IP_99_09, IP_95, IP_70, IP_50, IP_30, IP_0)
    var immutablePercentage: Double = 0.0

    @Benchmark
    fun addAndRemoveLast(): PersistentList.Builder<String> {
        val builder = persistentListBuilderAdd(size, immutablePercentage)
        for (i in 0 until size) {
            builder.removeAt(builder.size - 1)
        }
        return builder
    }

    /**
     * Adds [size] elements to an empty persistent list builder
     * and then removes one element from the beginning.
     *
     * Measures (mean time and memory spent per `add` operation) + (time and memory spent on `removeAt` operation) / size.
     *
     * Expected time: [Add.addLast] + nearly constant.
     * Expected memory: [Add.addLast] + nearly constant.
     */
    @Benchmark
    fun addAndRemoveFirst(): String {
        val builder = persistentListBuilderAdd(size, immutablePercentage)
        return builder.removeAt(0)
    }

    /**
     * Adds [size] elements to an empty persistent list builder
     * and then removes one element from the middle.
     *
     * Measures (mean time and memory spent per `add` operation) + (time and memory spent on `removeAt` operation) / size.
     *
     * Expected time: [Add.addLast] + nearly constant.
     * Expected memory: [Add.addLast] + nearly constant.
     */
    @Benchmark
    fun addAndRemoveMiddle(): String {
        val builder = persistentListBuilderAdd(size, immutablePercentage)
        return builder.removeAt(size / 2)
    }
}