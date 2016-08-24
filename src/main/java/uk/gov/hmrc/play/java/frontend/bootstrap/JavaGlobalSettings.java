/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.play.java.frontend.bootstrap;

import play.api.Application;
import play.api.Configuration;
import play.api.GlobalSettings;
import play.api.GlobalSettings$class;
import play.api.mvc.EssentialAction;
import play.api.mvc.Handler;
import play.api.mvc.RequestHeader;
import play.api.mvc.Result;
import scala.Enumeration;
import scala.Function1;
import scala.Option;
import scala.Tuple2;
import scala.concurrent.Future;

import java.io.File;

public class JavaGlobalSettings implements GlobalSettings {
    public void onStart(play.Application app) {
        onStart(app.getWrappedApplication());
    }

    public void onStop(play.Application app) {
        onStop(app.getWrappedApplication());
    }

    @Override
    public void beforeStart(Application app) {
        GlobalSettings$class.beforeStart(this, app);
    }

    @Override
    public void onStart(Application app) {
        GlobalSettings$class.onStart(this, app);
    }

    @Override
    public void onStop(Application app) {
        GlobalSettings$class.onStop(this, app);
    }

    @Override
    public Configuration configuration() {
        return GlobalSettings$class.configuration(this);
    }

    @Override
    public Configuration onLoadConfig(Configuration config, File path, ClassLoader classloader, Enumeration.Value mode) {
        return GlobalSettings$class.onLoadConfig(this, config, path, classloader, mode);
    }

    @Override
    public Tuple2<RequestHeader, Handler> onRequestReceived(RequestHeader request) {
        return GlobalSettings$class.onRequestReceived(this, request);
    }

    @Override
    public Function1<RequestHeader, Handler> doFilter(Function1<RequestHeader, Handler> next) {
        return GlobalSettings$class.doFilter(this, next);
    }

    @Override
    public EssentialAction doFilter(EssentialAction next) {
        return GlobalSettings$class.doFilter(this, next);
    }

    @Override
    public Option<Handler> onRouteRequest(RequestHeader request) {
        return GlobalSettings$class.onRouteRequest(this, request);
    }

    @Override
    public Future<Result> onError(RequestHeader request, Throwable ex) {
        return GlobalSettings$class.onError(this, request, ex);
    }

    @Override
    public Future<Result> onHandlerNotFound(RequestHeader request) {
        return GlobalSettings$class.onHandlerNotFound(this, request);
    }

    @Override
    public Future<Result> onBadRequest(RequestHeader request, String error) {
        return GlobalSettings$class.onBadRequest(this, request, error);
    }

    @Override
    public void onRequestCompletion(RequestHeader request) {
        GlobalSettings$class.onRequestCompletion(this, request);
    }

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass) {
        return (A) GlobalSettings$class.getControllerInstance(this, controllerClass);
    }
}
