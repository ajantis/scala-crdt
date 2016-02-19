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

/**
  * Grow-Only set. Immutable Set that allows only addition operations.
  * GSet is only a thin wrapper around normal Set forbidding remove operation.
  *
  * As Set union operation is commutative and convergent it's safe to use it here as long as it only grows.
  *
  * @param set an underlying set structure that carries elements
  *
  * @tparam E a type of the elements stored in this Set.
  */
class GSet[E](set: Set[E] = Set[E]()) extends CrdtSet[E, GSet[E]] {

  /**
    * Adds an element to the GSet.
    *
    * @param e an element to add
    * @return an updated GSet collection.
    */
  def add(e: E): GSet[E] = new GSet[E](set + e)

  /**
    * Merges current GSet data with another GSet. Produces a merge result in the form of a new GSet.
    *
    * @param other a GSet to merge current set with.
    *
    * @return a new GSet containing a result of merge operation between two GSets.
    */
  def merge(other: GSet[E]): GSet[E] = new GSet[E](other.lookup ++ lookup)

  /**
    *
    * @param other a GSet to calculate the difference against.
    *
    * @return a new GSet containing a result of diff operation on two GSets.
    */
  def diff(other: GSet[E]): GSet[E] = new GSet[E](set.diff(other.lookup))

  /**
    * @return a total value: a set of all elements stored in GSet.
    */
  def lookup: Set[E] = set
}
