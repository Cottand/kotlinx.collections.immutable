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

package kotlinx.collections.immutable.implementations.immutableSet

internal class PersistentHashSetMutableIterator<E>(private val builder: PersistentHashSetBuilder<E>)
    : PersistentHashSetIterator<E>(builder.node), MutableIterator<E> {
    private var lastIteratedElement: E? = null
    private var nextWasInvoked = false
    private var expectedModCount = builder.modCount

    override fun next(): E {
        checkForComodification()
        val next = super.next()
        lastIteratedElement = next
        nextWasInvoked = true
        return next
    }

    override fun remove() {
        checkNextWasInvoked()
        if (hasNext()) {
            val currentElement = currentElement()

            assert(builder.remove(lastIteratedElement))
            resetPath(currentElement.hashCode(), builder.node, currentElement, 0)
        } else {
            assert(builder.remove(lastIteratedElement))
        }

        lastIteratedElement = null
        nextWasInvoked = false
        expectedModCount = builder.modCount
    }

    private fun resetPath(hashCode: Int, node: TrieNode<*>, element: E, pathIndex: Int) {
        if (node.isCollision()) {
            val index = node.buffer.indexOf(element)
            assert(index != -1)
            path[pathIndex].reset(node.buffer, index)
            pathLastIndex = pathIndex
            return
        }

        val position = 1 shl ((hashCode shr (pathIndex * LOG_MAX_BRANCHING_FACTOR)) and MAX_BRANCHING_FACTOR_MINUS_ONE)
        val index = Integer.bitCount(node.bitmap and (position - 1))

        path[pathIndex].reset(node.buffer, index)

        val cell = node.buffer[index]
        if (cell is TrieNode<*>) {
            resetPath(hashCode, cell, element, pathIndex + 1)
        } else {
//            assert(cell == element)
            pathLastIndex = pathIndex
        }
    }

    private fun checkNextWasInvoked() {
        if (!nextWasInvoked)
            throw IllegalStateException()
    }

    private fun checkForComodification() {
        if (builder.modCount != expectedModCount)
            throw ConcurrentModificationException()
    }
}