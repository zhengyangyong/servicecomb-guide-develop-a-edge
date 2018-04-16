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

package org.apache.servicecomb;

import java.util.Map;

import org.apache.servicecomb.edge.core.AbstractEdgeDispatcher;
import org.apache.servicecomb.edge.core.EdgeInvocation;
import org.apache.servicecomb.serviceregistry.definition.DefinitionConst;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class EdgeDispatcher extends AbstractEdgeDispatcher {
  @Override
  public int getOrder() {
    return 10000;
  }

  @Override
  public void init(Router router) {
    String regex = "/([^\\\\/]+)/(.*)";
    router.routeWithRegex(regex).failureHandler(this::onFailure).handler(this::onRequest);
  }

  protected void onRequest(RoutingContext context) {
    Map<String, String> pathParams = context.pathParams();
    final String service = pathParams.get("param0");
    String path = "/" + pathParams.get("param1");

    EdgeInvocation edgeInvocation = new EdgeInvocation();
    edgeInvocation.setVersionRule(DefinitionConst.VERSION_RULE_ALL);
    edgeInvocation.init(service, context, path, httpServerFilters);
    edgeInvocation.edgeInvoke();
  }
}
