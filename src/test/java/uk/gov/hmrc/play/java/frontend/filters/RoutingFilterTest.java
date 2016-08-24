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

package uk.gov.hmrc.play.java.frontend.filters;

import org.junit.Test;
import play.api.mvc.Result;
import play.api.test.FakeRequest;
import uk.gov.hmrc.play.java.ScalaFixtures;
import uk.gov.hmrc.play.java.frontend.bootstrap.ShowErrorPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RoutingFilterTest extends ScalaFixtures {
    @Test
    public void blockUrlsMatchingTheGivenRegex() {
        FakeRequest request = FakeRequest.apply("GET", "/blocked/path");
        RoutingFilter.init(rh -> new ShowErrorPage().onHandlerNotFound(rh), "/blocked/.*");
        Result result = await(new RoutingFilter().apply(generateActionWithOkResponse()).apply(request));
        assertThat(result.header().status(), is(404));
    }

    @Test
    public void allowUrlsNOTMatchingTheGivenRegex() {
        FakeRequest request = FakeRequest.apply("GET", "/unblocked/path");
        RoutingFilter.init(rh -> new ShowErrorPage().onHandlerNotFound(rh), "/blocked/.*");
        Result result = await(new RoutingFilter().apply(generateActionWithOkResponse()).apply(request));
        assertThat(result.header().status(), is(200));
    }

    @Test
    public void allowUrlsIfNoRegex() {
        FakeRequest request = FakeRequest.apply("GET", "/blocked/path");
        RoutingFilter.init(rh -> new ShowErrorPage().onHandlerNotFound(rh), null);
        Result result = await(new RoutingFilter().apply(generateActionWithOkResponse()).apply(request));
        assertThat(result.header().status(), is(200));

        request = FakeRequest.apply("GET", "/unblocked/path");
        result = await(new RoutingFilter().apply(generateActionWithOkResponse()).apply(request));
        assertThat(result.header().status(), is(200));
    }
}
