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

package benchmarks.immutableSet

import benchmarks.*
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Thread)
open class Remove {
    @Param(BM_1, BM_10, BM_100, BM_1000, BM_10000, BM_100000, BM_1000000)
    var size: Int = 0

    @Param(HASH_IMPL, ORDERED_IMPL)
    var implementation = ""

    @Param(ASCENDING_HASH_CODE, RANDOM_HASH_CODE, COLLISION_HASH_CODE, NON_EXISTING_HASH_CODE)
    var hashCodeType = ""

    private var elements = listOf<IntWrapper>()
    private var persistentSet = persistentSetOf<IntWrapper>()
    private var halfHeightPersistentSet = persistentSetOf<IntWrapper>()

    @Setup(Level.Trial)
    fun prepare() {
        elements = generateElements(hashCodeType, size)
        persistentSet = persistentSetAdd(implementation, elements)
        halfHeightPersistentSet = halfHeightPersistentSet(persistentSet, elements)

        if (hashCodeType == NON_EXISTING_HASH_CODE)
            elements = generateElements(hashCodeType, size)
    }

    @Benchmark
    fun remove(): ImmutableSet<IntWrapper> {
        var set = persistentSet
        repeat(times = size) { index ->
            set = set.remove(elements[index])
        }
        return set
    }

    /**
     * Adds `size - elementsForHalfHeight(size)` new elements to a persistent set of size `elementsForHalfHeight(size)`
     * that had initially [size] elements.
     *
     * Measures mean time and memory spent per (roughly one) `add` operation.
     *
     * Expected time: [Add.add]
     * Expected memory: [Add.add]
     */
    @Benchmark
    fun addAfterRemove(): ImmutableSet<IntWrapper> {
        var set = halfHeightPersistentSet

        repeat(size - halfHeightPersistentSet.size) { index ->
            set = set.add(elements[index])
        }

        return set
    }

    /**
     * Iterates elements of a persistent set of size `elementsForHalfHeight(size)` several times until iterating [size] elements.
     *
     * Measures mean time and memory spent per `iterate` operation.
     *
     * Expected time: [Iterate.iterate] with [Iterate.size] = `elementsForHalfHeight([size])`
     * Expected memory: [Iterate.iterate] with [Iterate.size] = `elementsForHalfHeight([size])`
     */
    @Benchmark
    fun iterateAfterRemove(bh: Blackhole) {
        var count = 0
        while (count < size) {
            for (e in halfHeightPersistentSet) {
                bh.consume(e)

                if (++count == size)
                    break
            }
        }
    }
}