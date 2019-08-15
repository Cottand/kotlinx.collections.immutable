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
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentMapOf
import kotlin.math.ceil
import kotlin.math.log


fun <K, V> emptyPersistentMap(implementation: String): PersistentMap<K, V> = when (implementation) {
    HASH_IMPL -> persistentHashMapOf()
    ORDERED_IMPL -> persistentMapOf()
    else -> throw AssertionError("Unknown PersistentMap implementation: $implementation")
}

fun <K> persistentMapPut(implementation: String, keys: List<K>): PersistentMap<K, String> {
    var map = emptyPersistentMap<K, String>(implementation)
    for (key in keys) {
        map = map.put(key, "some element")
    }
    return map
}

private fun entriesForHalfHeight(size: Int): Int {
    val branchingFactor = 32
    val logBranchingFactor = 5

    val approximateHeight = ceil(log(size.toDouble(), branchingFactor.toDouble())).toInt()
    return 1 shl ((approximateHeight / 2) * logBranchingFactor)
}


fun <K> halfHeightPersistentMap(persistentMap: PersistentMap<K, String>, keys: List<K>): PersistentMap<K, String> {
    val elementsToLeave = entriesForHalfHeight(persistentMap.size)

    var map = persistentMap
    repeat(persistentMap.size - elementsToLeave) { index ->
        map = map.remove(keys[index])
    }
    return map
}
