/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Dmitry Ivanov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.dmitryivanov.crdt

import org.specs2.mutable._

import LWWSet.ElementState

class LWWSetTests extends Specification {
  "LWWSet" should {
    "compute lookup based on all added values" in {
      val lwwSet = new LWWSet[String]()

      val result = lwwSet.add(ElementState(1, "ape")).add(ElementState(1, "dog")).add(ElementState(1, "cat")).lookup

      result.size must beEqualTo(3)

      result must contain("ape", "dog", "cat")
    }

    "compute lookup based on all added and removed values" in {
      val lwwSet = new LWWSet[String]()

      val result = lwwSet.add(ElementState[String](2, "ape"))
        .add(ElementState(1, "dog")).remove(ElementState(1, "ape"))
        .remove(ElementState(2, "dog")).add(ElementState(1, "cat")).lookup

      result.size must beEqualTo(2)

      result must contain("cat", "ape")
    }

    "merge with another LWWSet correctly" in {
      val firstLwwSet = new LWWSet[String]()
        .add(ElementState(1, "cat")).add(ElementState(1, "dog")).remove(ElementState(3, "cat"))

      val secondLwwSet = new LWWSet[String]()
        .add(ElementState(5, "cat")).add(ElementState(1, "ape")).remove(ElementState(1, "cat"))

      val result = firstLwwSet merge secondLwwSet

      result.lookup.size must beEqualTo(3)

      result.lookup must contain("dog", "cat", "ape")
    }

    "compute a diff against another LWWSet correctly" in {
      val firstLwwSet = new LWWSet[String]()
        .add(ElementState(1, "cat")).add(ElementState(1, "dog")).remove(ElementState(3, "cat"))

      val secondLwwSet = new LWWSet[String]()
        .add(ElementState(5, "cat")).add(ElementState(1, "ape")).remove(ElementState(1, "cat"))

      val result = firstLwwSet diff secondLwwSet

      result.lookup.size must beEqualTo(1)

      result.lookup must contain("dog")
    }
  }
}
