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

import java.util.UUID

import OURSet.ElementState
import org.specs2.mutable.Specification

class OURSetTests extends Specification {

  case class StringValue(s: String) extends Ordered[StringValue] {
    override def compare(that: StringValue): Int = s.compareTo(that.s)
  }

  object StringValue {
    implicit def asStringValue(str: String): StringValue = StringValue(str)
  }

  "OURSet" should {
    import StringValue._

    "compute lookup based on all added values" in {
      val ourSet = new OURSet[StringValue]()

      val state1 = ElementState[StringValue](UUID.randomUUID(), 1, "cat")
      val state2 = ElementState[StringValue](UUID.randomUUID(), 1, "dog")
      val state3 = ElementState[StringValue](UUID.randomUUID(), 1, "ape")

      val result = ourSet.add(state1).add(state1).add(state2).add(state3).lookup

      result.size must beEqualTo(3)

      result.map(_.s) must contain("ape", "dog", "cat")
    }

    "compute lookup based on all added and removed values" in {
      val ourSet = new OURSet[StringValue]()

      val state1 = ElementState[StringValue](UUID.randomUUID(), 1, "cat")
      val state2 = ElementState[StringValue](UUID.randomUUID(), 1, "dog")
      val state3 = ElementState[StringValue](UUID.randomUUID(), 1, "ape")

      val result = ourSet.add(state1).add(state1).add(state2).add(state3)
        .remove(state2.copy(removed = true, timestamp = state2.timestamp + 1)).lookup

      result.size must beEqualTo(2)

      result.map(_.s) must contain("cat", "ape")
    }

    "merge with another ORSet correctly" in {
      val catRemoved = ElementState[StringValue](UUID.randomUUID(), 1, "cat")
      val catRenamed = catRemoved.copy(timestamp = catRemoved.timestamp + 1)

      val dog = ElementState[StringValue](UUID.randomUUID(), 1, "dog")
      val dogRemoved = dog.copy(removed = true, timestamp = dog.timestamp + 1)
      val ape = ElementState[StringValue](UUID.randomUUID(), 1, "ape")

      val firstOurSet = new OURSet[StringValue]().remove(catRemoved).remove(dogRemoved)
      val secondOurSet = new OURSet[StringValue]().add(catRenamed).add(dog).add(ape)

      val result = firstOurSet merge secondOurSet

      result.lookup.size must beEqualTo(2)

      result.lookup.map(_.s) must contain("cat", "ape")
    }

    /*
//    "compute a diff against another LWWSet correctly" in {
//      val firstOrSet = new ORSet[String]()
//        .add(ElementState("#a", "cat")).add(ElementState("#b", "dog")).remove(ElementState("#a", "cat"))
//
//      val secondOrSet = new ORSet[String]()
//        .add(ElementState("#c", "cat")).add(ElementState("#d", "ape")).remove(ElementState("#a", "cat"))
//
//      val result = firstOrSet diff secondOrSet
//
//      result.lookup.size must beEqualTo(2)
//
//      result.lookup must contain("tiger", "ape")
//    }
 */
  }
}
