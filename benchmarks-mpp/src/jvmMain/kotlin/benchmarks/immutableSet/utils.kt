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
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentHashSetOf
import kotlinx.collections.immutable.persistentSetOf
import kotlin.math.ceil
import kotlin.math.log


fun <E> emptyPersistentSet(implementation: String): PersistentSet<E> = when (implementation) {
    HASH_IMPL -> persistentHashSetOf()
    ORDERED_IMPL -> persistentSetOf()
    else -> throw AssertionError("Unknown PersistentSet implementation: $implementation")
}

fun <E> persistentSetAdd(implementation: String, elements: List<E>): PersistentSet<E> {
    var set = emptyPersistentSet<E>(implementation)
    for (element in elements) {
        set = set.add(element)
    }
    return set
}

private fun elementsForHalfHeight(size: Int): Int {
    val branchingFactor = 32
    val logBranchingFactor = 5

    val approximateHeight = ceil(log(size.toDouble(), branchingFactor.toDouble())).toInt()
    return 1 shl ((approximateHeight / 2) * logBranchingFactor)
}

fun <E> halfHeightPersistentSet(persistentSet: PersistentSet<E>, elements: List<E>): PersistentSet<E> {
    val elementsToLeave = elementsForHalfHeight(persistentSet.size)

    var set = persistentSet
    repeat(persistentSet.size - elementsToLeave) { index ->
        set = set.remove(elements[index])
    }
    return set
}
