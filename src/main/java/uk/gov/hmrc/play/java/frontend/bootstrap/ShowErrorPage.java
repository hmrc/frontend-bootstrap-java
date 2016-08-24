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

import play.api.mvc.Request;
import play.api.mvc.RequestHeader;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;
import scala.compat.java8.JFunction1;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import uk.gov.hmrc.play.frontend.bootstrap.ShowErrorPage$class;

public class ShowErrorPage extends JavaGlobalSettings implements uk.gov.hmrc.play.frontend.bootstrap.ShowErrorPage {

    private RequestHeader getCurrentRequestHeader() {
        return Http.Context.current()._requestHeader();
    }

    private F.Promise<Result> wrapAndReturn(Future<play.api.mvc.Result> result) {
        JFunction1<play.api.mvc.Result, Result> resultConverter = scalaResult -> (Result) () -> scalaResult;
        ExecutionContext ec = play.api.libs.concurrent.Execution.defaultContext();
        return F.Promise.wrap(result.map(resultConverter, ec));
    }

    public F.Promise<Result> onBadRequest(Http.RequestHeader rh, String error) {
        Future<play.api.mvc.Result> result = onBadRequest(getCurrentRequestHeader(), error);
        return wrapAndReturn(result);
    }

    public F.Promise<Result> onHandlerNotFound(Http.RequestHeader rh) {
        Future<play.api.mvc.Result> result = onHandlerNotFound(getCurrentRequestHeader());
        return wrapAndReturn(result);
    }

    public F.Promise<Result> onError(Http.RequestHeader rh, Throwable t) {
        Future<play.api.mvc.Result> result = onError(getCurrentRequestHeader(), t);
        return wrapAndReturn(result);
    }

    @Override
    public Html standardErrorTemplate(String pageTitle, String heading, String message, Request<?> request) {
        return views.html.global_error.render(pageTitle, heading, message);
    }

    @Override
    public Html badRequestTemplate(Request<?> request) {
        return ShowErrorPage$class.badRequestTemplate(this, request);
    }

    @Override
    public Html notFoundTemplate(Request<?> request) {
        return ShowErrorPage$class.notFoundTemplate(this, request);
    }

    @Override
    public Html internalServerErrorTemplate(Request<?> request) {
        return ShowErrorPage$class.internalServerErrorTemplate(this, request);
    }

    @Override
    public play.api.mvc.Result resolveError(RequestHeader rh, Throwable ex) {
        return ShowErrorPage$class.resolveError(this, rh, ex);
    }

    @Override
    public Future<play.api.mvc.Result> onError(RequestHeader request, Throwable ex) {
        return ShowErrorPage$class.onError(this, request, ex);
    }

    @Override
    public Future<play.api.mvc.Result> onHandlerNotFound(RequestHeader request) {
        return ShowErrorPage$class.onHandlerNotFound(this, request);
    }

    @Override
    public Future<play.api.mvc.Result> onBadRequest(RequestHeader request, String error) {
        return ShowErrorPage$class.onBadRequest(this, request, error);
    }
}
