/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.bark.web

import org.apache.jackrabbit.oak.bark.web.login.Login
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.{ BookmarkablePageLink, StatelessLink }
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.request.mapper.parameter.PageParameters

abstract class BaseTemplatePage(pp: PageParameters) extends BasePage(pp) {

  def this() = this(null);

  add(new Label("name", new PropertyModel(this, "oakSession.authInfo.userID")));

  add(new StatelessLink("logout") {
    override def onClick() = {
      getA.logout();
      setResponsePage(getApplication().getHomePage());
    }
  }.setVisibilityAllowed(!getA.isRO));

  add(new BookmarkablePageLink("login", classOf[Login]).setVisibilityAllowed(getA.isRO));
  add(new WebMarkupContainer("dirty").setVisibilityAllowed(oakRoot.isDefined && oakRoot.get.hasPendingChanges()));

  add(new StatelessLink("commit") {
    override def onClick() =
      oakRoot match {
        case Some(r) ⇒ { r.commit(); setResponsePage(getApplication().getHomePage()); }
        case _ ⇒ ;
      }
  });

  add(new StatelessLink("rebase") {
    override def onClick() =
      oakRoot match {
        case Some(r) ⇒ { r.rebase(); setResponsePage(getApplication().getHomePage()); }
        case _ ⇒ ;
      }
  });

  add(new StatelessLink("refresh") {
    override def onClick() =
      oakRoot match {
        case Some(r) ⇒ { r.refresh(); setResponsePage(getApplication().getHomePage()); }
        case _ ⇒ ;
      }
  });
}