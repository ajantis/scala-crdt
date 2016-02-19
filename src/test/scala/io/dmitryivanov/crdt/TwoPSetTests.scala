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

class TwoPSetTests extends Specification {
  "TwoPSet" should {
    "compute lookup based on all added values" in {
      val twoPSet = new TwoPSet[String]()

      val result = twoPSet.add("ape").add("dog").add("cat").lookup

      result.size must beEqualTo(3)

      result must contain("ape", "dog", "cat")
    }

    "compute lookup based on all added and removed values" in {
      val twoPSet = new TwoPSet[String]()

      val result = twoPSet.add("ape").add("dog").remove("ape").remove("dog").add("cat").lookup

      result.size must beEqualTo(1)

      result must contain("cat")
    }

    "merge with another TwoPSet correctly" in {
      val firstTwoPSet = new TwoPSet[String]().add("ape").add("dog").remove("ape").add("tiger")
      val secondTwoPSet = new TwoPSet[String]().add("cat")

      val result = firstTwoPSet merge secondTwoPSet

      result.lookup.size must beEqualTo(3)

      result.lookup must contain("dog", "cat", "tiger")
    }

    "compute a diff against another TwoPSet correctly" in {
      val firstTwoPSet = new TwoPSet[String]().add("ape").add("dog").remove("ape").add("tiger")
      val secondTwoPSet = new TwoPSet[String]().add("dog")

      val result = firstTwoPSet diff secondTwoPSet

      result.lookup.size must beEqualTo(1)

      result.lookup must contain("tiger")
    }
  }
}
