/*
 * Copyright 2016-2017 JetBrains s.r.o.
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

package kotlinx.collections.immutable

/**
 * A generic immutable unordered collection of elements that does not support duplicate elements.
 * Methods in this interface support only read-only access to the immutable set.
 *
 * Modification operations are supported through the [PersistentSet] interface.
 *
 * Implementors of this interface take responsibility to be immutable.
 * Once constructed they must contain the same elements in the same order.
 *
 * @param E the type of elements contained in the set. The set is covariant on its element type.
 */
public interface ImmutableSet<out E>: Set<E>, ImmutableCollection<E>

/**
 * A generic persistent unordered collection of elements that does not support duplicate elements, and supports
 * adding and removing elements.
 *
 * Modification operations return new instances of the persistent set with the modification applied.
 *
 * @param E the type of elements contained in the set. The persistent set is covariant on its element type.
 */
public interface PersistentSet<out E> : ImmutableSet<E>, PersistentCollection<E> {
    /**
     * Returns the result of adding the specified [element] to this set.
     *
     * @return a new persistent set with the specified [element] added;
     * or this instance if it already contains the element.
     */
    override fun add(element: @UnsafeVariance E): PersistentSet<E>

    /**
     * Returns the result of adding all elements of the specified [elements] collection to this set.
     *
     * @return a new persistent set with elements of the specified [elements] collection added;
     * or this instance if it already contains every element of the specified collection.
     */
    override fun addAll(elements: Collection<@UnsafeVariance E>): PersistentSet<E>

    /**
     * Returns the result of removing the specified [element] from this set.
     *
     * @return a new persistent set with the specified [element] removed;
     * or this instance if there is no such element in this set.
     */
    override fun remove(element: @UnsafeVariance E): PersistentSet<E>

    /**
     * Returns the result of removing all elements in this set that are also
     * contained in the specified [elements] collection.
     *
     * @return a new persistent set with elements in this set that are also
     * contained in the specified [elements] collection removed;
     * or this instance if no modifications were made in the result of this operation.
     */
    override fun removeAll(elements: Collection<@UnsafeVariance E>): PersistentSet<E>

    /**
     * Returns the result of removing all elements in this set that match the specified [predicate].
     *
     * @return a new persistent set with elements matching the specified [predicate] removed;
     * or this instance if no elements match the predicate.
     */
    override fun removeAll(predicate: (E) -> Boolean): PersistentSet<E>

    /**
     * Returns an empty persistent set.
     */
    override fun clear(): PersistentSet<E>

    /**
     * A generic builder of the persistent set. Builder exposes its modification operations through the [MutableSet] interface.
     *
     * Builders are reusable, that is [build] method can be called multiple times with modifications between these calls.
     * However, modifications applied do not affect previously built persistent set instances.
     *
     * Builder is backed by the same underlying data structure as the persistent set it was created from.
     * Thus, [builder] and [build] methods take constant time consisting of passing the backing storage to the
     * new builder and persistent set instances, respectively.
     *
     * The builder tracks which nodes in the structure are shared with the persistent set,
     * and which are owned by it exclusively. It owns the nodes it copied during modification
     * operations and avoids copying them on subsequent modifications.
     *
     * When [build] is called the builder forgets about all owned nodes it had created.
     */
    interface Builder<E>: MutableSet<E>, PersistentCollection.Builder<E> {
        override fun build(): PersistentSet<E>
    }

    override fun builder(): Builder<@UnsafeVariance E>
}