/*!
 * Copyright 2013-2014 Dennis Hörsch.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dennishoersch.dropwizard.blog.service

import de.dennishoersch.dropwizard.blog.domain.{PostByDate, Account, Author, Post}
import de.dennishoersch.util.hashing._
import de.dennishoersch.util.time._
import de.dennishoersch.dropwizard.blog.domain.Category._
import java.util.concurrent.atomic.AtomicLong
import scala.collection.mutable.ArrayBuffer

class DB {

  private val currentId = new AtomicLong
  def nextId() = currentId.incrementAndGet()

  private val authors = List(
    Author(nextId(), "Dennse Hi", "/images/avatar/dennse.png"),
    Author(nextId(), "Manni Mempfer", "/images/avatar/manni.png"))

  private val accounts = List(
    Account(nextId(), "dennis", "secret".asSHA256, authors(0)),
    Account(nextId(), "manni", "secret".asSHA256, authors(1)))

  private val posts = ArrayBuffer(

    Post(nextId(), authors(0), "01.07.2014 14:55".asDateTime, "Introducing Me", """
    		   |<p>
               |  Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. 
    		   |</p>    
       """.stripMargin, List(CSS, Pure)),

    Post(nextId(), authors(1), "03.07.2014 10:50".asDateTime, "Everything You Need", """
    		   |<p>
               |  Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
    		   |</p>    
       """.stripMargin, List(Javascript)),

    Post(nextId(), authors(1), "04.07.2014 10:50".asDateTime, "Photos from winter season", """
    		   |<div class="post-images pure-g">
               |    <div class="pure-u-1 pure-u-md-1-2">
               |        <a href="http://www.flickr.com/photos/uberlife/8915936174/">
               |            <img alt="Pool"
               |                 class="pure-img-responsive"
               |                 src="http://farm8.staticflickr.com/7448/8915936174_8d54ec76c6.jpg"/>
               |        </a>

               |        <div class="post-image-meta">
               |            <h3>Test1</h3>
               |        </div>
               |    </div>

               |    <div class="pure-u-1 pure-u-md-1-2">
               |        <a href="http://www.flickr.com/photos/uberlife/8907351301/">
               |            <img alt="Beach"
               |                 class="pure-img-responsive"
               |                 src="http://farm8.staticflickr.com/7382/8907351301_bd7460cffb.jpg"/>
               |        </a>

               |        <div class="post-image-meta">
               |            <h3>Other</h3>
               |        </div>
               |    </div>
               |</div>    
       """.stripMargin, List(Uncategorized)),

    Post(nextId(), authors(0), "01.07.2014 08:51".asDateTime, "Just with me and my crowd", """
    		   |<p>
               |  Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
    		   |</p>    
       """.stripMargin, List(Javascript)))

  def allAccounts(): List[Account] = accounts

  def allPosts(): List[Post] = posts.toList.sorted(PostByDate.reverse)

  def savePost(post: Post) = {
    posts += post
    post
  }
}