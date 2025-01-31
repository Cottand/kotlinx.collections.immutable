/*
 * Copyright 2016-2018 JetBrains s.r.o.
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

package kotlinx.collections.immutable.implementations.persistentOrderedMap

import kotlinx.collections.immutable.implementations.immutableMap.MapEntry

internal open class PersistentOrderedMapLinksIterator<K, V>(
        internal var nextKey: Any?,
        private val hashMap: Map<K, LinkedValue<V>>
) : Iterator<LinkedValue<V>> {
    internal var index = 0

    override fun hasNext(): Boolean {
        return index < hashMap.size
    }

    override fun next(): LinkedValue<V> {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        val result = hashMap[nextKey]!!
        index++
        nextKey = result.next
        return result
    }

}

internal class PersistentOrderedMapEntriesIterator<out K, out V>(map: PersistentOrderedMap<K, V>) : Iterator<Map.Entry<K, V>> {
    private val internal = PersistentOrderedMapLinksIterator(map.firstKey, map.hashMap)

    override fun hasNext(): Boolean {
        return internal.hasNext()
    }

    override fun next(): Map.Entry<K, V> {
        @Suppress("UNCHECKED_CAST")
        val nextKey = internal.nextKey as K
        val nextValue = internal.next().value
        return MapEntry(nextKey, nextValue)
    }
}

internal class PersistentOrderedMapKeysIterator<out K, out V>(map: PersistentOrderedMap<K, V>) : Iterator<K> {
    private val internal = PersistentOrderedMapLinksIterator(map.firstKey, map.hashMap)

    override fun hasNext(): Boolean {
        return internal.hasNext()
    }

    override fun next(): K {
        @Suppress("UNCHECKED_CAST")
        val nextKey = internal.nextKey as K
        internal.next()
        return nextKey
    }
}

internal class PersistentOrderedMapValuesIterator<out K, out V>(map: PersistentOrderedMap<K, V>) : Iterator<V> {
    private val internal = PersistentOrderedMapLinksIterator(map.firstKey, map.hashMap)

    override fun hasNext(): Boolean {
        return internal.hasNext()
    }

    override fun next(): V {
        return internal.next().value
    }
}