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

package benchmarks

class ObjectWrapper<K: Comparable<K>>(val obj: K, val hashCode: Int) : Comparable<ObjectWrapper<K>> {
    override fun hashCode(): Int {
        return hashCode
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ObjectWrapper<*>) {
            return false
        }
        assert(obj != other.obj || hashCode == other.hashCode)  // if elements are equal hashCodes must be equal
        return obj == other.obj
    }

    override fun compareTo(other: ObjectWrapper<K>): Int {
        return obj.compareTo(other.obj)
    }
}


typealias IntWrapper = ObjectWrapper<Int>