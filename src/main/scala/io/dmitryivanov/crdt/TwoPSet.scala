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

class TwoPSet[E](protected val addSet: GSet[E] = new GSet(),
                 protected val removeSet: GSet[E] = new GSet()) {

  def add(element: E): TwoPSet[E] = new TwoPSet[E](addSet.add(element), removeSet)

  def remove(element: E): TwoPSet[E] = new TwoPSet[E](addSet, removeSet.add(element))

  def merge(anotherSet: TwoPSet[E]): TwoPSet[E] =
    new TwoPSet[E](addSet.merge(anotherSet.addSet), removeSet.merge(anotherSet.removeSet))

  def diff(anotherSet: TwoPSet[E]): TwoPSet[E] =
    new TwoPSet[E](addSet.diff(anotherSet.addSet), removeSet.diff(anotherSet.removeSet))

  def lookup: Set[E] = addSet.diff(removeSet).lookup
}
