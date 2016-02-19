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

class GSetTests extends Specification {

  "GSet" should {
    "compute lookup based on all added values" in {
      val gSet = new GSet[String]()

      val result = gSet.add("ape").add("dog").add("cat").lookup

      result.size must beEqualTo(3)

      result must contain("ape", "dog", "cat")
    }

    "merge with another GSet correctly" in {
      val firstGSet = new GSet[String]().add("ape").add("dog").add("tiger")
      val secondGSet = new GSet[String]().add("cat")

      val result = firstGSet merge secondGSet

      result.lookup.size must beEqualTo(4)

      result.lookup must contain("ape", "dog", "cat", "tiger")
    }
  }
}
