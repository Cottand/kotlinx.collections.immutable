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

package benchmarks.immutableMap

import benchmarks.*
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
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

    private var keys = listOf<IntWrapper>()
    private var persistentMap = persistentMapOf<IntWrapper, String>()
    private var halfHeightPersistentMap = persistentMapOf<IntWrapper, String>()

    @Setup(Level.Trial)
    fun prepare() {
        keys = generateKeys(hashCodeType, size)
        persistentMap = persistentMapPut(implementation, keys)
        halfHeightPersistentMap = halfHeightPersistentMap(persistentMap, keys)

        if (hashCodeType == NON_EXISTING_HASH_CODE)
            keys = generateKeys(hashCodeType, size)
    }

    @Benchmark
    fun remove(): PersistentMap<IntWrapper, String> {
        var map = persistentMap
        repeat(times = size) { index ->
            map = map.remove(keys[index])
        }
        return map
    }

    /**
     * Puts `size - entriesForHalfHeight(size)` new entries to a persistent map of size `entriesForHalfHeight(size)`
     * that had initially [size] entries.
     *
     * Measures mean time and memory spent per (roughly one) `put` operation.
     *
     * Expected time: [Put.put]
     * Expected memory: [Put.put]
     */
    @Benchmark
    fun putAfterRemove(): PersistentMap<IntWrapper, String> {
        var map = halfHeightPersistentMap

        repeat(size - halfHeightPersistentMap.size) { index ->
            map = map.put(keys[index], "some element")
        }

        return map
    }

    /**
     * Iterates keys of a persistent map of size `entriesForHalfHeight(size)` several times until iterating [size] elements.
     *
     * Measures mean time and memory spent per `iterate` operation.
     *
     * Expected time: [Iterate.iterateKeys] with [Iterate.size] = `entriesForHalfHeight([size])`
     * Expected memory: [Iterate.iterateKeys] with [Iterate.size] = `entriesForHalfHeight([size])`
     */
    @Benchmark
    fun iterateKeysAfterRemove(bh: Blackhole) {
        var count = 0
        while (count < size) {
            for (e in halfHeightPersistentMap) {
                bh.consume(e)

                if (++count == size)
                    break
            }
        }
    }
}