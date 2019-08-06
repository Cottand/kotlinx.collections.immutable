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

package kotlinx.collections.immutable.implementations.immutableList

internal abstract class AbstractListIterator<out E>(var index: Int, var size: Int) : ListIterator<E> {
    override fun hasNext(): Boolean {
        return index < size
    }

    override fun hasPrevious(): Boolean {
        return index > 0
    }

    override fun nextIndex(): Int {
        return index
    }

    override fun previousIndex(): Int {
        return index - 1
    }

    internal fun checkHasNext() {
        if (!hasNext())
            throw NoSuchElementException()
    }

    internal fun checkHasPrevious() {
        if (!hasPrevious())
            throw NoSuchElementException()
    }
}


internal class SingleElementListIterator<E>(private val element: E, index: Int): AbstractListIterator<E>(index, 1) {
    override fun next(): E {
        checkHasNext()
        index++
        return element
    }

    override fun previous(): E {
        checkHasPrevious()
        index--
        return element
    }
}