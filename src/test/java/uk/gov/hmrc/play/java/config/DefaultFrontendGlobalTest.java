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

package uk.gov.hmrc.play.java.config;

import org.junit.Before;
import org.junit.Test;
import play.api.mvc.EssentialFilter;
import play.i18n.Messages;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import uk.gov.hmrc.play.java.frontend.bootstrap.DefaultFrontendGlobal;
import uk.gov.hmrc.play.java.frontend.filters.WhitelistFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.*;

public class DefaultFrontendGlobalTest extends DefaultFrontendGlobal {
    private Http.RequestHeader rh = mock(Http.RequestHeader.class);
    private int futureTimeout = 3000;

    @Before
    public void setUp() {
        Map<String, String> flashData = Collections.emptyMap();
        Map<String, Object> argData = Collections.emptyMap();
        Long id = 2L;
        play.api.mvc.RequestHeader header = mock(play.api.mvc.RequestHeader.class);
        Http.Request request = mock(Http.Request.class);
        Http.Context ctx = new Http.Context(id, header, request, flashData, flashData, argData);
        Http.Context.current.set(ctx);
    }

    @Test
    public void renderInternalServerError() {
        running(Helpers.fakeApplication(this), () -> {
            Exception exception = new Exception("Runtime exception");
            Result result = onError(rh, exception).get(futureTimeout);
            assertThat(status(result), is(INTERNAL_SERVER_ERROR));
            assertThat(contentAsString(result), containsString("Sorry, we’re experiencing technical difficulties"));
            assertThat(contentAsString(result), containsString("Please try again in a few minutes"));
        });
    }

    @Test
    public void renderPageNotFound() {
        running(Helpers.fakeApplication(this), () -> {
            Result result = onHandlerNotFound(rh).get(futureTimeout);
            assertThat(status(result), is(NOT_FOUND));
            assertThat(contentAsString(result), containsString("This page can’t be found"));
            assertThat(contentAsString(result), containsString("Please check that you have entered the correct web address"));
        });
    }

    @Test
    public void renderBadRequest() {
        running(Helpers.fakeApplication(this), () -> {
            Result result = onBadRequest(rh, "").get(futureTimeout);
            assertThat(status(result), is(BAD_REQUEST));
            assertThat(contentAsString(result), containsString("Bad request"));
            assertThat(contentAsString(result), containsString("Please check that you have entered the correct web address"));
        });
    }

    @Test
    public void renderNotFoundWithServer() {
        DefaultFrontendGlobal minusWhitelist = new DefaultFrontendGlobal() {
            @Override
            public <T extends EssentialFilter> Class<T>[] filters() {
                return (Class[]) Arrays.stream(super.filters()).filter(f -> !f.isAssignableFrom(WhitelistFilter.class)).toArray(size -> new Class[size]);
            }
        };

        running(testServer(3333, Helpers.fakeApplication(minusWhitelist)), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/notFound").pageSource();
            assertThat(browser.pageSource(), containsString(Messages.get("global.error.pageNotFound404.title")));
        });
    }
}
