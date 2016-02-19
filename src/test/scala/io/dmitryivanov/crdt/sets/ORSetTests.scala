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

package io.dmitryivanov.crdt.sets

import ORSet.ElementState
import org.specs2.mutable.Specification

class ORSetTests extends Specification {
  "ORSet" should {
    "compute lookup based on all added values" in {
      val orSet = new ORSet[String]()

      val result = orSet.add(ElementState("#a", "ape")).add(ElementState("#b", "dog")).add(ElementState("#c", "cat")).lookup

      result.size must beEqualTo(3)

      result must contain("ape", "dog", "cat")
    }

    "compute lookup based on all added and removed values" in {
      val orSet = new ORSet[String]()

      val result = orSet.add(ElementState[String]("#a", "ape"))
        .add(ElementState("#b", "dog")).remove(ElementState("#a", "ape"))
        .remove(ElementState("#b", "dog")).add(ElementState("#e", "cat")).lookup

      result.size must beEqualTo(1)

      result must contain("cat")
    }

    "merge with another ORSet correctly" in {
      val firstOrSet = new ORSet[String]()
        .add(ElementState("#a", "cat")).add(ElementState("#b", "dog")).remove(ElementState("#a", "cat"))

      val secondOrSet = new ORSet[String]()
        .add(ElementState("#c", "cat")).add(ElementState("#d", "ape")).remove(ElementState("#a", "cat"))

      val result = firstOrSet merge secondOrSet

      result.lookup.size must beEqualTo(3)

      result.lookup must contain("dog", "cat", "ape")
    }

    "compute a diff against another LWWSet correctly" in {
      val firstOrSet = new ORSet[String]()
        .add(ElementState("#a", "cat")).add(ElementState("#b", "dog")).remove(ElementState("#a", "cat"))

      val secondOrSet = new ORSet[String]()
        .add(ElementState("#c", "cat")).add(ElementState("#d", "ape")).remove(ElementState("#a", "cat"))

      val result = firstOrSet diff secondOrSet

      result.lookup.size must beEqualTo(2)

      result.lookup must contain("dog", "cat")
    }
  }
}
