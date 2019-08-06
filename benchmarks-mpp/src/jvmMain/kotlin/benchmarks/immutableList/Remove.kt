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

package benchmarks.immutableList

import benchmarks.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.openjdk.jmh.annotations.*

@State(Scope.Thread)
open class Remove {
    @Param(BM_1, BM_10, BM_100, BM_1000, BM_10000, BM_100000, BM_1000000, BM_10000000)
    var size: Int = 0

    private var persistentList: PersistentList<String> = persistentListOf()

    @Setup(Level.Trial)
    fun prepare() {
        persistentList = persistentListAdd(size)
    }

    @Benchmark
    fun removeLast(): ImmutableList<String> {
        var list = persistentList
        repeat(times = size) {
            list = list.removeAt(list.size - 1)
        }
        return list
    }

    /**
     * Removes one element from the beginning.
     *
     * Measures (time and memory spent on `removeAt` operation) / size.
     *
     * Expected time: nearly constant.
     * Expected memory: nearly constant.
     */
    @Benchmark
    fun removeFirst(): ImmutableList<String> {
        val list = persistentList
        return list.removeAt(0)
    }

    /**
     * Removes one element from the middle.
     *
     * Measures (time and memory spent on `removeAt` operation) / size.
     *
     * Expected time: nearly constant.
     * Expected memory: nearly constant.
     */
    @Benchmark
    fun removeMiddle(): ImmutableList<String> {
        val list = persistentList
        return list.removeAt(size / 2)
    }
}