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

import io.dmitryivanov.crdt.sets.OURSet.ElementState

class OURSet[E <: Ordered[E]](protected val elements: Set[ElementState[E]] = Set[ElementState[E]]()) {

  def add(state: ElementState[E]): OURSet[E] = {
    val update = new OURSet[E](Set(state.copy(removed = false)))
    merge(update)
  }

  def remove(state: ElementState[E]): OURSet[E] = {
    val update = new OURSet[E](Set(state.copy(removed = true)))
    merge(update)
  }

  def merge(anotherSet: OURSet[E]): OURSet[E] = {
    val newSet = (elements ++ anotherSet.elements).groupBy(_.id).map(_._2.max).toSet
    new OURSet[E](newSet)
  }

  def diff(anotherSet: OURSet[E]): OURSet[E] = {
    val mergeResult = merge(anotherSet)
    new OURSet[E](mergeResult.elements.diff(anotherSet.elements))
  }

  def lookup: Set[E] = elements.filterNot(_.removed).map(_.value)
}

object OURSet {

  case class ElementState[E <: Ordered[E]](id: UUID, timestamp: Long, value: E, removed: Boolean = false) extends Ordered[ElementState[E]] {

    type TupleType = (String, Long, Boolean, E)

    override def compare(that: ElementState[E]): Int = {
      import scala.math.Ordering._

      implicit val valueOrdering: Ordering[E] = Ordering.fromLessThan[E]((e1, e2) => e1.compare(e2) > 0)

      implicit val ordering = implicitly[Ordering[TupleType]]

      ordering.compare(getFieldTuple, that.getFieldTuple)
    }

    private def getFieldTuple: TupleType =
      (
        id.toString,
        timestamp,
        !removed, // Add bias
        value
      )
  }
}
